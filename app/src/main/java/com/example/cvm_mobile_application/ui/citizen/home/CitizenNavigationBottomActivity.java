package com.example.cvm_mobile_application.ui.citizen.home;


import android.content.DialogInterface;
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
import com.example.cvm_mobile_application.data.db.model.Citizen;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.example.cvm_mobile_application.ui.citizen.info.CitizenPersonalMenuFragment;
import com.example.cvm_mobile_application.ui.notification.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

@BuildCompat.PrereleaseSdkCheck
public class CitizenNavigationBottomActivity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    private CitizenHomeFragment citizenHomeFragment;
    private CitizenPersonalMenuFragment personalMenuFragment;
    private NotificationFragment notificationFragment;
    private Citizen citizen;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citizen_navigation_bottom);
        db = FirebaseFirestore.getInstance();

        username = getIntent().getStringExtra("username");
        implementView();
        bindViewData();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        username = getIntent().getStringExtra("username");
//
//        implementView();
//        bindViewData();
    }

    @Override
    public void implementView() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    @Override
    public void bindViewData() {
        getCitizenData();
    }

    @Override
    public void setViewListener() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("citizen", citizen);
        citizenHomeFragment = new CitizenHomeFragment();
        citizenHomeFragment.setArguments(bundle);
        replaceFragment(citizenHomeFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                default:
                case R.id.home:
                    citizenHomeFragment = new CitizenHomeFragment();
                    citizenHomeFragment.setArguments(bundle);
                    replaceFragment(citizenHomeFragment);
                    break;

                case R.id.registration:
                    // replaceFragment(new RegistrationFragment());
                    break;

                case R.id.notification:
                    notificationFragment = new NotificationFragment();
                    notificationFragment.setArguments(bundle);
                    replaceFragment(notificationFragment);
                    break;

                case R.id.info:
                    personalMenuFragment = new CitizenPersonalMenuFragment();
                    personalMenuFragment.setArguments(bundle);
                    replaceFragment(personalMenuFragment);
                    break;
            }
            return true;
        });

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

    public void getCitizenData() {
        db.collection("users")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            citizen = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                citizen = document.toObject(Citizen.class);
                            }
                            CitizenNavigationBottomActivity.this.setViewListener();
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(CitizenNavigationBottomActivity.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box{
        if (isTaskRoot()) {
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
}
