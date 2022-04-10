package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.databinding.ActivityDonationScreenBinding;

public class DonationScreen extends AppCompatActivity {

    private ActivityDonationScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AnimationDrawable animationDrawable = (AnimationDrawable) binding.donationLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/rewardapp-1070c.appspot.com/o/paytm_qr.jpeg?alt=media&token=4f469b76-1b93-4431-8a60-46066764e75f";
        Glide.with(this).load(imageUrl).into(binding.paytmQr);

        binding.donationBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DonationScreen.this, MainActivity.class);
                intent.putExtra("frag",2);
                startActivity(intent);
                finish();

            }
        });


        binding.rateUsDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(i);
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.paytmQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DonationScreen.this, "Scan QR and Support our Team.", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DonationScreen.this, MainActivity.class);
        intent.putExtra("frag",2);
        startActivity(intent);
        finish();
    }
}