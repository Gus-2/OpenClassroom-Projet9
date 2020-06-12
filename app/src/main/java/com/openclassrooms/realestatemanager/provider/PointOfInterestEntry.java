package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_POINT_OF_INTEREST;

public class PointOfInterestEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POINT_OF_INTEREST).build();
    static final String CONTENT_POINT_OF_INTEREST = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_POINT_OF_INTEREST;
    static final String CONTENT_ITEM_POINT_OF_INTEREST = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_POINT_OF_INTEREST;

    public static Uri buildPointOfInterestUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
