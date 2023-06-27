package com.example.cvm_mobile_application.ui.org.schedule.schedule_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@BuildCompat.PrereleaseSdkCheck
public class OrgScheduleListActivity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Organization org;
    private Button btnBack;
    private TextView tbTitle;
    private TextView tbMenu1;
    private LinearLayout btnScheduleFilter;
    private LinearLayout layoutScheduleFilter;
    private Button btnStartDateDP;
    private TextView tvStartDate;
    private DatePicker dpStartDate;
    private Button btnEndDateDP;
    private TextView tvEndDate;
    private DatePicker dpEndDate;
    private List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;
    private RecyclerView recyclerViewScheduleList;
    private OnScheduleItemClickListener onScheduleItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_schedule_list);
        db = FirebaseFirestore.getInstance();

        org = new Organization();
        scheduleList = new ArrayList<>();
    }

    protected void onStart() {
        super.onStart();
        org = getIntent().getParcelableExtra("org");

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);
        tbMenu1 = findViewById(R.id.tb_menu_1);

        btnScheduleFilter = findViewById(R.id.btn_schedule_filter);
        layoutScheduleFilter = findViewById(R.id.layout_linear_schedule_filter);

        tvStartDate = findViewById(R.id.tv_start_date);
        btnStartDateDP = findViewById(R.id.btn_start_date_dp);
        dpStartDate = findViewById(R.id.dp_start_date);

        tvEndDate = findViewById(R.id.tv_end_date);
        btnEndDateDP = findViewById(R.id.btn_end_date_dp);
        dpEndDate = findViewById(R.id.dp_end_date);

        recyclerViewScheduleList = findViewById(R.id.view_recycler_schedule_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewScheduleList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Danh sách lịch tiêm");
        tbMenu1.setText(org.getName());

        Timestamp timestamp = new Timestamp(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = timestamp.toDate();
        String onDateString = df.format(date);
        tvStartDate.setText(onDateString);
        tvEndDate.setText(onDateString);

        getScheduleList();
        scheduleAdapter = new ScheduleAdapter(getApplicationContext(), scheduleList);
        recyclerViewScheduleList.setAdapter(scheduleAdapter);

    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(view -> finish());

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

        btnStartDateDP.setOnClickListener(v -> {
            if (dpStartDate.getVisibility() == View.GONE) {
                dpStartDate.setVisibility(View.VISIBLE);
            } else {
                dpStartDate.setVisibility(View.GONE);
            }
        });

        dpStartDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            tvStartDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            getScheduleList();
        });

        btnEndDateDP.setOnClickListener(view -> {
            if (dpEndDate.getVisibility() == View.GONE) {
                dpEndDate.setVisibility(View.VISIBLE);
            } else {
                dpEndDate.setVisibility(View.GONE);
            }
        });

        dpEndDate.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            monthOfYear++;
            tvEndDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            OrgScheduleListActivity.this.getScheduleList();
        });

        onScheduleItemClickListener = item -> {
            Intent intent = new Intent(getBaseContext(), OrgScheduleManagementActivity.class);
            intent.putExtra("schedule", item);
            startActivity(intent);
        };
        scheduleAdapter.setListener(onScheduleItemClickListener);
    }

    private void getScheduleList() {
        String orgId = org.getId();
        String onSDateString = String.valueOf(tvStartDate.getText());
        String onEDateString = String.valueOf(tvEndDate.getText());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date onStartDate;
        Date onEndDate;
        try {
            onStartDate = df.parse(onSDateString);
            onEndDate = df.parse(onEDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Timestamp onSDate = new Timestamp(Objects.requireNonNull(onStartDate));
//        Timestamp onEDate = new Timestamp(onEndDate);

        if (onStartDate.compareTo(onEndDate) > 0) {
            Toast.makeText(this, "Chọn ngày bắt đầu nhỏ hơn ngày kết thúc!", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("schedules")
                    .whereEqualTo("org_id", orgId)
                    .whereGreaterThanOrEqualTo("on_date", onSDate)
//                    .whereLessThanOrEqualTo("on_date", onEDate)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            scheduleList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Schedule schedule = document.toObject(Schedule.class);
                                scheduleList.add(schedule);
                            }
                            scheduleAdapter.setScheduleList(scheduleList);
                            scheduleAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
