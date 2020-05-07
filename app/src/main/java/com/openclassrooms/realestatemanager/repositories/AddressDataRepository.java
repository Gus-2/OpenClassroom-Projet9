package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.models.pojo.Address;

import java.util.List;

public class AddressDataRepository {

    private final AddressDao addressDao;

    public AddressDataRepository(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public LiveData<Address> getAddress(int idAddress){
        return addressDao.getAddress(idAddress);
    }

    public List<Address> getAddress(){
        return addressDao.getAddress();
    }

    public long insertAddress(Address address){
        return addressDao.insertAddress(address);
    }

}