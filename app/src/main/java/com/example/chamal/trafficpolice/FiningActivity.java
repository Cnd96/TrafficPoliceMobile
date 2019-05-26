package com.example.chamal.trafficpolice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class FiningActivity extends AppCompatActivity {

    EditText mDriverVehicleNo;
    EditText mDriverLicenseNo;
    EditText mFineId;
    EditText mFinePlace;
    TextView mFineDate;
    TextView mFineTime;
    Button mFineSubmitButton;
    private ListView listViewOffences;
    private FineOffencesAdapter adapter;
    CheckBox mcheckBoxFineStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fining);

        mDriverLicenseNo=(EditText) findViewById(R.id.labelFineDriverLicenseNo);
        mDriverVehicleNo=(EditText) findViewById(R.id.labelFineVehicleNo);
        mFineId=(EditText) findViewById(R.id.labelFineID);
        mFinePlace=(EditText) findViewById(R.id.labelFinePlace);
        mFineDate=(TextView) findViewById(R.id.labelFineDate);
        mFineTime=(TextView) findViewById(R.id.labelFineTime);
        mFineSubmitButton=(Button) findViewById(R.id.btnSubmitFine);
        listViewOffences=(ListView) findViewById(R.id.ListView_Offences);
        mcheckBoxFineStatus=(CheckBox) findViewById(R.id.PaidUnpaidCheckBox);

        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String fineDate=dateFormat.format(date);
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat=new SimpleDateFormat("HH:mm");
        String time= timeFormat.format(date);
        mFineDate.setText(fineDate);
        mFineTime.setText(time);

        ArrayList<String> amountList=new ArrayList<>();
        ArrayList<String> provisionList=new ArrayList<>();


        Intent myIntent=getIntent();
        final String[] offences=myIntent.getStringArrayExtra("offences");
//        Log.d("chance","offences4 success");

        for (int i=0; i <offences.length;i++){
            if(OffencesModel.offenceMapping.containsKey(offences[i])) {
                Log.d("chance",OffencesModel.offenceMapping.get(offences[i]).getOffence());
                provisionList.add(OffencesModel.offenceMapping.get(offences[i]).getOffence());
                amountList.add("Rs."+Integer.toString(OffencesModel.offenceMapping.get(offences[i]).getAmount()));
            }
        }
