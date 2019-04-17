package com.medicus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> medNames = new ArrayList<>();
    private ArrayList<String> medTimes = new ArrayList<>();
    private Context context;

    public MedicineRecyclerViewAdapter(Context context, ArrayList<String> medNames, ArrayList<String> medTimes ) {
        this.medNames = medNames;
        this.medTimes = medTimes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_medicine, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.medName.setText(medNames.get(position));
        viewHolder.medTime.setText(medTimes.get(position));

        viewHolder.medLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Take "+medNames.get(position)+" at "+medTimes.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView medName, medTime;
        LinearLayout medLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.txt_medName);
            medTime = itemView.findViewById(R.id.txt_medTime);
            medLayout = itemView.findViewById(R.id.medicine_layout);
        }
    }
}
