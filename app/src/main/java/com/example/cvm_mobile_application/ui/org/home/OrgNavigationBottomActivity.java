package com.example.cvm_mobile_application.ui.org.home;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.ui.notification.NotificationFragment;
import com.example.cvm_mobile_application.ui.org.info.OrgOptionalMenuFragment;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.OrgScheduleFragment_unused;
import com.example.cvm_mobile_application.ui.org.schedule.schedule_management.OrgScheduleListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@BuildCompat.PrereleaseSdkCheck public class OrgNavigationBottomActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    private OrgHomeFragment orgHomeFragment;
    private NotificationFragment notificationFragment;
    private OrgOptionalMenuFragment orgOptionalMenuFragment;
    private OrgScheduleFragment_unused orgScheduleFragment_unused;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_navigation_bottom);
        db = FirebaseFirestore.getInstance();

        String username = getIntent().getStringExtra("username");
        saveData(username);

        implementView();
        setViewListener();
        getOrgData(username);
    }

    protected void onStart(){
        super.onStart();
//        String username = getIntent().getStringExtra("username");
//        saveData(username);
//
//        implementView();
//        setViewListener();
//        getOrgData(username);
    }
    public void implementView() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    public void setViewListener() {
        // This callback will only be called when MyFragment is at least Started.
        // requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()

        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        finish();
                        /**
                         * onBackPressed logic goes here - For instance:
                         * Prevents closing the app to go home screen when in the
                         * middle of entering data to a form
                         * or from accidentally leaving a fragment with a WebView in it
                         *
                         * Unregistering the callback to stop intercepting the back gesture:
                         * When the user transitions to the topmost screen (activity, fragment)
                         * in the BackStack, unregister the callback by using
                         * OnBackInvokeDispatcher.unregisterOnBackInvokedCallback
                         * (https://developer.android.com/reference/kotlin/android/view/OnBackInvokedDispatcher#unregisteronbackinvokedcallback)
                         */
                    }
            );
        }
    }

    public void getOrgData(String username) {
        db.collection("organizations").document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Organization org = new Organization();
                            DocumentSnapshot document = task.getResult();
                            org = document.toObject(Organization.class);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("org", org);
                            orgHomeFragment = new OrgHomeFragment();
                            orgHomeFragment.setArguments(bundle);
                            replaceFragment(orgHomeFragment);

                            bottomNavigationView.setOnItemSelectedListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.home:
                                        orgHomeFragment = new OrgHomeFragment();
                                        orgHomeFragment.setArguments(bundle);
                                        replaceFragment(orgHomeFragment);
                                        break;

                                    case R.id.schedule:
                                        orgScheduleFragment_unused = new OrgScheduleFragment_unused();
                                        orgScheduleFragment_unused.setArguments(bundle);
                                        replaceFragment(orgScheduleFragment_unused);
                                        break;

                                    case R.id.notification:
                                        notificationFragment = new NotificationFragment();
                                        notificationFragment.setArguments(bundle);
                                        replaceFragment(notificationFragment);
                                        break;

                                    case R.id.info:
                                        orgOptionalMenuFragment = new OrgOptionalMenuFragment();
                                        orgOptionalMenuFragment.setArguments(bundle);
                                        replaceFragment(orgOptionalMenuFragment);
                                        break;
                                }
                                return true;
                            });
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(OrgNavigationBottomActivity.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box{
        if(isTaskRoot()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thoát ứng dụng")
                    .setMessage("Bạn có muốn thoát ứng dụng không?")
                    .setCancelable(false)
                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void saveData(String username){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.commit();
    }
}
