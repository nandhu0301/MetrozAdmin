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

import java.util.List;

public class AssignOrderAdapter extends ArrayAdapter<OrderDetails>
{
    Activity context;
    List<OrderDetails> billDetailsList;
    TextView orderId, storeName, placedDate, orderStatus, assigenedToStatus;

    public AssignOrderAdapter(@NonNull Activity context, List<OrderDetails> billDetailsList) {
        super ( context, R.layout.order_details_layout, billDetailsList );
        this.context = context;
        this.billDetailsList = billDetailsList;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater ();
        View listview = inflater.inflate ( R.layout.order_details_layout, null, true );

        orderId = listview.findViewById ( R.id.orderId );
        storeName = listview.findViewById ( R.id.storeName );
        placedDate = listview.findViewById ( R.id.placedDate );
        orderStatus = listview.findViewById ( R.id.Status );
        assigenedToStatus = listview.findViewById ( R.id.AssignedTo );

        final OrderDetails billDetails = billDetailsList.get ( position );

        orderId.setText (String.valueOf (  billDetails.getOrderId ()) );
        storeName.setText ( billDetails.getStoreName ());
        placedDate.setText ( billDetails.getPaymentDate ());
        orderStatus.setText ( billDetails.getOrderStatus () );
        assigenedToStatus.setText ( billDetails.getAssignedTo () );
        return listview;
    }
}
