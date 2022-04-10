package com.sharkBytesLab.nocomusic.Screens;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sharkBytesLab.nocomusic.Adapters.DownloadedSongAdapter;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.Model.MusicModel;
import com.sharkBytesLab.nocomusic.databinding.ActivityPlayMusicOnDeviceScreenBinding;

import java.io.File;
import java.util.ArrayList;

public class PlayMusicOnDeviceScreen extends AppCompatActivity {

    private ActivityPlayMusicOnDeviceScreenBinding binding;

    private String[] items;
    private ArrayList<MusicModel> songsList = new ArrayList<>();
    private MusicPlayerScreen music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicOnDeviceScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        try {
            music = new MusicPlayerScreen();
            runTimePermission();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.playMusicArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PlayMusicOnDeviceScreen.this, MainActivity.class);
                intent.putExtra("frag",2);
                music.stopMusic();
                startActivity(intent);
                finish();


            }
        });


    }

    private void runTimePermission()
    {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                    {
                            //display();
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();
    }

    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile : files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findSong(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                {
                        arrayList.add(singleFile);
                }

            }

        }

        return arrayList;
    }

    void displaySong()
    {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";


            Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

            while(cursor.moveToNext())
            {
                MusicModel songData = new MusicModel(cursor.getString(1), cursor.getString(0), cursor.getString(2) );
                if(new File(songData.getPath()).exists())
                songsList.add(songData);

            }

            if(songsList.size() == 0)
            {
                binding.noSongText.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.mySongListView.setLayoutManager(new LinearLayoutManager(this));
                binding.mySongListView.setAdapter(new DownloadedSongAdapter(songsList, getApplicationContext()));
            }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(binding.mySongListView!=null)
        {
            binding.mySongListView.setAdapter(new DownloadedSongAdapter(songsList, getApplicationContext()));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PlayMusicOnDeviceScreen.this, MainActivity.class);
        intent.putExtra("frag",2);
        music.stopMusic();
        startActivity(intent);
        finish();



    }
}