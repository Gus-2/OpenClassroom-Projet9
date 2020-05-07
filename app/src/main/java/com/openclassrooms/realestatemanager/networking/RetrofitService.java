package com.openclassrooms.realestatemanager.networking;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService instance = null;
    private static JsonPlaceHolderApi jsonPlaceHolderApi;

    private RetrofitService() {
    }

    private static class SingletonHolder {
        private final static RetrofitService instance = new RetrofitService();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static RetrofitService getInstance() {
        if (instance == null) {
            instance = SingletonHolder.instance;
            Retrofit retrofitDetailsPlaces = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


            jsonPlaceHolderApi = retrofitDetailsPlaces.create(JsonPlaceHolderApi.class);
        }

        return instance;
    }

    public JsonPlaceHolderApi getJsonPlaceHolderApi() {
        return jsonPlaceHolderApi;
    }
}