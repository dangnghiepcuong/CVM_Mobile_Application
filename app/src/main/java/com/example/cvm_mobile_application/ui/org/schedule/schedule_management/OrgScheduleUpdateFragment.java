package com.example.cvm_mobile_application.ui.org.schedule.schedule_management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgScheduleUpdateFragment extends Fragment implements ViewStructure {
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

        schedule = requireArguments().getParcelable("schedule");

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    @Override
    public void implementView() {
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

    @Override
    public void bindViewData() {
        toolbar1.setVisibility(View.GONE);
        btnCreateSchedule.setVisibility(View.GONE);
        infoToInput.setVisibility(View.GONE);

        tvIfDate.setText("Lịch tiêm ngày: " + schedule.getOnDateString());
        tvIfVaccineType.setText("Loại vaccine: " + schedule.getVaccine_id());
        tvIfVaccineLot.setText("Số lô: " + schedule.getLot());

        etDayLimit.setText(String.valueOf(schedule.getLimit_day()));
        etNoonLimit.setText(String.valueOf(schedule.getLimit_noon()));
        etNightLimit.setText(String.valueOf(schedule.getLimit_night()));
    }

    @Override
    public void setViewListener() {
        btnUpdateSchedule.setOnClickListener(v -> {
            int limitDay = 0, limitNoon = 0, limitNight = 0;

            if (!String.valueOf(etDayLimit.getText()).equals("")) {
                limitDay = Integer.parseInt(String.valueOf(etDayLimit.getText()));
            }

            if (!String.valueOf(etNoonLimit.getText()).equals("")) {
                limitNoon = Integer.parseInt(String.valueOf(etNoonLimit.getText()));
            }

            if (!String.valueOf(etNightLimit.getText()).equals("")) {
                limitNight = Integer.parseInt(String.valueOf(etNightLimit.getText()));
            }

            Schedule updatedSchedule = schedule;
            updatedSchedule.setLimit_day(limitDay);
            updatedSchedule.setLimit_noon(limitNoon);
            updatedSchedule.setLimit_night(limitNight);

            OrgScheduleUpdateFragment.this.updateSchedule(updatedSchedule);
        });
    }

    public void updateSchedule(Schedule updatedSchedule) {
        db.collection("schedules").document(updatedSchedule.getId())
                .set(updatedSchedule);
    }
}
