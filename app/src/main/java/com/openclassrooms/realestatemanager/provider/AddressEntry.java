package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_ADDRESS;

public class AddressEntry implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADDRESS).build();
    static final String CONTENT_ADDRESS = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ADDRESS;
    static final String CONTENT_ITEM_ADDRESS = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ADDRESS;

    public static Uri buildAddressUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
