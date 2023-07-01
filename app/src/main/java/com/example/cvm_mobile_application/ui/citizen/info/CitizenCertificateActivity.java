package com.example.cvm_mobile_application.ui.citizen.info;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CitizenCertificateActivity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private Citizen citizen;
    private TextView tvNumInjection;
    private TextView tvFullName;
    private TextView tvBirthday;
    private TextView tvId;
    private Button btnBack;
    private TextView tbTitle;
    private LinearLayout tbBgrMenu;
    private LinearLayoutCompat bgrActivity;
    private LinearLayoutCompat bgrToolbar1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_cert);
        db = FirebaseFirestore.getInstance();
    }

    protected void onStart() {
        super.onStart();
        citizen = getIntent().getParcelableExtra("citizen");

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);
        tbBgrMenu = findViewById(R.id.tb_bgr_menu);
        bgrActivity = findViewById(R.id.brg_activity);
        bgrToolbar1 = findViewById(R.id.toolbar1);

        tvNumInjection = findViewById(R.id.tv_num_injection);
        tvFullName = findViewById(R.id.tv_fullName);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvId = findViewById(R.id.tv_id);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Chứng nhận tiêm chủng");

        CitizenCertificateActivity.this.getCertificateType();



        tvFullName.setText(citizen.getFull_name());
        tvBirthday.setText(citizen.getBirthdayString());
        tvId.setText(citizen.getId());
    }

    @Override
    public void setViewListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getCertificateType(){
        db.collection("certificates")
                .whereEqualTo("citizen_id", citizen.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Integer numInjection = Integer.valueOf(String.valueOf(document.get("doses")));
                            String status = "ĐÃ TIÊM " + numInjection + " MŨI VACCINE";

                            switch (numInjection){
                                case 0:
                                    bgrActivity.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.Red));
                                    tbBgrMenu.setBackgroundResource(R.drawable.card_red_rounded_corner);
                                    bgrToolbar1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.DarkRed));
                                    tvNumInjection.setText("CHƯA TIÊM VACCINE");
                                    break;
                                case 1:
                                    bgrActivity.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.Yellow));
                                    tbBgrMenu.setBackgroundResource(R.drawable.card_yellow_rounded_corner);
                                    bgrToolbar1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.DarkYellow));
                                    tvNumInjection.setText(status);
                                    break;
                                default:
                                    bgrActivity.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.Green));
                                    tbBgrMenu.setBackgroundResource(R.drawable.card_green_rounded_corner);
                                    bgrToolbar1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.DarkGreen));
                                    tvNumInjection.setText(status);
                                    break;
                            }
                        }
                    }
                });
    }

}
