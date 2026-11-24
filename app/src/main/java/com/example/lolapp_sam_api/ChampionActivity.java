package com.example.lolapp_sam_api;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class ChampionActivity extends AppCompatActivity {
    private TextView nom;
    private ImageView imagen;
    private TextView quote;
    private TextView releasedate;
    private TextView faction;
    private TextView race;
    private TextView rol;
    private TextView title;
    private TextView description;
    private ImageButton favorito;
    SharedPreferences sharedPreferences;



    private Champion champion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.champion_page);
        Bundle bundle = getIntent().getExtras();
        nom = findViewById(R.id.championNamePage);
        imagen = findViewById(R.id.championImagePage);
        quote = findViewById(R.id.championQuotePage);
        releasedate = findViewById(R.id.championReleaseDatePage);
        faction = findViewById(R.id.championFactionPage);
        race = findViewById(R.id.championRacePage);
        rol = findViewById(R.id.championRolPage);
        title = findViewById(R.id.championTitlePage);
        description = findViewById(R.id.championDescriptionPage);
        favorito = findViewById(R.id.championButtonFav);

        if (bundle != null) {
            champion = bundle.getParcelable("champ");
            Glide.with(getApplicationContext()).load(champion.getImage()).into(imagen);
            nom.setText(champion.getName());
            quote.setText(champion.getQuote());
            releasedate.setText("Release date: " + champion.getReleasedate());
            faction.setText("Faction: " + champion.getFaction());
            race.setText("Race: " + champion.getRace());
            rol.setText("Rol: " + champion.getRol());
            title.setText("Title: " + champion.getTitle());
            description.setText(champion.getBiography());
            sharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        }

            favorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(champion.getName().toLowerCase(Locale.ROOT), champion.getName().toLowerCase(Locale.ROOT));
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Marcado como favorito!",Toast.LENGTH_LONG).show();
                }
            });




    }




}
