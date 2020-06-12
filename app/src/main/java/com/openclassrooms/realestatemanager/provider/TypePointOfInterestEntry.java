package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_TYPE_POINT_OF_INTEREST;

public class TypePointOfInterestEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TYPE_POINT_OF_INTEREST).build();
    static final String CONTENT_TYPE_POINT_OF_INTEREST = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_TYPE_POINT_OF_INTEREST;
    static final String CONTENT_ITEM_TYPE_POINT_OF_INTEREST = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TYPE_POINT_OF_INTEREST;

    public static Uri buildTypePointOfInterestUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
