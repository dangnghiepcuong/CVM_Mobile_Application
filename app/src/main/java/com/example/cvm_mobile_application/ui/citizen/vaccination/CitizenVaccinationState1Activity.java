package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenProfileFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

@BuildCompat.PrereleaseSdkCheck public class CitizenVaccinationState1Activity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private List<Citizen> relatives;
    private Spinner spTargetList;
    private List<SpinnerOption> targetList;
    private String selectedTargetId;
//    private TextView btnDetailPersonalInfo;
//    private LinearLayout layoutDetailPersonalInfo;
    private SpinnerAdapter spTargetListAdapter;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_vaccination_state1);

        db = FirebaseFirestore.getInstance();

        //GET CITIZEN DATA FROM ACTIVITY
        citizen = getIntent().getParcelableExtra("citizen");

        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        bundle.putString("fromActivity", "CitizenVaccinationState1");
        CitizenProfileFragment citizenProfileFragment = new CitizenProfileFragment();
        citizenProfileFragment.setArguments(bundle);
        replaceFragment(citizenProfileFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        SpinnerOption spOption = new SpinnerOption(citizen.getFull_name(), citizen.getId());
        targetList = new ArrayList<>();
        targetList.add(spOption);

        //GET CITIZEN RELATIVES DATA FROM FIREBASE
        getTargetRelativesData();

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);

        spTargetList = findViewById(R.id.sp_target_list);
        spTargetListAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.item_string, targetList);
        spTargetList.setAdapter(spTargetListAdapter);

//        btnDetailPersonalInfo = findViewById(R.id.btn_detail_personal_info);
//        layoutDetailPersonalInfo = findViewById(R.id.layout_detail_personal_info);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Bước 1: Chọn đối tượng đăng ký tiêm");
        //SET TARGET ID
        selectedTargetId = citizen.getId();
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(v -> finish());

        //SET TARGET SPINNER LISTENER
        spTargetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerOption option = (SpinnerOption) parent.getItemAtPosition(position);

                // AVOID THE FIRST TRIGGER WHEN INITIALIZING SPINNER,
                // AND KEEP THE TRIGGERING OF NEXT SELECTION ON THE FIRST SELECTION
                if (selectedTargetId.equals(option.getValue())) {
                    return;
                }

                selectedTargetId = option.getValue();
                Log.i("myTAG", selectedTargetId);
                CitizenVaccinationState1Activity.this.getTargetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SET DETAIL PERSONAL INFO BUTTON LISTENER
//        btnDetailPersonalInfo.setOnClickListener(v -> {
//            int visibility = layoutDetailPersonalInfo.getVisibility();
//            switch (visibility) {
//                case View.GONE:
//                    layoutDetailPersonalInfo.setVisibility(View.VISIBLE);
//                    break;
//
//                case View.VISIBLE:
//                default:
//                case View.INVISIBLE:
//                    layoutDetailPersonalInfo.setVisibility(View.GONE);
//            }
//        });
    }

    public void getTargetData() {
        db.collection("users")
                .whereEqualTo("id", selectedTargetId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        citizen = new Citizen();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            citizen = document.toObject(Citizen.class);
                        }

                        CitizenVaccinationState1Activity.this.bindViewData();
                    }
                });
    }

    public void getTargetRelativesData() {
        db.collection("users")
                .whereEqualTo("guardian", citizen.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        relatives = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Citizen relative = document.toObject(Citizen.class);
                            relatives.add(relative);
                            SpinnerOption spinnerOption = new SpinnerOption(relative.getFull_name(), relative.getId());
                            targetList.add(spinnerOption);
                        }
                        spTargetListAdapter.setOptionList(targetList);
                        spTargetListAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
