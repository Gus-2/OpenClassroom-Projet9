package com.openclassrooms.realestatemanager.ui.realestateform;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;

public class FormActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("all")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        if (savedInstanceState == null)
            startFragment();
    }

    private void startFragment(){
        FragmentFormAddRealEstate fragmentFormAddRealEstate = new FragmentFormAddRealEstate();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view_add_real_estate, fragmentFormAddRealEstate).commit();
    }
}