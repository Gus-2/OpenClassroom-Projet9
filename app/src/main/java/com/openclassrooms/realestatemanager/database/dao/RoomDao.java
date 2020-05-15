package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.util.List;

@Dao
public interface RoomDao {

    @Query("SELECT * FROM rooms")
    List<Room> getRooms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoom(Room room);
}
