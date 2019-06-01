package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_PREFS = "MainPrefs";
    public static final String POLICEMAN_NAME_KEY = "PolicemanName";
    public static final String POLICESTATION_NAME_KEY = "PoliceStationName";
    public static final String POLICEMAN_ID_KEY = "PolicemanId";
    public static final String POLICEMAN_RANK_KEY = "PolicemanRank";
    public static final String API="http://192.168.8.135:3000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OffencesModel.createOffences();

        SharedPreferences prefs;
        if(isLogged()){
            prefs=getSharedPreferences(MAIN_PREFS,MODE_PRIVATE);
            String policemanName=prefs.getString(POLICEMAN_NAME_KEY,null);
            finish();
            Intent homeIntent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(homeIntent);
        }
        else {
            finish();
            Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private boolean isLogged(){
        SharedPreferences prefs= getSharedPreferences(MAIN_PREFS,MODE_PRIVATE);
        String policemanName=prefs.getString(POLICEMAN_NAME_KEY,null);
        Log.d("chance","checked islogged");
        return policemanName==null? false:true;
    }
}




