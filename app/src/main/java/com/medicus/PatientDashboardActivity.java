package com.medicus;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {

    UserSessionManager session;
    private static SQLiteHelper sqLiteHelper;

    Button todaySchedule, weekSchedule, emergencyCall, syncPrescriptions;
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
        sqLiteHelper = new SQLiteHelper(getApplicationContext(),"UserDB.sqlite",null,1);

        todaySchedule = (Button) findViewById(R.id.btn_todaySchedule);
        weekSchedule = (Button) findViewById(R.id.btn_weekSchedule);
        emergencyCall = (Button) findViewById(R.id.btn_emergencyCall);
        syncPrescriptions = (Button) findViewById(R.id.btn_syncmeds);

        pid = (TextView) findViewById(R.id.txt_patientId);
        pname = (TextView) findViewById(R.id.txt_patientName);
        paddr = (TextView) findViewById(R.id.txt_patientAddr);
        pemergency = (TextView) findViewById(R.id.txt_emergencyNo);

        checkTable();
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
                //setAlarm(1234,"Ajinkya Thakare","Paracetamol",0,38,4);
                //clearAlarm(1234);
            }
        });

        syncPrescriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncAllPrescriptions();
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
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.DAY_OF_WEEK,day);

        long alarm_time = cal.getTimeInMillis();

        if(cal.before(Calendar.getInstance()))
            alarm_time += AlarmManager.INTERVAL_DAY * 7;

        Log.i("Alarm set", "Set for "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" on day "+cal.get(Calendar.DAY_OF_WEEK));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyAlarm.class);
        intent.putExtra("name",firstName);
        intent.putExtra("medicine",medicine);
        intent.putExtra("reqno",prescNo);
        intent.putExtra("hour",hour);
        intent.putExtra("minute",minute);
        intent.putExtra("econtact",session.getEmergency());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, prescNo, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,AlarmManager.INTERVAL_DAY*7, pendingIntent);
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

    private void syncAllPrescriptions()
    {
        // get list of presc ids from local db
        List<Integer> prescIds = sqLiteHelper.getPrescription();

        // delete all alarms available for presc ids
        for(int prescid : prescIds) clearAlarm(prescid);

        // clear local db
        sqLiteHelper.deleteData();

        // fetch all prescs from firestore for current user id


        // load local db with new prescs data
        int uid = Integer.parseInt(session.getUserId());
        sqLiteHelper.insertPrescriptionData(uid,001,5,"3",6,8,"Crocine");
        sqLiteHelper.insertPrescriptionData(uid,001,6,"3",5,5,"Paracetamol");
        sqLiteHelper.insertPrescriptionData(uid,001,4,"3",5,3,"B-Capsule");
        sqLiteHelper.insertPrescriptionData(uid,001,6,"3",5,50,"Combiflame");
        sqLiteHelper.insertPrescriptionData(uid,001,3,"3",5,5,"Oncet-CF");
        sqLiteHelper.insertPrescriptionData(uid,001,1,"3",5,7,"Crocine");


        // set new alarms with new prescs data
        String userName = session.getUserName();
        String sql = "SELECT presId,medname,hour,minute,day FROM PRESCRIPTION";
        Cursor c = sqLiteHelper.getData(sql);
        if(c.moveToFirst())
        {
            do {
                setAlarm(c.getInt(0),userName,c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4));
            }
            while (c.moveToNext());
        }

    }

    private void checkTable(){
        Log.i("check table","checking db table ");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PRESCRIPTION(presId INTEGER PRIMARY KEY AUTOINCREMENT, patientID INTEGER,doctorID INTEGER, day INTEGER,duration VARCHAR,hour INTEGER,minute INTEGER,medname VARCHAR)");
    }

}
