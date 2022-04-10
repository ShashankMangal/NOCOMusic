package com.sharkBytesLab.nocomusic.Fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.Screens.AppStatsScreen;
import com.sharkBytesLab.nocomusic.Screens.DonationScreen;
import com.sharkBytesLab.nocomusic.Screens.PlayMusicOnDeviceScreen;
import com.sharkBytesLab.nocomusic.Screens.WallpaperScreen;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Button telegramChat, subscribe_yt;
    private CardView appStats, downloadPlaySongs, wallpaperSet, coinsReward, sponsorBtn, offerWall;
    private ConstraintLayout constraintLayout;
    private BottomSheetDialog underDevelopmentSheetDialog;


    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_community, container, false);



        telegramChat = view.findViewById(R.id.telegramChat);
        subscribe_yt = view.findViewById(R.id.subscribe_yt);
        sponsorBtn = view.findViewById(R.id.sponsorBtn);
        appStats = view.findViewById(R.id.appStats);
        offerWall = view.findViewById(R.id.offerWall);
        downloadPlaySongs = view.findViewById(R.id.downloadPlaySongs);
        wallpaperSet = view.findViewById(R.id.wallpaperSet);
        coinsReward = view.findViewById(R.id.coinsReward);
        constraintLayout = view.findViewById(R.id.fragment_community_const);



        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

//        rewardedAd = MaxRewardedAd.getInstance( "a76ff834b20ec1fe", getActivity() );
//        rewardedAd.setListener(this);
//
//        rewardedAd.loadAd();


        appStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), AppStatsScreen.class));


            }
        });

        offerWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(view.getContext(), OffersScreen.class));

                underDevelopmentSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);

                View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_in_development, getActivity().findViewById(R.id.sheetViewLayoutInDevelopment));

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




        coinsReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if ( rewardedAd.isReady() )
//                {
//                    rewardedAd.showAd();
//                }else
//                {
//                    Toast.makeText(getActivity(), "Ads not ready, Try again.", Toast.LENGTH_SHORT).show();
//                }

                underDevelopmentSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);

                View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_in_development, getActivity().findViewById(R.id.sheetViewLayoutInDevelopment));

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



        sponsorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), DonationScreen.class));
                getActivity().finish();

            }
        });


        downloadPlaySongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(view.getContext(), PlayMusicOnDeviceScreen.class);
                startActivity(in);

            }
        });

        wallpaperSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(view.getContext(), WallpaperScreen.class);
                startActivity(in);

            }
        });



        telegramChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/NCSHindiChatStation"));
                startActivity(intent);

            }
        });

        subscribe_yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/c/NoCopyrightSongsHindi"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

            }
        });


        return view;
    }



//
//    @Override
//    public void onRewardedVideoStarted(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onRewardedVideoCompleted(MaxAd ad) {
//
//        rewardUser();
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        intent.putExtra("frag",2);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onUserRewarded(MaxAd ad, MaxReward reward) {
//
//
//    }

    private void rewardUser() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        long coin = 50;
        database.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(coin)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "50 Coins added.", Toast.LENGTH_SHORT).show();

                }
        });

    }

//    @Override
//    public void onAdLoaded(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdDisplayed(MaxAd ad) {
//
//
//
//    }
//
//    @Override
//    public void onAdHidden(MaxAd ad) {
//        rewardedAd.loadAd();
//    }
//
//    @Override
//    public void onAdClicked(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdLoadFailed(String adUnitId, MaxError error) {
//        rewardedAd.loadAd();
//    }
//
//    @Override
//    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//        rewardedAd.loadAd();
//    }
}