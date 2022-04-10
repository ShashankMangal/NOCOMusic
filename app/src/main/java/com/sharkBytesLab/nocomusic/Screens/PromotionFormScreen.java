package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.databinding.ActivityPromotionFormScreenBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PromotionFormScreen extends AppCompatActivity {

    private ActivityPromotionFormScreenBinding binding;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String Date;
    private FirebaseFirestore firestore;
    private String uniqueId;
    private int updateCoins;
    private ArrayList<String> ids;


    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "coins";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromotionFormScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uniqueId = extras.getString("uniqueId");
        }

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date = simpleDateFormat.format(calendar.getTime());

        binding.dateCurrent.setText(Date);
        ids= new ArrayList<>();

        loadSharedPref();

        firestore = FirebaseFirestore.getInstance();


        AnimationDrawable animationDrawable = (AnimationDrawable) binding.promotionScreenConstraint.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


        Log.d("Updated coins",String.valueOf(updateCoins));

        if(updateCoins < 100)
        {
            binding.addPromotionButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "You have less coins right now.", Toast.LENGTH_SHORT).show();
            Log.d("clickable","false");
        }
        else
        {
            binding.addPromotionButton.setEnabled(true);
            Log.d("clickable","true");
        }

        binding.addPromotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.userNameText.getText().toString().trim())) {
                    binding.userNameText.setError("User Name Required!");
                    binding.userNameText.requestFocus();
                }
                else  if (TextUtils.isEmpty(binding.channelNameText.getText().toString().trim())) {
                    binding.channelNameText.setError("Channel Name Required!");
                    binding.channelNameText.requestFocus();
                }
                else  if (TextUtils.isEmpty(binding.channelUrlText.getText().toString().trim())) {
                    binding.channelUrlText.setError("Channel URL Required!");
                    binding.channelUrlText.requestFocus();
                }
                else
                {




                    ids.clear();

                    firestore.collection("PromotionList").
                            addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {




                                    if(error!=null){
                                        Log.d("Firestore error: ",error.getMessage());
                                        Toast.makeText(getApplicationContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();

                                        return;
                                    }

                                    for(DocumentChange dc : value.getDocumentChanges())
                                    {
                                        if(dc.getType() == DocumentChange.Type.ADDED)
                                        {

                                            String id = dc.getDocument().getId();
                                            ids.add(id);
                                            Log.d("Id's" ,id);

                                        }



                                    }

                                    if(ids.contains(uniqueId))
                                    {
                                        binding.addPromotionButton.setEnabled(false);
                                        binding.promoWarning.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        binding.promoWarning.setVisibility(View.GONE);
                                        binding.addPromotionButton.setEnabled(true);
                                        String channelName = binding.channelNameText.getText().toString().trim();
                                        String channelUrl = binding.channelUrlText.getText().toString().trim();
                                        String userName = binding.userNameText.getText().toString().trim();
                                        String userUniqueId = uniqueId.toString().trim();
                                        String status = "In Queue";

                                        addPromotion(channelName, channelUrl, userName, userUniqueId, updateCoins, status);
                                    }




                                }




                            });









                }



            }
        });

        String email = "shashankmangal10@gmail.com";
        String subject = "Enquiry regarding NCS Hindi";

        binding.promotionArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PromotionFormScreen.this, MainActivity.class);
                intent.putExtra("frag",3);
                startActivity(intent);
                finish();
            }
        });

        binding.enquiryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                i.putExtra(Intent.EXTRA_SUBJECT,subject);
                i.setData(Uri.parse("mailto:"));
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Type your Message here.", Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void addPromotion(String channelName, String channelUrl, String userName, String userUniqueId, int updatedCoins, String status)
    {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("channelName",channelName);
        map.put("channelUrl",channelUrl);
        map.put("userUniqueId",userUniqueId);
        map.put("updatedCoins",updatedCoins);
        map.put("status",status);
        map.put("addingDate", FieldValue.serverTimestamp());
        map.put("startingDate", "null");


        firestore.collection("PromotionList").document(userUniqueId).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getApplicationContext(), "Promotion added in queue.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PromotionFormScreen.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateCoinsAfterPromo(userUniqueId);

    }

    private void loadSharedPref()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        updateCoins = sharedPreferences.getInt(TEXT, 0);

    }

    private void updateCoinsAfterPromo(String userId)
    {

        firestore.collection("Users").document(userId).update("coins", FieldValue.increment(-100)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PromotionFormScreen.this, MainActivity.class);
        intent.putExtra("frag",3);
        startActivity(intent);
        finish();

    }
}