package com.openclassrooms.realestatemanager.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    @Query("SELECT *  FROM points_of_interest")
    LiveData<List<PointOfInterest>> getPointOfInterest();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPointOfInterest(PointOfInterest type);

    @Query("SELECT * FROM points_of_interest WHERE id_point_of_interest = :idPointOfInterest")
    PointOfInterest getPointOfInterestFromId(long idPointOfInterest);
}
