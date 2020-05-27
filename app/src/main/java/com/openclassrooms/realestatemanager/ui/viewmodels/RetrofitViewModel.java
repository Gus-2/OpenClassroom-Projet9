package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;
import com.openclassrooms.realestatemanager.repositories.CoordinatesRepository;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitViewModel extends ViewModel {

    private final CoordinatesRepository coordinatesRepository;

    public RetrofitViewModel(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    public Observable<Coordinates> getCoordinates(String address){
        return coordinatesRepository.getCoordinates(address);
    }
}
