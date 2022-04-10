package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_splash_screen, null, false);
            setContentView(binding.getRoot());



            auth = FirebaseAuth.getInstance();

            //binding.versionSplashText.setText("v " + String.valueOf(BuildConfig.VERSION_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }


        startThread();


    }

    private void startThread() {

        Thread thread = new Thread(){
            public void run(){

                try{
                    sleep(3000);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(auth.getCurrentUser()!=null)
                    {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra("frag",3);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreen.this, RegisterScreen.class);
                        startActivity(intent);
                    }
                }
            }
        };
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();

startThread();

    }
}