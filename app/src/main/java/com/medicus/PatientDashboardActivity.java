package com.medicus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class PatientDashboardActivity extends AppCompatActivity {

    UserSessionManager session;

    Button todaySchedule, weekSchedule, emergencyCall;
    TextView pid, pname, paddr, pemergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        session = new UserSessionManager(getApplicationContext());

        todaySchedule = (Button) findViewById(R.id.btn_todaySchedule);
        weekSchedule = (Button) findViewById(R.id.btn_weekSchedule);
        emergencyCall = (Button) findViewById(R.id.btn_emergencyCall);

        pid = (TextView) findViewById(R.id.txt_patientId);
        pname = (TextView) findViewById(R.id.txt_patientName);
        paddr = (TextView) findViewById(R.id.txt_patientAddr);
        pemergency = (TextView) findViewById(R.id.txt_emergencyNo);

        loadProfile();

        todaySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientTodaySchedule.class);
                startActivity(intent);
            }
        });

        weekSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientWeekSchedule.class);
                startActivity(intent);
            }
        });

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to call emergency contact
            }
        });
    }

    private void loadProfile()
    {
        HashMap<String, String> user = session.getUserDetails();

        pid.setText(user.get(UserSessionManager.U_ID));
        pname.setText(user.get(UserSessionManager.U_NAME));
        pemergency.setText(user.get(UserSessionManager.U_ECONTACT));
        paddr.setText(user.get(UserSessionManager.U_ADDR));
    }
}
