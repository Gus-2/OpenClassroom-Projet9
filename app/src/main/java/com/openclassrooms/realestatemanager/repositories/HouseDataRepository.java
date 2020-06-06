package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.HouseDao;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseDateState;

import java.util.List;

import io.reactivex.Completable;

public class HouseDataRepository {
    private final HouseDao houseDao;

    public HouseDataRepository(HouseDao houseDao) {
        this.houseDao = houseDao;
    }

    public LiveData<List<House>> getHouses(){
        return houseDao.getHouses();
    }

    public long insertHouse(House house){
        return houseDao.insertHouse(house);
    }

    public int updateSoldDate(long soldDate, long idHouse, String state){
        return houseDao.updateSoldDate(soldDate, idHouse, state);
    }

    public LiveData<HouseDateState> getHouseState(long idHouse){
        return houseDao.getHouseSate(idHouse);
    }

    public LiveData<House> getHouseFromId(long idHouse){
        return houseDao.getHouseFromId(idHouse);
    }

    public Completable updateHouse(House house){ return  houseDao.updateHouse(house); }
}
