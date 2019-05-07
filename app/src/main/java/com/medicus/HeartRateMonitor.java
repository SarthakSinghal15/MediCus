package com.medicus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HeartRateMonitor extends BroadcastReceiver {

    List<Integer> hr_bpm;
    List<String> hr_time;

    public HeartRateMonitor()
    {
        hr_bpm = new ArrayList<>();
        hr_time = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //accessGoogleFit(context);

        Log.i("hrmonitor","In hrmonitor");
        addHeartRateData();

    }

    private void accessGoogleFit(Context context)
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -2);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_HEART_RATE_BPM)
                .setTimeRange(startTime, endTime, TimeUnit.MINUTES)
                .build();

        Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context)).readData(readRequest)
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        List<DataSet> dataSets = task.getResult().getDataSets();
                        Log.i("HR get data","reading inserted data");
                        if(dataSets.size()>0) {
                            Log.i("dump data set","data found");
                            //for(DataSet ds: dataSets)
                                //dumpDataSet(ds);
                        }
                    }
                });
    }

    private void addHeartRateData()
    {
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        Random r = new Random();
        int heartbpm = r.nextInt(80-75)+75;

        if(hr_bpm.size()==10)
        {
            hr_bpm.remove(0);
            hr_time.remove(0);
        }
        hr_bpm.add(heartbpm);
        hr_time.add(currentDateTime);

        Log.i("hrmonitor","Time: "+currentDateTime+" ; BPM: "+heartbpm);
    }

    public List<Integer> getHeartRateBPM(){return hr_bpm;}

    public List<String> getHearRateTime() {return hr_time;}


}
