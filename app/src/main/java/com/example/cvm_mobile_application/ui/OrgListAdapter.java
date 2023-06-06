package com.example.cvm_mobile_application.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;

import java.util.List;

public class OrgListAdapter extends RecyclerView.Adapter<OrgListAdapter.OrganizationViewHolder> {
    private final Context context;
    private List<Organization> orgList;

    public OrgListAdapter(Context context, List<Organization> orgList) {
        this.context = context;
        this.orgList = orgList;
    }

    public List<Organization> getOrgList() {
        return orgList;
    }

    public void setOrgList (List<Organization> orgList) {
        this.orgList = orgList;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_org, parent, false);

        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationViewHolder holder, int position) {
        Organization org = orgList.get(position);
        holder.tvOrgName.setText(org.getName());

        holder.tvOrgAddress.setText(org.getStreet());
//        holder.tvOrgContact.setText(org.getContact());
//        holder.tvOrgNSchedules.setText(org.getNSchedules());
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    public static class OrganizationViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrgName;
        TextView tvOrgAddress;
        TextView tvOrgContact;
        TextView tvOrgNSchedules;

        public OrganizationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrgName = itemView.findViewById(R.id.tv_org_name);
            tvOrgAddress = itemView.findViewById(R.id.tv_org_address);
            tvOrgContact = itemView.findViewById(R.id.tv_org_contact);
            tvOrgNSchedules = itemView.findViewById(R.id.tv_org_n_schedules);
        }
    }
}
