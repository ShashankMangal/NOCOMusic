package com.sharkBytesLab.nocomusic.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharkBytesLab.nocomusic.Model.HistoryModel;
import com.sharkBytesLab.nocomusic.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<HistoryModel> historyList;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);


        return new MyViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

        HistoryModel model = historyList.get(position);

        holder.paytmDate.setText(" " + model.getDate() + " ");
        holder.paytmDetail.setText(" Transaction : " + model.getDetail().toString());
        holder.paytmNum.setText(" Paytm : " + model.getPaytm());
        holder.paytmReward.setText(" Paytm Reward :  ₹" + String.valueOf(model.getAmount()));

        if(model.getDetail().toString().equals("Successful"))
        {
            holder.paytmDetail.setTextColor(Color.GREEN);

        }
        else
        {
            holder.paytmDetail.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView paytmDetail, paytmNum, paytmDate, paytmReward;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            paytmDetail = itemView.findViewById(R.id.paytmDetail);
            paytmNum = itemView.findViewById(R.id.paytmNum);
            paytmDate = itemView.findViewById(R.id.paytmDate);
            paytmReward = itemView.findViewById(R.id.paytmReward);

        }
    }



}
