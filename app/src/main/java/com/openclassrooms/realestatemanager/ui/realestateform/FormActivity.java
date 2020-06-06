package com.openclassrooms.realestatemanager.ui.realestateform;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.openclassrooms.realestatemanager.tools.DataConverter;
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

public class FormActivity extends AppCompatActivity implements AdapterPointOfInterest.OnPointOfInterestClickListener {
    private final static int RESULT_OK = 80;
    private final static int ERROR_RESULT = 1;
    private final static int PLACE_RETRIEVED = -1;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final static int RESULT_LOAD_IMG = 2;
    private final static int RESULT_LOAD_VIDEO = 3;
    public final static String STATE_AVAILABLE = "Available";

    public final static String COUNTRY = "United States";
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
    private Disposable mapsImageObserver;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        notificationManager = NotificationManagerCompat.from(this);
        ButterKnife.bind(this);

        Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        executorService = Executors.newCachedThreadPool();
        Places.createClient(this);
        this.configureViewModels();

        this.configureDropDownHouseTypeMenu();
        this.configureNumberRoomDropDownMenus();
        this.configureButtonAvailibilityDate();
        this.configureButtonAddPointOfInterest();
        this.configureRecycleViewPointOfInterest();
        this.configureButtonAddHouse();
        this.configureButtonAddPictures();
        this.configureButtonAddVideo();

        GetDataFromDatabase getDataFromDatabase = new GetDataFromDatabase(this);
        getDataFromDatabase.execute();
        setResult(ERROR_RESULT);
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
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerHousePicture.setLayoutManager(layoutManager2);
        ArrayList<Photo> listPhotoAlreadyExist = null;

