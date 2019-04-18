package com.medicus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorViewPatient extends AppCompatActivity {

    private ArrayList<String> patNames,patIds,patAddresses,patContacts;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient);

        patNames = new ArrayList<>();
        patIds = new ArrayList<>();
        patAddresses = new ArrayList<>();
        patContacts = new ArrayList<>();

        getPatients();
        setRecyclerView();
    }

    private void getPatients()
    {
        // code to get today's medicines

        patNames.add("Crocine");
        patIds.add("9:00 AM");
        patAddresses.add("899, Morrison Park Drive, San Jose, CA, USA");
        patContacts.add("asdsadasd");

        patNames.add("Crocine");
        patIds.add("9:00 AM");
        patAddresses.add("asdsadasd");
        patContacts.add("asdsadasd");

        patNames.add("Crocine");
        patIds.add("9:00 AM");
        patAddresses.add("asdsadasd");
        patContacts.add("asdsadasd");

        patNames.add("Crocine");
        patIds.add("9:00 AM");
        patAddresses.add("asdsadasd");
        patContacts.add("asdsadasd");

        patNames.add("Crocine");
        patIds.add("9:00 AM");
        patAddresses.add("asdsadasd");
        patContacts.add("asdsadasd");

    }



    private void setRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rv_viewpatients);
        PatientRecyclerViewAdapter recyclerViewAdapter = new PatientRecyclerViewAdapter(this,patNames,patIds,patAddresses,patContacts);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
