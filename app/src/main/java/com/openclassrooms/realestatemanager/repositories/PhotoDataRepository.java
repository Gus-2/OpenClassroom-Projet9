package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.List;

import io.reactivex.Flowable;

public class PhotoDataRepository {

    private final PhotoDao photoDao;

    public PhotoDataRepository(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    public List<Photo> getPhotos(){
        return photoDao.getPhotos();
    }

    public long insertPhoto(Photo photo){
        return photoDao.insertPhotos(photo);
    }

    public List<Photo> getPhotoFromIdHouse(long idHouse){
        return photoDao.getPhotoFromIdHouse(idHouse);
    }

    public LiveData<List<Photo>> getLiveDataPhotoFromIdHouse(long idHouse){
        return photoDao.getLiveDataPhotoFromIdHouse(idHouse);
    }

    public Flowable<List<Photo>> getFlowablePhotoFromIdHouse(long idHouse){
        return photoDao.getFlowablePhotoFromIdHouse(idHouse);
    }
}
