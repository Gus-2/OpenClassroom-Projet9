package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "points_of_interest",
        foreignKeys = @ForeignKey(entity = TypePointOfInterest.class,
                    parentColumns = "id_type_point_of_interest",
                    childColumns = "type_point_of_interest"))
public class PointOfInterest implements Parcelable {

    @ColumnInfo(name = "id_point_of_interest")
    @PrimaryKey(autoGenerate = true)
    private long idPointOfInterest;

    @NonNull
    @ColumnInfo(name = "type_point_of_interest", index = true)
    private long typePointOfInterest;

    @NonNull
    @ColumnInfo(name = "address", index = true)
    private String address;

    @NonNull
    private String name;

    public PointOfInterest(long typePointOfInterest, String address, String name) {
        this.typePointOfInterest = typePointOfInterest;
        this.address = address;
        this.name = name;
    }

    protected PointOfInterest(Parcel in) {
        idPointOfInterest = in.readLong();
        typePointOfInterest = in.readLong();
        address = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idPointOfInterest);
        dest.writeLong(typePointOfInterest);
        dest.writeString(address);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PointOfInterest> CREATOR = new Creator<PointOfInterest>() {
        @Override
        public PointOfInterest createFromParcel(Parcel in) {
            return new PointOfInterest(in);
        }

        @Override
        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };

    public long getIdPointOfInterest() {
        return idPointOfInterest;
    }

    public void setIdPointOfInterest(long idPointOfInterest) {
        this.idPointOfInterest = idPointOfInterest;
    }

    public long getTypePointOfInterest() {
        return typePointOfInterest;
    }

    public void setTypePointOfInterest(long typePointOfInterest) {
        this.typePointOfInterest = typePointOfInterest;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
