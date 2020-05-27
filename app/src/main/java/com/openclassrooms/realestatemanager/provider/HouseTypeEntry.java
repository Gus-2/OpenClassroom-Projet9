package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_HOUSE_TYPE;

public class HouseTypeEntry implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSE_TYPE).build();
    public static final String CONTENT_HOUSE_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_HOUSE_TYPE;
    public static final String CONTENT_ITEM_HOUSE_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_HOUSE_TYPE;

    public static Uri buildHouseTypeWithUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
