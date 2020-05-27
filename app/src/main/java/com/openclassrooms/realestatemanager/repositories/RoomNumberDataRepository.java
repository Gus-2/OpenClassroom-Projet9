package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RoomNumberDao;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;

import java.util.List;

public class RoomNumberDataRepository {

    private final RoomNumberDao roomNumberDao;

    public RoomNumberDataRepository(RoomNumberDao roomNumberDao) {
        this.roomNumberDao = roomNumberDao;
    }

    public LiveData<List<RoomNumber>> getRoomNumber(){
        return roomNumberDao.getRoomNumber();
    }

    public long insertRoomNumber(RoomNumber roomNumber){
        return roomNumberDao.insertRoomNumber(roomNumber);
    }

    public List<RoomNumber> getRoomForHouse(long idHouse){
        return roomNumberDao.getRoomNumberForHouse(idHouse);
    }

    public LiveData<List<RoomNumber>> getLiveDataRoomNumberForHouse(long idHouse){
        return roomNumberDao.getLiveDataRoomNumberForHouse(idHouse);
    }
}
