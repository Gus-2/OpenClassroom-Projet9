package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.TypePointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

import java.util.List;

public class TypePointOfInterestDataRepository {

    private final TypePointOfInterestDao typePointOfInterestDao;

    public TypePointOfInterestDataRepository(TypePointOfInterestDao typePointOfInterestDao) {
        this.typePointOfInterestDao = typePointOfInterestDao;
    }

    public LiveData<List<TypePointOfInterest>> getTypePointOfInterest(){
        return typePointOfInterestDao.getTypePointOfInterest();
    }

    public long insertTypePointOfInterest(TypePointOfInterest typePointOfInterest){
        return typePointOfInterestDao.insertTypePointOfInterest(typePointOfInterest);
    }

    public Integer getIdTypePointOfInterest(String name){
        return typePointOfInterestDao.getIdPointOfInterest(name);
    }

    public TypePointOfInterest getTypePointOfInterest(long idTypePointOfInterest){
        return  typePointOfInterestDao.getTypePointOfInterestFromId(idTypePointOfInterest);
    }
}
