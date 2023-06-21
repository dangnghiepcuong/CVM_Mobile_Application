package com.example.cvm_mobile_application.ui.org.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

public class RegisterCitizenInfo extends AppCompatActivity implements ViewStructure {
    private Button btnBack;
    private TextView tbTitle;
    Citizen citizen;
    private FirebaseFirestore db;
    private TextView tvName;
    private TextView tvBirthday;
    private TextView tvGender;
    private TextView tvPhone;
    private TextView tvId;
    private TextView tvAdress;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_read_info_register);
        db = FirebaseFirestore.getInstance();
        citizen = new Citizen();
    }

    protected void onStart(){
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");

        try {
            implementView();
            bindViewData();
            setViewListener();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);

        tvName = findViewById(R.id.tv_name);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvGender = findViewById(R.id.tv_gender);
        tvPhone = findViewById(R.id.tv_phone);
        tvId = findViewById(R.id.tv_id);
        tvAdress = findViewById(R.id.tv_address);
    }

    @Override
    public void bindViewData() throws JSONException {
        tbTitle.setText("Thông tin công dân đăng ký tiêm chủng");

        tvName.setText(citizen.getFull_name());
        tvBirthday.setText(citizen.getBirthdayString());
        tvGender.setText(citizen.getGender());
        tvPhone.setText(citizen.getPhone());
        tvId.setText(citizen.getId());

        String adress = citizen.getStreet() + ", "
                + citizen.getWard_name() + ", "
                + citizen.getProvince_name();
        tvAdress.setText(adress);
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
