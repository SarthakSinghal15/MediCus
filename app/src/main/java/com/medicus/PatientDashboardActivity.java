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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                setAlarm(1234,"Ajinkya Thakare","Paracetamol",0,38,4);
                //clearAlarm(1234);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPermissions() {
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

    private void setAlarm(int prescNo, String name, String medicine, int hour, int minute, int day) {

        //Log.i("main activity","setting alarm");

        String firstName = name.split("\\s+")[0];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.DAY_OF_WEEK,day);

        Log.i("Alarm set", "Set for "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" on day "+cal.get(Calendar.DAY_OF_WEEK));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyAlarm.class);
        intent.putExtra("name",firstName);
        intent.putExtra("medicine",medicine);
        intent.putExtra("reqno",prescNo);
        intent.putExtra("hour",hour);
        intent.putExtra("minute",minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, prescNo, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, pendingIntent);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        //Toast.makeText(this, "Alarm is set "+prescNo,Toast.LENGTH_SHORT).show();

        Log.i("Alarm Set", "Name: "+ name + ", Medicine: "+ medicine + ", Request No.: "+prescNo);

    }

    private void clearAlarm( int prescNo)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);

        Log.i("Clear Alarm", "Clear alarm "+prescNo);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, prescNo, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);

        //Toast.makeText(this, "Cleared all alarms",Toast.LENGTH_SHORT).show();

    }
}
