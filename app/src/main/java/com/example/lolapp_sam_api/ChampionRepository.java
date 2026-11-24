package com.example.lolapp_sam_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class ChampionRepository {
    public interface Result<T> {
        void onSuccess(T value);
        void onError(Throwable error);
    }

    private final APILOLInterface api = APIConnection.getClient().create(APILOLInterface.class);

    public Call<ResponseBody> loadSlugs(Result<List<String>> result) {
        Call<ResponseBody> call = api.getChampionSlugs();
        call.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    result.onError(new IOException("Respuesta HTTP " + response.code()));
                    return;
                }
                try (ResponseBody body = response.body()) {
                    JSONArray entries = new JSONObject(body.string()).getJSONArray("champions");
                    List<String> slugs = new ArrayList<>();
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject entry = entries.optJSONObject(i);
                        String slug = entry == null ? "" : entry.optString("slug", "");
                        if (!slug.trim().isEmpty()) slugs.add(slug);
                    }
                    result.onSuccess(slugs);
                } catch (Exception error) {
                    result.onError(error);
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable error) {
                if (!call.isCanceled()) result.onError(error);
            }
        });
        return call;
    }

    public Call<ResponseBody> loadChampion(String slug, Result<Champion> result) {
        Call<ResponseBody> call = api.getChampion(slug);
        call.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    result.onError(new IOException("Respuesta HTTP " + response.code()));
                    return;
                }
                try (ResponseBody body = response.body()) {
                    result.onSuccess(parseChampion(new JSONObject(body.string())));
                } catch (Exception error) {
                    result.onError(error);
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable error) {
                if (!call.isCanceled()) result.onError(error);
            }
        });
        return call;
    }

    private Champion parseChampion(JSONObject root) throws JSONException {
        JSONObject data = root.optJSONObject("champion");
        if (data == null) throw new JSONException("Falta el objeto champion");
        Champion champion = new Champion();
        champion.setId(root.optString("id", ""));
        champion.setName(root.optString("name", ""));
        JSONObject image = data.optJSONObject("image");
        champion.setImage(image == null ? null : image.optString("uri", null));
        champion.setFaction(data.optString("associated-faction-slug", "unaffiliated"));
        champion.setReleasedate(data.optString("release-date", null));
        JSONArray races = data.optJSONArray("races");
        JSONObject race = races == null || races.length() == 0 ? null : races.optJSONObject(0);
        champion.setRace(race == null ? "Desconocido" : race.optString("name", "Desconocido"));
        champion.setTitle(data.optString("title", ""));
        JSONArray roles = data.optJSONArray("roles");
        JSONObject role = roles == null || roles.length() == 0 ? null : roles.optJSONObject(0);
        champion.setRol(role == null ? "Desconocido" : role.optString("name", "Desconocido"));
        JSONObject bio = data.optJSONObject("biography");
        if (bio != null) {
            champion.setQuote(bio.optString("quote", ""));
            champion.setBiography(bio.optString("short", ""));
        }
        return champion;
    }
}
