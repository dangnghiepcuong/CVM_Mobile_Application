package com.example.cvm_mobile_application.ui.citizen.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.vaccination.CitizenVaccinationState1Activity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck public class CitizenRegistrationFragment extends Fragment implements ViewStructure {
    private FirebaseFirestore db;
    private RecyclerView recyclerViewRegistrationHistoryList;
    private VaccinationRegistrationAdapter vaccinationRegistrationAdapter;
    private List<Register> registrationHistoryList;
    private View view;
    private Citizen citizen;
    private String fromActivity;
    private Button btnRegisterVaccination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_registration_list, container, false);
        db = FirebaseFirestore.getInstance();
        registrationHistoryList = new ArrayList<>();

        citizen = requireArguments().getParcelable("citizen");
        fromActivity = requireArguments().getString("fromActivity");

        implementView();
        bindViewData();
        setViewListener();

        return view;
    }

    @Override
    public void implementView() {
        recyclerViewRegistrationHistoryList = view.findViewById(R.id.view_recycler_reg_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRegistrationHistoryList.setLayoutManager(linearLayoutManager);

        btnRegisterVaccination = view.findViewById(R.id.btn_register_vaccination);
    }

    @Override
    public void bindViewData() {
        vaccinationRegistrationAdapter = new VaccinationRegistrationAdapter(
                getContext(),
                registrationHistoryList
        );
        getRegistrationHistory();
        recyclerViewRegistrationHistoryList.setAdapter(vaccinationRegistrationAdapter);

        if (fromActivity.equals("org")) {
            btnRegisterVaccination.setVisibility(View.GONE);
        }
    }

    @Override
    public void setViewListener() {
        btnRegisterVaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CitizenVaccinationState1Activity.class);
                intent.putExtra("citizen", citizen);
                startActivity(intent);
            }
        });

    }

    public void getRegistrationHistory() {
        if (fromActivity.equals("org")) {
            db.collection("registry")
                    .whereEqualTo("citizen_id", citizen.getId())
                    .whereEqualTo("status", 2)
                    .get()
                    .addOnCompleteListener(task -> {
                        registrationHistoryList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Register register = document.toObject(Register.class);
                                registrationHistoryList.add(register);
                                CitizenRegistrationFragment.this.getSchedule(register.getSchedule_id(),
                                        registrationHistoryList.size()-1);
                            }
                        }
                    });
        } else {
            db.collection("registry")
                    .whereEqualTo("citizen_id", citizen.getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        registrationHistoryList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Register register = document.toObject(Register.class);
                                registrationHistoryList.add(register);
                                CitizenRegistrationFragment.this.getSchedule(register.getSchedule_id(),
                                        registrationHistoryList.size()-1);
                            }
                        }
                    });
        }

    }

    public void getSchedule(String scheduleId, int position) {
        db.collection("schedules")
                .document(scheduleId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Schedule schedule = task.getResult().toObject(Schedule.class);
                        registrationHistoryList.get(position).setSchedule(schedule);
                        CitizenRegistrationFragment.this.getOrg(
                                registrationHistoryList.get(position).getSchedule().getOrg_id(),
                                position
                        );
                    } else {
                        Toast.makeText(getContext(),
                                "Lỗi truy vấn dữ liệu. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getOrg(String orgId, int position) {
        db.collection("organizations")
                .document(orgId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Organization org = task.getResult().toObject(Organization.class);
                        registrationHistoryList.get(position).getSchedule().setOrg(org);
                        vaccinationRegistrationAdapter.setRegistrationList(registrationHistoryList);
                        vaccinationRegistrationAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),
                                "Lỗi truy vấn dữ liệu. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}