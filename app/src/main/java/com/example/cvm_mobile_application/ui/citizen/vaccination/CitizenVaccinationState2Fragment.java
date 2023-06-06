package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.objects.DVHCHelper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citizen_vaccination_state2, container, false);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(requireActivity().getApplicationContext());

        citizen = getArguments().getParcelable("citizen");
        orgList = new ArrayList<>();

        try {
            implementView();
            bindViewData();
            setViewListener();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    @Override
    public void implementView() {
        btnRegionFilter = view.findViewById(R.id.btn_region_filter);
        layoutRegionFilter = view.findViewById(R.id.layout_linear_region_filter);

        spProvince = view.findViewById(R.id.sp_province);
        spDistrict = view.findViewById(R.id.sp_district);
        spWard = view.findViewById(R.id.sp_ward);

        //SET ORG LIST VIEW
        recyclerViewOrgList = view.findViewById(R.id.view_recycler_org_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewOrgList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() throws JSONException {

        //GET PROVINCE LIST
        provinceList = dvhcHelper.getLocalList(DVHCHelper.PROVINCE_LEVEL, null);

        //SET PROVINCE INFO VALUE
        spProvinceListAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(),
                R.layout.item_string, provinceList);
        spProvince.setAdapter(spProvinceListAdapter);
        int provincePosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.PROVINCE_LEVEL, citizen.getProvince_name(), null);
        spProvince.setSelection(provincePosition, true);

        //GET DISTRICT LIST
        String provinceCode = ((SpinnerOption) spProvince.getItemAtPosition(spProvince.getSelectedItemPosition())).getValue();
        districtList = dvhcHelper.getLocalList(DVHCHelper.DISTRICT_LEVEL, provinceCode);

        //SET DISTRICT INFO VALUE
        spDistrictListAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(),
                R.layout.item_string, districtList);
        spDistrict.setAdapter(spDistrictListAdapter);
        int districtPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.DISTRICT_LEVEL, citizen.getDistrict_name(), provinceCode);
        spDistrict.setSelection(districtPosition, true);

        //GET WARD LIST
        String districtCode = ((SpinnerOption) spDistrict.getItemAtPosition(spDistrict.getSelectedItemPosition())).getValue();
        wardList = dvhcHelper.getLocalList(DVHCHelper.WARD_LEVEL, districtCode);

        //SET WARD INFO VALUE
        spWardListAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(),
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = dvhcHelper.getLocalPositionFromList(
                DVHCHelper.WARD_LEVEL, citizen.getWard_name(), districtCode);
        spWard.setSelection(wardPosition, true);

        //GET THE ORG LIST BASE ON THE INIT FILTER
        getOrgList(citizen.getProvince_name(), citizen.getDistrict_name(), citizen.getWard_name());

        //BIND ORG LIST DATA
        orgListAdapter = new OrgListAdapter(getActivity().getApplicationContext(), orgList);
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

        //SET PROVINCE SPINNER LISTENER
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET DISTRICT LIST, WARD LIST
                CitizenVaccinationState2Fragment.this
                        .spProvinceTriggeredActivities();
                Log.i("myTAG", "province spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SET DISTRICT SPINNER LISTENER
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET WARD LIST
                CitizenVaccinationState2Fragment.this
                        .spDistrictTriggeredActivities();
                Log.i("myTAG", "district spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitizenVaccinationState2Fragment.this
                        .spWardTriggeredActivities();
                Log.i("myTAG", "ward spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//        spProvince.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
//            @Override
//            public void onChildViewAdded(View parent, View child) {
//
//            }
//
//            @Override
//            public void onChildViewRemoved(View parent, View child) {
//
//            }
//        });
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
}
