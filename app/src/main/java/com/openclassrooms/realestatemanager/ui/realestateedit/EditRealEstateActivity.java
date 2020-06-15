package com.openclassrooms.realestatemanager.ui.realestateedit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRealEstateActivity extends AppCompatActivity {

    private House house;
    private Address address;
    private ArrayList<Photo> listPhoto;
    private ArrayList<RealEstateAgent> realEstateAgentList;
    private List<RoomNumber> listRoomNumber;
    private List<PointOfInterest> listPointOfInterest;
    private HashMap<Long, HouseType> hashMapHouseTypes;
    private List<HousePointOfInterest> listHousePointOfInterest;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_real_estate);
        if(getIntent() != null){
            house = getIntent().getParcelableExtra(MainActivity.HOUSES);
            address = getIntent().getParcelableExtra(MainActivity.ADDRESS);
            realEstateAgentList = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT));
            listRoomNumber = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.ROOM_NUMBER));
            listPointOfInterest = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.POINT_OF_INTEREST));
            listHousePointOfInterest = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.HOUSE_POINT_OF_INTEREST));
            listPhoto = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.PHOTOS));
            hashMapHouseTypes = (HashMap<Long, HouseType>) getIntent().getSerializableExtra(MainActivity.HOUSES_TYPES);
        }
        if (savedInstanceState == null)
            startFragment();
    }

    private void startFragment(){
        EditRealEstateFragment editRealEstateFragment = new EditRealEstateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.HOUSES, house);
        bundle.putParcelable(MainActivity.ADDRESS, address);
        bundle.putParcelableArrayList(MainActivity.REAL_ESTATE_AGENT, realEstateAgentList);
        bundle.putParcelableArrayList(MainActivity.ROOM_NUMBER, new ArrayList<>(listRoomNumber));
        bundle.putParcelableArrayList(MainActivity.POINT_OF_INTEREST, new ArrayList<>(listPointOfInterest));
        bundle.putParcelableArrayList(MainActivity.HOUSE_POINT_OF_INTEREST, new ArrayList<>(listHousePointOfInterest));
        bundle.putParcelableArrayList(MainActivity.PHOTOS, new ArrayList<>(listPhoto));
        bundle.putSerializable(MainActivity.HOUSES_TYPES, hashMapHouseTypes);
        editRealEstateFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view_edit_real_estate, editRealEstateFragment).commit();
    }
}
