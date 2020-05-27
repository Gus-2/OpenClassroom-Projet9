package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;

import java.util.List;

@Dao
public interface HousePointOfInterestDao {

    @Query("SELECT *  FROM house_point_of_interest")
    LiveData<List<HousePointOfInterest>> getHousePointOfInterest();

    @Query("SELECT *  FROM house_point_of_interest")
    Cursor getHousePointOfInterestWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHousePointOfInterest(HousePointOfInterest housePointOfInterest);

    @Query("SELECT * FROM house_point_of_interest WHERE id_house = :idHouse")
    List<HousePointOfInterest> getHousePointOfInterestFromHouseId(long idHouse);

    @Query("SELECT * FROM house_point_of_interest WHERE id_house = :idHouse")
    Cursor getHousePointOfInterestFromHouseIdWithCursor(long idHouse);

    @Query("SELECT * FROM house_point_of_interest WHERE id_house = :idHouse")
    LiveData<List<HousePointOfInterest>> getLiveDataHousePointOfInterestFromHouseId(long idHouse);


}
