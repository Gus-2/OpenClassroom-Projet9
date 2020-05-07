package com.openclassrooms.realestatemanager.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.repositories.AddressDataRepository;
import com.openclassrooms.realestatemanager.repositories.CoordinatesRepository;
import com.openclassrooms.realestatemanager.repositories.PointOfInterestDataRepository;
import com.openclassrooms.realestatemanager.repositories.HouseDataRepository;
import com.openclassrooms.realestatemanager.repositories.HousePointOfInterestDataRepository;
import com.openclassrooms.realestatemanager.repositories.HouseTypeDataRepository;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateAgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.RoomDataRepository;
import com.openclassrooms.realestatemanager.repositories.RoomNumberDataRepository;
import com.openclassrooms.realestatemanager.repositories.TypePointOfInterestDataRepository;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AddressDataRepository addressDataRepository;
    private final HouseDataRepository houseDataRepository;
    private final HousePointOfInterestDataRepository housePointOfInterestDataRepository;
    private final HouseTypeDataRepository houseTypeDataRepository;
    private final PhotoDataRepository photoDataRepository;
    private final PointOfInterestDataRepository pointOfInterestDataRepository;
    private final RealEstateAgentDataRepository realEstateAgentDataRepository;
    private final RoomNumberDataRepository roomNumberDataRepository;
    private final RoomDataRepository roomDataRepository;
    private final TypePointOfInterestDataRepository typePointOfInterestDataRepository;
    private final Executor executor;
    private final CoordinatesRepository coordinatesRepository;

    public ViewModelFactory(AddressDataRepository addressDataRepository, HouseDataRepository houseDataRepository,
                            HousePointOfInterestDataRepository housePointOfInterestDataRepository,
                            HouseTypeDataRepository houseTypeDataRepository, PhotoDataRepository photoDataRepository,
                            PointOfInterestDataRepository pointOfInterestDataRepository, RealEstateAgentDataRepository realEstateAgentDataRepository,
                            RoomNumberDataRepository roomNumberDataRepository, RoomDataRepository roomDataRepository,
                            TypePointOfInterestDataRepository typePointOfInterestDataRepository, Executor executor,
                            CoordinatesRepository coordinatesRepository) {
        this.addressDataRepository = addressDataRepository;
        this.houseDataRepository = houseDataRepository;
        this.housePointOfInterestDataRepository = housePointOfInterestDataRepository;
        this.houseTypeDataRepository = houseTypeDataRepository;
        this.photoDataRepository = photoDataRepository;
        this.pointOfInterestDataRepository = pointOfInterestDataRepository;
        this.realEstateAgentDataRepository = realEstateAgentDataRepository;
        this.roomNumberDataRepository = roomNumberDataRepository;
        this.roomDataRepository = roomDataRepository;
        this.typePointOfInterestDataRepository = typePointOfInterestDataRepository;
        this.executor = executor;
        this.coordinatesRepository = coordinatesRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            return (T) new RealEstateViewModel(addressDataRepository, houseDataRepository, housePointOfInterestDataRepository, houseTypeDataRepository, photoDataRepository, pointOfInterestDataRepository, realEstateAgentDataRepository, roomNumberDataRepository, roomDataRepository, typePointOfInterestDataRepository, executor);
        } else if (modelClass.isAssignableFrom(RetrofitViewModel.class)) {
            return (T) new RetrofitViewModel(coordinatesRepository);
        } else if(modelClass.isAssignableFrom(SharedViewModel.class)){
            return (T) new SharedViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
