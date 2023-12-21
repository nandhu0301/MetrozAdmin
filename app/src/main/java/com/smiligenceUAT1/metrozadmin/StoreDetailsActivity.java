package com.smiligenceUAT1.metrozadmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetailsNew;
import com.smiligenceUAT1.metrozadmin.bean.StoreTimings;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StoreDetailsActivity extends AppCompatActivity {

    DatabaseReference databaseReference, storeMaintainenceRef, metrozStoreTimingRef;
    EditText storeNameEditText, firstname_storeEditText, lastNameEditText, selectedcategoryEditText, storeAddress,
            email_store, phonenumber_store, pincode_store, bankname, branchName_store, Accountnumber, ifsc_store, Fssai_store, Business_type, aadharnumber, gstnumber_store;
    ImageView storeLogo, aadharImage, fssaiCertificate;
    ArrayList<CategoryDetailsNew> categoryDetailsArrayList = new ArrayList<>();
    ArrayList<CategoryDetailsNew> categoryDetailsArrayList1 = new ArrayList<>();
    ImageView backButton;
    String getStoreID;
    AlertDialog dialog;
    Switch shopOpenCloseStatus;
    Switch shopLiveSuspendStatus;
    String pattern = "hh:mm aa";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String currentTime;
    public static String DATE_FORMAT = "MMMM dd, yyyy";
    DateFormat date;
    Date currentLocalTime;
    String currentDateAndTime;
    String metrozStartTime;
    String metrozStopTime;
    TextView reasonForSuspensionText;
    androidx.appcompat.app.AlertDialog alertDialogForSuspenson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        storeNameEditText = findViewById(R.id.storeName);
        firstname_storeEditText = findViewById(R.id.firstname_store);
        lastNameEditText = findViewById(R.id.lastName);
        selectedcategoryEditText = findViewById(R.id.selectedcategory);
        storeAddress = findViewById(R.id.storeAddress_store);
        email_store = findViewById(R.id.email_store);
        phonenumber_store = findViewById(R.id.phonenumber_store);
        pincode_store = findViewById(R.id.pincode_store);
        bankname = findViewById(R.id.bankname_store);
        branchName_store = findViewById(R.id.branchName_store);
        Accountnumber = findViewById(R.id.Accountnumber);
        ifsc_store = findViewById(R.id.ifsc_store);
        Fssai_store = findViewById(R.id.Fssai_store);
        Business_type = findViewById(R.id.Business_type);
        aadharnumber = findViewById(R.id.aadharnumber);
        gstnumber_store = findViewById(R.id.gstnumber_store);
        storeLogo = findViewById(R.id.storelogo);
        aadharImage = findViewById(R.id.aadharID);
        fssaiCertificate = findViewById(R.id.fssaicertificate);
        backButton = findViewById(R.id.back_button);
        shopOpenCloseStatus = findViewById(R.id.openShopManuallySwitch);
        shopLiveSuspendStatus=findViewById(R.id.shopLiveSuspendStatus);
        reasonForSuspensionText=findViewById(R.id.reasonForSuspensionText);

        databaseReference = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SellerLoginDetails");
        storeMaintainenceRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("storeTimingMaintenance");
        metrozStoreTimingRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("MetrozstoreTiming");

        getStoreID = getIntent().getStringExtra("StoreId");
        Query query = databaseReference.child(getStoreID);


        Calendar cal = Calendar.getInstance();
        currentLocalTime = cal.getTime();
        date = new SimpleDateFormat("HH:mm aa");
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        currentDateAndTime = dateFormat.format(new Date());
        currentTime = date.format(currentLocalTime);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreDetailsActivity.this, StoreHistory.class);
                intent.putExtra("StoreId", getStoreID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        shopLiveSuspendStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(StoreDetailsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.reason_for_suspension, null);
                    dialogBuilder.setView(dialogView);
                    final EditText updateReason = dialogView.findViewById(R.id.reasonForSuspension);
                    Button reasonForUploadSuspension=dialogView.findViewById(R.id.reasonUploadSuspension);
                    ImageView cancel = dialogView.findViewById(R.id.cancelSuspensionDialog);
                     alertDialogForSuspenson = dialogBuilder.create();
                    alertDialogForSuspenson.show();
                    alertDialogForSuspenson.setCancelable(false);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialogForSuspenson.dismiss();
                            shopLiveSuspendStatus.setChecked(false);
                        }
                    });

                    reasonForUploadSuspension.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("storeTimingMaintenance").child(getStoreID);
                            startTimeDataRef.child("storeLiveUpdate").setValue("Suspend");
                            startTimeDataRef.child("reasonForSuspend").setValue(updateReason.getText().toString());
                            Toast.makeText(StoreDetailsActivity.this, "Reason for suspension updated", Toast.LENGTH_SHORT).show();
                            alertDialogForSuspenson.dismiss();
                        }
                    });
                }
                else
                {
                    DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("storeTimingMaintenance").child(getStoreID);
                    startTimeDataRef.child("storeLiveUpdate").setValue("Live");
                    startTimeDataRef.child("reasonForSuspend").setValue("");

                }
            }
        });

        /*shopSuspendStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    shopLiveStatus.setChecked(false);
                    DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance()
                            .getReference("storeTimingMaintenance").child(getStoreID);
                    startTimeDataRef.child("storeLiveUpdate").setValue("Suspend");
                }
                else
                {
                    shopLiveStatus.setChecked(true);
                }
            }
        });*/

        if (getStoreID != null && !"".equals(getStoreID)) {
            //if result returns 1 => shop starts time is after metroz time
            //if result returns -1 => shops end time is before metroz end time
            //if result returns 0 => shop start,stop and metroz starts and stop time are same

            //Starting time of shop is after metroz time && ending time of shop is before metroz time

            metrozStoreTimingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {

                        StoreTimings metrozStoreTimings = dataSnapshot.getValue(StoreTimings.class);

                        metrozStartTime = metrozStoreTimings.getShopStartTime();
                        metrozStopTime = metrozStoreTimings.getShopEndTime();

                        storeMaintainenceRef.child(String.valueOf(getStoreID)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.getChildrenCount() > 0) {

                                    StoreTimings storeTimings = dataSnapshot1.getValue(StoreTimings.class);

                                    if (storeTimings.getStoreLiveUpdate()!=null )
                                    {
                                        if (storeTimings.getStoreLiveUpdate().equals("Live"))
                                        {
                                            shopLiveSuspendStatus.setChecked(false);
                                            reasonForSuspensionText.setVisibility(View.INVISIBLE);
                                        }
                                        else
                                        {
                                            shopLiveSuspendStatus.setChecked(true);
                                            reasonForSuspensionText.setText("Reason For Suspension:  "+storeTimings.getReasonForSuspend());
                                            alertDialogForSuspenson.dismiss();
                                            reasonForSuspensionText.setVisibility(View.VISIBLE);

                                        }

                                    }
                                    else
                                    {
                                        shopLiveSuspendStatus.setChecked(false);
                                    }
                                    try {

                                        //if result returns 1 => shop starts time is after metroz time
                                        //if result returns -1 => shops end time is before metroz end time
                                        //if result returns 0 => shop start,stop and metroz starts and stop time are same

                                        //Starting time of shop is after metroz time && ending time of shop is before metroz time
                                        if ((sdf.parse(currentTime).compareTo(sdf.parse(metrozStartTime)) == 1) && !(sdf.parse(currentTime).compareTo(sdf.parse(metrozStopTime)) == 1)) {

                                            if ((sdf.parse(storeTimings.getShopStartTime()).compareTo(sdf.parse(metrozStartTime)) == 1 &&
                                                    (sdf.parse(storeTimings.getShopEndTime()).compareTo(sdf.parse(metrozStopTime)) == -1))
                                                    ||  // Starting time of shop is equal to metroz &&  ending time of shop is before metroz time
                                                    (sdf.parse(storeTimings.getShopStartTime()).compareTo(sdf.parse(metrozStartTime)) == 0 &&
                                                            (sdf.parse(storeTimings.getShopEndTime()).compareTo(sdf.parse(metrozStopTime)) == -1))
                                                    || //Starting time of shop is after metroz time && ending time of metroz is equal to metroz timing
                                                    (sdf.parse(storeTimings.getShopStartTime()).compareTo(sdf.parse(metrozStartTime)) == 1 &&
                                                            sdf.parse(storeTimings.getShopEndTime()).compareTo(sdf.parse(metrozStopTime)) == 0)
                                                    ||  //start and end time is equal to metroz timings
                                                    (sdf.parse(storeTimings.getShopStartTime()).compareTo(sdf.parse(metrozStartTime)) == 0 &&
                                                            sdf.parse(storeTimings.getShopEndTime()).compareTo(sdf.parse(metrozStopTime)) == 0)) {


                                                //avail stores list
                                                if (storeTimings.getStoreStatus().equalsIgnoreCase("")) {

                                                    if ((sdf.parse(currentTime).compareTo(sdf.parse(storeTimings.getShopStartTime())) == 1) &&
                                                            !(sdf.parse(currentTime).compareTo(sdf.parse(storeTimings.getShopEndTime())) == 1)) {
                                                        shopOpenCloseStatus.setChecked(true);
                                                    } else if ((sdf.parse(currentTime).compareTo(sdf.parse(storeTimings.getShopEndTime())) == 1) ||
                                                            (sdf.parse(currentTime).compareTo(sdf.parse(storeTimings.getShopStartTime())) == -1)) {
                                                        shopOpenCloseStatus.setChecked(false);

                                                    }
                                                } else {
                                                    if (storeTimings.getStoreStatus().equalsIgnoreCase("Opened")) {
                                                        shopOpenCloseStatus.setChecked(true);
                                                    }
                                                    if (storeTimings.getStoreStatus().equalsIgnoreCase("Closed")) {
                                                        shopOpenCloseStatus.setChecked(false);
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(StoreDetailsActivity.this, "Enters 5", Toast.LENGTH_SHORT).show();
                                            shopOpenCloseStatus.setChecked(false);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }

                                shopOpenCloseStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                    .getReference("storeTimingMaintenance").child(getStoreID);
                                            startTimeDataRef.child("storeStatus").setValue("Opened");
                                            startTimeDataRef.child("creationDate").setValue(currentDateAndTime);
                                        } else {
                                            DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                    .getReference("storeTimingMaintenance").child(getStoreID);
                                            startTimeDataRef.child("storeStatus").setValue("Closed");
                                            startTimeDataRef.child("creationDate").setValue(currentDateAndTime);
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                    if (!"".equalsIgnoreCase(userDetails.getStoreName()) && userDetails.getCategoryList() != null) {


                        storeNameEditText.setText(userDetails.getStoreName());
                        firstname_storeEditText.setText(userDetails.getFirstName());
                        lastNameEditText.setText(userDetails.getLastName());


                        categoryDetailsArrayList = (ArrayList<CategoryDetailsNew>) userDetails.getCategoryList();


                        for (int i = 0; i < categoryDetailsArrayList.size(); i++) {
                            if (userDetails.getCategoryList().size() == 1) {
                                selectedcategoryEditText.append(categoryDetailsArrayList.get(i).getCategoryName() + ".");
                            } else {
                                selectedcategoryEditText.append(categoryDetailsArrayList.get(i).getCategoryName() + ",");
                            }
                        }


                        storeAddress.setText(userDetails.getAddress());
                        email_store.setText(userDetails.getEmail_Id());
                        phonenumber_store.setText(userDetails.getPhoneNumber());
                        pincode_store.setText(userDetails.getPincode());
                        bankname.setText(userDetails.getBankName());
                        branchName_store.setText(userDetails.getBranchName());
                        Accountnumber.setText(userDetails.getAccountNumber());
                        ifsc_store.setText(userDetails.getIfscCode());
                        Fssai_store.setText(userDetails.getFssaiNumber());
                        Business_type.setText(userDetails.getBusinessType());
                        aadharnumber.setText(userDetails.getAadharNumber());
                        gstnumber_store.setText(userDetails.getGstNumber());


                        Glide.with(StoreDetailsActivity.this).
                                load(userDetails.getStoreLogo()).fitCenter().into(storeLogo);


                        Glide.with(StoreDetailsActivity.this).
                                load(userDetails.getAadharImage()).fitCenter().into(aadharImage);


                        aadharImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StoreDetailsActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.seller_aadhar_dialog, null);
                                ImageView AadharImage = mView.findViewById(R.id.Aadharimage_store);
                                ImageView Cancel = mView.findViewById(R.id.cancelIcon);
                                TextView title = mView.findViewById(R.id.texttitle);

                                title.setText("Aadhar Image");
                                mBuilder.setView(mView);
                                dialog = mBuilder.create();
                                dialog.show();
                                dialog.setCancelable(false);

                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });


                                Glide.with(StoreDetailsActivity.this).
                                        load(userDetails.getAadharImage()).fitCenter().into(AadharImage);

                            }
                        });

                        Glide.with(StoreDetailsActivity.this).
                                load(userDetails.getFssaiCertificateImage()).fitCenter().into(fssaiCertificate);
                        fssaiCertificate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StoreDetailsActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.seller_aadhar_dialog, null);
                                ImageView AadharImage = mView.findViewById(R.id.Aadharimage_store);
                                ImageView Cancel = mView.findViewById(R.id.cancelIcon);
                                TextView title = mView.findViewById(R.id.texttitle);

                                title.setText("Fssai Certificate Image");


                                mBuilder.setView(mView);
                                dialog = mBuilder.create();
                                dialog.show();
                                dialog.setCancelable(false);

                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                Glide.with(StoreDetailsActivity.this).
                                        load(userDetails.getFssaiCertificateImage()).fitCenter().into(AadharImage);
                            }
                        });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}