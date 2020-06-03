package com.openclassrooms.realestatemanager.ui.realestateedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.EditRealEstateFragmentBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.tools.DataConverter;
import com.openclassrooms.realestatemanager.tools.DateConverter;
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestateform.AdapterPointOfInterest;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EditRealEstateFragment extends Fragment implements AdapterPointOfInterest.OnPointOfInterestClickListener, AdapterPicturesHouseEdit.PhotoOnClickListener{
    public final static int PLACE_REQUEST = 1;
    public final static int PLACE_RETRIEVED = -1;
    public final static int LOAD_IMG_REQUEST = 2;
    public final static int LOAD_VIDEO_REQUEST = 3;

    private Context context;
    private House house;
    private Address address;
    private long availabilityDate;
    private List<Photo> listPhoto;
    private List<Photo> listPhotoDisplayed;
    private List<Photo> listPhotoToRemove;
    private List<Photo> listPhotoToAdd;
    private List<RealEstateAgent> listRealEstateAgent;
    private List<RoomNumber> listRoomNumber;
    private List<PointOfInterest> listPointOfInterest;
    private List<PointOfInterest> listPointOfInterestToRemove;
    private List<PointOfInterest> listPointOfInterestToAdd;
    private List<PointOfInterest> listPointOfInterestToDisplay;
    private List<HousePointOfInterest> listHousePointOfInterest;
    private List<Room> listRoom;
    private HashMap<Long, HouseType> hashMapHouseTypes;
    private EditRealEstateFragmentBinding binding;
    private DateFormat df;
    private RecyclerView recyclerViewPointOfInterest;
    private RecyclerView recyclerViewPhoto;
    private AdapterPointOfInterest adapterPointOfInterest;
    private AdapterPicturesHouseEdit adapterPicturesHouse;
    private ExecutorService executorService;
    private Place place;
    private RealEstateViewModel realEstateViewModel;
    private RetrofitViewModel retrofitViewModel;
    private boolean removeVideo;
    private String videoPathToRemove;

    @Override
    @SuppressWarnings("all")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null) context = getActivity();
        Places.initialize(getActivity().getApplicationContext(), BuildConfig.API_KEY);
        executorService = Executors.newCachedThreadPool();
        Places.createClient(context);
        if(getArguments() != null){
            house = getArguments().getParcelable(MainActivity.HOUSES);
            address = getArguments().getParcelable(MainActivity.ADDRESS);
            listRealEstateAgent = new ArrayList<>(getArguments().getParcelableArrayList(MainActivity.REAL_ESTATE_AGENT));
            listRoomNumber = new ArrayList<>(getArguments().getParcelableArrayList(MainActivity.ROOM_NUMBER));
            listPointOfInterest = new ArrayList<>(getArguments().getParcelableArrayList(MainActivity.POINT_OF_INTEREST));
            listHousePointOfInterest = new ArrayList<>(getArguments().getParcelableArrayList(MainActivity.HOUSE_POINT_OF_INTEREST));
            listPhoto = new ArrayList<>(getArguments().getParcelableArrayList(MainActivity.PHOTOS));
            hashMapHouseTypes = (HashMap<Long, HouseType>) getArguments().getSerializable(MainActivity.HOUSES_TYPES);
        }
        configureViewModels();
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        listPointOfInterestToRemove = new ArrayList<>();
        listPointOfInterestToAdd = new ArrayList<>();
        listPointOfInterestToDisplay = new ArrayList<>(listPointOfInterest);
        listPhotoToRemove = new ArrayList<>();
        listPhotoToAdd = new ArrayList<>();
        listPhotoDisplayed = new ArrayList<>(listPhoto);
        removeVideo = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditRealEstateFragmentBinding.inflate(inflater, container, false);
        fillHouseContent();
        fillNumberRoomDropDownMenus();
        fillAddressAndAvailabilityDate();
        fillDropDownRealEstateAgentMenu();
        configureButtonAvailibilityDate();
        configureButtonAddPointOfInterest();
        configureButtonAddPictures();
        configureButtonAddVideo();
        configureRecyclerViewPointOfInterest();
        getRoomFromDatabase();
        return binding.getRoot();
    }

    private void getRoomFromDatabase() {
        realEstateViewModel.getRoomSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Room>>() {

                    @Override
                    public void onSuccess(List<Room> rooms) {
                        listRoom = new ArrayList<>(rooms);
                        configureRecyclerViewPhoto();
                    }

                    @Override
                    public void onError(Throwable e) { }
                });
    }

    /**
     * Configure recycler view to display the known house's point of interest
     */
    public void configureRecyclerViewPointOfInterest(){
        recyclerViewPointOfInterest = binding.rvPointOfInterest;
        recyclerViewPointOfInterest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRecyclerViewPointOfInterest = new LinearLayoutManager(context);
        recyclerViewPointOfInterest.setLayoutManager(layoutManagerRecyclerViewPointOfInterest);
        adapterPointOfInterest = new AdapterPointOfInterest(listPointOfInterestToDisplay, this);
        recyclerViewPointOfInterest.setAdapter(adapterPointOfInterest);
    }

    /**
     * Configure recycler view to display the known house's point of interest
     */
    public void configureRecyclerViewPhoto(){
        recyclerViewPhoto = binding.rvHousePicture;
        recyclerViewPhoto.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRecyclerViewPhoto = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPhoto.setLayoutManager(layoutManagerRecyclerViewPhoto);
        adapterPicturesHouse = new AdapterPicturesHouseEdit(context, listPhotoDisplayed, listRoom, TypeConverter.listToTableRoom(listRoom), this);
        recyclerViewPhoto.setAdapter(adapterPicturesHouse);
    }

    /**
     * Configure button to set availability date
     */
    private void configureButtonAvailibilityDate() {
        binding.btAddAvailibilityDate.setOnClickListener(v -> showDatePickerDialog());
    }

    /**
     * Show the MaterialDatePicker and show the availability date
     * in the availabilityDate textview and set the availability date
     * with the date retrived (long format)
     */
    @SuppressWarnings("all")
    private void showDatePickerDialog(){
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(getActivity().getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            binding.tietAvailibilityDate.setText(df.format(datePicker.getSelection()));
            availabilityDate = (long) selection;
        });
    }
    /**
     * Fill the form with the known house's info
     * Setted infos : price, surface, description, house type
     */
    private void fillHouseContent() {
        if(house.getPrice() != -1)
            binding.tvHousePrice.setText(Utils.formatDisplayedPrice(house.getPrice()));
        else
            binding.tvHousePrice.setText(R.string.non_specified);

        if(house.getSurface() != -1)
            binding.tvHouseSurface.setText(String.format(getString(R.string.number_format_double), house.getSurface()));
        else
            binding.tvHouseSurface.setText(R.string.non_specified);

        if(house.getDescription().equals(""))
            binding.edDescriptionHouse.setText(R.string.unspecified_description);
        else
            binding.edDescriptionHouse.setText(house.getDescription());

        // Fill the House's Types dropDownMenu
        List<HouseType> listHouseTypes = new ArrayList<>();
        for(Long key : hashMapHouseTypes.keySet()){
            listHouseTypes.add(hashMapHouseTypes.get(key));
        }
        String[] houseType = TypeConverter.houseTypeToStringArray(listHouseTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, houseType);
        binding.tvHouseType.setAdapter(adapter);
        binding.tvHouseType.setText(houseType[(int) house.getIdHouseType()-1], false);
        if(house.getVideoPath() != null)
            binding.tvAddVideo.setText(house.getVideoPath());
    }

    /**
     * Fill the form with the number of house's rooms known
     */
    private void fillNumberRoomDropDownMenus() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, FormActivity.numberRoom);

        binding.actvNumberKitchen.setAdapter(adapter);
        binding.actvNumberBathroom.setAdapter(adapter);
        binding.actvNumberBedroom.setAdapter(adapter);
        binding.actvNumberLivingroom.setAdapter(adapter);
        binding.actvNumberToilet.setAdapter(adapter);
        binding.actvNumberCellar.setAdapter(adapter);
        binding.actvNumberPool.setAdapter(adapter);

        binding.actvNumberKitchen.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(0).getNumber()), false);
        binding.actvNumberBathroom.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(1).getNumber()), false);
        binding.actvNumberBedroom.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(2).getNumber()), false);
        binding.actvNumberLivingroom.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(3).getNumber()), false);
        binding.actvNumberToilet.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(4).getNumber()), false);
        binding.actvNumberCellar.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(5).getNumber()), false);
        binding.actvNumberPool.setText(String.format(getString(R.string.number_format_room), listRoomNumber.get(6).getNumber()), false);
    }

    /**
     * Fill the form with the known house's address
     * Setted infos : street, number, district, city, postcode, additionalInformation, availabilityDate
     */
    private void fillAddressAndAvailabilityDate() {
        if(address != null){
            if(address.getStreet().equals(""))
                binding.tvStreet.setText(R.string.unspecified);
            else
                binding.tvStreet.setText(address.getStreet());

            if(address.getNumber().equals(""))
                binding.tvNumber.setText(R.string.unspecified);
            else
                binding.tvNumber.setText(address.getNumber());

            if(address.getDistrict().equals(""))
                binding.tvDistrict.setText(R.string.unspecified);
            else
                binding.tvDistrict.setText(address.getDistrict());

            if(address.getCity().equals(""))
                binding.tvCity.setText(R.string.unspecified);
            else
                binding.tvCity.setText(address.getCity());

            if(address.getPostCode().equals(""))
                binding.tvPostcode.setText(R.string.unspecified);
            else
                binding.tvPostcode.setText(address.getPostCode());

            if(address.getAdditionalInformation().equals(""))
                binding.tvAdditionalInformation.setText(R.string.unspecified);
            else
                binding.tvAdditionalInformation.setText(address.getAdditionalInformation());
        }

        if(house.getAvailableDate() > 0){
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.tietAvailibilityDate.setText(df.format(DateConverter.toDate(house.getAvailableDate())));
        }else{
            binding.tietAvailibilityDate.setText(R.string.date_unspecified);
        }
    }

    /**
     * Fill the dropdownmenu of realEstateAgent in the layout
     * with the ones in the database.
     * Set the selected realestateagent with the one in the house object (if exist)
     */
    private void fillDropDownRealEstateAgentMenu() {
        String[] realEstateAgents = TypeConverter.realEstateAgentToStringArray(listRealEstateAgent);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, realEstateAgents);
        binding.atvRealEstateAgent.setText(realEstateAgents[(int) house.getIdRealEstateAgent()-1]);
        binding.atvRealEstateAgent.setAdapter(adapter);
    }

    /**
     * Remove the point of interest that is selected by the user in the recycler view
     * @param position The position of the Point Of Interest in the listPointOfInterest
     */
    @Override
    public void onPointOfInterestClickListener(int position) {
        PointOfInterest pointOfInterestToRemove = listPointOfInterestToDisplay.get(position);
        if(listPointOfInterest.contains(pointOfInterestToRemove)){
            listPointOfInterestToRemove.add(pointOfInterestToRemove);
            listPointOfInterest.remove(pointOfInterestToRemove);
        }
        listPointOfInterestToDisplay.remove(position);
        adapterPointOfInterest.notifyItemRemoved(position);
        Toast.makeText(context, R.string.point_of_interest_succesfully_removed, Toast.LENGTH_SHORT).show();
    }

    /**
     * Configure the button add point of interest
     * When the button is clicked, the method lunch the Place Activity
     */
    private void configureButtonAddPointOfInterest() {
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(context);
        binding.btAddPointOfInterest.setOnClickListener(v -> startActivityForResult(intent, PLACE_REQUEST));
    }

    private void configureButtonAddPictures() {
        binding.btAddPictures.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,getString(R.string.select_picture)), LOAD_IMG_REQUEST);
        });
    }

    private void configureButtonAddVideo() {
        binding.btAddVideo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Video"), LOAD_VIDEO_REQUEST);
        });
    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(context);
        this.realEstateViewModel = new ViewModelProvider(getActivity(), mViewModelFactory).get(RealEstateViewModel.class);
        this.retrofitViewModel = new ViewModelProvider(getActivity(), mViewModelFactory).get(RetrofitViewModel.class);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_REQUEST) {
            if (resultCode == PLACE_RETRIEVED) {
                place = Autocomplete.getPlaceFromIntent(data);
                Callable<Integer> insertCallable = () -> realEstateViewModel.getidTypePointOfInterest(Autocomplete.getPlaceFromIntent(data).getName());
                Future<Integer> future = executorService.submit(insertCallable);
                try {
                    if (future.get() != null) {
                        addPointOfInterest(future.get());
                    } else {
                        insertNewTypePointOfInterest();
                    }
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(context, R.string.error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == LOAD_IMG_REQUEST && data != null){
            if(null != data.getClipData()) {
                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                    createNewPhoto(data.getClipData().getItemAt(i).getUri());
                }
            } else if(data.getData() != null){
                createNewPhoto(data.getData());
            }
            adapterPicturesHouse.notifyDataSetChanged();
        }else if(requestCode == LOAD_VIDEO_REQUEST && data != null){
            String uri = data.getData().toString();
            if(!removeVideo){
                removeVideo = true;
                videoPathToRemove = house.getVideoPath();
            }
            house.setVideoPath(uri);
            binding.tvAddVideo.setText(uri);
        }
    }

    public void createNewPhoto(Uri uri){
        Bitmap bitmap = DataConverter.convertUriToBitmap(uri, context);
        String childPath = house.getIdHouse() + "" + listPhotoDisplayed.size()+1 + ".jpg";
        String path = ImageUtils.saveToInternalStorage(childPath, bitmap, context, ImageUtils.HOUSE_PICTURES);
        Photo photo = new Photo();
        photo.setIdHouse(house.getIdHouse());
        photo.setChildPath(childPath);
        photo.setPath(path);
        listPhotoToAdd.add(photo);
        listPhotoDisplayed.add(photo);
        adapterPicturesHouse.notifyDataSetChanged();
    }

    private void addPointOfInterest(int id){
        PointOfInterest pointOfInterest = new PointOfInterest(id, place.getAddress(), place.getName());
        listPointOfInterestToDisplay.add(pointOfInterest);
        listPointOfInterestToAdd.add(pointOfInterest);
        adapterPointOfInterest.notifyDataSetChanged();
    }

    /**
     * Insert into the database a new type point of interest (School, supermarket, ...)
     */
    private void insertNewTypePointOfInterest() {
        int id;
        Callable<Long> insertCallable = () -> realEstateViewModel.insertTypePointOfInterest(new TypePointOfInterest(TypeConverter.convertPlaceTypeString(place.getTypes().get(0).toString())));
        Future<Long> future = executorService.submit(insertCallable);
        try {
            id = future.get().intValue();
            addPointOfInterest(id);
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onDeletePhotoClickListener(int position) {
        Photo photoToRemove = listPhotoDisplayed.get(position);
        if(listPhoto.contains(photoToRemove)) listPhoto.remove(photoToRemove);
        listPhotoToRemove.add(photoToRemove);
        listPhotoDisplayed.remove(position);
        adapterPicturesHouse.notifyItemRemoved(position);
        Toast.makeText(context, R.string.photo_successfully_removed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetAsDefaultPictureListener(int position) {
        Photo photoSelected = listPhotoDisplayed.get(position);
        for(Photo photoDisplayed : listPhotoDisplayed){
            if(photoDisplayed.equals(photoSelected)) photoDisplayed.setMainPicture(true);
            else photoDisplayed.setMainPicture(false);
        }

        for(Photo photo : listPhoto){
            if(photo.equals(photoSelected)) photo.setMainPicture(true);
            else photo.setMainPicture(false);
        }

        for(Photo photoToAdd : listPhotoToAdd){
            if(photoToAdd.equals(photoSelected)) photoToAdd.setMainPicture(true);
            else photoToAdd.setMainPicture(false);
        }
        Toast.makeText(context, R.string.picture_set_default, Toast.LENGTH_SHORT).show();
        adapterPicturesHouse.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
    }


}
