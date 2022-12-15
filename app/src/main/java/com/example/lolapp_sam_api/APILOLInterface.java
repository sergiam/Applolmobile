package com.example.lolapp_sam_api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APILOLInterface {
    //definir parametros
    @GET("v1/es_es/champion-browse/index.json")
    Call<LoreResults> GetLoreResults();
}
