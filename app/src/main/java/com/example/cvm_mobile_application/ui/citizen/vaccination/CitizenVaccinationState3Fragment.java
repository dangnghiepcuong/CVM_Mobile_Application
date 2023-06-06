package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.data.db.model.Shift;
import com.example.cvm_mobile_application.ui.ScheduleListAdapter;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.org.OnScheduleItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitizenVaccinationState3Fragment extends Fragment implements ViewStructure {

    private View view;
    private FirebaseFirestore db;
    private Citizen citizen;
    private Organization org;
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
    private ScheduleListAdapter scheduleListAdapter;
    private OnScheduleItemClickListener onScheduleItemClickListener;
    private Button btnOnDateDP;
    private TextView tvOnDate;
    private DatePicker dpOnDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state3, null);
        db = FirebaseFirestore.getInstance();

        citizen = getArguments().getParcelable("citizen");
        org = getArguments().getParcelable("organization");

        vaccineList = new ArrayList<>();
        shiftList = new ArrayList<>();
        scheduleList = new ArrayList<>();

        implementView();
        try {
            bindViewData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        setViewListener();
        return view;
    }

    @Override
    public void implementView() {
        btnScheduleFilter = view.findViewById(R.id.btn_schedule_filter);
        layoutScheduleFilter = view.findViewById(R.id.layout_linear_schedule_filter);

        tvOnDate = view.findViewById(R.id.tv_on_date);
        btnOnDateDP = view.findViewById(R.id.btn_on_date_dp);
        dpOnDate = view.findViewById(R.id.dp_on_date);

        spVaccineType = view.findViewById(R.id.sp_vaccine_type);
        spShift = view.findViewById(R.id.sp_shift);

        recyclerViewScheduleList = view.findViewById(R.id.view_recycler_schedule_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewScheduleList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() throws JSONException {
        Timestamp timestamp = new Timestamp(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = timestamp.toDate();
        String onDateString = df.format(date);
        tvOnDate.setText(onDateString);

        getVaccineList();
        spVaccineTypeAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), R.layout.item_string, vaccineList);
        spVaccineType.setAdapter(spVaccineTypeAdapter);

        for (Shift shift : Shift.values()) {
            SpinnerOption spinnerOption = new SpinnerOption(
                    shift.getShift(), String.valueOf(shift.getValue()));
            shiftList.add(spinnerOption);
        }
        spShiftAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), R.layout.item_string, shiftList);
        spShift.setAdapter(spShiftAdapter);

        scheduleListAdapter = new ScheduleListAdapter(
                requireActivity().getApplicationContext(),
                scheduleList);
        recyclerViewScheduleList.setAdapter(scheduleListAdapter);
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


        btnOnDateDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dpOnDate.getVisibility() == View.GONE) {
                    dpOnDate.setVisibility(View.VISIBLE);
                } else {
                    dpOnDate.setVisibility(View.GONE);
                }
            }
        });

        dpOnDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tvOnDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                CitizenVaccinationState3Fragment.this.getScheduleList();
            }
        });


        spVaccineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenVaccinationState3Fragment.this.getScheduleList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenVaccinationState3Fragment.this.getScheduleList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onScheduleItemClickListener = new OnScheduleItemClickListener() {
            @Override
            public void onItemClick(Schedule item) {
                Toast.makeText(requireActivity().getApplicationContext()
                        , item.getId(), Toast.LENGTH_SHORT).show();
            }
        };
        scheduleListAdapter.setListener(onScheduleItemClickListener);
    }

    public void getVaccineList() {
        // DISABLE SPINNER VACCINE LOT BEFORE GETTING NEW VACCINE TYPE
        db.collection("vaccines").get().addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        SpinnerOption spOption = new SpinnerOption((String) document.get("name"), (String) document.get("id"));
                        vaccineList.add(spOption);
                    }
                    spVaccineTypeAdapter.notifyDataSetChanged();
                } else {
                    Log.d("myTAG", "Retrieving Data: getVaccineList");
                    Toast.makeText(requireActivity().getApplicationContext(), "Lỗi khi lấy danh sách vaccine", Toast.LENGTH_LONG).show();
                }
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

        SpinnerOption spOption= (SpinnerOption) spVaccineType.getItemAtPosition(
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
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            scheduleList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Schedule schedule = (Schedule) document.toObject(Schedule.class);
                                scheduleList.add(schedule);
                            }
                            scheduleListAdapter.setScheduleList(scheduleList);
                            scheduleListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
