package com.openclassrooms.realestatemanager.models.pojo;


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
public class HousePointOfInterest {

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
