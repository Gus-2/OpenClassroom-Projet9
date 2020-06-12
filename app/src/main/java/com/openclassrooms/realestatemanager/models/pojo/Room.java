package com.openclassrooms.realestatemanager.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rooms")
public class Room implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_room")
    private long idRoom;

    @NonNull
    @ColumnInfo(name = "room_type")
    private String roomType;

    public Room(@NonNull String roomType) {
        this.roomType = roomType;
    }

    protected Room(Parcel in) {
        idRoom = in.readLong();
        roomType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idRoom);
        dest.writeString(roomType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
    @NonNull
    public String getRoomType() {
        return roomType;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }
}
