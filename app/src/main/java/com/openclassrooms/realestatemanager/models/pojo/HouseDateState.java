package com.openclassrooms.realestatemanager.models.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class HouseDateState {

    @ColumnInfo(name = "sold_date")
    private long soldDate;

    @NonNull
    private String state;

    public HouseDateState(long soldDate, @NonNull String state) {
        this.soldDate = soldDate;
        this.state = state;
    }

    public long getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(long soldDate) {
        this.soldDate = soldDate;
    }

    @NonNull
    public String getState() {
        return state;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }
}
