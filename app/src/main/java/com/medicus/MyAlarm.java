package com.medicus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;

import static com.medicus.App.channel_id;


public class MyAlarm extends BroadcastReceiver {

    private MediaPlayer mediaPlayer;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        //mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        //mediaPlayer.start();

        sendNotification(context,intent);

        sleepCheckNotify(context,intent);

    }

    private void sleepCheckNotify(Context context, Intent intent)
    {
        Log.i("Alarm Check","Inside Sleep-Check-Notify");

        final Context context1 = context;
        final Intent intent1 = intent;

        final Thread thread = new Thread(){
            public void run()
            {
                int count = 0;
                while(count<3)
                {
                    try
                    {
                        Log.i("Alarm Check","Sleeping for 10 secs");
                        Thread.sleep(10000);

                        if(!isNotificationRead(context1,intent1))
                        {
                            if(count<2) {
                                count++;
                                Log.i("Alarm Check", "Alarm unread, resending");
                                sendNotification(context1, intent1);
                            }
                            else {
                                count++;
                                sendMissedNotification(context1, intent1);
                            }
                        }
                        else
                        {
                            Log.i("Alarm Check", "Notification Read");
                            return;
                        }
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        thread.start();
    }



    private boolean isNotificationRead(Context context,Intent intent)
    {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] sbn = nm.getActiveNotifications();

        if(sbn.length>0)
            Log.i("Alarm Notifications","Notifications found");
        else
        {
            Log.i("Alarm Notifications","No notifications found");
        }

        for(StatusBarNotification s : sbn)
        {
            if(s.getId()==intent.getIntExtra("reqno",0))
                return false;
        }

        return true;

    }


    private void sendNotification(Context context, Intent intent)
    {
        String name = intent.getStringExtra("name");
        String medicine = intent.getStringExtra("medicine");
        int reqno = intent.getIntExtra("reqno",0);

        notificationManager = NotificationManagerCompat.from(context);

        Intent activityIntent = new Intent(context,MainActivity.class);
        //activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,reqno,activityIntent,0);

        Log.i("Alarm Ring", "Name: "+ name + ", Medicine: "+ medicine + ", Request No.: "+ reqno);

        String message = name + "'s time to take " + medicine + " medicine. Tap to confirm!";

        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Medicine Time!")
                .setColor(Color.BLUE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(reqno,notification);
    }

    private void sendMissedNotification(Context context, Intent intent)
    {
        Log.i("Alarm missed","Alarm missed, sending notification");
        String name = intent.getStringExtra("name");
        String medicine = intent.getStringExtra("medicine");
        int reqno = intent.getIntExtra("reqno",0);

        notificationManager = NotificationManagerCompat.from(context);

        Intent activityIntent = new Intent(context,MainActivity.class);
        //activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,reqno,activityIntent,0);

        Log.i("Alarm Ring Missed", "Name: "+ name + ", Medicine: "+ medicine + ", Request No.: "+ reqno);

        String message = name + " has missed to take " + medicine + " medicine";

        Notification notification = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Medicine Missed!")
                .setColor(Color.BLUE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(reqno,notification);

        sendAlertMessage(intent,"6692309354");
    }

    private void sendAlertMessage(Intent intent,String phonenumber)
    {
        Log.i("Alarm Alert SMS","Sending alert sms");
        String name = intent.getStringExtra("name");
        String medicine = intent.getStringExtra("medicine");

        String message = name + " has forgot to take " + medicine + " medicine scheduled at <some time>, kindly remind them.";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phonenumber,null,message,null,null);

        Log.i("Alarm Alert SMS","Sent alert sms");
    }
}
