package com.openclassrooms.realestatemanager.database.dao;


import androidx.lifecycle.LiveData;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRealEstateAgent(RealEstateAgent realEstateAgent);

}
