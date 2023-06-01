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
import com.example.cvm_mobile_application.data.db.model.Schedule;

import java.util.List;

public class ScheduleListAdapter extends ArrayAdapter<Schedule> {
    private List<Schedule> scheduleList;
    private final LayoutInflater inflater;

    public ScheduleListAdapter(@NonNull Context context, int resource, @NonNull List<Schedule> objects) {
        super(context, resource, objects);
        this.scheduleList = objects;
        inflater = LayoutInflater.from(context);
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_schedule, null);

        TextView tvOrgName = itemView.findViewById(R.id.tv_org_name);
        tvOrgName.setText(scheduleList.get(position).getOrg().getName());
        TextView tvVaccineType = itemView.findViewById(R.id.tv_vaccine_type);

        TextView tvOnDate = itemView.findViewById(R.id.tv_on_date);

        TextView tvNRegisted = itemView.findViewById(R.id.iv_n_registed);

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_string, parent, false);

        TextView tvOrgName = convertView.findViewById(R.id.tv_org_name);

        TextView tvVaccineType = convertView.findViewById(R.id.tv_vaccine_type);

        TextView tvOnDate = convertView.findViewById(R.id.tv_on_date);

        TextView tvNRegisted = convertView.findViewById(R.id.iv_n_registed);
        return convertView;
    }
}
