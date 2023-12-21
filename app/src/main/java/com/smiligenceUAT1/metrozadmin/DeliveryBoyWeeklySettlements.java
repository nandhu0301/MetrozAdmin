package com.smiligenceUAT1.metrozadmin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
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
import com.google.android.material.navigation.NavigationView;
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
import com.smiligenceUAT1.metrozadmin.adapter.DeliveryBoySettlementAdapter;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryPartnerCharge;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.PaymentDetails;
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

public class DeliveryBoyWeeklySettlements extends AppCompatActivity {
    View mHeaderView;
    NavigationView navigationView;
    TextView textViewUsername;
    DatabaseReference categoryRef, itemDetailsRef;
    DatabaseReference orderdetailRef;
    ImageView imageView;
    String sellerIdIntent,storeNameIntent,storeImage;
    TextView totalSalesAmount;
    OrderDetails orderDetails;
    ArrayList<ItemDetails> itemDetailsArrayList=new ArrayList<>();
    final ArrayList<String> list = new ArrayList<String>();
    final ArrayList<String> orderListSize = new ArrayList<String>();
    int resultTotalAmount=0;
    DatabaseReference deliveryDataRef,deliveryPaymentsRef;
    String startDateMon,endDateSunday;
    PaymentDetails paymentDetails=new PaymentDetails();
    ArrayList<PaymentDetails> paymentDetailsArrayList=new ArrayList<PaymentDetails>();
    ListView list_details;
    AlertDialog.Builder dialogBuilder,cardDialogBuilder;
    AlertDialog cashDialog,cardDialog;
    ListView cashExpenselistview,cardExpenseListView;
    PaymentDetails sellerPaymentDetailsConstant;
    private StorageTask mItemStorageTask;
    String storeType;
    int Percentage;
    ImageView cancelDialog;
    TextView totalAmountInfo;
    int totalAmountTemp=0;
    boolean  check=true;
    Button chooseeReceipt,changePaymentStatus;
    ImageView receiptImage;
    String getDeliveryBoyId;
    SweetAlertDialog pDialog;
    Uri storeImageUri;
    StorageReference receiptReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    ArrayList<OrderDetails> orderDetailsArrayList=new ArrayList<>();
    ArrayList<ItemDetails> itemDetailsArrayListHere=new ArrayList<>();
    ImageView backButton;
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    DateFormat date;
    Date currentLocalTime;
    String currentDateAndTime;
    DatabaseReference DeliveryBoyCharges,DeliveryBoyChargesQuery;
    int distanceDeliveryBoyToStore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_weekly_settlements);

        getDeliveryBoyId = getIntent().getStringExtra("deliveryBoyID");
        backButton=findViewById(R.id.backicon);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( DeliveryBoyWeeklySettlements.this, DeliveryBoyHistory.class );
                intent.putExtra ( "deliveryBoyID",getDeliveryBoyId );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        });

        categoryRef = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "Category" );
        totalSalesAmount=findViewById(R.id.totalSalesamount);


        orderdetailRef = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "OrderDetails" );
        deliveryDataRef= FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyLoginDetails");
        deliveryPaymentsRef= FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyPayments");
        list_details=findViewById(R.id.list_details);
        receiptReference= FirebaseStorage.getInstance("gs://testmetrozproject.appspot.com").getReference("ReceiptSettlements");
        DeliveryBoyCharges=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyChargeDetails");
        DeliveryBoyChargesQuery=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyChargeDetails").child("DeliveryBoyCharges");


        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        System.out.println();
        // Print dates of the current week starting on Monday
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        startDateMon=df.format(c.getTime());

        list.add(df.format(c.getTime()));
        for (int i = 0; i <6; i++) {
            c.add(Calendar.DATE, 1);
            list.add(df.format(c.getTime()));

        }
        endDateSunday=df.format(c.getTime());



        DeliveryBoyChargesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    DeliveryPartnerCharge otherCategoryDetails = dataSnapshot.getValue(DeliveryPartnerCharge.class);
                    if (otherCategoryDetails != null) {
                        if (!"".equals(otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore())) {
                            distanceDeliveryBoyToStore = otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore();
                        }
                        orderdetailRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount()>0) {

                                    orderListSize.clear();
                                    for (DataSnapshot itemSnap : dataSnapshot.getChildren()) {
                                        orderDetails = itemSnap.getValue(OrderDetails.class);

                                        if (orderDetails.getDeliverboyId() != null && !orderDetails.getDeliverboyId().equals("")) {
                                            if (orderDetails.getDeliverboyId().equals(getDeliveryBoyId)) {
                                                for (int i = 0; i < list.size(); i++) {
                                                    if (orderDetails.getOrderStatus().equals("Delivered")) {
                                                        if (orderDetails.getFormattedDate() != null && !orderDetails.getFormattedDate().equals("")) {
                                                            if (orderDetails.getFormattedDate().equals(list.get(i))) {
                                                                int DS=orderDetails.getTotalDistanceDeliveryBoyFromCurrentLocationToStore()*distanceDeliveryBoyToStore;
                                                                resultTotalAmount = resultTotalAmount + orderDetails.getTotalFeeForDeliveryBoy()+DS + orderDetails.getTipAmount();
                                                                orderListSize.add(orderDetails.getOrderId());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }totalSalesAmount.setText("₹ " + resultTotalAmount);
                                    DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                            .getReference("DeliveryBoyPayments").child(getDeliveryBoyId).child(startDateMon);
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        deliveryPaymentsRef.child(getDeliveryBoyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {
                    paymentDetailsArrayList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        PaymentDetails paymentDetails = snap.getValue(PaymentDetails.class);
                        paymentDetailsArrayList.add(paymentDetails);
                        Collections.reverse(paymentDetailsArrayList);
                    }
                    DeliveryBoySettlementAdapter paymentAdapter = new DeliveryBoySettlementAdapter(DeliveryBoyWeeklySettlements.this, paymentDetailsArrayList);
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
                        if (sdf.parse(currentDateAndTime).compareTo(sdf.parse(sellerPaymentDetailsConstant.getEndDate())) == 1) {
                            if (sellerPaymentDetailsConstant.getPaymentStatus() == null) {
                                dialogBuilder = new AlertDialog.Builder(DeliveryBoyWeeklySettlements.this);
                                LayoutInflater inflater = getLayoutInflater();
                                final View dialogView = inflater.inflate(R.layout.bill_details, null);
                                dialogBuilder.setView(dialogView);
                                dialogView.invalidate();
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

                                                            System.out.println("INSIDE" + getDeliveryBoyId);
                                                            if (orderDetails.getDeliverboyId() != null && !orderDetails.getDeliverboyId().equalsIgnoreCase("")) {
                                                                if (orderDetails.getDeliverboyId().equals(getDeliveryBoyId)) {
                                                                    if (orderDetails.getOrderStatus().equals("Delivered")) {
                                                                        System.out.println("OUTSIDE" + itemDetailsArrayListHere.get(0).getSellerId());
                                                                        orderDetailsArrayList.add(orderDetails);
                                                                        totalAmountTemp = totalAmountTemp + orderDetails.getTotalFeeForDeliveryBoy() + orderDetails.getTipAmount();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                                totalAmountInfo.setText("₹ " + totalAmountTemp);
                                                BillDetailsAdapter billDetailsAdapter = new BillDetailsAdapter(DeliveryBoyWeeklySettlements.this, orderDetailsArrayList, "Delivery");
                                                cashExpenselistview.setAdapter(billDetailsAdapter);
                                                if (sellerPaymentDetailsConstant.getTotalAmount() == 0) {
                                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DeliveryBoyWeeklySettlements.this, SweetAlertDialog.ERROR_TYPE);
                                                    sweetAlertDialog.setCancelable(false);
                                                    sweetAlertDialog.setTitleText("No more orders taken for this week").show();
                                                } else {
                                                    if (!((Activity) DeliveryBoyWeeklySettlements.this).isFinishing()) {


                                                            cashDialog = dialogBuilder.create();
                                                            cashDialog.show();

                                                    }
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
                                                pDialog = new SweetAlertDialog(DeliveryBoyWeeklySettlements.this, SweetAlertDialog.PROGRESS_TYPE);
                                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#73b504"));
                                                pDialog.setTitleText("Uploading receipt Details....");
                                                pDialog.setCancelable(false);
                                                pDialog.show();
                                                StorageReference imageFileStorageRef = receiptReference.child("DeliveryBoyReceiptDetails/"
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

                                                                deliveryPaymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        //if (dataSnapshot.getChildrenCount() > 0) {
                                                                        storeImageUri = null;
                                                                        receiptImage.setImageBitmap(null);
                                                                        if (!((Activity) DeliveryBoyWeeklySettlements.this).isFinishing()) {
                                                                            DatabaseReference startTimeDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                                                    .getReference("DeliveryBoyPayments").child(getDeliveryBoyId).child(sellerPaymentDetailsConstant.getStartDate());
                                                                            startTimeDataRef.child("paymentStatus").setValue("Settled");
                                                                            startTimeDataRef.child("receiptURL").setValue(downloadUrl.toString());
                                                                        }
                                                                        if (!((Activity) DeliveryBoyWeeklySettlements.this).isFinishing()) {
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
                                                        Toast.makeText(DeliveryBoyWeeklySettlements.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(DeliveryBoyWeeklySettlements.this, "Please upload receipt", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                });

                                cancelDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!((Activity) DeliveryBoyWeeklySettlements.this).isFinishing()) {
                                            cashDialog.dismiss();

                                        }
                                    }
                                });
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (sellerPaymentDetailsConstant.getPaymentStatus() != null) {
                        if (sellerPaymentDetailsConstant.getPaymentStatus().equals("Settled")) {

                            dialogBuilder = new AlertDialog.Builder(DeliveryBoyWeeklySettlements.this);
                            LayoutInflater inflater1 = getLayoutInflater();
                            final View dialogView1 = inflater1.inflate(R.layout.bill_details_new, null);
                            dialogBuilder.setView(dialogView1);
                            cancelDialog = dialogView1.findViewById(R.id.Cancel);
                            receiptImage = dialogView1.findViewById(R.id.receiptImageview);

                            if (sellerPaymentDetailsConstant.getReceiptURL() != null && !sellerPaymentDetailsConstant.getReceiptURL().equals("")) {
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
    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}