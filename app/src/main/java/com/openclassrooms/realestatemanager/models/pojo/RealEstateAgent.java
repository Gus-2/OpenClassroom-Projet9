package com.openclassrooms.realestatemanager.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "real_estate_agents")
public class RealEstateAgent implements Parcelable {

    @ColumnInfo(name = "id_real_estate_agent")
    @PrimaryKey(autoGenerate = true)
    private long idRealEstateAgent;

    @NonNull
    private String name;

    @NonNull
    private String firstname;


    public RealEstateAgent(@NonNull String name,@NonNull String firstname) {
        this.name = name;
        this.firstname = firstname;
    }

    protected RealEstateAgent(Parcel in) {
        idRealEstateAgent = in.readLong();
        name = in.readString();
        firstname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idRealEstateAgent);
        dest.writeString(name);
        dest.writeString(firstname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RealEstateAgent> CREATOR = new Creator<RealEstateAgent>() {
        @Override
        public RealEstateAgent createFromParcel(Parcel in) {
            return new RealEstateAgent(in);
        }

        @Override
        public RealEstateAgent[] newArray(int size) {
            return new RealEstateAgent[size];
        }
    };
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
    @NonNull
    public String getFirstname() {
        return firstname;
    }

    public long getIdRealEstateAgent() {
        return idRealEstateAgent;
    }

    public void setIdRealEstateAgent(long idRealEstateAgent) {
        this.idRealEstateAgent = idRealEstateAgent;
    }
}
