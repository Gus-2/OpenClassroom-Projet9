package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RoomDao;
import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.util.List;

import io.reactivex.Single;

public class RoomDataRepository {

    private final RoomDao roomDao;

    public RoomDataRepository(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<Room> getRooms(){
        return roomDao.getRooms();
    }

    public Single<List<Room>> getRoomsSingle(){
        return roomDao.getRoomsSingle();
    }

    public long insertRoom(Room room){
        return roomDao.insertRoom(room);
    }
}
