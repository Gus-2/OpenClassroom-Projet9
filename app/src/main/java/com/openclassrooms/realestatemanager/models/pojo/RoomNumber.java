package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "room_number",
        primaryKeys = {"id_house", "id_room"},
        foreignKeys = {
                @ForeignKey(entity = House.class,
                        parentColumns = "id_house",
                        childColumns = "id_house"),
                @ForeignKey(entity = Room.class,
                        parentColumns = "id_room",
                        childColumns = "id_room")
        })
public class RoomNumber implements Parcelable {

    @ColumnInfo(name = "id_house")
    @NonNull
    private long idHouse;

    @ColumnInfo(name = "id_room", index = true)
    @NonNull
    private long idRoom;

    @NonNull
    private int number;

    public RoomNumber(long idHouse, long idRoom, int number) {
        this.idHouse = idHouse;
        this.idRoom = idRoom;
        this.number = number;
    }

    @Ignore
    public RoomNumber(long idRoom, int number) {
        this.idRoom = idRoom;
        this.number = number;
    }

    protected RoomNumber(Parcel in) {
        idHouse = in.readLong();
        idRoom = in.readLong();
        number = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHouse);
        dest.writeLong(idRoom);
        dest.writeInt(number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoomNumber> CREATOR = new Creator<RoomNumber>() {
        @Override
        public RoomNumber createFromParcel(Parcel in) {
            return new RoomNumber(in);
        }

        @Override
        public RoomNumber[] newArray(int size) {
            return new RoomNumber[size];
        }
    };

    public long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }
}
