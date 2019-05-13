package com.example.chamal.trafficpolice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class OffencesActivity extends AppCompatActivity {
    CheckBox[] mcheckBox=new CheckBox[13];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offences);
        initializeCheckBoxes();
        Button submitButton=(Button) findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selectedOffencesList=  getSelectedOffencesList();
//                Log.d("chance","offences success");
                String[] offences=new String[selectedOffencesList.size()];
                for(int i=0;i<selectedOffencesList.size();i++){
                    offences[i]=selectedOffencesList.get(i);
                };
//                Log.d("chance","offences2 success");
                Intent finingIntent=new Intent(OffencesActivity.this,FiningActivity.class);
                finingIntent.putExtra("offences",offences);
//                Log.d("chance","offences3 success");
                startActivity(finingIntent);
            }
        });
    }


    private ArrayList<String> getSelectedOffencesList(){
        ArrayList<String> offencesList=new ArrayList<String>();
        for (int i=0;i<mcheckBox.length;i++){
            if(mcheckBox[i].isChecked()){
                String offence=mcheckBox[i].getContentDescription().toString();
                offencesList.add(offence);
            }
        };

        return offencesList;

    }
    private void initializeCheckBoxes(){
        mcheckBox[0]=(CheckBox) findViewById(R.id.checkBox0);
        mcheckBox[1]=(CheckBox) findViewById(R.id.checkBox1);
        mcheckBox[2]=(CheckBox) findViewById(R.id.checkBox2);
        mcheckBox[3]=(CheckBox) findViewById(R.id.checkBox3);
        mcheckBox[4]=(CheckBox) findViewById(R.id.checkBox4);
        mcheckBox[5]=(CheckBox) findViewById(R.id.checkBox5);
        mcheckBox[6]=(CheckBox) findViewById(R.id.checkBox6);
        mcheckBox[7]=(CheckBox) findViewById(R.id.checkBox7);
        mcheckBox[8]=(CheckBox) findViewById(R.id.checkBox8);
        mcheckBox[9]=(CheckBox) findViewById(R.id.checkBox9);
        mcheckBox[10]=(CheckBox) findViewById(R.id.checkBox10);
        mcheckBox[11]=(CheckBox) findViewById(R.id.checkBox11);
        mcheckBox[12]=(CheckBox) findViewById(R.id.checkBox12);
    }

}
