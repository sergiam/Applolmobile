package com.example.lolapp_sam_api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Champion> champions = new ArrayList<>();
    private Adapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        getAll();
    }


    public void getAll(){
        Call<LoreResults> callLore = APIConnection.getClient("https://universe-meeps.leagueoflegends.com/").create(APILOLInterface.class).GetLoreResults();
        callLore.enqueue(new Callback<LoreResults>() {
            @Override
            public void onResponse(Call<LoreResults> call, Response<LoreResults> response) {
                if (response.isSuccessful()){
                    LoreResults lore = response.body();
                    champions = lore.getChampions();

                    adapter = new Adapter(champions, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<LoreResults> call, Throwable t) {

            }
        });
    }
}