package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.objects.DVHCHelper;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState1Fragment extends Fragment implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private List<Citizen> relatives;
    private View view;
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
    private LinearLayout btnDetailPersonalInfo;
    private LinearLayout layoutDetailPersonalInfo;
    private EditText etFullName;
    private SpinnerAdapter spTargetListAdapter;
    private Button btnSave;
    private FragmentManager fragmentManager;
    private CitizenVaccinationState2Fragment state2Fragment;
    private RadioButton rdBtnGenderMale;
    private RadioButton rdBtnGenderFemale;
    private RadioButton rdBtnGenderOther;
    private RadioGroup rdGroupGender;
    private RadioButton rdBtnGender;

    public Citizen getTarget() {
        return citizen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(requireActivity().getApplicationContext());

        //GET CITIZEN DATA FROM ACTIVITY
        citizen = requireArguments().getParcelable("citizen");
        SpinnerOption spOption = new SpinnerOption(citizen.getFull_name(), citizen.getId());
        targetList = new ArrayList<>();
        targetList.add(spOption);

        //GET CITIZEN RELATIVES DATA FROM FIREBASE
        getTargetRelativesData();

        try {
            implementView();
            bindViewData();
            setViewListener();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    @Override
    public void implementView() {
        spTargetList = view.findViewById(R.id.sp_target_list);
        spTargetListAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spTargetListAdapter);

        btnDetailPersonalInfo = view.findViewById(R.id.btn_detail_personal_info);
        layoutDetailPersonalInfo = view.findViewById(R.id.layout_detail_personal_info);

        etFullName = view.findViewById(R.id.et_fullname);

        tvBirthday = view.findViewById(R.id.tv_birthday);
        btnBirthdayDP = view.findViewById(R.id.btn_birthday_dp);
        dpBirhtday = view.findViewById(R.id.dp_birthday);

        rdGroupGender = view.findViewById(R.id.rd_group_gender);
        rdBtnGenderMale = view.findViewById(R.id.rd_btn_gender_male);
        rdBtnGenderFemale = view.findViewById(R.id.rd_btn_gender_female);
        rdBtnGenderOther = view.findViewById(R.id.rd_btn_gender_another);

        etPhone = view.findViewById(R.id.et_phone);
        etId = view.findViewById(R.id.et_id);
        etEmail = view.findViewById(R.id.et_email);

        dvhcHelper.setSpProvince(view.findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(view.findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(view.findViewById(R.id.sp_ward));

        spProvince = dvhcHelper.getSpProvince();
        spDistrict = dvhcHelper.getSpDistrict();
        spWard = dvhcHelper.getSpWard();

        etStreet = view.findViewById(R.id.et_street);

        btnSave = view.findViewById(R.id.btn_next);
    }

    @Override
    public void bindViewData() throws JSONException {
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
        dvhcHelper.bindLocalListSpinnerData(getContext(), citizen);

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
                CitizenVaccinationState1Fragment.this.getTargetData();
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

        btnBirthdayDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dpBirhtday.getVisibility() == View.GONE) {
                    dpBirhtday.setVisibility(View.VISIBLE);
                } else {
                    dpBirhtday.setVisibility(View.GONE);
                }
            }
        });

        dpBirhtday.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tvBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });

        dvhcHelper.setLocalListSpinnerListener();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UPDATE PROFILE THEN UPDATE UI TO NEXT SCREEN
                CitizenVaccinationState1Fragment.this.updateProfile();
            }
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

                        try {
                            CitizenVaccinationState1Fragment.this.bindViewData();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    public void updateProfile() {
        Citizen profile = new Citizen();
        profile.setFull_name(String.valueOf(etFullName.getText()));
        if (profile.getFull_name().equals("")) {
            Toast.makeText(getContext(), "*Nhập họ và tên", Toast.LENGTH_SHORT).show();
            return;
        }

        profile.setBirthdayFromString(String.valueOf(tvBirthday.getText()));
        if (profile.getBirthdayString().equals("")) {
            Toast.makeText(getContext(), "*Chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rdGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "*Chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }
        rdBtnGender = view.findViewById(rdGroupGender.getCheckedRadioButtonId());
        profile.setGender(String.valueOf(rdBtnGender.getText()));

        profile.setPhone(String.valueOf(etPhone.getText()));
        if (profile.getPhone().equals("")) {
            Toast.makeText(getContext(), "*Nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        profile.setId(String.valueOf(etId.getText()));
        if (profile.getId().equals("")) {
            Toast.makeText(getContext(), "*Nhập CCCD", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!String.valueOf(etEmail.getText()).equals(citizen.getEmail())) {
            Toast.makeText(getContext(), "*Email không được thay đổi", Toast.LENGTH_SHORT).show();
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
                CitizenVaccinationState1Fragment.this.citizen = profile;

                Bundle bundle = new Bundle();
                bundle.putParcelable("citizen", citizen);

                state2Fragment = new CitizenVaccinationState2Fragment();
                state2Fragment.setArguments(bundle);
                CitizenVaccinationState1Fragment.this.replaceFragment(state2Fragment);
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
