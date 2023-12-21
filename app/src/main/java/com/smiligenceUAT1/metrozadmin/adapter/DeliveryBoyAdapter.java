package com.smiligenceUAT1.metrozadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;

import java.util.List;

public class DeliveryBoyAdapter extends BaseAdapter {

    private Context mcontext;
    private List<DeliveryBoy> itemList;
    LayoutInflater inflater;

    public DeliveryBoyAdapter(Context context, List<DeliveryBoy> itemListŇew) {
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

    private static class ViewHolder {
        ImageView images;
        TextView t_name, t_price_percent, t_total_amount;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_details_layout, parent, false);
            holder.t_name = row.findViewById(R.id.itemName);
            holder.t_price_percent = row.findViewById(R.id.item_qty);
            holder.t_total_amount = row.findViewById(R.id.itemTotal);

            holder.images = row.findViewById(R.id.itemImage);
            row.setTag(holder);
        } else {

            holder = (ViewHolder) row.getTag();
        }

        DeliveryBoy itemDetailsObj = itemList.get(position);
        holder.t_name.setTypeface(null, Typeface.BOLD);
        holder.t_name.setText(itemDetailsObj.getFirstName());
        holder.t_price_percent.setText(itemDetailsObj.getMobileNumber());
        holder.t_total_amount.setText(itemDetailsObj.getEmailId());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);


        if (!((Activity) mcontext).isFinishing()) {
            Glide.with ( mcontext )
                    .setDefaultRequestOptions ( requestOptions )
                    .load ( itemDetailsObj.getDeliveryBoyProfile () ).fitCenter ().into ( holder.images );
        }
        return row;


    }
}
