package com.example.hitesh.kuchbhi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.HashMap;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity {

    ScanBTLE scanBTLE = new ScanBTLE( this , 7500 , -75);
    public HashMap<String , String> scanDeviceHashMap = new HashMap<String , String> ();
    public TreeSet<String> scanDeviceArrayList = new  TreeSet<String>();
    public ListAdapter adapter;
    public String[] array;
    private static final String TAG = "myTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BluetoothAdapter  mBluetoothAdapter =callBluetoothManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ensureBLEExists())
        {
           displayWarning("WARNING","This App Is Not Supported On Your Device");
        }
        toogleButtonPressed(mBluetoothAdapter);
    }



    public BluetoothAdapter callBluetoothManager() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter  mBluetoothAdapter = bluetoothManager.getAdapter();
        return mBluetoothAdapter;
    }

    public ToggleButton toogleButtonPressed(final BluetoothAdapter mBluetoothAdapter) {

        ToggleButton toggle = (ToggleButton) findViewById(R.id.scanToggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scanDeviceArrayList=new TreeSet<String>();
                if (isChecked) {
                    checkBluetooth(mBluetoothAdapter);
                    if(scanDeviceHashMap.size() !=0) {
                        scanDeviceHashMap.clear();
                        scanDeviceArrayList=new TreeSet<String>();
                    }
                    scanDeviceArrayList=new TreeSet<String>();
                    TextView scanText = (TextView) findViewById(R.id.scanText);
                    scanText.setVisibility(View.GONE);
                    scanBTLE.scanLeDevice(true , mBluetoothAdapter);
                    ListView listView = (ListView) findViewById(R.id.displayList);
                    listView.setVisibility(View.VISIBLE);

                } else {
                    scanBTLE.scanLeDevice(false , mBluetoothAdapter);
                    ListView listView = (ListView) findViewById(R.id.displayList);
                    listView.setAdapter(null);
                    listView.setVisibility(View.GONE);
                    TextView scanText = (TextView) findViewById(R.id.scanText);
                    scanText.setVisibility(View.VISIBLE);
                    scanText.setText("Click The Button To Start Scanning");
                }
            }
        });
        return toggle;
    }

    public void addDevice(BluetoothDevice device , int rssi , byte[] scanRecord) {

        HashMap<Integer , String> gap = new HashMap<Integer , String>();
        gap.put(1,"Flags");
        gap.put(Integer.parseInt("2",16), "Incomplete List of 16-bit Service Class UUIDs");
        gap.put(Integer.parseInt("3",16), "Complete List of 16-bit Service Class UUIDs");
        gap.put(Integer.parseInt("4",16), "Incomplete List of 32-bit Service Class UUIDs");
        gap.put(Integer.parseInt("5",16), "Complete List of 32-bit Service Class UUIDs");
        gap.put(Integer.parseInt("6",16), "Incomplete List of 128-bit Service Class UUIDs");
        gap.put(Integer.parseInt("7",16), "Complete List of 128-bit Service Class UUIDs");
        gap.put(Integer.parseInt("8",16), "Shortened Local Name");
        gap.put(Integer.parseInt("9",16), "Complete Local Name");
        gap.put(Integer.parseInt("0A",16), "Tx Power Level");
        gap.put(Integer.parseInt("0D",16), "Class of Device");
        gap.put(Integer.parseInt("0E",16), "Simple Pairing Hash C");
        gap.put(Integer.parseInt("0F",16), "Simple Pairing Randomizer R");
        gap.put(Integer.parseInt("10",16), "Device ID");
        gap.put(Integer.parseInt("11",16), "Security Manager Out of Band Flags");
        gap.put(Integer.parseInt("12",16), "Slave Connection Interval Range");
        gap.put(Integer.parseInt("14",16), "List of 16-bit Service Solicitation UUIDs");
        gap.put(Integer.parseInt("1F",16), "List of 32-bit Service Solicitation UUIDs");
        gap.put(Integer.parseInt("15",16), "List of 128-bit Service Solicitation UUIDs");
        gap.put(Integer.parseInt("16",16), "Service Data - 16-bit UUID");
        gap.put(Integer.parseInt("20",16), "Service Data - 32-bit UUID");
        gap.put(Integer.parseInt("21",16), "Service Data - 128-bit UUID");
        gap.put(Integer.parseInt("22",16), "LE Secure Connections Confirmation Value");
        gap.put(Integer.parseInt("23",16), "LE Secure Connections Random Value");
        gap.put(Integer.parseInt("24",16), "URI");
        gap.put(Integer.parseInt("25",16), "Indoor Positioning");
        gap.put(Integer.parseInt("26",16), "Transport Discovery Data");
        gap.put(Integer.parseInt("17",16), "Public Target Address");
        gap.put(Integer.parseInt("18",16), "Random Target Address");
        gap.put(Integer.parseInt("19",16), "Appearance");
        gap.put(Integer.parseInt("1A",16), "Advertising Interval");
        gap.put(Integer.parseInt("1B",16), "LE Bluetooth Device Address");
        gap.put(Integer.parseInt("1C",16), "LE Role");
        gap.put(Integer.parseInt("1D",16), "Simple Pairing Hash C-256");
        gap.put(Integer.parseInt("1E",16), "Simple Pairing Randomizer R-256");
        gap.put(Integer.parseInt("3D",16), "3D Information Data");
        gap.put(Integer.parseInt("FF",16), "Manufacturer Specific Data");
        gap.put(Integer.parseInt("27",16), "LE Supported Features");
        gap.put(Integer.parseInt("28",16), "Channel Map Update Indication");

        int pointer = 0;
        String address = device.getAddress();
        while(pointer!=scanRecord.length-1)
        {
            int length = Integer.parseInt(scanRecord[pointer]+"");
            if(pointer-3 < scanRecord.length){
                pointer++;
                int type = Integer.parseInt(scanRecord[pointer]+"");
                length--;
                switch (type) {

                    case 1:
                        String description1 = gap.get(type);
                        pointer++;
                        int value1 = Integer.parseInt(scanRecord[pointer]+"");
                        pointer++;
                        break;
                    case 3:
                        String description3 = gap.get(type);
                        pointer = pointer+2;
                        String value3 = scanRecord[pointer]+""+scanRecord[pointer-1];
                        pointer++;
                        break;
                    case 8:
                    case 9:
                        String description9 = gap.get(type);
                        String data9 = "";
                        while(length>0) {
                            int temp = Integer.parseInt(scanRecord[pointer]+"");
                            data9 = data9+ (char)temp;
                            length--;
                            pointer++;
                        }
                        scanDeviceArrayList.add(data9);
                        scanDeviceHashMap.put(address , data9);
                        break;
                }
            }
        }
        int i=0;
        this.array = new String[scanDeviceArrayList.size()];
        for(String Names : scanDeviceArrayList)
        {
        array[i] = Names;
          i++;
        }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            ListView listView = (ListView) findViewById(R.id.displayList);
            listView.setAdapter(adapter);
        }

    public void checkBluetooth(BluetoothAdapter mBluetoothAdapter)
    {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
            startActivity(discoverableIntent);
        }
    }

    private void displayDialogBox(String title , String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("OFFER for: "+title);
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static int id = 12345;

    private void displayNotification()
    {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

    }

    private boolean ensureBLEExists()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    private void displayWarning(String title , String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this , DisplayNotification.class);
        startService(intent);
                }




    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkBluetooth();
        Log.i(TAG ,"onResume");
    }


    @Override
    protected void onStart() {
        super.onStart();
        // checkBluetooth();
        Log.i(TAG ,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //checkBluetooth();
        Log.i(TAG ,"onRestart");
    }

    public boolean checktoogleButtonPressed() {
        final BluetoothAdapter mBluetoothAdapter = callBluetoothManager();
        return toogleButtonPressed(mBluetoothAdapter).isChecked();
    }
}
