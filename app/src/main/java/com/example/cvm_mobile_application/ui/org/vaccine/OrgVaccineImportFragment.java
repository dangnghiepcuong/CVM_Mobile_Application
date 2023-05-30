package com.example.cvm_mobile_application.ui.org.vaccine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.db.model.VaccineLot;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgVaccineImportFragment extends Fragment {
    private View view;
    private FirebaseFirestore db;
    private Organization org;
    private List<SpinnerOption> vaccineList;
    private Spinner spVaccineType;
    private SpinnerAdapter spVaccineTypeAdapter;
    private EditText etVaccineLot;
    private TextView tvImportDate;
    private Button btnImportDP;
    private EditText etQuantity;
    private TextView tvExpirationDate;
    private Button btnExpirationDP;
    private Button btnImportVaccine;
    private DatePicker dpImportDate;
    private DatePicker dpExpirationDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org_vaccine_import, container, false);
        db = FirebaseFirestore.getInstance();
        org = requireArguments().getParcelable("org");
        vaccineList = new ArrayList<>();

        implementView();
        bindViewData();
        setViewListener();
        return view;
    }

    public void implementView() {
        spVaccineType = view.findViewById(R.id.sp_vaccine_type);
        etVaccineLot = view.findViewById(R.id.et_vaccine_lot);
        tvImportDate = view.findViewById(R.id.tv_import_date);
        btnImportDP = view.findViewById(R.id.btn_import_dp);
        dpImportDate = view.findViewById(R.id.dp_import_date);
        etQuantity = view.findViewById(R.id.et_quantity);
        tvExpirationDate = view.findViewById(R.id.tv_expiration_date);
        btnExpirationDP = view.findViewById(R.id.btn_expiration_dp);
        dpExpirationDate = view.findViewById(R.id.dp_expiration_date);
        btnImportVaccine = view.findViewById(R.id.btn_import_vaccine);
    }

    public void bindViewData() {
        getVaccineList();
        spVaccineTypeAdapter = new SpinnerAdapter(requireActivity().getApplicationContext(), R.layout.item_string, vaccineList);
        spVaccineType.setAdapter(spVaccineTypeAdapter);
    }

    public void setViewListener() {
        btnImportDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dpImportDate.getVisibility() == View.GONE) {
                    dpImportDate.setVisibility(View.VISIBLE);
                } else {
                    dpImportDate.setVisibility(View.GONE);
                }
            }
        });

        dpImportDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tvImportDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });

        btnExpirationDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dpExpirationDate.getVisibility() == View.GONE) {
                    dpExpirationDate.setVisibility(View.VISIBLE);
                } else {
                    dpExpirationDate.setVisibility(View.GONE);
                }
            }
        });

        dpExpirationDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                tvExpirationDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        });

        btnImportVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(etQuantity.getText()).equals("")) {
                    etQuantity.setText("0");
                }

                // IF DATA HAVEN'T RETRIEVED YET, RETURN
                SpinnerOption spOption = new SpinnerOption();
                try {
                    spOption = (SpinnerOption) spVaccineType.getSelectedItem();
                } catch (NullPointerException e) {
                    return;
                }

                VaccineLot vaccineLot = new VaccineLot();
                try {
                    vaccineLot = new VaccineLot(org.getId(), String.valueOf(etVaccineLot.getText()), String.valueOf(spOption.getValue()), String.valueOf(tvImportDate.getText()), Integer.parseInt(String.valueOf(etQuantity.getText())), String.valueOf(tvExpirationDate.getText()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "*Số lượng phải là số", Toast.LENGTH_LONG).show();
                    return;
                }

                if (vaccineLot.getVaccineId() == null || vaccineLot.getVaccineId().equals("")) {
                    Toast.makeText(getActivity(), "*Chọn loại vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }

                if (vaccineLot.getLot().equals("")) {
                    Toast.makeText(getActivity(), "*Nhập số lô vắc-xin", Toast.LENGTH_LONG).show();
                    return;
                }

                if (vaccineLot.getImportDate().equals("")) {
                    Toast.makeText(getActivity(), "*Chọn ngày nhập", Toast.LENGTH_LONG).show();
                    return;
                }

//                if (vaccineLot.getQuantity() == 0) {
//                    Toast.makeText(getActivity(), "*Nhập số lượng", Toast.LENGTH_LONG).show();
//                    return;
//                }

                if (vaccineLot.getExpirationDate().equals("")) {
                    Toast.makeText(getActivity(), "*Nhập ngày hết hạn", Toast.LENGTH_LONG).show();
                    return;
                }

                OrgVaccineImportFragment.this.getVaccineLot(vaccineLot);
            }
        });
    }

    public void getVaccineList() {
        // DISABLE SPINNER VACCINE LOT BEFORE GETTING NEW VACCINE TYPE
        db.collection("vaccines").get().addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        SpinnerOption spOption = new SpinnerOption((String) document.get("name"), (String) document.get("id"));
                        vaccineList.add(spOption);
                    }
                    spVaccineTypeAdapter.notifyDataSetChanged();
                } else {
                    Log.d("myTAG", "Retrieving Data: getVaccineList");
                    Toast.makeText(getActivity().getApplicationContext(), "Lỗi khi lấy danh sách vaccine", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getVaccineLot(VaccineLot vaccineLot) {
        db.collection("vaccine_inventory").whereEqualTo("lot", vaccineLot.getLot()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String vaccineLot_queried = "";
                    if (task.getResult().isEmpty() == false) {
                        Toast.makeText(getActivity(), "Lô vắc-xin đã có trong kho,\nvui lòng thực hiện cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        OrgVaccineImportFragment.this.importVaccine(vaccineLot);
                    }
                } else {
                    Log.d("myTAG", "Retrieving Data: getVaccineLot");
                    Toast.makeText(requireActivity().getApplicationContext(), "Lỗi khi lấy dữ liệu từ kho vắc-xin", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void importVaccine(VaccineLot vaccineLot) {
        Map<String, Object> data = new HashMap<>();
        data.put("org_id", vaccineLot.getOrgId());
        data.put("lot", vaccineLot.getLot());
        data.put("vaccine_id", vaccineLot.getVaccineId());
        data.put("import_date", vaccineLot.getImportDate());
        data.put("quantity", vaccineLot.getQuantity());
        data.put("expiration_date", vaccineLot.getExpirationDate());

        db.collection("vaccine_inventory")
                .document(vaccineLot.getOrgId() + vaccineLot.getVaccineId() + vaccineLot.getLot())
                .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Nhập dữ liệu vào kho vắc-xin thành công!", Toast.LENGTH_SHORT).show();

                            etVaccineLot.setText("");
                            tvImportDate.setText("");
                            etQuantity.setText("");
                            tvExpirationDate.setText("");
                        } else {
                            Log.d("myTAG", "Writing Data: importVaccine");
                            Toast.makeText(requireActivity().getApplicationContext(), "Lỗi khi nhập dữ liệu vào kho vắc-xin", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
