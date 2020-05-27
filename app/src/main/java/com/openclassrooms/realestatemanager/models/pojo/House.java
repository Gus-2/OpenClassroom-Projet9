package com.openclassrooms.realestatemanager.models.pojo;

import android.content.ContentValues;
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

    public static final String ID_HOUSE = "id_house";
    public static final String ID_HOUSE_TYPE = "id_house_type";
    public static final String ID_REAL_ESTATE_AGENT = "id_real_estate_agent";
    public static final String ID_ADDRESS = "id_address";
    public static final String PRICE = "price";
    public static final String SURFACE = "surface";
    public static final String DESCRIPTION = "description";
    public static final String STATE = "state";
    public static final String AVAILABLE_DATE = "available_date";
    public static final String SOLD_DATE = "sold_date";
    public static final String PARENT_PATH_PLACE_PREVIEW = "parent_path_place_preview";
    public static final String CHILD_PATH_PLACE_PREVIEW = "child_path_place_preview";

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

    private String parentPathPlacePreview;

    private String childPathPlacePreview;

    public House(@NonNull long idHouseType, long idRealEstateAgent, double price, double surface, @NonNull String description, @NonNull String state, long availableDate) {
        this.idHouseType = idHouseType;
        this.idRealEstateAgent = idRealEstateAgent;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.state = state;
        this.availableDate = availableDate;
        soldDate = -1;
    }

    public House(){}

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
        parentPathPlacePreview = in.readString();
        childPathPlacePreview = in.readString();
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
        dest.writeString(parentPathPlacePreview);
        dest.writeString(childPathPlacePreview);
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

    public String getParentPathPlacePreview() {
        return parentPathPlacePreview;
    }

    public void setParentPathPlacePreview(String parentPathPlacePreview) {
        this.parentPathPlacePreview = parentPathPlacePreview;
    }

    public String getChildPathPlacePreview() {
        return childPathPlacePreview;
    }

    public void setChildPathPlacePreview(String childPathPlacePreview) {
        this.childPathPlacePreview = childPathPlacePreview;
    }

    public static House fromContentValues(ContentValues values){
        final House house = new House();
        if(values.containsKey(ID_HOUSE)) house.setIdHouse(values.getAsLong(ID_HOUSE));
        if(values.containsKey(ID_HOUSE_TYPE)) house.setIdHouseType(values.getAsLong(ID_HOUSE_TYPE));
        if(values.containsKey(ID_REAL_ESTATE_AGENT)) house.setIdRealEstateAgent(values.getAsLong(ID_REAL_ESTATE_AGENT));
        if(values.containsKey(ID_ADDRESS)) house.setIdAddress(values.getAsLong(ID_ADDRESS));
        if(values.containsKey(PRICE)) house.setPrice(values.getAsDouble(PRICE));
        if(values.containsKey(SURFACE)) house.setSurface(values.getAsDouble(SURFACE));
        if(values.containsKey(DESCRIPTION)) house.setDescription(values.getAsString(DESCRIPTION));
        if(values.containsKey(STATE)) house.setState(values.getAsString(STATE));
        if(values.containsKey(AVAILABLE_DATE)) house.setAvailableDate(values.getAsLong(AVAILABLE_DATE));
        if(values.containsKey(SOLD_DATE)) house.setSoldDate(values.getAsLong(SOLD_DATE));
        return house;
    }
}
