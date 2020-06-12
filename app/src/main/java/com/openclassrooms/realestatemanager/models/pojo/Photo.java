package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos",
        foreignKeys = {
                @ForeignKey(entity = House.class,
                        parentColumns = "id_house",
                        childColumns = "id_house"),
        })
public class Photo implements Parcelable {

    @ColumnInfo(name = "id_photo")
    @PrimaryKey(autoGenerate = true)
    private long idPhoto;

    @ColumnInfo(name = "id_house", index = true)
    private long idHouse;

    @ColumnInfo(name = "id_room", index = true)
    private long idRoom;

    @ColumnInfo(name = "num_order_room")
    private long numOrderRoom;

    @ColumnInfo(name = "specific_room")
    private String specificRoom;

    @ColumnInfo(name = "is_main_picture")
    private boolean isMainPicture;

    @NonNull
    private String path;

    @NonNull
    private String childPath;

    public Photo() {
        this.isMainPicture = false;
        idRoom = -1;
        numOrderRoom = -1;
    }

    protected Photo(Parcel in) {
        idPhoto = in.readLong();
        idHouse = in.readLong();
        idRoom = in.readLong();
        numOrderRoom = in.readLong();
        specificRoom = in.readString();
        isMainPicture = in.readByte() != 0;
        path = in.readString();
        childPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idPhoto);
        dest.writeLong(idHouse);
        dest.writeLong(idRoom);
        dest.writeLong(numOrderRoom);
        dest.writeString(specificRoom);
        dest.writeByte((byte) (isMainPicture ? 1 : 0));
        dest.writeString(path);
        dest.writeString(childPath);
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

    public long getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(long idPhoto) {
        this.idPhoto = idPhoto;
    }

    public long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }

    public long getNumOrderRoom() {
        return numOrderRoom;
    }

    public void setNumOrderRoom(long numOrderRoom) {
        this.numOrderRoom = numOrderRoom;
    }

    public String getSpecificRoom() {
        return specificRoom;
    }

    public void setSpecificRoom(String specificRoom) {
        this.specificRoom = specificRoom;
    }

    public boolean isMainPicture() {
        return isMainPicture;
    }

    public void setMainPicture(boolean mainPicture) {
        isMainPicture = mainPicture;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
    }

    @NonNull
    public String getChildPath() {
        return childPath;
    }

    public void setChildPath(@NonNull String childPath) {
        this.childPath = childPath;
    }
}
