package com.example.cvm_mobile_application.ui.org.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;
import com.example.cvm_mobile_application.ui.org.info.OrgProfileActivity;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.OrgCreateScheduleActivity;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.OrgScheduleListActivity;
import com.example.cvm_mobile_application.ui.org.vaccine.OrgVaccineManagementActivity;

@BuildCompat.PrereleaseSdkCheck public class OrgHomeFragment extends Fragment {

    private TextView fullName;
    private View view;
    private Organization org;
    private LinearLayout btnCreateSchedule;
    private LinearLayout btnVaccineManagement;
    private LinearLayout btnScheduleManagement;
    private Button btnProfile;
    private Button btnAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_home, container, false);

        org = getArguments().getParcelable("org");

        implementView();
        bindViewData();
        setViewListener();

        return view;
    }

    public void implementView() {
        fullName = view.findViewById(R.id.tv_name);
        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
        btnVaccineManagement = view.findViewById(R.id.btn_vaccine_inventory);
        btnScheduleManagement = view.findViewById(R.id.btn_schedule_mangement);
        btnProfile = view.findViewById(R.id.btn_profile);
        btnAccount = view.findViewById(R.id.btn_account);
    }

    public void bindViewData() {
        fullName.setText(org.getName());
    }

    public void setViewListener() {
        btnCreateSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getBaseContext(),
                    OrgCreateScheduleActivity.class);
            intent.putExtra("org", org);
            startActivity(intent);
        });

        btnVaccineManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getBaseContext(),
                    OrgVaccineManagementActivity.class);
            intent.putExtra("org", org);
            startActivity(intent);
        });

        btnScheduleManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getBaseContext(),
                    OrgScheduleListActivity.class);
            intent.putExtra("org", org);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OrgProfileActivity.class);
            intent.putExtra("org", org);
            startActivity(intent);
        });

//        btnAccount.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), OrgProfileActivity.class);
//            intent.putExtra("org", org);
//            startActivity(intent);
//        });
    }
}
