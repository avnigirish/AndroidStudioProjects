package com.example.broadcastdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.id_test);

        BatteryMonitor myMonitor = new BatteryMonitor();
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myMonitor, batteryFilter);

    }

    public class BatteryMonitor extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Battery has been changed", Toast.LENGTH_SHORT).show();
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -99);
            if(status == -1)
                textView.setText("Error");
            if(status == 5)
                textView.setText("Full Charge");
            if(status == 2)
                textView.setText("Charging...");
        }
    }
}