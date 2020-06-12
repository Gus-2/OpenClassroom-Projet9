package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "house_types")
public class HouseType implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_house_type")
    private long idHouseType;

    @NonNull
    @ColumnInfo(name = "house_type")
    private String houseType;

    public HouseType(@NotNull String houseType) {
        this.houseType = houseType;
    }

    protected HouseType(Parcel in) {
        idHouseType = in.readLong();
        houseType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHouseType);
        dest.writeString(houseType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HouseType> CREATOR = new Creator<HouseType>() {
        @Override
        public HouseType createFromParcel(Parcel in) {
            return new HouseType(in);
        }

        @Override
        public HouseType[] newArray(int size) {
            return new HouseType[size];
        }
    };

    public long getIdHouseType() {
        return idHouseType;
    }

    public void setIdHouseType(long idHouseType) {
        this.idHouseType = idHouseType;
    }

    @NonNull
    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(@NonNull String houseType) {
        this.houseType = houseType;
    }
}
