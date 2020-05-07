package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestatedetail.RealEstateDetailActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealEstateListFragment extends Fragment implements RealEstateListAdapter.OnItemClickListener {

    private BottomAppBar bottomAppBar;
    private RecyclerView recyclerView;
    private SharedViewModel sharedViewModel;
    private List<House> listHouses;
    private HashMap<Long, HouseType> hashMapHouseType;
    private HashMap<Long, Address> hashMapAddress;
    private HashMap<Long, List<Photo>> hashMapPhoto;
    private List<RealEstateAgent> listRealEstateAgent;
    private LiveData<List<RoomNumber>> listRoomNumberLiveDate;
    private List<RoomNumber> listRoomNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listHouses = new ArrayList<>();
        hashMapPhoto = new HashMap<>();
        hashMapHouseType = new HashMap<>();
        hashMapAddress = new HashMap<>();
        listRealEstateAgent = new ArrayList<>();
        listRoomNumber = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.realestatelist_fragment, container, false);
        recyclerView = result.findViewById(R.id.rc_fr_real_estate);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(getActivity() != null){
            bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void configureList() {
        sharedViewModel.getListData().observe(getViewLifecycleOwner(), databaseValue -> {
            listHouses = (List<House>) databaseValue.get(MainActivity.HOUSES);
            hashMapPhoto = TypeConverter.convertPhotoListToHashMap((List<Photo>) databaseValue.get(MainActivity.PHOTOS));
            hashMapHouseType = TypeConverter.convertHouseTypeListToHashMap((List<HouseType>) databaseValue.get(MainActivity.HOUSES_TYPES));
            hashMapAddress = TypeConverter.convertAddressListToHashMap((List<Address>) databaseValue.get(MainActivity.ADDRESS));
            listRealEstateAgent = (List<RealEstateAgent>) databaseValue.get(MainActivity.REAL_ESTATE_AGENT);
            listRoomNumberLiveDate = (LiveData<List<RoomNumber>>)databaseValue.get(MainActivity.ROOM_NUMBER);
            initializeAdapter();
            roomNumberObserver();
        });
    }

    private void roomNumberObserver(){
        listRoomNumberLiveDate.observe(getViewLifecycleOwner(), roomNumbersList -> setListRoomNumber(roomNumbersList));
    }

    private void setListRoomNumber(List<RoomNumber> listRoomNumber){
       this.listRoomNumber = listRoomNumber;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() != null){
            sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
            this.configureList();
        }
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

    private void initializeAdapter(){
        RealEstateListAdapter realEstateListAdapter = new RealEstateListAdapter(getContext(), listHouses, hashMapHouseType,
                hashMapAddress, hashMapPhoto, this);
        recyclerView.setAdapter(realEstateListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClickt(int position) {
        Intent intent = new Intent(getActivity(), RealEstateDetailActivity.class);
        House house = listHouses.get(position);
        intent.putExtra(MainActivity.HOUSES, house);
        intent.putExtra(MainActivity.HOUSES_TYPES, hashMapHouseType.get(house.getIdHouseType()));
        intent.putParcelableArrayListExtra(MainActivity.PHOTOS, ((ArrayList<Photo>) hashMapPhoto.get(house.getIdHouse())));
        intent.putParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT, ((ArrayList<RealEstateAgent>) listRealEstateAgent));
        intent.putExtra(MainActivity.ADDRESS, hashMapAddress.get(house.getIdAddress()));
        intent.putParcelableArrayListExtra(MainActivity.ROOM_NUMBER, ((ArrayList<RoomNumber>) listRoomNumber));
        startActivity(intent);
    }
}