        adapterHousePicture = new AdapterPicturesHouse(listUri, hashMapUriPhoto, hashMapUriBitmap,roomList, tabStringRoom, getApplicationContext());
        adapterHousePicture.setOnItemClickListener(position -> {
            for(Uri uri : listUri){
                hashMapUriPhoto.get(uri).setMainPicture(false);
            }
            hashMapUriPhoto.get(listUri.get(position)).setMainPicture(true);
            adapterHousePicture.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), R.string.picture_set_default, Toast.LENGTH_SHORT).show();
        });
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

    public void configureRecycleViewPointOfInterest(){
        recyclerViewPointOfInterest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerViewPointOfInterest.setLayoutManager(layoutManager1);
        listPointOfInterests = new ArrayList<>();
        adapterPointOfInterest = new AdapterPointOfInterest(listPointOfInterests, this);
        recyclerViewPointOfInterest.setAdapter(adapterPointOfInterest);
    }

    private void initializeDropDownRealEstateAgent(){

        String[] realEstateAgents = TypeConverter.realEstateAgentToStringArray(listRealEstateAgent);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getApplicationContext(), R.layout.dropdown_menu_popup_item, realEstateAgents);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Toast.makeText(this, R.string.error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == RESULT_LOAD_IMG && data != null){
            if(null != data.getClipData()) {
                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    Bitmap bitmap = DataConverter.convertUriToBitmap(uri, getApplicationContext());
                    listUri.add(uri);
                    hashMapUriBitmap.put(uri, bitmap);
                    hashMapUriPhoto.put(uri, new Photo());
                }
            } else if(data.getData() != null){
                Uri uri = data.getData();
                Bitmap bitmap = DataConverter.convertUriToBitmap(uri, getApplicationContext());
                listUri.add(uri);
                hashMapUriBitmap.put(uri, bitmap);
                hashMapUriPhoto.put(uri, new Photo());
            }
            adapterHousePicture.notifyDataSetChanged();
        } else if(requestCode == RESULT_LOAD_VIDEO && data != null){
            videoPath = data.getData();
            tvAddVideo.setText(videoPath.getPath());
            ImageUtils.saveVideoToInternalStorage(videoPath, this);
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

    public void addPointOfInterest(int id){
        listPointOfInterests.add(new PointOfInterest(id, place.getAddress(), place.getName()));
        adapterPointOfInterest.notifyDataSetChanged();
    }

    private void configureButtonAddPointOfInterest() {
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        buttonAddPointOfInterest.setOnClickListener(v -> startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE));
    }

    private void configureButtonAvailibilityDate() {
        buttonAddAvailibilityDate.setOnClickListener(v -> showDatePickerDialog());
    }

    @SuppressWarnings("unchecked")
    private void showDatePickerDialog(){
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
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
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, numberRoom);
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
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
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
    public void initializeDropDownHouseTypeMenu(){
        String[] houseType = TypeConverter.houseTypeToStringArray(listHouseTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getApplicationContext(), R.layout.dropdown_menu_popup_item, houseType);
        this.runOnUiThread(() -> {
            dropDownMenuHouseType.setAdapter(adapter);
            dropDownMenuHouseType.setText(houseType[0], false);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        executorService.shutdownNow();
        if(coordinatesObserver!= null && !coordinatesObserver.isDisposed())
            coordinatesObserver.dispose();
        if(mapsImageObserver!= null && !mapsImageObserver.isDisposed())
            mapsImageObserver.dispose();
    }

    public void showNotification(){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_check_blue_24dp)
                .setContentTitle("Good news !")
                .setContentText("The new property has been added successfully !")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build();
        notificationManager.notify(1, notification);

        finish();
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
        Picasso.with(getApplicationContext())
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

    private static class InsertIntoDatabase extends AsyncTask<Void, Void, Void>{

        WeakReference<FormActivity> weakReference;

        InsertIntoDatabase(FormActivity formActivity) {
            this.weakReference = new WeakReference<>(formActivity);
        }
        @Override
        protected Void doInBackground(Void... strings) {
            FormActivity formActivity = weakReference.get();

            long idAddress = formActivity.realEstateViewModel.insertAddress(formActivity.newAddressToInsert);
            formActivity.houseToInsert.setIdAddress(idAddress);

            if(formActivity.photoMap != null){
                String childPath = idAddress + "_map_image.jpg";
                String parentPathPlacePreview = ImageUtils.saveToInternalStorage(childPath, formActivity.photoMap, formActivity.getApplicationContext(), ImageUtils.MAP_IMAGE);
                formActivity.houseToInsert.setChildPathPlacePreview(childPath);
                formActivity.houseToInsert.setParentPathPlacePreview(parentPathPlacePreview);
            }
            if(formActivity.videoPath != null){
                formActivity.houseToInsert.setVideoPath(ImageUtils.saveVideoToInternalStorage(formActivity.videoPath, formActivity));
                Log.d("VIDEO_PATH", formActivity.houseToInsert.getVideoPath());
            }

            long houseId = formActivity.realEstateViewModel.insertHouse(formActivity.houseToInsert);

            for(PointOfInterest pointOfInterest: formActivity.listPointOfInterests){
                long pointOfInterestId = formActivity.realEstateViewModel.insertPointOfInterest(pointOfInterest);
                formActivity.realEstateViewModel.insertHousePointOfInterest(new HousePointOfInterest(houseId, pointOfInterestId));
            }

            for(RoomNumber roomNumber : formActivity.roomNumbersList){
                roomNumber.setIdHouse(houseId);
                formActivity.realEstateViewModel.insertRoomNumber(roomNumber);
            }
            Utils.setNumberRoomForEachPhoto(formActivity.roomList, formActivity.hashMapUriPhoto);
            int i = 0;
            for(Uri uri : formActivity.listUri){
                formActivity.hashMapUriPhoto.get(uri).setIdHouse(houseId);
                String childPath = houseId + "" + i + ".jpg";
                String path = ImageUtils.saveToInternalStorage(childPath, formActivity.hashMapUriBitmap.get(uri), formActivity.getApplicationContext(), ImageUtils.HOUSE_PICTURES);
                formActivity.hashMapUriPhoto.get(uri).setPath(path);
                formActivity.hashMapUriPhoto.get(uri).setChildPath(childPath);
                formActivity.realEstateViewModel.insertPhoto(formActivity.hashMapUriPhoto.get(uri));
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FormActivity formActivity = weakReference.get();
            formActivity.setResult(RESULT_OK);
            formActivity.showNotification();
        }
    }

    private static class GetDataFromDatabase extends AsyncTask<String, Void, String>{

        WeakReference<FormActivity> weakReference;

        GetDataFromDatabase(FormActivity formActivity) {
            this.weakReference = new WeakReference<>(formActivity);
        }
        @Override
        protected String doInBackground(String... strings) {
            FormActivity formActivity = weakReference.get();
            List<Room> listRoomTmp = formActivity.realEstateViewModel.getRoom();
            formActivity.roomList = new ArrayList<>(listRoomTmp);
            formActivity.tabStringRoom = TypeConverter.listToTableRoom(listRoomTmp);
            formActivity.listRealEstateAgent = new ArrayList<>(formActivity.realEstateViewModel.getRealEstateAgent());
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            FormActivity formActivity = weakReference.get();
            formActivity.configureRecycleViewPhoto();
            formActivity.initializeDropDownRealEstateAgent();
        }
    }
}