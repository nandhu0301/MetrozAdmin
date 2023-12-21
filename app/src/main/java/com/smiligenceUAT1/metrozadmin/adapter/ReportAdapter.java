package com.smiligenceUAT1.metrozadmin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;

import java.util.ArrayList;


public class ReportAdapter extends ArrayAdapter<OrderDetails> {
    public Activity context;

    public ArrayList<OrderDetails> billreportsList;

    TextView itemCount, qty, Total_price, Date_time, billnum, discount;

    public ReportAdapter(Activity context, ArrayList<OrderDetails> billreportsList) {
        super(context, R.layout.reportlist, billreportsList);
        this.context = context;
        this.billreportsList = billreportsList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listview = inflater.inflate(R.layout.reportlist, null, true);

        Date_time = (TextView) listview.findViewById(R.id.Date);
        itemCount = (TextView) listview.findViewById(R.id.item);
        qty = (TextView) listview.findViewById(R.id.qty);
        Total_price = listview.findViewById(R.id.f_b_amount);
        billnum = listview.findViewById(R.id.billNo);


        if(billreportsList != null && !billreportsList.isEmpty()) {
            OrderDetails billDetails = billreportsList.get(position);
            Date_time.setText(billDetails.getPaymentDate());
            qty.setText(String.valueOf(billDetails.getItemDetails().getItemQuantity()));
            itemCount.setText(String.valueOf(billDetails.getTotalItem()));
            Total_price.setText(String.valueOf(billDetails.getTotalAmount()));
            billnum.setText(String.valueOf(billDetails.getOrderId()));
        }


        return listview;
    }
}
