package com.medicus;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorViewPatient extends AppCompatActivity {

    private ArrayList<String> patNames,patIds,patAddresses,patContacts;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient);

        patNames = new ArrayList<>();
        patIds = new ArrayList<>();
        patAddresses = new ArrayList<>();
        patContacts = new ArrayList<>();

        getPatients();
        //setRecyclerView();
    }

    private void getPatients()
    {
        Log.d("Data*********", "In get patients");
        // code to get today's medicines
        int did= UserSessionManager.userid;
        CollectionReference ref= db.collection("relationTable");
        Query rData = ref.whereEqualTo("DID", did);
        Task<QuerySnapshot> querySnapshotTask = rData.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Data*********", "In doc search");

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        int pid = Integer.parseInt(doc.get("PID").toString());
                        CollectionReference col= db.collection("Patients");
                        Query patients = col.whereEqualTo("pid", pid);
                        Task<QuerySnapshot> querySnapshotTask = patients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            public void onComplete(@NonNull Task<QuerySnapshot> subtask) {
                                if (subtask.isSuccessful()) {
                                    Log.d("Data*********", "In subtask successful");
                                    for (QueryDocumentSnapshot doc : subtask.getResult()) {
                                        Log.d("Data*********", "In subtask successfulfor");
                                        String pid = doc.get("pid").toString();
                                        String firstname =doc.get("firstname").toString();
                                        String address = doc.get("Address").toString();
                                        String emergency = doc.get("Emergency").toString();
                                        Log.d("Data*********", pid+" "+firstname+" "+address+" "+emergency+" ");
                                        patNames.add(firstname);
                                        patIds.add(pid);
                                        patAddresses.add(address);
                                        patContacts.add(emergency);
                                    }
                                    setRecyclerView();
                                } else {

                                }
                            }
                        });
                    }

                } else {

                }
            }
        });
//        patNames.add("Crocine");
//        patIds.add("9:00 AM");
//        patAddresses.add("899, Morrison Park Drive, San Jose, CA, USA");
//        patContacts.add("asdsadasd");
//
//        patNames.add("Crocine");
//        patIds.add("9:00 AM");
//        patAddresses.add("asdsadasd");
//        patContacts.add("asdsadasd");
//
//        patNames.add("Crocine");
//        patIds.add("9:00 AM");
//        patAddresses.add("asdsadasd");
//        patContacts.add("asdsadasd");
//
//        patNames.add("Crocine");
//        patIds.add("9:00 AM");
//        patAddresses.add("asdsadasd");
//        patContacts.add("asdsadasd");
//
//        patNames.add("Crocine");
//        patIds.add("9:00 AM");
//        patAddresses.add("asdsadasd");
//        patContacts.add("asdsadasd");

    }



    private void setRecyclerView()
    {
        Log.d("***DATA***", "Parat async call");
        recyclerView = (RecyclerView) findViewById(R.id.rv_viewpatients);
        PatientRecyclerViewAdapter recyclerViewAdapter = new PatientRecyclerViewAdapter(this,patNames,patIds,patAddresses,patContacts);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