//        Log.d("chance","offences5 success");

        adapter=new FineOffencesAdapter(this,provisionList,amountList);
        listViewOffences.setAdapter(adapter);

        mFineSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFineDetais(offences);
            }
        });
    }

    private void submitFineDetais(String[] offences){
        SharedPreferences prefs=getSharedPreferences(MainActivity.MAIN_PREFS,MODE_PRIVATE);
        int totalAmountPaid=0;
        Boolean fineStatus=mcheckBoxFineStatus.isChecked();
        String driverLicenseNo=mDriverLicenseNo.getText().toString();
        String driverVehicleNo=mDriverVehicleNo.getText().toString();
        String fineTime=mFineTime.getText().toString();
        String fineDate=mFineDate.getText().toString();
        String finePlace=mFinePlace.getText().toString();
        String fineId=mFineId.getText().toString();
        String policemanId=prefs.getString(MainActivity.POLICEMAN_ID_KEY,null);
        Log.d("chance",fineTime);


        SimpleDateFormat validUntilDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{

            c.setTime(validUntilDateFormat.parse(fineDate));
        }catch(Exception e){
            e.printStackTrace();
        }


        c.add(Calendar.DAY_OF_MONTH, 28);
        String validUntilDate = validUntilDateFormat.format(c.getTime());
        Log.d("chance",fineDate);
        Log.d("chance",validUntilDate);

        boolean fineIdValidity=fineId.matches("\\d{4,7}");

        if(!fineIdValidity)
        {
            Toast.makeText(FiningActivity.this,"Wrong Fine Id pattern",Toast.LENGTH_SHORT).show();
            return;
        };

        boolean licensNoValidity=driverLicenseNo.matches("[A-Z]{1}\\d{7}");

        if(!licensNoValidity)
        {
            Toast.makeText(FiningActivity.this,"Wrong license number",Toast.LENGTH_SHORT).show();
            return;
        };

        boolean vehicleValidity1=driverVehicleNo.matches("[A-Za-z]{2}-[A-Za-z]{2}-\\d{4}");
        boolean vehicleValidity2=driverVehicleNo.matches("[A-Za-z]{2}-[A-Za-z]{3}-\\d{4}");

        if(!(vehicleValidity1||vehicleValidity2))
        {
            Toast.makeText(FiningActivity.this,"Wrong vehicle number",Toast.LENGTH_SHORT).show();
            return;
        };

        if(finePlace.length()==0)
        {
            Toast.makeText(FiningActivity.this,"Enter a place",Toast.LENGTH_SHORT).show();
            return;
        };


        if(mcheckBoxFineStatus.isChecked()){
            for (int i=0; i <offences.length;i++) {
                totalAmountPaid+=OffencesModel.offenceMapping.get(offences[i]).getAmount();
            }
        }


        Fine fine=new Fine(fineId,finePlace,driverLicenseNo,driverVehicleNo,fineTime,validUntilDate,fineDate,policemanId,fineStatus,totalAmountPaid);
        FineOffences fineOffences=new FineOffences(fineId,offences);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Toast.makeText(FiningActivity.this,"Sending Fine",Toast.LENGTH_SHORT).show();
            sendFineToServer(fine,fineOffences);
            connected = true;
        }
        else {
            Toast.makeText(FiningActivity.this,"Offline",Toast.LENGTH_SHORT).show();
            saveFineInOutBox(fine,fineOffences);
            connected = false;
        }



    }

    private void sendFineToServer(Fine fine,FineOffences fineOffences){
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
            jsonParams.put("policemanId", fine.getPolicemanId());
            jsonParams.put("fineStatus",fine.getFineStatus());
            jsonParams.put("totalAmountPaid",fine.getTotalAmountPaid());
            StringEntity entity = new StringEntity(jsonParams.toString());
            Log.d("chance",jsArray.toString());

            AsyncHttpClient client=new AsyncHttpClient();
            client.post(this,"http://192.168.8.135:3000/api/fines",entity, "application/json",new JsonHttpResponseHandler(){
                @Override
                public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    Log.d("chance",response.toString());
                    Toast.makeText(FiningActivity.this,"Recorded successfully", Toast.LENGTH_SHORT).show();
                    Intent homeIntent=new Intent(FiningActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
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
    }

    private void saveFineInOutBox(Fine fine,FineOffences fineOffences){
        try{
            Log.d("chance","out1");
            String[] offences=fineOffences.getSectionOfAct();

            Log.d("chance",fine.getFineId());
            final SQLiteDatabase myDb=this.openOrCreateDatabase("Police",MODE_PRIVATE,null);
            myDb.execSQL("CREATE TABLE IF NOT EXISTS fines (fineId VARCHAR,driverLicenseNo VARCHAR,driverVehicleNo VARCHAR,fineTime VARCHAR,fineDate VARCHAR,policemanId VARCHAR,totalAmountPaid INT(6),fineStatus BOOLEAN,finePlace VARCHAR,validUntilDate VARCHAR,PRIMARY KEY (fineId))");
            myDb.execSQL("CREATE TABLE IF NOT EXISTS fineOffences (fid VARCHAR  NOT NULL,offence VARCHAR NOT NULL,FOREIGN KEY (fid) REFERENCES fines(fineId),PRIMARY KEY (fid, offence))");
            Log.d("chance","out2");

            myDb.execSQL("INSERT INTO fines (fineId,driverLicenseNo,driverVehicleNo,fineTime,fineDate,policemanId,totalAmountPaid,fineStatus,finePlace,validUntilDate) VALUES " +
                    "('"+fine.getFineId()+"','"+fine.getDriverLicenseNo()+"','"+fine.getDriverVehicleNo()+"','"+fine.getFineTime()+"','"+fine.getFineDate()+"','"+fine.getPolicemanId()+"','"+fine.getTotalAmountPaid()+"','"+fine.getFineStatus()+"','"+fine.getFinePlace()+"','"+fine.getValidUntilDate()+"')");

            Log.d("chance","out3");
            for (int i=0; i <offences.length;i++) {
                myDb.execSQL("INSERT INTO fineOffences (fid,offence) VALUES ('" + fineOffences.getFineId() + "','" + offences[i] + "')");
            }
            Log.d("chance","out4");
            Toast.makeText(FiningActivity.this,"Recorded in outbox", Toast.LENGTH_SHORT).show();
            Intent homeIntent=new Intent(FiningActivity.this,HomeActivity.class);
            startActivity(homeIntent);
            Log.d("chance","out5");

        }catch (Exception e){
            Toast.makeText(FiningActivity.this,"Error saving", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
