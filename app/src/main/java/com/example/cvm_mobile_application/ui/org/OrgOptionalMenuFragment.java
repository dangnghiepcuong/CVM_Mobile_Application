package com.example.cvm_mobile_application.ui.org;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileActivity;

public class OrgOptionalMenuFragment extends Fragment {
    private View view;
    private Organization org;
    private LinearLayout menuTabProfile;
    private LinearLayout menuTabLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_personal_menu, container, false);

        org = getArguments().getParcelable("org");

        implementView();
        setViewListener();
        return view;
    }

    public void implementView() {
        TextView tvName = view.findViewById(R.id.tv_Name);
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
    }

    public void getProfileActivity(Organization org){
        Intent intent = new Intent(getActivity(), CitizenProfileActivity.class);
        intent.putExtra("org", org);
        startActivity(intent);
    }
}
