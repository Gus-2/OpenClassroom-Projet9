package com.openclassrooms.realestatemanager.database.dao;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;

import java.util.List;

@Dao
public interface RealEstateAgentDao {

    @Query("SELECT *  FROM real_estate_agents")
    List<RealEstateAgent> getRealEstateAgent();

    @Query("SELECT *  FROM real_estate_agents")
    Cursor getRealEstateAgentWithCursor();

    @Query("SELECT *  FROM real_estate_agents WHERE id_real_estate_agent = :idRealEstateAgent")
    Cursor getRealEstateAgentFromIdWithCursor(long idRealEstateAgent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRealEstateAgent(RealEstateAgent realEstateAgent);

}
