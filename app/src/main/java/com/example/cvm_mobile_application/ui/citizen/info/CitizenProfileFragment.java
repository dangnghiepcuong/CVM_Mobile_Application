package com.example.cvm_mobile_application.ui.citizen.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Form;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.helpers.DVHCHelper;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.home.CitizenNavigationBottomActivity;
import com.example.cvm_mobile_application.ui.citizen.vaccination.CitizenVaccinationState2Activity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck public class CitizenProfileFragment extends Fragment implements ViewStructure {
    private View view;
    private EditText etFullName;
    private RadioButton rdBtnGenderMale;
    private RadioButton rdBtnGenderFemale;
    private RadioButton rdBtnGenderAnother;
    private EditText etPhone;
    private EditText etId;
    private EditText etEmail;
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
    private List<String> registrationHistory;
    private List<Register> registrationList;
    private List<String> formHistory;
    private List<Form> formList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_profile, container, false);

        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(requireContext());

        citizen = requireArguments().getParcelable("citizen");

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    @Override
    public void implementView() {
        etFullName = view.findViewById(R.id.et_fullname);

        tvBirthday = view.findViewById(R.id.tv_birthday);
        btnBirthdayDP = view.findViewById(R.id.btn_birthday_dp);
        dpBirthday = view.findViewById(R.id.dp_birthday);

        rdGroupGender = view.findViewById(R.id.rd_group_gender);
        rdBtnGenderMale = view.findViewById(R.id.rd_btn_gender_male);
        rdBtnGenderFemale = view.findViewById(R.id.rd_btn_gender_female);
        rdBtnGenderAnother = view.findViewById(R.id.rd_btn_gender_another);
        etPhone = view.findViewById(R.id.et_phone);
        etId = view.findViewById(R.id.et_id);
        etEmail = view.findViewById(R.id.et_email);

        dvhcHelper.setSpProvince(view.findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(view.findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(view.findViewById(R.id.sp_ward));

        etStreet = view.findViewById(R.id.et_street);

        btnSaveProfile = view.findViewById(R.id.btn_save_profile);
    }

    @Override
    public void bindViewData() {
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
        try {
            dvhcHelper.bindLocalListSpinnerData(getContext(),
                    citizen.getProvince_name(),
                    citizen.getDistrict_name(),
                    citizen.getWard_name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        etStreet.setText(citizen.getStreet());
    }

    @Override
    public void setViewListener() {
        btnBirthdayDP.setOnClickListener(v -> {
            if (dpBirthday.getVisibility() == View.GONE) {
                dpBirthday.setVisibility(View.VISIBLE);
            } else {
                dpBirthday.setVisibility(View.GONE);
            }
        });

        dpBirthday.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            tvBirthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        });

        dvhcHelper.setLocalListSpinnerListener(() -> {});

        btnSaveProfile.setOnClickListener(view -> CitizenProfileFragment.this.getUserTransactionData());
    }

    public void getUserTransactionData() {
        registrationHistory = new ArrayList<>();
        registrationList = new ArrayList<>();
        formHistory = new ArrayList<>();
        formList = new ArrayList<>();
        getUserRegistrationHistory();
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
                        CitizenProfileFragment.this.getUserFormHistory();
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
                        CitizenProfileFragment.this.updateProfile();
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

        SpinnerOption spOption = dvhcHelper.getSelectedLocal(DVHCHelper.PROVINCE_LEVEL);
        profile.setProvince_name(spOption.getOption());

        spOption = dvhcHelper.getSelectedLocal(DVHCHelper.DISTRICT_LEVEL);
        profile.setDistrict_name(spOption.getOption());

        spOption = dvhcHelper.getSelectedLocal(DVHCHelper.WARD_LEVEL);
        profile.setWard_name(spOption.getOption());

        profile.setStreet(String.valueOf(etStreet.getText()));

        WriteBatch batch = db.batch();
        DocumentReference profileAccount = db.collection("accounts").document(profile.getEmail());
        DocumentReference oldProfile = db.collection("users").document(citizen.getId());

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
                String newId = profile.getId() + "#"
                        + registrationList.get(i).getSchedule_id();

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
                Toast.makeText(getContext(),
                        "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),
                        "Đã có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }

            String fromActivity = requireArguments().getString("fromActivity");
            Intent intent;
            switch (fromActivity) {
                case "CitizenProfile":
                    intent = new Intent(getActivity(),
                            CitizenNavigationBottomActivity.class);
                    intent.putExtra("username", citizen.getEmail());

                    requireActivity().finish();
                    startActivity(intent);
                    break;

                case "CitizenVaccinationState1":
                    intent = new Intent(getActivity(),
                            CitizenVaccinationState2Activity.class);
                    intent.putExtra("citizen", citizen);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        });
    }
}
