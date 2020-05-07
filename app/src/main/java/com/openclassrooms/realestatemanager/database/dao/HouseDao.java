package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.House;

import java.util.List;

@Dao
public interface HouseDao {

    @Query("SELECT * FROM houses")
    LiveData<List<House>> getHouses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHouse(House house);
}
