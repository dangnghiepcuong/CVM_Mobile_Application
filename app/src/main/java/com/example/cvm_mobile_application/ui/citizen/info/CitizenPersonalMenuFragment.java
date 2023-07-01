package com.example.cvm_mobile_application.ui.citizen.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;


@BuildCompat.PrereleaseSdkCheck public class CitizenPersonalMenuFragment extends Fragment {

    private Citizen citizen;
    private View view;
    private TextView fullName;
    private TextView phoneNumber;
    private LinearLayout menuTabProfile;
    private LinearLayout menuTabRelative;
    private LinearLayout menuTabCertificate;
    private LinearLayout menuTabIntroduction;
    private LinearLayout menuTabSetting;
    private LinearLayout menuTabLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_personal_menu, container, false);

        citizen = requireArguments().getParcelable("citizen");

        implementView();
        setViewListener();
        return view;
    }

    public void implementView() {
        fullName = view.findViewById(R.id.FullName);
        fullName.setText(citizen.getFull_name());

        phoneNumber = view.findViewById(R.id.PhoneNumber);
        phoneNumber.setText(citizen.getPhone());

        menuTabProfile = view.findViewById(R.id.menu_tab_profile);
        menuTabRelative = view.findViewById(R.id.menu_tab_relative);
        menuTabCertificate = view.findViewById(R.id.menu_tab_certificate);
        menuTabIntroduction = view.findViewById(R.id.menu_tab_introduction);
        menuTabSetting = view.findViewById(R.id.menu_tab_setting);

        menuTabLogout = view.findViewById(R.id.menu_tab_logout);
    }

    public void setViewListener() {
        menuTabProfile.setOnClickListener(view -> CitizenPersonalMenuFragment.this.getProfileActivity(citizen));

//        menuTabRelative.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), CitizenCertificateActivity.class);
//            intent.putExtra("citizen", citizen);
//            startActivity(intent);
//        });

        menuTabCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CitizenCertificateActivity.class);
            intent.putExtra("citizen", citizen);
            startActivity(intent);
        });


        menuTabLogout.setOnClickListener(v -> CitizenPersonalMenuFragment.this.logOut());
    }

    public void getProfileActivity(Citizen citizen){
        Intent intent = new Intent(getActivity(), CitizenProfileActivity.class);
        intent.putExtra("citizen", citizen);
        startActivity(intent);
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(false)
                .setPositiveButton("Đăng xuất", (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                    dialog.cancel();
                })
                .setNegativeButton("Hủy bỏ", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}