package com.example.cvm_mobile_application.ui.citizen.home;

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
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenCertificateActivity;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;
import com.example.cvm_mobile_application.ui.citizen.vaccination.CitizenVaccinationState1Activity;

@BuildCompat.PrereleaseSdkCheck public class CitizenHomeFragment extends Fragment {
    private LinearLayout btnVaccination;
    private Citizen citizen;
    private View view;
    private TextView fullName;
    private LinearLayout btnCertificate;
    private Button btnProfile;
    private Button btnAccount;
    private Button btnRelative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_home, container, false);

        citizen = new Citizen();
        citizen = requireArguments().getParcelable("citizen");

        implementView();
        setViewListener();
        bindViewData();

        return view;
    }

    public void implementView() {
        fullName = view.findViewById(R.id.FullName);
        btnVaccination = view.findViewById(R.id.btn_vaccination);
        btnCertificate = view.findViewById(R.id.btn_certificate);
        btnProfile = view.findViewById(R.id.btn_profile);
        btnAccount = view.findViewById(R.id.btn_account);
        btnRelative = view.findViewById(R.id.btn_relative);
    }

    public void bindViewData() {
        fullName.setText(citizen.getFull_name());
    }

    public void setViewListener() {
        btnVaccination.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CitizenVaccinationState1Activity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });

        btnCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CitizenCertificateActivity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CitizenProfileActivity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CitizenProfileActivity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });

        btnRelative.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CitizenProfileActivity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });

    }
}