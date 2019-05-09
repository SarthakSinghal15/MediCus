package com.medicus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HeartRateHistoryAdapter extends RecyclerView.Adapter<HeartRateHistoryAdapter.ViewHolder>{

    private ArrayList<Integer> bpmRate = new ArrayList<>();
    private ArrayList<String> bpmTime = new ArrayList<>();
    private Context context;

    public HeartRateHistoryAdapter(Context context, ArrayList<Integer> bpmRate, ArrayList<String> bpmTime ) {
        this.bpmRate = bpmRate;
        this.bpmTime = bpmTime;
        this.context = context;
    }

    @NonNull
    @Override
    public HeartRateHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_heartrate_history, viewGroup,false);
        HeartRateHistoryAdapter.ViewHolder viewHolder = new HeartRateHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }


    public void onBindViewHolder(@NonNull HeartRateHistoryAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.bpmRate.setText(bpmRate.get(position)+" BPM");
        viewHolder.bpmTime.setText(bpmTime.get(position));

        viewHolder.hrhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bpmRate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bpmRate, bpmTime;
        LinearLayout hrhistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bpmRate = itemView.findViewById(R.id.txt_bpmRate);
            bpmTime = itemView.findViewById(R.id.txt_bpmTime);
            hrhistory = itemView.findViewById(R.id.hearthistory_layout);
        }
    }
}
