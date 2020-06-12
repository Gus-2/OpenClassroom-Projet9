package com.openclassrooms.realestatemanager.ui.realestateform;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
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
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.BuildConfig;
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
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.models.pojo.RoomNumber;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.ref.WeakReference;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.openclassrooms.realestatemanager.tools.App.CHANNEL_1_ID;

public class FragmentFormAddRealEstate extends Fragment implements AdapterPointOfInterest.OnPointOfInterestClickListener, AdapterPicturesHouse.OnItemClickListener {

    private final static int RESULT_OK = 80;
    private final static int ERROR_RESULT = 1;
    private final static int PLACE_RETRIEVED = -1;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final static int RESULT_LOAD_IMG = 2;
    private final static int RESULT_LOAD_VIDEO = 3;
    private final static int REQUEST_IMAGE_CAPTURE = 4;
    public final static String STATE_AVAILABLE = "Available";

    private final static String COUNTRY = "United States";
    public final static Integer[] numberRoom = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @BindView(R.id.actv_number_bathroom)
    AutoCompleteTextView dropDownMenuNumberBathroom;
    @BindView(R.id.actv_number_cellar)
    AutoCompleteTextView dropDownMenuNumberCellar;
    @BindView(R.id.actv_number_bedroom)
    AutoCompleteTextView dropDownMenuNumberBedroom;
    @BindView(R.id.actv_number_kitchen)
    AutoCompleteTextView dropDownMenuNumberKitchen;
    @BindView(R.id.actv_number_livingroom)
    AutoCompleteTextView dropDownMenuNumberLivingRoom;
    @BindView(R.id.actv_number_toilet)
    AutoCompleteTextView dropDownMenuNumberToilet;
    @BindView(R.id.actv_number_pool)
    AutoCompleteTextView dropDownMenuNumberPool;
    @BindView(R.id.tv_house_type)
    AutoCompleteTextView dropDownMenuHouseType;
    @BindView(R.id.atv_real_estate_agent)
    AutoCompleteTextView dropDownMenuRealEstateAgent;

    @BindView(R.id.tiet_availibility_date)
    TextInputEditText editTextAvailibilityDate;
    @BindView(R.id.tv_house_price)
    TextInputEditText editTextPrice;
    @BindView(R.id.tv_house_surface)
    TextInputEditText editTextSurface;
    @BindView(R.id.ed_description_house)
    TextInputEditText editTextDescription;
    @BindView(R.id.tv_street)
    TextInputEditText editTextAddressStreet;
    @BindView(R.id.tv_number)
    TextInputEditText editTextAddressNumber;
    @BindView(R.id.tv_district)
    TextInputEditText editTextAddressDistrict;
    @BindView(R.id.tv_city)
    TextInputEditText editTextAddressCity;
    @BindView(R.id.tv_postcode)
    TextInputEditText editTextAddressPostCode;
    @BindView(R.id.tv_additional_information)
    TextInputEditText editTextAddressAdditionnalInformation;

    @BindView(R.id.bt_add_availibility_date)
    Button buttonAddAvailibilityDate;
    @BindView(R.id.bt_add_point_of_interest)
    Button buttonAddPointOfInterest;
    @BindView(R.id.bt_add_pictures)
    Button buttonAddPicture;
    @BindView(R.id.bt_take_pictures)
    Button buttonTakePicture;
    @BindView(R.id.bt_add_property)
    Button buttonAddProperty;

    @BindView(R.id.rv_point_of_interest)
    RecyclerView recyclerViewPointOfInterest;
    @BindView(R.id.rv_house_picture)
    RecyclerView recyclerHousePicture;
    @BindView(R.id.tv_add_video)
    TextView tvAddVideo;
    @BindView(R.id.bt_add_video)
    Button buttonAddVideo;

