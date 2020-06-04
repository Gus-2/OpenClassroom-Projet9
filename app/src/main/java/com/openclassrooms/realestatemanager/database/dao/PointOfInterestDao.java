package com.openclassrooms.realestatemanager.database.dao;


import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PointOfInterestDao {

    @Query("SELECT *  FROM points_of_interest")
    LiveData<List<PointOfInterest>> getPointOfInterest();

    @Query("SELECT *  FROM points_of_interest")
    List<PointOfInterest> getPointOfInterestList();

    @Query("SELECT *  FROM points_of_interest")
    Cursor getPointOfInterestWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPointOfInterest(PointOfInterest type);

    @Query("SELECT * FROM points_of_interest WHERE id_point_of_interest = :idPointOfInterest")
    PointOfInterest getPointOfInterestFromId(long idPointOfInterest);

    @Query("SELECT * FROM points_of_interest WHERE id_point_of_interest = :idPointOfInterest")
    Cursor getPointOfInterestFromIdWithCursor(long idPointOfInterest);

    @Query("SELECT * FROM points_of_interest")
    Single<List<PointOfInterest>> getSinglePointOfInterest();


}
