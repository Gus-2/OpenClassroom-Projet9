package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;

public class ItemContentProvider extends ContentProvider {

    public static final int HOUSE = 100;
    public static final int HOUSE_ID = 101;
    public static final int ADDRESS = 200;
    public static final int ADDRESS_ID = 201;
    public static final int HOUSE_POINT_OF_INTEREST = 300;
    public static final int HOUSE_POINT_OF_INTEREST_ID = 301;
    public static final int HOUSE_TYPE = 400;
    public static final int HOUSE_TYPE_ID = 401;
    public static final int PHOTO = 500;
    public static final int PHOTO_ID = 501;
    public static final int POINT_OF_INTEREST = 600;
    public static final int POINT_OF_INTEREST_ID = 601;
    public static final int REAL_ESTATE_AGENT = 700;
    public static final int REAL_ESTATE_AGENT_ID = 701;
    public static final int ROOM = 800;
    public static final int ROOM_ID = 801;
    public static final int ROOM_NUMBER = 900;
    public static final int ROOM_NUMBER_ID = 901;
    public static final int TYPE_POINT_OF_INTEREST = 1000;
    public static final int TYPE_POINT_OF_INTEREST_ID = 1001;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        return true;
    }

    // Allow the external app to make specific query in the app by to uri provided
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if(getContext() == null) throw new IllegalArgumentException("Failed to get context for uri " + uri);
        Cursor cursor;
        long idHouse;
        switch(sUriMatcher.match(uri)){
            case HOUSE:
                cursor = RealEstateDatabase.getInstance(getContext()).houseDao().getHousesWithCursor();
                break;
            case HOUSE_ID:
                idHouse = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).houseDao().getHouseFromIdWithCursor(idHouse);
                break;
            case ADDRESS:
                cursor = RealEstateDatabase.getInstance(getContext()).addressDao().getAddressCursor();
                break;
            case ADDRESS_ID :
                long idAddress = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).addressDao().getAddressFromIdWithCursor(idAddress);
                break;
            case HOUSE_POINT_OF_INTEREST :
                cursor = RealEstateDatabase.getInstance(getContext()).housePointOfInterestDao().getHousePointOfInterestWithCursor();
                break;
            case HOUSE_POINT_OF_INTEREST_ID:
                idHouse = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).housePointOfInterestDao().getHousePointOfInterestFromHouseIdWithCursor(idHouse);
                break;
            case HOUSE_TYPE:
                cursor = RealEstateDatabase.getInstance(getContext()).houseTypeDao().getHouseTypesWithCursor();
                break;
            case HOUSE_TYPE_ID:
                long idHouseType = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).houseTypeDao().getHouseTypeFromIdWithCursor(idHouseType);
                break;
            case POINT_OF_INTEREST:
                cursor = RealEstateDatabase.getInstance(getContext()).pointOfInterestDao().getPointOfInterestWithCursor();
                break;
            case POINT_OF_INTEREST_ID:
                long idPointOfInterest = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).pointOfInterestDao().getPointOfInterestFromIdWithCursor(idPointOfInterest);
                break;
            case REAL_ESTATE_AGENT:
                cursor = RealEstateDatabase.getInstance(getContext()).realEstateAgentDao().getRealEstateAgentWithCursor();
                break;
            case REAL_ESTATE_AGENT_ID:
                long idRealEstateAgent = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).realEstateAgentDao().getRealEstateAgentFromIdWithCursor(idRealEstateAgent);
                break;
            case ROOM:
                cursor = RealEstateDatabase.getInstance(getContext()).roomDao().getRoomsWithCursor();
                break;
            case ROOM_ID:
                long idRoom = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).roomDao().getRoomFromIdWithCursor(idRoom);
                break;
            case ROOM_NUMBER:
                cursor = RealEstateDatabase.getInstance(getContext()).roomNumberDao().getRoomNumberWithCursor();
                break;
            case ROOM_NUMBER_ID:
                idHouse = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).roomNumberDao().getRoomNumberForIdHouseWithCursor(idHouse);
                break;
            case TYPE_POINT_OF_INTEREST:
                cursor = RealEstateDatabase.getInstance(getContext()).typePointOfInterestDao().getTypePointOfInterestWithCursor();
                break;
            case TYPE_POINT_OF_INTEREST_ID:
                long idTypePointOfInterest = ContentUris.parseId(uri);
                cursor = RealEstateDatabase.getInstance(getContext()).typePointOfInterestDao().getTypePointOfInterestFromIdWithCursor(idTypePointOfInterest);
                break;
            default:
                throw new UnsupportedOperationException("Failed to query row for uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // Return the MIME type linked to the URI
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case HOUSE:
                return HouseEntry.CONTENT_HOUSE;
            case HOUSE_ID:
                return HouseEntry.CONTENT_ITEM_HOUSE;
            case ADDRESS:
                return AddressEntry.CONTENT_ADDRESS;
            case ADDRESS_ID:
                return AddressEntry.CONTENT_ITEM_ADDRESS;
            case HOUSE_POINT_OF_INTEREST:
                return HousePointOfInterestEntry.CONTENT_HOUSE_POINT_OF_INTEREST;
            case HOUSE_POINT_OF_INTEREST_ID:
                return HousePointOfInterestEntry.CONTENT_ITEM_HOUSE_POINT_OF_INTEREST;
            case HOUSE_TYPE:
                return HouseTypeEntry.CONTENT_HOUSE_TYPE;
            case HOUSE_TYPE_ID:
                return HouseTypeEntry.CONTENT_ITEM_HOUSE_TYPE;
            case PHOTO:
                return PhotoEntry.CONTENT_PHOTO;
            case PHOTO_ID:
                return PhotoEntry.CONTENT_ITEM_PHOTO;
            case POINT_OF_INTEREST:
                return PointOfInterestEntry.CONTENT_POINT_OF_INTEREST;
            case POINT_OF_INTEREST_ID:
                return PointOfInterestEntry.CONTENT_ITEM_POINT_OF_INTEREST;
            case REAL_ESTATE_AGENT:
                return RealEstateAgentEntry.CONTENT_REAL_ESTATE_AGENT;
            case REAL_ESTATE_AGENT_ID:
                return RealEstateAgentEntry.CONTENT_ITEM_REAL_ESTATE_AGENT;
            case ROOM:
                return RoomEntry.CONTENT_ROOM;
            case ROOM_ID :
                return RoomEntry.CONTENT_ITEM_ROOM;
            case ROOM_NUMBER:
                return RoomNumberEntry.CONTENT_ROOM_NUMBER;
            case ROOM_NUMBER_ID:
                return RoomNumberEntry.CONTENT_ITEM_ROOM_NUMBER;
            case TYPE_POINT_OF_INTEREST:
                return TypePointOfInterestEntry.CONTENT_TYPE_POINT_OF_INTEREST;
            case TYPE_POINT_OF_INTEREST_ID:
                return TypePointOfInterestEntry.CONTENT_ITEM_TYPE_POINT_OF_INTEREST;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        String content = HouseContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(content, HouseContract.PATH_HOUSE, HOUSE);
        matcher.addURI(content, HouseContract.PATH_HOUSE + "/#", HOUSE_ID);

        matcher.addURI(content, HouseContract.PATH_ADDRESS, ADDRESS);
        matcher.addURI(content, HouseContract.PATH_ADDRESS + "/#", ADDRESS_ID);

        matcher.addURI(content, HouseContract.PATH_HOUSE_POINT_OF_INTEREST, HOUSE_POINT_OF_INTEREST);
        matcher.addURI(content, HouseContract.PATH_HOUSE_POINT_OF_INTEREST + "/#", HOUSE_POINT_OF_INTEREST_ID);

        matcher.addURI(content, HouseContract.PATH_HOUSE_TYPE, HOUSE_TYPE);
        matcher.addURI(content, HouseContract.PATH_HOUSE_TYPE + "/#", HOUSE_TYPE_ID);

        matcher.addURI(content, HouseContract.PATH_PHOTO, PHOTO);
        matcher.addURI(content, HouseContract.PATH_PHOTO + "/#", PHOTO_ID);

        matcher.addURI(content, HouseContract.PATH_POINT_OF_INTEREST, POINT_OF_INTEREST);
        matcher.addURI(content, HouseContract.PATH_POINT_OF_INTEREST + "/#", POINT_OF_INTEREST_ID);

        matcher.addURI(content, HouseContract.PATH_REAL_ESTATE_AGENT, REAL_ESTATE_AGENT);
        matcher.addURI(content, HouseContract.PATH_REAL_ESTATE_AGENT + "/#", REAL_ESTATE_AGENT_ID);

        matcher.addURI(content, HouseContract.PATH_ROOM, ROOM);
        matcher.addURI(content, HouseContract.PATH_ROOM + "/#", ROOM_ID);

        matcher.addURI(content, HouseContract.PATH_ROOM_NUMBER, ROOM_NUMBER);
        matcher.addURI(content, HouseContract.PATH_ROOM_NUMBER + "/#", ROOM_NUMBER_ID);

        matcher.addURI(content, HouseContract.PATH_TYPE_POINT_OF_INTEREST, TYPE_POINT_OF_INTEREST);
        matcher.addURI(content, HouseContract.PATH_TYPE_POINT_OF_INTEREST + "/#", TYPE_POINT_OF_INTEREST_ID);

        return matcher;
    }
}
