package com.sharkBytesLab.nocomusic;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sharkBytesLab.nocomusic.Fragment.CommunityFragment;
import com.sharkBytesLab.nocomusic.Fragment.DownloadFragment;
import com.sharkBytesLab.nocomusic.Fragment.ProfileFragment;
import com.sharkBytesLab.nocomusic.Fragment.PromotionFragment;
import com.sharkBytesLab.nocomusic.Fragment.SettingsFragment;
import com.sharkBytesLab.nocomusic.Model.ProfileModel;
import com.sharkBytesLab.nocomusic.Screens.WeeklyRewardScreen;
import com.sharkBytesLab.nocomusic.databinding.ActivityMainBinding;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private FirebaseAnalytics firebaseAnalytics;
    private DocumentSnapshot snapshot;
    private DocumentReference reference;
    private ProfileModel profileModel;
    private FirebaseRemoteConfig remoteConfig;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "coins";

    private int versionCode;
    private  boolean isPressed = false;
    private int frag = 3;

    private BottomSheetDialog underDevelopmentSheetDialog;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable())
        {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

            Button tryButton = dialog.findViewById(R.id.internetTryAgain);
            tryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    recreate();

                }
            });

            dialog.show();
        }



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

            try {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    frag = extras.getInt("frag");
                    Log.d("Fragment in Main: ",String.valueOf(frag));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
            AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
                {

                }
            } );

            binding.applovinMainAd.loadAd();



            switch (frag)
            {
                case 1:
                    replaceFragment(new DownloadFragment());
                    binding.bottomNavigation.setSelectedItemId(R.id.downloadSong);
                    break;

                case 2:
                    replaceFragment(new CommunityFragment());
                    binding.bottomNavigation.setSelectedItemId(R.id.community);
                    break;

                case 3:
                    replaceFragment(new PromotionFragment());
                    binding.bottomNavigation.setSelectedItemId(R.id.promotion);
                    break;

                case 4:
                    replaceFragment(new ProfileFragment());
                    binding.bottomNavigation.setSelectedItemId(R.id.profile);
                    break;

                case 5:
                    replaceFragment(new SettingsFragment());
                    binding.bottomNavigation.setSelectedItemId(R.id.settings);
                    break;

            }





        versionCode = getCurrentVersionCode();



        remoteConfig = FirebaseRemoteConfig.getInstance();

        checkUpdate();
        maintenanceBreakUpdate();
        rateUsDialogCheck();





            getDataFromDatabase();


            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build();
            remoteConfig.setConfigSettingsAsync(configSettings);

            remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {

                    if(task.isSuccessful())
                    {
                        final String weeklyReward = remoteConfig.getString("weekly_reward");

                        if(Integer.parseInt(weeklyReward) > 0)
                        {
                            Animation anim = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.shake);
                            anim.setDuration(200L);
                            binding.weeklyReward.startAnimation(anim);


                            binding.weeklyReward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                        Intent in = new Intent(MainActivity.this, WeeklyRewardScreen.class);
                                        int fragId = binding.bottomNavigation.getSelectedItemId();
                                        Log.d("Frag Id : " , String.valueOf(fragId));
                                        in.putExtra("previousFrag",fragId);
                                        startActivity(in);
                                }
                            });

                        }else
                        {

                            binding.weeklyReward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    underDevelopmentSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.AppBottomSheetDialogTheme);

                                    View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_in_development, findViewById(R.id.sheetViewLayoutInDevelopment));

                                    underDevelopmentSheetDialog.setContentView(sheetView);
                                    underDevelopmentSheetDialog.findViewById(R.id.closeInDevelopmentSheet).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            underDevelopmentSheetDialog.dismiss();

                                        }
                                    });
                                    underDevelopmentSheetDialog.show();


                                }
                            });

                        }

                    }



                }
            });







            binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {




                    switch (item.getItemId())

                    {
                        case R.id.promotion:

                            replaceFragment(new PromotionFragment());
                            break;

                        case R.id.downloadSong:


                            replaceFragment(new DownloadFragment());
                            break;

                        case R.id.community:

                            replaceFragment(new CommunityFragment());
                            break;


                        case R.id.profile:

                            replaceFragment(new ProfileFragment());
                            break;

                        case R.id.settings:

                            replaceFragment(new SettingsFragment());
                            break;
                    }
                    return true;
                }


            });






        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Database Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Database Error : ",e.getMessage());
        }




    }

    private void checkUpdate() {

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                if(task.isSuccessful())
                {
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if(Integer.parseInt(new_version_code) > getCurrentVersionCode())
                    {
                        showUpdateDialog();

                    }

                }



            }
        });

    }


    private void maintenanceBreakUpdate() {

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                if(task.isSuccessful())
                {
                    final String maintenance_break_code = remoteConfig.getString("maintenance_break_code");
                    if(Integer.parseInt(maintenance_break_code) > 0)
                    {
                        maintenanceDialog();

                    }

                }



            }
        });

    }


    private void rateUsDialogCheck() {

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                if(task.isSuccessful())
                {
                    final String rate_us = remoteConfig.getString("rate_us_code");

                    if(Integer.parseInt(rate_us) > 0)
                    {
                        rateUsDialog();

                    }

                }



            }
        });

    }

    private void maintenanceDialog()
    {
        Dialog dialog;
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.maintenance_break);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.maintenance_dialog_bg));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();

    }

    private void rateUsDialog()
    {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todaysDate = year + "" + month + "" + day;


        SharedPreferences sharedPreferences = getSharedPreferences("RATE", Context.MODE_PRIVATE);
        boolean currentDay = sharedPreferences.getBoolean(todaysDate, false);

        if(!currentDay)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(todaysDate, true);
            editor.apply();
            RateUsDialog rateUsDialog = new RateUsDialog(MainActivity.this);
            rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            rateUsDialog.setCancelable(false);
            rateUsDialog.show();
        }


    }

    private void showUpdateDialog()
    {

        Dialog dialog;
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.update_app);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.update_dialog_bg));

        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();

        Button update = dialog.findViewById(R.id.update_app);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    Intent in = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(in);

                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void getDataFromDatabase()
    {

        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileModel = documentSnapshot.toObject(ProfileModel.class);
                binding.mainCoins.setText(String.valueOf(profileModel.getCoins()));

                saveSharedPref(profileModel.getCoins());
            }
        });


    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, fragment);
        transaction.commit();

    }

    private void saveSharedPref(int currentCoins)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TEXT,currentCoins);
        editor.apply();
        Log.d("current coins : ",String.valueOf(currentCoins));

    }

    private int getCurrentVersionCode()
    {
        PackageInfo packageInfo = null;
        try{
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        }catch(Exception e)
        {
            Toast.makeText(this, "Application error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return packageInfo.versionCode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUpdate();
        getDataFromDatabase();
    }


    @Override
    public void onBackPressed() {

        if(isPressed)
        {
            finishAffinity();
            System.exit(0);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Press back again to Exit.", Toast.LENGTH_SHORT).show();
            isPressed = true;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isPressed = false;
            }
        };
        new Handler().postDelayed(runnable,2000);

    }
}