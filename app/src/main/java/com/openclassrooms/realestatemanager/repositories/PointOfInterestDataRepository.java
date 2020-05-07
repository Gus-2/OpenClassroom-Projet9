package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.database.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import java.util.List;

public class PointOfInterestDataRepository {

    private final PointOfInterestDao pointOfInterestDao;

    public PointOfInterestDataRepository(PointOfInterestDao pointOfInterestDao) {
        this.pointOfInterestDao = pointOfInterestDao;
    }

    public LiveData<List<PointOfInterest>> getPointOfInterest(){
        return pointOfInterestDao.getPointOfInterest();
    }

    public long insertPointOfInterest(PointOfInterest pointOfInterest){
        return pointOfInterestDao.insertPointOfInterest(pointOfInterest);
    }

    public PointOfInterest getPointOfInterestFromId(long idPointOfInterest){
        return  pointOfInterestDao.getPointOfInterestFromId(idPointOfInterest);
    }
}
