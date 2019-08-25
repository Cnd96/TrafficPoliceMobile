package com.example.chamal.trafficpolice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSms extends AppCompatActivity {

    private final static int SEND_SMS_PERMISSION_CODE=111;
    private Button mSendSmsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        mSendSmsBtn=findViewById(R.id.sendSmsButton);

        final EditText phoneNumberTxt=findViewById(R.id.phoneNumberText);
        if(checkPermission(Manifest.permission.SEND_SMS)){

        }else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_CODE);
        }

        mSendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsSingleton smsSingleton=SmsSingleton.getInstance();

                String message="Dear sir/madam you have been fined at "+smsSingleton.getFinePlace()+" on " +
                        ""+smsSingleton.getFineDate()+" at "+smsSingleton.getFineTime()+". Your fine ID="+smsSingleton.getFineId();
                String phoneNumber =phoneNumberTxt.getText().toString();

                if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null,message,null,null);
                    Toast.makeText(SendSms.this,"Send SMS", Toast.LENGTH_SHORT).show();
                    Intent homeIntent=new Intent(SendSms.this,HomeActivity.class);
                    startActivity(homeIntent);
                }else {
                    Toast.makeText(SendSms.this,"Could not send sms", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermission(String permission) {
        int checkPermission= ContextCompat.checkSelfPermission(this,permission);
        return checkPermission== PackageManager.PERMISSION_GRANTED;
    }


}
