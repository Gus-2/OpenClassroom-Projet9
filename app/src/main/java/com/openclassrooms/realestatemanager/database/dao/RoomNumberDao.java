package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;

import java.util.List;

@Dao
public interface RoomNumberDao {

    @Query("SELECT * FROM room_number")
    LiveData<List<RoomNumber>> getRoomNumber();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoomNumber(RoomNumber roomNumber);
}
