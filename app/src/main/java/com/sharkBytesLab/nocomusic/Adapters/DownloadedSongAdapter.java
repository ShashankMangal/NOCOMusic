package com.sharkBytesLab.nocomusic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharkBytesLab.nocomusic.Model.MusicModel;
import com.sharkBytesLab.nocomusic.MyMediaPlayer;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.Screens.MusicPlayerScreen;

import java.util.ArrayList;

public class DownloadedSongAdapter extends RecyclerView.Adapter<DownloadedSongAdapter.ViewHolder> {

    ArrayList<MusicModel> songsList;
    Context context;

    public DownloadedSongAdapter(ArrayList<MusicModel> songsList, Context context) {

        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_device_list, parent, false);
        return new DownloadedSongAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MusicModel songData = songsList.get(position);
        holder.title.setText(songData.getTitle());
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);

        if(MyMediaPlayer.currentIndex==position)
        {
            holder.title.setTextColor(Color.parseColor("#F80404"));
            holder.icon.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }
        else
        {
            holder.title.setTextColor(Color.parseColor("#001729"));
            holder.icon.setImageResource(R.drawable.musicicon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerScreen.class);
                intent.putExtra("list", songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textSong);
            icon = itemView.findViewById(R.id.ncsLogoInSongList);

        }
    }
}
