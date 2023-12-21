package com.smiligenceUAT1.metrozadmin.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;

import java.util.HashMap;
import java.util.List;

public class DeliveryBoyListingAdapter extends BaseExpandableListAdapter {

    private Context context;

    private List<String> expandableListTitle;
    private HashMap<String, List<DeliveryBoy>> expandableBillDetail;
    String cus_name, billNum, finalAmount, timeStamp, paymentMode, orderStatusText,categoryTypeId;

    public DeliveryBoyListingAdapter(Context context, List<String> expandableListTitle,
                                     HashMap<String, List<DeliveryBoy>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableBillDetail = expandableListDetail;


    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {


        return this.expandableBillDetail.get ( this.expandableListTitle.get ( listPosition ) )
                .get ( expandedListPosition );

    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            convertView = layoutInflater.inflate ( R.layout.list_of_item_store, null );
        }
        TextView storeName = convertView
                .findViewById ( R.id.storeNameList );
        TextView storeAddress = convertView
                .findViewById ( R.id.storeAddressList );
        ImageView storeImageView = convertView
                .findViewById ( R.id.storeImageList );



        DeliveryBoy deliveryBoy = this.expandableBillDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);

        storeName.setText(deliveryBoy.getFirstName());
        storeAddress.setText(deliveryBoy.getLastName());

        RequestOptions requestOptions = new RequestOptions ();
        requestOptions.placeholder ( R.mipmap.ic_launcher );
        requestOptions.error ( R.mipmap.ic_launcher );
        if (!((Activity) context).isFinishing())
        {
            Glide.with ( context )
                    .setDefaultRequestOptions ( requestOptions )
                    .load ( deliveryBoy.getDeliveryBoyProfile() ).fitCenter ().into ( storeImageView );
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableBillDetail.get ( this.expandableListTitle.get ( listPosition ) )
                .size ();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get ( listPosition );
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size ();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup ( listPosition );
        final String[] day = {(String) getGroup ( listPosition )};


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            convertView = layoutInflater.inflate ( R.layout.deliveryboy_orderslist, null );
        }
        TextView listTitleTextView = convertView
                .findViewById ( R.id.lblListHeader );

        listTitleTextView.setText ( listTitle );
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
