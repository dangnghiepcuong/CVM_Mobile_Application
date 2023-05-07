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
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState1Fragment extends Fragment {
    private FirebaseFirestore db;
    private Citizen citizen;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state1, container, false);
        db = FirebaseFirestore.getInstance();
        getViewData();
        return view;
    }

    public void getViewData() {
        citizen = getArguments().getParcelable("citizen");

        targetList = new ArrayList<>();
        SpinnerOption spinnerOption = new SpinnerOption(citizen.getFull_name(), citizen.getId());
        targetList.add(spinnerOption);

        spTargetList = view.findViewById(R.id.sp_target_list);
        SpinnerAdapter spAdapter = new SpinnerAdapter(getActivity().getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spAdapter);

        selectedTargetId = citizen.getId();

        etTargetId = view.findViewById(R.id.et_target_id);
        etTargetId.setText(selectedTargetId);

        bindViewData(citizen);

        spTargetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getItemAtPosition(position);
                selectedTargetId = option.getValue();

                etTargetId = CitizenVaccinationState1Fragment.this.view.findViewById(R.id.et_target_id);
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

                            CitizenVaccinationState1Fragment.this.bindViewData(target);
                        }
                    }
                });
    }


    public void bindViewData(Citizen target) {
        etTargetBirthday = view.findViewById(R.id.et_target_birthday);
        etTargetBirthday.setText(target.getBirthday());

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

        etTargetPhone = view.findViewById(R.id.et_target_phone);
        etTargetPhone.setText(target.getPhone());

        etTargetEmail = view.findViewById(R.id.et_target_email);
        etTargetEmail.setText(target.getEmail());

        etTargetStreet = view.findViewById(R.id.et_target_street);
        etTargetStreet.setText(target.getStreet());
    }
}
