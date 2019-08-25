package com.example.chamal.trafficpolice;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OutboxFinesAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> fineId;
    private ArrayList<String> licenseNo;
    private ArrayList<String> date;
    private ArrayList<String> vehicleNo;


    public OutboxFinesAdapter(@NonNull  Activity context, ArrayList<String> fineId, ArrayList<String> licenseNo, ArrayList<String> date, ArrayList<String> vehicleNo) {
        super(context,R.layout.outboxfine_row,fineId);
        this.context = context;
        this.fineId=fineId;
        this.licenseNo=licenseNo;
        this.date=date;
        this.vehicleNo=vehicleNo;
    }

    class ViewHolder{
        TextView textViewFineId;
        TextView textViewLicenseNo;
        TextView textViewDate;
        TextView textViewVehicleNO;

        ViewHolder(View v){
            textViewFineId=(TextView) v.findViewById(R.id.row_outBoxFineId);
            textViewLicenseNo=(TextView) v.findViewById(R.id.row_outBoxLicenseNo);
            textViewDate=(TextView) v.findViewById(R.id.row_outBoxdate);
            textViewVehicleNO=(TextView) v.findViewById(R.id.row_outBoxVehicleNo);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        OutboxFinesAdapter.ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.outboxfine_row,null,true);
            viewHolder=new OutboxFinesAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(OutboxFinesAdapter.ViewHolder) r.getTag();
        }

        viewHolder.textViewFineId.setText(fineId.get(position));
        viewHolder.textViewLicenseNo.setText(licenseNo.get(position));
        viewHolder.textViewDate.setText(date.get(position));
        viewHolder.textViewVehicleNO.setText(vehicleNo.get(position));
        return r;
    }
}
