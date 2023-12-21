package com.smiligenceUAT1.metrozadmin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.smiligenceUAT1.metrozadmin.adapter.ItemOrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.OneTimeDiscount;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewItemDetailsCategoryActivity extends AppCompatActivity {
    TextView order_Date, order_Id, order_Total, type_Of_Payment, fullName, shipping_Address, amount, shipping, wholeCharge, order_status,
            customerPhoneNumber, giftAmount, storeNameText, orderTimeTxt, deliverPinTxt, noContactDelivery, anyInstructions;
    BottomNavigationView bottomNavigation;
    ConstraintLayout orderDetailsLayout, cancelOrder,paymentDetailsLayout, shippingAddressLayout, orderSummaryLayout, specialinstructionLayout;
    RelativeLayout itemDetailsLayout, itemHeaderlayout;
    DatabaseReference databaseReference,oneTimeDiscountDataRef;
    private ArrayList<ItemDetails> openTicketItemList = new ArrayList<>();
    ItemOrderDetails itemOrderDetails;
    ListView listView;


    ImageView returnhome;
    RazorpayClient razorpay;
    Payment payment;

    String getOrderIdValue;
    LinearLayout addDiscounts;
    long maxidForOneTimeDiscount=0;
    OrderDetails orderDetails;
    OneTimeDiscount oneTimeDiscount=new OneTimeDiscount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_details_category);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        returnhome = findViewById(R.id.redirecttohome);
        itemDetailsLayout = findViewById(R.id.itemdetailslayout);
        itemHeaderlayout = findViewById(R.id.itemdetailslayoutheader);


        checkGPSConnection(getApplicationContext());


        returnhome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        databaseReference = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderDetails");
        oneTimeDiscountDataRef= FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OneTimeDiscount");

        //Order details value
        orderDetailsLayout = findViewById(R.id.order_details_layout);
        order_Date = orderDetailsLayout.findViewById(R.id.orderdate);
        order_Id = orderDetailsLayout.findViewById(R.id.bill_num);
        order_Total = orderDetailsLayout.findViewById(R.id.order_total);
        order_status = orderDetailsLayout.findViewById(R.id.order_status);
        orderTimeTxt = orderDetailsLayout.findViewById(R.id.ordertimetxt);


        specialinstructionLayout = findViewById(R.id.special_instructions);

        noContactDelivery = findViewById(R.id.noContactDelivery);
        anyInstructions = findViewById(R.id.specialinsructions);


        //Payment details
        paymentDetailsLayout = findViewById(R.id.payment_details);
        //card_Details = paymentDetailsLayout.findViewById ( R.id.card_details );
        type_Of_Payment = paymentDetailsLayout.findViewById(R.id.type_of_payment);

        //Shipping Address Details
        shippingAddressLayout = findViewById(R.id.shipping_details_layout);
        fullName = shippingAddressLayout.findViewById(R.id.full_name);
        shipping_Address = shippingAddressLayout.findViewById(R.id.address);
        //pinocde = shippingAddressLayout.findViewById ( R.id.pincode );
        customerPhoneNumber = shippingAddressLayout.findViewById(R.id.phoneNumber);

        //Order summary
        orderSummaryLayout = findViewById(R.id.cart_total_amount_layout);

        amount = orderSummaryLayout.findViewById(R.id.tips_price1);
        shipping = orderSummaryLayout.findViewById(R.id.tips_price);
        wholeCharge = orderSummaryLayout.findViewById(R.id.total_price);
        giftAmount = orderSummaryLayout.findViewById(R.id.gift_price);

        //ItemDetails
        listView = itemDetailsLayout.findViewById(R.id.itemDetailslist);

        //ItemHeaderlayout
        storeNameText = itemHeaderlayout.findViewById(R.id.storeName);

        addDiscounts=findViewById(R.id.addDiscounts);


        getOrderIdValue = getIntent().getStringExtra("OrderidDetails");

        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 1000);
        final Query getitemDetailsQuery = databaseReference.orderByChild("orderId").equalTo(String.valueOf(getOrderIdValue));
        getitemDetailsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postdataSnapshot : dataSnapshot.getChildren()) {

                    OrderDetails openTickets = postdataSnapshot.getValue(OrderDetails.class);
                    openTicketItemList = openTickets.getItemDetailList();


                    itemOrderDetails = new ItemOrderDetails(ViewItemDetailsCategoryActivity.this, openTicketItemList);
                    itemOrderDetails.notifyDataSetChanged();
                    listView.setAdapter(itemOrderDetails);

                    if (listView != null) {
                        int totalHeight = 0;

                        for (int i = 0; i < itemOrderDetails.getCount(); i++) {
                            View listItem = itemOrderDetails.getView(i, null, listView);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }

                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
                        listView.setLayoutParams(params);
                        listView.requestLayout();
                        listView.setAdapter(itemOrderDetails);
                        itemOrderDetails.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new  SayHello();

        oneTimeDiscountDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                maxidForOneTimeDiscount=dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewItemDetailsCategoryActivity.this);
                LayoutInflater inflater =(ViewItemDetailsCategoryActivity.this).getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_coupon, null);

                TextView storeName=dialogView.findViewById(R.id.dialog_title);
                TextView customerName=dialogView.findViewById(R.id.storenametxt);
                LinearLayout cancel=dialogView.findViewById(R.id.bt_no);
                Button addDiscount=dialogView.findViewById(R.id.bt_send);
                EditText discountName=dialogView.findViewById(R.id.coupon_name);
                EditText discountPrice=dialogView.findViewById(R.id.discount_amount);
                EditText dicountReason=dialogView.findViewById(R.id.discount_reason);

                final Query getOrderDetails = databaseReference.orderByChild("orderId").equalTo(getOrderIdValue);


                getOrderDetails.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                orderDetails = dataSnapshot1.getValue(OrderDetails.class);
                                openTicketItemList = orderDetails.getItemDetailList();
                                storeName.setText("Store Name: "+orderDetails.getStoreName());
                                customerName.setText("Customer Name: "+orderDetails.getCustomerName());


                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                b.show();
                b.setCancelable(false);
                addDiscount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if ("".equals(discountName.getText().toString()))
                        {
                            discountName.setError("Required");
                            return;
                        }
                        else  if ("".equals(discountPrice.getText().toString()))
                        {
                            discountPrice.setError("Required");
                            return;
                        }else if (Integer.parseInt(discountPrice.getText().toString())>=orderDetails.getTotalAmount())
                        {
                            discountPrice.setError("Discount price must be less than total amount ₹ "+orderDetails.getTotalAmount());
                            return;
                        }else if ("".equals(dicountReason.getText().toString()))
                        {
                            dicountReason.setError("Required");
                            return;
                        }else
                        {
                            oneTimeDiscount.setId(String.valueOf(maxidForOneTimeDiscount + 1));
                            oneTimeDiscount.setCustomerName(orderDetails.getCustomerName());
                            oneTimeDiscount.setCustomerId(orderDetails.getCustomerId());
                            oneTimeDiscount.setStoreId(openTicketItemList.get(0).getSellerId());
                            oneTimeDiscount.setStoreName(orderDetails.getStoreName());
                            oneTimeDiscount.setCouponName(discountName.getText().toString());
                            oneTimeDiscount.setCouponAmount(Integer.parseInt(discountPrice.getText().toString()));
                            oneTimeDiscount.setReasonForThisCoupon(dicountReason.getText().toString());
                            oneTimeDiscount.setUsedTag("false");
                            oneTimeDiscount.setTotalAmount(orderDetails.getTotalAmount());
                            oneTimeDiscount.setOrderId(orderDetails.getOrderId());
                            oneTimeDiscount.setDiscountGivenBy("Admin");

                            //Except creation date
                            oneTimeDiscountDataRef.child(String.valueOf(maxidForOneTimeDiscount+1)).setValue(oneTimeDiscount);
                            b.dismiss();
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ViewItemDetailsCategoryActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.setTitleText("Discount Added").show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ViewItemDetailsCategoryActivity.this, DashBoardActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void checkGPSConnection(Context context) {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS)
            Toast.makeText(context.getApplicationContext(), "GPS is disable!", Toast.LENGTH_LONG).show();
    }

    public void setCompulsoryAsterisk() {
        String txt_name = "No Contact Delivery";
        String colored = "*";
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(txt_name);
        int start = strBuilder.length();
        strBuilder.append(colored);
        int end = strBuilder.length();
        strBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        noContactDelivery.setText(strBuilder);
    }



    public  class SayHello extends TimerTask {
        public void run() {

            final Query getOrderDetails = databaseReference.orderByChild("orderId").equalTo(getOrderIdValue);


            getOrderDetails.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            OrderDetails orderDetails = dataSnapshot1.getValue(OrderDetails.class);
                            order_Date.setText(orderDetails.getPaymentDate());
                            order_Id.setText(orderDetails.getOrderId());
                            order_Total.setText(" ₹" + String.valueOf(orderDetails.getPaymentamount()));
                            order_status.setText(orderDetails.getOrderStatus());





                            storeNameText.setText(orderDetails.getStoreName());
                            orderTimeTxt.setText(orderDetails.getOrderTime());

                            type_Of_Payment.setText(orderDetails.getPaymentType());

                            fullName.setText(orderDetails.getFullName());
                            shipping_Address.setText(orderDetails.getShippingaddress());
                            customerPhoneNumber.setText(orderDetails.getCustomerPhoneNumber());
                            fullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_outline_24, 0, 0, 0);
                            shipping_Address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outline_location_on_24, 0, 0, 0);
                            customerPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outline_local_phone_24, 0, 0, 0);

                            amount.setText(" ₹" + String.valueOf(orderDetails.getTotalAmount()));
                            shipping.setText(" ₹" + String.valueOf(orderDetails.getTipAmount()));
                            wholeCharge.setText(" ₹" + String.valueOf(orderDetails.getPaymentamount()));
                            giftAmount.setText(" ₹" + String.valueOf(orderDetails.getGiftWrapCharge()));
                            if (orderDetails.getInstructionsToDeliveryBoy() != null && !"".equalsIgnoreCase
                                    (orderDetails.getInstructionsToDeliveryBoy())) {
                                anyInstructions.setText(orderDetails.getInstructionsToDeliveryBoy());
                            }
                            if (orderDetails.getDeliveryType() != null && !"".equals(orderDetails.getDeliveryType())) {
                                if (orderDetails.getDeliveryType().equalsIgnoreCase("No Contact Delivery")) {
                                    //noContactDelivery.setText ( "No Contact Delivery" );
                                    setCompulsoryAsterisk();
                                }
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
