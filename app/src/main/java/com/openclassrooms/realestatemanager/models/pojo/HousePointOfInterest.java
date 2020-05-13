package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "house_point_of_interest",
        primaryKeys = { "id_house", "id_point_of_interest" },
        foreignKeys = {
            @ForeignKey(entity = House.class,
                    parentColumns = "id_house",
                    childColumns = "id_house"),
            @ForeignKey(entity = PointOfInterest.class,
                    parentColumns = "id_point_of_interest",
                    childColumns = "id_point_of_interest")
        })
public class HousePointOfInterest implements Parcelable {

    @NonNull
    @ColumnInfo(name = "id_house")
    private long idHouse;

    @NonNull
    @ColumnInfo(name = "id_point_of_interest", index = true)
    private long idPointOfInterest;

    public HousePointOfInterest(long idHouse, long idPointOfInterest) {
        this.idHouse = idHouse;
        this.idPointOfInterest = idPointOfInterest;
    }

    protected HousePointOfInterest(Parcel in) {
        idHouse = in.readLong();
        idPointOfInterest = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHouse);
        dest.writeLong(idPointOfInterest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HousePointOfInterest> CREATOR = new Creator<HousePointOfInterest>() {
        @Override
        public HousePointOfInterest createFromParcel(Parcel in) {
            return new HousePointOfInterest(in);
        }

        @Override
        public HousePointOfInterest[] newArray(int size) {
            return new HousePointOfInterest[size];
        }
    };

    public long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }

    public long getIdPointOfInterest() {
        return idPointOfInterest;
    }

    public void setIdPointOfInterest(long idPointOfInterest) {
        this.idPointOfInterest = idPointOfInterest;
    }
}
