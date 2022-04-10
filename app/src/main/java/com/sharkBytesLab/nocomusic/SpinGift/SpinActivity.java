package com.sharkBytesLab.nocomusic.SpinGift;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.SpinGift.model.LuckyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {

    private LuckyWheelView wheelView;
    private ImageView spinBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spin);

        wheelView = (LuckyWheelView) findViewById(R.id.wheelView);
        spinBtn = findViewById(R.id.spinBtn);



        List<LuckyItem> data = new ArrayList<>();

        LuckyItem item1 = new LuckyItem();
        item1.topText = "70";
        item1.secondaryText = "Coins";
        item1.color = Color.parseColor("#00cf00");
        item1.textColor = Color.parseColor("#ffffff");
        data.add(item1);

        LuckyItem item2 = new LuckyItem();
        item2.topText = "50";
        item2.secondaryText = "Coins";
        item2.color = Color.parseColor("#ffffff");
        item2.textColor = Color.parseColor("#000000");
        data.add(item2);

        LuckyItem item = new LuckyItem();
        item.topText = "20";
        item.secondaryText = "Coins";
        item.color = Color.parseColor("#05346c");
        item.textColor = Color.parseColor("#ffffff");
        data.add(item);

        LuckyItem item3 = new LuckyItem();
        item3.topText = "10";
        item3.secondaryText = "Coins";
        item3.color = Color.parseColor("#ffffff");
        item3.textColor = Color.parseColor("#000000");
        data.add(item3);

        LuckyItem item4 = new LuckyItem();
        item4.topText = "40";
        item4.secondaryText = "Coins";
        item4.color = Color.parseColor("#b0481e");
        item4.textColor = Color.parseColor("#ffffff");
        data.add(item4);

        LuckyItem item5 = new LuckyItem();
        item5.topText = "30";
        item5.secondaryText = "Coins";
        item5.color = Color.parseColor("#ffffff");
        item5.textColor = Color.parseColor("#000000");
        data.add(item5);

        LuckyItem item6 = new LuckyItem();
        item6.topText = "90";
        item6.secondaryText = "Coins";
        item6.color = Color.parseColor("#961eb0");
        item6.textColor = Color.parseColor("#ffffff");
        data.add(item6);

        LuckyItem item7 = new LuckyItem();
        item7.topText = "0";
        item7.secondaryText = "Coins";
        item7.color = Color.parseColor("#ffffff");
        item7.textColor = Color.parseColor("#000000");
        data.add(item7);


        wheelView.setData(data);
        wheelView.setRound(5);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Random r = new Random();
                int randomNumber = r.nextInt(8);
                wheelView.startLuckyWheelWithTargetIndex(randomNumber);


            }
        }, 1500);







        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        wheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCoins(index);
            }
        });

    }

    void updateCoins(int index){
        long coin =0;
        switch(index)
        {
            case 0:
                coin = 70;
                break;

            case 1:
                coin = 50;
                break;

            case 2:
                coin = 20;
                break;

            case 3:
                coin = 10;
                break;

            case 4:
                coin = 40;
                break;

            case 5:
                coin = 30;
                break;

            case 6:
                coin = 90;
                break;

            case 7:
                coin = 0;
                break;
        }

        final long updateCoin = coin;

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(coin)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SpinActivity.this, String.valueOf(updateCoin) + " Coins added.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SpinActivity.this, MainActivity.class);
                intent.putExtra("frag",1);
                startActivity(intent);
                finish();


            }
        });

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SpinActivity.this, MainActivity.class);
        intent.putExtra("frag",1);
        startActivity(intent);
        finish();

    }
}