package com.example.hitesh.kuchbhi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

/**
 * Created by hitesh on 12/28/2016.
 */

public class ScanBTLE extends Activity{

    private boolean mScanning;
    private Handler mHandler;
    private MainActivity ma ;
    long scanPeriod;
    int signalStrength;



    public ScanBTLE(MainActivity mainActivity , long scanPeriod , int signalStrength)
    {
        ma = mainActivity;
        mHandler = new Handler();
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;


    }

    public void scanLeDevice(final boolean enable , final BluetoothAdapter  mBluetoothAdapter) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override

                public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
                    final int new_rssi = rssi;
                    if(rssi >= signalStrength)
                    {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ma.addDevice(device , new_rssi , scanRecord);
                    }


                        });
                    }
                }



            };
}
