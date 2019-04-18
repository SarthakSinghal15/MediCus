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

public class PatientRecyclerViewAdapter extends RecyclerView.Adapter<PatientRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> patNames = new ArrayList<>();
    private ArrayList<String> patIds = new ArrayList<>();
    private ArrayList<String> patAddresses = new ArrayList<>();
    private ArrayList<String> patContacts = new ArrayList<>();
    private Context context;

    public PatientRecyclerViewAdapter(Context context, ArrayList<String> patNames, ArrayList<String> patIds,ArrayList<String> patAddress, ArrayList<String> patContact ) {
        this.patNames = patNames;
        this.patIds = patIds;
        this.patAddresses = patAddress;
        this.patContacts = patContact;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_patient, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.patName.setText(patNames.get(position));
        viewHolder.patID.setText(patIds.get(position));
        viewHolder.patAddress.setText(patAddresses.get(position));
        viewHolder.patContact.setText(patContacts.get(position));

        viewHolder.patLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Patient "+patNames.get(position)+"'s Id is "+patIds.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return patNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView patID, patName,patAddress,patContact;
        LinearLayout patLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patID = itemView.findViewById(R.id.txt_patId);
            patName = itemView.findViewById(R.id.txt_patName);
            patAddress = itemView.findViewById(R.id.txt_patAddr);
            patContact = itemView.findViewById(R.id.txt_patContact);
            patLayout = itemView.findViewById(R.id.patient_layout);
        }
    }
}

