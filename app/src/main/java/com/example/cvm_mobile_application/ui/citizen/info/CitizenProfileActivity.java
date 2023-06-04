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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_citizen_profile);
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
        spProvince.setSelection(provincePosition);

        //GET DISTRICT LIST
        String provinceCode = ((SpinnerOption) spProvince.getItemAtPosition(spProvince.getSelectedItemPosition())).getValue();
        districtList = dvhcHelper.getLocalList(DVHCHelper.DISTRICT_LEVEL, provinceCode);

        //SET DISTRICT INFO VALUE
        spDistrictListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, districtList);
        spDistrict.setAdapter(spDistrictListAdapter);
        int districtPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.DISTRICT_LEVEL, citizen.getDistrict_name(), provinceCode);
        spDistrict.setSelection(districtPosition);

        //GET WARD LIST
        String districtCode = ((SpinnerOption) spDistrict.getItemAtPosition(spDistrict.getSelectedItemPosition())).getValue();
        wardList = dvhcHelper.getLocalList(DVHCHelper.WARD_LEVEL, districtCode);

        //SET WARD INFO VALUE
        spWardListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.WARD_LEVEL, citizen.getWard_name(), districtCode);
        spWard.setSelection(wardPosition);

        etStreet.setText(citizen.getStreet());
    }

    @Override
    public void setViewListener() {

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
                try {
                    SpinnerOption provinceOtion = (SpinnerOption) provinceList.get(position);
                    districtList = dvhcHelper.getLocalList(
                            DVHCHelper.DISTRICT_LEVEL, provinceOtion.getValue());
                    spDistrictListAdapter.setOptionList(districtList);
                    spDistrictListAdapter.notifyDataSetChanged();
//                    spDistrict.setSelection(0);

                    // Changing selected province triggers district listener to change district list.
                    // And when changing district list, we also need to change ward list,
                    // but changing district list does not mean that
                    // selected district position on spinner would change too,
                    // which triggers ward listener to change ward list
                    // So we need to check whether the selected district is still stay 0
                    // or it has been changed before
                    if (spDistrict.getSelectedItemPosition() == 0) {
                        // if selected district still stay 0, actively change ward list
                        SpinnerOption districtOption = (SpinnerOption) districtList.get(0);
                        wardList = dvhcHelper.getLocalList(
                                DVHCHelper.WARD_LEVEL, districtOption.getValue());
                        spWardListAdapter.setOptionList(wardList);
                        spWardListAdapter.notifyDataSetChanged();
                    } else {
                        // if selected district has been changed before, setSelection back to 0
                        // and animate=true to trigger ward list listener
                        spDistrict.setSelection(0, true);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    SpinnerOption districtOption = (SpinnerOption) districtList.get(position);
                    wardList = dvhcHelper.getLocalList(
                            DVHCHelper.WARD_LEVEL, districtOption.getValue());
                    spWardListAdapter.setOptionList(wardList);
                    spWardListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Log.i("myTAG", "district spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        // USER CHANGE EMAIL, THEN DELETE THE OLD DOCUMENT
        if (!profile.getEmail().equals(citizen.getEmail())) {
            DocumentReference oldProfile = db.collection("users").document(this.citizen.getEmail());
            batch.delete(oldProfile);
        }
        // THE SET() OPERATION WILL CREATE A NEW DOCUMENT OR OVER EXISTING
        // DEPENDS ON THE EMAIL HAS BEEN CHANGED OR NOT (THE DOCUMENT IS DELETED OR NOT)
        DocumentReference newProfile = db.collection("users").document(profile.getEmail());
        batch.set(newProfile, profile);

        DocumentReference profileAccount = db.collection("accounts").document(profile.getEmail());
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
                startActivity(intent);
            }
        });

    }
}
