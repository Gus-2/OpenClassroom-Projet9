package com.openclassrooms.realestatemanager.networking;

import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("json?")
    Observable<Coordinates> getCoordinates(
            @Query("address") String address,
            @Query("key") String key
            );

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadMapPicture(@Url String fileUrl);

}
