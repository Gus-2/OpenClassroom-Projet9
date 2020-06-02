package com.openclassrooms.realestatemanager.ui.realestateedit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.openclassrooms.realestatemanager.models.pojoapi.Coordinates;
import com.openclassrooms.realestatemanager.tools.DataConverter;
import com.openclassrooms.realestatemanager.tools.DateConverter;
import com.openclassrooms.realestatemanager.tools.ImageUtils;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestate.MainActivity;
import com.openclassrooms.realestatemanager.ui.realestatedetail.RealEstateDetailFragment;
import com.openclassrooms.realestatemanager.ui.realestateform.AdapterPicturesHouse;
import com.openclassrooms.realestatemanager.ui.realestateform.AdapterPointOfInterest;
import com.openclassrooms.realestatemanager.ui.realestateform.DataInsertConverter;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;
import com.openclassrooms.realestatemanager.ui.viewmodels.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.RetrofitViewModel;

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
import io.reactivex.observers.DisposableObserver;

public class EditRealEstateActivity extends AppCompatActivity {

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

    public final static int PLACE_REQUEST = 1;
    public final static int PLACE_RETRIEVED = -1;
    public final static int RESULT_LOAD_IMG = 2;
    private RealEstateViewModel realEstateViewModel;
    private RetrofitViewModel retrofitViewModel;
    private ExecutorService executorService;
    private Bundle extras = null;
    private Place place;
    private AdapterPointOfInterest adapterPointOfInterest;
    private AdapterPicturesHouse adapterHousePicture;
    private House house;
    private long availabilityDate;
    private ArrayList<PointOfInterest> listPointOfInterests;
    private ArrayList<PointOfInterest> listPointOfInterestsToUpdate = new ArrayList<>();
    private List<RoomNumber> roomNumbersList = new ArrayList<>();
    private List<Room> roomList;
    private String[] tabStringRoom;
    List<Room> listRoom;
    private List<Uri> listUri = new ArrayList<>();
    private List<Uri> listUriToUpdate = new ArrayList<>();
    private HashMap<Uri, Photo> hashMapUriPhoto = new HashMap<>();
    private HashMap<Uri, Bitmap> hashMapUriBitmap = new HashMap<>();
    private DisposableObserver disposableObserver;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        ButterKnife.bind(this);
        Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        executorService = Executors.newCachedThreadPool();
        Places.createClient(this);
        if(getIntent().getExtras() != null){
            extras = getIntent().getExtras();
            house = (House) extras.get(MainActivity.HOUSES);
        }
        buttonAddProperty.setText(R.string.update_the_property);
        fillAddressAndAvailabilityDate();
        fillPriceSurfaceAndDescription();
        fillDropDownHouseTypeMenu();
        fillDropDownRealEstateAgentMenu();
        fillNumberRoomDropDownMenus();
        fillAvailibilityDateField();
        fillRecyclerViewPointOfInterest();
        configureButtonAvailibilityDate();
        configureButtonAddPointOfInterest();
        configureViewModels();
        configureButtonAddPictures();
        configureRecycleViewPhoto();
        configureButtonAddHouse();
    }

    @SuppressWarnings("all")
    public void configureRecycleViewPhoto(){
        recyclerHousePicture.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerHousePicture.setLayoutManager(layoutManager2);
        ArrayList<Photo> photos = extras.getParcelableArrayList(MainActivity.PHOTOS);
        for(Photo photo : photos ){
            Uri uri = Uri.parse(photo.getPath() + "" + photo.getChildPath());
            listUri.add(uri);
            hashMapUriPhoto.put(uri, photo);
            hashMapUriBitmap.put(uri, ImageUtils.loadImageFromStorage(photo.getPath(), photo.getChildPath()));
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                tabStringRoom = TypeConverter.listToTableRoom(realEstateViewModel.getRoom());
                roomList = realEstateViewModel.getRoom();
                initializeRecyclerViewPhoto();
            }
        });

    }

    public void initializeRecyclerViewPhoto(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterHousePicture = new AdapterPicturesHouse(listUri, hashMapUriPhoto, hashMapUriBitmap, roomList, tabStringRoom, getApplicationContext());
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
        });
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

    private void configureViewModels(){
        ViewModelFactory mViewModelFactory = Injection.provideDaoViewModelFactory(this);
        this.realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.retrofitViewModel = new ViewModelProvider(this, mViewModelFactory).get(RetrofitViewModel.class);
    }

    public void fillRecyclerViewPointOfInterest(){
        recyclerViewPointOfInterest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerViewPointOfInterest.setLayoutManager(layoutManager1);
        if(extras != null)
            listPointOfInterests = extras.getParcelableArrayList(MainActivity.POINT_OF_INTEREST);
        else
            listPointOfInterests = new ArrayList<>();
        adapterPointOfInterest = new AdapterPointOfInterest(listPointOfInterests);
        recyclerViewPointOfInterest.setAdapter(adapterPointOfInterest);
    }

    private void fillAvailibilityDateField() {
        if(house.getState().equals(RealEstateDetailFragment.STATE_SOLD))
            editTextAvailibilityDate.setText("House is sold since the " + df.format(house.getSoldDate()));
        else
            editTextAvailibilityDate.setText(df.format(house.getAvailableDate()));
    }

    private void configureButtonAvailibilityDate() {
        if(!house.getState().equals(RealEstateDetailFragment.STATE_SOLD))
            buttonAddAvailibilityDate.setOnClickListener(v -> showDatePickerDialog());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Toast.makeText(this, R.string.error_retrieving_data, Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == RESULT_LOAD_IMG && data != null){
                if(null != data.getClipData()) {
                    for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        Bitmap bitmap = DataConverter.convertUriToBitmap(uri, getApplicationContext());
                        listUriToUpdate.add(uri);
                        listUri.add(uri);
                        hashMapUriBitmap.put(uri, bitmap);
                        hashMapUriPhoto.put(uri, new Photo());

                    }
                } else if(data.getData() != null){
                    Uri uri = data.getData();
                    Bitmap bitmap = DataConverter.convertUriToBitmap(uri, getApplicationContext());
                    listUriToUpdate.add(uri);
                    listUri.add(uri);
                    hashMapUriBitmap.put(uri, bitmap);
                    hashMapUriPhoto.put(uri, new Photo());
                }
                adapterHousePicture.notifyDataSetChanged();
            }
        }


    /**
     * Insert into the database a new point of interest
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

    public void addPointOfInterest(int id){
        PointOfInterest pointOfInterest = new PointOfInterest(id, place.getAddress(), place.getName());
        listPointOfInterests.add(pointOfInterest);
        listPointOfInterestsToUpdate.add(pointOfInterest);
        adapterPointOfInterest.notifyDataSetChanged();
    }

    private void configureButtonAddPointOfInterest() {
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        buttonAddPointOfInterest.setOnClickListener(v -> startActivityForResult(intent, PLACE_REQUEST));
    }

    @SuppressWarnings("unchecked")
    private void showDatePickerDialog(){
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {

            editTextAvailibilityDate.setText(df.format(datePicker.getSelection()));
            availabilityDate = (long) selection;
        });
    }
    ArrayList<RealEstateAgent> realEstateAgentArrayList;
    private void fillDropDownRealEstateAgentMenu() {
        realEstateAgentArrayList = extras.getParcelableArrayList(MainActivity.REAL_ESTATE_AGENT);
        String[] realEstateAgents = TypeConverter.realEstateAgentToStringArray(realEstateAgentArrayList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getApplicationContext(), R.layout.dropdown_menu_popup_item, realEstateAgents);
        dropDownMenuRealEstateAgent.setText(realEstateAgents[(int) house.getIdRealEstateAgent()-1]);
        dropDownMenuRealEstateAgent.setAdapter(adapter);
    }
    Address address;
    private void fillAddressAndAvailabilityDate() {
         address = extras.getParcelable(MainActivity.ADDRESS);

        if(address != null){
            if(address.getStreet().equals(""))
                editTextAddressStreet.setText(R.string.unspecified);
            else
                editTextAddressStreet.setText(address.getStreet());

            if(address.getNumber().equals(""))
                editTextAddressNumber.setText(R.string.unspecified);
            else
                editTextAddressStreet.setText(address.getNumber());

            if(address.getDistrict().equals(""))
                editTextAddressDistrict.setText(R.string.unspecified);
            else
                editTextAddressDistrict.setText(address.getDistrict());

            if(address.getCity().equals(""))
                editTextAddressCity.setText(R.string.unspecified);
            else
                editTextAddressCity.setText(address.getCity());

            if(address.getPostCode().equals(""))
                editTextAddressPostCode.setText(R.string.unspecified);
            else
                editTextAddressPostCode.setText(address.getPostCode());

            if(address.getAdditionalInformation().equals(""))
                editTextAddressAdditionnalInformation.setText(R.string.unspecified);
            else
                editTextAddressAdditionnalInformation.setText(address.getAdditionalInformation());
        }

        if(house.getAvailableDate() > 0){
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            editTextAvailibilityDate.setText(df.format(DateConverter.toDate(house.getAvailableDate())));
        }else{
            editTextAvailibilityDate.setText(R.string.date_unspecified);
        }
    }

    private void fillPriceSurfaceAndDescription() {
        if(house.getPrice() != -1)
            editTextPrice.setText(String.format(getString(R.string.number_format_double), house.getPrice()));
        else
            editTextPrice.setText(R.string.non_specified);

        if(house.getSurface() != -1)
            editTextSurface.setText(String.format(getString(R.string.number_format_double), house.getSurface()));
        else
            editTextSurface.setText(R.string.non_specified);

        if(house.getDescription().equals(""))
            editTextDescription.setText(R.string.unspecified_description);
        else
            editTextDescription.setText(house.getDescription());

    }
    List<HouseType> listHouseTypes = new ArrayList<>();
    public void fillDropDownHouseTypeMenu(){

        HashMap<Long, HouseType> longHouseTypeHashMap = (HashMap<Long, HouseType>) extras.getSerializable(MainActivity.HOUSES_TYPES);
        for(Long key : longHouseTypeHashMap.keySet()){
            listHouseTypes.add(longHouseTypeHashMap.get(key));
        }
        String[] houseType = TypeConverter.houseTypeToStringArray(listHouseTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( getApplicationContext(), R.layout.dropdown_menu_popup_item, houseType);
        this.runOnUiThread(() -> {
            dropDownMenuHouseType.setAdapter(adapter);
            dropDownMenuHouseType.setText(houseType[(int) house.getIdHouseType()-1], false);

        });
    }

    private void fillNumberRoomDropDownMenus() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, FormActivity.numberRoom);
        dropDownMenuNumberKitchen.setAdapter(adapter);
        dropDownMenuNumberBathroom.setAdapter(adapter);
        dropDownMenuNumberBedroom.setAdapter(adapter);
        dropDownMenuNumberLivingRoom.setAdapter(adapter);
        dropDownMenuNumberToilet.setAdapter(adapter);
        dropDownMenuNumberCellar.setAdapter(adapter);
        dropDownMenuNumberPool.setAdapter(adapter);

        List<RoomNumber> listRoomNumbers = extras.getParcelableArrayList(MainActivity.ROOM_NUMBER);
        dropDownMenuNumberKitchen.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(0).getNumber()), false);
        dropDownMenuNumberBathroom.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(1).getNumber()), false);
        dropDownMenuNumberBedroom.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(2).getNumber()), false);
        dropDownMenuNumberLivingRoom.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(3).getNumber()), false);
        dropDownMenuNumberToilet.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(4).getNumber()), false);
        dropDownMenuNumberCellar.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(5).getNumber()), false);
        dropDownMenuNumberPool.setText(String.format(getString(R.string.number_format_room), listRoomNumbers.get(6).getNumber()), false);
    }

    private static class InsertIntoDatabase extends AsyncTask<String, Void, String>{

        WeakReference<EditRealEstateActivity> weakReference;

        InsertIntoDatabase(EditRealEstateActivity editRealEstateActivity) {
            this.weakReference = new WeakReference<>(editRealEstateActivity);
        }
        @Override
        protected String doInBackground(String... strings) {
            weakReference.get().realEstateViewModel.insertAddress(weakReference.get().address);
            weakReference.get().realEstateViewModel.insertHouse(weakReference.get().house);

            for(PointOfInterest pointOfInterest: weakReference.get().listPointOfInterestsToUpdate){
                long pointOfInterestId = weakReference.get().realEstateViewModel.insertPointOfInterest(pointOfInterest);
                weakReference.get().realEstateViewModel.insertHousePointOfInterest(new HousePointOfInterest(weakReference.get().house.getIdHouse(), pointOfInterestId));
            }
            for(RoomNumber roomNumber : weakReference.get().roomNumbersList){
                weakReference.get().realEstateViewModel.insertRoomNumber(roomNumber);
            }
            int i = weakReference.get().listUri.size() - weakReference.get().listUriToUpdate.size();
            for(Uri uri : weakReference.get().listUri){
                if(weakReference.get().listUriToUpdate.contains(uri)){
                    weakReference.get().hashMapUriPhoto.get(uri).setIdHouse(weakReference.get().house.getIdHouse());
                    String childPath = weakReference.get().house.getIdHouse() + "" + i + ".jpg";
                    //String path = PictureDownloader.saveToInternalStorage(childPath, weakReference.get().hashMapUriBitmap.get(uri), weakReference.get().getApplicationContext());
                    //weakReference.get().hashMapUriPhoto.get(uri).setPath(path);
                    weakReference.get().hashMapUriPhoto.get(uri).setChildPath(childPath);
                    weakReference.get().realEstateViewModel.insertPhoto(weakReference.get().hashMapUriPhoto.get(uri));
                    i++;
                }else{
                    weakReference.get().realEstateViewModel.insertPhoto(weakReference.get().hashMapUriPhoto.get(uri));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            weakReference.get().setResult(RESULT_OK);
            weakReference.get().finish();
        }
    }

    private void configureButtonAddHouse() {
        buttonAddProperty.setOnClickListener(v -> {
            address.setStreet(DataInsertConverter.getStringFromEditTextString(editTextAddressStreet.getText().toString()));
            address.setDistrict(DataInsertConverter.getStringFromEditTextString(editTextAddressDistrict.getText().toString()));
            address.setCity(DataInsertConverter.getStringFromEditTextString(editTextAddressCity.getText().toString()));
            address.setPostCode(DataInsertConverter.getStringFromEditTextString(editTextAddressPostCode.getText().toString()));
            address.setAdditionalInformation(DataInsertConverter.getStringFromEditTextString(editTextAddressAdditionnalInformation.getText().toString()));

            house.setIdHouseType(TypeConverter.getHouseTypeId(listHouseTypes, dropDownMenuHouseType.getText().toString()));
            house.setIdRealEstateAgent(TypeConverter.getRealEstateAgentId(realEstateAgentArrayList, dropDownMenuRealEstateAgent.getText().toString()));

            String priceString = editTextPrice.getText().toString();
            if(!priceString.equals("")){
                String price = DataInsertConverter.getHousePrice(editTextPrice.getText().toString().replace("\\s", "")).replace(",", ".");
                price.trim();
                house.setPrice(Double.parseDouble(price));
            }


            String surfaceString = editTextSurface.getText().toString();

            if(!surfaceString.equals(""))
                house.setSurface(Double.parseDouble(DataInsertConverter.getHousePrice(editTextSurface.getText().toString())));

            house.setDescription(DataInsertConverter.getDescription(editTextDescription.getText().toString()));

            roomNumbersList.add(new RoomNumber(1, Integer.parseInt(dropDownMenuNumberKitchen.getText().toString())));
            roomNumbersList.add(new RoomNumber(2, Integer.parseInt(dropDownMenuNumberBathroom.getText().toString())));
            roomNumbersList.add(new RoomNumber(3, Integer.parseInt(dropDownMenuNumberBedroom.getText().toString())));
            roomNumbersList.add(new RoomNumber(4, Integer.parseInt(dropDownMenuNumberLivingRoom.getText().toString())));
            roomNumbersList.add(new RoomNumber(5, Integer.parseInt(dropDownMenuNumberToilet.getText().toString())));
            roomNumbersList.add(new RoomNumber(6, Integer.parseInt(dropDownMenuNumberCellar.getText().toString())));
            roomNumbersList.add(new RoomNumber(7, Integer.parseInt(dropDownMenuNumberPool.getText().toString())));

            getLongitudeAndLatitudeForAddress(editTextAddressNumber.getText().toString() + " " +  editTextAddressStreet.getText().toString() + "," +  editTextAddressDistrict.getText().toString() + "," + editTextAddressPostCode.getText().toString() + "," + editTextAddressCity.getText().toString());
        });
    }

    private void getLongitudeAndLatitudeForAddress(String stringAddress){
        disposableObserver = retrofitViewModel.getCoordinates(stringAddress).subscribeWith(new DisposableObserver<Coordinates>() {
            @Override
            public void onNext(Coordinates coordinates) {
                if(coordinates.getResults().size() != 0){
                    address.setLatitude( coordinates.getResults().get(0).getGeometry().getLocation().getLat());
                    address.setLongitude( coordinates.getResults().get(0).getGeometry().getLocation().getLng());
                }
                insertIntoDatabase();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), R.string.couldnt_get_place_location, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {  }
        });
    }

    private void insertIntoDatabase(){
        InsertIntoDatabase insertIntoDatabase = new InsertIntoDatabase(this);
        insertIntoDatabase.execute();
    }

}
