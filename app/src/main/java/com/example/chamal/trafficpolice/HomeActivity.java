package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    Button mRecordFinesButton;
    FloatingActionButton mSearchDriverButton;
    Button mCourtCasesButton;
    AutoCompleteTextView mSearchDriverText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecordFinesButton=(Button) findViewById(R.id.btnRecordFines);
        mSearchDriverButton=(FloatingActionButton) findViewById(R.id.btnDriverDetailsSearch);
        mCourtCasesButton=(Button) findViewById(R.id.btnCourtCases);
        mSearchDriverText=(AutoCompleteTextView) findViewById(R.id.searchDriverText);


        mSearchDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDriverDetails();
            }
        });


        mRecordFinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent offenceIntent=new Intent(HomeActivity.this,OffencesActivity.class);
                startActivity(offenceIntent);
            }
        });
    }

    private  void getDriverDetails(){
        String driverLicenseNo=mSearchDriverText.getText().toString();
        if(driverLicenseNo.equals(""))return;


        Log.d("chance","Searching"+driverLicenseNo);
        Toast.makeText(this,"Searching..", Toast.LENGTH_SHORT).show();

        AsyncHttpClient client=new AsyncHttpClient();
        client.get("http://192.168.8.135:3000/api/driverFines/"+driverLicenseNo,new JsonHttpResponseHandler(){
            @Override
            public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("chance","success");
                Log.d("chance",response.toString());
                setDriverDetails(response);
                Intent driverDetailsIntent=new Intent(HomeActivity.this,DriverDetailsActivity.class);
                startActivity(driverDetailsIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                Log.d("chance","fail");

            }
        });
    }

    private void setDriverDetails(JSONObject jsonObject){

        DriverDetailsModel driver=DriverDetailsModel.getInstance();
        try {
            driver.setName(jsonObject.getString("Name"));

//            weatherDataModel.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
//            weatherDataModel.mIconName=updateWeatherIcon(weatherDataModel.mCondition);
//
//            double tempResult=(jsonObject.getJSONObject("main").getDouble("temp")-273.15);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}