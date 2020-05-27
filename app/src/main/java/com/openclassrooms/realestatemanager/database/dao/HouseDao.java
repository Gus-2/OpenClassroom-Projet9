package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseDateState;

import java.util.List;

@Dao
public interface HouseDao {

    @Query("SELECT * FROM houses")
    LiveData<List<House>> getHouses();

    @Query("SELECT * FROM houses")
    Cursor getHousesWithCursor();

    @Query("SELECT * FROM houses WHERE id_house = :idHouse")
    Cursor getHouseFromIdWithCursor(long idHouse);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHouse(House house);

    @Query("UPDATE houses SET sold_date = :soldDate, state = :state WHERE id_house = :idHouse")
    int updateSoldDate(long soldDate, long idHouse, String state);

    @Query("SELECT state, sold_date FROM houses WHERE id_house = :idHouse")
    LiveData<HouseDateState> getHouseSate(long idHouse);

    @Query("SELECT * FROM houses WHERE id_house = :idHouse")
    LiveData<House> getHouseFromId(long idHouse);
}
