package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.models.pojo.Address;

import java.util.List;

import io.reactivex.Completable;

public class AddressDataRepository {

    private final AddressDao addressDao;

    public AddressDataRepository(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public LiveData<Address> getAddress(long idAddress){
        return addressDao.getAddress(idAddress);
    }

    public Address getAddressFromId(long idAddress){
        return addressDao.getAddressFromId(idAddress);
    }

    public List<Address> getAddress(){
        return addressDao.getAddress();
    }

    public long insertAddress(Address address){
        return addressDao.insertAddress(address);
    }

    public Completable updateAddress(Address address){
        return addressDao.updateAddress(address);
    }
}
