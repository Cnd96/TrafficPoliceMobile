package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {


    EditText mPassword;
    EditText mPolicemanId;
    Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPolicemanId=(EditText) findViewById(R.id.idNumberText);
        mPassword=(EditText) findViewById(R.id.passwordText);
        mLoginButton=(Button) findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    private void attemptLogin() {
        String id=mPolicemanId.getText().toString();
        String password=mPassword.getText().toString();

        if(id.equals("")||password.equals(""))return;
        Toast.makeText(this,"Login in Progress..", Toast.LENGTH_SHORT).show();
        try{
//            String[] colors = { "blue", "yellow" };
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("policemanId", id);
            jsonParams.put("password", password);
//            jsonParams.put("colors", colors);


            StringEntity entity = new StringEntity(jsonParams.toString());

            AsyncHttpClient client=new AsyncHttpClient();
            client.post(this,"http://192.168.8.135:3000/api/policemenLogin",entity,"application/json",new JsonHttpResponseHandler(){
                @Override
                public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    Log.d("chance","success");
                    Toast.makeText(LoginActivity.this,"Login in succesfull", Toast.LENGTH_SHORT).show();
                    setMainAppMemory(response);
                    finish();
                    Intent homeIntent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                    Log.d("chance","fail");
                    Toast.makeText(LoginActivity.this,"Login in Failed", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onRetry(int retryNo) {
                    Log.d("chance","fail");
                }

            });
        }catch (Exception e){
            Log.d("chance","exception");
            e.printStackTrace();
        }
    }

    private void setMainAppMemory(JSONObject jsonObject){
        try {

            String name = jsonObject.getString("name");
            String id=jsonObject.getString("_id");
            String rank=jsonObject.getJSONObject("rank").getString("name");
            String policeStationName=jsonObject.getJSONObject("policeStation").getString("policeStationName");

            SharedPreferences prefs=getSharedPreferences(MainActivity.MAIN_PREFS,0);
            prefs.edit().putString(MainActivity.POLICEMAN_NAME_KEY,name).apply();
            prefs.edit().putString(MainActivity.POLICEMAN_ID_KEY,id).apply();
            prefs.edit().putString(MainActivity.POLICEMAN_RANK_KEY,rank).apply();
            prefs.edit().putString(MainActivity.POLICESTATION_NAME_KEY,policeStationName).apply();
            
        }catch (JSONException e){
            e.printStackTrace();

        }
    }
}
