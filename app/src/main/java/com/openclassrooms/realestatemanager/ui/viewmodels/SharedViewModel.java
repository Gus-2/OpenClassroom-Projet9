package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Object>> listData = new MutableLiveData<>();

    public void setListData(HashMap<String, Object> listData){
        this.listData.postValue(listData);
    }

    public LiveData<HashMap<String, Object>> getListData() {
        return listData;
    }

}
