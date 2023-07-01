package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck
public class OrgScheduleRegistrationListFragment extends Fragment {
    private View view;
    private Schedule schedule;
    private Spinner spShift;
    private SpinnerAdapter spShiftAdapter;
    private List<SpinnerOption> shiftList;
    private FirebaseFirestore db;
    private List<Register> registrationList;
    private Citizen citizen;
    private ScheduleRegistrationAdapter scheduleRegistrationAdapter;
    private RecyclerView recyclerViewRegistrationList;
    private Register register;
    private AdapterView.OnItemSelectedListener onMoreOptionsClickListener;
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_schedule_registration_list, container, false);

        db = FirebaseFirestore.getInstance();
        schedule = requireArguments().getParcelable("schedule");
        shiftList = new ArrayList<>();
        citizen = new Citizen();
        registrationList = new ArrayList<>();

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    private void implementView() {
        spShift = view.findViewById(R.id.sp_shift);

        recyclerViewRegistrationList = view.findViewById(R.id.view_recycler_registry_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewRegistrationList.setLayoutManager(linearLayoutManager);
    }

    private void bindViewData() {
        for (Shift shift : Shift.values()) {
            SpinnerOption spinnerOption = new SpinnerOption(
                    shift.getShift(), String.valueOf(shift.getValue()));
            shiftList.add(spinnerOption);
        }
        spShiftAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), R.layout.item_string, shiftList);
        spShift.setAdapter(spShiftAdapter);

        scheduleRegistrationAdapter = new ScheduleRegistrationAdapter(
                requireActivity().getApplicationContext(),
                registrationList);
        getRegisterList();
        recyclerViewRegistrationList.setAdapter(scheduleRegistrationAdapter);
    }

    private void setViewListener() {
        spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgScheduleRegistrationListFragment.this.getRegisterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onMoreOptionsClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getSelectedItem();

                String[] registryValues = String.valueOf(option.getValue()).split("#");
                String updateOption = registryValues[0];
                String citizenId = registryValues[1];
                String shift = registryValues[2];

                switch (updateOption) {
                    case "-1":
                        OrgScheduleRegistrationListFragment.this.getCitizenVaccinationProfile(citizenId);
                        break;
                    case "1":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 1);
                        break;

                    case "2":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 2);
                        break;

                    case "3":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 3);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

//        scheduleRegistrationAdapter.setListener(onMoreOptionsClickListener);

        onMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String itemValue = String.valueOf(item.getContentDescription());

                String[] registryValues = String.valueOf(itemValue).split("#");
                String updateOption = registryValues[0];
                String citizenId = registryValues[1];
                String shift = registryValues[2];

                switch (updateOption) {
                    case "-1":
                        OrgScheduleRegistrationListFragment.this.getCitizenVaccinationProfile(citizenId);
                        break;
                    case "1":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 1);
                        break;

                    case "2":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 2);
                        break;

                    case "3":
                        OrgScheduleRegistrationListFragment.this.updateRegistration(citizenId, shift, 3);
                        break;

                    default:
                        break;
                }
                return false;
            }
        };

        scheduleRegistrationAdapter.setListener(onMenuItemClickListener);
    }

    private void getRegisterList() {
        String scheduleId = schedule.getId();

        SpinnerOption spOption = (SpinnerOption) spShift.getItemAtPosition(
                spShift.getSelectedItemPosition());
        String shift = spOption.getValue();

        switch (shift) {
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
                .orderBy("number_order", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationList = new ArrayList<>();
                        register = new Register();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            register = document.toObject(Register.class);
                            registrationList.add(register);
                        }
                        scheduleRegistrationAdapter.setRegistryList(registrationList);
                        scheduleRegistrationAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getCitizenVaccinationProfile(String id) {
        db.collection("users")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
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

    public void updateRegistration(String citizenId, String shift, int status) {
        String registrationId = citizenId + "#"
                + schedule.getId();

        DocumentReference registryRef = db.collection("registry").document(registrationId);
        DocumentReference scheduleRef = db.collection("schedules").document(schedule.getId());
        DocumentReference certificateRef = db.collection("certificates").document(citizenId);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot scheduleSnapshot = transaction.get(scheduleRef);
                Long dayRegistered = scheduleSnapshot.getLong("day_registered");
                Long noonRegistered = scheduleSnapshot.getLong("noon_registered");
                Long nightRegistered = scheduleSnapshot.getLong("night_registered");

                DocumentSnapshot certificateSnapshot = transaction.get(certificateRef);
                Long doses = certificateSnapshot.getLong("doses");

                switch (status) {
                    case 0:
                        break;

                    // ROLL UP
                    case 1:
                        transaction.update(registryRef, "status", status);
                        break;

                    // INJECTED
                    case 2:
                        transaction.update(registryRef, "status", status);
                        transaction.update(certificateRef, "doses", doses + 1);
                        break;

                    // CANCELED
                    case 3:
                        transaction.update(registryRef, "status", status);
                        switch (shift) {
                            case "Sáng":
                                transaction.update(scheduleRef, "day_registered", dayRegistered - 1);
                                break;

                            case "Chiều":
                                transaction.update(scheduleRef, "noon_registered", noonRegistered - 1);
                                break;

                            case "Tối":
                                transaction.update(scheduleRef, "night_registered", nightRegistered - 1);
                                break;

                            default:
                                break;
                        }
                        break;
                }
                return null;
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String notification = "";
                switch (status) {
                    case 1:
                        notification = "Điểm danh công dân: " + citizenId;
                        break;

                    case 2:
                        notification = "Đã tiêm cho công dân: " + citizenId;
                        break;

                    case 3:
                        notification = "Đã hủy tiêm chủng cho công dân: " + citizenId;
                        break;

                    default:
                        break;
                }
                Toast.makeText(getContext(), notification, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đã có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
