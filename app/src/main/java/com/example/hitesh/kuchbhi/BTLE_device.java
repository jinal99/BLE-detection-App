package com.example.hitesh.kuchbhi;

import android.bluetooth.BluetoothDevice;

/**
 * Created by hitesh on 12/28/2016.
 */

public class BTLE_device {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BTLE_device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }
}

