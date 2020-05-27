package com.openclassrooms.realestatemanager.provider;

import android.net.Uri;

public class HouseContract {

    public static final String PATH_HOUSE = "house";
    public static final String PATH_ADDRESS = "address";
    public static final String PATH_HOUSE_TYPE = "houseType";
    public static final String PATH_PHOTO = "photos";
    public static final String PATH_POINT_OF_INTEREST = "pointOfInterest";
    public static final String PATH_HOUSE_POINT_OF_INTEREST = "house_point_of_interest";
    public static final String PATH_REAL_ESTATE_AGENT = "realEstateAgent";
    public static final String PATH_ROOM = "room";
    public static final String PATH_ROOM_NUMBER = "roomNumber";
    public static final String PATH_TYPE_POINT_OF_INTEREST = "typePointOfInterest";

    public static final String CONTENT_AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/");


}
