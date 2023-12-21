package com.smiligenceUAT1.metrozadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.AdvertisementDetails;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementAdapter extends RecyclerView.Adapter<AdvertisementAdapter.ImageViewHolder> {
    private Context mcontext;
    private List<AdvertisementDetails> categoryDetailsList;
    private OnItemClicklistener mlistener;
    ImageViewHolder imageViewHolder;
    private int indicator;
    ArrayList<UserDetails> userDetailsArrayList=new ArrayList<UserDetails>();

    ArrayList<ArrayList<UserDetails>> userDetailsArrayListNew=new ArrayList<ArrayList<UserDetails>>();
    public interface OnItemClicklistener {
        void Onitemclick(int Position);
    }

    public void setOnItemclickListener(OnItemClicklistener listener) {
        mlistener = listener;
    }

    public AdvertisementAdapter(Context context, List<AdvertisementDetails> catagories) {
        mcontext = context;
        categoryDetailsList = catagories;
    }

    @NonNull

    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.advertisement_adapter, parent, false);
        imageViewHolder = new ImageViewHolder(v, mlistener);
        return imageViewHolder;
    }


    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {


        AdvertisementDetails categoryDetails = categoryDetailsList.get(position);
        userDetailsArrayList = (ArrayList<UserDetails>) categoryDetails.getAdvertisingStoreList();
        if (userDetailsArrayList!=null && userDetailsArrayList.size()>0) {
            holder.store_Name.setText(userDetailsArrayList.get(0).getStoreName());
        }
        holder.start_Date.setText(categoryDetails.getScheduledDate());
        holder.Duration.setText(categoryDetails.getAdvertisingDuration());
        holder.Cost.setText("₹"+categoryDetails.getAdvertisementAmount());

       if( !categoryDetailsList.isEmpty()&&categoryDetailsList.size()!=0&&categoryDetailsList!=null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher);
            requestOptions.error(R.mipmap.ic_launcher);
            Glide.with(mcontext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(categoryDetails.getImage()).fitCenter().into(holder.itemImages);

    }
    }


    public int getItemCount() {
        return categoryDetailsList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView catagoryName;
        ImageView itemImages;
        TextView store_Name,start_Date,Duration,Cost;

        public ImageViewHolder(@NonNull View itemView, final OnItemClicklistener itemClicklistener) {
            super(itemView);
           // catagoryName = itemView.findViewById(R.id.catagoryName_adapter);
            itemImages = itemView.findViewById(R.id.advertisementsimg);
            store_Name=itemView.findViewById(R.id.storename);
                    start_Date=itemView.findViewById(R.id.startDate);
            Duration=itemView.findViewById(R.id.Duration);
            Cost=itemView.findViewById(R.id.Cost);


            itemView.setOnClickListener(v -> {
                if (itemClicklistener != null) {
                    int Position = getAdapterPosition();
                    if (Position != RecyclerView.NO_POSITION) {
                        itemClicklistener.Onitemclick(Position);
                    }
                }
            });
        }
    }
}
