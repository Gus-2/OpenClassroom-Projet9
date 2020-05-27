package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

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

    @Query("SELECT * FROM rooms")
    Cursor getRoomsWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoom(Room room);

    @Query("SELECT * FROM rooms WHERE id_room = :idRoom")
    Cursor getRoomFromIdWithCursor(long idRoom);
}
