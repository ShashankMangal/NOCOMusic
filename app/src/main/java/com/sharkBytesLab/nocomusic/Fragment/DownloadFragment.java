package com.sharkBytesLab.nocomusic.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sharkBytesLab.nocomusic.Adapters.DownloadSongAdapter;
import com.sharkBytesLab.nocomusic.DateTimeOnline;
import com.sharkBytesLab.nocomusic.Model.DownloadModel;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.SpinGift.SpinActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<DownloadModel> list;
    private DownloadSongAdapter adapter;
    private SearchView searchView;
    private  ProgressDialog pd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton spin_fab;
    private String todaysDate = null;

    public String getTodaysDate() {
        return todaysDate;
    }

    public void setTodaysDate(String todaysDate) {
        this.todaysDate = todaysDate;
    }

    private SharedPreferences sharedPreferences;




    public DownloadFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
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
        View view =  inflater.inflate(R.layout.fragment_download, container, false);



        recyclerView = view.findViewById(R.id.downloadRecyclerView);
        searchView = view.findViewById(R.id.search_song);
        swipeRefreshLayout = view.findViewById(R.id.refreshView);
        constraintLayout = view.findViewById(R.id.fragment_download_constraint);
        spin_fab = view.findViewById(R.id.spin_fab);








        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        pd = new ProgressDialog(getActivity());
        pd.show();
        pd.setContentView(R.layout.progress_dialog);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Thread timer = new Thread()
        {
            @Override
            public void run() {
                try{
                    sleep(3000);

                    pd.dismiss();


                    super.run();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        };
        timer.start();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();


        clearAll();


        DateTimeOnline dateTimeOnline = new DateTimeOnline(getActivity());
        dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
            @Override
            public void onGetDateTime(String date, String time) {

                Log.e("Date",date);
                Log.e("Time",time);
                try {
                    spinWheel(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });

        spin_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DateTimeOnline dateTimeOnline = new DateTimeOnline(getActivity());
                dateTimeOnline.getDateTime(new DateTimeOnline.VolleyCallBack() {
                    @Override
                    public void onGetDateTime(String date, String time) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(date, true);
                        editor.apply();


                    }
                });




                    startActivity(new Intent(getActivity(), SpinActivity.class));
                    getActivity().finish();






            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getDataFromFirebase();


                } catch (Exception e) {
                    e.printStackTrace();

                }


                swipeRefreshLayout.setRefreshing(false);

            }

        });


        //fetch data and save in objects
        try {
            getDataFromFirebase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error on getting songs list.", Toast.LENGTH_SHORT).show();
        }



        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            getDataFromFirebase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error on getting songs list.", Toast.LENGTH_SHORT).show();
        }

    }

    public void getDataFromFirebase()
    {

        Runnable objRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    Query query = myRef.child("DownloadData").orderByChild("serial");
                    query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    clearAll();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    DownloadModel model = new DownloadModel();
                    model.setImage(snapshot1.child("image").getValue().toString());
                    model.setTitle(snapshot1.child("title").getValue().toString());
                    model.setLink(snapshot1.child("link").getValue().toString());
                    model.setVideoId(snapshot1.child("videoId").getValue().toString());
                    list.add(model);
                }

                adapter = new DownloadSongAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread objBgThread = new Thread(objRunnable);
        objBgThread.start();


    }

    private void processSearch(String s)
    {
        Query query = myRef.child("DownloadData");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();

                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    DownloadModel model = new DownloadModel();

                    if (snapshot1.child("title").getValue().toString().toLowerCase().contains(s.toLowerCase())) {

                        model.setImage(snapshot1.child("image").getValue().toString());
                        model.setTitle(snapshot1.child("title").getValue().toString());
                        model.setLink(snapshot1.child("link").getValue().toString());
                        model.setVideoId(snapshot1.child("videoId").getValue().toString());
                        list.add(model);


                    }
                }

                adapter = new DownloadSongAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAll()
    {
        if(list != null)
        {
            list.clear();

            if(adapter != null)
            {
                adapter.notifyDataSetChanged();
            }

        }

        list = new ArrayList<>();
    }

    private void spinWheel(String todaysD)
    {
        sharedPreferences = getActivity().getSharedPreferences("SPIN", Context.MODE_PRIVATE);
        boolean currentDay = sharedPreferences.getBoolean(todaysD, false);

        if(!currentDay)
        {

            spin_fab.setEnabled(true);
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(spin_fab.getContext(),  R.anim.shake);
            anim.setDuration(200L);
            spin_fab.startAnimation(anim);
        }
        else
        {

            spin_fab.setEnabled(false);


        }
    }


}