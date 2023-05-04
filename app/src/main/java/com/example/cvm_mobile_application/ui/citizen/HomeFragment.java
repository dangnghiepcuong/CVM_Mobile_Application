package com.example.cvm_mobile_application.ui.citizen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cvm_mobile_application.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    FirebaseFirestore firestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Map<String, Object> users = new HashMap<>();
        

        return inflater.inflate(R.layout.fragment_citizen_home, container, false);
    }
}