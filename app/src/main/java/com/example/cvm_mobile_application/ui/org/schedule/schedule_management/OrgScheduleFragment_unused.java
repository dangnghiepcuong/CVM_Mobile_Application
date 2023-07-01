package com.example.cvm_mobile_application.ui.org.schedule.schedule_management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;

public class OrgScheduleFragment_unused extends Fragment {
    private View view;
    private Organization org;
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflate.inflate(R.layout.activity_org_schedule_list, container, false);
        org = requireArguments().getParcelable("org");
        return view;
    }

}
