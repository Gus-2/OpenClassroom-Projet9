package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

import java.util.List;

@Dao
public interface TypePointOfInterestDao {

    @Query("SELECT *  FROM type_point_of_interest")
    LiveData<List<TypePointOfInterest>> getTypePointOfInterest();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTypePointOfInterest(TypePointOfInterest type);

    @Query("SELECT id_type_point_of_interest FROM type_point_of_interest WHERE type_point_of_interest = :nameTypePointOfInterest")
    Integer getIdPointOfInterest(String nameTypePointOfInterest);

    @Query("SELECT * FROM type_point_of_interest WHERE id_type_point_of_interest = :idTypePointOfInterest")
    TypePointOfInterest getTypePointOfInterestFromId(long idTypePointOfInterest);

}
