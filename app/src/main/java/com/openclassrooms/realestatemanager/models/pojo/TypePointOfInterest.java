package com.openclassrooms.realestatemanager.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "type_point_of_interest")
public class TypePointOfInterest implements Parcelable {

    @ColumnInfo(name = "id_type_point_of_interest")
    @PrimaryKey(autoGenerate = true)
    private long idTypePointOfInterest;

    @ColumnInfo(name = "type_point_of_interest")
    @NonNull
    private String typePointOfInterest;

    public TypePointOfInterest(String typePointOfInterest) {
        this.typePointOfInterest = typePointOfInterest;
    }

    protected TypePointOfInterest(Parcel in) {
        idTypePointOfInterest = in.readLong();
        typePointOfInterest = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idTypePointOfInterest);
        dest.writeString(typePointOfInterest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TypePointOfInterest> CREATOR = new Creator<TypePointOfInterest>() {
        @Override
        public TypePointOfInterest createFromParcel(Parcel in) {
            return new TypePointOfInterest(in);
        }

        @Override
        public TypePointOfInterest[] newArray(int size) {
            return new TypePointOfInterest[size];
        }
    };

    public String getTypePointOfInterest() {
        return typePointOfInterest;
    }

    public void setTypePointOfInterest(String typePointOfInterest) {
        this.typePointOfInterest = typePointOfInterest;
    }

    public long getIdTypePointOfInterest() {
        return idTypePointOfInterest;
    }

    public void setIdTypePointOfInterest(long idTypePointOfInterest) {
        this.idTypePointOfInterest = idTypePointOfInterest;
    }
}
