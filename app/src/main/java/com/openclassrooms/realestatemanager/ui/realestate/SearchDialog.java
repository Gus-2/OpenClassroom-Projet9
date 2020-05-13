package com.openclassrooms.realestatemanager.ui.realestate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.ui.realestateform.FormActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.bendik.simplerangeview.SimpleRangeView;

import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.DISTRICT;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MAX_PRICE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MAX_SURFACE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MIN_PRICE;
import static com.openclassrooms.realestatemanager.ui.realestate.RealEstateListFragment.MIN_SURFACE;

public class SearchDialog extends AppCompatDialogFragment {

    @BindView(R.id.tv_house_type_dialog)
    AutoCompleteTextView dropDownHouseType;
    @BindView(R.id.tv_district_dialog)
    AutoCompleteTextView dropDownDistrict;
    @BindView(R.id.tv_nb_photo_selected)
    AutoCompleteTextView dropDownNumberPhoto;

    @BindView(R.id.sb_surface)
    SimpleRangeView sbSurface;
    @BindView(R.id.sb_price)
    SimpleRangeView sbPrice;

    @BindView(R.id.tiet_availibility_date_dialog)
    TextInputEditText tietAvailabilityDate;

    @BindView(R.id.bt_add_availability_date_dialog)
    Button btAddAvailabilityDate;

    private SearchDialogListener searchDialogListener;
    private List<HouseType> listHousesTypes;
    private ArrayList<String> listDistrict;
    private Activity activity;
    private double maxPrice;
    private double minPrice;
    private double maxSurface;
    private double minSurface;
    private long availabilityDate;
    private long maxSurfaceSelected = -1;
    private long minSurfaceSelected = 0;
    private long maxPriceSelected = -1;
    private long minPriceSelected = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchDialogListener = (SearchDialogListener) getTargetFragment();
        if(getActivity() != null)
            activity = getActivity();
        listHousesTypes = getArguments().getParcelableArrayList(MainActivity.HOUSES_TYPES);
        maxPrice = getArguments().getDouble(MAX_PRICE);
        minPrice = getArguments().getDouble(MIN_PRICE);
        maxSurface = getArguments().getDouble(MAX_SURFACE);
        minSurface = getArguments().getDouble(MIN_SURFACE);
        listDistrict = getArguments().getStringArrayList(DISTRICT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        ButterKnife.bind(this, view);

        this.configureHouseTypes();
        this.configureDistricts();
        this.configureNumberPhoto();
        this.configureAddAvailabilityDateButton();
        this.configureSeekBarSurface();
        this.configureSeekBarPrice();
        return builder.setView(view)
                .setTitle(R.string.search_dialog)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                })
                .setPositiveButton(R.string.search, (dialog, which) -> {
                    long numberPhoto = -1;
                    if(!dropDownNumberPhoto.getText().toString().equals(""))
                        numberPhoto = Long.parseLong(dropDownNumberPhoto.getText().toString());

                    searchDialogListener.search(TypeConverter.getHouseTypeId(listHousesTypes, dropDownHouseType.getText().toString()),
                            minSurfaceSelected, maxSurfaceSelected, minPriceSelected, maxPriceSelected,
                            availabilityDate,dropDownDistrict.getText().toString(), numberPhoto);
                })
                .create();
    }

    private void configureSeekBarPrice() {
        if(minPrice != -1){
            sbPrice.setOnChangeRangeListener((simpleRangeView, i, i1) -> {
                minPriceSelected = i;
                maxPriceSelected = i1;
            });

            int rangePrice = (int) (maxPrice - minPrice) / 10;
            sbPrice.setOnRangeLabelsListener((simpleRangeView, i, state) -> String.valueOf(rangePrice * i));
        }else{
            sbPrice.setOnRangeLabelsListener((simpleRangeView, i, state) -> {
                if(i == 2)
                    return getString(R.string.no_price_available);
                else
                    return "";
            });
        }
    }

    private void configureSeekBarSurface() {
        if(minSurface != -1){
            sbSurface.setOnChangeRangeListener((simpleRangeView, i, i1) -> {
                minSurfaceSelected = i;
                maxSurfaceSelected = i1;
            });

            int rangeSurface = (int) (maxSurface - minSurface) / 10;
            sbSurface.setOnRangeLabelsListener((simpleRangeView, i, state) -> String.valueOf(rangeSurface * i));
        }else{
            sbSurface.setOnRangeLabelsListener((simpleRangeView, i, state) -> {
                if(i == 2)
                    return getString(R.string.no_surface_available);
                else
                    return "";
            });
        }
    }

    private void configureAddAvailabilityDateButton() {
        btAddAvailabilityDate.setOnClickListener(l -> showDatePickerDialog());
    }

    private void configureNumberPhoto() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(activity, R.layout.dropdown_menu_popup_item, FormActivity.numberRoom);
        dropDownNumberPhoto.setAdapter(adapter);
    }

    private void configureDistricts() {
        String[] districtArray = listDistrict.toArray(new String[listDistrict.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.dropdown_menu_popup_item, districtArray);
        if(listDistrict.size() == 1 && listDistrict.get(0).equals(""))
            dropDownDistrict.setText(R.string.no_district_defined);
        else
            dropDownDistrict.setText(districtArray[0]);
        dropDownDistrict.setAdapter(adapter);
    }

    private void configureHouseTypes() {
        String[] houseTypesArray = new String[listHousesTypes.size()+1];
        for(int i = 0; i < listHousesTypes.size(); i++)
            houseTypesArray[i] = listHousesTypes.get(i).getHouseType();
        houseTypesArray[listHousesTypes.size()] = "";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.dropdown_menu_popup_item, houseTypesArray);
        dropDownHouseType.setText("");
        dropDownHouseType.setAdapter(adapter);

    }

    @SuppressWarnings("unchecked")
    private void showDatePickerDialog(){
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(getActivity().getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tietAvailabilityDate.setText(df.format(datePicker.getSelection()));
            availabilityDate = (long) selection;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Button negativeButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 10, 0);
        negativeButton.setLayoutParams(params);
    }

    public interface SearchDialogListener{
        void search(long houseType, long minSurface, long maxSurface, long minPrice, long maxPrice,
            long availabilityDate, String district, long numberPhoto);
    }
}
