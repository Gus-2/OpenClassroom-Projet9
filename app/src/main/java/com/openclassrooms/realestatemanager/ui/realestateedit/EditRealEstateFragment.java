package com.openclassrooms.realestatemanager.ui.realestateedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestateform.AdapterPointOfInterest;
import com.openclassrooms.realestatemanager.ui.realestateform.FragmentFormAddRealEstate;
import com.openclassrooms.realestatemanager.ui.realestateform.ToolsUpdateData;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EditRealEstateFragment extends Fragment implements AdapterPointOfInterest.OnPointOfInterestClickListener, AdapterPicturesHouseEdit.PhotoOnClickListener{
    private final static int PLACE_REQUEST = 1;
    private final static int PLACE_RETRIEVED = -1;
    private final static int LOAD_IMG_REQUEST = 2;
    private final static int LOAD_VIDEO_REQUEST = 3;

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
    private List<HousePointOfInterest> listHousePointOfInterestToRemove;
    private List<Room> listRoom;
    private List<HouseType> listHouseTypes;
    private HashMap<Long, HouseType> hashMapHouseTypes;
    private EditRealEstateFragmentBinding binding;
    private DateFormat df;
    private AdapterPointOfInterest adapterPointOfInterest;
    private AdapterPicturesHouseEdit adapterPicturesHouse;
    private ExecutorService executorService;
    private Place place;
    private RealEstateViewModel realEstateViewModel;
    private RetrofitViewModel retrofitViewModel;
    private Disposable coordinatesObserver;
    private boolean removeVideo;
    private String videoPathToRemove;
    private House houseToUpdate;
    private Address addressToUpdate;
    private Bitmap newBitmapToInsert;
    private List<RoomNumber> listRoomNumberProcessed;
    private List<HousePointOfInterest> listHousePointOfInterestAdd = new ArrayList<>();
    private String childPath;
    private String parentPathPlacePreview;

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
            listHousePointOfInterestToRemove = new ArrayList<>();
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
        listHouseTypes= new ArrayList<>();
        for(Long key : hashMapHouseTypes.keySet()){
            listHouseTypes.add(hashMapHouseTypes.get(key));
        }
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
        configureButtonAddHouse();
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
    private void configureRecyclerViewPointOfInterest(){
        RecyclerView recyclerViewPointOfInterest = binding.rvPointOfInterest;
        recyclerViewPointOfInterest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRecyclerViewPointOfInterest = new LinearLayoutManager(context);
        recyclerViewPointOfInterest.setLayoutManager(layoutManagerRecyclerViewPointOfInterest);
        adapterPointOfInterest = new AdapterPointOfInterest(listPointOfInterestToDisplay, this);
        recyclerViewPointOfInterest.setAdapter(adapterPointOfInterest);
    }

    /**
     * Configure recycler view to display the known house's point of interest
     */
    private void configureRecyclerViewPhoto(){
        RecyclerView recyclerViewPhoto = binding.rvHousePicture;
        recyclerViewPhoto.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRecyclerViewPhoto = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPhoto.setLayoutManager(layoutManagerRecyclerViewPhoto);
        adapterPicturesHouse = new AdapterPicturesHouseEdit(context, listPhotoDisplayed, listPhotoToAdd, listRoom, TypeConverter.listToTableRoom(listRoom), this);
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
            binding.tvHousePrice.setText(String.format(getString(R.string.number_format_double), house.getPrice()));
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

        String[] houseType = TypeConverter.houseTypeToStringArray(listHouseTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, houseType);
        binding.tvHouseType.setAdapter(adapter);
        binding.tvHouseType.setText(houseType[(int) house.getIdHouseType()-1], false);
        if(house.getVideoPath() != null)
            binding.tvAddVideo.setText(house.getVideoPath());
        else
            binding.tvAddVideo.setText(R.string.no_video_added);
    }

    /**
     * Fill the form with the number of house's rooms known
     */
    private void fillNumberRoomDropDownMenus() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, FragmentFormAddRealEstate.numberRoom);

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
            binding.tietAvailibilityDate.setText(df.format(Utils.toDate(house.getAvailableDate())));
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
            listHousePointOfInterestToRemove.add(new HousePointOfInterest(house.getIdHouse(), pointOfInterestToRemove.getIdPointOfInterest()));
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
        binding.btAddPointOfInterest.setOnClickListener(v -> {
            if (getContext() != null && Utils.isInternetAvailable(getContext())) {
                startActivityForResult(intent, PLACE_REQUEST);
            } else {
                Toast.makeText(getContext(), "You need internet to add a point of interest !", Toast.LENGTH_LONG).show();
            }
        });
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
        if(getActivity() != null){
            this.realEstateViewModel = new ViewModelProvider(getActivity(), mViewModelFactory).get(RealEstateViewModel.class);
            this.retrofitViewModel = new ViewModelProvider(getActivity(), mViewModelFactory).get(RetrofitViewModel.class);
        }
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
            } else if(resultCode == AutocompleteActivity.RESULT_ERROR) {
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

    private void createNewPhoto(Uri uri){
        Bitmap bitmap = ImageUtils.convertUriToBitmap(uri, context);
        String childPath = house.getIdHouse() + "" + listPhotoDisplayed.size()+1 + ".jpg";
        String path = ImageUtils.saveToInternalStorage(childPath, bitmap, context, ImageUtils.HOUSE_PICTURES);
        Photo photo = new Photo();
        photo.setIdHouse(house.getIdHouse());
        photo.setChildPath(childPath);
        photo.setPath(path);
        listPhotoToAdd.add(photo);
        listPhotoDisplayed.add(photo);
        configureRecyclerViewPhoto();
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
        Callable<Long> insertCallable = () -> realEstateViewModel.insertTypePointOfInterest(new TypePointOfInterest(place.getTypes().get(0).toString()));
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
        listPhotoToRemove.add(photoToRemove);
        if(listPhotoToAdd.contains(photoToRemove)) listPhotoToAdd.remove(photoToRemove);
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

    private void configureButtonAddHouse() {
        binding.btAddProperty.setOnClickListener(v -> {

            String street = binding.tvStreet.getText().toString();
            String number = binding.tvNumber.getText().toString();
            String district = binding.tvDistrict.getText().toString();
            String city = binding.tvCity.getText().toString();
            String postCode = binding.tvPostcode.getText().toString();
            String additionalInformation = binding.tvAdditionalInformation.getText().toString();
            addressToUpdate = ToolsUpdateData.processAddress(address, street, number, district, city, postCode, additionalInformation);

            long houseTypeId = TypeConverter.getHouseTypeId(listHouseTypes, binding.tvHouseType.getText().toString());
            long idRealEstateAgent = TypeConverter.getRealEstateAgentId(listRealEstateAgent, binding.atvRealEstateAgent.getText().toString());
            String price = binding.tvHousePrice.getText().toString();
            String surface = binding.tvHouseSurface.getText().toString();
            String state = FragmentFormAddRealEstate.STATE_AVAILABLE;
            String description = binding.edDescriptionHouse.getText().toString();
            houseToUpdate = ToolsUpdateData.processHouse(house, houseTypeId, idRealEstateAgent, price, surface, description, state, availabilityDate);

            listRoomNumberProcessed = ToolsUpdateData.processRoom(house.getIdHouse(), listRoomNumber, binding.actvNumberKitchen.getText().toString(), binding.actvNumberBathroom.getText().toString(), binding.actvNumberBedroom.getText().toString(),
                    binding.actvNumberLivingroom.getText().toString(), binding.actvNumberToilet.getText().toString(), binding.actvNumberCellar.getText().toString(), binding.actvNumberPool.getText().toString());

            if(addressToUpdate != null){
                getLongitudeAndLatitudeForAddress(addressToUpdate.getNumber() + " " +  addressToUpdate.getStreet() + "," +  addressToUpdate.getDistrict() + "," + addressToUpdate.getPostCode() + "," +addressToUpdate.getCity());
            }

            if(addressToUpdate == null && houseToUpdate != null && !removeVideo)
                updateHouse();

            if(listRoomNumberProcessed != null)
                updateRoomNumber();

            if(removeVideo){
                if(videoPathToRemove != null)
                    ImageUtils.deleteFile(videoPathToRemove);
                 Executor executor = Executors.newSingleThreadExecutor();
                 executor.execute(() -> {
                     house.setVideoPath(ImageUtils.saveVideoToInternalStorage(Uri.parse(house.getVideoPath()), context));
                     updateHouse();
                 });
            }

            if(listPointOfInterestToRemove.size() > 0){
                deleteHousePointOfInterests();
            }

            if(listPointOfInterestToAdd.size() > 0){
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }

            Utils.updateNumberRoomForEachPhoto(listRoom, listPhotoToAdd, listPhotoDisplayed);

            if(listPhotoToAdd.size() > 0 || listPhotoToRemove.size() > 0){
                if(listPhotoToAdd.size() > 0){
                    insertPhoto();
                }
                if(listPhotoToRemove.size() > 0){
                    deletePhoto();
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() ->{
                        for(Photo photo : listPhotoToRemove){
                            ImageUtils.deleteFileFromStorageChildPathParentPath(photo.getPath(), photo.getChildPath());
                        }
                    });
                }
            }
            listPhotoDisplayed.removeAll(listPhotoToRemove);
            updateList();
        });
    }

    private void updateList() {
        Utils.setNumberRoomForEachPhotoList(listRoom, listPhotoDisplayed);
        realEstateViewModel.updateListPhoto(listPhotoDisplayed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        if(getActivity() != null)
        getActivity().finish();
    }

    private void deletePhoto(){
        realEstateViewModel.deleteListPhoto(listPhotoToRemove)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void insertPhoto(){
        Utils.setNumberRoomForEachPhotoList(listRoom, listPhoto);
        realEstateViewModel.insertListPhoto(listPhotoToAdd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    private void insertHousePointOfInterest(){
        realEstateViewModel.insertListHousePointOfInterest(listHousePointOfInterestAdd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            for(PointOfInterest pointOfInterest : listPointOfInterestToAdd){
               listHousePointOfInterestAdd.add(new HousePointOfInterest(house.getIdHouse(), realEstateViewModel.insertPointOfInterest(pointOfInterest)));
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            insertHousePointOfInterest();
        }
    }

    private void deleteHousePointOfInterests() {
        realEstateViewModel.deleteListHousePointOfInterest(listHousePointOfInterestToRemove)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void updateRoomNumber() {
        realEstateViewModel.updateRoomNumber(listRoomNumberProcessed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    private void getLongitudeAndLatitudeForAddress(String address) {
        coordinatesObserver = retrofitViewModel.getCoordinates(address)
                .subscribe(coordinates -> {
                    if(coordinates.getResults().size() != 0){
                        Double lat = coordinates.getResults().get(0).getGeometry().getLocation().getLat();
                        Double lon = coordinates.getResults().get(0).getGeometry().getLocation().getLng();
                        addressToUpdate.setLatitude(lat);
                        addressToUpdate.setLongitude(lon);
                        getMapPicture(lat, lon);
                    }else{
                        updateAddress();
                    }
                });
    }

    private void getMapPicture(Double lat, Double lon){
        if(getActivity() != null){
            Picasso.with(getActivity().getApplicationContext())
                    .load(String.format(FragmentFormAddRealEstate.URL, lat, lon))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            newBitmapToInsert = bitmap;
                            insertNewStaticMap();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });
        }
    }

    private void insertNewStaticMap(){
        if(getActivity() != null && newBitmapToInsert != null){
            childPath = addressToUpdate.getIdAddress() + "_map_image.jpg";
            parentPathPlacePreview = ImageUtils.saveToInternalStorage(childPath, newBitmapToInsert, getActivity().getApplicationContext(), ImageUtils.MAP_IMAGE);
            updateAddress();
        }
    }

    private void updateAddress(){
        realEstateViewModel.updateAddress(addressToUpdate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        if(!removeVideo)
                            updateHouse();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void updateHouse(){
        if(removeVideo){
            if(houseToUpdate == null && childPath != null){
                house.setChildPathPlacePreview(childPath);
                house.setParentPathPlacePreview(parentPathPlacePreview);
                houseToUpdate = house;
            } else if(houseToUpdate != null && childPath != null){
                houseToUpdate.setParentPathPlacePreview(parentPathPlacePreview);
                houseToUpdate.setChildPathPlacePreview(childPath);
                houseToUpdate.setVideoPath(house.getVideoPath());
            }else if(houseToUpdate == null){
                houseToUpdate = house;
            }else{
                houseToUpdate.setVideoPath(house.getVideoPath());
            }
        }else{
            if(houseToUpdate == null && childPath != null){
                house.setChildPathPlacePreview(childPath);
                house.setParentPathPlacePreview(parentPathPlacePreview);
                houseToUpdate = house;
            } else if(houseToUpdate != null && childPath != null){
                houseToUpdate.setParentPathPlacePreview(parentPathPlacePreview);
                houseToUpdate.setChildPathPlacePreview(childPath);
            }
        }

        realEstateViewModel.updateHouse(houseToUpdate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(coordinatesObserver != null && !coordinatesObserver.isDisposed())
            coordinatesObserver.dispose();
        executorService.shutdownNow();
    }
}
