package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TypePointOfInterestDao {

    @Query("SELECT *  FROM type_point_of_interest")
    LiveData<List<TypePointOfInterest>> getTypePointOfInterest();

    @Query("SELECT *  FROM type_point_of_interest")
    List<TypePointOfInterest> getListTypePointOfInterest();

    @Query("SELECT *  FROM type_point_of_interest")
    Cursor getTypePointOfInterestWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTypePointOfInterest(TypePointOfInterest type);

    @Query("SELECT id_type_point_of_interest FROM type_point_of_interest WHERE type_point_of_interest = :nameTypePointOfInterest")
    Integer getIdPointOfInterest(String nameTypePointOfInterest);

    @Query("SELECT * FROM type_point_of_interest WHERE id_type_point_of_interest = :idTypePointOfInterest")
    TypePointOfInterest getTypePointOfInterestFromId(long idTypePointOfInterest);

    @Query("SELECT * FROM type_point_of_interest WHERE id_type_point_of_interest = :idTypePointOfInterest")
    Cursor getTypePointOfInterestFromIdWithCursor(long idTypePointOfInterest);

    @Query("SELECT * FROM type_point_of_interest")
    Single<List<TypePointOfInterest>> getSingleTypePointOfInterestList();
}
