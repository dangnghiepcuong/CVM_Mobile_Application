package com.example.cvm_mobile_application.ui.org.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;

public class OrgHomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_home, container, false);

        Organization organization = getArguments().getParcelable("org");

        TextView fullName = view.findViewById(R.id.tv_Name);
        fullName.setText(organization.getName());
        return view;
    }
}
