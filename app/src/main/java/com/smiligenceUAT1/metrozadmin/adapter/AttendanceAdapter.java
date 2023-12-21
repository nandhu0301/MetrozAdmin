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
import com.smiligenceUAT1.metrozadmin.bean.CheckIn_CheckOut;

import java.util.List;

public class AttendanceAdapter extends ArrayAdapter<CheckIn_CheckOut> {

    Activity context;
    List<CheckIn_CheckOut> billDetailsList;
    TextView date, checkInOutTime, intervalTime;

    public AttendanceAdapter(@NonNull Activity context, List<CheckIn_CheckOut> billDetailsList) {
        super ( context, R.layout.attendance_detail_layout, billDetailsList );
        this.context = context;
        this.billDetailsList = billDetailsList;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater ();
        View listview = inflater.inflate ( R.layout.attendance_detail_layout, null, true );

        date = (TextView) listview.findViewById ( R.id.date );
        checkInOutTime = (TextView) listview.findViewById ( R.id.checkInTime );
        intervalTime = (TextView) listview.findViewById ( R.id.checkOutTime );


        final CheckIn_CheckOut billDetails = billDetailsList.get ( position );


        date.setText ( String.valueOf ( billDetails.getCheckInDate () ) );
        checkInOutTime.setText ( billDetails.getTotalBreakTime () );
        intervalTime.setText ( billDetails.getTotalCheckInCheckOutTime () );

        return listview;
    }
}
