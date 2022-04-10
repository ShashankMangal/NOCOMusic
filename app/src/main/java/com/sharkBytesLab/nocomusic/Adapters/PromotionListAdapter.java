package com.sharkBytesLab.nocomusic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sharkBytesLab.nocomusic.Model.PromotionModel;
import com.sharkBytesLab.nocomusic.R;

import java.util.ArrayList;

public class PromotionListAdapter extends RecyclerView.Adapter<PromotionListAdapter.MyViewHolder> {


    Context context;
    ArrayList<PromotionModel> promotionModelArrayList;

    public PromotionListAdapter(Context context, ArrayList<PromotionModel> promotionModelArrayList) {

        this.context = context;
        this.promotionModelArrayList = promotionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.promotion_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PromotionModel model = promotionModelArrayList.get(position);

        holder.channelName.setText("Channel Name : "+model.getChannelName());
        holder.uniqueId.setText("PromoId : "+model.getUserUniqueId());
        holder.addingDate.setText("Status : " + model.getStatus());
        holder.channel_url.setText("URL : " + model.getChannelUrl());

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_2));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context, "Channel Url Copied.", Toast.LENGTH_SHORT).show();
                setClipboard(context, model.getChannelUrl().toString());
                return true;
            }
        });





    }

    @Override
    public int getItemCount() {
        return promotionModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView  channelName, uniqueId , addingDate, channel_url;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            channelName = itemView.findViewById(R.id.channelNameItem);
            uniqueId = itemView.findViewById(R.id.uniqueIdItem);
            addingDate = itemView.findViewById(R.id.addingDateItem);
            cardView = itemView.findViewById(R.id.promotionListCardView);
            channel_url = itemView.findViewById(R.id.channel_url);


        }
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied.", text);
            clipboard.setPrimaryClip(clip);

        }
    }


}
