package com.sharkBytesLab.nocomusic.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.DateTimeOnline;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.Model.ProfileModel;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.databinding.ActivityWeeklyRewardScreenBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class WeeklyRewardScreen extends AppCompatActivity implements MaxRewardedAdListener{

    private ActivityWeeklyRewardScreenBinding binding;
    private int fragId;
    private int sendFragId;
    private BottomSheetDialog bottomSheetDialog, bottomSheetDialogGold, bottomSheetDialogLucky;
    private FirebaseFirestore firestore;
    private ProfileModel profileModel;
    private SharedPreferences sharedPreferences;
    private EditText paytmWeeklyEditText, paytmGoldEditText, paytmLuckyEditText;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String Date;
    private MaxRewardedAd rewardedAd;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeeklyRewardScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firestore = FirebaseFirestore.getInstance();
        binding.luckyCardClaim.setClickable(false);
        binding.weeklyCardClaim.setClickable(false);
        binding.goldCardClaim.setClickable(false);

        rewardedAd = MaxRewardedAd.getInstance( "b6416d1ed280d7bf", WeeklyRewardScreen.this );
        rewardedAd.setListener((MaxRewardedAdListener) this);

        rewardedAd.loadAd();


        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileModel = documentSnapshot.toObject(ProfileModel.class);




                if(profileModel.getCoins() >= 1000)
                {
                    binding.giftCard.setAlpha(1f);
                    binding.weeklyCardClaim.setClickable(true);
                }
                else
                {
                    binding.giftCard.setAlpha(0.5f);
                    binding.weeklyCardClaim.setClickable(false);
                }


                if(profileModel.getCoins() >= 5000)
                {
                    binding.goldGiftCard.setAlpha(1f);
                    binding.goldCardClaim.setClickable(true);
                }
                else
                {
                    binding.goldGiftCard.setAlpha(0.5f);
                    binding.goldCardClaim.setClickable(false);
                }


                if(profileModel.getCoins() >= 15000)
                {
                    binding.luckyGiftCard.setAlpha(1f);
                    binding.luckyCardClaim.setClickable(true);
                }
                else
                {
                    binding.luckyGiftCard.setAlpha(0.5f);
                    binding.luckyCardClaim.setClickable(false);
                }


            }
        });

        DateTimeOnline dateTimeOnline = new DateTimeOnline(WeeklyRewardScreen.this);
        dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
            @Override
            public void onGetDateTime(String date, String time) {

                checkDailyLimit(date);


            }
        });



        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                fragId = extras.getInt("previousFrag");

               if(fragId == R.id.downloadSong || fragId == 1)
               {
                   sendFragId = 1;
               }
               else if(fragId == R.id.community || fragId == 2)
               {
                   sendFragId = 2;
               }
               else if(fragId == R.id.promotion || fragId == 3)
               {
                   sendFragId = 3;
               }
               else if(fragId == R.id.profile || fragId == 4)
               {
                   sendFragId = 4;
               }
               else if(fragId == R.id.settings || fragId == 5)
               {
                   sendFragId = 5;
               }


                Log.d("Fragment in Previous: ",String.valueOf(fragId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.redeemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WeeklyRewardScreen.this, HistoryScreen.class);
                intent.putExtra("fragId",sendFragId);
                startActivity(intent);
                finish();

            }
        });


        binding.freeCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( rewardedAd.isReady() )
                {
                    rewardedAd.showAd();
                }else
                {
                    Toast.makeText(WeeklyRewardScreen.this, "Ads not ready, Try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.weeklyCardClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(WeeklyRewardScreen.this, R.style.AppBottomSheetDialogTheme);

                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_weekly_reward_sheet, findViewById(R.id.sheetViewLayout));

                bottomSheetDialog.setContentView(sheetView);

                paytmWeeklyEditText = bottomSheetDialog.findViewById(R.id.paytmWeekly);

                bottomSheetDialog.findViewById(R.id.closeWeeklySheet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.findViewById(R.id.claimWeeklyReward).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(TextUtils.isEmpty(paytmWeeklyEditText.getEditableText().toString()) || paytmWeeklyEditText.getEditableText().length()<10)
                        {
                            paytmWeeklyEditText.requestFocus();
                            paytmWeeklyEditText.setError("Invalid Number");
                        }
                        else
                        {
                            calendar = Calendar.getInstance();
                            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date = simpleDateFormat.format(calendar.getTime());


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("amount",1);
                            map.put("date", Date);
                            map.put("paytm", paytmWeeklyEditText.getEditableText().toString());
                            map.put("detail","Pending");
                            map.put("id",FirebaseAuth.getInstance().getUid().toString());


                            firestore.collection("Payments").document(FirebaseAuth.getInstance().getUid()+Date).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(getApplicationContext(), "Successfully Added." , Toast.LENGTH_SHORT).show();

                                    DateTimeOnline dateTimeOnline = new DateTimeOnline(WeeklyRewardScreen.this);
                                    dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
                                        @Override
                                        public void onGetDateTime(String date, String time) {

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(date, true);
                                            editor.apply();


                                        }
                                    });

                                    updateCoinsAfterRedeem(FirebaseAuth.getInstance().getUid(), 1000);

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }



                    }
                });


                bottomSheetDialog.show();


            }
        });


        binding.goldCardClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogGold = new BottomSheetDialog(WeeklyRewardScreen.this, R.style.AppBottomSheetDialogTheme);

                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_gold_reward_sheet, findViewById(R.id.sheetViewLayoutGold));

                bottomSheetDialogGold.setContentView(sheetView);

                paytmGoldEditText = bottomSheetDialogGold.findViewById(R.id.paytmGold);

                bottomSheetDialogGold.findViewById(R.id.closeGoldSheet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialogGold.dismiss();

                    }
                });


                bottomSheetDialogGold.findViewById(R.id.claimGoldReward).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(TextUtils.isEmpty(paytmGoldEditText.getEditableText().toString()) || paytmGoldEditText.getEditableText().length()<10)
                        {
                            paytmGoldEditText.requestFocus();
                            paytmGoldEditText.setError("Invalid Number");
                        }
                        else
                        {
                            calendar = Calendar.getInstance();
                            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date = simpleDateFormat.format(calendar.getTime());


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("amount",5);
                            map.put("date", Date);
                            map.put("paytm", paytmGoldEditText.getEditableText().toString());
                            map.put("detail","Pending");
                            map.put("id",FirebaseAuth.getInstance().getUid().toString());


                            firestore.collection("Payments").document(FirebaseAuth.getInstance().getUid()+Date).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(getApplicationContext(), "Successfully Added." , Toast.LENGTH_SHORT).show();

                                    DateTimeOnline dateTimeOnline = new DateTimeOnline(WeeklyRewardScreen.this);
                                    dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
                                        @Override
                                        public void onGetDateTime(String date, String time) {

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(date, true);
                                            editor.apply();


                                        }
                                    });

                                    updateCoinsAfterRedeem(FirebaseAuth.getInstance().getUid(), 5000);

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }



                    }
                });





                bottomSheetDialogGold.show();


            }
        });



        binding.luckyCardClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogLucky = new BottomSheetDialog(WeeklyRewardScreen.this, R.style.AppBottomSheetDialogTheme);

                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_lucky_reward_sheet, findViewById(R.id.sheetViewLayoutLucky));

                bottomSheetDialogLucky.setContentView(sheetView);

                paytmLuckyEditText = bottomSheetDialogLucky.findViewById(R.id.paytmLucky);

                bottomSheetDialogLucky.findViewById(R.id.closeLuckySheet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialogLucky.dismiss();

                    }
                });


                bottomSheetDialogLucky.findViewById(R.id.claimLuckyReward).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(TextUtils.isEmpty(paytmLuckyEditText.getEditableText().toString()) || paytmLuckyEditText.getEditableText().length()<10)
                        {
                            paytmLuckyEditText.requestFocus();
                            paytmLuckyEditText.setError("Invalid Number");
                        }
                        else
                        {
                            calendar = Calendar.getInstance();
                            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date = simpleDateFormat.format(calendar.getTime());


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("amount",20);
                            map.put("date", Date);
                            map.put("paytm", paytmLuckyEditText.getEditableText().toString());
                            map.put("detail","Pending");
                            map.put("id",FirebaseAuth.getInstance().getUid().toString());


                            firestore.collection("Payments").document(FirebaseAuth.getInstance().getUid()+Date).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(getApplicationContext(), "Successfully Added." , Toast.LENGTH_SHORT).show();

                                    DateTimeOnline dateTimeOnline = new DateTimeOnline(WeeklyRewardScreen.this);
                                    dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
                                        @Override
                                        public void onGetDateTime(String date, String time) {

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(date, true);
                                            editor.apply();


                                        }
                                    });

                                    updateCoinsAfterRedeem(FirebaseAuth.getInstance().getUid(), 15000);

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }



                    }
                });




                bottomSheetDialogLucky.show();


            }
        });



        binding.backWeeklyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WeeklyRewardScreen.this, MainActivity.class);
                intent.putExtra("frag",sendFragId);
                startActivity(intent);
                finish();

            }
        });




    }


    private void checkDailyLimit(String todaysD)
    {
        sharedPreferences = getSharedPreferences("REWARD", Context.MODE_PRIVATE);
        boolean currentDay = sharedPreferences.getBoolean(todaysD, false);

        if(!currentDay)
        {

            binding.weeklyCardClaim.setClickable(true);
            binding.goldCardClaim.setClickable(true);
            binding.luckyCardClaim.setClickable(true);

        }
        else
        {
            Toast.makeText(this, "Already redeem a card today, come back tomorrow.", Toast.LENGTH_SHORT).show();
            binding.weeklyCardClaim.setClickable(false);
            binding.goldCardClaim.setClickable(false);
            binding.luckyCardClaim.setClickable(false);

        }
    }

    private void updateCoinsAfterRedeem(String userId, int amount)
    {

        firestore.collection("Users").document(userId).update("coins", FieldValue.increment(-amount)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        });

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(WeeklyRewardScreen.this, MainActivity.class);
        intent.putExtra("frag",sendFragId);
        startActivity(intent);
        finish();

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {



    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

        clicked = false;
        Toast.makeText(WeeklyRewardScreen.this, "Visit ad to get coins.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        rewardedAd.loadAd();
    }

    @Override
    public void onAdClicked(MaxAd ad) {

        if(!clicked)
        {
        Toast.makeText(WeeklyRewardScreen.this, "Wait 5 seconds.", Toast.LENGTH_SHORT).show();
        rewardUser();
        clicked = true;
        }
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        rewardedAd.loadAd();
    }

    private void rewardUser() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        long coin = 100;
        database.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(coin)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(WeeklyRewardScreen.this, "100 Coins added.", Toast.LENGTH_SHORT).show();

            }
        });

    }


}