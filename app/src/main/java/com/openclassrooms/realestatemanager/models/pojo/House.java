package com.openclassrooms.realestatemanager.models.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "houses", foreignKeys = {
        @ForeignKey(entity = Address.class,
            parentColumns = "id_address",
            childColumns = "id_address"),
        @ForeignKey(entity = RealEstateAgent.class,
            parentColumns = "id_real_estate_agent",
            childColumns = "id_real_estate_agent"),
        @ForeignKey(entity = HouseType.class,
            parentColumns = "id_house_type",
            childColumns = "id_house_type")
})
public class House implements Parcelable {

    @ColumnInfo(name = "id_house")
    @PrimaryKey(autoGenerate = true)
    private long idHouse;

    @ColumnInfo(name = "id_house_type", index = true)
    @NonNull
    private long idHouseType;

    @ColumnInfo(name = "id_real_estate_agent", index = true)
    @NonNull
    private long idRealEstateAgent;

    @ColumnInfo(name = "id_address", index = true)
    @NonNull
    private long idAddress;

    @NonNull
    private double price;

    @NonNull
    private double surface;

    @NonNull
    private String description;

    @NonNull
    private String state;

    @NonNull
    @ColumnInfo(name = "available_date")
    private long availableDate;

    @ColumnInfo(name = "sold_date")
    private long soldDate;

    public House(@NonNull long idHouseType, long idRealEstateAgent, double price, double surface, @NonNull String description, @NonNull String state, long availableDate) {
        this.idHouseType = idHouseType;
        this.idRealEstateAgent = idRealEstateAgent;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.state = state;
        this.availableDate = availableDate;
    }

    protected House(Parcel in) {
        idHouse = in.readLong();
        idHouseType = in.readLong();
        idRealEstateAgent = in.readLong();
        idAddress = in.readLong();
        price = in.readDouble();
        surface = in.readDouble();
        description = in.readString();
        state = in.readString();
        availableDate = in.readLong();
        soldDate = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHouse);
        dest.writeLong(idHouseType);
        dest.writeLong(idRealEstateAgent);
        dest.writeLong(idAddress);
        dest.writeDouble(price);
        dest.writeDouble(surface);
        dest.writeString(description);
        dest.writeString(state);
        dest.writeLong(availableDate);
        dest.writeLong(soldDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<House> CREATOR = new Creator<House>() {
        @Override
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };

    public long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(long idHouse) {
        this.idHouse = idHouse;
    }

    public long getIdHouseType() {
        return idHouseType;
    }

    public void setIdHouseType(long idHouseType) {
        this.idHouseType = idHouseType;
    }

    public long getIdRealEstateAgent() {
        return idRealEstateAgent;
    }

    public void setIdRealEstateAgent(long idRealEstateAgent) {
        this.idRealEstateAgent = idRealEstateAgent;
    }

    public long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getState() {
        return state;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }

    public long getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(long availableDate) {
        this.availableDate = availableDate;
    }

    public long getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(long soldDate) {
        this.soldDate = soldDate;
    }
}
