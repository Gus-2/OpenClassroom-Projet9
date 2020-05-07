package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "photos",
        primaryKeys = {"id_house","num_order"},
        foreignKeys = {
                @ForeignKey(entity = House.class,
                        parentColumns = "id_house",
                        childColumns = "id_house"),
        })
public class Photo implements Parcelable {

    @ColumnInfo(name = "id_house")
    @NonNull
    private long idHouse;


    @ColumnInfo(name = "room", index = true)
    private String room;

    @ColumnInfo(name = "num_order")
    @NonNull
    private int numOrder;

    @NonNull
    @ColumnInfo(name = "is_main_picture")
    private boolean isMainPicture;

    @NonNull
    private byte[] image;



    public Photo(int numOrder, boolean isMainPicture, @NonNull byte[] image) {
        this.numOrder = numOrder;
        this.isMainPicture = isMainPicture;
        this.image = image;
    }

    protected Photo(Parcel in) {
        idHouse = in.readLong();
        room = in.readString();
        numOrder = in.readInt();
        isMainPicture = in.readByte() != 0;
        image = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHouse);
        dest.writeString(room);
        dest.writeInt(numOrder);
        dest.writeByte((byte) (isMainPicture ? 1 : 0));
        dest.writeByteArray(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }

    @NonNull
    public String getRoom() {
        return room;
    }

    public void setRoom(@NonNull String room) {
        this.room = room;
    }

    public int getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
    }

    public boolean isMainPicture() {
        return isMainPicture;
    }

    public void setMainPicture(boolean mainPicture) {
        isMainPicture = mainPicture;
    }

    @NonNull
    public byte[] getImage() {
        return image;
    }

    public void setImage(@NonNull byte[] image) {
        this.image = image;
    }
}
