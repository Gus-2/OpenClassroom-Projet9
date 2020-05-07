package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.HouseDao;
import com.openclassrooms.realestatemanager.models.pojo.House;

import java.util.List;

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
}
