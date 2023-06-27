package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.Register;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.data.db.model.Shift;
import com.example.cvm_mobile_application.ui.CustomDialog;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.ScheduleAdapter;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.OnScheduleItemClickListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CitizenVaccinationState3Activity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private Organization org;
    private TextView tvOrgName;
    private LinearLayout btnScheduleFilter;
    private LinearLayout layoutScheduleFilter;
    private List<SpinnerOption> vaccineList;
    private Spinner spVaccineType;
    private SpinnerAdapter spVaccineTypeAdapter;
    private Spinner spShift;
    private SpinnerAdapter spShiftAdapter;
    private List<SpinnerOption> shiftList;
    private List<Schedule> scheduleList;
    private RecyclerView recyclerViewScheduleList;
    private ScheduleAdapter scheduleAdapter;
    private OnScheduleItemClickListener onScheduleItemClickListener;
    private Button btnOnDateDP;
    private TextView tvOnDate;
    private DatePicker dpOnDate;
    private CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_vaccination_state3);

        db = FirebaseFirestore.getInstance();
        vaccineList = new ArrayList<>();
        shiftList = new ArrayList<>();
        scheduleList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        citizen = getIntent().getParcelableExtra("citizen");
        org = getIntent().getParcelableExtra("organization");

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        tvOrgName = findViewById(R.id.tv_org_name);

        btnScheduleFilter = findViewById(R.id.btn_schedule_filter);
        layoutScheduleFilter = findViewById(R.id.layout_linear_schedule_filter);

        tvOnDate = findViewById(R.id.tv_on_date);
        btnOnDateDP = findViewById(R.id.btn_on_date_dp);
        dpOnDate = findViewById(R.id.dp_on_date);

        spVaccineType = findViewById(R.id.sp_vaccine_type);
        spShift = findViewById(R.id.sp_shift);

        recyclerViewScheduleList = findViewById(R.id.view_recycler_schedule_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewScheduleList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() {
        tvOrgName.setText(org.getName());

        Timestamp timestamp = new Timestamp(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = timestamp.toDate();
        String onDateString = df.format(date);
        tvOnDate.setText(onDateString);

        getVaccineList();
        spVaccineTypeAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.item_string, vaccineList);
        spVaccineType.setAdapter(spVaccineTypeAdapter);

        for (Shift shift : Shift.values()) {
            SpinnerOption spinnerOption = new SpinnerOption(
                    shift.getShift(), String.valueOf(shift.getValue()));
            shiftList.add(spinnerOption);
        }
        spShiftAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.item_string, shiftList);
        spShift.setAdapter(spShiftAdapter);

        scheduleAdapter = new ScheduleAdapter(
                getApplicationContext(),
                scheduleList);
        recyclerViewScheduleList.setAdapter(scheduleAdapter);
    }

    @Override
    public void setViewListener() {
        //SET DETAIL PERSONAL INFO BUTTON LISTENER
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


        btnOnDateDP.setOnClickListener(v -> {
            if (dpOnDate.getVisibility() == View.GONE) {
                dpOnDate.setVisibility(View.VISIBLE);
            } else {
                dpOnDate.setVisibility(View.GONE);
            }
        });

        dpOnDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            tvOnDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            CitizenVaccinationState3Activity.this.getScheduleList();
        });


        spVaccineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenVaccinationState3Activity.this.getScheduleList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenVaccinationState3Activity.this.getScheduleList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onScheduleItemClickListener = CitizenVaccinationState3Activity.this::checkConstraintBeforeVaccination;
        scheduleAdapter.setListener(onScheduleItemClickListener);
    }

    public void getVaccineList() {
        // DISABLE SPINNER VACCINE LOT BEFORE GETTING NEW VACCINE TYPE
        db.collection("vaccines").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    SpinnerOption spOption = new SpinnerOption((String) document.get("name"), (String) document.get("id"));
                    vaccineList.add(spOption);
                }
                spVaccineTypeAdapter.notifyDataSetChanged();
            } else {
                Log.d("myTAG", "Retrieving Data: getVaccineList");
                Toast.makeText(this, "Lỗi khi lấy danh sách vaccine", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getScheduleList() {
        String orgId = org.getId();
        String onDateString = String.valueOf(tvOnDate.getText());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(onDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Timestamp onDate = new Timestamp(date);

        SpinnerOption spOption = (SpinnerOption) spVaccineType.getItemAtPosition(
                spVaccineType.getSelectedItemPosition());
        if (spOption == null) {
            return;
        }
        String vaccineType = spOption.getValue();

        db.collection("schedules")
                .whereEqualTo("org_id", orgId)
//                .whereEqualTo("on_date", onDate)
                .whereGreaterThanOrEqualTo("on_date", onDate)
                .whereEqualTo("vaccine_id", vaccineType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        scheduleList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Schedule schedule = (Schedule) document.toObject(Schedule.class);
                            scheduleList.add(schedule);
                        }
                        scheduleAdapter.setScheduleList(scheduleList);
                        scheduleAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void checkConstraintBeforeVaccination(Schedule schedule) {
        db.collection("registry")
                .whereEqualTo("citizen_id", citizen.getId())
                .whereLessThan("status", 2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            CitizenVaccinationState3Activity.this.vaccinationRegistration(schedule);
                        } else {
                            Toast.makeText(this, "Không thể đăng ký tiêm chủng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void vaccinationRegistration(Schedule schedule) {
        customDialog = new CustomDialog(getApplicationContext());

        customDialog.setViewListener(new CustomDialog.OnClickButtonListener() {
            @Override
            public void onClickCancel() {
                CitizenVaccinationState3Activity.this.dialogOnCancel();
            }

            @Override
            public void onClickConfirm() {
                CitizenVaccinationState3Activity.this.dialogOnConfirm(schedule.getId());
            }
        });

        customDialog.showDialog("Xác nhận đăng ký tiêm chủng?",
                "Lịch tiêm:...");
    }

    public void dialogOnCancel() {
        customDialog.getDialog().dismiss();
    }

    public void dialogOnConfirm(String scheduleId) {
        customDialog.getDialog().dismiss();

        DocumentReference scheduleRef =
                db.collection("schedules").document(scheduleId);
        DocumentReference registryRef =
                db.collection("registry").document(citizen.getId() + scheduleId);
        db.runTransaction(new Transaction.Function<Integer>() {
            @Nullable
            @Override
            public Integer apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot scheduleSnapshot = transaction.get(scheduleRef);

                Double dayRegistered = scheduleSnapshot.getDouble("day_registered");
                Double noonRegistered = scheduleSnapshot.getDouble("noon_registered");
                Double nightRegistered = scheduleSnapshot.getDouble("night_registered");
                Double limitDay = scheduleSnapshot.getDouble("limit_day");
                Double limitNoon = scheduleSnapshot.getDouble("limit_noon");
                Double limitNight = scheduleSnapshot.getDouble("limit_night");

                Log.i("myTAG", dayRegistered + " " + noonRegistered + " " + nightRegistered
                        + " " + limitDay + " " + limitNoon + " " + limitNight);

                SpinnerOption shiftOption = (SpinnerOption) spShift.getSelectedItem();
                String shiftValue = shiftOption.getValue();
                String shiftName = shiftOption.getOption().substring(0, shiftOption.getOption().length() - 3);
                switch (shiftValue) {
                    default:
                    case "0":
                        if (dayRegistered == limitDay)
                            return null;
                        else
                            transaction.update(scheduleRef, "day_registered", dayRegistered + 1);
                        break;
                    case "1":
                        if (noonRegistered == limitNoon)
                            return null;
                        else
                            transaction.update(scheduleRef, "noon_registered", noonRegistered + 1);
                        break;
                    case "2":
                        if (nightRegistered == limitNight)
                            return null;
                        else
                            transaction.update(scheduleRef, "night_registered", nightRegistered + 1);
                        break;
                }

                Register register = new Register();
                register.setCitizen_id(citizen.getId());
                register.setCitizen_name(citizen.getFull_name());
                register.setSchedule_id(scheduleId);
                register.setShift(shiftName);
                register.setNum_order(Objects.requireNonNull(dayRegistered).intValue()
                        + Objects.requireNonNull(noonRegistered).intValue()
                        + Objects.requireNonNull(nightRegistered).intValue()
                        + 1);
                register.setStatus(0);

                transaction.set(registryRef, register);
                return 1;
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Đăng ký tiêm chủng thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng ký tiêm chủng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
