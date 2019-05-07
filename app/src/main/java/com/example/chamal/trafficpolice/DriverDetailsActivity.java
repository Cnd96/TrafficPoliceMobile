package com.example.chamal.trafficpolice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DriverDetailsActivity extends AppCompatActivity {

    TextView mDriverName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        DriverDetailsModel driver=DriverDetailsModel.getInstance();
        mDriverName=(TextView) findViewById(R.id.labelDriverName);

        mDriverName.setText(driver.getName());
    }
}
