package com.medicus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class PatientTodaySchedule extends AppCompatActivity {

    private ArrayList<String> medNames,medTimes;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_today_schedule);

        medNames = new ArrayList<>();
        medTimes = new ArrayList<>();

        getTodaysMedicines();
        setRecyclerView();
    }

    private void getTodaysMedicines()
    {
        // code to get today's medicines

        medNames.add("Crocine");
        medTimes.add("9:00 AM");

        medNames.add("Paracetamol");
        medTimes.add("12:00 PM");

        medNames.add("Combiflame");
        medTimes.add("3:00 PM");

        medNames.add("Oncet CF");
        medTimes.add("6:00 PM");

        medNames.add("B-Capsule");
        medTimes.add("9:00 PM");

    }

    private void setRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rv_todaymeds);
        MedicineRecyclerViewAdapter recyclerViewAdapter = new MedicineRecyclerViewAdapter(this,medNames,medTimes);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
