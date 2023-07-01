package com.example.cvm_mobile_application.ui.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.data.db.model.Notification;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Notification> notificationList;
    private NotificationAdapter notificationAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        try {
            Citizen citizen = getArguments().getParcelable("citizen");

            getNotifications(citizen.getId(), view);
        } catch (Exception e){
            Organization organization = getArguments().getParcelable("org");
        }


        return view;
    }

    public void getNotifications(String userid, View view) {
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getActivity().getApplicationContext(), notificationList);

        recyclerView = view.findViewById(R.id.notification_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);

        db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .whereEqualTo("userid", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Notification notification = new Notification();
                                notification = document.toObject(Notification.class);
                                notificationList.add(notification);
                                notificationAdapter.notifyItemChanged(notificationList.size());
                            }

                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(getActivity(), "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}