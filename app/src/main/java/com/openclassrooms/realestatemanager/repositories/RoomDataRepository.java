package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RoomDao;
import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.util.List;

public class RoomDataRepository {

    private final RoomDao roomDao;

    public RoomDataRepository(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public LiveData<List<Room>> getRooms(){
        return roomDao.getRooms();
    }

    public long insertRoom(Room room){
        return roomDao.insertRoom(room);
    }
}
