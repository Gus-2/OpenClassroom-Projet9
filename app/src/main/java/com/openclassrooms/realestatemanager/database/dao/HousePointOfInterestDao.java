package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.DELETE;

@Dao
public interface HousePointOfInterestDao {

    @Query("SELECT *  FROM house_point_of_interest")
    LiveData<List<HousePointOfInterest>> getHousePointOfInterest();

    @Query("SELECT *  FROM house_point_of_interest")
    List<HousePointOfInterest> getListHousePointOfInterest();

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

    @Query("SELECT * FROM house_point_of_interest WHERE id_house = :idHouse")
    Flowable<List<HousePointOfInterest>> getFlowableHousePointOfInterestFromHouseId(long idHouse);

    @Query("SELECT * FROM house_point_of_interest")
    Single<List<HousePointOfInterest>> getSingleListHousePointOfInterest();

    @Delete
    Completable deleteListHousePointOfInterest(List<HousePointOfInterest> listHousePointOfInterest);

    @Insert
    Completable insertHousePointOfInterest(List<HousePointOfInterest> listHousePointOfInterest);
}
