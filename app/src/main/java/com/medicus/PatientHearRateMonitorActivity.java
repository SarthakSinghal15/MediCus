package com.medicus;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class PatientHearRateMonitorActivity extends AppCompatActivity {

    Switch hr_switch, hr_emercall;
    UserSessionManager session;
    public static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE =1234;
    public static final int HEART_RATE_MONITOR_REQUEST_CODE = 2222;
    List<Integer> heartRateBPM;
    List<String> heartRateTime;
    HeartRateMonitor hrmonitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_hear_rate_monitor);
        session = new UserSessionManager(getApplicationContext());
        hrmonitor = new HeartRateMonitor();

        hr_switch = findViewById(R.id.hr_switch);
        hr_emercall = findViewById(R.id.hr_emercall);

        boolean hrstatus = session.getHRMonitorStatus();
        boolean hrecall = session.getHRECallStatus();
        changeComponentAccess(hrstatus);
        hr_switch.setChecked(hrstatus);
        hr_emercall.setChecked(hrecall);

        hr_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    session.setHRMonitorStatus(true);
                    changeComponentAccess(true);

                    getGoogleFitPermission();
                    //addGoogleFitData();
                    //accessGoogleFit();
                    setHeartRateMonitor();
                }
                else
                {
                    session.setHRMonitorStatus(false);
                    changeComponentAccess(false);
                    hr_emercall.setChecked(false);
                    cancelHeartRateMonitor();
                }
            }
        });

        hr_emercall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    session.setHRECallStatus(true);
                }
                else {
                    session.setHRECallStatus(false);
                }
            }
        });
    }

    private void setHeartRateMonitor()
    {
        // We want the alarm to go off 2 seconds from now.
//        long firstTime = SystemClock.elapsedRealtime();
//        firstTime += 2 * 1000;//start 2 seconds after first register.

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        long alarm_time = cal.getTimeInMillis();

//        if(cal.before(Calendar.getInstance()))
//            alarm_time += 60*1000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, HeartRateMonitor.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, HEART_RATE_MONITOR_REQUEST_CODE, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,90000, pendingIntent);

        Log.i("hrmonitor","hrmonitor alarm set");
    }

    private void cancelHeartRateMonitor()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, HeartRateMonitor.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, HEART_RATE_MONITOR_REQUEST_CODE, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.i("hrmonitor","hrmonitor alarm cancelled");
    }



    private void getGoogleFitPermission()
    {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_HEART_RATE_BPM,FitnessOptions.ACCESS_WRITE)
                .build();

        if(!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this),fitnessOptions))
        {
            GoogleSignIn.requestPermissions(this,GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, GoogleSignIn.getLastSignedInAccount(this),fitnessOptions);
        }
        else
        {
            Log.i("Google fit", "Already have permission");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
            {
                Log.i("Google fit", "Got permission");
            }
        }
    }

    private void changeComponentAccess(boolean flag)
    {
        LinearLayout layout = findViewById(R.id.hr_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(flag);
        }

        LinearLayout layout2 = findViewById(R.id.newbpm_layout);
        for (int i = 0; i < layout2.getChildCount(); i++) {
            View child = layout2.getChildAt(i);
            child.setEnabled(flag);
        }

        LinearLayout layout3 = findViewById(R.id.avgbpm_layout);
        for (int i = 0; i < layout3.getChildCount(); i++) {
            View child = layout3.getChildAt(i);
            child.setEnabled(flag);
        }
    }
}
