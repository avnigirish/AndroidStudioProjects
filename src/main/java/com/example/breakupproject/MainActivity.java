package com.example.breakupproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    String msg = "";
    String number;
    String userMessage;
    TextView text;
    BroadcastReceiver broadcastReceiver;
    int count=0;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        text = findViewById(R.id.text);

        broadcastReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("TAG", "onReceive");
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[])bundle.get("pdus");
                SmsMessage[] arr = new SmsMessage[pdus.length];

                for(int x=0; x<pdus.length; x++){
                    arr[x] = SmsMessage.createFromPdu((byte[])pdus[x], bundle.getString("format"));
                    number = arr[0].getOriginatingAddress();
                    userMessage = arr[x].getMessageBody();
                    Log.d("TAG", "Phone num: " + number);
                    Log.d("TAG", "Message: " + userMessage);
                }
                switch (count){
                    case 0 : if(userMessage.contains("He") || userMessage.contains("Hi")){
                        msg = "Hey, we need to talk.";
                        text.setText("State: greeting stage");
                        count++;
                    }
                    else{
                        msg = "Sorry. I don't understand what you are saying...";
                    }
                        break;
                    case 1: if(userMessage.contains("Sure") || userMessage.contains("Ok")){
                        msg = "I want to breakup with you";
                        text.setText("State: breaking up stage");
                        count++;
                    }
                    else{
                        msg = "Sorry. I don't understand what you are saying...";
                    }
                        break;
                    case 2: if(userMessage.contains("Why") || userMessage.contains("What") || userMessage.contains("?")){
                        msg = "I'm sorry this isn't working out anymore.";
                        text.setText("State: questioning stage");
                        count++;
                    }
                    else{
                        msg = "Sorry. I don't understand what you are saying...";
                    }
                        break;
                    case 3:  if(userMessage.contains("Sorry")||userMessage.contains("Ok")||userMessage.contains("No") ||userMessage.contains("don't")){
                        msg = "Goodbye...";
                        text.setText("State: final stage");
                        count++;
                    }
                    else{
                        msg = "Sorry. I don't understand what you are saying...";
                    }
                        break;
                    case 4:
                        text.setText("Stages complete!");
                        count++;
                        break;
                    default:
                        break;
                }
                if(count<5){
                    Handler handler = new Handler();
                    handler.postDelayed(runnable(),(long)Math.random()*4000+2000);
                    Log.d("TAG", "Text sent");
                }

            }
        };
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, filter);
    }

    public Runnable runnable(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SmsManager text = SmsManager.getDefault();
                text.sendTextMessage(number,null, msg,null,null);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    Thread.currentThread().interrupt();
                }
            }
        };
        return runnable;
    }
}