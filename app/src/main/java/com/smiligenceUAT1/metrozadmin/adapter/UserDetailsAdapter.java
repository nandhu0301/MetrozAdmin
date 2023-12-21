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
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;

import java.util.List;

public class UserDetailsAdapter extends ArrayAdapter<UserDetails> {
    Activity context;
    List<UserDetails> userDetailsList;
    TextView firstName, lastName, phoneNumber;

    public UserDetailsAdapter(@NonNull Activity context, List<UserDetails> userDetailsList) {
        super ( context, R.layout.user_deatails_layout, userDetailsList );
        this.context = context;
        this.userDetailsList = userDetailsList;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater ();
        View listview = inflater.inflate ( R.layout.user_deatails_layout, null, true );

        firstName = listview.findViewById ( R.id.firstName );
        lastName = listview.findViewById ( R.id.lastName );
        phoneNumber = listview.findViewById ( R.id.phoneNumber );


        final UserDetails userDetails = userDetailsList.get ( position );

        firstName.setText ( userDetails.getFirstName () );
        lastName.setText ( userDetails.getLastName () );
        phoneNumber.setText ( userDetails.getPhoneNumber () );


        return listview;
    }
}
