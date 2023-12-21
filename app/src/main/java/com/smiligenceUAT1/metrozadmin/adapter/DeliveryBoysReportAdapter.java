package com.smiligenceUAT1.metrozadmin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;

import java.util.ArrayList;


public class DeliveryBoysReportAdapter extends ArrayAdapter<DeliveryBoy> {
    public Activity context;
    public ArrayList<DeliveryBoy> billreportsList;

    TextView deliveryboyName,deliveryBoyContact;
    ImageView profile;

    public DeliveryBoysReportAdapter(Activity context, ArrayList<DeliveryBoy> billreportsList) {
        super(context, R.layout.reportlist, billreportsList);
        this.context = context;
        this.billreportsList = billreportsList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listview = inflater.inflate(R.layout.delivery_reportlist, null, true);

        profile = (ImageView) listview.findViewById(R.id.image);
        deliveryboyName = (TextView) listview.findViewById(R.id.name);
        deliveryBoyContact = (TextView) listview.findViewById(R.id.contact);



        if(billreportsList != null && !billreportsList.isEmpty()) {
            DeliveryBoy deliveryBoy = billreportsList.get(position);
            Glide.with ( context ).load ( deliveryBoy.getDeliveryBoyProfile () ).into ( profile );
            deliveryboyName.setText(deliveryBoy.getFirstName()+" "+deliveryBoy.getLastName());
            deliveryBoyContact.setText(deliveryBoy.getMobileNumber());
        }


        return listview;
    }
}
