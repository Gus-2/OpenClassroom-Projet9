package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
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
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.tools.SearchUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestatedetail.RealEstateDetailActivity;
import com.openclassrooms.realestatemanager.ui.realestatedetail.RealEstateDetailFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.DISTRICT;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MAX_PRICE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MAX_SURFACE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MIN_PRICE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MIN_SURFACE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.TAG_SEARCH_DIALOG;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener, SearchDialog.SearchDialogListener {

    private static final int DEFAULT_ZOOM = 10;
    private static final int REQUEST_CHECK_SETTINGS = 20;
    private static final int REQUEST_LOCATION_AUTHORIZED = -1;
    private static final int REQUEST_LOCATION_NON_AUTHORIZED = 0;

    private BottomAppBar bottomAppBar;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean mLocationPermissionGranted;
    private SharedViewModel sharedViewModel;
    private List<House> listHouses;
    private List<House> listHousesDisplayed;
    private HashMap<Long, HouseType> hashMapHouseType;
    private HashMap<Long, Address> hashMapAddress;
    private HashMap<Long, Address> hashMapAddressDisplayed;
    private HashMap<Long, ArrayList<Photo>> hashMapPhoto;
    private HashMap<String, Long> hashMapIdMarkerIdHouse;
    private HashMap<Long, List<HousePointOfInterest>> hashMapHouseTypePointOfInterest;
    private HashMap<Long, PointOfInterest> hashMapPointOfInterest;
    private HashMap<Long, TypePointOfInterest> hashMapTypePointOfInterest;
    private List<RealEstateAgent> listRealEstateAgent;
    private ArrayList<HouseType> listHousesTypes;
    private ArrayList<String> listDistrict = new ArrayList<>();
    private double minPrice = 0;
    private double maxPrice = 0;
    private double maxSurface;
    private double minSurface;
    private LocationSettingsRequest.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null){
            mLocationPermissionGranted = ((MainActivity) getActivity()).ismLocationPermissionGranted();
        }
        hashMapIdMarkerIdHouse = new HashMap<>();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTINGS){
            if(resultCode == REQUEST_LOCATION_AUTHORIZED){
                getDeviceLocation();
            }else if(resultCode == REQUEST_LOCATION_NON_AUTHORIZED){
                Toast.makeText(getContext(), R.string.unable_get_location, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.maps_fragment, container, false);
        setHasOptionsMenu(true);
        if(getActivity() != null) bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        mapView = result.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if(mLocationPermissionGranted) initMap();
        return result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() != null)
            sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(getActivity() != null && getActivity().findViewById(R.id.frame_layout_detail) == null){
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.search){
            openDialog();
            return true;
        }
        return false;
    }

    private void openDialog() {
        getMaxPrice();
        getMinPrice();
        getMaxSurface();
        getMinSurface();
        getAllDistrict();

        SearchDialog searchDialog = new SearchDialog();
        searchDialog.setTargetFragment(this, 0);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.HOUSES_TYPES, listHousesTypes);
        bundle.putDouble(MAX_PRICE, maxPrice);
        bundle.putDouble(MIN_PRICE, minPrice);
        bundle.putDouble(MAX_SURFACE, maxSurface);
        bundle.putDouble(MIN_SURFACE, minSurface);
        bundle.putStringArrayList(DISTRICT, listDistrict);

        searchDialog.setArguments(bundle);
        if(getActivity() != null)
            searchDialog.show(getActivity().getSupportFragmentManager(), TAG_SEARCH_DIALOG);
    }

    private void getAllDistrict() {
        for(House house : listHouses){
            Address address = hashMapAddress.get(house.getIdHouse());
            if(address != null && !listDistrict.contains(address.getDistrict())) listDistrict.add(address.getDistrict());
        }
    }

    private void getMaxPrice(){
        for(House house : listHouses) if(house.getPrice() > maxPrice) maxPrice = house.getPrice();

    }
    private void getMinPrice(){
        if(listHouses.size() == 1)
            minPrice = 0;
        else{
            minPrice = maxPrice;
            for(House house : listHouses) if(house.getPrice() < minPrice) minPrice = house.getPrice();
        }
    }

    private void getMaxSurface(){
        for(House house : listHouses) if(house.getSurface() > maxSurface) maxSurface = house.getSurface();

    }
    private void getMinSurface(){
        if(listHouses.size() == 1)
            minSurface = 0;
        else{
            minSurface = maxSurface;
            for(House house : listHouses) if(house.getSurface() < minSurface) minSurface = house.getSurface();
        }
    }

    @SuppressWarnings("unchecked")
    private void configureList() {
        sharedViewModel.getListData().observe(getViewLifecycleOwner(), databaseValue -> {
            listHouses = (List<House>) databaseValue.get(MainActivity.HOUSES);
            if(listHouses != null) listHousesDisplayed = new ArrayList<>(listHouses);
            hashMapPhoto = new HashMap<>(TypeConverter.getPhotoListToHashMap((List<Photo>) databaseValue.get(MainActivity.PHOTOS)));
            listHousesTypes = (ArrayList<HouseType>) databaseValue.get(MainActivity.HOUSES_TYPES);
            if(listHousesTypes != null) hashMapHouseType = TypeConverter.convertHouseTypeListToHashMap(listHousesTypes);
            hashMapAddress = TypeConverter.convertAddressListToHashMap((List<Address>) databaseValue.get(MainActivity.ADDRESS));
            hashMapAddressDisplayed = new HashMap<>(hashMapAddress);
            listRealEstateAgent = (List<RealEstateAgent>) databaseValue.get(MainActivity.REAL_ESTATE_AGENT);
            hashMapPointOfInterest = (HashMap<Long, PointOfInterest>) databaseValue.get(MainActivity.POINT_OF_INTEREST);
            hashMapHouseTypePointOfInterest = (HashMap<Long, List<HousePointOfInterest>>) databaseValue.get(MainActivity.HOUSE_POINT_OF_INTEREST);
            hashMapTypePointOfInterest = (HashMap<Long, TypePointOfInterest>) databaseValue.get(MainActivity.TYPE_POINT_OF_INTEREST);
            displayHousesOnTheMap();
        });
    }

    private void displayHousesOnTheMap(){
        googleMap.clear();
        for (House house : listHousesDisplayed) {
            Address address = hashMapAddressDisplayed.get(house.getIdHouse());
            if(address != null && address.getLongitude() != null){
                Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_place_blue_24dp);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).icon(markerIcon);
                Marker marker = googleMap.addMarker(markerOptions);
                hashMapIdMarkerIdHouse.put(marker.getId(), house.getIdHouse());
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

    private void initMap(){
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLoadedCallback(this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        getDeviceLocation();
    }

    private void getDeviceLocation(){
        if(getContext() != null) fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if(mLocationPermissionGranted){
                Task<Location> location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Location currentLocation = task.getResult();
                        if(currentLocation == null){
                            builder = new LocationSettingsRequest.Builder();
                            checkIfCurrentLocationSettingsSatisfied();
                        }else{
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                        }
                    }else{
                        Toast.makeText(getContext(), R.string.couldnt_get_location, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng latLng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    private void checkIfCurrentLocationSettingsSatisfied(){
        if(getActivity() != null){
            SettingsClient client = LocationServices.getSettingsClient(getActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(locationSettingsResponse ->
                createLocationRequest()
            );

            task.addOnFailureListener(getActivity(), e -> {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                    } catch (IntentSender.SendIntentException sendEx) {
                        sendEx.printStackTrace();
                    }
                }
            });
        }
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        initializeLocationCallback();
    }

    private void initializeLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getContext(), R.string.couldnt_get_location, Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    moveCamera(new LatLng(location.getLatitude(),location.getLongitude()));
                }
            }
        };
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null && getActivity().findViewById(R.id.frame_layout_detail) == null){
            bottomAppBar.setNavigationIcon(R.drawable.ic_list_white_32dp);
            bottomAppBar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_main, new RealEstateListFragment())
                    .addToBackStack(null)
                    .commit());
        }
        mapView.onResume();
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(getActivity()!= null && getActivity().findViewById(R.id.frame_layout_detail) == null){
            Intent intent = new Intent(getActivity(), RealEstateDetailActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra(RealEstateDetailActivity.ID_HOUSE, hashMapIdMarkerIdHouse.get(marker.getId()));
            intent.putExtra(MainActivity.HOUSES_TYPES, hashMapHouseType);
            intent.putParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT, (ArrayList<RealEstateAgent>) listRealEstateAgent);
            intent.putExtras(bundle);
            startActivity(intent);
            return false;
        }else{
            Long idHouse;
            if((idHouse = hashMapIdMarkerIdHouse.get(marker.getId())) != null){
                RealEstateDetailFragment realEstateDetailFragment = new RealEstateDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(RealEstateDetailActivity.ID_HOUSE, idHouse);
                bundle.putParcelableArrayList(RealEstateDetailActivity.REAL_ESTATE_AGENT_LIST, (ArrayList<RealEstateAgent>) listRealEstateAgent);
                bundle.putSerializable(RealEstateDetailActivity.HOUSE_TYPE_HASH_MAP, TypeConverter.convertHouseTypeListToHashMap(listHousesTypes));
                realEstateDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_detail, realEstateDetailFragment)
                        .commit();
                return true;
            }else{
                Toast.makeText(getActivity(), R.string.not_retrieving_data_for_place, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public void search(long houseType, long minSurface, long maxSurface, long minPrice, long maxPrice, long availabilityDate, String district, long numberPhoto, HashMap<String, Boolean> nearbyTypesHashMap) {
        listHousesDisplayed.clear();
        listHousesDisplayed.addAll(SearchUtils.getListHouseFiltered(listHouses, hashMapAddress, hashMapPhoto, houseType, minSurface, maxSurface, minPrice, maxPrice,
                availabilityDate, district, numberPhoto, nearbyTypesHashMap, getContext(), hashMapHouseTypePointOfInterest, hashMapPointOfInterest, hashMapTypePointOfInterest));
        hashMapIdMarkerIdHouse.clear();
        displayHousesOnTheMap();
    }
}
