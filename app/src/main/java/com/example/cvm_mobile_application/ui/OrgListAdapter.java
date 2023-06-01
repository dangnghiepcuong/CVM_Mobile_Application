package com.example.cvm_mobile_application.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;

import java.util.List;

public class OrgListAdapter extends ArrayAdapter<Organization> {
    private List<Organization> orgList;
    private final LayoutInflater inflater;

    public OrgListAdapter(@NonNull Context context, int resource, @NonNull List<Organization> objects) {
        super(context, resource, objects);
        this.orgList = objects;
        inflater = LayoutInflater.from(context);
    }

    public List<Organization> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Organization> orgList) {
        this.orgList = orgList;
    }

    @Override
    public int getCount() {
        return orgList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_org, null);

        TextView tvOrgName = itemView.findViewById(R.id.tv_org_name);
        tvOrgName.setText(orgList.get(position).getName());

        String orgAddress =
                "Địa chỉ: " + orgList.get(position).getStreet() + ", "
                + orgList.get(position).getWard_name() + ", "
                + orgList.get(position).getDistrict_name() + ", "
                + orgList.get(position).getProvince_name();

        TextView tvOrgAddress = itemView.findViewById(R.id.tv_org_address);
        tvOrgAddress.setText(orgAddress);
        TextView tvOrgContact = itemView.findViewById(R.id.tv_org_contact);
        tvOrgContact.setText("Liên hệ: "+ "contact");

        TextView tcOrgNSchedule = itemView.findViewById(R.id.tv_org_n_schedules);
        tcOrgNSchedule.setText("Số lịch tiêm hiện có: " + "n_schedule");

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_string, parent, false);

        TextView tvOrgName = convertView.findViewById(R.id.tv_org_name);

        TextView tvVaccineType = convertView.findViewById(R.id.tv_vaccine_type);

        TextView tvOnDate = convertView.findViewById(R.id.tv_on_date);

        TextView tvNRegisted = convertView.findViewById(R.id.tv_org_n_schedules);
        return convertView;
    }

}
