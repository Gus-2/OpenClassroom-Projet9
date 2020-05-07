package com.openclassrooms.realestatemanager.models.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rooms")
public class Room {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_room")
    private long idRoom;

    @NonNull
    @ColumnInfo(name = "room_type")
    private String roomType;

    public Room(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }
}
