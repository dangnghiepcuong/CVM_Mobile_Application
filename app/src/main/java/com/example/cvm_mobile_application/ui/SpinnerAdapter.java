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

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private List<String> optionList;
    private LayoutInflater inflater;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.optionList = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_thumbnail, null);
        TextView thumbnail = itemView.findViewById(R.id.thumbnail);
        thumbnail.setText(optionList.get(position).getName());
        return itemView;
    }
}
