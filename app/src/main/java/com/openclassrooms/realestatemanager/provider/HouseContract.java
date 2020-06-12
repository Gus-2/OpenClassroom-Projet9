package com.openclassrooms.realestatemanager.provider;

import android.net.Uri;

public class HouseContract {
    static final String PATH_HOUSE = "house";
    static final String PATH_ADDRESS = "address";
    static final String PATH_HOUSE_TYPE = "houseType";
    static final String PATH_PHOTO = "photos";
    static final String PATH_POINT_OF_INTEREST = "pointOfInterest";
    static final String PATH_HOUSE_POINT_OF_INTEREST = "house_point_of_interest";
    static final String PATH_REAL_ESTATE_AGENT = "realEstateAgent";
    static final String PATH_ROOM = "room";
    static final String PATH_ROOM_NUMBER = "roomNumber";
    static final String PATH_TYPE_POINT_OF_INTEREST = "typePointOfInterest";
    static final String CONTENT_AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/");
}
