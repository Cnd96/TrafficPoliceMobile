package com.example.chamal.trafficpolice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class fineSendingThread implements Runnable {
    Fine fine;
    FineOffences fineOffences;
    private Context context;


    public fineSendingThread(Fine fine,FineOffences fineOffences,Context context) {
        this.fine = fine;
        this.fineOffences = fineOffences;
        this.context = context;
    }

    @Override
    public void run() {
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
            client.post(this.context,MainActivity.API+"fines",entity, "application/json",new JsonHttpResponseHandler(){
                @Override
                public  void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    Log.d("chance",response.toString());
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
