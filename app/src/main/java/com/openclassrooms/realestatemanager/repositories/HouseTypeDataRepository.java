package com.openclassrooms.realestatemanager.repositories;

import com.openclassrooms.realestatemanager.database.dao.HouseTypeDao;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;

import java.util.List;

public class HouseTypeDataRepository {

    private final HouseTypeDao houseTypeDao;

    public HouseTypeDataRepository(HouseTypeDao houseTypeDao) {
        this.houseTypeDao = houseTypeDao;
    }

    public List<HouseType> getHouseType(){
        return houseTypeDao.getHouseTypes();
    }

    public long insertHouseType(HouseType houseType){
        return houseTypeDao.insertHouseType(houseType);
    }

    public HouseType getHouseTypeFromId(long idHouseType){
        return houseTypeDao.getHouseTypeFromId(idHouseType);
    }
}
