package com.openclassrooms.realestatemanager.database.dao;


import android.database.Cursor;

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

    @Query("SELECT *  FROM house_types")
    Cursor getHouseTypesWithCursor();

    @Query("SELECT * FROM house_types WHERE id_house_type = :idHouseType")
    HouseType getHouseTypeFromId(long idHouseType);

    @Query("SELECT * FROM house_types WHERE id_house_type = :idHouseType")
    Cursor getHouseTypeFromIdWithCursor(long idHouseType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHouseType(HouseType housePointOfInterest);

}
