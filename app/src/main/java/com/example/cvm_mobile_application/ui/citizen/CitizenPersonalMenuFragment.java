package com.example.cvm_mobile_application.ui.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.admin.info.ProfileFragment;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileFragment;

import org.checkerframework.checker.units.qual.Current;

public class CitizenPersonalMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citizen_personal_menu, container, false);

        Citizen citizen = getArguments().getParcelable("citizen");

        TextView fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        TextView phoneNumber = view.findViewById(R.id.PhoneNumber);
        phoneNumber.setText(citizen.getPhone());

        LinearLayout profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfileActivity(citizen);
            }
        });
        return view;
    }

    public void getProfileActivity(Citizen citizen){
        Intent intent = new Intent(getActivity(), CitizenProfileFragment.class);
        intent.putExtra("citizen", citizen);
        startActivity(intent);
    }
}