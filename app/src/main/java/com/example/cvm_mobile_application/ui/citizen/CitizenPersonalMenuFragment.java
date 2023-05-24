package com.example.cvm_mobile_application.ui.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;

public class CitizenPersonalMenuFragment extends Fragment {

    private LinearLayout menuTabProfile;
    private Citizen citizen;
    private View view;
    private LinearLayout menuTabLogout;
    private TextView fullName;
    private TextView phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_personal_menu, container, false);

        citizen = getArguments().getParcelable("citizen");



        return view;
    }

    public void implementView() {
        fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        phoneNumber = view.findViewById(R.id.PhoneNumber);
        phoneNumber.setText(citizen.getPhone());

        menuTabProfile = view.findViewById(R.id.menu_tab_profile);

        menuTabLogout = view.findViewById(R.id.menu_tab_logout);
    }

    public void setViewListener() {
        menuTabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfileActivity(citizen);
            }
        });

        menuTabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getProfileActivity(Citizen citizen){
        Intent intent = new Intent(getActivity(), CitizenProfileActivity.class);
        intent.putExtra("citizen", citizen);
        startActivity(intent);
    }

    public void logOut() {

    }
}