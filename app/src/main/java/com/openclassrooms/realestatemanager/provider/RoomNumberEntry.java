package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_ROOM_NUMBER;

public class RoomNumberEntry implements BaseColumns {

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOM_NUMBER).build();
    public static final String CONTENT_ROOM_NUMBER = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ROOM_NUMBER;
    public static final String CONTENT_ITEM_ROOM_NUMBER = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ROOM_NUMBER;

    public static Uri buildRoomNumberUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
