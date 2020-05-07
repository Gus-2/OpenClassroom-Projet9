package com.openclassrooms.realestatemanager.repositories;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.List;

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
}
