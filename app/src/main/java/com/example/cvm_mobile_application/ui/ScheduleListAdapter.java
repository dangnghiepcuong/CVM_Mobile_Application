package com.example.cvm_mobile_application.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Schedule;
import com.example.cvm_mobile_application.ui.org.OnScheduleItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {
    private final Context context;
    private List<Schedule> scheduleList;
    private OnScheduleItemClickListener listener;
    public ScheduleListAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public OnScheduleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnScheduleItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        holder.tvOrgName.setText(schedule.getOrgId());
        holder.tvVaccineTupe.setText(schedule.getVaccineId());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String onDate = df.format(schedule.getOnDate());
        holder.tvOnDate.setText(onDate);

        String nRegistered =
                "Sáng: " + schedule.getDayRegistered() +"/"
                        + schedule.getLimitDay()
                        + ". Chiều: " + schedule.getNoonRegistered() +"/"
                        + schedule.getLimitNoon()
                        + ". Tối: " + schedule.getNightRegistered() +"/"
                        + schedule.getLimitNight();
        holder.tvNRegistered.setText(nRegistered);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(schedule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrgName;
        TextView tvVaccineTupe;
        TextView tvOnDate;
        TextView tvNRegistered;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrgName = itemView.findViewById(R.id.tv_org_name);
            tvVaccineTupe = itemView.findViewById(R.id.tv_vaccine_type);
            tvOnDate = itemView.findViewById(R.id.tv_on_date);
            tvNRegistered = itemView.findViewById(R.id.iv_n_registered);
        }
    }
}
