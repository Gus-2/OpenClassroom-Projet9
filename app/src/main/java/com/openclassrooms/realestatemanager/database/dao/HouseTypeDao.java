package com.openclassrooms.realestatemanager.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.HouseType;

import java.util.List;

@Dao
public interface HouseTypeDao {

    @Query("SELECT *  FROM house_types")
    List<HouseType> getHouseTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHouseType(HouseType housePointOfInterest);

}
