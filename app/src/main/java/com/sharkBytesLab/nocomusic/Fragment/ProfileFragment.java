package com.sharkBytesLab.nocomusic.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkBytesLab.nocomusic.BuildConfig;
import com.sharkBytesLab.nocomusic.Model.ProfileModel;
import com.sharkBytesLab.nocomusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout constraintLayout;
    private FirebaseFirestore firestore;
    private TextView userNameProfile, referCode, versionName;
    private ProfileModel profileModel;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button referBtn;


    public ProfileFragment() {

    }



    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        constraintLayout = view.findViewById(R.id.fragment_profile_constraint);
        userNameProfile = view.findViewById(R.id.userNameProfile);
        referCode = view.findViewById(R.id.referCode);
        referBtn = view.findViewById(R.id.referBtn);
        versionName = view.findViewById(R.id.versionName);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");


        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        getUserNameFromFirebase();

        versionName.setText("v " + String.valueOf(BuildConfig.VERSION_NAME));

        referCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setClipboard(getContext(), referCode.getText().toString());

            }
        });

        referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referC = referCode.getText().toString();
                String shareBody = "Hey, I'm using NCS Hindi songs for my social platforms without any copyright. Join using my invite link and get 50 coins. My invite code is " + referC + " \n" +
                        "Download from Play Store\n" + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(intent);

            }
        });


        return view;
    }




    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(getContext(), "Copied.", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Refer Code Copied.", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserNameFromFirebase()
    {

        Runnable objRunnable4 = new Runnable() {
            @Override
            public void run() {
                try {

        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileModel = documentSnapshot.toObject(ProfileModel.class);
                userNameProfile.setText(String.valueOf(profileModel.getName()));
                referCode.setText(String.valueOf(profileModel.getReferCode()));


            }
        });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread objBgThread = new Thread(objRunnable4);
        objBgThread.start();


    }

}