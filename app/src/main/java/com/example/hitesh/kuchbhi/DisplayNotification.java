package com.example.hitesh.kuchbhi;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import java.util.TreeSet;

public class DisplayNotification extends IntentService {

    public DisplayNotification() {
        super("DisplayNotification");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
MainActivity ma = new MainActivity();
        final BluetoothAdapter mBluetoothAdapter =ma.callBluetoothManager();
        if(ma.checktoogleButtonPressed()==true)
        {
            if (ma.scanDeviceHashMap.size() != 0) {
                ma.scanDeviceHashMap.clear();
                ma.scanDeviceArrayList = new TreeSet<String>();
            }
            ma.scanDeviceArrayList = new TreeSet<String>();
            ma.scanBTLE.scanLeDevice(true, mBluetoothAdapter);
        }
        if(ma.scanDeviceArrayList.size() !=0)
        {
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification)
                            .setContentTitle("Beacon Detected")
                            .setContentText("Display Offer here");
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
        }



    }

    }



