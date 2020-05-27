package com.openclassrooms.realestatemanager.repositories;

import android.os.Environment;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;
import com.openclassrooms.realestatemanager.networking.JsonPlaceHolderApi;
import com.openclassrooms.realestatemanager.networking.RetrofitService;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.functions.Func1;

public class CoordinatesRepository {

    private static CoordinatesRepository coordinatesRepository;

    public static CoordinatesRepository getInstance(){
        if (coordinatesRepository == null){
            coordinatesRepository = new CoordinatesRepository();
        }
        return coordinatesRepository;
    }

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private CoordinatesRepository(){
        jsonPlaceHolderApi = RetrofitService.getInstance().getJsonPlaceHolderApi();
    }

    public Observable<Coordinates> getCoordinates(String address){
        return RetrofitService.getInstance().getJsonPlaceHolderApi().getCoordinates(address, BuildConfig.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10, TimeUnit.SECONDS);
    }
}
