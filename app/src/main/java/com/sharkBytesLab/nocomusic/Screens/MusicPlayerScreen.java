package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sharkBytesLab.nocomusic.Model.MusicModel;
import com.sharkBytesLab.nocomusic.MyMediaPlayer;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.databinding.ActivityMusicPlayerScreenBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerScreen extends AppCompatActivity {

    private ActivityMusicPlayerScreenBinding binding;
    private ArrayList<MusicModel> songsList;
    private MusicModel currentSong;
    private MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    private int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playingSongName.setSelected(true);

        AnimationDrawable animationDrawable = (AnimationDrawable) binding.playSongLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();



        songsList = (ArrayList<MusicModel>) getIntent().getSerializableExtra("list");

        setResourcesWithMusic();


        MusicPlayerScreen.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null)
                {
                    binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    binding.currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying())
                    {
                        binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        binding.musicIcon.setRotation(x++);
                    }else
                    {
                        binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        binding.musicIcon.setRotation(x);
                    }

                }
                new Handler().postDelayed(this, 100);

            }
        });

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

                if(mediaPlayer!=null && fromUser)
                {
                    mediaPlayer.seekTo(i);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        binding.playMusicMediaPlayerArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), PlayMusicOnDeviceScreen.class));
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), PlayMusicOnDeviceScreen.class));
        finish();

    }

    void setResourcesWithMusic()
    {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        binding.playingSongName.setText(currentSong.getTitle());
        binding.totalTime.setText(convertToMMSS(currentSong.getDuration()));

        binding.pausePlay.setOnClickListener(v-> pausePlay());
        binding.next.setOnClickListener(v-> playNextSong());
        binding.previous.setOnClickListener(v-> playPreviousSong());

        playMusic();

    }

    private void playMusic()
    {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            binding.seekbar.setProgress(0);
            binding.seekbar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            Toast.makeText(this, "Music Player Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void playNextSong()
    {
        if(MyMediaPlayer.currentIndex == songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setResourcesWithMusic();



    }

    private void playPreviousSong()
    {
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    public void pausePlay()
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public void stopMusic()
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public static String convertToMMSS(String duration)
    {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

}