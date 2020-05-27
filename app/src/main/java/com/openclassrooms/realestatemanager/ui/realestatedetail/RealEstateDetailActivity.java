package com.openclassrooms.realestatemanager.ui.realestatedetail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.tools.DateConverter;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestate.PicturePagerAdapter;
import com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment;
import com.openclassrooms.realestatemanager.ui.realestateedit.EditRealEstateActivity;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateDetailActivity extends AppCompatActivity {
    private final static int REQUEST_CODE = 20;
    private final static int RESULT_CODE = 30;
    private final static int UPDATE_PICTURE = 31;
    private final static int UPDATE_POINT_OF_INTEREST = 32;
    public final static String STATE_SOLD = "Sold";

    @BindView(R.id.tv_type_detail)
    MaterialTextView tvTypeDetail;
    @BindView(R.id.tv_surface_detail)
    MaterialTextView tvSurfaceDetail;
    @BindView(R.id.tv_state_detail)
    MaterialTextView tvStateDetail;
    @BindView(R.id.tv_real_estate_agent_detail)
    MaterialTextView tvRealEstateAgentDetail;

    @BindView(R.id.tv_number_kitchen_detail)
    MaterialTextView tvKitchenDetail;
    @BindView(R.id.tv_number_bathrooms_detail)
    MaterialTextView tvBathroomDetail;
    @BindView(R.id.tv_number_bedroom_detail)
    MaterialTextView tvBedroomDetail;
    @BindView(R.id.tv_number_living_room_detail)
    MaterialTextView tvLivingRoomDetail;
    @BindView(R.id.tv_number_toilet_detail)
    MaterialTextView tvToiletDetail;
    @BindView(R.id.tv_number_cellar_detail)
    MaterialTextView tvCellarDetail;
    @BindView(R.id.tv_number_pool_detail)
    MaterialTextView tvPoolDetail;

    @BindView(R.id.tv_description_content_detail)
    MaterialTextView tvDescriptionContentDetail;

    @BindView(R.id.tv_street_number_detail)
    MaterialTextView tvStreetNumberDetail;
    @BindView(R.id.tv_district_detail)
    MaterialTextView tvDistrictDetail;
    @BindView(R.id.tv_postcode_city_detail)
    MaterialTextView tvPostcodeCityDetail;
    @BindView(R.id.tv_country_detail)
    MaterialTextView tvCountryDetail;
    @BindView(R.id.tv_additionnal_information_detail)
    MaterialTextView tvAdditionnalInformationDetail;

    @BindView(R.id.rv_point_of_interest_detail)
    RecyclerView rvPointOfInterestDetail;

    @BindView(R.id.iv_map_detail)
    ImageView ivMapDetail;

    @BindView(R.id.fab_home_sold)
    FloatingActionButton fabHomeSold;

    @BindView(R.id.vp_real_estate_detail)
    ViewPager vpHousePictureDetail;

    private long idHouse;
    private House house;
    private Address address = null;
    private ArrayList<Photo> photoList;
    private HouseType houseType;
    private ArrayList<RealEstateAgent> realEstateAgents;
    private List<RoomNumber> listRoomNumber;
    private List<PointOfInterest> listPointOfInterest = new ArrayList<>();
    private HashMap<Long, TypePointOfInterest> listTypePointOfInterest = new HashMap<>();
    private HashMap<Long, HouseType> listHouseTypes;
    private List<HousePointOfInterest> listHousePointOfInterest = new ArrayList<>();
    private AdapterPointOfInterestDetail adapterPointOfInterest;
    private RealEstateViewModel realEstateViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_detail);
        ButterKnife.bind(this);
        if(getIntent() != null){
            idHouse = getIntent().getLongExtra(RealEstateListFragment.HOUSE_ID, 0);
            realEstateAgents = new ArrayList<>(getIntent().getParcelableArrayListExtra(MainActivity.REAL_ESTATE_AGENT));
            listHouseTypes = (HashMap<Long, HouseType>) getIntent().getSerializableExtra(MainActivity.HOUSES_TYPES);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.initializeRecyclerViewPointOfInterest();
        this.configureSoldHomeButton();
        this.configureViewModels();
    }

    private void configureViewPager(){
        this.runOnUiThread(() -> {
            if(photoList != null){
                PicturePagerAdapter picturePagerAdapter = new PicturePagerAdapter(getApplicationContext(), photoList, null);
                vpHousePictureDetail.setAdapter(picturePagerAdapter);
            }
        });
    }

    private void configureSoldHomeButton() {
        fabHomeSold.setOnClickListener(v -> {
            if(!house.getState().equals(STATE_SOLD)){
                realEstateViewModel.updateSoldDate(new Date().getTime(), house.getIdHouse(), STATE_SOLD);
                Toast.makeText(this,  R.string.house_has_been_sold, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,  R.string.house_is_already_sold, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit){
            Intent intent = new Intent(this, EditRealEstateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainActivity.HOUSES, house);
            bundle.putParcelable(MainActivity.ADDRESS, address);
            bundle.putParcelableArrayList(MainActivity.REAL_ESTATE_AGENT, realEstateAgents);
            bundle.putParcelableArrayList(MainActivity.ROOM_NUMBER, new ArrayList<>(listRoomNumber));
            bundle.putParcelableArrayList(MainActivity.POINT_OF_INTEREST, new ArrayList<>(listPointOfInterest));
            bundle.putParcelableArrayList("HOUSE_POINT_OF_INTEREST", (ArrayList<HousePointOfInterest>) listHousePointOfInterest);
            bundle.putParcelableArrayList(MainActivity.PHOTOS, photoList);
            bundle.putSerializable(MainActivity.HOUSES_TYPES, listHouseTypes);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
            if(resultCode == RESULT_CODE){
                Toast.makeText(this, R.string.info_success_update, Toast.LENGTH_SHORT).show();
            }
    }

    private void getPointOfInterestFromDatabase() {
        AsyncTask.execute(() -> {
            listHousePointOfInterest = realEstateViewModel.getHousePointOfInterestFromHouseId(house.getIdHouse());
            for(HousePointOfInterest housePointOfInterest : listHousePointOfInterest) listPointOfInterest.add(realEstateViewModel.getPointOfInterestFromId(housePointOfInterest.getIdPointOfInterest()));
            for(PointOfInterest pointOfInterest: listPointOfInterest) listTypePointOfInterest.put(pointOfInterest.getIdPointOfInterest(), realEstateViewModel.getTypePointOfInterest(pointOfInterest.getTypePointOfInterest()));
            updateAdapterPointOfInterest();
        });
    }

    private void updateAdapterPointOfInterest(){
        this.runOnUiThread(() -> adapterPointOfInterest.notifyDataSetChanged());
    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.realEstateViewModel.getHouseFromId(idHouse).observe(this, new Observer<House>() {
            @Override
            public void onChanged(House houseData) {
                house = houseData;
                houseType = listHouseTypes.get(house.getIdHouseType());
                initializeHouseDetail();
                if(address == null){
                    initializeDescription();
                    initializeRoomNumber();
                    initializeAddress(house.getIdAddress());
                    getPhotoFromDatabase();
                    getPointOfInterestFromDatabase();
                }
            }
        });
    }

    private void initializeRoomNumber() {
        this.realEstateViewModel.getLiveDataRoomNumberForHouse(house.getIdHouse()).observe(this, roomNumbers -> {
            listRoomNumber = new ArrayList<>(roomNumbers);
            tvKitchenDetail.setText(String.valueOf(roomNumbers.get(0).getNumber()));
            tvBathroomDetail.setText(String.valueOf(roomNumbers.get(1).getNumber()));
            tvBedroomDetail.setText(String.valueOf(roomNumbers.get(2).getNumber()));
            tvLivingRoomDetail.setText(String.valueOf(roomNumbers.get(3).getNumber()));
            tvToiletDetail.setText(String.valueOf(roomNumbers.get(4).getNumber()));
            tvCellarDetail.setText(String.valueOf(roomNumbers.get(5).getNumber()));
            tvPoolDetail.setText(String.valueOf(roomNumbers.get(6).getNumber()));
        });
    }

    public void initializeRecyclerViewPointOfInterest(){
        rvPointOfInterestDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rvPointOfInterestDetail.setLayoutManager(layoutManager1);
        adapterPointOfInterest = new AdapterPointOfInterestDetail(listPointOfInterest, listTypePointOfInterest);
        rvPointOfInterestDetail.setAdapter(adapterPointOfInterest);
    }

    private void initializeAddress(long idAddress) {
        this.realEstateViewModel.getAddress(idAddress).observe(this, addressData -> {
            address = addressData;
            if(!address.getStreet().equals("") || !address.getNumber().equals(""))
                tvStreetNumberDetail.setText(String.format(this.getString(R.string.address_street_number), address.getStreet(), address.getNumber()));
            else
                tvStreetNumberDetail.setText(R.string.street_not_specified);

            if(!address.getDistrict().equals(""))
                tvDistrictDetail.setText(address.getDistrict());
            else
                tvDistrictDetail.setText(R.string.district_not_specified);

            if(!address.getPostCode().equals("") || !address.getCity().equals(""))
                tvPostcodeCityDetail.setText(String.format(getString(R.string.address_district_city), address.getPostCode(), address.getCity()));
            else
                tvPostcodeCityDetail.setText(R.string.city_not_specified);

            tvCountryDetail.setText(address.getCountry());

            if(!address.getAdditionalInformation().equals(""))
                tvAdditionnalInformationDetail.setText(address.getAdditionalInformation());
            else
                tvAdditionnalInformationDetail.setText(R.string.no_add_information);
        });

    }

    private void initializeDescription() {
        if(house.getDescription().equals(""))
            tvDescriptionContentDetail.setText(R.string.description_not_specified);
        else
            tvDescriptionContentDetail.setText(house.getDescription());
    }

    private void initializeHouseDetail() {
        if(houseType != null)
            tvTypeDetail.setText(houseType.getHouseType());
        else
            tvTypeDetail.setText(R.string.house_type_not_specified);

        if(house.getSurface() == 0)
            tvSurfaceDetail.setText(String.valueOf(house.getSurface()));
        else
            tvSurfaceDetail.setText(R.string.house_surface_not_specified);

        realEstateViewModel.getHouseSate(house.getIdHouse()).observe(this, houseDateState -> {
            if(houseDateState.getState().equals(FormActivity.STATE_AVAILABLE))
                if(house.getAvailableDate() == 0)
                    tvStateDetail.setText(R.string.availability_date_not_specified);
                else
                    tvStateDetail.setText(String.format(getString(R.string.available_for), TypeConverter.convertDateToString(DateConverter.toDate(house.getAvailableDate()))));
            else
                tvStateDetail.setText(String.format(getString(R.string.sold), TypeConverter.convertDateToString(DateConverter.toDate(houseDateState.getSoldDate()))));

        });
        tvRealEstateAgentDetail.setText(String.format(getString(R.string.real_estate_agent_name), realEstateAgents.get((int) house.getIdRealEstateAgent()-1).getName(),  realEstateAgents.get((int) house.getIdRealEstateAgent()-1).getFirstname()));
    }

    private void getPhotoFromDatabase(){
        AsyncTask.execute(() -> {
            photoList = (ArrayList<Photo>) realEstateViewModel.getPhotoFromIdHouse(house.getIdHouse());
            configureViewPager();
        });
    }
}
