package com.example.cvm_mobile_application.ui.org.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.citizen.vaccination.CitizenVaccinationActivity;

public class OrgHomeFragment extends Fragment {

    private TextView fullName;
    private View view;
    private Organization org;
    private LinearLayout btnCreateSchedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_home, container, false);

        org = getArguments().getParcelable("org");

        implementView();
        fullName.setText(org.getName());
        return view;
    }

    public void implementView() {
        fullName = view.findViewById(R.id.tv_Name);
        btnCreateSchedule = view.findViewById(R.id.btn_create_schedule);
    }

    public void bindViewData() {
        fullName.setText(org.getName());
    }

    public void setViewListener() {
        btnCreateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), CitizenVaccinationActivity.class);
                intent.putExtra("org", org);
                startActivity(intent);
            }
        });
    }
}
