package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.BuildConfig;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.Model.AppStatsModel;
import com.sharkBytesLab.nocomusic.databinding.ActivityAppStatsScreenBinding;

public class AppStatsScreen extends AppCompatActivity {

    private ActivityAppStatsScreenBinding binding;
    private FirebaseFirestore firestore;
    private AppStatsModel model;
    private int totalUsers = 0;
    private long totalCoins = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppStatsScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        binding.versionStatsText.setText("Version : v  " + String.valueOf(BuildConfig.VERSION_NAME));

        Runnable objRunnable5 = new Runnable() {
            @Override
            public void run() {
                try {

        firestore.collection("DownloadCount").document("count").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                model = documentSnapshot.toObject(AppStatsModel.class);

                binding.songStatsText.setText("Total Songs : " + model.getSongs());
                binding.downloadStatsText.setText("Total Downloads : " + model.getNum());
                binding.ytSubsStatsText.setText("YouTube Subs : " + model.getSubs() + "+");

            }
        });

        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        totalUsers++;
                        try {
                            totalCoins += (long)documentChange.getDocument().get("coins");
                        } catch (Exception e) {
                            Log.d("Total Coins error : ", e.getMessage());
                            e.printStackTrace();
                        }


                    }
            }
                binding.usersStatsText.setText("Total Users : " + String.valueOf(totalUsers));
                try {
                    Log.d("Total Coins : ", String.valueOf(totalCoins));
                   binding.coinsStatsText.setText("Total Coins : " + String.valueOf(totalCoins));
                } catch (Exception e) {
                    Log.d("Total Coins error : ", e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}
        };
                Thread objBgThread = new Thread(objRunnable5);
                objBgThread.start();

        binding.appStatsBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AppStatsScreen.this, MainActivity.class);
                intent.putExtra("frag",2);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AppStatsScreen.this, MainActivity.class);
        intent.putExtra("frag",2);
        startActivity(intent);
        finish();

    }
}