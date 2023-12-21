package com.smiligenceUAT1.metrozadmin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.ItemApprovalActivity;
import com.smiligenceUAT1.metrozadmin.R;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ApprovalAdapter extends RecyclerView.Adapter<Approve_Decline_Item_Adapter.ImageViewHolder> {
    private Context mcontext;
    private List<ItemDetails> sellerDetailsList;
    ArrayList<String> arrayListUser = new ArrayList<>();
    ArrayList<String> arrayListUser1 = new ArrayList<>();
    public Approve_Decline_Item_Adapter.OnItemClicklistener mlistener;
    Boolean flag;
    private int lastPosition = -1;
    DatabaseReference itemDataRef;
    boolean check = false;
    ItemDetails itemDetails;
    SweetAlertDialog sweetAlertDialog;
    int counter = 0;
    Thread thread;


    public void setOnItemclickListener(Approve_Decline_Item_Adapter.OnItemClicklistener listener) {
        mlistener = listener;
    }

    public interface OnItemClicklistener {
        void Onitemclick(int Position);
    }


    public ApprovalAdapter(Context context, List<ItemDetails> userDetails, Boolean flag) {
        mcontext = context;
        sellerDetailsList = userDetails;
        this.flag = flag;
        notifyDataSetChanged();
    }

    @NonNull

    public Approve_Decline_Item_Adapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.approve_or_decline_layout, parent, false);
        Approve_Decline_Item_Adapter.ImageViewHolder imageViewHolder = new Approve_Decline_Item_Adapter.ImageViewHolder(v, mlistener);
        return imageViewHolder;
    }

    public void onBindViewHolder(@NonNull final Approve_Decline_Item_Adapter.ImageViewHolder holder, final int position) {

        final ItemDetails sellerUserDetails = sellerDetailsList.get(position);
        if (!sellerDetailsList.isEmpty() && sellerDetailsList != null) {

            holder.itemNameAd.setText(sellerUserDetails.getItemName());
            holder.categoryNameAd.setText(sellerUserDetails.getCategoryName());
            holder.sellerNameAd.setText(sellerUserDetails.getStoreName());
            itemDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("ProductDetails");

        }
        Query itemApprovalStatusQuery = itemDataRef.orderByChild("itemApprovalStatus").equalTo("Waiting for approval");
        itemApprovalStatusQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() <= 0) {
                    if (counter == 1) {
                        if (!((Activity) mcontext).isFinishing()) {
                            sweetAlertDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.show();


                            sweetAlertDialog.setTitleText("Item Approved !")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismiss();
                                            counter = 3;
                                            if (counter == 3) {
                                                Intent intent = new Intent(mcontext, ItemApprovalActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                mcontext.startActivity(intent);
                                            }
                                        }
                                    });

                            sweetAlertDialog.setCancelable(false);

                        }
                    } else if (counter == 4) {

                        if (!((Activity) mcontext).isFinishing()) {

                            sweetAlertDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.show();


                            sweetAlertDialog.setTitleText("Item Declined !")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismiss();
                                            counter = 6;
                                            if (counter == 6) {
                                                Intent intent = new Intent(mcontext, ItemApprovalActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                mcontext.startActivity(intent);
                                            }
                                        }
                                    });

                            sweetAlertDialog.setCancelable(false);

                        }


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 0) {
                    String status = sellerDetailsList.get(position).getItemApprovalStatus();
                    if ("Waiting for approval".equalsIgnoreCase(status)) {

                        itemDataRef.child(String.valueOf(sellerDetailsList.get(position).getItemId())).child("itemApprovalStatus").setValue("Approved");

                        if (!((Activity) mcontext).isFinishing()) {
                            sweetAlertDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.show();


                            sweetAlertDialog.setTitleText("Item Approved !")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismiss();

                                        }
                                    });

                            sweetAlertDialog.setCancelable(false);
                        }

                        counter = 1;
                    }


                }

            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 0) {
                    String status = sellerDetailsList.get(position).getItemApprovalStatus();
                    if ("Waiting for approval".equalsIgnoreCase(status)) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
                        final EditText input = new EditText(mcontext);
                        alertDialog.setMessage("Enter Reason for Rejection");

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);


                        alertDialog.setPositiveButton("Upload Reason",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String reasonText = input.getText().toString().trim();
                                        if ("".equalsIgnoreCase(reasonText)) {
                                            input.setError("Required");
                                            input.setEnabled(true);
                                            input.setError(Html.fromHtml("<font color='red'>this is the error</font>"));
                                            if (!((Activity) mcontext).isFinishing()) {
                                                sweetAlertDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.ERROR_TYPE);
                                                sweetAlertDialog.show();


                                                sweetAlertDialog.setTitleText(" You can't able to  Reject the item Without valid Reason")
                                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                sweetAlertDialog.dismiss();

                                                            }
                                                        });

                                                sweetAlertDialog.setCancelable(false);
                                            }
                                        } else {
                                            itemDataRef.child(String.valueOf(sellerDetailsList.get(position).getItemId())).child("reasonForRejection").setValue(reasonText);
                                            itemDataRef.child(String.valueOf(sellerDetailsList.get(position).getItemId())).child("itemApprovalStatus").setValue("Declined");

                                            if (!((Activity) mcontext).isFinishing()) {
                                                sweetAlertDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.ERROR_TYPE);
                                                sweetAlertDialog.show();


                                                sweetAlertDialog.setTitleText("Item Declined !")
                                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                sweetAlertDialog.dismiss();

                                                            }
                                                        });

                                                sweetAlertDialog.setCancelable(false);
                                            }
                                            counter = 4;
                                        }
                                    }
                                });

                        alertDialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });
                        alertDialog.show();
                        alertDialog.setCancelable(false);

                    }

                }

            }
        });


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        Glide.with(mcontext)
                .setDefaultRequestOptions(requestOptions)
                .load(sellerUserDetails.getStoreLogo()).fitCenter().into(holder.imageViewAd);


    }

    public int getItemCount() {
        return sellerDetailsList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameAd, categoryNameAd, sellerNameAd;
        ImageView imageViewAd;
        public static Button approveButton, declineButton;


        public ImageViewHolder(@NonNull View itemView, final Approve_Decline_Item_Adapter.OnItemClicklistener itemClicklistener) {
            super(itemView);
            itemNameAd = itemView.findViewById(R.id.approveItemname);
            categoryNameAd = itemView.findViewById(R.id.approveCategoryName);
            sellerNameAd = itemView.findViewById(R.id.sellerName);
            imageViewAd = itemView.findViewById(R.id.approveImage);
            approveButton = itemView.findViewById(R.id.Approve);
            declineButton = itemView.findViewById(R.id.Decline);


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

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mcontext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
