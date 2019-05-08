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

public class FineOffencesAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> provision;
    private ArrayList<String> amount;

    public FineOffencesAdapter(@NonNull Activity context, ArrayList<String> provision,ArrayList<String> amount) {
        super(context, R.layout.offence_row,provision);
        this.context = context;
        this.provision=provision;
        this.amount=amount;
    }

    class ViewHolder{
        TextView tvw1;
        TextView tvw2;

        ViewHolder(View v){
            tvw1=(TextView) v.findViewById(R.id.row_provisionDescription);
            tvw2=(TextView) v.findViewById(R.id.row_offenceAmount);
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.offence_row,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) r.getTag();
        }

        viewHolder.tvw1.setText(provision.get(position));
        viewHolder.tvw2.setText(amount.get(position));
        return r;
    }
}
