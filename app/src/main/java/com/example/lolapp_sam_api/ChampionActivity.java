package com.example.lolapp_sam_api;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class ChampionActivity extends AppCompatActivity {
    private Champion champion;
    private SharedPreferences preferences;
    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.champion_page);

        champion = getIntent().getParcelableExtra("champ");
        if (champion == null) {
            Toast.makeText(this, getString(R.string.champion_open_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView name = findViewById(R.id.championNamePage);
        ImageView image = findViewById(R.id.championImagePage);
        TextView quote = findViewById(R.id.championQuotePage);
        TextView releaseDate = findViewById(R.id.championReleaseDatePage);
        TextView faction = findViewById(R.id.championFactionPage);
        TextView race = findViewById(R.id.championRacePage);
        TextView role = findViewById(R.id.championRolPage);
        TextView title = findViewById(R.id.championTitlePage);
        TextView biography = findViewById(R.id.championDescriptionPage);
        favoriteButton = findViewById(R.id.championButtonFav);

        name.setText(champion.getName());
        quote.setText(champion.getQuote());
        releaseDate.setText(getString(R.string.champion_release_date, champion.getReleasedate()));
        faction.setText(getString(R.string.champion_faction, champion.getFaction()));
        race.setText(getString(R.string.champion_race, champion.getRace()));
        role.setText(getString(R.string.champion_role, champion.getRol()));
        title.setText(getString(R.string.champion_title, champion.getTitle()));
        biography.setText(champion.getBiography());
        Glide.with(this).load(champion.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(image);

        preferences = getSharedPreferences("Data", MODE_PRIVATE);
        updateFavoriteButton();
        favoriteButton.setOnClickListener(view -> toggleFavorite());
    }

    private String favoriteKey() {
        return champion.getName().toLowerCase(Locale.ROOT);
    }

    private void updateFavoriteButton() {
        boolean favorite = preferences.contains(favoriteKey());
        favoriteButton.setSelected(favorite);
        favoriteButton.setImageResource(favorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        favoriteButton.setContentDescription(getString(favorite ? R.string.favorite_remove_description : R.string.favorite_add_description));
    }

    private void toggleFavorite() {
        String key = favoriteKey();
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains(key)) {
            editor.remove(key);
            Toast.makeText(this, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
        } else {
            editor.putBoolean(key, true);
            Toast.makeText(this, getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
        }
        editor.apply();
        updateFavoriteButton();
    }
}
