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


public class DeliveryBoysOrderDetailsAdapter extends ArrayAdapter<OrderDetails> {
    public Activity context;
    public ArrayList<OrderDetails> billreportsList;

    TextView storeName,orderID,categoryName,billDate;


    public DeliveryBoysOrderDetailsAdapter(Activity context, ArrayList<OrderDetails> billreportsList) {
        super(context, R.layout.reportlist, billreportsList);
        this.context = context;
        this.billreportsList = billreportsList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.deliveryboy_orderslist, null, true);

        storeName =(TextView) listview.findViewById(R.id.storename);
        orderID = (TextView) listview.findViewById(R.id.orderno);
        categoryName = (TextView) listview.findViewById(R.id.order_delivery_type);
        billDate = (TextView) listview.findViewById(R.id.billeddate);



        if(billreportsList != null && !billreportsList.isEmpty()) {
            OrderDetails orderDetails = billreportsList.get(position);
           storeName.setText(orderDetails.getStoreName());
            orderID.setText(orderDetails.getOrderId());
            if("1".equalsIgnoreCase(orderDetails.getCategoryTypeId())){
                categoryName.setText("Delivered from Store");
            }else if("2".equalsIgnoreCase(orderDetails.getCategoryTypeId())){
                categoryName.setText("Pickup and Drop");
            }
            billDate.setText(orderDetails.getPaymentDate());

        }


        return listview;
    }
}
