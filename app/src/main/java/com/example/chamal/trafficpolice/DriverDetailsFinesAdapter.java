package com.example.chamal.trafficpolice;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DriverDetailsFinesAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> fineId;
    private ArrayList<String> amount;
    private ArrayList<String> date;

    public DriverDetailsFinesAdapter(@NonNull Activity context, ArrayList<String> fineId, ArrayList<String> amount, ArrayList<String> date) {
        super(context, R.layout.fine_row,fineId);
        this.context = context;
        this.fineId=fineId;
        this.amount=amount;
        this.date=date;
    }

    class ViewHolder{
        TextView textViewId;
        TextView textViewAmount;
        TextView textViewDate;

        ViewHolder(View v){
            textViewId=(TextView) v.findViewById(R.id.row_fineId);
            textViewAmount=(TextView) v.findViewById(R.id.row_amount);
            textViewDate=(TextView) v.findViewById(R.id.row_date);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        DriverDetailsFinesAdapter.ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.fine_row,null,true);
            viewHolder=new DriverDetailsFinesAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(DriverDetailsFinesAdapter.ViewHolder) r.getTag();
        }

        viewHolder.textViewId.setText(fineId.get(position));
        viewHolder.textViewAmount.setText(amount.get(position));
        viewHolder.textViewDate.setText(date.get(position));
        return r;
    }
}
