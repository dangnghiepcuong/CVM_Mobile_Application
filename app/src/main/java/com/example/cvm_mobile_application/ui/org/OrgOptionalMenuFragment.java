package com.example.cvm_mobile_application.ui.org;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.MainActivity;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;
import com.example.cvm_mobile_application.ui.org.info.OrgProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

@BuildCompat.PrereleaseSdkCheck public class OrgOptionalMenuFragment extends Fragment {
    private View view;
    private Organization org;
    private LinearLayout menuTabProfile;
    private LinearLayout menuTabLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_optional_menu, container, false);

        org = getArguments().getParcelable("org");

        implementView();
        setViewListener();
        return view;
    }

    public void implementView() {
        TextView tvName = view.findViewById(R.id.tv_Name);
        tvName.setText(org.getName());

        menuTabProfile = view.findViewById(R.id.menu_tab_profile);
        menuTabLogout = view.findViewById(R.id.menu_tab_logout);
    }

    public void setViewListener() {
        menuTabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrgOptionalMenuFragment.this.getProfileActivity(org);
            }
        });

        menuTabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrgOptionalMenuFragment.this.logOut();
            }
        });
    }

    public void getProfileActivity(Organization org){
        Intent intent = new Intent(getActivity(), OrgProfileActivity.class);
        intent.putExtra("org", org);
        startActivity(intent);
    }

    public void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(false)
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences settings = getContext().getSharedPreferences("SHARED_PREFS", getContext().MODE_PRIVATE);
                        settings.edit().remove("username").commit();

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
