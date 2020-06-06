package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.database.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class PointOfInterestDataRepository {

    private final PointOfInterestDao pointOfInterestDao;

    public PointOfInterestDataRepository(PointOfInterestDao pointOfInterestDao) {
        this.pointOfInterestDao = pointOfInterestDao;
    }

    public LiveData<List<PointOfInterest>> getPointOfInterest(){
        return pointOfInterestDao.getPointOfInterest();
    }

    public Single<List<PointOfInterest>> getSinglePointOfInterest(){
        return pointOfInterestDao.getSinglePointOfInterest();
    }

    public List<PointOfInterest> getListPointOfInterest(){
        return pointOfInterestDao.getPointOfInterestList();
    }

    public long insertPointOfInterest(PointOfInterest pointOfInterest){
        return pointOfInterestDao.insertPointOfInterest(pointOfInterest);
    }

    public PointOfInterest getPointOfInterestFromId(long idPointOfInterest){
        return  pointOfInterestDao.getPointOfInterestFromId(idPointOfInterest);
    }

    public Completable deleteListPointOfInterest(List<PointOfInterest> listPointOfInterest){
        return pointOfInterestDao.deleteListPointOfInterest(listPointOfInterest);
    }
}
