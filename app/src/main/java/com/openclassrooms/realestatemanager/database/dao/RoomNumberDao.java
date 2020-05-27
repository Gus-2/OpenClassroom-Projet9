package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

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

    @Query("SELECT * FROM room_number")
    Cursor getRoomNumberWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoomNumber(RoomNumber roomNumber);

    @Query("SELECT * FROM room_number WHERE id_house = :idHouse")
    List<RoomNumber> getRoomNumberForHouse(long idHouse);

    @Query("SELECT * FROM room_number WHERE id_house = :idHouse")
    Cursor getRoomNumberForIdHouseWithCursor(long idHouse);

    @Query("SELECT * FROM room_number WHERE id_house = :idHouse")
    LiveData<List<RoomNumber>> getLiveDataRoomNumberForHouse(long idHouse);

}
