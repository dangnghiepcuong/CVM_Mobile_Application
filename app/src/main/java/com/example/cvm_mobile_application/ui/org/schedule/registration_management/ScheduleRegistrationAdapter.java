package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

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

public class ScheduleRegistrationAdapter extends RecyclerView.Adapter<ScheduleRegistrationAdapter.RegisterViewHolder>{
    private final Context context;
    private List<Register> registryList;
    private OnRegistrationItemClickListener listener;

    public ScheduleRegistrationAdapter(Context context, List<Register> registryList) {
        this.context = context;
        this.registryList = registryList;
    }

    public List<Register> getRegistryList() {
        return registryList;
    }

    public void setRegistryList(List<Register> registryList) {
        this.registryList = registryList;
    }

    public OnRegistrationItemClickListener getListener(){
        return listener;
    }

    public void setListener(OnRegistrationItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleRegistrationAdapter.RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule_registration, parent, false);

        return new RegisterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterViewHolder holder, int position) {
        Register registry = registryList.get(position);

        holder.tvName.setText(registry.getCitizen_name());

        String shift = "Đăng ký buổi " + registry.getShift().toLowerCase();
        holder.tvShift.setText(shift);

        holder.tvId.setText(registry.getCitizen_id());

        String numOrder = String.valueOf(registry.getNum_order());
        holder.tvNumOrder.setText(numOrder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(registry);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(registryList == null) return 0;
        return registryList.size();
    }

    public static class RegisterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvShift;
        TextView tvId;
        TextView tvNumOrder;

        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShift = itemView.findViewById(R.id.tv_shift);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNumOrder = itemView.findViewById(R.id.tv_num_order);
        }
    }
}
