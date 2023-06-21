package com.example.cvm_mobile_application.ui.org.schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrgScheduleUpdateFragment extends Fragment {
    private View view;
    private LinearLayoutCompat toolbar1;
    private Button btnCreateSchedule;
    private FirebaseFirestore db;
    private LinearLayout infoToInput;
    private TextView tvIfDate;
    private TextView tvIfVaccineType;
    private TextView tvIfVaccineLot;
    private EditText etDayLimit;
    private EditText etNoonLimit;
    private EditText etNightLimit;
    private Button btnUpdateSchedule;

    private Schedule schedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_org_schedule_create, container, false);
        db = FirebaseFirestore.getInstance();

        schedule = getArguments().getParcelable("schedule");

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    private void implementView() {
        toolbar1 = view.findViewById(R.id.toolbar1);
        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
        infoToInput = view.findViewById(R.id.info_to_input);

        tvIfDate = view.findViewById(R.id.tv_if_date);
        tvIfVaccineType = view.findViewById(R.id.tv_if_vaccine_type);
        tvIfVaccineLot = view.findViewById(R.id.iv_if_vaccine_lot);

        etDayLimit = view.findViewById(R.id.et_day_limit);
        etNoonLimit = view.findViewById(R.id.et_noon_limit);
        etNightLimit = view.findViewById(R.id.et_night_limit);

        btnUpdateSchedule  = view.findViewById(R.id.btn_update_schedule);
    }

    private void bindViewData() {
        toolbar1.setVisibility(View.GONE);
        btnCreateSchedule.setVisibility(View.GONE);
        infoToInput.setVisibility(View.GONE);

        Date date = schedule.getOn_date();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String onDate = df.format(date);

        tvIfDate.setText("Lịch tiêm ngày: " + onDate);
        tvIfVaccineType.setText("Loại vaccine: " + schedule.getVaccine_id());
        tvIfVaccineLot.setText("Số lô: " + schedule.getLot());

        String limitDay = String.valueOf(schedule.getLimit_day());
        String limitNoon = String.valueOf(schedule.getLimit_noon());
        String limitNight = String.valueOf(schedule.getLimit_night());

        etDayLimit.setText(limitDay);
        etNoonLimit.setText(limitNoon);
        etNightLimit.setText(limitNight);
    }

    private void setViewListener() {

    }
}
