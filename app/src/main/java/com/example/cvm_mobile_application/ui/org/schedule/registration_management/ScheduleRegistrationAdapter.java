package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Register;

import java.util.List;

public class ScheduleRegistrationAdapter extends RecyclerView.Adapter<ScheduleRegistrationAdapter.RegisterViewHolder>{
    private final Context context;
    private List<Register> registryList;
    private View.OnClickListener listenerMoreBtn;
    private View.OnClickListener listenerViewProfile;
    private View.OnClickListener listenerCheckIn;
    private View.OnClickListener listenerInject;
    private View.OnClickListener listenerCancel;
    public ScheduleRegistrationAdapter(Context context, List<Register> registryList) {
        this.context = context;
        this.registryList = registryList;
    }

    public void setRegistryList(List<Register> registryList) {
        this.registryList = registryList;
    }

    public void setListener(View.OnClickListener listenerMoreBtn){
        this.listenerMoreBtn = listenerMoreBtn;
    }

    public void setListenerViewProfile(View.OnClickListener listenerViewProfile) {
        this.listenerViewProfile = listenerViewProfile;
    }

    public void setListenerCheckIn(View.OnClickListener listenerCheckIn) {
        this.listenerCheckIn = listenerCheckIn;
    }

    public void setListenerInject(View.OnClickListener listenerInject) {
        this.listenerInject = listenerInject;
    }

    public void setListenerCancel(View.OnClickListener listenerCancel) {
        this.listenerCancel = listenerCancel;
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

        String shift = "Buổi tiêm: " + registry.getShift();
        holder.tvShift.setText(shift);

        holder.tvId.setText(registry.getCitizen_id());

        String numOrder = String.valueOf(registry.getNumber_order());
        holder.tvNumOrder.setText(numOrder);

        holder.ibShowMore.setOnClickListener(v -> {
            if (holder.layoutMoreBtn.getVisibility() == View.GONE) {
                holder.layoutMoreBtn.setVisibility(View.VISIBLE);
            } else {
                holder.layoutMoreBtn.setVisibility(View.GONE);
            }

            switch (registry.getStatus()) {
                case 0:
                    holder.btnCheckIn.setVisibility(View.VISIBLE);
                    holder.btnInject.setVisibility(View.GONE);
                    holder.btnCancel.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    holder.btnCheckIn.setVisibility(View.GONE);
                    holder.btnInject.setVisibility(View.VISIBLE);
                    holder.btnCancel.setVisibility(View.VISIBLE);
                    break;

                default:
                case 2:
                case 3:
                    holder.btnCheckIn.setVisibility(View.GONE);
                    holder.btnInject.setVisibility(View.GONE);
                    holder.btnCancel.setVisibility(View.GONE);
                    break;
            }
        });

        holder.btnViewProfile.setOnClickListener(v -> {
            listenerViewProfile.onClick(v);
        });

        holder.btnCheckIn.setOnClickListener(v -> {
            listenerCheckIn.onClick(v);
        });

        holder.btnInject.setOnClickListener(v -> {
            listenerInject.onClick(v);
        });

        holder.btnCancel.setOnClickListener(v -> {
            listenerCancel.onClick(v);
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
        ImageButton ibShowMore;
        Button btnViewProfile;
        Button btnCheckIn;
        Button btnInject;
        Button btnCancel;
        LinearLayout layoutMoreBtn;
        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShift = itemView.findViewById(R.id.tv_shift);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNumOrder = itemView.findViewById(R.id.tv_num_order);
            ibShowMore = itemView.findViewById(R.id.ib_show_more);
            btnViewProfile = itemView.findViewById(R.id.btn_view_profile);
            btnCheckIn = itemView.findViewById(R.id.btn_check_in);
            btnInject = itemView.findViewById(R.id.btn_inject);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            layoutMoreBtn = itemView.findViewById(R.id.layout_more_btn);
        }
    }

    public interface OnRegistrationItemClickListener {
        public void onMoreOptionsClickListener(Register register);
    }
}
