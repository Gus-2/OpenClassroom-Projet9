package com.openclassrooms.realestatemanager.database.dao;


import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.pojo.Address;

import java.util.List;

@Dao
public interface AddressDao {

    @Query("SELECT *  FROM address WHERE id_address = :idAddress")
    LiveData<Address> getAddress(long idAddress);

    @Query("SELECT *  FROM address WHERE id_address = :idAddress")
    Address getAddressFromId(long idAddress);

    @Query("SELECT *  FROM address WHERE id_address = :idAddress")
    Cursor getAddressFromIdWithCursor(long idAddress);

    @Query("SELECT *  FROM address")
    List<Address> getAddress();

    @Query("SELECT *  FROM address")
    Cursor getAddressCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress(Address address);

}
