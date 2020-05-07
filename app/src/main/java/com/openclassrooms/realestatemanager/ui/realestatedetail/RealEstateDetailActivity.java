package com.openclassrooms.realestatemanager.ui.realestatedetail;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.openclassrooms.realestatemanager.ui.realestateform.AdapterPointOfInterest;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.SharedViewModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateDetailActivity extends AppCompatActivity {

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

    private House house;
    private Address address;
    private ArrayList<Photo> photoList;
    private HouseType houseType;
    private List<RealEstateAgent> realEstateAgents;
    private List<RoomNumber> listRoomNumber;
    private List<PointOfInterest> listPointOfInterest = new ArrayList<>();
    private HashMap<Long, TypePointOfInterest> listTypePointOfInterest = new HashMap<>();
    private List<HousePointOfInterest> listHousePointOfInterest = new ArrayList<>();
    private AdapterPointOfInterestDetail adapterPointOfInterest;
    private RealEstateViewModel realEstateViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_detail);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            house = extras.getParcelable(MainActivity.HOUSES);
            address = extras.getParcelable(MainActivity.ADDRESS);
            houseType = extras.getParcelable(MainActivity.HOUSES_TYPES);
            photoList = extras.getParcelableArrayList(MainActivity.PHOTOS);
            realEstateAgents = extras.getParcelableArrayList(MainActivity.REAL_ESTATE_AGENT);
            listRoomNumber = extras.getParcelableArrayList(MainActivity.ROOM_NUMBER);
        }
        this.configureViewModels();
        this.initializeHouseDetail();
        this.initializeRoomNumber();
        this.initializeDescription();
        this.initializeAddress();
        this.initializeRecyclerViewPointOfInterest();
        this.getPointsOfInterest();
    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
    }

    private void getPointsOfInterest() {
        GetDataFromDatabaseAsyncTask dataFromDatabaseAsyncTask = new GetDataFromDatabaseAsyncTask(this);
        dataFromDatabaseAsyncTask.execute();
    }

    public void initializeRecyclerViewPointOfInterest(){
        rvPointOfInterestDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rvPointOfInterestDetail.setLayoutManager(layoutManager1);
        adapterPointOfInterest = new AdapterPointOfInterestDetail(listPointOfInterest, listTypePointOfInterest);
        rvPointOfInterestDetail.setAdapter(adapterPointOfInterest);
    }

    private void initializeAddress() {
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
    }

    private void initializeDescription() {
        if(house.getDescription().equals(""))
            tvDescriptionContentDetail.setText(R.string.description_not_specified);
        else
            tvDescriptionContentDetail.setText(house.getDescription());
    }

    private void initializeRoomNumber() {
        tvKitchenDetail.setText(String.valueOf(listRoomNumber.get(0).getNumber()));
        tvBathroomDetail.setText(String.valueOf(listRoomNumber.get(1).getNumber()));
        tvBedroomDetail.setText(String.valueOf(listRoomNumber.get(2).getNumber()));
        tvLivingRoomDetail.setText(String.valueOf(listRoomNumber.get(3).getNumber()));
        tvToiletDetail.setText(String.valueOf(listRoomNumber.get(4).getNumber()));
        tvCellarDetail.setText(String.valueOf(listRoomNumber.get(4).getNumber()));
        tvPoolDetail.setText(String.valueOf(listRoomNumber.get(6).getNumber()));
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

        if(house.getState().equals(FormActivity.STATE_AVAILABLE))
            if(house.getAvailableDate() == 0)
                tvStateDetail.setText(R.string.availability_date_not_specified);
            else
                tvStateDetail.setText(String.format(getString(R.string.available_for), TypeConverter.convertDateToString(DateConverter.toDate(house.getAvailableDate()))));
        else
            tvStateDetail.setText(String.format(getString(R.string.sold), TypeConverter.convertDateToString(DateConverter.toDate(house.getSoldDate()))));

        tvRealEstateAgentDetail.setText(String.format(getString(R.string.real_estate_agent_name), realEstateAgents.get((int) house.getIdRealEstateAgent()).getName(),  realEstateAgents.get((int) house.getIdRealEstateAgent()).getFirstname()));
    }
    private static class GetDataFromDatabaseAsyncTask extends AsyncTask<String, Void, String> {

        WeakReference<RealEstateDetailActivity> weakReference;

        GetDataFromDatabaseAsyncTask(RealEstateDetailActivity realEstateDetailActivity) {
            this.weakReference = new WeakReference<>(realEstateDetailActivity);
        }
        @Override
        protected String doInBackground(String... strings) {
            weakReference.get().listHousePointOfInterest = weakReference.get().realEstateViewModel.getHousePointOfInterestFromHouseId(weakReference.get().house.getIdHouse());

            for(HousePointOfInterest housePointOfInterest : weakReference.get().listHousePointOfInterest){
                weakReference.get().listPointOfInterest.add(weakReference.get().realEstateViewModel.getPointOfInterestFromId(housePointOfInterest.getIdPointOfInterest()));
            }

            for(PointOfInterest pointOfInterest: weakReference.get().listPointOfInterest){
                weakReference.get().listTypePointOfInterest.put(pointOfInterest.getIdPointOfInterest(), weakReference.get().realEstateViewModel.getTypePointOfInterest(pointOfInterest.getTypePointOfInterest()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            weakReference.get().adapterPointOfInterest.notifyDataSetChanged();
        }
    }

}
