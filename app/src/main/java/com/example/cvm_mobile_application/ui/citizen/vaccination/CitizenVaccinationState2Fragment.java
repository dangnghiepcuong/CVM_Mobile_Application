package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.helpers.DVHCHelper;
import com.example.cvm_mobile_application.ui.OnOrgItemClickListener;
import com.example.cvm_mobile_application.ui.OrgListAdapter;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState2Fragment extends Fragment implements ViewStructure {

    private View view;
    private FirebaseFirestore db;
    private Citizen citizen;
    private Spinner spProvince;
    private List<SpinnerOption> provinceList = new ArrayList<>();
    private SpinnerAdapter spProvinceListAdapter;
    private Spinner spDistrict;
    private List<SpinnerOption> districtList = new ArrayList<>();
    private SpinnerAdapter spDistrictListAdapter;
    private Spinner spWard;
    private List<SpinnerOption> wardList = new ArrayList<>();
    private SpinnerAdapter spWardListAdapter;
    private DVHCHelper dvhcHelper;
    private List<Organization> orgList;
    private RecyclerView recyclerViewOrgList;
    private OrgListAdapter orgListAdapter;
    private LinearLayout btnRegionFilter;
    private LinearLayout layoutRegionFilter;
    private OnOrgItemClickListener onOrgItemClickListener;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state2, container, false);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(requireActivity().getApplicationContext());

        citizen = getArguments().getParcelable("citizen");
        orgList = new ArrayList<>();

        implementView();
        bindViewData();
        setViewListener();

        return view;
    }

    @Override
    public void implementView() {
        btnRegionFilter = view.findViewById(R.id.btn_region_filter);
        layoutRegionFilter = view.findViewById(R.id.layout_linear_region_filter);

        dvhcHelper.setSpProvince(view.findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(view.findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(view.findViewById(R.id.sp_ward));

        spProvince = view.findViewById(R.id.sp_province);
        spDistrict = view.findViewById(R.id.sp_district);
        spWard = view.findViewById(R.id.sp_ward);

        //SET ORG LIST VIEW
        recyclerViewOrgList = view.findViewById(R.id.view_recycler_org_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewOrgList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() {
        //SET LOCAL VALUE
        try {
            dvhcHelper.bindLocalListSpinnerData(getContext(),
                    citizen.getProvince_name(),
                    citizen.getDistrict_name(),
                    citizen.getWard_name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //GET THE ORG LIST BASE ON THE INIT FILTER
        getOrgList(citizen.getProvince_name(), citizen.getDistrict_name(), citizen.getWard_name());

        //BIND ORG LIST DATA
        orgListAdapter = new OrgListAdapter(requireActivity().getApplicationContext(), orgList);
        recyclerViewOrgList.setAdapter(orgListAdapter);
    }

    @Override
    public void setViewListener() {
        //SET DETAIL PERSONAL INFO BUTTON LISTENER
        btnRegionFilter.setOnClickListener(v -> {
            int visibility = layoutRegionFilter.getVisibility();
            switch (visibility) {
                case View.GONE:
                    layoutRegionFilter.setVisibility(View.VISIBLE);
                    break;

                case View.VISIBLE:
                default:
                case View.INVISIBLE:
                    layoutRegionFilter.setVisibility(View.GONE);
            }
        });

        dvhcHelper.setLocalListSpinnerListener();

        onOrgItemClickListener = new OnOrgItemClickListener() {

            private CitizenVaccinationState3Fragment state3Fragment;

            @Override
            public void onItemClick(Organization item) {
                Toast.makeText(getContext(),
                        item.getId(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putParcelable("citizen", citizen);
                bundle.putParcelable("organization", item);

                state3Fragment = new CitizenVaccinationState3Fragment();
                state3Fragment.setArguments(bundle);
                CitizenVaccinationState2Fragment.this.replaceFragment(state3Fragment);

            }
        };
        orgListAdapter.setListener(onOrgItemClickListener);
    }

    public void spProvinceTriggeredActivities() {
        try {
            SpinnerOption provinceOption =
                    (SpinnerOption) provinceList.get(spProvince.getSelectedItemPosition());
            districtList = dvhcHelper.getLocalList(
                    DVHCHelper.DISTRICT_LEVEL, provinceOption.getValue());
            spDistrictListAdapter.setOptionList(districtList);
            spDistrictListAdapter.notifyDataSetChanged();

            // Changing selected province triggers district listener to change district list.
            // And when changing district list, we also need to change ward list,

            // IN CASE, THE DISTRICT SPINNER HAS NOT BEEN SELECTED
            // (SELECTED POSITION STAYS = 0)
            // THEN WHEN .setSelection(0) IS CALLED
            // IT DOES NOT TRIGGER THE SELECTION OF THE DISTRICT SPINNER
            // (THE ACTIVITY WHEN DISTRICT SPINNER IS TRIGGERED IS CHANGING THE WARD LIST)
            // SO WE NEED TO DO THE ACTIVITY OF THE DISTRICT SPINNER TRIGGER BY HAND HERE
            if (spDistrict.getSelectedItemPosition() == 0) {
//                SpinnerOption districtOption = (SpinnerOption) districtList.get(0);
//                wardList = dvhcHelper.getLocalList(
//                        DVHCHelper.WARD_LEVEL, districtOption.getValue());
//                spWardListAdapter.setOptionList(wardList);
//                spWardListAdapter.notifyDataSetChanged();

                spDistrictTriggeredActivities();
            }
            // ELSE SET SELECTION TO 0 AND TRIGGER THE DISTRICT SPINNER AUTOMATICALLY
            else {
                spDistrict.setSelection(0, true);
            }

            // MORE EXPLANATION
            // THE REASON WE NEED TO TRIGGER THESE LISTENERS IN CHAIN THAT IS
            // TO MAKE SURE ALL THESE ACTIVITIES ARE ACTIVATED COMPLETELY AND IN ORDERLY

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void spDistrictTriggeredActivities() {
        try {
            SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
            wardList = dvhcHelper.getLocalList(
                    DVHCHelper.WARD_LEVEL, districtOption.getValue());
            spWardListAdapter.setOptionList(wardList);
            spWardListAdapter.notifyDataSetChanged();

            // TRIGGER WARD SPINNER SELECTION FOR THE NEXT ACTIVITIES

            if (spWard.getSelectedItemPosition() == 0) {
                spWardTriggeredActivities();
            } else {
                spWard.setSelection(0, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void spWardTriggeredActivities() {
        SpinnerOption provinceOption = (SpinnerOption) spProvince.getSelectedItem();
        SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
        SpinnerOption wardOption =
                (SpinnerOption) wardList.get(spWard.getSelectedItemPosition());

        getOrgList(provinceOption.getOption(),
                districtOption.getOption(),
                wardOption.getOption());
    }

    public void getOrgList(String provinceName, String districtName, String wardName) {
        CollectionReference orgRef = db.collection("organizations");

        Query query = orgRef;
        if (!provinceName.equals("Tất cả")) {
            query = query.whereEqualTo("province_name", provinceName);
            if (!districtName.equals("Tất cả")) {
                query = query.whereEqualTo("district_name", districtName);
                if (!wardName.equals("Tất cả")) {
                    query = query.whereEqualTo("ward_name", wardName);
                }
            }
        }

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            orgList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Organization org = new Organization();
                                org.setId(String.valueOf(document.get("id")));
                                org.setName((String) document.get("name"));
                                org.setProvince_name(provinceName);
                                org.setDistrict_name(districtName);
                                org.setWard_name(wardName);
                                org.setStreet(String.valueOf(document.get("street")));

                                orgList.add(org);
                            }
                            orgListAdapter.setOrgList(orgList);
                            orgListAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
