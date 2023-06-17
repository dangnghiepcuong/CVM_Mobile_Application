package com.example.cvm_mobile_application.ui.citizen.info;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;

@BuildCompat.PrereleaseSdkCheck public class CitizenPersonalMenuFragment extends Fragment {

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

        menuTabLogout = view.findViewById(R.id.menu_tab_logout);
    }

    public void setViewListener() {
        menuTabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitizenPersonalMenuFragment.this.getProfileActivity(citizen);
            }
        });

        menuTabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CitizenPersonalMenuFragment.this.logOut();
            }
        });
    }

    public void getProfileActivity(Citizen citizen){
        Intent intent = new Intent(getActivity(), CitizenProfileActivity.class);
        intent.putExtra("citizen", citizen);
        startActivity(intent);
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(false)
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}