package com.example.cvm_mobile_application.ui.org.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgCreateScheduleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Organization org;
    private Button btnCreate;
    private EditText etOnDate;
    private Spinner spVaccineType;
    private Spinner spVaccineLot;
    private EditText etDayLimit;
    private EditText etNoonLimit;
    private EditText etNightLimit;
    private List<SpinnerOption> vaccineList;
    private List<SpinnerOption> vaccineInventoryList;
    private ProgressBar pbSpVaccineList;
    private ProgressBar pbSpVaccineLot;
    private SpinnerAdapter spVaccineTypeAdapter;
    private SpinnerAdapter spVaccineLotAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_schedule_create);

        db = FirebaseFirestore.getInstance();
        org = new Organization();
        vaccineList = new ArrayList<>();
        vaccineInventoryList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        org = getIntent().getParcelableExtra("org");

        implementView();
        bindViewData();
        setViewListener();
    }

    public void implementView() {
        etOnDate = findViewById(R.id.et_on_date);

        spVaccineType = findViewById(R.id.sp_vaccine_type);
//        pbSpVaccineList = findViewById(R.id.pb_sp_vaccine_type);

        spVaccineLot = findViewById(R.id.sp_vaccine_lot);
        spVaccineLot.setEnabled(false);
//        pbSpVaccineLot = findViewById(R.id.pb_sp_vaccine_lot);

        etDayLimit = findViewById(R.id.et_day_limit);
        etNoonLimit = findViewById(R.id.et_noon_limit);
        etNightLimit = findViewById(R.id.et_night_limit);

        btnCreate = findViewById(R.id.btn_create_schedule);
        btnCreate.setEnabled(false);
    }

    public void bindViewData() {
        getVaccineList();
        spVaccineTypeAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.item_string, vaccineList);
        spVaccineType.setAdapter(spVaccineTypeAdapter);

        spVaccineLotAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.item_string, vaccineInventoryList);
        spVaccineLot.setAdapter(spVaccineLotAdapter);
    }

    public void setViewListener() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrgCreateScheduleActivity.this.createSchedule();
            }
        });

        // TRIGGER SELECTING VACCINE TYPE, GET LIST OF VACCINE LOT IN INVENTORY
        spVaccineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption spSelected = (SpinnerOption) parent.getSelectedItem();
                OrgCreateScheduleActivity.this.getVaccineInventoryList(spSelected.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getVaccineList() {
        // DISABLE SPINNER VACCINE LOT BEFORE GETTING NEW VACCINE TYPE
        spVaccineLot.setEnabled(false);
//        pbSpVaccineList.setVisibility(View.VISIBLE);
        db.collection("vaccines")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        SpinnerOption spOption = new SpinnerOption(
                                                (String) document.get("name"),
                                                (String) document.get("id")
                                        );
                                        vaccineList.add(spOption);
                                    }
                                    spVaccineTypeAdapter.notifyDataSetChanged();

                                    // RETRIEVE VACCINE LOT LIST IN INVENTORY
                                    OrgCreateScheduleActivity.this.getVaccineInventoryList(vaccineList.get(0).getValue());

                                    // ENABLE SPINNER VACCINE LOT AFTER SUCCESSFULLY RETRIEVING VACCINE TYPE
                                    spVaccineLot.setEnabled(true);
                                } else {
                                    Log.d("myTAG", "Retrieving Data: getVaccineList");
                                    Toast.makeText(OrgCreateScheduleActivity.this,
                                            "Lỗi khi lấy danh sách vaccine", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    public void getVaccineInventoryList(String vaccineId) {
        btnCreate.setEnabled(false);
        db.collection("vaccine_inventory")
                .whereEqualTo("org_id", org.getId())
                .whereEqualTo("vaccine_id", vaccineId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    vaccineInventoryList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        SpinnerOption spOption = new SpinnerOption(
                                                (String) document.get("lot")
                                                        + " - sl: "
                                                        + document.get("quantity"),
                                                (String) document.get("id")
                                        );
                                        vaccineInventoryList.add(spOption);
                                    }
                                    spVaccineLotAdapter.setOptionList(vaccineInventoryList);
                                    spVaccineLotAdapter.notifyDataSetChanged();
                                    btnCreate.setEnabled(true);
                                } else {
                                    Log.d("myTAG", "Retrieving Data: getVaccineInventoryList");
                                    Toast.makeText(OrgCreateScheduleActivity.this,
                                            "Lỗi khi lấy danh sách lot vaccine", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    public void createSchedule() {
        String onDate = String.valueOf(etOnDate.getText());

        SpinnerOption vaccine = (SpinnerOption) spVaccineType.getSelectedItem();
        String vaccineType = vaccine.getValue();

        vaccine = (SpinnerOption) spVaccineLot.getSelectedItem();
        String vaccineLot = vaccine.getValue();

        String limitDay = String.valueOf(etDayLimit.getText());
        String limitNoon = String.valueOf(etNoonLimit.getText());
        String limitNight = String.valueOf(etNightLimit.getText());

        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("on_date", onDate);
        data.put("vaccine_id", vaccineType);
        data.put("vaccine_lot", vaccineLot);
        data.put("limit_day", limitDay);
        data.put("limit_noon", limitNoon);
        data.put("limit_night", limitNight);

        db.collection("schedules")
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d("myTAG", "DocumentSnapshot written with ID: " + task.getResult().getId());
                            Toast.makeText(OrgCreateScheduleActivity.this,
                                    "Tạo lịch tiêm chủng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("myTAG", "Error adding document", task.getException());
                            Toast.makeText(OrgCreateScheduleActivity.this,
                                    "Đã có lỗi xảy, vui lòng thử lại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
