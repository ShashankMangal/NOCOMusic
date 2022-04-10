package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.sharkBytesLab.nocomusic.Adapters.WallpaperAdapter;
import com.sharkBytesLab.nocomusic.ApiUtilities;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.Model.ImageModel;
import com.sharkBytesLab.nocomusic.Model.SearchModel;
import com.sharkBytesLab.nocomusic.databinding.ActivityWallpaperScreenBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpaperScreen extends AppCompatActivity {

    private ActivityWallpaperScreenBinding binding;
    private ArrayList<ImageModel> modelClassList;
    private WallpaperAdapter adapter;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWallpaperScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        modelClassList = new ArrayList<>();
        binding.wallpaperRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.wallpaperRecyclerView.setHasFixedSize(true);
        adapter = new WallpaperAdapter(getApplicationContext(), modelClassList);
        binding.wallpaperRecyclerView.setAdapter(adapter);

        findPhotos(1);

        binding.musicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String query = "music";
                getSearchImage(query);

            }


        });

        binding.trendingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pageNum++;
               findPhotos(pageNum);

            }


        });


        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String query = binding.searchWallpaperEditText.getText().toString().trim().toLowerCase();

                if(query.isEmpty())
                {
                    Toast.makeText(WallpaperScreen.this, "Enter Image Name.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getSearchImage(query);
                }

            }
        });



        binding.wallpaperBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WallpaperScreen.this, MainActivity.class);
                intent.putExtra("frag",2);
                startActivity(intent);
                finish();

            }
        });

    }
    private void getSearchImage(String query) {

        ApiUtilities.getApiInterface().getSearchImage(query, 1, 80).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {

                modelClassList.clear();
                if(response.isSuccessful())
                {

                    modelClassList.addAll(response.body().getPhotos());
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(WallpaperScreen.this, "Error Occurred on getting Images.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

            }
        });

    }

    private void findPhotos(int pageNum) {


        ApiUtilities.getApiInterface().getImage(pageNum,80).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {

                modelClassList.clear();
                if(response.isSuccessful())
                {

                    modelClassList.addAll(response.body().getPhotos());
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(WallpaperScreen.this, "Error Occurred on getting Images.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(WallpaperScreen.this, MainActivity.class);
        intent.putExtra("frag",2);
        startActivity(intent);
        finish();
    }
}