    public static final String URL = "https://maps.googleapis.com/maps/api/staticmap?zoom=17&size=350x300&maptype=roadmap&markers=color:red|%4.6f,%4.6f&key=AIzaSyBk5oJO4prnmEvqvzwO6koHtHDLXBlfByA";
    private ExecutorService executorService;
    private Disposable coordinatesObserver;
    private Bitmap photoMap = null;
    private AdapterPointOfInterest adapterPointOfInterest;
    private AdapterPicturesHouse adapterHousePicture;
    private RealEstateViewModel realEstateViewModel;
    private RetrofitViewModel retrofitViewModel;
    private List<HouseType> listHouseTypes;
    private List<Uri> listUri = new ArrayList<>();
    private List<PointOfInterest> listPointOfInterests;
    private List<RealEstateAgent> listRealEstateAgent;
    private List<RoomNumber> roomNumbersList = new ArrayList<>();
    private List<Room> roomList;
    private String[] tabStringRoom;
    private HashMap<Uri, Photo> hashMapUriPhoto = new HashMap<>();
    private HashMap<Uri, Bitmap> hashMapUriBitmap = new HashMap<>();
    private Place place;
    private Address newAddressToInsert;
    private House houseToInsert;
    private long availabilityDate = 0;
    private NotificationManagerCompat notificationManager;
    private Uri videoPath;
    private String pathToFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null && getContext() != null){
            notificationManager = NotificationManagerCompat.from(getContext());
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.API_KEY);
            executorService = Executors.newCachedThreadPool();
            Places.createClient(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form_add_real_estate, container, false);
        ButterKnife.bind(this, rootView);
        this.configureViewModels();

        this.configureDropDownHouseTypeMenu();
        this.configureNumberRoomDropDownMenus();
        this.configureButtonAvailibilityDate();
        this.configureButtonAddPointOfInterest();
        this.configureRecycleViewPointOfInterest();
        this.configureButtonAddHouse();
        this.configureButtonAddPictures();
        this.configureButtonTakePictures();
        this.configureButtonAddVideo();

        GetDataFromDatabase getDataFromDatabase = new GetDataFromDatabase(this);
        getDataFromDatabase.execute();
        return rootView;

    }

    private void configureButtonAddVideo() {
        buttonAddVideo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Video"), RESULT_LOAD_VIDEO);
        });
    }

    private void configureButtonAddHouse() {
        buttonAddProperty.setOnClickListener(v -> {
            createAndInsertNewData();
        });
    }

    private void createAndInsertNewData() {
        String street = ToolsUpdateData.processAddressString(editTextAddressStreet.getText().toString());
        String addressNumber = ToolsUpdateData.processAddressString(editTextAddressNumber.getText().toString());
        String district = ToolsUpdateData.processAddressString(editTextAddressDistrict.getText().toString());
        String city = ToolsUpdateData.processAddressString(editTextAddressCity.getText().toString());
        String postCode = ToolsUpdateData.processAddressString(editTextAddressPostCode.getText().toString());
        String additionalInformation = ToolsUpdateData.processAddressString(editTextAddressAdditionnalInformation.getText().toString());

        newAddressToInsert = new Address(street, addressNumber, district, city, COUNTRY,  postCode, additionalInformation);

        long idHouseTypeToInsert = TypeConverter.getHouseTypeId(listHouseTypes, dropDownMenuHouseType.getText().toString());
        long idRealEstateAgentToInsert = TypeConverter.getRealEstateAgentId(listRealEstateAgent, dropDownMenuRealEstateAgent.getText().toString());

        double price  = ToolsUpdateData.processPriceAndSurfaceString(editTextPrice.getText().toString());
        double surface = ToolsUpdateData.processPriceAndSurfaceString(editTextSurface.getText().toString());

        String description = ToolsUpdateData.processDescriptionString(editTextDescription.getText().toString());

        houseToInsert = new House(idHouseTypeToInsert, idRealEstateAgentToInsert, price, surface, description, STATE_AVAILABLE, availabilityDate);

        roomNumbersList.add(new RoomNumber(1, Integer.parseInt(dropDownMenuNumberKitchen.getText().toString())));
        roomNumbersList.add(new RoomNumber(2, Integer.parseInt(dropDownMenuNumberBathroom.getText().toString())));
        roomNumbersList.add(new RoomNumber(3, Integer.parseInt(dropDownMenuNumberBedroom.getText().toString())));
        roomNumbersList.add(new RoomNumber(4, Integer.parseInt(dropDownMenuNumberLivingRoom.getText().toString())));
        roomNumbersList.add(new RoomNumber(5, Integer.parseInt(dropDownMenuNumberToilet.getText().toString())));
        roomNumbersList.add(new RoomNumber(6, Integer.parseInt(dropDownMenuNumberCellar.getText().toString())));
        roomNumbersList.add(new RoomNumber(7, Integer.parseInt(dropDownMenuNumberPool.getText().toString())));

        getLongitudeAndLatitudeForAddress(addressNumber + " " +  street + "," +  district + "," + postCode + "," + city);
    }

    @SuppressWarnings("all")
    public void configureRecycleViewPhoto(){
        recyclerHousePicture.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerHousePicture.setLayoutManager(layoutManager2);
        ArrayList<Photo> listPhotoAlreadyExist = null;

        adapterHousePicture = new AdapterPicturesHouse(listUri, hashMapUriPhoto, hashMapUriBitmap,roomList, tabStringRoom, getActivity().getApplicationContext(), this);
        recyclerHousePicture.setAdapter(adapterHousePicture);
    }

    private void configureButtonAddPictures() {
        buttonAddPicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,getString(R.string.select_picture)), RESULT_LOAD_IMG);
        });
    }

    private void configureButtonTakePictures() {
        buttonTakePicture.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (getActivity() != null && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = ImageUtils.createPhotoFile(getActivity());
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private void configureRecycleViewPointOfInterest(){
        recyclerViewPointOfInterest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewPointOfInterest.setLayoutManager(layoutManager1);
        listPointOfInterests = new ArrayList<>();
        adapterPointOfInterest = new AdapterPointOfInterest(listPointOfInterests, this);
        recyclerViewPointOfInterest.setAdapter(adapterPointOfInterest);
    }

    private void initializeDropDownRealEstateAgent(){
        String[] realEstateAgents = TypeConverter.realEstateAgentToStringArray(listRealEstateAgent);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getActivity().getApplicationContext(), R.layout.dropdown_menu_popup_item, realEstateAgents);
        dropDownMenuRealEstateAgent.setText(realEstateAgents[0]);
        dropDownMenuRealEstateAgent.setAdapter(adapter);
    }

    /**
     * If the request code is the one used by google search places api
     * check if the place is already in database. If it isn's, it's added to it
     * and add the place to the point of interest list by calling addPointOfInterest
     * method
     * If the request is the one use by the intent that retrieve picture
     * Add uri to the list and convert the picture to a bitmap format
     * @param requestCode request code used by the new intent that is launched
     * @param resultCode code return by the intent that has been launched
     * @param data values returned by the intent that has been launched
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
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
                Toast.makeText(getContext(), R.string.error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == RESULT_LOAD_IMG && data != null){
            if(null != data.getClipData()) {
                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    Bitmap bitmap = ImageUtils.convertUriToBitmap(uri, getContext());
                    listUri.add(uri);
                    hashMapUriBitmap.put(uri, bitmap);
                    hashMapUriPhoto.put(uri, new Photo());
                }
            } else if(data.getData() != null){
                Uri uri = data.getData();
                Bitmap bitmap = ImageUtils.convertUriToBitmap(uri, getContext());
                listUri.add(uri);
                hashMapUriBitmap.put(uri, bitmap);
                hashMapUriPhoto.put(uri, new Photo());
            }
            adapterHousePicture.notifyDataSetChanged();
        } else if(requestCode == RESULT_LOAD_VIDEO && data != null){
            videoPath = data.getData();
            tvAddVideo.setText(videoPath.getPath());
            ImageUtils.saveVideoToInternalStorage(videoPath, getContext());
        }else if(requestCode == REQUEST_IMAGE_CAPTURE){
            if (resultCode == PLACE_RETRIEVED) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(pathToFile);
                Uri uri = Uri.parse(pathToFile);
                listUri.add(uri);
                hashMapUriBitmap.put(uri, imageBitmap);
                hashMapUriPhoto.put(uri, new Photo());
                adapterHousePicture.notifyDataSetChanged();
            }
        }
    }


    /**
     * Insert into the database a new point of interest
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

    private void addPointOfInterest(int id){
        listPointOfInterests.add(new PointOfInterest(id, place.getAddress(), place.getName()));
        adapterPointOfInterest.notifyDataSetChanged();
    }

    private void configureButtonAddPointOfInterest() {
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        buttonAddPointOfInterest.setOnClickListener(v -> {
            if(Utils.isInternetAvailable(getContext())){
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }else{
                Toast.makeText(getContext(), "You need an internet connection to add a point of interest !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureButtonAvailibilityDate() {
        buttonAddAvailibilityDate.setOnClickListener(v -> showDatePickerDialog());
    }

    @SuppressWarnings("unchecked")
    private void showDatePickerDialog(){
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(getActivity().getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextAvailibilityDate.setText(df.format(datePicker.getSelection()));
            availabilityDate = (long) selection;
        });
    }

    /**
     * Charge all of the drop down menu for
     * the number of rooms
     */
    private void configureNumberRoomDropDownMenus() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.dropdown_menu_popup_item, numberRoom);
        dropDownMenuNumberKitchen.setAdapter(adapter);
        dropDownMenuNumberBathroom.setAdapter(adapter);
        dropDownMenuNumberBedroom.setAdapter(adapter);
        dropDownMenuNumberLivingRoom.setAdapter(adapter);
        dropDownMenuNumberToilet.setAdapter(adapter);
        dropDownMenuNumberCellar.setAdapter(adapter);
        dropDownMenuNumberPool.setAdapter(adapter);
        dropDownMenuNumberKitchen.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberBathroom.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberBedroom.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberLivingRoom.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberToilet.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberCellar.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);
        dropDownMenuNumberPool.setText(String.format(getString(R.string.number_format_room), numberRoom[0]), false);

    }

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(getActivity());
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.retrofitViewModel = new ViewModelProvider(this, mViewModelFactory).get(RetrofitViewModel.class);
    }

    /**
     * Get house types from the database and then call
     * the initialize drop down menu method to
     * display the house types
     */
    private void configureDropDownHouseTypeMenu(){
        AsyncTask.execute(() -> {
            listHouseTypes = realEstateViewModel.getHouseType();
            initializeDropDownHouseTypeMenu();
        });
    }

    /**
     * Initialize the dropdrown menu on the main thread
     */
    private void initializeDropDownHouseTypeMenu(){
        if(getActivity() != null && getContext() != null){
            String[] houseType = TypeConverter.houseTypeToStringArray(listHouseTypes);
            ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), R.layout.dropdown_menu_popup_item, houseType);
            getActivity().runOnUiThread(() -> {
                dropDownMenuHouseType.setAdapter(adapter);
                dropDownMenuHouseType.setText(houseType[0], false);
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        if(coordinatesObserver!= null && !coordinatesObserver.isDisposed())
            coordinatesObserver.dispose();
    }

    private void showNotification(){
        if(getContext() != null && getActivity() != null){
            Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_check_blue_24dp)
                    .setContentTitle("Good news !")
                    .setContentText("The new property has been added successfully !")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .build();
            notificationManager.notify(1, notification);

            getActivity().finish();
        }
    }

    private void insertIntoDatabase(){
        InsertIntoDatabase insertIntoDatabase = new InsertIntoDatabase(this);
        insertIntoDatabase.execute();
    }

    private void getLongitudeAndLatitudeForAddress(String address) {
        coordinatesObserver = retrofitViewModel.getCoordinates(address)
                .subscribe(coordinates -> {
                    if(coordinates.getResults().size() != 0){
                        Double lat = coordinates.getResults().get(0).getGeometry().getLocation().getLat();
                        Double lon = coordinates.getResults().get(0).getGeometry().getLocation().getLng();
                        newAddressToInsert.setLatitude(lat);
                        newAddressToInsert.setLongitude(lon);
                        getMapPicture(lat, lon);
                    }else{
                        insertIntoDatabase();
                    }
                });
    }

    private void getMapPicture(Double lat, Double lon){
        Picasso.with(getActivity().getApplicationContext())
                .load(String.format(URL, lat, lon))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        photoMap = bitmap;
                        insertIntoDatabase();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) { }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });
    }

    @Override
    public void onPointOfInterestClickListener(int position) {
        listPointOfInterests.remove(position);
        adapterPointOfInterest.notifyItemRemoved(position);
    }

    @Override
    public void onDeletePhotoClickListener(int position) {
        Uri uri = listUri.get(position);
        listUri.remove(uri);
        hashMapUriBitmap.remove(uri);
        hashMapUriPhoto.remove(uri);
        Toast.makeText(getActivity().getApplicationContext(), R.string.photo_successfully_removed, Toast.LENGTH_SHORT).show();
        adapterHousePicture.notifyDataSetChanged();
    }

    @Override
    public void onSetAsDefaultPictureListener(int position) {
        for(Uri uri : listUri){
            hashMapUriPhoto.get(uri).setMainPicture(false);
        }
        hashMapUriPhoto.get(listUri.get(position)).setMainPicture(true);
        adapterHousePicture.notifyDataSetChanged();
        Toast.makeText(getActivity().getApplicationContext(), R.string.picture_set_default, Toast.LENGTH_SHORT).show();
    }

    private static class InsertIntoDatabase extends AsyncTask<Void, Void, Void>{

        WeakReference<FragmentFormAddRealEstate> weakReference;

        InsertIntoDatabase(FragmentFormAddRealEstate fragmentFormAddRealEstate) {
            this.weakReference = new WeakReference<>(fragmentFormAddRealEstate);
        }
        @Override
        protected Void doInBackground(Void... strings) {
            FragmentFormAddRealEstate fragmentFormAddRealEstate = weakReference.get();

            long idAddress = fragmentFormAddRealEstate.realEstateViewModel.insertAddress(fragmentFormAddRealEstate.newAddressToInsert);
            fragmentFormAddRealEstate.houseToInsert.setIdAddress(idAddress);

            if(fragmentFormAddRealEstate.photoMap != null){
                String childPath = idAddress + "_map_image.jpg";
                String parentPathPlacePreview = ImageUtils.saveToInternalStorage(childPath, fragmentFormAddRealEstate.photoMap, fragmentFormAddRealEstate.getActivity().getApplicationContext(), ImageUtils.MAP_IMAGE);
                fragmentFormAddRealEstate.houseToInsert.setChildPathPlacePreview(childPath);
                fragmentFormAddRealEstate.houseToInsert.setParentPathPlacePreview(parentPathPlacePreview);
            }
            if(fragmentFormAddRealEstate.videoPath != null){
                fragmentFormAddRealEstate.houseToInsert.setVideoPath(ImageUtils.saveVideoToInternalStorage(fragmentFormAddRealEstate.videoPath, fragmentFormAddRealEstate.getContext()));
                Log.d("VIDEO_PATH", fragmentFormAddRealEstate.houseToInsert.getVideoPath());
            }

            long houseId = fragmentFormAddRealEstate.realEstateViewModel.insertHouse(fragmentFormAddRealEstate.houseToInsert);

            for(PointOfInterest pointOfInterest: fragmentFormAddRealEstate.listPointOfInterests){
                long pointOfInterestId = fragmentFormAddRealEstate.realEstateViewModel.insertPointOfInterest(pointOfInterest);
                fragmentFormAddRealEstate.realEstateViewModel.insertHousePointOfInterest(new HousePointOfInterest(houseId, pointOfInterestId));
            }

            for(RoomNumber roomNumber : fragmentFormAddRealEstate.roomNumbersList){
                roomNumber.setIdHouse(houseId);
                fragmentFormAddRealEstate.realEstateViewModel.insertRoomNumber(roomNumber);
            }
            Utils.setNumberRoomForEachPhoto(fragmentFormAddRealEstate.roomList, fragmentFormAddRealEstate.hashMapUriPhoto);
            int i = 0;
            for(Uri uri : fragmentFormAddRealEstate.listUri){
                fragmentFormAddRealEstate.hashMapUriPhoto.get(uri).setIdHouse(houseId);
                String childPath = houseId + "" + i + ".jpg";
                String path = ImageUtils.saveToInternalStorage(childPath, fragmentFormAddRealEstate.hashMapUriBitmap.get(uri), fragmentFormAddRealEstate.getActivity().getApplicationContext(), ImageUtils.HOUSE_PICTURES);
                fragmentFormAddRealEstate.hashMapUriPhoto.get(uri).setPath(path);
                fragmentFormAddRealEstate.hashMapUriPhoto.get(uri).setChildPath(childPath);
                fragmentFormAddRealEstate.realEstateViewModel.insertPhoto(fragmentFormAddRealEstate.hashMapUriPhoto.get(uri));
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FragmentFormAddRealEstate fragmentFormAddRealEstate = weakReference.get();
            fragmentFormAddRealEstate.showNotification();
        }
    }

    private static class GetDataFromDatabase extends AsyncTask<String, Void, String>{

        WeakReference<FragmentFormAddRealEstate> weakReference;

        GetDataFromDatabase(FragmentFormAddRealEstate fragmentFormAddRealEstate) {
            this.weakReference = new WeakReference<>(fragmentFormAddRealEstate);
        }
        @Override
        protected String doInBackground(String... strings) {
            FragmentFormAddRealEstate fragmentFormAddRealEstate = weakReference.get();
            List<Room> listRoomTmp = fragmentFormAddRealEstate.realEstateViewModel.getRoom();
            fragmentFormAddRealEstate.roomList = new ArrayList<>(listRoomTmp);
            fragmentFormAddRealEstate.tabStringRoom = TypeConverter.listToTableRoom(listRoomTmp);
            fragmentFormAddRealEstate.listRealEstateAgent = new ArrayList<>(fragmentFormAddRealEstate.realEstateViewModel.getRealEstateAgent());
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            FragmentFormAddRealEstate fragmentFormAddRealEstate = weakReference.get();
            fragmentFormAddRealEstate.configureRecycleViewPhoto();
            fragmentFormAddRealEstate.initializeDropDownRealEstateAgent();
        }
    }
}
