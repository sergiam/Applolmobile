package com.example.lolapp_sam_api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APILOLInterface {
    //definir parametros
    @GET("v1/es_es/champions/{id}/index.json")
    Call<ResponseBody> getJsno(@Path("id") String id);
    @GET("v1/es_es/champion-browse/index.json")
    Call<ResponseBody> GetLoreResults();
    @GET("v1/es_es/champions/aatrox/index.json")
    Call<Champion> Getchampion();

}
