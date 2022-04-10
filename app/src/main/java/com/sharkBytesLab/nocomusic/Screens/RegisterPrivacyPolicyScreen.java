package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sharkBytesLab.nocomusic.databinding.ActivityRegisterPrivacyPolicyScreenBinding;

public class RegisterPrivacyPolicyScreen extends AppCompatActivity {

    private ActivityRegisterPrivacyPolicyScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPrivacyPolicyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {

            binding.termsWebViewRegister.getSettings().setJavaScriptEnabled(true);
            binding.termsWebViewRegister.loadUrl("file:///android_asset/policy.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.registerPolicyBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterPrivacyPolicyScreen.this, RegisterScreen.class));
                finish();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(RegisterPrivacyPolicyScreen.this, RegisterScreen.class));
        finish();

    }
}