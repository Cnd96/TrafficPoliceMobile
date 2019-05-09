package com.example.chamal.trafficpolice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DriverDetailsActivity extends AppCompatActivity {

    TextView mDriverName;
    TextView mDriverLicenseNo;
    TextView mLicenseExpireDate;
    TextView mLicenseIssuedDate;
    TextView mCategoriesOfVehicle;
    TextView mTotalPaidFines;
    TextView mUnpaidCourtFineId;
    TextView mUnpaidCourtAmount;
    TextView mUnpaidCourtDate;

    TextView mUnpaidCourtFineIdText;
    TextView mUnpaidCourtAmountText;
    TextView mUnpaidCourtDateText;
    TextView mUnpaidCourtTitle;



    private DriverDetailsFinesAdapter adapter;
    private ListView listViewFiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        String unpaidCourtFineId="";
        String unpaidCourtAmount="";
        String unpaidCourtDate="";
        String txtUnpaidCourtFineId="";
        String txtUnpaidCourtAmount="";
        String txtUnpaidCourtDate="";
        String unpaidCourtTitle="No unpaid fines or court cases";

        DriverDetailsModel driver=DriverDetailsModel.getInstance();

        listViewFiness=(ListView) findViewById(R.id.ListView_PaidFines);
        mDriverName=(TextView) findViewById(R.id.labelDriverName);
        mDriverLicenseNo=(TextView) findViewById(R.id.labelDriverLicenseNo);
        mLicenseExpireDate=(TextView) findViewById(R.id.labelExpire);
        mLicenseIssuedDate=(TextView) findViewById(R.id.labelIssued);
        mCategoriesOfVehicle=(TextView) findViewById(R.id.labelCategories);
        mTotalPaidFines=(TextView) findViewById(R.id.LabelPaidFinesTotal);

        mUnpaidCourtFineId=(TextView) findViewById(R.id.labelUnpaidCourtFineId);
        mUnpaidCourtAmount=(TextView) findViewById(R.id.labelUnpaidCourtAmount);
        mUnpaidCourtDate=(TextView) findViewById(R.id.labelUnpaidCourtDate);

        mUnpaidCourtFineIdText=(TextView) findViewById(R.id.txtUnpaidCourFineID);
        mUnpaidCourtAmountText=(TextView) findViewById(R.id.txtUnpaidCourtAmount);
        mUnpaidCourtDateText=(TextView) findViewById(R.id.txtUnpaidCourtDate);

        mUnpaidCourtTitle=(TextView) findViewById(R.id.txtUnpaidCourtTitle);

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


        ArrayList<DriverDetailsFinesModel> driverFinesList=driver.getDriverFinesList();
        int totalPaidFines=driverFinesList.size();
        Log.d("chance","okayy");
        ArrayList<String> amountList=new ArrayList<>();
        ArrayList<String> fineIdList=new ArrayList<>();
        ArrayList<String> dateList=new ArrayList<>();
        for (int i=0; i <driverFinesList.size();i++){
            if(!driverFinesList.get(i).isFineStatus()){
                unpaidCourtAmount=Integer.toString(driverFinesList.get(i).getAmount());
                unpaidCourtFineId=Integer.toString(driverFinesList.get(i).getId());
                unpaidCourtDate= driverFinesList.get(i).getDate();
                unpaidCourtTitle="Unpaid Fine";
                txtUnpaidCourtAmount="Amount :";
                txtUnpaidCourtDate="Date :";
                txtUnpaidCourtFineId="Fine ID:";
                totalPaidFines--;
            }else {
                amountList.add("Amount :Rs "+Integer.toString(driverFinesList.get(i).getTotalAmountPaid()));
                fineIdList.add("Fine ID :"+Integer.toString(driverFinesList.get(i).getId()));
                dateList.add("Date :"+driverFinesList.get(i).getDate());
            }

        }
        Log.d("chance","okayyy2");
        mUnpaidCourtAmount.setText(unpaidCourtAmount);
        mUnpaidCourtFineId.setText(unpaidCourtFineId);
        mUnpaidCourtDate.setText(unpaidCourtDate);
        mUnpaidCourtTitle.setText(unpaidCourtTitle);
        mUnpaidCourtFineIdText.setText(txtUnpaidCourtFineId);
        mUnpaidCourtDateText.setText(txtUnpaidCourtDate);
        mUnpaidCourtAmountText.setText(txtUnpaidCourtAmount);
        mTotalPaidFines.setText(""+totalPaidFines);

        adapter=new DriverDetailsFinesAdapter(this,fineIdList,amountList,dateList);
        listViewFiness.setAdapter(adapter);
    }
}
