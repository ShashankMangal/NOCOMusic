package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.Adapters.SliderAdapter;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.databinding.ActivityDownloadSongScreenBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DownloadSongScreen extends AppCompatActivity {

    private ActivityDownloadSongScreenBinding binding;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private String fileExtention = ".mp3";
    private String songTitle ;
    private String songImage ;
    private String songLink ;
    private String songId ;
    private int[] images = {
            R.drawable.newone,
            R.drawable.newtwo,
            R.drawable.newthree,
            R.drawable.newfour,
            R.drawable.newfive,
            R.drawable.newsix
    };

    private Boolean reward;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String videoLink;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadSongScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



                   binding.downloadImage.setEnabled(false);
                   binding.downloadImage.setAlpha(0.3F);
                   long dur = TimeUnit.SECONDS.toMillis(41);

                   new CountDownTimer(dur, 1000){

                       @Override
                       public void onTick(long l) {

                           String sDuration = String.format(Locale.ENGLISH, "%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(l), TimeUnit.MILLISECONDS.toSeconds(l)-
                                   TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                           binding.timerDownloadSong.setText("Preparing File : " + sDuration.toString());


                       }

                       @Override
                       public void onFinish() {

                           binding.timerDownloadSong.setVisibility(View.GONE);
                           binding.skipTimer.setVisibility(View.GONE);
                           binding.downloadImage.setEnabled(true);
                           binding.downloadImage.setAlpha(1.0F);

                       }
                   }.start();





        reward = false;
//        binding.applovinAd.loadAd();
//        interstitialAd = new MaxInterstitialAd( "e691b303b405b300", DownloadSongScreen.this );
//        interstitialAd.setListener( DownloadSongScreen.this );
//        interstitialAd.loadAd();
        binding.skipTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.timerDownloadSong.setVisibility(View.GONE);
                binding.skipTimer.setVisibility(View.GONE);
                binding.downloadImage.setEnabled(true);
                binding.downloadImage.setAlpha(1.0F);

//                Toast.makeText(DownloadSongScreen.this, "Loading Ads to Skip Timer.", Toast.LENGTH_SHORT).show();
//
//                if ( interstitialAd.isReady() )
//                {
//                    interstitialAd.showAd();
//                }

            }
        });



                   AnimationDrawable animationDrawable = (AnimationDrawable) binding.downloadSongScreenAnimation.getBackground();
                   animationDrawable.setEnterFadeDuration(1500);
                   animationDrawable.setExitFadeDuration(2000);
                   animationDrawable.start();



                   auth = FirebaseAuth.getInstance();
                   user = auth.getCurrentUser();
                   firestore = FirebaseFirestore.getInstance();

                   try {
                       Bundle extras = getIntent().getExtras();
                       if (extras != null) {
                           songTitle = extras.getString("title");
                           songImage = extras.getString("image");
                           songLink = extras.getString("link");
                           songId = extras.getString("videoId");

                       }



                   } catch (Exception e) {
                       e.printStackTrace();
                   }



                   SliderAdapter sliderAdapter = new SliderAdapter(images);
                   binding.sliderView.setSliderAdapter(sliderAdapter);
                   binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                   binding.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                   binding.sliderView.startAutoCycle();

                   binding.songNameFlowingText.setText(songTitle);
                   binding.songNameFlowingText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                   binding.songNameFlowingText.setSelected(true);


                   binding.playSong.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           try {
//                       getLifecycle().addObserver(binding.youtubePlayerView);
//
//                       binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                           @Override
//                           public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//
//                               youTubePlayer.loadVideo(songId, 0);
//                               youTubePlayer.pause();
//                           }
//                       });
                               videoLink = "https://youtu.be/" + songId;

                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               intent.setPackage("com.google.android.youtube");
                               startActivity(intent);


                           } catch (Exception e) {
                               Log.d("Youtube Player Error : ",e.getMessage());
                               Toast.makeText(DownloadSongScreen.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                           }

                       }
                   });







                   binding.downloadImage.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {


                           try {

                               if(!reward)
                               {
                                   updateCoinsOnDownloadSong(user.getUid().toString());
                                   downloadCount();
                                   reward = true;

                               }


                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songLink));
                               startActivity(intent);

                           } catch (Exception e) {
                               Log.d("Mega WebView Error : ",e.getMessage());
                           }


                       }
                   });

                   


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DownloadSongScreen.this, MainActivity.class);
        intent.putExtra("frag",1);
        startActivity(intent);
        finish();

    }

    private void updateCoinsOnDownloadSong(String userId)
    {

        firestore.collection("Users").document(userId).update("coins", FieldValue.increment(5)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void downloadCount()
    {

        firestore.collection("DownloadCount").document("count").update("num", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();





    }
//
//    @Override
//    public void onAdLoaded(MaxAd ad) {
//
//        Toast.makeText(this, "You can Skip the Timer.", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onAdDisplayed(MaxAd ad) {
//
//        binding.timerDownloadSong.setVisibility(View.GONE);
//        binding.skipTimer.setVisibility(View.GONE);
//        binding.downloadImage.setEnabled(true);
//        binding.downloadImage.setAlpha(1.0F);
//
//    }
//
//    @Override
//    public void onAdHidden(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdClicked(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdLoadFailed(String adUnitId, MaxError error) {
//
//    }
//
//    @Override
//    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//
//    }
}



