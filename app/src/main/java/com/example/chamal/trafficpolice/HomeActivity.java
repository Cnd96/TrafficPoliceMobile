package com.example.chamal.trafficpolice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HomeActivity extends AppCompatActivity {

    Button mRecordFinesButton;
    FloatingActionButton mSearchDriverButton;
    Button mCourtCasesButton;
    AutoCompleteTextView mSearchDriverText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.LogOut:
                SharedPreferences preferences =getSharedPreferences(MainActivity.MAIN_PREFS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
                return true;
                default:return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        final SQLiteDatabase myDb=this.openOrCreateDatabase("Police",MODE_PRIVATE,null);
//        myDb.execSQL("CREATE TABLE IF NOT EXISTS fines (id INT(8),licenseNo VARCHAR,vehicleNo VARCHAR,time VARCHAR," +
//                "date VARCHAR,policemanId VARCHAR,total INT(6),fineStatus BOOLEAN,)");


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

        mCourtCasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDb();
            }

        });

    }

    private void showDb(){
        final SQLiteDatabase myDb=this.openOrCreateDatabase("Police",MODE_PRIVATE,null);
        myDb.execSQL("CREATE TABLE IF NOT EXISTS fines (fineId VARCHAR,driverLicenseNo VARCHAR,driverVehicleNo VARCHAR,fineTime VARCHAR," +
                "fineDate VARCHAR,policemanId VARCHAR,totalAmountPaid INT(6),fineStatus BOOLEAN,finePlace VARCHAR,validUntilDate VARCHAR,PRIMARY KEY (fineId))");
        myDb.execSQL("CREATE TABLE IF NOT EXISTS fineOffences (fid VARCHAR  NOT NULL,offence VARCHAR NOT NULL,FOREIGN KEY (fid) REFERENCES fines(fineId),PRIMARY KEY (fid, offence))");



        try{

            Cursor c1=myDb.rawQuery("SELECT fineId from fines",null);
            int noOfFines=c1.getCount();
//            Log.d("chance",   Integer.toString(noOfFines));
            if(noOfFines ==0){
                Toast.makeText(HomeActivity.this,"No Outbox",Toast.LENGTH_SHORT).show();
                return;
            }
            int fineIdIndexofFines=c1.getColumnIndex("fineId");
            c1.moveToFirst();

            for(int i=0;i<noOfFines;i++){
                Cursor c=myDb.rawQuery("SELECT f.fineId, f.finePlace,f.driverLicenseNo,f.driverVehicleNo,f.fineTime" +
                        ",f.validUntilDate,f.fineDate,f.policemanId,f.fineStatus,f.totalAmountPaid ,fo.offence\n" +
                        "FROM fines f INNER JOIN fineOffences fo ON f.fineId = fo.fid where f.fineId='"+c1.getString(fineIdIndexofFines)+"'",null);

                int fineIDIndex=c.getColumnIndex("fineId");
                int finePlaceIndex=c.getColumnIndex("finePlace");
                int driverLicenseNoIndex=c.getColumnIndex("driverLicenseNo");
                int driverVehicleNoIndex=c.getColumnIndex("driverVehicleNo");
                int fineTimeIndex=c.getColumnIndex("fineTime");
                int policemanIdIndex=c.getColumnIndex("policemanId");
                int offenceIndex=c.getColumnIndex("offence");
                int validUntilDateIndex=c.getColumnIndex("validUntilDate");
                int fineDateIndex=c.getColumnIndex("fineDate");
                int fineStatusIndex=c.getColumnIndex("fineStatus");
                int totalAmountPaid=c.getColumnIndex("totalAmountPaid");
                int noOfOffences=c.getCount();
                String[] offences=new String[noOfOffences];
                c.moveToFirst();
//                Log.d("chance",   Integer.toString(noOfOffences));
                String fineId=c.getString(fineIDIndex);
                Fine fine=new Fine(c.getString(fineIDIndex),c.getString(finePlaceIndex),c.getString(driverLicenseNoIndex),c.getString(driverVehicleNoIndex),
                        c.getString(fineTimeIndex),c.getString(validUntilDateIndex),c.getString(fineDateIndex),c.getString(policemanIdIndex),Boolean.valueOf(c.getString(fineStatusIndex)),
                        Integer.valueOf(c.getString(totalAmountPaid)));

                for(int j=0;j<noOfOffences;j++){
                    offences[j]=c.getString(offenceIndex);

                    Log.d("chance",c.getString(fineIDIndex)+" "+
                            c.getString(driverLicenseNoIndex)+" "+
                            c.getString(policemanIdIndex)+" "+
                            c.getString(finePlaceIndex)+" "+
                            c.getString(fineTimeIndex)+" "+
                            c.getString(validUntilDateIndex)+" "+
                            c.getString(fineDateIndex)+" "+
                            c.getString(fineStatusIndex)+" "+
                            c.getString(totalAmountPaid)+" "+
                            c.getString(offenceIndex)
                    );

                    c.moveToNext();
                }
                FineOffences fineOffences=new FineOffences(fineId,offences);
                myDb.execSQL("DELETE from fines where fineId='"+fineId+"'");
                myDb.execSQL("DELETE from fineOffences where fid='"+fineId+"'");
                Log.d("chance",fineId);
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

//                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
//                {
//
//                    Toast.makeText(HomeActivity.this,"Sending Fine",Toast.LENGTH_SHORT).show();
//                    try{
//
//                        JSONArray jsArray = new JSONArray(fineOffences.getSectionOfAct());
//
////            String[] fineOffences =offences;
//                        JSONObject jsonParams = new JSONObject();
//                        jsonParams.put("fineId", fine.getFineId());
//                        jsonParams.put("place", fine.getFinePlace());
//                        jsonParams.put("licenseNo", fine.getDriverLicenseNo());
//                        jsonParams.put("vehicleNo", fine.getDriverVehicleNo());
//                        jsonParams.put("offences",jsArray);
//                        jsonParams.put("time", fine.getFineTime());
//                        jsonParams.put("place", fine.getFinePlace());
//                        jsonParams.put("validUntil", fine.getValidUntilDate());
//                        jsonParams.put("date", fine.getFineDate());
//                        jsonParams.put("policemanId", fine.getPolicemanId());
//                        jsonParams.put("fineStatus",fine.getFineStatus());
//                        jsonParams.put("totalAmountPaid",fine.getTotalAmountPaid());
//                        final StringEntity entity = new StringEntity(jsonParams.toString());
//                        Log.d("chance",jsArray.toString());
//
//                        AsyncHttpClient client=new AsyncHttpClient();
//                        client.post(this,MainActivity.API+"fines",entity, "application/json",new JsonHttpResponseHandler(){
//
//                            @Override
//                            public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
//                                try {
//                                    Log.d("chance",response.getString("_id"));
//                                    Cursor cd1=myDb.rawQuery("DELETE  from fines where fineId='"+response.getString("_id")+"'",null);
//                                    Cursor cd=myDb.rawQuery("DELETE  from fineOffences where fid='"+response.getString("_id")+"'",null);
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
//                                Log.d("chance","fail");
//                            }
//                            @Override
//                            public void onRetry(int retryNo) {
//                                Log.d("chance","fail");
//                            }
//
//                        });
//                    }catch (Exception e){
//                        Log.d("chance","exception");
//                        e.printStackTrace();
//                    }
//                }
//                else {
//
//                    connected = false;
//                }


                Log.d("chance", "next fine");
                c1.moveToNext();

            }


        }catch (Exception e){
            Log.d("chance", e.getMessage());
        }
        Log.d("chance", "sent");
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


        boolean x=driverLicenseNo.matches("[A-Z]{1}\\d{7}");

        if(!x)
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

        DriverDetails driver= DriverDetails.getInstance();
        try {

            int catogeriesOfVehiclesLength = jsonObject.getJSONArray("CatogeriesOfVehicles").length();
            ArrayList<String> categoriesList = new ArrayList<String>();
            for (int i=0;i<catogeriesOfVehiclesLength;i++){
                categoriesList.add(jsonObject.getJSONArray("CatogeriesOfVehicles").getString(i));
            }

            int driverFinesListLength = jsonObject.getJSONArray("fines").length();
            ArrayList<DriverDetailsFines> driverFinesList = new ArrayList<DriverDetailsFines>();

            for (int i=0;i<driverFinesListLength;i++){
                driverFinesList.add(new DriverDetailsFines(
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
