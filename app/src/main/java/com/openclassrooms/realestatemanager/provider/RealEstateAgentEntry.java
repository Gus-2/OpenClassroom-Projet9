package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_POINT_OF_INTEREST;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_REAL_ESTATE_AGENT;

public class RealEstateAgentEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POINT_OF_INTEREST).build();
    public static final String CONTENT_REAL_ESTATE_AGENT = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_REAL_ESTATE_AGENT;
    public static final String CONTENT_ITEM_REAL_ESTATE_AGENT = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_REAL_ESTATE_AGENT;

    public static Uri buildRealEstateAgentUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
