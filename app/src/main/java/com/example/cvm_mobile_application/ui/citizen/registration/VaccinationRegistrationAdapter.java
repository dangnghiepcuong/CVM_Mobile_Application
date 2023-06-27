package com.example.cvm_mobile_application.ui.citizen.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Register;

import java.util.List;

public class VaccinationRegistrationAdapter extends RecyclerView.Adapter<VaccinationRegistrationAdapter.VaccinationRegistrationViewHolder>{
    private final Context context;
    private List<Register> registrationList;
    public VaccinationRegistrationAdapter(Context context, List<Register> registrationList) {
        this.context = context;
        this.registrationList = registrationList;
    }

    public List<Register> getRegistrationList() {
        return registrationList;
    }

    public void setRegistrationList(List<Register> registrationList) {
        this.registrationList = registrationList;
    }

    @NonNull
    @Override
    public VaccinationRegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vaccination_registration, parent, false);

        return new VaccinationRegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VaccinationRegistrationViewHolder holder, int position) {
        Register register = registrationList.get(position);

        String[] dateString = register.getSchedule().getOnDateString().split("-");

        holder.tvDD.setText(dateString[2]);
        holder.tvMMYYYY.setText(dateString[1] + "/" + dateString[0]);
        holder.tvOrgName.setText(register.getSchedule().getOrg().getName());
        holder.tvVaccineType.setText(register.getSchedule().getVaccine_id());
        holder.tvNO.setText(String.valueOf(register.getNum_order()));
    }

    @Override
    public int getItemCount() {
        return registrationList.size();
    }

    public static class VaccinationRegistrationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDD;
        TextView tvMMYYYY;
        TextView tvOrgName;
        TextView tvNO;
        TextView tvVaccineType;
        TextView tvStatus;
        public VaccinationRegistrationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDD = itemView.findViewById(R.id.tv_dd_registration);
            tvMMYYYY = itemView.findViewById(R.id.tv_mmyyyy_registration);
            tvOrgName = itemView.findViewById(R.id.tv_org_name);
            tvNO = itemView.findViewById(R.id.tv_number_order);
            tvVaccineType = itemView.findViewById(R.id.tv_vaccine_type);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
