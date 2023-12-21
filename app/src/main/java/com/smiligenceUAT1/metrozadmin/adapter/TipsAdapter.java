package com.smiligenceUAT1.metrozadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.Tip;

import java.util.List;

public class TipsAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Tip> tipList1;
    LayoutInflater inflater;

    public TipsAdapter(Context context, List<Tip> tipList) {
        mcontext = context;
        tipList1 = tipList;
        inflater = (LayoutInflater.from ( context ));
    }

    @Override
    public int getCount() {
        return tipList1.size ();
    }

    @Override
    public Object getItem(int position) {
        return tipList1.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView images;
        TextView t_name, t_price_percent;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TipsAdapter.ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate ( R.layout.tip_adapter, parent, false );
            holder.t_name = row.findViewById ( R.id.tipName );
            holder.t_price_percent = row.findViewById ( R.id.tipAmount );
            holder.images = row.findViewById ( R.id.tipImage );
            row.setTag ( holder );
        } else {
            holder = (TipsAdapter.ViewHolder) row.getTag ();
        }

        Tip tip = tipList1.get ( position );

        holder.t_price_percent.setText ( "₹ " + String.valueOf ( tip.getTipsAmount () ) );
        holder.t_price_percent.setSelected ( true );

        holder.t_name.setText ( tip.getTipsName () );
        holder.t_name.setSelected ( true );
        RequestOptions requestOptions = new RequestOptions ();
        requestOptions.placeholder ( R.mipmap.ic_launcher );
        requestOptions.error ( R.mipmap.ic_launcher );
        Glide.with ( mcontext )
                .setDefaultRequestOptions ( requestOptions )
                .load ( tip.getUrl () ).fitCenter ().into ( holder.images );
        return row;
    }
}