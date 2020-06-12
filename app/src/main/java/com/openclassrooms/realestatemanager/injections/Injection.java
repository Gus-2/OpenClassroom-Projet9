package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static AddressDataRepository provideAddressDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new AddressDataRepository(realEstateDatabase.addressDao());
    }

    private static PointOfInterestDataRepository provideDataPointOfInterestRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new PointOfInterestDataRepository(realEstateDatabase.pointOfInterestDao());
    }

    private static HouseDataRepository provideHouseDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new HouseDataRepository(realEstateDatabase.houseDao());
    }

    private static HousePointOfInterestDataRepository provideHousePointOfInterestDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new HousePointOfInterestDataRepository(realEstateDatabase.housePointOfInterestDao());
    }

    private static HouseTypeDataRepository provideHouseTypeRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new HouseTypeDataRepository(realEstateDatabase.houseTypeDao());
    }

    private static PhotoDataRepository providePhotoDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new PhotoDataRepository(realEstateDatabase.photoDao());
    }

    private static RealEstateAgentDataRepository provideRealEstateAgentDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RealEstateAgentDataRepository(realEstateDatabase.realEstateAgentDao());
    }

    private static RoomDataRepository provideRoomDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RoomDataRepository(realEstateDatabase.roomDao());
    }

    private static RoomNumberDataRepository provideRoomNumberDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RoomNumberDataRepository(realEstateDatabase.roomNumberDao());
    }

    private static TypePointOfInterestDataRepository provideTypePointOfInterestDataRepository(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new TypePointOfInterestDataRepository(realEstateDatabase.typePointOfInterestDao());
    }

    private static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    private static CoordinatesRepository provideCoordinatesRepository(){
        return CoordinatesRepository.getInstance();
    }

    public static ViewModelFactory provideDaoViewModelFactory(Context context) {
        AddressDataRepository addressDataRepository = provideAddressDataRepository(context);
        PointOfInterestDataRepository pointOfInterestDataRepository = provideDataPointOfInterestRepository(context);
        HouseDataRepository houseDataRepository = provideHouseDataRepository(context);
        HousePointOfInterestDataRepository housePointOfInterestDataRepository = provideHousePointOfInterestDataRepository(context);
        HouseTypeDataRepository houseTypeDataRepository = provideHouseTypeRepository(context);
        PhotoDataRepository photoDataRepository = providePhotoDataRepository(context);
        RealEstateAgentDataRepository realEstateAgentDataRepository = provideRealEstateAgentDataRepository(context);
        RoomDataRepository roomDataRepository = provideRoomDataRepository(context);
        RoomNumberDataRepository roomNumberDataRepository = provideRoomNumberDataRepository(context);
        TypePointOfInterestDataRepository typePointOfInterestDataRepository = provideTypePointOfInterestDataRepository(context);
        Executor executor = provideExecutor();
        CoordinatesRepository coordinatesRepository = provideCoordinatesRepository();
        return new ViewModelFactory(addressDataRepository, houseDataRepository, housePointOfInterestDataRepository, houseTypeDataRepository, photoDataRepository, pointOfInterestDataRepository, realEstateAgentDataRepository, roomNumberDataRepository, roomDataRepository, typePointOfInterestDataRepository,
                executor, coordinatesRepository);
    }
}