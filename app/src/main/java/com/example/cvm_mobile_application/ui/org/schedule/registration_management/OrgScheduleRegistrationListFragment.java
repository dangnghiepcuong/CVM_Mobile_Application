package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.data.db.model.Shift;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck public class OrgScheduleRegistrationListFragment extends Fragment {
    private View view;
    private Schedule schedule;
    private Spinner spShift;
    private SpinnerAdapter spShiftAdapter;
    private List<SpinnerOption> shiftList;
    private FirebaseFirestore db;
    private LinearLayout btnScheduleFilter;
    private LinearLayout layoutScheduleFilter;
    private List<Register> registryList;
    private Citizen citizen;
    private ScheduleRegistrationAdapter scheduleRegistrationAdapter;
    private RecyclerView recyclerViewRegistryList;
    private Register register;
    private OnRegistrationItemClickListener onRegistrationItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_schedule_registration_list, container, false);

        db = FirebaseFirestore.getInstance();
        schedule = requireArguments().getParcelable("schedule");
        shiftList = new ArrayList<>();
        citizen = new Citizen();

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    private void implementView() {
        btnScheduleFilter = view.findViewById(R.id.btn_schedule_filter);
        layoutScheduleFilter = view.findViewById(R.id.layout_linear_schedule_filter);

        spShift = view.findViewById(R.id.sp_shift);

        recyclerViewRegistryList = view.findViewById(R.id.view_recycler_registry_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRegistryList.setLayoutManager(linearLayoutManager);
    }

    private void bindViewData() {
        for (Shift shift : Shift.values()) {
            SpinnerOption spinnerOption = new SpinnerOption(
                    shift.getShift(), String.valueOf(shift.getValue()));
            shiftList.add(spinnerOption);
        }
        spShiftAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), R.layout.item_string, shiftList);
        spShift.setAdapter(spShiftAdapter);

        getRegisterList();
        scheduleRegistrationAdapter = new ScheduleRegistrationAdapter(
                requireActivity().getApplicationContext(),
                registryList);
        recyclerViewRegistryList.setAdapter(scheduleRegistrationAdapter);
    }

    private void setViewListener() {
        btnScheduleFilter.setOnClickListener(v -> {
            int visibility = layoutScheduleFilter.getVisibility();
            switch (visibility) {
                case View.GONE:
                    layoutScheduleFilter.setVisibility(View.VISIBLE);
                    break;

                case View.VISIBLE:
                default:
                case View.INVISIBLE:
                    layoutScheduleFilter.setVisibility(View.GONE);
            }
        });

        spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgScheduleRegistrationListFragment.this.getRegisterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onRegistrationItemClickListener = item -> getCitizen(item.getCitizen_id());
        scheduleRegistrationAdapter.setListener(onRegistrationItemClickListener);
    }

    private void getRegisterList() {
        String scheduleId = schedule.getId();

        SpinnerOption spOption= (SpinnerOption) spShift.getItemAtPosition(
                spShift.getSelectedItemPosition());
        String shift = spOption.getValue();

        switch(shift){
            case "0":
                shift = "Sáng ";
                break;
            case "1":
                shift = "Chiều ";
                break;
            case "2":
                shift = "Tối ";
                break;
        }

        db.collection("registry")
                .whereEqualTo("schedule_id", scheduleId)
                .whereEqualTo("shift", shift)
//                .orderBy("num_order", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        registryList = new ArrayList<>();
                        register = new Register();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            register = document.toObject(Register.class);
                            registryList.add(register);
                        }
                        scheduleRegistrationAdapter.setRegistryList(registryList);
                        scheduleRegistrationAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getCitizen(String id){
        db.collection("users")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        citizen = new Citizen();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            citizen = document.toObject(Citizen.class);
                            Intent intent = new Intent(getContext(), OrgViewVaccinationProfile.class);
                            intent.putExtra("citizen", citizen);
                            startActivity(intent);
                        }
                    }
                });
    }

}
