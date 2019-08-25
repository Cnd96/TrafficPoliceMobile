package com.example.chamal.trafficpolice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OutboxActivity extends AppCompatActivity {

    Button mSendOutBoxFinesButton;
    private OutboxFinesAdapter adapter;
    private ListView listViewOutBoxFines;
    SQLiteDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbox);
        listViewOutBoxFines=(ListView) findViewById(R.id.ListView_OutboxFines);
        myDb=this.openOrCreateDatabase("Police",MODE_PRIVATE,null);
        myDb.execSQL("CREATE TABLE IF NOT EXISTS fines (fineId VARCHAR,driverLicenseNo VARCHAR,driverVehicleNo VARCHAR," +
                "fineTime VARCHAR,fineDate VARCHAR,policemanId VARCHAR,paidRecordedBy VARCHAR,unpaidRecordedBy VARCHAR,totalAmountPaid INT(6),fineStatus BOOLEAN,finePlace VARCHAR," +
                "validUntilDate VARCHAR,PRIMARY KEY (fineId))");
        myDb.execSQL("CREATE TABLE IF NOT EXISTS fineOffences (fid VARCHAR  NOT NULL,offence VARCHAR NOT NULL,FOREIGN KEY (fid) REFERENCES fines(fineId),PRIMARY KEY (fid, offence))");

        mSendOutBoxFinesButton=(Button) findViewById(R.id.btnSubmitOutBoxFines);
        getOutBoxFines();

        mSendOutBoxFinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    sendFinesToServer();
                    connected = true;
                    Intent homeIntent=new Intent(OutboxActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }
                else {
                    Toast.makeText(OutboxActivity.this,"Offline",Toast.LENGTH_SHORT).show();
                    connected = false;
                    return;
                }

            }
        });
    }


    private void getOutBoxFines(){

        ArrayList<String> licenseNoList=new ArrayList<>();
        ArrayList<String> vehicleNoList=new ArrayList<>();
        ArrayList<String> fineIdList=new ArrayList<>();
        ArrayList<String> dateList=new ArrayList<>();

        try{

            Cursor c1=myDb.rawQuery("SELECT * from fines",null);
            int noOfFines=c1.getCount();
            if(noOfFines ==0){
                Toast.makeText(OutboxActivity.this,"No Outbox",Toast.LENGTH_SHORT).show();
                return;
            }
            int fineIDIndex=c1.getColumnIndex("fineId");
            int driverLicenseNoIndex=c1.getColumnIndex("driverLicenseNo");
            int driverVehicleNoIndex=c1.getColumnIndex("driverVehicleNo");
            int fineDateIndex=c1.getColumnIndex("fineDate");

            c1.moveToFirst();

            for(int i=0;i<noOfFines;i++){
                Log.d("chance", c1.getString(driverLicenseNoIndex));
                licenseNoList.add("License :"+c1.getString(driverLicenseNoIndex));
                vehicleNoList.add("Vehicle :"+c1.getString(driverVehicleNoIndex));
                fineIdList.add(" Fine ID :"+c1.getString(fineIDIndex));
                dateList.add(" Date :"+c1.getString(fineDateIndex));
                c1.moveToNext();
            }
            adapter=new OutboxFinesAdapter(this,fineIdList,licenseNoList,dateList,vehicleNoList);
            listViewOutBoxFines.setAdapter(adapter);

        }catch (Exception e){
            Log.d("chance", e.getMessage());
        }
        Log.d("chance", "sent");
    }

    private void sendFinesToServer(){
        try{

            Cursor c1=myDb.rawQuery("SELECT fineId from fines",null);
            int noOfFines=c1.getCount();
            Log.d("chance",  "1");
            if(noOfFines ==0){
                return;
            }
            int fineIdIndexofFines=c1.getColumnIndex("fineId");
            c1.moveToFirst();
            Log.d("chance",  "2");
            for(int i=0;i<noOfFines;i++){
                Cursor c=myDb.rawQuery("SELECT f.fineId, f.finePlace,f.driverLicenseNo,f.driverVehicleNo,f.fineTime" +
                        ",f.validUntilDate,f.fineDate,f.policemanId,f.fineStatus,f.totalAmountPaid,f.paidRecordedBy,f.unpaidRecordedBy ,fo.offence\n" +
                        "FROM fines f INNER JOIN fineOffences fo ON f.fineId = fo.fid where f.fineId='"+c1.getString(fineIdIndexofFines)+"'",null);

                int fineIDIndex=c.getColumnIndex("fineId");
                int finePlaceIndex=c.getColumnIndex("finePlace");
                int driverLicenseNoIndex=c.getColumnIndex("driverLicenseNo");
                int driverVehicleNoIndex=c.getColumnIndex("driverVehicleNo");
                int fineTimeIndex=c.getColumnIndex("fineTime");
                int policemanIdIndex=c.getColumnIndex("policemanId");
                int unpaidRecordedByIndex=c.getColumnIndex("unpaidRecordedBy");
                int paidRecordedByIndex=c.getColumnIndex("paidRecordedBy");
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
                Fine fine=new Fine(c.getString(fineIDIndex),c.getString(driverLicenseNoIndex),c.getString(driverVehicleNoIndex),c.getString(finePlaceIndex),
                        c.getString(fineTimeIndex),c.getString(validUntilDateIndex),c.getString(fineDateIndex),c.getString(policemanIdIndex),Boolean.valueOf(c.getString(fineStatusIndex)),
                        Integer.valueOf(c.getString(totalAmountPaid)),c.getString(paidRecordedByIndex),c.getString(unpaidRecordedByIndex));

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
//                myDb.execSQL("DELETE from fines where fineId='"+fineId+"'");
//                myDb.execSQL("DELETE from fineOffences where fid='"+fineId+"'");
                Log.d("chance",fineId);




                    Toast.makeText(OutboxActivity.this,"Sending Fines",Toast.LENGTH_SHORT).show();
                    try{

                        JSONArray jsArray = new JSONArray(fineOffences.getSectionOfAct());

//            String[] fineOffences =offences;
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("fineId", fine.getFineId());
                        jsonParams.put("place", fine.getFinePlace());
                        jsonParams.put("licenseNo", fine.getDriverLicenseNo());
                        jsonParams.put("vehicleNo", fine.getDriverVehicleNo());
                        jsonParams.put("offences",jsArray);
                        jsonParams.put("time", fine.getFineTime());
                        jsonParams.put("place", fine.getFinePlace());
                        jsonParams.put("validUntil", fine.getValidUntilDate());
                        jsonParams.put("date", fine.getFineDate());
                        jsonParams.put("paidDate", fine.getFineDate());
                        jsonParams.put("policemanId", fine.getPolicemanId());
                        jsonParams.put("paidRecordedBy", fine.getPaidRecordedBy());
                        jsonParams.put("unpaidRecordedBy", fine.getUnpaidRecordedBy());
                        jsonParams.put("fineStatus",fine.getFineStatus());
                        jsonParams.put("totalAmountPaid",fine.getTotalAmountPaid());
                        final StringEntity entity = new StringEntity(jsonParams.toString());
                        Log.d("chance",jsArray.toString());

                        AsyncHttpClient client=new AsyncHttpClient();
                        client.post(this,MainActivity.PhoneIP +"fines",entity, "application/json",new JsonHttpResponseHandler(){

                            @Override
                            public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                try {
                                    Log.d("chance",response.getString("_id"));
                                    myDb.execSQL("DELETE from fines where fineId='"+ response.getString("_id")+"'");
                                    myDb.execSQL("DELETE from fineOffences where fid='"+ response.getString("_id")+"'");
                                    Toast.makeText(OutboxActivity.this,"Successfully send"+response.getString("_id"),Toast.LENGTH_SHORT).show();
                                }catch (JSONException e){
                                    e.printStackTrace();

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                                Log.d("chance","fail");
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




                Log.d("chance", "next fine");
                c1.moveToNext();

            }


        }catch (Exception e){
            Log.d("chance", e.getMessage());
        }
        Log.d("chance", "sent");
    }
}
