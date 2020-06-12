package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_PHOTO;

public class PhotoEntry implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTO).build();
    static final String CONTENT_PHOTO = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PHOTO;
    static final String CONTENT_ITEM_PHOTO = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PHOTO;

    public static Uri buildPhotoUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
