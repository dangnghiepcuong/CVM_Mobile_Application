package com.example.cvm_mobile_application.ui.citizen.info;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;

public class CitizenProfileFragment extends AppCompatActivity {
    EditText et_fullName;
    EditText et_birthday;
    RadioButton rdo_gender_man;
    RadioButton rdo_gender_women;
    RadioButton rdo_gender_other;
    EditText et_phone;
    EditText et_id;
    EditText et_email;
    EditText et_street;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_citizen_profile);
        et_fullName = findViewById(R.id.et_fullname);
        et_birthday = findViewById(R.id.et_birthday);
        rdo_gender_man = findViewById(R.id.rdo_gender_man);
        rdo_gender_women = findViewById(R.id.rdo_gender_woman);
        rdo_gender_women = findViewById(R.id.rdo_gender_other);
        et_phone = findViewById(R.id.et_phone);
        et_id = findViewById(R.id.et_id);
        et_email = findViewById(R.id.et_email);
        et_street = findViewById(R.id.et_street);

    }

    protected void onStart() {
        super.onStart();
        Citizen citizen = getIntent().getParcelableExtra("citizen");
        et_fullName.setText(citizen.getFull_name());
        et_birthday.setText(citizen.getBirthday());

        switch (citizen.getGender()) {
            case "Nam":
                rdo_gender_man.setChecked(true);
                break;
            case "Nữ":
                rdo_gender_women.setChecked(true);
                break;
            case "Khác":
                rdo_gender_other.setChecked(true);
                break;
        }
        et_phone.setText(citizen.getPhone());
        et_id.setText(citizen.getId());
        et_email.setText(citizen.getEmail());
        et_street.setText(citizen.getStreet());
    }

}
