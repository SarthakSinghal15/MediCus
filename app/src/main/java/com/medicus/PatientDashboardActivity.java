package com.medicus;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

public class PatientDashboardActivity extends AppCompatActivity {

    UserSessionManager session;

    Button todaySchedule, weekSchedule, emergencyCall;
    TextView pid, pname, paddr, pemergency;

    String name,medicine;
    int piReqNo = 0;

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
        getPermissions();

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

    private void getPermissions() {
        /*
        int smsPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int phoneStatePermissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);

        if(smsPermissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Log.i("Message Permission","Permission granted");
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},0);
        }

        if(phoneStatePermissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Log.i("Read Phone State","Permission granted");
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);
        }
        */

        String[] permissions = {Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE};

        ActivityCompat.requestPermissions(this,permissions,0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case 0:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("Message Permission","Permission granted");
                }
                else
                    Log.i("Message Permission","Permission not granted");
                break;
            case 1:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("Read Phone State","Permission granted");
                }
                else
                    Log.i("Read Phone State","Permission not granted");
                break;
        }
    }

    private void loadProfile()
    {
        Log.i("load profile","loading profile");
        HashMap<String, String> user = session.getUserDetails();

        pid.setText(user.get(UserSessionManager.U_ID));
        pname.setText(user.get(UserSessionManager.U_NAME));
        pemergency.setText(user.get(UserSessionManager.U_ECONTACT));
        paddr.setText(user.get(UserSessionManager.U_ADDR));

        Log.i("load profile","loaded profile");
    }

    private void setAlarm(long timeInMillis) {

        //Log.i("main activity","setting alarm");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyAlarm.class);
        intent.putExtra("name",name);
        intent.putExtra("medicine",medicine);
        intent.putExtra("reqno",piReqNo);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, piReqNo, intent,PendingIntent.FLAG_CANCEL_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        Toast.makeText(this, "Alarm is set "+piReqNo,Toast.LENGTH_SHORT).show();

        Log.i("Alarm Set", "Name: "+ name + ", Medicine: "+ medicine + ", Request No.: "+piReqNo);

        piReqNo++;
    }

    private void clearAlarms()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);

        for(int i=0;i<piReqNo;i++)
        {
            Log.i("Clear Alarm", "Clear alarm "+i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(this, "Cleared all alarms",Toast.LENGTH_SHORT).show();

        piReqNo = 0;
    }
}
