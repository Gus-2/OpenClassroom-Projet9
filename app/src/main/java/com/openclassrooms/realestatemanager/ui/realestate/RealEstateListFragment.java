package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestatedetail.RealEstateDetailActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealEstateListFragment extends Fragment implements RealEstateListAdapter.OnItemClickListener, SearchDialog.SearchDialogListener {

    final static String MAX_PRICE = "MAX_PRICE";
    public final static String MIN_PRICE = "MIN_PRICE";
    public final static String MIN_SURFACE = "MIN_SURFACE";
    public final static String MAX_SURFACE = "MAX_SURFACE";
    public final static String TAG_SEARCH_DIALOG = "search dialog";
    public final static String HOUSE_ID = "HOUSE_ID";
    public static String DISTRICT = "DISTRICT";

    private BottomAppBar bottomAppBar;
    private RecyclerView recyclerView;
    private RealEstateListAdapter realEstateListAdapter;
    private SharedViewModel sharedViewModel;
    private List<House> listHouses;
    private List<House> listHousesDisplayed;
    private HashMap<Long, HouseType> hashMapHouseType;
    private HashMap<Long, Address> hashMapAddress;
    private HashMap<Long, ArrayList<Photo>> hashMapPhoto;
    private ArrayList<RealEstateAgent> listRealEstateAgent;
    private List<Room> listRoom;
    private ArrayList<HouseType> listHousesTypes;
    private ArrayList<String> listDistrict = new ArrayList<>();
    private double minPrice = 0;
    private double maxPrice = 0;
    private double maxSurface;
    private double minSurface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listHouses = new ArrayList<>();
        listHousesDisplayed = new ArrayList<>();
        hashMapPhoto = new HashMap<>();
        hashMapHouseType = new HashMap<>();
        hashMapAddress = new HashMap<>();
        listRealEstateAgent = new ArrayList<>();
        listRoom = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.realestatelist_fragment, container, false);
        setHasOptionsMenu(true);
        recyclerView = result.findViewById(R.id.rc_fr_real_estate);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(getActivity() != null) bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        this.configureViewModel();
        return result;
    }

    @SuppressWarnings("unchecked")
    private void configureList() {
        sharedViewModel.getListData().observe(getViewLifecycleOwner(), databaseValue -> {
            listHouses = (List<House>) databaseValue.get(MainActivity.HOUSES);
            listHousesDisplayed = new ArrayList<>(listHouses);
            hashMapPhoto = TypeConverter.getPhotoListToHashMap((List<Photo>) databaseValue.get(MainActivity.PHOTOS));
            listHousesTypes = (ArrayList<HouseType>) databaseValue.get(MainActivity.HOUSES_TYPES);
            hashMapHouseType = TypeConverter.convertHouseTypeListToHashMap(listHousesTypes);
            hashMapAddress = TypeConverter.convertAddressListToHashMap((List<Address>) databaseValue.get(MainActivity.ADDRESS));
            listRealEstateAgent = (ArrayList<RealEstateAgent>) databaseValue.get(MainActivity.REAL_ESTATE_AGENT);
            listRoom = (List<Room>) databaseValue.get(MainActivity.ROOM);
            realEstateListAdapter = null;
            initializeAdapter();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
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
        searchDialog.show(getActivity().getSupportFragmentManager(), TAG_SEARCH_DIALOG);
    }

    private void getAllDistrict() {
        for(House house : listHouses){
            Address address = hashMapAddress.get(house.getIdHouse());
            if(!listDistrict.contains(address.getDistrict())) listDistrict.add(address.getDistrict());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void configureViewModel(){
        if(getActivity() != null){
            sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
            this.configureList();
        }
    }

    private void initializeAdapter(){
        realEstateListAdapter = new RealEstateListAdapter(getContext(), listHousesDisplayed, hashMapHouseType,
                hashMapAddress, hashMapPhoto, TypeConverter.listRoomToHashMap(listRoom), this);
        recyclerView.setAdapter(realEstateListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClickt(int position) {
        Intent intent = new Intent(getActivity(), RealEstateDetailActivity.class);
        House house = listHousesDisplayed.get(position);
        intent.putExtra("HOUSE_ID", house.getIdHouse());
        intent.putExtra(MainActivity.HOUSES_TYPES, hashMapHouseType);
        intent.putParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT, listRealEstateAgent);
        startActivity(intent);
    }

    @Override
    public void search(long houseType, long minSurface, long maxSurface, long minPrice, long maxPrice, long availabilityDate, String district, long numberPhoto) {
        listHousesDisplayed.clear();

        if(houseType == -1 && maxSurface == -1 && maxPrice == -1 && availabilityDate == 0 && district.equals(getString(R.string.no_district_defined)) && numberPhoto == -1){
            listHousesDisplayed.addAll(listHouses);
            realEstateListAdapter.notifyDataSetChanged();
            return;
        }

        if(houseType != -1){
            for(House house : listHouses)
                if(house.getIdHouseType() == houseType)
                    listHousesDisplayed.add(house);
        }

        if(maxSurface != -1){
            for(House house : listHouses)
                if(house.getSurface() <= maxSurface && house.getSurface() >= minSurface && !listHousesDisplayed.contains(house))
                    listHousesDisplayed.add(house);
        }

        if(maxPrice != -1){
            for(House house : listHouses)
                if(house.getPrice() <= maxPrice && house.getPrice() >= minPrice && !listHousesDisplayed.contains(house))
                    listHousesDisplayed.add(house);
        }

        if(availabilityDate != 0){
            for(House house : listHouses)
                if(house.getAvailableDate() >= availabilityDate && !listHousesDisplayed.contains(house))
                    listHousesDisplayed.add(house);
        }

        if(!district.equals("")){
            for(House house : listHouses)
                if(hashMapAddress.get(house.getIdAddress()).getDistrict().equals(district) && !listHousesDisplayed.contains(house))
                    listHousesDisplayed.add(house);
        }

        if(numberPhoto > 0){
            for(House house : listHouses)
                if(hashMapPhoto.get(house.getIdHouse()) != null && hashMapPhoto.get(house.getIdHouse()).size() >= numberPhoto && !listHousesDisplayed.contains(house))
                    listHousesDisplayed.add(house);
        }

        realEstateListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomAppBar.setNavigationIcon(R.drawable.ic_map_white_32dp);
        if(getActivity()!= null){
            bottomAppBar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new MapsFragment())
                    .addToBackStack(null)
                    .commit());
        }
    }
}
