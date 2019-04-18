package com.medicus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DoctorAddPatientActivity extends AppCompatActivity {

    EditText add_patient;
    Button add_p;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_patient);
        add_patient = (EditText)findViewById(R.id.input_patient_id);
        add_p= (Button)findViewById(R.id.btn_addPatientID);
        add_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patient_id= add_patient.getText().toString();
                int did= UserSessionManager.userid;
                Map<String, Object> relation= new HashMap<>();
                relation.put("PID", patient_id);
                relation.put("DID", did);
                db.collection("relationTable").document().set(relation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("****DATA****", "data hass been saved");
                        Intent intent = new Intent(getApplicationContext(),DoctorDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
