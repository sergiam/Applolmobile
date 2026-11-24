package com.example.lolapp_sam_api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static List<Champion> champions = new ArrayList<>();
    private List<String> championsNames = new ArrayList<>();
    private Adapter adapter;
    private RecyclerView recyclerView;
    private Adapter.RecyclerViewOnClickListener listener;
    private Button button;
    private int champs = 0;
    private Spinner spinner;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        }
        sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        listener = new Adapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(View v, int id) {
                Intent intent = new Intent(getApplicationContext(), ChampionActivity.class);
                intent.putExtra("champ",adapter.getChampions().get(id));
                startActivity(intent);
            }
        };
        getNames();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == champions.size()-1){
                    nextChamps();

                }
            }
        });


        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.factions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getSelectedItem().toString().equals("All")) {
                    orden(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                getFirst();
                spinner.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void favorito(){
        Map<String,String>  favs= (Map<String, String>) sharedPreferences.getAll();
        List<String> ids =  favs.values().stream().collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i++) {

        }
        List<String> result = ids.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        ArrayList<Champion> champs = (ArrayList<Champion>) adapter.getChampions().stream().filter(champion -> ids.contains(champion.getName().toLowerCase(Locale.ROOT))).collect(Collectors.toList());

        adapter.setChampions(champs);
        adapter.notifyDataSetChanged();
    }
    public void loadAll(){
        for (int i = 0; i < 20; i++) {
            nextChamps();
        }
        adapter.saveChampions();
    }

    public void orden(String faction){
        loadAll();
        if (faction.equals("fav")){
            favorito();
        }
        if (faction.equals("all")){
            adapter.setChampions((List<Champion>) adapter.getChampions());
        }else {
            ArrayList<Champion> champions = (ArrayList<Champion>) adapter.getChampions().stream().filter(champion -> champion.getFaction().equals(faction)).collect(Collectors.toList());
            adapter.setChampions(champions);
        }
        adapter.notifyDataSetChanged();
    }




    public void nextChamps(){

Handler handler = new Handler();
handler.post(new Runnable() {
    @Override
    public void run() {

        int aux = champs+20;
        while (champs<aux &&  champs<championsNames.size()) {

                getChampion(championsNames.get(champs));
                champs++;
        }
    }
});



    }


    public void getFirst(){
        adapter = new Adapter(champions, getApplicationContext(),listener);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 20; champs++, i++) {

            getChampion(championsNames.get(i));

        }


    }


    public Champion getChampion(String id){
        Champion champion = new Champion();
        Call<ResponseBody> call = APIConnection.getClient("https://universe-meeps.leagueoflegends.com/").create(APILOLInterface.class).getJsno(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //JSONObject jsonObject = response.body();
                    String json = null;
                    try {
                        json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        champion.setName(jsonObject.getString("name"));
                        JSONObject campeon = new JSONObject(String.valueOf(jsonObject.getJSONObject("champion")));

                        JSONObject image = new JSONObject(String.valueOf(campeon.getJSONObject("image")));
                        champion.setImage(image.getString("uri"));

                        champion.setFaction(campeon.getString("associated-faction-slug"));
                        champion.setReleasedate(campeon.getString("release-date"));

                        if (campeon.getJSONArray("races").length()>0) {
                            champion.setRace(campeon.getJSONArray("races").getJSONObject(0).getString("name"));
                        } else {
                            champion.setRace("Desconocido");
                        }
                        champion.setTitle(campeon.getString("title"));

                        champion.setRol(campeon.getJSONArray("roles").getJSONObject(0).getString("name"));

                        JSONObject bio = new JSONObject(String.valueOf(campeon.getJSONObject("biography")));
                        champion.setQuote(bio.getString("quote"));
                        champion.setBiography(bio.getString("short"));
                        champions.add(champion);
                        adapter.getChampionsList().put(champion.getName(),champion);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e){
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();

            }
        });

        return champion;
    }




    public void getNames(){
        Call<ResponseBody> callLore = APIConnection.getClient("https://universe-meeps.leagueoflegends.com/").create(APILOLInterface.class).GetLoreResults();
        callLore.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    String json = null;
                    try {
                        json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("champions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            championsNames.add(jsonObject1.getString("slug"));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}