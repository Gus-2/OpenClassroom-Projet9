package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.HousePointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;

import java.util.List;

public class HousePointOfInterestDataRepository {

    private final HousePointOfInterestDao housePointOfInterestDao;

    public HousePointOfInterestDataRepository(HousePointOfInterestDao housePointOfInterestDao) {
        this.housePointOfInterestDao = housePointOfInterestDao;
    }

    public LiveData<List<HousePointOfInterest>> getHousePointOfInterest(){
        return housePointOfInterestDao.getHousePointOfInterest();
    }

    public long insertHousePointOfInterest(HousePointOfInterest housePointOfInterest){
         return housePointOfInterestDao.insertHousePointOfInterest(housePointOfInterest);
    }

    public List<HousePointOfInterest> getHousePointOfInterestFromHouseId(long idHouse){
        return housePointOfInterestDao.getHousePointOfInterestFromHouseId(idHouse);
    }
}
