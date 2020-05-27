package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_HOUSE;


public class HouseEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSE).build();
    public static final String CONTENT_HOUSE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_HOUSE;
    public static final String CONTENT_ITEM_HOUSE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_HOUSE;

    public static Uri buildHouseUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}