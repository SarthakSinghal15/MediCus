package com.medicus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class PatientHeartRateHistoryActivity extends AppCompatActivity {

    private ArrayList<Integer> bpmRate;
    private ArrayList<String> bpmTime;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_heart_rate_history);

        bpmRate = (ArrayList<Integer>) getIntent().getSerializableExtra("bpmList");
        bpmTime = (ArrayList<String>) getIntent().getSerializableExtra("bpmTime");

        setRecyclerView();
    }

    private void setRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rv_hrhistory);
        HeartRateHistoryAdapter recyclerViewAdapter = new HeartRateHistoryAdapter(this,bpmRate,bpmTime);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
