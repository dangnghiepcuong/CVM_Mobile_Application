package com.example.cvm_mobile_application.ui.citizen.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CitizenRegistrationFragment extends Fragment implements ViewStructure {
    private FirebaseFirestore db;

    private RecyclerView recyclerViewRegistrationHistoryList;
    private VaccinationRegistrationAdapter vaccinationRegistrationAdapter;
    private List<Register> registrationHistoryList;
    private View view;
    private Citizen citizen;
    private String fromActivity;

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
    }

    @Override
    public void bindViewData() {
        vaccinationRegistrationAdapter = new VaccinationRegistrationAdapter(
                getContext(),
                registrationHistoryList
        );
        getRegistrationHistory();
        recyclerViewRegistrationHistoryList.setAdapter(vaccinationRegistrationAdapter);
    }

    @Override
    public void setViewListener() {

    }

    public void getRegistrationHistory() {
        int status = 0;
        if (fromActivity.equals("org")) {
            status = 0;
        }

        db.collection("registry")
                .whereEqualTo("citizen_id", citizen.getId())
                .whereEqualTo("status", status)
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