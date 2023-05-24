package com.example.cvm_mobile_application.ui.citizen;


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
import com.example.cvm_mobile_application.databinding.ActivityMainBinding;
import com.example.cvm_mobile_application.ui.citizen.notification.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

@BuildCompat.PrereleaseSdkCheck
public class CitizenNavigationBottom extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = getIntent().getStringExtra("username");
        getCitizenNavigationBottom(username);

        // This callback will only be called when MyFragment is at least Started.

//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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

    public void getCitizenNavigationBottom(String username) {
        setContentView(R.layout.citizen_navigation_bottom);
        getHomeScreen(username);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                default:
                case R.id.home:
                    getHomeScreen(username);
                    break;

                case R.id.info:
                    getPersonalMenuScreen(username);
                    break;

                case R.id.notification:
                    getNotificationScreen(username);
                    break;

                case R.id.registration:
                    replaceFragment(new RegistrationFragment());
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void getHomeScreen(String username) {
        db.collection("users")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Citizen citizen = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                citizen = document.toObject(Citizen.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("citizen", citizen);
                            CitizenHomeFragment citizenHomeFragment = new CitizenHomeFragment();
                            citizenHomeFragment.setArguments(bundle);
                            replaceFragment(citizenHomeFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(CitizenNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getPersonalMenuScreen(String username) {
        db.collection("users")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Citizen citizen = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                citizen = document.toObject(Citizen.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("citizen", citizen);
                            CitizenPersonalMenuFragment personalMenuFragment = new CitizenPersonalMenuFragment();
                            personalMenuFragment.setArguments(bundle);
                            replaceFragment(personalMenuFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(CitizenNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getNotificationScreen(String username) {
        db.collection("users")
                .whereEqualTo("email", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Citizen citizen = new Citizen();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                citizen = document.toObject(Citizen.class);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("citizen", citizen);
                            NotificationFragment notificationFragment = new NotificationFragment();
                            notificationFragment.setArguments(bundle);
                            replaceFragment(notificationFragment);
                        } else {
                            Log.w("myTAG", "queryCollection:failure", task.getException());
                            Toast.makeText(CitizenNavigationBottom.this, "*Đã có lỗi xảy ra. Vui lòng thử lại!"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box{
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Exiting the App")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // The user wants to leave - so dismiss the dialog and exit
                        finish();
                        dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // The user is not sure, so you can exit or just stay
                        dialog.dismiss();
                    }
                }).show();
    }
}
