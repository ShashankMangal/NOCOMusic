package com.sharkBytesLab.nocomusic.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.Adapters.PromotionListAdapter;
import com.sharkBytesLab.nocomusic.Model.PromotionModel;
import com.sharkBytesLab.nocomusic.Model.PromotionWebViewModel;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.Screens.PromotionFormScreen;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PromotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;
    private FloatingActionButton fab;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView uniqueId;
    private Button subscribeButton;
    private RecyclerView promotionRecyclerView;
    private ArrayList<PromotionModel> promotionArrayList;
    private PromotionListAdapter adapter;
    private FirebaseFirestore firestore;
    private  ProgressDialog progressDialog;
    private PromotionWebViewModel promoModel;
    private TextView promotionByTv;
    private String promoUrl;
    private TextView channelNameTv, promotion_duration;
    private ImageView youtube_logo;


    public PromotionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromotionFragment newInstance(String param1, String param2) {
        PromotionFragment fragment = new PromotionFragment();
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
        View view =  inflater.inflate(R.layout.fragment_promotion, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Promotion Data...");
        //progressDialog.show();


        constraintLayout = view.findViewById(R.id.fragment_promotion_constraint);
        fab = view.findViewById(R.id.home_fab);

        uniqueId = view.findViewById(R.id.uniqueId);
        subscribeButton = view.findViewById(R.id.subscribeButton);
        promotionRecyclerView = view.findViewById(R.id.promotionRecyclerView);
        promotionByTv = view.findViewById(R.id.promotionByTv);
        channelNameTv = view.findViewById(R.id.channelNameTv);
        youtube_logo = view.findViewById(R.id.youtube_logo);
        promotion_duration = view.findViewById(R.id.promotion_duration);


        promotionRecyclerView.setHasFixedSize(true);
        promotionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        try {
            promotionWebViewData();


            Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab.getContext(),  R.anim.shake);
            anim.setDuration(200L);
            fab.startAnimation(anim);

            promotionArrayList = new ArrayList<PromotionModel>();
            adapter = new PromotionListAdapter(getActivity(), promotionArrayList);

            promotionRecyclerView.setAdapter(adapter);

            AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
            animationDrawable.setEnterFadeDuration(1500);
            animationDrawable.setExitFadeDuration(2000);
            animationDrawable.start();

            promotionListUpdater();


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), PromotionFormScreen.class);
                    intent.putExtra("uniqueId",user.getUid().toString());
                    startActivity(intent);

                }
            });


            uniqueId.setText("Your promoId :  "+user.getUid().toString());

            uniqueId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setClipboard(getContext(), user.getUid().toString());

                }
            });

            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(promoUrl));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.google.android.youtube");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            youtube_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getContext(), "Subscribe to this channel.", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(view.getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void  promotionListUpdater()
    {

        Runnable objRunnable3 = new Runnable() {
            @Override
            public void run() {
                try {


        firestore.collection("PromotionList").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){
                            Log.d("Firestore error: ",error.getMessage());
                            Toast.makeText(getActivity(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                promotionArrayList.add(dc.getDocument().toObject(PromotionModel.class));
                            }

                            adapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }

                        }


                    }
                });

    } catch (Exception e) {
        e.printStackTrace();
    }
}
        };
                Thread objBgThread = new Thread(objRunnable3);
                objBgThread.start();


    }

    private void promotionWebViewData()
    {
        firestore.collection("Promotion").document("promotion").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                promoModel = documentSnapshot.toObject(PromotionWebViewModel.class);
                promotionByTv.setText(String.valueOf("Promotion raised by : " + promoModel.getPromotionBy()));
                promoUrl = promoModel.getChannelUrl();
                channelNameTv.setText(promoModel.getChannelName());
                channelNameTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                channelNameTv.setSelected(true);
                promotion_duration.setText(promoModel.getStartDate() + " to " + promoModel.getEndDate());

            }
        });
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(getContext(), "PromoId Copied.", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Refer Code Copied.", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "PromoId Copied.", Toast.LENGTH_SHORT).show();
        }
    }



}