package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.helpers.DVHCHelper;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CitizenVaccinationState2Activity extends AppCompatActivity implements ViewStructure {

    private FirebaseFirestore db;
    private Citizen citizen;
    private DVHCHelper dvhcHelper;
    private List<Organization> orgList;
    private RecyclerView recyclerViewOrgList;
    private OrgAdapter orgAdapter;
    private TextView btnRegionFilter;
    private LinearLayout layoutRegionFilter;
    private OnOrgItemClickListener onOrgItemClickListener;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_vaccination_state2);

        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        citizen = getIntent().getParcelableExtra("citizen");
        orgList = new ArrayList<>();

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);

        btnRegionFilter = findViewById(R.id.btn_region_filter);
        layoutRegionFilter = findViewById(R.id.layout_linear_region_filter);

        dvhcHelper.setSpProvince(findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(findViewById(R.id.sp_ward));

        //SET ORG LIST VIEW
        recyclerViewOrgList = findViewById(R.id.view_recycler_org_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewOrgList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Bước 2: Chọn đơn vị tiêm chủng");

        //SET LOCAL VALUE
        try {
            dvhcHelper.bindLocalListSpinnerData(getApplicationContext(),
                    citizen.getProvince_name(),
                    citizen.getDistrict_name(),
                    citizen.getWard_name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //GET THE ORG LIST BASE ON THE INIT FILTER
        getOrgList(citizen.getProvince_name(), citizen.getDistrict_name(), citizen.getWard_name());

        //BIND ORG LIST DATA
        orgAdapter = new OrgAdapter(getApplicationContext(), orgList);
        recyclerViewOrgList.setAdapter(orgAdapter);
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(v -> finish());

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

        onOrgItemClickListener = item -> {
            Intent intent = new Intent(getApplicationContext(), CitizenVaccinationState3Activity.class);
            intent.putExtra("citizen", citizen);
            intent.putExtra("organization", item);
            startActivity(intent);
        };
        orgAdapter.setListener(onOrgItemClickListener);

        dvhcHelper.setLocalListSpinnerListener(() -> {
            CitizenVaccinationState2Activity.this.getOrgList(
                    dvhcHelper.getSelectedLocal(DVHCHelper.PROVINCE_LEVEL).getOption(),
                    dvhcHelper.getSelectedLocal(DVHCHelper.DISTRICT_LEVEL).getOption(),
                    dvhcHelper.getSelectedLocal(DVHCHelper.WARD_LEVEL).getOption());
        });
    }

    public void getOrgList(String provinceName, String districtName, String wardName) {
        Query query = db.collection("organizations");
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orgList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Organization org = document.toObject(Organization.class);
//                            org.setId(String.valueOf(document.get("id")));
//                            org.setName((String) document.get("name"));
//                            org.setProvince_name(provinceName);
//                            org.setDistrict_name(districtName);
//                            org.setWard_name(wardName);
//                            org.setStreet(String.valueOf(document.get("street")));

                            orgList.add(org);
                        }
                        orgAdapter.setOrgList(orgList);
                        orgAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CitizenVaccinationState2Activity.this,
                                "Lỗi khi lấy dữ liệu. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
