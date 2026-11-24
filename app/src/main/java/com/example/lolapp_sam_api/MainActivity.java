package com.example.lolapp_sam_api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private static final int PAGE_SIZE = 20;

    private final List<Champion> allChampions = new ArrayList<>();
    private final List<String> championSlugs = new ArrayList<>();
    private final List<Call<ResponseBody>> activeCalls = new ArrayList<>();
    private ChampionRepository repository;
    private Adapter adapter;
    private RecyclerView recyclerView;
    private Button startButton;
    private Spinner spinner;
    private SharedPreferences preferences;
    private int nextSlug;
    private boolean batchLoading;
    private boolean started;
    private boolean namesLoading;
    private String selectedFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new ChampionRepository();
        preferences = getSharedPreferences("Data", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recycler);
        int columns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        adapter = new Adapter(allChampions, (view, position) -> {
            Champion selected = adapter.getChampion(position);
            if (selected == null) return;
            Intent intent = new Intent(this, ChampionActivity.class);
            intent.putExtra("champ", selected);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.factions, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilter = parent.getItemAtPosition(position).toString();
                refreshDisplayedChampions();
                if (started && isNearEnd()) loadNextBatch();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        startButton = findViewById(R.id.button);
        startButton.setEnabled(false);
        startButton.setOnClickListener(view -> startLoading());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
                if (dy > 0 && isNearEnd()) loadNextBatch();
            }
        });

        loadChampionSlugs();
    }

    private void loadChampionSlugs() {
        namesLoading = true;
        Call<ResponseBody> call = repository.loadSlugs(new ChampionRepository.Result<List<String>>() {
            @Override public void onSuccess(List<String> value) {
                namesLoading = false;
                championSlugs.clear();
                championSlugs.addAll(value);
                startButton.setEnabled(!championSlugs.isEmpty());
                if (championSlugs.isEmpty()) showLoadError();
            }
            @Override public void onError(Throwable error) {
                namesLoading = false;
                startButton.setEnabled(true);
                Toast.makeText(MainActivity.this, getString(R.string.champion_slugs_error), Toast.LENGTH_LONG).show();
            }
        });
        activeCalls.add(call);
    }

    private void startLoading() {
        if (namesLoading || batchLoading) return;
        if (championSlugs.isEmpty()) {
            startButton.setEnabled(false);
            loadChampionSlugs();
            return;
        }
        if (!started) {
            started = true;
            startButton.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }
        loadNextBatch();
    }

    private void loadNextBatch() {
        if (!started || batchLoading || nextSlug >= championSlugs.size()) return;
        batchLoading = true;
        int count = Math.min(PAGE_SIZE, championSlugs.size() - nextSlug);
        List<String> batchSlugs = new ArrayList<>(championSlugs.subList(nextSlug, nextSlug + count));
        nextSlug += count;
        Champion[] results = new Champion[count];
        boolean[] failed = {false};
        int[] remaining = {count};

        for (int i = 0; i < count; i++) {
            final int index = i;
            Call<ResponseBody> call = repository.loadChampion(batchSlugs.get(i), new ChampionRepository.Result<Champion>() {
                @Override public void onSuccess(Champion value) {
                    results[index] = value;
                    completeBatchItem(remaining, results, failed);
                }
                @Override public void onError(Throwable error) {
                    failed[0] = true;
                    completeBatchItem(remaining, results, failed);
                }
            });
            activeCalls.add(call);
        }
    }

    private void completeBatchItem(int[] remaining, Champion[] results, boolean[] failed) {
        if (--remaining[0] != 0 || isFinishing() || isDestroyed()) return;
        for (Champion champion : results) {
            if (champion != null) allChampions.add(champion);
        }
        batchLoading = false;
        refreshDisplayedChampions();
        if (failed[0]) Toast.makeText(this, getString(R.string.champion_batch_error), Toast.LENGTH_SHORT).show();
        if ((isNearEnd() || "fav".equalsIgnoreCase(selectedFilter)) && nextSlug < championSlugs.size()) loadNextBatch();
    }

    private boolean isNearEnd() {
        if (recyclerView == null || recyclerView.getLayoutManager() == null || adapter == null || adapter.getItemCount() == 0) return false;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (!(manager instanceof GridLayoutManager)) return false;
        int last = ((GridLayoutManager) manager).findLastVisibleItemPosition();
        return adapter.getItemCount() == 0 || last >= adapter.getItemCount() - 5;
    }

    private void refreshDisplayedChampions() {
        if (adapter == null) return;
        String filter = selectedFilter == null ? "All" : selectedFilter;
        List<Champion> displayed = new ArrayList<>();
        Set<String> favorites = preferences == null ? new HashSet<>() : preferences.getAll().keySet();
        for (Champion champion : allChampions) {
            if ("fav".equalsIgnoreCase(filter)) {
                if (favorites.contains(champion.getName().toLowerCase(Locale.ROOT))) displayed.add(champion);
            } else if ("All".equalsIgnoreCase(filter) || "all".equalsIgnoreCase(filter)
                    || filter.equalsIgnoreCase(champion.getFaction())) {
                displayed.add(champion);
            }
        }
        adapter.setChampions(displayed);
    }

    private void showLoadError() {
        Toast.makeText(this, getString(R.string.champion_list_error), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        for (Call<ResponseBody> call : activeCalls) {
            if (call != null) call.cancel();
        }
        activeCalls.clear();
        super.onDestroy();
    }
}
