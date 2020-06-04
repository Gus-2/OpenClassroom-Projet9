package com.openclassrooms.realestatemanager.ui.realestate;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.tools.SearchUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String FINE_LOCATTION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    public static final String PHOTOS = "PHOTOS";
    public static final String HOUSES_TYPES = "HOUSES_TYPES";
    public static final String HOUSES = "HOUSES";
    public static final String ADDRESS = "ADDRESS";
    public static final String REAL_ESTATE_AGENT = "REAL_ESTATE_AGENT";
    public static final String ROOM_NUMBER = "ROOM_NUMBER";
    public static final String POINT_OF_INTEREST = "POINT_OF_INTEREST";
    public static final String ROOM = "ROOM";
    public static final String HOUSE_POINT_OF_INTEREST = "HOUSE_POINT_OF_INTEREST";
    public static final String TYPE_POINT_OF_INTEREST = "TYPE_POINT_OF_INTEREST";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 20;
    private final int ERROR_DIALOG_REQUEST = 9001;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton addHouse;
    private final int ADD_PROPERTY = 80;
    private RealEstateViewModel realEstateViewModel;
    private GetDataFromDatabaseAsyncTask getDataFromDatabaseAsyncTask;
    private boolean mLocationPermissionGranted = false;
    private SharedViewModel sharedViewModel;
    private HashMap<String, Object> databaseValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationPermission();
        if(iseServicesOk()){
            bottomAppBar = findViewById(R.id.bottom_app_bar);
            setSupportActionBar(bottomAppBar);
            addHouse = findViewById(R.id.fab_add_house);
            configureViewModels();
            databaseValues = new HashMap<>();
            getDataFromDatabaseAsyncTask = new GetDataFromDatabaseAsyncTask(this);
            this.configureFabAddHouse();
            this.getDataFromDatabase();
            startFragment();
        }
    }

    public void startFragment(){
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, new RealEstateListFragment()).commit();
    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.sharedViewModel = new ViewModelProvider(this, mViewModelFactory).get(SharedViewModel.class);
    }



    public void getDataFromDatabase(){
        realEstateViewModel.getHouseData().observe(this, houses -> {
            databaseValues.clear();
            databaseValues.put(HOUSES, houses);
            GetDataFromDatabaseAsyncTask dataFromDatabaseAsyncTask = new GetDataFromDatabaseAsyncTask(this);
            dataFromDatabaseAsyncTask.execute();
        });
    }


    private void configureFabAddHouse() {
        addHouse.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FormActivity.class);
            startActivityForResult(intent, ADD_PROPERTY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ADD_PROPERTY){
            Toast.makeText(this, R.string.house_successfully_added, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean iseServicesOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, R.string.cant_make_map_request, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this, FINE_LOCATTION ) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    return;
                }
                break;
        }
    }

    public boolean ismLocationPermissionGranted() {
        return mLocationPermissionGranted;
    }

    public void updateSharedViewModel(){
        sharedViewModel.setListData(databaseValues);
    }

    private static class GetDataFromDatabaseAsyncTask extends AsyncTask<String, Void, String> {

        WeakReference<MainActivity> weakReference;

        GetDataFromDatabaseAsyncTask(MainActivity mainActivity) {
            this.weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected String doInBackground(String... strings) {
            weakReference.get().databaseValues.put(HOUSES_TYPES, weakReference.get().realEstateViewModel.getHouseType());
            weakReference.get().databaseValues.put(ADDRESS, weakReference.get().realEstateViewModel.getAddress());
            weakReference.get().databaseValues.put(PHOTOS, weakReference.get().realEstateViewModel.getPhoto());
            weakReference.get().databaseValues.put(REAL_ESTATE_AGENT, weakReference.get().realEstateViewModel.getRealEstateAgent());
            weakReference.get().databaseValues.put(ROOM, weakReference.get().realEstateViewModel.getRoom());
            weakReference.get().databaseValues.put(POINT_OF_INTEREST, TypeConverter.listPointOfInterestToHashMap(weakReference.get().realEstateViewModel.getListPointOfInterest()));
            weakReference.get().databaseValues.put(HOUSE_POINT_OF_INTEREST, TypeConverter.listHousePointOfInterestToHashMap(weakReference.get().realEstateViewModel.getListHousePointOfInterest()));
            weakReference.get().databaseValues.put(TYPE_POINT_OF_INTEREST, TypeConverter.listTypePointOfInterestToHashMap(weakReference.get().realEstateViewModel.getListTypePointOfInterest()));
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            weakReference.get().updateSharedViewModel();
        }
    }
}
