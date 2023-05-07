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
import com.example.cvm_mobile_application.data.SpinnerOption;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerOption> {
    private List<SpinnerOption> optionList;
    private LayoutInflater inflater;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerOption> objects) {
        super(context, resource, objects);
        this.optionList = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_string, null);

        TextView option = itemView.findViewById(R.id.option);
        option.setText(optionList.get(position).getOption());

        TextView value = itemView.findViewById(R.id.value);
        value.setText(optionList.get(position).getValue());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_string, parent, false);

        TextView option = convertView.findViewById(R.id.option);
        option.setText(optionList.get(position).getOption());

        TextView value = convertView.findViewById(R.id.value);
        value.setText(optionList.get(position).getValue());
        return convertView;
    }
}
