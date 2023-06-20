package com.example.cvm_mobile_application.ui.org.schedule;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.ui.ViewStructure;

import org.json.JSONException;

public class RegisterCitizenInfo extends AppCompatActivity implements ViewStructure {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_profile);
    }


    @Override
    public void implementView() {

    }

    @Override
    public void bindViewData() throws JSONException {

    }

    @Override
    public void setViewListener() {

    }
}
