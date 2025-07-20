package com.example.mad_demo;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView ramText, wifiText,ramavail,wifiip,ram,storageText,batteryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ramText = findViewById(R.id.ramText);
        ram = findViewById(R.id.ram);
        wifiText = findViewById(R.id.wifitext);
        ramavail = findViewById(R.id.ramavail);
        wifiip = findViewById(R.id.wifiip);
        storageText=findViewById(R.id.storageText);
        batteryText=findViewById(R.id.batteryText);
        displayRamInfo();
        displayWifiSpeed();
        displayStorageInfo();
        displayBatteryDetails();
    }


    private void displayRamInfo() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long usedMemInMB = (memoryInfo.totalMem - memoryInfo.availMem) / (1000000000);
        ramText.setText("RAM: " + usedMemInMB + " GB");
        ram.setText("Total Memory: " + memoryInfo.totalMem/(1000000000) + " GB");
        ramavail.setText("Available Memory: " + memoryInfo.availMem/(1000000000) + " GB\n" );
    }

    private void displayWifiSpeed() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int linkSpeed = wifiInfo.getLinkSpeed();
        wifiText.setText("Wi-Fi Speed: " + linkSpeed + " Mbps");
        String ip=Formatter.formatIpAddress(wifiInfo.getIpAddress());
        wifiip.setText("Ip Address: " + ip);
    }
    private void displayStorageInfo() {
        long totalStorage = getTotalInternalMemorySize() / (1000000000); // MB
        long availableStorage = getAvailableInternalMemorySize() / (1000000000); // MB

        storageText.append("Total: " + totalStorage + " GB\nAvailable: " + availableStorage + " GB");
    }

    private long getTotalInternalMemorySize() {
        return new android.os.StatFs(getFilesDir().getPath()).getTotalBytes();
    }

    private long getAvailableInternalMemorySize() {
        return new android.os.StatFs(getFilesDir().getPath()).getAvailableBytes();
    }
    private void displayBatteryDetails() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, intentFilter);

        if (batteryStatus != null) {
            int temperature = batteryStatus.getIntExtra("temperature", -1) / 10; // in °C
            int health = batteryStatus.getIntExtra("health", 0);

            String healthStatus = "Null";
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthStatus = "Good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthStatus = "Overheat";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthStatus = "Dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthStatus = "Over Voltage";
                    break;
            }

            batteryText.append("Temp: " + temperature + "°C Health: " + healthStatus);
        }
    }


}