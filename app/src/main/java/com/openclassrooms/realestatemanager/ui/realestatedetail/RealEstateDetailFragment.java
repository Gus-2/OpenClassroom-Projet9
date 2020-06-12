package com.openclassrooms.realestatemanager.ui.realestatedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.RealEstateDetailContentBinding;
import com.openclassrooms.realestatemanager.databinding.RealEstateDetailFragmentBinding;
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
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestate.PicturePagerAdapter;
import com.openclassrooms.realestatemanager.ui.realestateedit.EditRealEstateActivity;
import com.openclassrooms.realestatemanager.ui.realestateform.FragmentFormAddRealEstate;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class RealEstateDetailFragment extends Fragment {
    private final static int REQUEST_CODE = 20;
    private final static int RESULT_CODE = 30;
    private final static String STATE_SOLD = "Sold";

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
    private PicturePagerAdapter picturePagerAdapter;
    private RealEstateViewModel realEstateViewModel;
    private SimpleExoPlayer player;
    private RealEstateDetailFragmentBinding binding;
    private RealEstateDetailContentBinding realEstateDetailContentBinding;
    private Context context;
    private CollapsingToolbarLayout collapsingToolbar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    @SuppressWarnings("all")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null) context = getActivity();
        if(getArguments() != null){
            idHouse = getArguments().getLong(RealEstateDetailActivity.ID_HOUSE);
            realEstateAgents = new ArrayList<>(getArguments().getParcelableArrayList(RealEstateDetailActivity.REAL_ESTATE_AGENT_LIST));
            listHouseTypes = new HashMap<>((HashMap<Long, HouseType>)getArguments().getSerializable(RealEstateDetailActivity.HOUSE_TYPE_HASH_MAP));
        }
        photoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RealEstateDetailFragmentBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        realEstateDetailContentBinding = binding.layoutIncludedRealEstateDetailFragment;
        player = new SimpleExoPlayer.Builder(context).build();
        collapsingToolbar = binding.tbCollapsing;
        this.configureViewPager();
        this.initializeRecyclerViewPointOfInterest();
        this.configureSoldHomeButton();
        this.configureViewModels();
        this.setFragmentMenu();

        return binding.getRoot();
    }

    private void configureViewPager(){
        picturePagerAdapter = new PicturePagerAdapter(getContext(), photoList, null);
        binding.vpRealEstateDetail.setAdapter(picturePagerAdapter);
    }

    private void setFragmentMenu(){
        binding.toolbarDetailActivity.inflateMenu(R.menu.menu_item_edit);
        binding.toolbarDetailActivity.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit) {
                if(house.getState().equals(STATE_SOLD)){
                    Toast.makeText(context, R.string.unchange_house_sold, Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    Intent intent = new Intent(getActivity(), EditRealEstateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.HOUSES, house);
                    bundle.putParcelable(MainActivity.ADDRESS, address);
                    bundle.putParcelableArrayList(MainActivity.REAL_ESTATE_AGENT, realEstateAgents);
                    bundle.putParcelableArrayList(MainActivity.ROOM_NUMBER, new ArrayList<>(listRoomNumber));
                    bundle.putParcelableArrayList(MainActivity.POINT_OF_INTEREST, new ArrayList<>(listPointOfInterest));
                    bundle.putParcelableArrayList(MainActivity.HOUSE_POINT_OF_INTEREST, (ArrayList<HousePointOfInterest>) listHousePointOfInterest);
                    bundle.putParcelableArrayList(MainActivity.PHOTOS, photoList);
                    bundle.putSerializable(MainActivity.HOUSES_TYPES, listHouseTypes);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE);
                    return true;
                }
            }
            return false;
        });
    }

    private void configureSoldHomeButton() {
        binding.fabHomeSold.setOnClickListener(v -> {
            if(!house.getState().equals(STATE_SOLD)){
                realEstateViewModel.updateSoldDate(new Date().getTime(), house.getIdHouse(), STATE_SOLD);
                Toast.makeText(getActivity(),  R.string.house_has_been_sold, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(),  R.string.house_is_already_sold, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
            if(resultCode == RESULT_CODE){
                Toast.makeText(getActivity(), R.string.info_success_update, Toast.LENGTH_SHORT).show();
            }
    }

    @SuppressWarnings("all")
    private void getPointOfInterestFromDatabase() {
        compositeDisposable.add(realEstateViewModel.getFlowableHousePointOfInterestFromHouseId(house.getIdHouse())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSubscriber<List<HousePointOfInterest>>() {
                    @Override
                    public void onNext(List<HousePointOfInterest> housePointOfInterestList) {
                        listHousePointOfInterest.clear();
                        listHousePointOfInterest = new ArrayList<>(housePointOfInterestList);
                        listPointOfInterest.clear();
                        for(HousePointOfInterest housePointOfInterest : listHousePointOfInterest)
                            listPointOfInterest.add(realEstateViewModel.getPointOfInterestFromId(housePointOfInterest.getIdPointOfInterest()));
                        for(PointOfInterest pointOfInterest: listPointOfInterest)
                            listTypePointOfInterest.put(pointOfInterest.getIdPointOfInterest(), realEstateViewModel.getTypePointOfInterest(pointOfInterest.getTypePointOfInterest()));

                        Handler mainHandler = new Handler(context.getMainLooper());
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {updateAdapterPointOfInterest();} // This is your code
                        };

                        mainHandler.post(myRunnable);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() { }
                })
        );
    }

    private void updateAdapterPointOfInterest(){
        adapterPointOfInterest.notifyDataSetChanged();
    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(getActivity());
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.realEstateViewModel.getHouseFromId(idHouse).observe(getViewLifecycleOwner(), houseData -> {
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
        });
    }

    private void initializeRoomNumber() {
        this.realEstateViewModel.getLiveDataRoomNumberForHouse(house.getIdHouse()).observe(getViewLifecycleOwner(), roomNumbers -> {
            listRoomNumber = new ArrayList<>(roomNumbers);
            realEstateDetailContentBinding.tvNumberKitchenDetail.setText(String.valueOf(roomNumbers.get(0).getNumber()));
            realEstateDetailContentBinding.tvNumberBathroomsDetail.setText(String.valueOf(roomNumbers.get(1).getNumber()));
            realEstateDetailContentBinding.tvNumberBedroomDetail.setText(String.valueOf(roomNumbers.get(2).getNumber()));
            realEstateDetailContentBinding.tvNumberLivingRoomDetail.setText(String.valueOf(roomNumbers.get(3).getNumber()));
            realEstateDetailContentBinding.tvNumberToiletDetail.setText(String.valueOf(roomNumbers.get(4).getNumber()));
            realEstateDetailContentBinding.tvNumberCellarDetail.setText(String.valueOf(roomNumbers.get(5).getNumber()));
            realEstateDetailContentBinding.tvNumberPoolDetail.setText(String.valueOf(roomNumbers.get(6).getNumber()));
        });
    }

    private void initializeRecyclerViewPointOfInterest(){
        realEstateDetailContentBinding.rvPointOfInterestDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context);
        realEstateDetailContentBinding.rvPointOfInterestDetail.setLayoutManager(layoutManager1);
        adapterPointOfInterest = new AdapterPointOfInterestDetail(listPointOfInterest);
        realEstateDetailContentBinding.rvPointOfInterestDetail.setAdapter(adapterPointOfInterest);
    }

    private void initializeAddress(long idAddress) {
        this.realEstateViewModel.getAddress(idAddress).observe(getViewLifecycleOwner(), addressData -> {
            address = addressData;
            if(!address.getStreet().equals("") || !address.getNumber().equals(""))
                realEstateDetailContentBinding.tvStreetNumberDetail.setText(String.format(this.getString(R.string.address_street_number), address.getStreet(), address.getNumber()));
            else
                realEstateDetailContentBinding.tvStreetNumberDetail.setText(R.string.street_not_specified);

            if(!address.getDistrict().equals(""))
                realEstateDetailContentBinding.tvDistrictDetail.setText(address.getDistrict());
            else
                realEstateDetailContentBinding.tvDistrictDetail.setText(R.string.district_not_specified);

            if(!address.getPostCode().equals("") || !address.getCity().equals(""))
                realEstateDetailContentBinding.tvPostcodeCityDetail.setText(String.format(getString(R.string.address_district_city), address.getPostCode(), address.getCity()));
            else
                realEstateDetailContentBinding.tvPostcodeCityDetail.setText(R.string.city_not_specified);

            realEstateDetailContentBinding.tvCountryDetail.setText(address.getCountry());

            if(!address.getAdditionalInformation().equals(""))
                realEstateDetailContentBinding.tvAdditionnalInformationDetail.setText(address.getAdditionalInformation());
            else
                realEstateDetailContentBinding.tvAdditionnalInformationDetail.setText(R.string.no_add_information);
        });
    }

    private void initializeDescription() {
        if(house.getDescription().equals(""))
            realEstateDetailContentBinding.tvDescriptionContentDetail.setText(R.string.description_not_specified);
        else
            realEstateDetailContentBinding.tvDescriptionContentDetail.setText(house.getDescription());
    }

    private void initializeHouseDetail() {
        if(house.getPrice() != -1){
            collapsingToolbar.setTitle(Utils.formatDisplayedPrice(house.getPrice()));
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText);
        }

        if(houseType != null)
            realEstateDetailContentBinding.tvTypeDetail.setText(houseType.getHouseType());
        else
            realEstateDetailContentBinding.tvTypeDetail.setText(R.string.house_type_not_specified);

        if(house.getSurface() == 0)
            realEstateDetailContentBinding.tvSurfaceDetail.setText(String.valueOf(house.getSurface()));
        else
            realEstateDetailContentBinding.tvSurfaceDetail.setText(R.string.house_surface_not_specified);

        realEstateViewModel.getHouseSate(house.getIdHouse()).observe(getViewLifecycleOwner(), houseDateState -> {
            if(houseDateState.getState().equals(FragmentFormAddRealEstate.STATE_AVAILABLE))
                if(house.getAvailableDate() == 0)
                    realEstateDetailContentBinding.tvStateDetail.setText(R.string.availability_date_not_specified);
                else
                    realEstateDetailContentBinding.tvStateDetail.setText(String.format(getString(R.string.available_for), TypeConverter.convertDateToString(Utils.toDate(house.getAvailableDate()))));
            else
                realEstateDetailContentBinding.tvStateDetail.setText(String.format(getString(R.string.sold), TypeConverter.convertDateToString(Utils.toDate(houseDateState.getSoldDate()))));

        });
        realEstateDetailContentBinding.tvRealEstateAgentDetail.setText(String.format(getString(R.string.real_estate_agent_name), realEstateAgents.get((int) house.getIdRealEstateAgent()-1).getName(),  realEstateAgents.get((int) house.getIdRealEstateAgent()-1).getFirstname()));

        if(house.getChildPathPlacePreview() != null){
            realEstateDetailContentBinding.cvMapDetail.setVisibility(View.VISIBLE);
            realEstateDetailContentBinding.ivMapDetail.setImageBitmap(ImageUtils.loadImageFromStorage(house.getParentPathPlacePreview(), house.getChildPathPlacePreview()));
        }else{
            realEstateDetailContentBinding.cvMapDetail.setVisibility(View.GONE);
        }

        if(house.getVideoPath() != null){
            binding.layoutIncludedRealEstateDetailFragment.cvVideoDetail.setVisibility(View.VISIBLE);
            File file = new File(house.getVideoPath());
            Uri localUri = Uri.fromFile(file);
            initializePlayer(localUri);
        }else{
            binding.layoutIncludedRealEstateDetailFragment.cvVideoDetail.setVisibility(View.GONE);
        }
    }

    private void initializePlayer(Uri uri){
        realEstateDetailContentBinding.exoPlayerVideoDetail.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getApplicationInfo().loadLabel(context.getPackageManager()).toString()));
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
        player.prepare(videoSource);
    }

    private void getPhotoFromDatabase(){
        compositeDisposable.add(realEstateViewModel.getListPhotoFlowableFromIdHouse(house.getIdHouse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Photo>>() {
                    @Override
                    public void onNext(List<Photo> photos) {
                        photoList.clear();
                        photoList.addAll(photos);
                        updateViewPagerPhoto();
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() { }
                })
        );
    }

    private void updateViewPagerPhoto(){
        picturePagerAdapter = null;
        configureViewPager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(compositeDisposable != null && compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
