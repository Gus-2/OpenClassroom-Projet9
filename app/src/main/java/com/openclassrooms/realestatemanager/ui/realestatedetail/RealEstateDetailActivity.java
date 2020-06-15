package com.openclassrooms.realestatemanager.ui.realestatedetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class RealEstateDetailActivity extends AppCompatActivity {

    public static final String ID_HOUSE = "HOUSE_ID";
    public static final String REAL_ESTATE_AGENT_LIST = "REAL_ESTATE_AGENT";
    public static final String HOUSE_TYPE_HASH_MAP = "HOUSE_TYPE_HASH_MAP";

    private long idHouse;
    private ArrayList<RealEstateAgent> realEstateAgentList;
    private HashMap<Long, HouseType> longHouseTypeHashMap;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_detail);
        if(getIntent() != null){
            idHouse = getIntent().getLongExtra(RealEstateListFragment.HOUSE_ID, 0);
            realEstateAgentList = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT));
            longHouseTypeHashMap = new HashMap<>((HashMap<Long, HouseType>) getIntent().getSerializableExtra(MainActivity.HOUSES_TYPES));
        }
        if (savedInstanceState == null)
            startFragment();
    }

    public void startFragment(){
        RealEstateDetailFragment realEstateDetailFragment = new RealEstateDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_HOUSE, idHouse);
        bundle.putParcelableArrayList(REAL_ESTATE_AGENT_LIST, realEstateAgentList);
        bundle.putSerializable(HOUSE_TYPE_HASH_MAP, longHouseTypeHashMap);
        realEstateDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view_detail, realEstateDetailFragment).commit();
    }
}