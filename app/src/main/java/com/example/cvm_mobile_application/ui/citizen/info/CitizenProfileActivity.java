package com.example.cvm_mobile_application.ui.citizen.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        dvhcHelper.setSpProvince(findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(findViewById(R.id.sp_ward));

        spProvince = dvhcHelper.getSpProvince();
        spDistrict = dvhcHelper.getSpDistrict();
        spWard = dvhcHelper.getSpWard();

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

        //SET LOCAL VALUE
        dvhcHelper.bindLocalListSpinnerData(getApplicationContext(), citizen);

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

        dvhcHelper.setLocalListSpinnerListener();

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitizenProfileActivity.this.updateProfile();
            }
        });
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

        SpinnerOption spOption = (SpinnerOption) dvhcHelper.getSelectedLocal(DVHCHelper.PROVINCE_LEVEL);
        profile.setProvince_name(spOption.getOption());

        spOption = (SpinnerOption) dvhcHelper.getSelectedLocal(DVHCHelper.DISTRICT_LEVEL);
        profile.setDistrict_name(spOption.getOption());

        spOption = (SpinnerOption) dvhcHelper.getSelectedLocal(DVHCHelper.WARD_LEVEL);
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