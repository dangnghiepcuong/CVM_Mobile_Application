package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.objects.DVHCHelper;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState1Fragment extends Fragment {
    private FirebaseFirestore db;
    private Citizen citizen;
    private List<Citizen> relatives;
    private View view;
    private Spinner spTargetList;
    private List<SpinnerOption> targetList;
    private String selectedTargetId;

    private EditText etTargetId;
    private EditText etTargetBirthday;
    private RadioButton rdBtnTargetGender;
    private EditText etTargetPhone;
    private EditText etTargetEmail;
    private EditText etTargetStreet;
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
    private EditText etTargetFullName;
    private SpinnerAdapter spTargetListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getActivity().getApplicationContext());

        //GET CITIZEN DATA FROM ACTIVITY
        citizen = getArguments().getParcelable("citizen");
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

    public void implementView() throws JSONException {
        spTargetList = view.findViewById(R.id.sp_target_list);

        btnDetailPersonalInfo = view.findViewById(R.id.btn_detail_personal_info);
        layoutDetailPersonalInfo = view.findViewById(R.id.layout_detail_personal_info);

        etTargetFullName = view.findViewById(R.id.et_fullname);
        etTargetBirthday = view.findViewById(R.id.et_target_birthday);
        etTargetPhone = view.findViewById(R.id.et_target_phone);
        etTargetId = view.findViewById(R.id.et_target_id);
        etTargetEmail = view.findViewById(R.id.et_target_email);
        spProvince = view.findViewById(R.id.sp_province);
        spDistrict = view.findViewById(R.id.sp_district);
        spWard = view.findViewById(R.id.sp_ward);
        etTargetStreet = view.findViewById(R.id.et_target_street);
    }

    public void getTargetData() {
//        QuerySnapshot document =
        db.collection("users")
                .whereEqualTo("id", selectedTargetId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }

    public void getTargetRelativesData() {
        db.collection("users")
                .whereEqualTo("id", "07202007263")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            relatives = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Citizen relative = document.toObject(Citizen.class);
                                relatives.add(relative);
                                SpinnerOption spinnerOption = new SpinnerOption(relative.getFull_name(), relative.getId());
                                targetList.add(spinnerOption);
                            }
                        }
                    }
                });
    }

    public void bindViewData() throws JSONException {
        spTargetListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spTargetListAdapter);

        selectedTargetId = citizen.getId();

        //SET FULL NAME INFO VALUE
        etTargetFullName.setText(citizen.getFull_name());

        // SET BIRTHDAY INFO VALUE
        etTargetBirthday.setText(citizen.getBirthday());

        // SET GENDER INFO VALUE
        switch (citizen.getGender()) {
            case "Nam":
                rdBtnTargetGender = view.findViewById(R.id.rd_btn_target_gender_male);
                break;
            case "Nữ":
                rdBtnTargetGender = view.findViewById(R.id.rd_btn_target_gender_female);
                break;
            case "Khác":
                rdBtnTargetGender = view.findViewById(R.id.rd_btn_target_gender_another);
                break;
        }
        rdBtnTargetGender.setChecked(true);

        //SET PHONE INFO VALUE
        etTargetPhone.setText(citizen.getPhone());

        //SET ID INFO VALUE
        etTargetId.setText(selectedTargetId);

        //SET EMAIL INFO VALUE
        etTargetEmail.setText(citizen.getEmail());

        //GET PROVINCE LIST
        provinceList = dvhcHelper.getLocalList(DVHCHelper.PROVINCE_LEVEL, null);

        //SET PROVINCE INFO VALUE
        spProvinceListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, provinceList);
        spProvince.setAdapter(spProvinceListAdapter);
        int provincePosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.PROVINCE_LEVEL, citizen.getProvince_name(), null);
        spProvince.setSelection(provincePosition);

        //GET DISTRICT LIST
        String provinceCode = ((SpinnerOption) spProvince.getItemAtPosition(spProvince.getSelectedItemPosition())).getValue();
        districtList = dvhcHelper.getLocalList(DVHCHelper.DISTRICT_LEVEL, provinceCode);

        //SET DISTRICT INFO VALUE
        spDistrictListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, districtList);
        spDistrict.setAdapter(spDistrictListAdapter);
        int districtPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.DISTRICT_LEVEL, citizen.getDistrict_name(), provinceCode);
        spDistrict.setSelection(districtPosition);

        //GET WARD LIST
        String districtCode = ((SpinnerOption) spDistrict.getItemAtPosition(spDistrict.getSelectedItemPosition())).getValue();
        wardList = dvhcHelper.getLocalList(DVHCHelper.WARD_LEVEL, districtCode);

        //SET WARD INFO VALUE
        spWardListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.WARD_LEVEL, citizen.getWard_name(), districtCode);
        spWard.setSelection(wardPosition);

        //SET ADDRESS INFO VALUE
        etTargetStreet.setText(citizen.getStreet());
    }

    public void setViewListener() {
        //SET TARGET SPINNER LISTENER
        spTargetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getItemAtPosition(position);
                selectedTargetId = option.getValue();
                etTargetId.setText(selectedTargetId);
                Log.i("myTAG", selectedTargetId);
                CitizenVaccinationState1Fragment.this.getTargetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //SET DETAIL PERSONAL INFO BUTTON LISTENER
        btnDetailPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = layoutDetailPersonalInfo.getVisibility();
                switch (visibility) {
                    case View.GONE:
                        layoutDetailPersonalInfo.setVisibility(View.VISIBLE);
                        break;

                    case View.VISIBLE:
                    default:
                        layoutDetailPersonalInfo.setVisibility(View.GONE);
                }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
