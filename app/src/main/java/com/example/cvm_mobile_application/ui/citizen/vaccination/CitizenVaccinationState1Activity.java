package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Form;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.helpers.DVHCHelper;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState1Activity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private List<Citizen> relatives;
    private Spinner spTargetList;
    private List<SpinnerOption> targetList;
    private String selectedTargetId;
    private EditText etId;
    private TextView tvBirthday;
    private Button btnBirthdayDP;
    private DatePicker dpBirhtday;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etStreet;
    private DVHCHelper dvhcHelper;
    private LinearLayout btnDetailPersonalInfo;
    private LinearLayout layoutDetailPersonalInfo;
    private EditText etFullName;
    private SpinnerAdapter spTargetListAdapter;
    private Button btnSave;
    private RadioButton rdBtnGenderMale;
    private RadioButton rdBtnGenderFemale;
    private RadioButton rdBtnGenderOther;
    private RadioGroup rdGroupGender;
    private RadioButton rdBtnGender;
    private List<String> registrationHistory;
    private List<Register> registrationList;
    private List<String> formHistory;
    private List<Form> formList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_vaccination_state1);

        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        //GET CITIZEN DATA FROM ACTIVITY
        citizen = getIntent().getParcelableExtra("citizen");
        SpinnerOption spOption = new SpinnerOption(citizen.getFull_name(), citizen.getId());
        targetList = new ArrayList<>();
        targetList.add(spOption);

        //GET CITIZEN RELATIVES DATA FROM FIREBASE
        getTargetRelativesData();

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        spTargetList = findViewById(R.id.sp_target_list);
        spTargetListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spTargetListAdapter);

        btnDetailPersonalInfo = findViewById(R.id.btn_detail_personal_info);
        layoutDetailPersonalInfo = findViewById(R.id.layout_detail_personal_info);

        etFullName = findViewById(R.id.et_fullname);

        tvBirthday = findViewById(R.id.tv_birthday);
        btnBirthdayDP = findViewById(R.id.btn_birthday_dp);
        dpBirhtday = findViewById(R.id.dp_birthday);

        rdGroupGender = findViewById(R.id.rd_group_gender);
        rdBtnGenderMale = findViewById(R.id.rd_btn_gender_male);
        rdBtnGenderFemale = findViewById(R.id.rd_btn_gender_female);
        rdBtnGenderOther = findViewById(R.id.rd_btn_gender_another);

        etPhone = findViewById(R.id.et_phone);
        etId = findViewById(R.id.et_id);
        etEmail = findViewById(R.id.et_email);

        dvhcHelper.setSpProvince(findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(findViewById(R.id.sp_ward));

        etStreet = findViewById(R.id.et_street);

        btnSave = findViewById(R.id.btn_next);
    }

    @Override
    public void bindViewData() {
        //SET TARGET ID
        selectedTargetId = citizen.getId();

        //SET FULL NAME INFO VALUE
        etFullName.setText(citizen.getFull_name());

        // SET BIRTHDAY INFO VALUE
        tvBirthday.setText(citizen.getBirthdayString());

        // SET GENDER INFO VALUE
        switch (citizen.getGender()) {
            case "Nam":
                rdBtnGenderMale.setChecked(true);
                break;
            case "Nữ":
                rdBtnGenderFemale.setChecked(true);
                break;
            default:
            case "Khác":
                rdBtnGenderOther.setChecked(true);
                break;
        }

        //SET PHONE INFO VALUE
        etPhone.setText(citizen.getPhone());

        //SET ID INFO VALUE
        etId.setText(selectedTargetId);

        //SET EMAIL INFO VALUE
        etEmail.setText(citizen.getEmail());

        //SET LOCAL VALUE
        try {
            dvhcHelper.bindLocalListSpinnerData(getApplicationContext(),
                    citizen.getProvince_name(),
                    citizen.getDistrict_name(),
                    citizen.getWard_name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //SET ADDRESS INFO VALUE
        etStreet.setText(citizen.getStreet());
    }

    @Override
    public void setViewListener() {
        //SET TARGET SPINNER LISTENER
        spTargetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getItemAtPosition(position);

                // AVOID THE FIRST TRIGGER WHEN INITIALIZING SPINNER,
                // AND KEEP THE TRIGGERING OF NEXT SELECTION ON THE FIRST SELECTION
                if (selectedTargetId.equals(option.getValue())) {
                    return;
                }

                selectedTargetId = option.getValue();
                Log.i("myTAG", selectedTargetId);
                CitizenVaccinationState1Activity.this.getTargetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SET DETAIL PERSONAL INFO BUTTON LISTENER
        btnDetailPersonalInfo.setOnClickListener(v -> {
            int visibility = layoutDetailPersonalInfo.getVisibility();
            switch (visibility) {
                case View.GONE:
                    layoutDetailPersonalInfo.setVisibility(View.VISIBLE);
                    break;

                case View.VISIBLE:
                default:
                case View.INVISIBLE:
                    layoutDetailPersonalInfo.setVisibility(View.GONE);
            }
        });

        btnBirthdayDP.setOnClickListener(v -> {
            if (dpBirhtday.getVisibility() == View.GONE) {
                dpBirhtday.setVisibility(View.VISIBLE);
            } else {
                dpBirhtday.setVisibility(View.GONE);
            }
        });

        dpBirhtday.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            tvBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        });

        dvhcHelper.setLocalListSpinnerListener();

        btnSave.setOnClickListener(v -> {
            // UPDATE PROFILE THEN UPDATE UI TO NEXT SCREEN
            CitizenVaccinationState1Activity.this.updateProfile();
        });
    }

    public void getTargetData() {
        db.collection("users")
                .whereEqualTo("id", selectedTargetId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        citizen = new Citizen();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            citizen = document.toObject(Citizen.class);
                        }

                        CitizenVaccinationState1Activity.this.bindViewData();
                    }
                });
    }

    public void getTargetRelativesData() {
        db.collection("users")
                .whereEqualTo("guardian", citizen.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        relatives = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Citizen relative = document.toObject(Citizen.class);
                            relatives.add(relative);
                            SpinnerOption spinnerOption = new SpinnerOption(relative.getFull_name(), relative.getId());
                            targetList.add(spinnerOption);
                        }
                        spTargetListAdapter.setOptionList(targetList);
                        spTargetListAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getUserRegistrationHistory() {
        db.collection("registry")
                .whereEqualTo("citizen_id", citizen.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            registrationHistory.add(document.getId());
                            Register register = document.toObject(Register.class);
                            registrationList.add(register);
                        }
                        CitizenVaccinationState1Activity.this.getUserFormHistory();
                    }
                });
    }

    public void getUserFormHistory() {
        db.collection("forms")
                .whereEqualTo("citizen_id", citizen.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            formHistory.add(document.getId());
                            Form form = document.toObject(Form.class);
                            formList.add(form);
                        }
                        CitizenVaccinationState1Activity.this.updateProfile();
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

        SpinnerOption spOption = dvhcHelper.getSelectedLocal(DVHCHelper.PROVINCE_LEVEL);
        profile.setProvince_name(spOption.getOption());

        spOption = dvhcHelper.getSelectedLocal(DVHCHelper.DISTRICT_LEVEL);
        profile.setDistrict_name(spOption.getOption());

        spOption = dvhcHelper.getSelectedLocal(DVHCHelper.WARD_LEVEL);
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

            // UPDATE USER TRANSACTION DATA
            int i = 0;
            for (String id : registrationHistory) {
                // get the registration ref (which has old id) from Firestore and delete it
                DocumentReference registrationRef = db.collection("registry").document(id);
                batch.delete(registrationRef);

                // create new document id (ref) for the registration
                String newId = profile.getId()
                        + registrationList.get(i).getSchedule().getId()
                        + registrationList.get(i).getId();

                // update the object transaction data
                registrationList.get(i).setCitizen_id(profile.getId());

                // set a new registration document with the id above in Firestore
                DocumentReference newRegistrationRef = db.collection("registry").document(newId);
                batch.set(newRegistrationRef, registrationList.get(i));

                i++;
            }

            for (String id : formHistory) {
                // get the form ref and update citizen_id
                DocumentReference formRef = db.collection("forms").document(id);
                batch.update(formRef, "citizen_id", profile.getId());
            }
        }

        // THE SET() OPERATION WILL CREATE A NEW DOCUMENT OR OVERWRITE THE EXISTING PROFILE
        // DEPENDS ON THE ID HAS BEEN CHANGED OR NOT (THE DOCUMENT IS DELETED OR NOT) BEFORE
        DocumentReference newProfile = db.collection("users").document(profile.getId());
        batch.set(newProfile, profile);

        // UPDATE USER ACCOUNT TO MATCH USER ID IN USERS,
        // AND USER ACCOUNT STATUS IS SET TO 1
        batch.update(profileAccount, "user_id", profile.getId());
        batch.update(profileAccount, "status", 1);

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CitizenVaccinationState1Activity.this,
                        "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show();
                citizen = profile;
            } else {
                Toast.makeText(CitizenVaccinationState1Activity.this,
                        "Đã có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(CitizenVaccinationState1Activity.this.getBaseContext(),
                    CitizenVaccinationState2Activity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });
    }
}
