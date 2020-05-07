package com.openclassrooms.realestatemanager.models.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "address")
public class Address implements Parcelable {

    @ColumnInfo(name = "id_address")
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long idAddress;

    @NonNull
    private String street;

    @NonNull
    private String number;

    @NonNull
    private String district;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @ColumnInfo(name = "post_code")
    @NonNull
    private String postCode;

    @ColumnInfo(name = "additional_information")
    private String additionalInformation;

    private Double latitude;

    private Double longitude;

    public Address(@NonNull String street, @NonNull String number, @NonNull String district, @NonNull String city, @NonNull String country, @NonNull String postCode, String additionalInformation) {
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.country = country;
        this.postCode = postCode;
        this.additionalInformation = additionalInformation;
        this.latitude = null;
        this.longitude = null;
    }

    protected Address(Parcel in) {
        idAddress = in.readLong();
        street = in.readString();
        number = in.readString();
        district = in.readString();
        city = in.readString();
        country = in.readString();
        postCode = in.readString();
        additionalInformation = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idAddress);
        dest.writeString(street);
        dest.writeString(number);
        dest.writeString(district);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(postCode);
        dest.writeString(additionalInformation);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

    @NonNull
    public String getStreet() {
        return street;
    }

    public void setStreet(@NonNull String street) {
        this.street = street;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    @NonNull
    public String getDistrict() {
        return district;
    }

    public void setDistrict(@NonNull String district) {
        this.district = district;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }

    @NonNull
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(@NonNull String postCode) {
        this.postCode = postCode;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
