package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private View view;
    private Spinner spTargetList;
    private final List<SpinnerOption> targetList = new ArrayList<>();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getActivity().getApplicationContext());

        try {
            implementView();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    public void implementView() throws JSONException {
        citizen = getArguments().getParcelable("citizen");

        SpinnerOption spinnerOption = new SpinnerOption(citizen.getFull_name(), citizen.getId());
        targetList.add(spinnerOption);

        spTargetList = view.findViewById(R.id.sp_target_list);
        SpinnerAdapter spTargetListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spTargetListAdapter);

        selectedTargetId = citizen.getId();

        etTargetId = view.findViewById(R.id.et_target_id);
        etTargetId.setText(selectedTargetId);

        bindViewData(citizen);

        spTargetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getItemAtPosition(position);
                selectedTargetId = option.getValue();
                etTargetId.setText(selectedTargetId);

                CitizenVaccinationState1Fragment.this.getTargetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getTargetData() {
        db.collection("users")
                .whereEqualTo("id", selectedTargetId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Citizen target = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                target = document.toObject(Citizen.class);
                            }

                            try {
                                CitizenVaccinationState1Fragment.this.bindViewData(target);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    public void bindViewData(Citizen target) throws JSONException {
        // SET BIRTHDAY INFO VALUE
        etTargetBirthday = view.findViewById(R.id.et_target_birthday);
        etTargetBirthday.setText(target.getBirthday());

        // SET GENDER INFO VALUE
        switch (target.getGender()) {
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
        etTargetPhone = view.findViewById(R.id.et_target_phone);
        etTargetPhone.setText(target.getPhone());

        //SET EMAIL INFO VALUE
        etTargetEmail = view.findViewById(R.id.et_target_email);
        etTargetEmail.setText(target.getEmail());


        //GET PROVINCE LIST
        provinceList = dvhcHelper.getLocalList(DVHCHelper.PROVINCE_LEVEL, null);

        //SET PROVINCE INFO VALUE
        spProvince = view.findViewById(R.id.sp_province);
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
        spDistrict = view.findViewById(R.id.sp_district);
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
        spWard = view.findViewById(R.id.sp_ward);
        spWardListAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.WARD_LEVEL, citizen.getWard_name(), districtCode);
        spWard.setSelection(wardPosition);

        etTargetStreet = view.findViewById(R.id.et_target_street);
        etTargetStreet.setText(target.getStreet());

        setViewListener();
    }

    public void setViewListener() {
        //SET PROVINCE SPINNER LISTENER
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET DISTRICT LIST, WARD LIST
                try {
                    SpinnerOption provinceOtion = (SpinnerOption) parent.getItemAtPosition(position);
                    districtList = dvhcHelper.getLocalList(
                            DVHCHelper.DISTRICT_LEVEL, provinceOtion.getValue());
                    spDistrictListAdapter.setOptionList(districtList);
                    spDistrictListAdapter.notifyDataSetChanged();
                    spDistrict.setSelection(0);

                    SpinnerOption districtOption = (SpinnerOption) districtList.get(0);
                    wardList = dvhcHelper.getLocalList(
                            DVHCHelper.WARD_LEVEL, districtOption.getValue());
                    spWardListAdapter.setOptionList(wardList);
                    spWardListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
                    SpinnerOption districtOption = (SpinnerOption) parent.getItemAtPosition(position);
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
