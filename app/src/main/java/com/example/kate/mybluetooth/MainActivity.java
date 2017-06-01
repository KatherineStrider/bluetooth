package com.example.kate.mybluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DevicesReceiver receiver;
    DevicesReceiver receiverWifi;
    TextView txtDevBluetooth;
    TextView txtDevWifi;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDevBluetooth = (TextView) findViewById(R.id.textBluetoothDevices);
        txtDevWifi = (TextView) findViewById(R.id.textWifiDevices);
        receiver = new DevicesReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver,intentFilter);

        receiverWifi = new DevicesReceiver();
        IntentFilter intentFilterWiFi = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(receiverWifi,intentFilterWiFi);

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        this.unregisterReceiver(receiverWifi);
        super.onDestroy();
    }

    public class DevicesReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevice.getName();
                txtDevBluetooth.append("\n" + bluetoothDevice.getName());
            }

            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> scanResults = wifiManager.getScanResults();

                Object[] result = scanResults.toArray();

                for (int i = 0; i < result.length; i++) {
                    String s = "\n" + result[i].toString();
                    txtDevWifi.append(s);
                }
            }
        }
    }
}
