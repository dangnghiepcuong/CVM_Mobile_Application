package com.example.cvm_mobile_application.ui.org.schedule.registration_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Register;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRegistrationAdapter extends RecyclerView.Adapter<ScheduleRegistrationAdapter.RegisterViewHolder>{
    private final Context context;
    private List<Register> registryList;
    private PopupMenu.OnMenuItemClickListener listener;

    public ScheduleRegistrationAdapter(Context context, List<Register> registryList) {
        this.context = context;
        this.registryList = registryList;
    }

    public void setRegistryList(List<Register> registryList) {
        this.registryList = registryList;
    }

    public void setListener(PopupMenu.OnMenuItemClickListener listener){
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

        String shift = "Buổi tiêm: " + registry.getShift();
        holder.tvShift.setText(shift);

        holder.tvId.setText(registry.getCitizen_id());

        String numOrder = String.valueOf(registry.getNumber_order());
        holder.tvNumOrder.setText(numOrder);

        String registryValues = "#" + registry.getCitizen_id() + "#" + registry.getShift();

        List<SpinnerOption> registrationOptions = new ArrayList<>();
        PopupMenu popupMenu = new PopupMenu(context, holder.ibMoreOptions);
        MenuInflater inflater = popupMenu.getMenuInflater();
        MenuItem item;
        switch (registry.getStatus()) {
            case 0:
//                registrationOptions.add(new SpinnerOption("Điểm danh", "1" + registryValues));
//                registrationOptions.add(new SpinnerOption("Hủy tiêm", "3" + registryValues));

//                popupMenu.getMenuInflater().inflate(R.menu.registration_0_menu, popupMenu.getMenu());
                inflater.inflate(R.menu.registration_0_menu, popupMenu.getMenu());
                item = popupMenu.getMenu().getItem(0);
                item.setContentDescription("0" + registryValues);
                item = popupMenu.getMenu().getItem(1);
                item.setContentDescription("1" + registryValues);
                item = popupMenu.getMenu().getItem(2);
                item.setContentDescription("3" + registryValues);
                popupMenu.show();
                break;

            case 1:
//                registrationOptions.add(new SpinnerOption("Đã tiêm", "2" + registryValues));
//                registrationOptions.add(new SpinnerOption("Hủy tiêm", "3" + registryValues));
//                popupMenu.getMenuInflater().inflate(R.menu.registration_1_menu, popupMenu.getMenu());
                inflater.inflate(R.menu.registration_1_menu, popupMenu.getMenu());
                item = popupMenu.getMenu().getItem(0);
                item.setContentDescription("0" + registryValues);
                item = popupMenu.getMenu().getItem(1);
                item.setContentDescription("2" + registryValues);
                item = popupMenu.getMenu().getItem(2);
                item.setContentDescription("3" + registryValues);
                popupMenu.show();
                break;

            case 2:
            case 3:
            default:
//                registrationOptions.add(new SpinnerOption("Xem hồ sơ tiêm chủng", "-1" + registryValues));
                break;
        }
//        SpinnerAdapter optionAdapter = new SpinnerAdapter(context, R.layout.item_string, registrationOptions);
//        holder.ibMoreOptions.setAdapter(optionAdapter);

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
        ImageButton ibMoreOptions;
        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShift = itemView.findViewById(R.id.tv_shift);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNumOrder = itemView.findViewById(R.id.tv_num_order);
//            ibMoreOptions = itemView.findViewById(R.id.iv_more_options);
        }
    }

    public interface OnRegistrationItemClickListener {
        public void onMoreOptionsClickListener(Register register);
    }
}
