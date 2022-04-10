package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.databinding.ActivityOffersScreenBinding;

public class OffersScreen extends AppCompatActivity implements MaxRewardedAdListener{

    private ActivityOffersScreenBinding binding;
    private MaxRewardedAd rewardedAd;
    private boolean clicked;
//    private RewardedAd mRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOffersScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        IronSource.init(this, "136ef50f1", IronSource.AD_UNIT.OFFERWALL);
//        rewardedAd = MaxRewardedAd.getInstance( "4ab29f581c9c42ef", OffersScreen.this );
//        rewardedAd.setListener((MaxRewardedAdListener) this);
//
//        rewardedAd.loadAd();
//
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        RewardedAd.load(this, "ca-app-pub-5127713321341585/5624789207",
//                adRequest, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d("Ad Error:", loadAdError.getMessage());
//                        mRewardedAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        Toast.makeText(OffersScreen.this, "Mega Offer 2 is Ready..", Toast.LENGTH_SHORT).show();
//                        Log.d("Ad Error:", "Ad was loaded.");
//                    }
//                });
//
//
//        IronSource.setOfferwallListener(new OfferwallListener() {
//            @Override
//            public void onOfferwallAvailable(boolean isAvailable) {
//
//                if(isAvailable)
//                {
//                    Toast.makeText(OffersScreen.this, "Offer Wall is ready .", Toast.LENGTH_SHORT).show();
//                    binding.offersButton.setClickable(true);
//                }
//
//                else
//                {
//                    Toast.makeText(OffersScreen.this, "Offer Wall no ready .", Toast.LENGTH_SHORT).show();
//                    binding.offersButton.setClickable(false);
//                }
//
//
//            }
//            /**
//             * Invoked when the Offerwall successfully loads for the user, after calling the 'showOfferwall' method
//             */
//            @Override
//            public void onOfferwallOpened() {
//            }
//            /**
//             * Invoked when the method 'showOfferWall' is called and the OfferWall fails to load.
//             * @param error - A IronSourceError Object which represents the reason of 'showOfferwall' failure.
//             */
//            @Override
//            public void onOfferwallShowFailed(IronSourceError error) {
//            }
//            /**
//             * Invoked each time the user completes an Offer.
//             * Award the user with the credit amount corresponding to the value of the *‘credits’ parameter.
//             * @param credits - The number of credits the user has earned.
//             * @param totalCredits - The total number of credits ever earned by the user.
//             * @param totalCreditsFlag - In some cases, we won’t be able to provide the exact
//             * amount of credits since the last event (specifically if the user clears
//             * the app’s data). In this case the ‘credits’ will be equal to the ‘totalCredits’, and this flag will be ‘true’.
//             * @return boolean - true if you received the callback and rewarded the user, otherwise false.
//             */
//            @Override
//            public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
//                rewardUser(credits);
//                Toast.makeText(OffersScreen.this, String.valueOf(credits) + " added into your account.", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            /**
//             * Invoked when the method 'getOfferWallCredits' fails to retrieve
//             * the user's credit balance info.
//             * @param error - A IronSourceError object which represents the reason of 'getOfferwallCredits' failure.
//             * If using client-side callbacks to reward users, it is mandatory to return true on this event
//             */
//            @Override
//            public void onGetOfferwallCreditsFailed(IronSourceError error) {
//            }
//            /**
//             * Invoked when the user is about to return to the application after closing
//             * the Offerwall.
//             */
//            @Override
//            public void onOfferwallClosed() {
//            }
//        });
//
//
//        binding.offersButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                IronSource.showOfferwall("DefaultOfferWall");
//
//            }
//        });
//
//        binding.megaCoinAdmobButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (mRewardedAd != null) {
//                    Activity activityContext = OffersScreen.this;
//                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
//                        @Override
//                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                            // Handle the reward.
//                            Log.d("Coins:", "The user earned the reward.");
//                            int rewardAmount = rewardItem.getAmount();
//                            String rewardType = rewardItem.getType();
//                            rewardUser(rewardAmount);
//                        }
//                    });
//                } else {
//                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
//                    Toast.makeText(OffersScreen.this, "Ads not ready ,Try again later.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//
//
//        binding.offersArrowBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(OffersScreen.this, MainActivity.class);
//                intent.putExtra("frag",2);
//                startActivity(intent);
//                finish();
//
//            }
//        });
//
//
//        binding.megaCoinButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if ( rewardedAd.isReady() )
//                {
//                    rewardedAd.showAd();
//                }else
//                {
//                    Toast.makeText(OffersScreen.this, "Ads not ready, Try again.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//


    }

    protected void onResume() {
        super.onResume();
//        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
//        IronSource.onPause(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OffersScreen.this, MainActivity.class);
        intent.putExtra("frag",2);
        startActivity(intent);
        finish();


    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

        Toast.makeText(this, "Mega Offer 1 is Ready..", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
        clicked = false;
        Toast.makeText(this, "Install this app from playstore", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdHidden(MaxAd ad) {
        rewardedAd.loadAd();
    }

    @Override
    public void onAdClicked(MaxAd ad) {


        if(!clicked)
        {
            Toast.makeText(OffersScreen.this, "Install this app.", Toast.LENGTH_SHORT).show();

            clicked = true;

            rewardUser(500);
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

    private void rewardUser(int coin) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(coin)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(OffersScreen.this, String.valueOf(coin)+" Coins added.", Toast.LENGTH_SHORT).show();

            }
        });

    }



}