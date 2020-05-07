package com.openclassrooms.realestatemanager.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos")
    List<Photo> getPhotos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPhotos(Photo photo);
}
