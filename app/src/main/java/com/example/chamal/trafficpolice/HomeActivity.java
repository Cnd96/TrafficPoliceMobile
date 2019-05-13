package com.example.chamal.trafficpolice;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Toast.makeText(HomeActivity.this,"Internet connected",Toast.LENGTH_SHORT).show();
            connected = true;
        }
        else {
            connected = false;
            Toast.makeText(HomeActivity.this,"Internet not connected",Toast.LENGTH_SHORT).show();
            return;
        }
        if(driverLicenseNo.length()!=8)
        {
            Toast.makeText(HomeActivity.this,"Wrong license number",Toast.LENGTH_SHORT).show();
            return;
        };


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
                Toast.makeText(HomeActivity.this,"Failed To Connect",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDriverDetails(JSONObject jsonObject){

        DriverDetailsModel driver=DriverDetailsModel.getInstance();
        try {

            int catogeriesOfVehiclesLength = jsonObject.getJSONArray("CatogeriesOfVehicles").length();
            ArrayList<String> categoriesList = new ArrayList<String>();
            for (int i=0;i<catogeriesOfVehiclesLength;i++){
                categoriesList.add(jsonObject.getJSONArray("CatogeriesOfVehicles").getString(i));
            }

            int driverFinesListLength = jsonObject.getJSONArray("fines").length();
            ArrayList<DriverDetailsFinesModel> driverFinesList = new ArrayList<DriverDetailsFinesModel>();

            for (int i=0;i<driverFinesListLength;i++){
                driverFinesList.add(new DriverDetailsFinesModel(
                        jsonObject.getJSONArray("fines").getJSONObject(i).getInt("_id"),
                        jsonObject.getJSONArray("fines").getJSONObject(i).getInt("amount"),
                        jsonObject.getJSONArray("fines").getJSONObject(i).getInt("totalAmountPaid"),
                        jsonObject.getJSONArray("fines").getJSONObject(i).getBoolean("fineStatus"),
                        jsonObject.getJSONArray("fines").getJSONObject(i).getString("date")));
            }

            driver.setName(jsonObject.getString("Name"));
            driver.setAddress(jsonObject.getString("Address"));
            driver.setDateOfExpire(jsonObject.getString("DateOfExpire"));
            driver.setDateOfIssue(jsonObject.getString("DateOfIssue"));
            driver.setLicenseNO(jsonObject.getString("_id"));
            driver.setCatogeriesOfVehicles(categoriesList);
            driver.setDriverFinesList(driverFinesList);


        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
