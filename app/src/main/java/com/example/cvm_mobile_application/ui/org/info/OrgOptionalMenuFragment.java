package com.example.cvm_mobile_application.ui.org.info;

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

@BuildCompat.PrereleaseSdkCheck public class OrgOptionalMenuFragment extends Fragment {
    private View view;
    private Organization org;
    private LinearLayout menuTabProfile;
    private LinearLayout menuTabLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_optional_menu, container, false);

        org = requireArguments().getParcelable("org");

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
        menuTabProfile.setOnClickListener(v -> OrgOptionalMenuFragment.this.getProfileActivity(org));

        menuTabLogout.setOnClickListener(view -> OrgOptionalMenuFragment.this.logOut());
    }

    public void getProfileActivity(Organization org){
        Intent intent = new Intent(getActivity(), OrgProfileActivity.class);
        intent.putExtra("org", org);
        startActivity(intent);
    }

    public void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(false)
                .setPositiveButton("Đăng xuất", (dialog, id) -> {
                    SharedPreferences settings = requireContext().getSharedPreferences("SHARED_PREFS", getContext().MODE_PRIVATE);
                    settings.edit().remove("username").apply();

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
