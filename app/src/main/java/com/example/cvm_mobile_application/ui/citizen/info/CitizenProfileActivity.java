package com.example.cvm_mobile_application.ui.citizen.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.objects.DVHCHelper;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.home.CitizenNavigationBottomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck public class CitizenProfileActivity extends AppCompatActivity implements ViewStructure {
    private EditText etFullName;
    private RadioButton rdBtnGenderMale;
    private RadioButton rdBtnGenderFemale;
    private RadioButton rdBtnGenderAnother;
    private EditText etPhone;
    private EditText etId;
    private EditText etEmail;
    private Spinner spProvince;
    private List<SpinnerOption> provinceList = new ArrayList<>();
    private SpinnerAdapter spProvinceListAdapter;
    private Spinner spDistrict;
    private List<SpinnerOption> districtList = new ArrayList<>();
    private SpinnerAdapter spDistrictListAdapter;
    private Spinner spWard;
    private List<SpinnerOption> wardList = new ArrayList<>();
    private SpinnerAdapter spWardListAdapter;
    private DVHCHelper dvhcHelper;
    private EditText etStreet;
    private TextView tvBirthday;
    private Button btnBirthdayDP;
    private DatePicker dpBirthday;
    private Citizen citizen;
    private Button btnSaveProfile;
    private RadioGroup rdGroupGender;
    private FirebaseFirestore db;
    private RadioButton rdBtnGender;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_profile);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getApplicationContext());
    }

    protected void onStart() {
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

        etFullName = findViewById(R.id.et_fullname);

        tvBirthday = findViewById(R.id.tv_birthday);
        btnBirthdayDP = findViewById(R.id.btn_birthday_dp);
        dpBirthday = findViewById(R.id.dp_birthday);

        rdGroupGender = findViewById(R.id.rd_group_gender);
        rdBtnGenderMale = findViewById(R.id.rd_btn_gender_male);
        rdBtnGenderFemale = findViewById(R.id.rd_btn_gender_female);
        rdBtnGenderAnother = findViewById(R.id.rd_btn_gender_another);
        etPhone = findViewById(R.id.et_phone);
        etId = findViewById(R.id.et_id);
        etEmail = findViewById(R.id.et_email);
        spProvince = findViewById(R.id.sp_province);
        spDistrict = findViewById(R.id.sp_district);
        spWard = findViewById(R.id.sp_ward);
        etStreet = findViewById(R.id.et_street);

        btnSaveProfile = findViewById(R.id.btn_save_profile);
    }

    @Override
    public void bindViewData() throws JSONException {
        tbTitle.setText("Chỉnh sửa thông tin cá nhân");

        etFullName.setText(citizen.getFull_name());

        tvBirthday.setText(citizen.getBirthdayString());

        switch (citizen.getGender()) {
            case "Nam":
                rdBtnGenderMale.setChecked(true);
                break;
            case "Nữ":
                rdBtnGenderFemale.setChecked(true);
                break;
            case "Khác":
                rdBtnGenderAnother.setChecked(true);
                break;
        }
        etPhone.setText(citizen.getPhone());
        etId.setText(citizen.getId());
        etEmail.setText(citizen.getEmail());

        //GET PROVINCE LIST
        provinceList = dvhcHelper.getLocalList(DVHCHelper.PROVINCE_LEVEL, null);

        //SET PROVINCE INFO VALUE
        spProvinceListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, provinceList);
        spProvince.setAdapter(spProvinceListAdapter);
        int provincePosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.PROVINCE_LEVEL, citizen.getProvince_name(), null);
        spProvince.setSelection(provincePosition, true);

        //GET DISTRICT LIST
        String provinceCode = ((SpinnerOption) spProvince.getItemAtPosition(spProvince.getSelectedItemPosition())).getValue();
        districtList = dvhcHelper.getLocalList(DVHCHelper.DISTRICT_LEVEL, provinceCode);

        //SET DISTRICT INFO VALUE
        spDistrictListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, districtList);
        spDistrict.setAdapter(spDistrictListAdapter);
        int districtPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.DISTRICT_LEVEL, citizen.getDistrict_name(), provinceCode);
        spDistrict.setSelection(districtPosition, true);

        //GET WARD LIST
        String districtCode = ((SpinnerOption) spDistrict.getItemAtPosition(spDistrict.getSelectedItemPosition())).getValue();
        wardList = dvhcHelper.getLocalList(DVHCHelper.WARD_LEVEL, districtCode);

        //SET WARD INFO VALUE
        spWardListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.WARD_LEVEL, citizen.getWard_name(), districtCode);
        spWard.setSelection(wardPosition, true);

        etStreet.setText(citizen.getStreet());
    }

    @Override
    public void setViewListener() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBirthdayDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dpBirthday.getVisibility() == View.GONE) {
                    dpBirthday.setVisibility(View.VISIBLE);
                } else {
                    dpBirthday.setVisibility(View.GONE);
                }
            }
        });

        dpBirthday.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tvBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });

        //SET PROVINCE SPINNER LISTENER
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET DISTRICT LIST, WARD LIST
                CitizenProfileActivity.this
                        .spProvinceTriggeredActivities();
                Log.i("myTAG", "province spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SET DISTRICT SPINNER LISTENER
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET WARD LIST
                CitizenProfileActivity.this
                        .spDistrictTriggeredActivities();
                Log.i("myTAG", "district spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenProfileActivity.this
                        .spWardTriggeredActivities();
                Log.i("myTAG", "ward spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitizenProfileActivity.this.updateProfile();
            }
        });
    }

    public void spProvinceTriggeredActivities() {
        try {
            SpinnerOption provinceOption =
                    (SpinnerOption) provinceList.get(spProvince.getSelectedItemPosition());
            districtList = dvhcHelper.getLocalList(
                    DVHCHelper.DISTRICT_LEVEL, provinceOption.getValue());
            spDistrictListAdapter.setOptionList(districtList);
            spDistrictListAdapter.notifyDataSetChanged();

            // Changing selected province triggers district listener to change district list.
            // And when changing district list, we also need to change ward list,

            // IN CASE, THE DISTRICT SPINNER HAS NOT BEEN SELECTED
            // (SELECTED POSITION STAYS = 0)
            // THEN WHEN .setSelection(0) IS CALLED
            // IT DOES NOT TRIGGER THE SELECTION OF THE DISTRICT SPINNER
            // (THE ACTIVITY WHEN DISTRICT SPINNER IS TRIGGERED IS CHANGING THE WARD LIST)
            // SO WE NEED TO DO THE ACTIVITY OF THE DISTRICT SPINNER TRIGGER BY HAND HERE
            if (spDistrict.getSelectedItemPosition() == 0) {
//                SpinnerOption districtOption = (SpinnerOption) districtList.get(0);
//                wardList = dvhcHelper.getLocalList(
//                        DVHCHelper.WARD_LEVEL, districtOption.getValue());
//                spWardListAdapter.setOptionList(wardList);
//                spWardListAdapter.notifyDataSetChanged();

                spDistrictTriggeredActivities();
            }
            // ELSE SET SELECTION TO 0 AND TRIGGER THE DISTRICT SPINNER AUTOMATICALLY
            else {
                spDistrict.setSelection(0, true);
            }

            // MORE EXPLANATION
            // THE REASON WE NEED TO TRIGGER THESE LISTENERS IN CHAIN THAT IS
            // TO MAKE SURE ALL THESE ACTIVITIES ARE ACTIVATED COMPLETELY AND IN ORDERLY

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void spDistrictTriggeredActivities() {
        try {
            SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
            wardList = dvhcHelper.getLocalList(
                    DVHCHelper.WARD_LEVEL, districtOption.getValue());
            spWardListAdapter.setOptionList(wardList);
            spWardListAdapter.notifyDataSetChanged();

            // TRIGGER WARD SPINNER SELECTION FOR THE NEXT ACTIVITIES

            if (spWard.getSelectedItemPosition() == 0) {
                spWardTriggeredActivities();
            } else {
                spWard.setSelection(0, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void spWardTriggeredActivities() {
        SpinnerOption provinceOption = (SpinnerOption) spProvince.getSelectedItem();
        SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
        SpinnerOption wardOption =
                (SpinnerOption) wardList.get(spWard.getSelectedItemPosition());
    }

    public void updateProfile() {
        Citizen profile = new Citizen();
        profile.setFull_name(String.valueOf(etFullName.getText()));
        if (profile.getFull_name().equals("")) {
            Toast.makeText(this, "*Nhập họ và tên", Toast.LENGTH_SHORT).show();
            return;
        }

        profile.setBirthdayFromString(String.valueOf(tvBirthday.getText()));
        if (profile.getBirthdayString().equals("")) {
            Toast.makeText(this, "*Chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rdGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "*Chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }
        rdBtnGender = findViewById(rdGroupGender.getCheckedRadioButtonId());
        profile.setGender(String.valueOf(rdBtnGender.getText()));

        profile.setPhone(String.valueOf(etPhone.getText()));
        if (profile.getPhone().equals("")) {
            Toast.makeText(this, "*Nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }
        
        profile.setId(String.valueOf(etId.getText()));
        if (profile.getId().equals("")) {
            Toast.makeText(this, "*Nhập CCCD", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!String.valueOf(etEmail.getText()).equals(citizen.getEmail())) {
            Toast.makeText(this, "*Email không được thay đổi", Toast.LENGTH_SHORT).show();
            return;
        }
        profile.setEmail(String.valueOf(citizen.getEmail()));

        SpinnerOption spOption = (SpinnerOption) spProvince.getSelectedItem();
        profile.setProvince_name(spOption.getOption());

        spOption = (SpinnerOption) spDistrict.getSelectedItem();
        profile.setDistrict_name(spOption.getOption());

        spOption = (SpinnerOption) spWard.getSelectedItem();
        profile.setWard_name(spOption.getOption());

        profile.setStreet(String.valueOf(etStreet.getText()));

        WriteBatch batch = db.batch();
        DocumentReference profileAccount = db.collection("accounts").document(profile.getEmail());
        DocumentReference oldProfile = db.collection("users").document(this.citizen.getId());

        // IF USER CHANGE THE ID, DELETE THE EXISTING PROFILE WHICH MATCH THE ID
        // UPDATE THE USER_ID FIELD OF THE ACCOUNT WHICH MATCH THE ID
        if (!profile.getId().equals(citizen.getId())) {
            batch.delete(oldProfile);
            batch.update(profileAccount, "user_id", profile.getId());
        }

        // THE SET() OPERATION WILL CREATE A NEW DOCUMENT OR OVERWRITE THE EXISTING PROFILE
        // DEPENDS ON THE ID HAS BEEN CHANGED OR NOT (THE DOCUMENT IS DELETED OR NOT) BEFORE
        DocumentReference newProfile = db.collection("users").document(profile.getId());
        batch.set(newProfile, profile);

        // UPDATE USER ACCOUNT TO MATCH USER ID IN USERS,
        // AND USER ACCOUNT STATUS IS SET TO 1
        batch.update(profileAccount, "user_id", profile.getId());
        batch.update(profileAccount, "status", 1);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CitizenProfileActivity.this,
                            "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CitizenProfileActivity.this,
                            "Đã có lỗi xảy ra", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(CitizenProfileActivity.this.getBaseContext(),
                        CitizenNavigationBottomActivity.class);
                intent.putExtra("username", citizen.getEmail());

                CitizenProfileActivity.this.finish();
                startActivity(intent);
            }
        });

    }
}
