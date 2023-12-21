package com.smiligenceUAT1.metrozadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.AdminRevenueDetails;

import java.util.List;

public class AdminWeeklyPaymentAdapter extends BaseAdapter {

    private Context mcontext;
    private List<AdminRevenueDetails> itemList;
    LayoutInflater inflater;
    String paymentType;
    private List<String> giftWrappingDetails;
    int Percentage;


    public AdminWeeklyPaymentAdapter(Context context, List<AdminRevenueDetails> itemListŇew) {
        mcontext = context;
        itemList = itemListŇew;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {

        TextView startDate, endDate, revenueFromProductDelivery,revenueFromNewStore,revenueFromPickupAndDrop,revenueFromAdvertisemnetbanners,deliveryBoyPayout,totalRevenueText;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        AdminWeeklyPaymentAdapter.ViewHolder holder = new AdminWeeklyPaymentAdapter.ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.admin_details_card, parent, false);
            holder.startDate = row.findViewById(R.id.startDate);
            holder.endDate = row.findViewById(R.id.endDate);
            holder.revenueFromProductDelivery = row.findViewById(R.id.revenuefromproductdelivery);
            holder.revenueFromNewStore = row.findViewById(R.id.revenueFromNewStore);
            holder.revenueFromPickupAndDrop = row.findViewById(R.id.revenuefrompickupanddrop);
            holder.deliveryBoyPayout = row.findViewById(R.id.deliverypayout);
            holder.totalRevenueText = row.findViewById(R.id.totalRevenueText);
            holder.revenueFromAdvertisemnetbanners=row.findViewById(R.id.revenuefromadvertisingbanners);
            row.setTag(holder);
        } else {
            holder = (AdminWeeklyPaymentAdapter.ViewHolder) row.getTag();
        }

        AdminRevenueDetails itemDetailsObj = itemList.get(position);
        holder.startDate.setText(itemDetailsObj.getStartDate());
        holder.endDate.setText(itemDetailsObj.getEndDate());
        holder.revenueFromProductDelivery.setText(String.valueOf(itemDetailsObj.getRevenueFromProductDelivery()));
        holder.revenueFromNewStore.setText(String.valueOf(itemDetailsObj.getRevenueFromNewStore()));
        holder.revenueFromPickupAndDrop.setText(String.valueOf(itemDetailsObj.getRevenueFromPickuUpAndDrop()));
        holder.deliveryBoyPayout .setText(String.valueOf(itemDetailsObj.getDeliveryBoyPayout()));
        holder.totalRevenueText.setText(String.valueOf(itemDetailsObj.getTotalRevenueAmount()));
        holder.revenueFromAdvertisemnetbanners.setText(String.valueOf(itemDetailsObj.getRevenueFromAdvertingBanners()));
        return row;

    }
}
