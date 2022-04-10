package com.sharkBytesLab.nocomusic.Screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.Adapters.HistoryAdapter;
import com.sharkBytesLab.nocomusic.Model.HistoryModel;
import com.sharkBytesLab.nocomusic.databinding.ActivityHistoryScreenBinding;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryScreen extends AppCompatActivity {

    private ActivityHistoryScreenBinding binding;
    private ArrayList<HistoryModel> historyArrayList;
    private HistoryAdapter adapter;
    private FirebaseFirestore firestore;
    private ProgressDialog pd;
    private int frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Fetching Details...");
        pd.show();

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                frag = extras.getInt("fragId");

            }
        }catch (Exception e)
        {
            Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        AnimationDrawable animationDrawable = (AnimationDrawable) binding.historyConstraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        binding.historyRecyclerView.setHasFixedSize(true);
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyArrayList = new ArrayList<HistoryModel>();
        adapter = new HistoryAdapter(HistoryScreen.this, historyArrayList);

        binding.historyRecyclerView.setAdapter(adapter);


        binding.historyArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HistoryScreen.this, WeeklyRewardScreen.class);
                intent.putExtra("previousFrag",frag);
                startActivity(intent);
                finish();

            }
        });


        getHistoryOfReward();




    }

    private void getHistoryOfReward()
    {

        firestore.collection("Payments").whereEqualTo("id", FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    if(pd.isShowing())
                        pd.dismiss();
                    Toast.makeText(HistoryScreen.this, "History Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("History : " , error.getMessage());
                }
                else
                {

                    for(DocumentChange dc : value.getDocumentChanges())
                    {
                        if(dc.getType() == DocumentChange.Type.ADDED)
                        {

                            historyArrayList.add(dc.getDocument().toObject(HistoryModel.class));


                        }



                        adapter.notifyDataSetChanged();
                        if(pd.isShowing())
                            pd.dismiss();

                    }

                    pd.dismiss();
                    Collections.reverse(historyArrayList);




                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(HistoryScreen.this, WeeklyRewardScreen.class);
        intent.putExtra("previousFrag",frag);
        startActivity(intent);
        finish();

    }
}