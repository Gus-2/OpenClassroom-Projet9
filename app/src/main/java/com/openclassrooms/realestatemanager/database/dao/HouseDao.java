package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseDateState;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;

import java.util.List;

@Dao
public interface HouseDao {

    @Query("SELECT * FROM houses")
    LiveData<List<House>> getHouses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHouse(House house);

    @Query("UPDATE houses SET sold_date = :soldDate, state = :state WHERE id_house = :idHouse")
    int updateSoldDate(long soldDate, long idHouse, String state);

    @Query("SELECT state, sold_date FROM houses WHERE id_house = :idHouse")
    LiveData<HouseDateState> getHouseSate(long idHouse);
}
