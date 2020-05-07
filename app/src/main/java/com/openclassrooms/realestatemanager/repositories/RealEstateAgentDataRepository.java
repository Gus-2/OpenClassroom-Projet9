package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateAgentDao;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;

import java.util.List;

public class RealEstateAgentDataRepository {

    private final RealEstateAgentDao realEstateAgentDao;

    public RealEstateAgentDataRepository(RealEstateAgentDao realEstateAgentDao) {
        this.realEstateAgentDao = realEstateAgentDao;
    }

    public List<RealEstateAgent> getRealEstateAgent(){
        return realEstateAgentDao.getRealEstateAgent();
    }

    public long insertRealEstateAgent(RealEstateAgent realEstateAgent){
        return realEstateAgentDao.insertRealEstateAgent(realEstateAgent);
    }
}
