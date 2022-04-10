package com.sharkBytesLab.nocomusic.Screens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sharkBytesLab.nocomusic.R;

public class newlayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlayout);



//
//
//
//        package com.sharkBytesLab.rewardapp.Fragment;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.drawable.AnimationDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.MaxReward;
//import com.applovin.mediation.MaxRewardedAdListener;
//import com.applovin.mediation.ads.MaxRewardedAd;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.sharkBytesLab.rewardapp.Adapters.CommunityAdapter;
//import com.sharkBytesLab.rewardapp.Adapters.SliderAdapter;
//import com.sharkBytesLab.rewardapp.MainActivity;
//import com.sharkBytesLab.rewardapp.Model.CommunityModel;
//import com.sharkBytesLab.rewardapp.R;
//import com.sharkBytesLab.rewardapp.Screens.AppStatsScreen;
//import com.sharkBytesLab.rewardapp.Screens.DonationScreen;
//import com.sharkBytesLab.rewardapp.Screens.PlayMusicOnDeviceScreen;
//import com.sharkBytesLab.rewardapp.Screens.WallpaperScreen;
//import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
//import com.smarteist.autoimageslider.SliderAnimations;
//import com.smarteist.autoimageslider.SliderView;
//
//import java.util.ArrayList;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CommunityFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//        public class CommunityFragment extends Fragment implements MaxRewardedAdListener {
//
//            // TODO: Rename parameter arguments, choose names that match
//            // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//            private static final String ARG_PARAM1 = "param1";
//            private static final String ARG_PARAM2 = "param2";
//
//            // TODO: Rename and change types of parameters
//            private String mParam1;
//            private String mParam2;
//
//            private ConstraintLayout constraintLayout;
//            private Button telegramChat, subscribe_yt;
//            private RecyclerView communityRecyclerView;
//            private FirebaseFirestore firestore;
//            private ArrayList<CommunityModel> communityList;
//            private CommunityAdapter adapter;
//            private ProgressDialog progressDialog;
//            private CardView appStats, downloadPlaySongs, wallpaperSet, coinsReward, sponsorBtn;
//            private SliderView sliderViewSponsor;
//            private MaxRewardedAd rewardedAd;
//
//            private int[] images = {
//
//                    R.drawable.latestrateussponsor,
//                    R.drawable.latestsponsor
//            };
//
//            public CommunityFragment() {
//                // Required empty public constructor
//            }
//
//            /**
//             * Use this factory method to create a new instance of
//             * this fragment using the provided parameters.
//             *
//             * @param param1 Parameter 1.
//             * @param param2 Parameter 2.
//             * @return A new instance of fragment CommunityFragment.
//             */
//            // TODO: Rename and change types and number of parameters
//            public static com.sharkBytesLab.rewardapp.Fragment.CommunityFragment newInstance(String param1, String param2) {
//                com.sharkBytesLab.rewardapp.Fragment.CommunityFragment fragment = new com.sharkBytesLab.rewardapp.Fragment.CommunityFragment();
//                Bundle args = new Bundle();
//                args.putString(ARG_PARAM1, param1);
//                args.putString(ARG_PARAM2, param2);
//                fragment.setArguments(args);
//                return fragment;
//            }
//
//            @Override
//            public void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                if (getArguments() != null) {
//                    mParam1 = getArguments().getString(ARG_PARAM1);
//                    mParam2 = getArguments().getString(ARG_PARAM2);
//                }
//            }
//
//            @Override
//            public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                     Bundle savedInstanceState) {
//                // Inflate the layout for this fragment
//                View view =  inflater.inflate(R.layout.fragment_community, container, false);
//
//
//
//
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setCancelable(false);
//                progressDialog.setMessage("Fetching Community Data...");
//                progressDialog.show();
//
//                constraintLayout = view.findViewById(R.id.fragment_community_constraint);
//                telegramChat = view.findViewById(R.id.telegramChat);
//                subscribe_yt = view.findViewById(R.id.subscribe_yt);
//                sponsorBtn = view.findViewById(R.id.sponsorBtn);
//                communityRecyclerView = view.findViewById(R.id.communityRecyclerView);
//                appStats = view.findViewById(R.id.appStats);
//                downloadPlaySongs = view.findViewById(R.id.downloadPlaySongs);
//                wallpaperSet = view.findViewById(R.id.wallpaperSet);
//                sliderViewSponsor = view.findViewById(R.id.sliderViewSponsor);
//                coinsReward = view.findViewById(R.id.coinsReward);
//
//
//                firestore = FirebaseFirestore.getInstance();
//
//                rewardedAd = MaxRewardedAd.getInstance( "a76ff834b20ec1fe", getActivity() );
//                rewardedAd.setListener(this);
//
//                rewardedAd.loadAd();
//
//                AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
//                animationDrawable.setEnterFadeDuration(1500);
//                animationDrawable.setExitFadeDuration(2000);
//                animationDrawable.start();
//
//                communityRecyclerView.setHasFixedSize(true);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
////        communityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                communityRecyclerView.setLayoutManager(layoutManager);
//
//                communityList = new ArrayList<CommunityModel>();
//                adapter = new CommunityAdapter(getContext(), communityList);
//
//                communityRecyclerView.setAdapter(adapter);
//                communityListUpdater();
//
//                appStats.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        startActivity(new Intent(view.getContext(), AppStatsScreen.class));
//
//
//                    }
//                });
//
//                SliderAdapter sliderAdapter = new SliderAdapter(images);
//                sliderViewSponsor.setSliderAdapter(sliderAdapter);
//                sliderViewSponsor.setIndicatorAnimation(IndicatorAnimationType.WORM);
//                sliderViewSponsor.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//                sliderViewSponsor.startAutoCycle();
//
//
//
//                coinsReward.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if ( rewardedAd.isReady() )
//                        {
//                            rewardedAd.showAd();
//                        }else
//                        {
//                            Toast.makeText(getActivity(), "Ads not ready, Try again.", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//
//
//                sponsorBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        startActivity(new Intent(getActivity(), DonationScreen.class));
//                        getActivity().finish();
//
//                    }
//                });
//
//
//                downloadPlaySongs.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent in = new Intent(view.getContext(), PlayMusicOnDeviceScreen.class);
//                        startActivity(in);
//
//                    }
//                });
//
//                wallpaperSet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent in = new Intent(view.getContext(), WallpaperScreen.class);
//                        startActivity(in);
//
//                    }
//                });
//
//
//
//                telegramChat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/NCSHindiChatStation"));
//                        startActivity(intent);
//
//                    }
//                });
//
//                subscribe_yt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/c/NoCopyrightSongsHindi"));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setPackage("com.google.android.youtube");
//                        startActivity(intent);
//
//                    }
//                });
//
//
//                return view;
//            }
//
//
//            private void  communityListUpdater()
//            {
//
//                Runnable objRunnable2 = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//
//                            firestore.collection("Community").orderBy("n", Query.Direction.DESCENDING).
//                                    addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                                            if(error!=null){
//                                                Log.d("Firestore error: ",error.getMessage());
//                                                Toast.makeText(getActivity(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                                                if(progressDialog.isShowing())
//                                                {
//                                                    progressDialog.dismiss();
//                                                }
//                                                return;
//                                            }
//
//                                            for(DocumentChange dc : value.getDocumentChanges())
//                                            {
//                                                if(dc.getType() == DocumentChange.Type.ADDED)
//                                                {
//                                                    communityList.add(dc.getDocument().toObject(CommunityModel.class));
//                                                }
//
//                                                adapter.notifyDataSetChanged();
//                                                if(progressDialog.isShowing())
//                                                {
//                                                    progressDialog.dismiss();
//                                                }
//
//                                            }
//
//
//                                        }
//                                    });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                Thread objBgThread = new Thread(objRunnable2);
//                objBgThread.start();
//            }
//
//            @Override
//            public void onRewardedVideoStarted(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onRewardedVideoCompleted(MaxAd ad) {
//
//                rewardUser();
//            }
//
//            @Override
//            public void onUserRewarded(MaxAd ad, MaxReward reward) {
//
//
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra("frag",2);
//                startActivity(intent);
//
//
//            }
//
//            private void rewardUser() {
//
//                FirebaseFirestore database = FirebaseFirestore.getInstance();
//                long coin = 50;
//                database.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(coin)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getActivity(), "50 Coins added.", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onAdLoaded(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onAdDisplayed(MaxAd ad) {
//
//
//
//            }
//
//            @Override
//            public void onAdHidden(MaxAd ad) {
//                rewardedAd.loadAd();
//            }
//
//            @Override
//            public void onAdClicked(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onAdLoadFailed(String adUnitId, MaxError error) {
//                rewardedAd.loadAd();
//            }
//
//            @Override
//            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//                rewardedAd.loadAd();
//            }
//        }
//



    }
}