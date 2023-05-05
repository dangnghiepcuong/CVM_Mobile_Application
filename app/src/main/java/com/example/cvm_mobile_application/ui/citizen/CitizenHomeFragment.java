package com.example.cvm_mobile_application.ui.citizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;

public class CitizenHomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Map<String, Object> users = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_citizen_home, container, false);

        Citizen citizen = getArguments().getParcelable("citizen");

        TextView fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        return view;
    }
}