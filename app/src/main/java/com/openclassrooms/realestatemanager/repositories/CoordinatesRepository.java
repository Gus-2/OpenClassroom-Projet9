package com.openclassrooms.realestatemanager.repositories;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;
import com.openclassrooms.realestatemanager.networking.JsonPlaceHolderApi;
import com.openclassrooms.realestatemanager.networking.RetrofitService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CoordinatesRepository {

    private static CoordinatesRepository coordinatesRepository;

    public static CoordinatesRepository getInstance(){
        if (coordinatesRepository == null){
            coordinatesRepository = new CoordinatesRepository();
        }
        return coordinatesRepository;
    }

    public Observable<Coordinates> getCoordinates(String address){
        return RetrofitService.getInstance().getJsonPlaceHolderApi().getCoordinates(address, BuildConfig.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10, TimeUnit.SECONDS);
    }
}
