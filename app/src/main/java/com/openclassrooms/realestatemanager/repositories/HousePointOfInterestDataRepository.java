package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.HousePointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class HousePointOfInterestDataRepository {

    private final HousePointOfInterestDao housePointOfInterestDao;

    public HousePointOfInterestDataRepository(HousePointOfInterestDao housePointOfInterestDao) {
        this.housePointOfInterestDao = housePointOfInterestDao;
    }

    public LiveData<List<HousePointOfInterest>> getHousePointOfInterest(){
        return housePointOfInterestDao.getHousePointOfInterest();
    }

    public List<HousePointOfInterest> getListHousePointOfInterest(){
        return housePointOfInterestDao.getListHousePointOfInterest();
    }

    public long insertHousePointOfInterest(HousePointOfInterest housePointOfInterest){
         return housePointOfInterestDao.insertHousePointOfInterest(housePointOfInterest);
    }

    public List<HousePointOfInterest> getHousePointOfInterestFromHouseId(long idHouse){
        return housePointOfInterestDao.getHousePointOfInterestFromHouseId(idHouse);
    }

    public LiveData<List<HousePointOfInterest>> getLiveDataHousePointOfInterestFromHouseId(long idHouse){
        return housePointOfInterestDao.getLiveDataHousePointOfInterestFromHouseId(idHouse);
    }

    public Flowable<List<HousePointOfInterest>> getFlowableHousePointOfInterestFromHouseId(long idHouse){
        return housePointOfInterestDao.getFlowableHousePointOfInterestFromHouseId(idHouse);
    }

    public Single<List<HousePointOfInterest>> getSingleousePointOfInterest(){
        return housePointOfInterestDao.getSingleListHousePointOfInterest();
    }


}
