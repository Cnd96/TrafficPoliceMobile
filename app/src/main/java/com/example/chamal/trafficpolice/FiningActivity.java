package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    TextView mFineDate;
    TextView mFineTime;
    Button mFineSubmitButton;
    private ListView listViewOffences;
    private FineOffencesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fining);

        mDriverLicenseNo=(EditText) findViewById(R.id.labelFineDriverLicenseNo);
        mDriverVehicleNo=(EditText) findViewById(R.id.labelFineVehicleNo);
        mFineDate=(TextView) findViewById(R.id.labelFineDate);
        mFineTime=(TextView) findViewById(R.id.labelFineTime);
        mFineSubmitButton=(Button) findViewById(R.id.btnSubmitFine);
        listViewOffences=(ListView) findViewById(R.id.ListView_Offences);

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
        Log.d("chance",offences[0]);
        String driverLicenseNo=mDriverLicenseNo.getText().toString();
        String driverVehicleNo=mDriverVehicleNo.getText().toString();
        String fineTime=mFineTime.getText().toString();
        String fineDate=mFineDate.getText().toString();
        Log.d("chance",fineTime);
        try{
            JSONArray jsArray = new JSONArray(offences);

            String[] fineOffences =offences;
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("licenseNo", driverLicenseNo);
            jsonParams.put("vehicleNo", driverVehicleNo);
            jsonParams.put("offences",jsArray);
            jsonParams.put("time", fineTime);
            jsonParams.put("date", fineDate);
            jsonParams.put("fineStatus", 0);
            jsonParams.put("policemanId", "10002");

            StringEntity entity = new StringEntity(jsonParams.toString());
            Log.d("chance",jsonParams.toString());

            AsyncHttpClient client=new AsyncHttpClient();
            client.post(this,"http://192.168.8.135:3000/api/fines",entity, "application/json",new JsonHttpResponseHandler(){
                @Override
                public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    Log.d("chance",response.toString());
                    Toast.makeText(FiningActivity.this,"Recorded successfully", Toast.LENGTH_SHORT).show();

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
}
