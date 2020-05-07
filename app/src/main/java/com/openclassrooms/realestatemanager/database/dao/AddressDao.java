package com.openclassrooms.realestatemanager.database.dao;


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
    LiveData<Address> getAddress(int idAddress);

    @Query("SELECT *  FROM address")
    List<Address> getAddress();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress(Address address);

}
