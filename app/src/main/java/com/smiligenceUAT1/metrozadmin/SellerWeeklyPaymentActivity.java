package com.smiligenceUAT1.metrozadmin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.smiligenceUAT1.metrozadmin.adapter.BillDetailsAdapter;
import com.smiligenceUAT1.metrozadmin.adapter.SellerWeeklySettlemetAdapter;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.PaymentDetails;
import com.smiligenceUAT1.metrozadmin.bean.SellerPaymentDetailsConstant;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SellerWeeklyPaymentActivity extends AppCompatActivity {


    DatabaseReference orderdetailRef;
    TextView totalSalesAmount;
    OrderDetails orderDetails;
    ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
    final ArrayList<String> list = new ArrayList<String>();
    final ArrayList<String> orderListSize = new ArrayList<String>();
    int resultTotalAmount = 0;
    DatabaseReference paymentRef, sellerDataRef, sellerPaymentsRef;
    String startDateMon, endDateSunday;
    PaymentDetails paymentDetails = new PaymentDetails();
    ArrayList<PaymentDetails> paymentDetailsArrayList = new ArrayList<PaymentDetails>();
    ListView list_details;
    PaymentDetails sellerPaymentDetailsConstant;
    AlertDialog.Builder dialogBuilder, cardDialogBuilder;
    ListView cashExpenselistview, cardExpenseListView;
    String storeType;
    ImageView cancelDialog;
    int Percentage;
    ImageView backIcon;
    AlertDialog cashDialog, cardDialog;
    String getStoreID;
    ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
    ArrayList<ItemDetails> itemDetailsArrayListHere = new ArrayList<>();
    TextView totalAmountInfo;
    int totalAmountTemp = 0;
    boolean check = true;
    Button chooseeReceipt, changePaymentStatus;
    ImageView receiptImage;
    Intent intent;
    SweetAlertDialog pDialog;
    Uri storeImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    StorageReference receiptReference;
    private StorageTask mItemStorageTask;
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    DateFormat date;
    Date currentLocalTime;
    String currentDateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_weekly_payment);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        totalSalesAmount = findViewById(R.id.totalSalesamount);
        orderdetailRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderDetails");
        paymentRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Payments");
        sellerDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SellerLoginDetails");
        sellerPaymentsRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SellerPayments");
        receiptReference = FirebaseStorage.getInstance("gs://testmetrozproject.appspot.com").getReference("ReceiptSettlements");
        list_details = findViewById(R.id.list_details);


        backIcon = findViewById(R.id.back);

        getStoreID = getIntent().getStringExtra("StoreId");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToHomeintent = new Intent(SellerWeeklyPaymentActivity.this, StoreHistory.class);
                startActivity(backToHomeintent);
            }
        });
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        System.out.println();
        // Print dates of the current week starting on Monday
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        startDateMon = df.format(c.getTime());
        list.add(df.format(c.getTime()));
        for (int i = 0; i < 6; i++) {
            c.add(Calendar.DATE, 1);
            list.add(df.format(c.getTime()));

        }
        endDateSunday = df.format(c.getTime());


        orderdetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot itemSnap : dataSnapshot.getChildren()) {
                        orderDetails = itemSnap.getValue(OrderDetails.class);
                        itemDetailsArrayList = (ArrayList<ItemDetails>) orderDetails.getItemDetailList();

                        if (itemDetailsArrayList.size()>0 && itemDetailsArrayList!=null) {
                            if (itemDetailsArrayList.get(0).getSellerId().equals(getStoreID)) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (orderDetails.getFormattedDate().equals(list.get(i))) {
                                        resultTotalAmount = resultTotalAmount + orderDetails.getPaymentamount()-orderDetails.getTipAmount();
                                        orderListSize.add(orderDetails.getOrderId());
                                    }
                                }
                            }
                        }
                    }
                    totalSalesAmount.setText("₹ " + resultTotalAmount);

                    DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Payments").child(getStoreID).child(startDateMon);
                    startTimeDataRef.child("startDate").setValue(startDateMon);
                    startTimeDataRef.child("endDate").setValue(endDateSunday);
                    startTimeDataRef.child("totalAmount").setValue(resultTotalAmount);
                    startTimeDataRef.child("orderCount").setValue(String.valueOf(orderListSize.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sellerDataRef.child(getStoreID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                storeType = userDetails.getPaymentType();
                if (storeType == null) {
                    storeType = "Basic";
                }

                if (storeType != null) {
                    sellerPaymentsRef.child(storeType).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                SellerPaymentDetailsConstant sellerPaymentDetailsConstant = dataSnapshot.getValue(SellerPaymentDetailsConstant.class);
                                Percentage = sellerPaymentDetailsConstant.getPercentage();
                            }
                            paymentRef.child(getStoreID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        paymentDetailsArrayList.clear();
                                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                            PaymentDetails paymentDetails = snap.getValue(PaymentDetails.class);
                                            paymentDetailsArrayList.add(paymentDetails);
                                            Collections.reverse(paymentDetailsArrayList);
                                        }
                                        SellerWeeklySettlemetAdapter paymentAdapter = new SellerWeeklySettlemetAdapter(SellerWeeklyPaymentActivity.this, paymentDetailsArrayList, storeType, Percentage);
                                        list_details.setAdapter(paymentAdapter);
                                        paymentAdapter.notifyDataSetChanged();
                                        if (paymentAdapter != null) {
                                            int totalHeight = 0;
                                            for (int i = 0; i < paymentAdapter.getCount(); i++) {
                                                View listItem = paymentAdapter.getView(i, null, list_details);
                                                listItem.measure(0, 0);
                                                totalHeight += listItem.getMeasuredHeight();
                                            }
                                            ViewGroup.LayoutParams params = list_details.getLayoutParams();
                                            params.height = totalHeight + (list_details.getDividerHeight() * (paymentAdapter.getCount() - 1));
                                            list_details.setLayoutParams(params);
                                            list_details.requestLayout();
                                            list_details.setAdapter(paymentAdapter);
                                            paymentAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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



        list_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sellerPaymentDetailsConstant = paymentDetailsArrayList.get(i);
                Calendar cal = Calendar.getInstance();
                currentLocalTime = cal.getTime();
                date = new SimpleDateFormat(pattern);
                DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT_YYYYMD);
                currentDateAndTime = dateFormat.format(new Date());

                try {
                    if (sdf.parse(currentDateAndTime).compareTo(sdf.parse(sellerPaymentDetailsConstant.getEndDate())) == 1)
                    {
                        if ( sellerPaymentDetailsConstant.getPaymentStatus()==null ) {

                            dialogBuilder = new AlertDialog.Builder(SellerWeeklyPaymentActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.bill_details, null);
                            dialogBuilder.setView(dialogView);
                            totalAmountInfo = dialogView.findViewById(R.id.totalAmountInfo);
                            cancelDialog = dialogView.findViewById(R.id.Cancel);
                            chooseeReceipt = dialogView.findViewById(R.id.uploadReceipt);
                            changePaymentStatus = dialogView.findViewById(R.id.changePaymentStatusButton);
                            receiptImage = dialogView.findViewById(R.id.receiptImageview);
                            cashExpenselistview = dialogView.findViewById(R.id.billetailslist);


                            chooseeReceipt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                                }
                            });

                            orderdetailRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (check) {
                                        if (dataSnapshot.getChildrenCount() > 0) {

                                            orderDetailsArrayList.clear();
                                            itemDetailsArrayListHere.clear();
                                            totalAmountTemp = 0;
                                            for (DataSnapshot resSnap : dataSnapshot.getChildren()) {
                                                orderDetails = resSnap.getValue(OrderDetails.class);
                                                itemDetailsArrayListHere = (ArrayList<ItemDetails>) orderDetails.getItemDetailList();

                                                DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                                                List<Date> dates = getDates(sellerPaymentDetailsConstant.getStartDate(), sellerPaymentDetailsConstant.getEndDate());
                                                for (Date date : dates) {
                                                    if (orderDetails.getFormattedDate().equals(df1.format(date))) {
                                                        if (orderDetails.getOrderStatus().equals("Delivered")) {
                                                            if (itemDetailsArrayListHere.get(0).getSellerId().equals(getStoreID)) {

                                                                orderDetailsArrayList.add(orderDetails);
                                                                totalAmountTemp = totalAmountTemp + orderDetails.getTotalAmount();
                                                            }
                                                        }
                                                    }
                                                    totalAmountInfo.setText("₹ " + totalAmountTemp);
                                                    BillDetailsAdapter billDetailsAdapter = new BillDetailsAdapter(SellerWeeklyPaymentActivity.this, orderDetailsArrayList,"Seller");
                                                    cashExpenselistview.setAdapter(billDetailsAdapter);
                                                }
                                            }

                                            if (sellerPaymentDetailsConstant.getTotalAmount() == 0) {

                                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SellerWeeklyPaymentActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                sweetAlertDialog.setCancelable(false);
                                                sweetAlertDialog.setTitleText("No more orders taken for this week").show();
                                            } else {
                                                cashDialog = dialogBuilder.create();
                                                cashDialog.show();
                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            changePaymentStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    {
                                        if (storeImageUri != null) {
                                            pDialog = new SweetAlertDialog(SellerWeeklyPaymentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#73b504"));
                                            pDialog.setTitleText("Uploading receipt Details....");
                                            pDialog.setCancelable(false);
                                            pDialog.show();
                                            StorageReference imageFileStorageRef = receiptReference.child("ReceiptDetails/"
                                                    + System.currentTimeMillis() + "." + getExtenstion(storeImageUri));

                                            Bitmap bmp = null;
                                            try {
                                                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), storeImageUri);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                                            byte[] data = baos.toByteArray();

                                            mItemStorageTask = imageFileStorageRef.putBytes(data).addOnSuccessListener(
                                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                            while (!urlTask.isSuccessful()) ;
                                                            final Uri downloadUrl = urlTask.getResult();

                                                            paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    //if (dataSnapshot.getChildrenCount() > 0) {
                                                                    storeImageUri = null;
                                                                    receiptImage.setImageBitmap(null);
                                                                    if (!((Activity) SellerWeeklyPaymentActivity.this).isFinishing()) {
                                                                        DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                                                .getReference("Payments").child(getStoreID).child(sellerPaymentDetailsConstant.getStartDate());
                                                                        startTimeDataRef.child("paymentStatus").setValue("Settled");
                                                                        startTimeDataRef.child("receiptURL").setValue(downloadUrl.toString());
                                                                    }
                                                                    if (!((Activity) SellerWeeklyPaymentActivity.this).isFinishing()) {
                                                                        pDialog.dismiss();
                                                                        cashDialog.dismiss();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SellerWeeklyPaymentActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(SellerWeeklyPaymentActivity.this, "Please upload receipt", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });

                            cancelDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cashDialog.dismiss();
                                }
                            });
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (sellerPaymentDetailsConstant.getPaymentStatus()!=null) {
                    if (sellerPaymentDetailsConstant.getPaymentStatus().equals("Settled")) {

                        dialogBuilder = new AlertDialog.Builder(SellerWeeklyPaymentActivity.this);
                        LayoutInflater inflater1 = getLayoutInflater();
                        final View dialogView1 = inflater1.inflate(R.layout.bill_details_new, null);
                        dialogBuilder.setView(dialogView1);
                        cancelDialog = dialogView1.findViewById(R.id.Cancel);
                        receiptImage = dialogView1.findViewById(R.id.receiptImageview);


                        if (sellerPaymentDetailsConstant.getReceiptURL()!=null && !sellerPaymentDetailsConstant.getReceiptURL().equals("")) {
                            String drivingLicenseProofUri = String.valueOf(Uri.parse(sellerPaymentDetailsConstant.getReceiptURL()));
                            Picasso.get().load(drivingLicenseProofUri).into(receiptImage);
                            cashDialog = dialogBuilder.create();
                            cashDialog.show();
                        }
                        cancelDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cashDialog.dismiss();
                            }
                        });

                    }
                }
            }
        });
    }

    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            storeImageUri = data.getData();
            Picasso.get().load(storeImageUri).into(receiptImage);
        }
    }

    private static List<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}