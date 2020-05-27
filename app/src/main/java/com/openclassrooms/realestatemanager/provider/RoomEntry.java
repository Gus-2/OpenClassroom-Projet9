package com.openclassrooms.realestatemanager.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.openclassrooms.realestatemanager.provider.HouseContract.BASE_CONTENT_URI;
import static com.openclassrooms.realestatemanager.provider.HouseContract.PATH_ROOM;

public class RoomEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOM).build();
    public static final String CONTENT_ROOM = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ROOM;
    public static final String CONTENT_ITEM_ROOM = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ROOM;

    public static Uri buildRoomUriWithId(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
