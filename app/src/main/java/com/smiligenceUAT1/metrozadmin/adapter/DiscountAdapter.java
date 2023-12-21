package com.smiligenceUAT1.metrozadmin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.Discount;

import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ACTIVE_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BOOLEAN_TRUE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.INACTIVE_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PERCENT_DISCOUNT;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PRICE_DISCOUNT;


public class DiscountAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Discount> discountList;
    LayoutInflater inflater;

    public DiscountAdapter(Context context, List<Discount> listDiscount) {
        mcontext = context;
        discountList = listDiscount;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return discountList.size();
    }

    @Override
    public Object getItem(int position) {
        return discountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView images;
        TextView discountName, t_discountType,t_price_percent,t_maxDiscount,t_minimumamount;
        LinearLayout linearLayout;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.discount_adapter_grid, parent, false);
            holder.discountName = row.findViewById(R.id.dis_name);
            holder.t_discountType = row.findViewById(R.id.dis_type);
            holder.t_price_percent=row.findViewById(R.id.dis_per_price);
            holder.t_maxDiscount=row.findViewById(R.id.max_dis);
            holder.t_minimumamount=row.findViewById(R.id.min_billAmount);
            holder.images = (ImageView) row.findViewById(R.id.image1);
            holder.linearLayout=row.findViewById(R.id.layout);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Discount newDiscount = discountList.get(position);




           if(ACTIVE_STATUS.equalsIgnoreCase(newDiscount.getDiscountStatus())){

               if (newDiscount.getDiscountPrice() == (null)||"".equals(newDiscount.getDiscountPrice())) {
                   holder.t_maxDiscount.setVisibility(View.VISIBLE);
                   holder.t_minimumamount.setVisibility(View.VISIBLE);
                   holder.discountName.setText(newDiscount.getDiscountName());
                   holder.t_discountType.setText(PERCENT_DISCOUNT);
                   holder.t_price_percent.setText("Discount Percent "+newDiscount.getDiscountPercentageValue()+"%");
                   holder.t_maxDiscount.setText("Maximum Amount for Discount ₹"+newDiscount.getMaxAmountForDiscount());
                   holder.t_minimumamount.setText("Minimum Bill Amount ₹"+newDiscount.getMinmumBillAmount());

                   holder.discountName.setSelected(BOOLEAN_TRUE);
                   holder.t_maxDiscount.setSelected(BOOLEAN_TRUE);

               } else if (newDiscount.getDiscountPercentageValue() == (null)||"".equals(newDiscount.getDiscountPercentageValue())) {
                   holder.t_maxDiscount.setVisibility(View.INVISIBLE);
                   holder.t_minimumamount.setText("Minimum Bill Amount ₹"+newDiscount.getMinmumBillAmount());
                   holder.discountName.setText(newDiscount.getDiscountName());
                   holder.t_discountType.setText(PRICE_DISCOUNT);

                   holder.t_price_percent.setText("Discount Amount ₹"+newDiscount.getDiscountPrice());
                   holder.discountName.setSelected(BOOLEAN_TRUE);

               }



           }else if(INACTIVE_STATUS.equalsIgnoreCase(newDiscount.getDiscountStatus())) {


               if (newDiscount.getDiscountPrice() == (null)||"".equals(newDiscount.getDiscountPrice())) {
                   holder.t_maxDiscount.setVisibility(View.VISIBLE);
                   holder.t_minimumamount.setVisibility(View.VISIBLE);
                   holder.discountName.setText(newDiscount.getDiscountName());
                   holder.t_discountType.setText(PERCENT_DISCOUNT);
                   holder.t_price_percent.setText("Discount Percent"+newDiscount.getDiscountPercentageValue()+"%");
                   holder.t_maxDiscount.setText("Maximum Amount for Discount ₹"+newDiscount.getMaxAmountForDiscount());
                   holder.t_minimumamount.setText("Minimum Bill Amount ₹"+newDiscount.getMinmumBillAmount());

                   holder.discountName.setSelected(BOOLEAN_TRUE);
                   holder.t_maxDiscount.setSelected(BOOLEAN_TRUE);
                   holder.linearLayout.setBackgroundColor(Color.rgb(123,123,123));
                   holder.images.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

               } else if (newDiscount.getDiscountPercentageValue() == (null)||"".equals(newDiscount.getDiscountPercentageValue())) {
                   holder.t_maxDiscount.setVisibility(View.INVISIBLE);
                   holder.t_minimumamount.setText("Minimum Bill Amount ₹"+newDiscount.getMinmumBillAmount());
                   holder.discountName.setText(newDiscount.getDiscountName());
                   holder.t_discountType.setText(PRICE_DISCOUNT);
                   holder.discountName.setSelected(BOOLEAN_TRUE);
                   holder.t_price_percent.setText("Discount Amount ₹"+newDiscount.getDiscountPrice());
                   holder.linearLayout.setBackgroundColor(Color.rgb(123,123,123));
                   holder.images.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
               }

           }







        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher_foreground);
        requestOptions.error(R.mipmap.ic_launcher_foreground);
        Glide.with(mcontext)
                .setDefaultRequestOptions(requestOptions)
                .load(newDiscount.getDiscountImage()).fitCenter().into(holder.images);
        return row;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}