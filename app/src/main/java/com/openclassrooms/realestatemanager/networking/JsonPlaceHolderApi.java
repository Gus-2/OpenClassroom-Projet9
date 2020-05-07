package com.openclassrooms.realestatemanager.networking;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("json?")
    Observable<Coordinates> getCoordinates(
            @Query("address") String address,
            @Query("key") String key
            );

}
