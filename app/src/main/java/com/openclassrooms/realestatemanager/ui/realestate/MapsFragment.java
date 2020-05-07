package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private static final int DEFAULT_ZOOM = 15;

    private BottomAppBar bottomAppBar;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private SharedViewModel sharedViewModel;
    private List<House> listHouses;
    private List<Photo> listPhotos;
    private List<HouseType> listHouseTypes;
    private List<Address> listAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationPermissionGranted = ((MainActivity) getActivity()).ismLocationPermissionGranted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.maps_fragment, container, false);

        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        mapView = result.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if(mLocationPermissionGranted) initMap();
        return result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

    }

    private void configureList() {
        sharedViewModel.getListData().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> databaseValue) {

                listHouses = (List<House>) databaseValue.get(MainActivity.HOUSES);
                listPhotos = (List<Photo>) databaseValue.get(MainActivity.PHOTOS);
                listHouseTypes = (List<HouseType>) databaseValue.get(MainActivity.HOUSES_TYPES);
                listAddress = (List<Address>) databaseValue.get(MainActivity.ADDRESS);
                displayHousesOnTheMap();
            }
        });
    }

    public void displayHousesOnTheMap(){
        for(Address address : listAddress){
            if(address.getLongitude() != null){
                Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_place_blue_24dp);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).icon(markerIcon);
                Marker marker = googleMap.addMarker(markerOptions);
            }
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void initMap(){
        mapView.getMapAsync(this);
    }

    private void getDeviceLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if(mLocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Location currentLocation = (Location) task.getResult();
                        if(currentLocation == null){
                            LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            String provider = locationManager.getBestProvider(criteria, true);
                            currentLocation = locationManager.getLastKnownLocation(provider);
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                    }else{
                        Toast.makeText(getContext(), R.string.couldnt_get_location, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomAppBar.setNavigationIcon(R.drawable.ic_list_white_32dp);
        bottomAppBar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, new RealEstateListFragment())
                .addToBackStack(null)
                .commit());
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLoadedCallback(this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        getDeviceLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapLoaded() {
        this.configureList();
    }
}
