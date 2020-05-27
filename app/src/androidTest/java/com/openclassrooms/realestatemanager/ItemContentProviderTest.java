package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.AddressEntry;
import com.openclassrooms.realestatemanager.provider.HouseEntry;
import com.openclassrooms.realestatemanager.provider.HousePointOfInterestEntry;
import com.openclassrooms.realestatemanager.provider.HouseTypeEntry;
import com.openclassrooms.realestatemanager.provider.PointOfInterestEntry;
import com.openclassrooms.realestatemanager.provider.RealEstateAgentEntry;
import com.openclassrooms.realestatemanager.provider.RoomEntry;
import com.openclassrooms.realestatemanager.provider.RoomNumberEntry;
import com.openclassrooms.realestatemanager.provider.TypePointOfInterestEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ItemContentProviderTest {

    private ContentResolver mContentResolver;

    private static long ID_TEST = 1;

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
    }

    @Test
    public void getHouseWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HouseEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getHouseWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HouseEntry.buildHouseUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }
    @Test

    public void getAddressWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(AddressEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getAddressWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(AddressEntry.buildAddressUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getHousePointOfInterestWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HousePointOfInterestEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getHousePointOfInterestWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HousePointOfInterestEntry.buildHousePointOfInterestWithUri(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getHouseTypeWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HouseTypeEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(7));
        cursor.close();
    }

    @Test
    public void getHouseTypeWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(HouseTypeEntry.buildHouseTypeWithUri(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        cursor.close();
    }

    @Test
    public void getPointOfInterestWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(PointOfInterestEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getPointOfInterestWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(PointOfInterestEntry.buildPointOfInterestUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getRealEstateAgentWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RealEstateAgentEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getRealEstateAgentWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RealEstateAgentEntry.buildRealEstateAgentUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getRoomWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RoomEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(7));
        cursor.close();
    }

    @Test
    public void getRoomWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RoomEntry.buildRoomUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        cursor.close();
    }

    @Test
    public void getRoomNumberWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RoomNumberEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getRoomNumberWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(RoomNumberEntry.buildRoomNumberUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getTypePointOfInterestWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(TypePointOfInterestEntry.CONTENT_URI, null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void getTypePointOfInterestWithIdWhenNoItemInserted(){
        final Cursor cursor = mContentResolver.query(TypePointOfInterestEntry.buildTypePointOfInterestUriWithId(ID_TEST), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }




}
