package com.sharkBytesLab.nocomusic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sharkBytesLab.nocomusic.Model.CommunityModel;
import com.sharkBytesLab.nocomusic.R;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {

    Context context;
    ArrayList<CommunityModel> communityArrayList;

    public CommunityAdapter(Context context, ArrayList<CommunityModel> communityArrayList) {
        this.context = context;
        this.communityArrayList = communityArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.community_msg, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CommunityModel model = communityArrayList.get(position);

        holder.msg.setText(model.getChat());

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_2));

    }

    @Override
    public int getItemCount() {
        return communityArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView msg;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            msg = itemView.findViewById(R.id.msg_community);
            cardView = itemView.findViewById(R.id.community_msg_cardView);

        }
    }
}
