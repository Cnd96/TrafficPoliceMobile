package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FiningActivity extends AppCompatActivity {

    private ListView listViewOffences;
    private FineOffencesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fining);

        listViewOffences=(ListView) findViewById(R.id.ListView_Offences);

        ArrayList<String> amountList=new ArrayList<>();
        ArrayList<String> provisionList=new ArrayList<>();


        Intent myIntent=getIntent();
        String[] offences=myIntent.getStringArrayExtra("offences");
        Log.d("chance","fine"+offences[0]);

        for (int i=0; i <offences.length;i++){
            if(OffencesModel.offenceMapping.containsKey(offences[i])) {
                Log.d("chance",OffencesModel.offenceMapping.get(offences[i]).getOffence());
                provisionList.add(OffencesModel.offenceMapping.get(offences[i]).getOffence());
                amountList.add("Rs."+Integer.toString(OffencesModel.offenceMapping.get(offences[i]).getAmount()));
            }
        }


        adapter=new FineOffencesAdapter(this,provisionList,amountList);
        listViewOffences.setAdapter(adapter);


    }
}
