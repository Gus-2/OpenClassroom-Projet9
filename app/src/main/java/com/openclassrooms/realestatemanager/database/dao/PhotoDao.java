package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos")
    List<Photo> getPhotos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPhotos(Photo photo);

    @Query("SELECT * FROM photos WHERE id_house = :idHouse")
    List<Photo> getPhotoFromIdHouse(long idHouse);

    @Query("SELECT * FROM photos WHERE id_house = :idHouse")
    LiveData<List<Photo>> getLiveDataPhotoFromIdHouse(long idHouse);

    @Query("SELECT * FROM photos WHERE id_house = :idHouse")
    Flowable<List<Photo>> getFlowablePhotoFromIdHouse(long idHouse);

    @Delete
    Completable deleteListPhoto(List<Photo> listPhotoToDelete);

    @Insert
    Completable insertListPhoto(List<Photo> listPhotoToInsert);

    @Update
    Completable updateListPhoto(List<Photo> listPhotoToUpdate);

    @Query("SELECT * FROM photos")
    Flowable<List<Photo>> getFlowablePhoto();

}
