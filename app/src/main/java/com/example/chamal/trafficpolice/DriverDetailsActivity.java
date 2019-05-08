package com.example.chamal.trafficpolice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class DriverDetailsActivity extends AppCompatActivity {

    TextView mDriverName;
    TextView mDriverLicenseNo;
    TextView mLicenseExpireDate;
    TextView mLicenseIssuedDate;
    TextView mCategoriesOfVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        DriverDetailsModel driver=DriverDetailsModel.getInstance();

        mDriverName=(TextView) findViewById(R.id.labelDriverName);
        mDriverLicenseNo=(TextView) findViewById(R.id.labelDriverLicenseNo);
        mLicenseExpireDate=(TextView) findViewById(R.id.labelExpire);
        mLicenseIssuedDate=(TextView) findViewById(R.id.labelIssued);
        mCategoriesOfVehicle=(TextView) findViewById(R.id.labelCategories);

        mDriverName.setText(driver.getName());
        mDriverLicenseNo.setText(driver.getLicenseNO());
        mLicenseExpireDate.setText(driver.getDateOfExpire());
        mLicenseIssuedDate.setText(driver.getDateOfIssue());

        ArrayList<String> categoriesList=driver.getCatogeriesOfVehicles();
        String categoriesListString=categoriesList.get(0);

        for (int i=1;i<categoriesList.size();i++){
            categoriesListString= categoriesListString+" , "+categoriesList.get(i);
        }
        mCategoriesOfVehicle.setText(categoriesListString);
    }
}
