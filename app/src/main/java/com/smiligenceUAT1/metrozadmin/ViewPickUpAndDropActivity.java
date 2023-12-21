package com.smiligenceUAT1.metrozadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.PickUpAndDrop;
import com.smiligenceUAT1.metrozadmin.common.Constant;

import java.util.ArrayList;

public class ViewPickUpAndDropActivity extends AppCompatActivity
{
    ImageView backToHome;
    TextView order_Date, order_Id, order_Total, type_Of_Payment, amount, shipping, wholeCharge, pickUpAddress, dropAddress, itemDetailsForPickup, baseFair, minimumFair, minimumFairTxt, order_status, orderTimeTxt, distance_txt, deliverPinTxt, noContactDelivery, anyInstructions, customerName, customerPhoneNumber;
    DatabaseReference databaseReference;
    private ArrayList<PickUpAndDrop> pickUpAndDropArrayList = new ArrayList<> ();
    ConstraintLayout orderDetailsLayout, paymentDetailsLayout, orderSummaryLayout, shippingLayout, itemDetailsLayout, specialinstructionLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_view_pick_up_and_drop );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        orderDetailsLayout = findViewById ( R.id.order_details_layout );
        order_Date = orderDetailsLayout.findViewById ( R.id.orderdate );
        order_Id = orderDetailsLayout.findViewById ( R.id.bill_num );
        order_Total = orderDetailsLayout.findViewById ( R.id.order_total );
        order_status = orderDetailsLayout.findViewById ( R.id.order_status );
        orderTimeTxt = orderDetailsLayout.findViewById ( R.id.ordertimetxt );


        databaseReference = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "OrderDetails" );

        paymentDetailsLayout = findViewById ( R.id.payment_details );
        type_Of_Payment = paymentDetailsLayout.findViewById ( R.id.type_of_payment );

        orderSummaryLayout = findViewById ( R.id.cart_total_amount_layout );
        amount = orderSummaryLayout.findViewById ( R.id.tips_price1 );
        shipping = orderSummaryLayout.findViewById ( R.id.tips_price );
        wholeCharge = orderSummaryLayout.findViewById ( R.id.total_price );
        baseFair = orderSummaryLayout.findViewById ( R.id.base_fair_txt );
        minimumFair = orderSummaryLayout.findViewById ( R.id.minimumfairtxtres );
        minimumFairTxt = orderSummaryLayout.findViewById ( R.id.minimumfairtxt );

        shippingLayout = findViewById ( R.id.start_end_address );
        pickUpAddress = shippingLayout.findViewById ( R.id.pickUpAddressTxt );
        dropAddress = shippingLayout.findViewById ( R.id.deliveryAddressTxt );
        customerName = shippingLayout.findViewById ( R.id.customerNameshipping );
        customerPhoneNumber = shippingLayout.findViewById ( R.id.customerMobileNumberShipping );

        specialinstructionLayout = findViewById ( R.id.special_instructions );

        noContactDelivery = findViewById ( R.id.noContactDelivery );
        anyInstructions = findViewById ( R.id.specialinsructions );

        itemDetailsLayout = findViewById ( R.id.itemDetails );
        itemDetailsForPickup = itemDetailsLayout.findViewById ( R.id.full_name );

        String getOrderIdValue = getIntent ().getStringExtra ( "OrderidDetails" );

        backToHome = findViewById ( R.id.redirecttohome );

        backToHome.setOnClickListener (v -> {
            Intent backToHomeintent = new Intent ( ViewPickUpAndDropActivity.this, OrderDetailsActivity.class );
            startActivity ( backToHomeintent );
        });


        final Query getitemDetailsQuery = databaseReference.orderByChild ( "orderId" ).equalTo ( String.valueOf ( getOrderIdValue ) );

        getitemDetailsQuery.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot postdataSnapshot : dataSnapshot.getChildren () ) {

                    OrderDetails orderDetails = postdataSnapshot.getValue ( OrderDetails.class );
                    pickUpAndDropArrayList = (ArrayList<PickUpAndDrop>) orderDetails.getPickUpAndDroplist ();
                    PickUpAndDrop pickUpAndDrop = pickUpAndDropArrayList.get ( 0 );
                    baseFair.setText ( "₹ " + Constant.BASIC_FAIR );
                    if (pickUpAndDrop.getTotalDistance () > 5) {
                        minimumFairTxt.setText ( "Minimum Fair ₹7 Per Km" );
                        minimumFair.setText ( "₹ " + (Constant.PER_Km * pickUpAndDrop.getTotalDistance ()) );
                    } else {
                        minimumFairTxt.setText ( "Minimum Fair less than 5 Kms" );
                        minimumFair.setText ( "₹ " + Constant.MINIMUM_FAIR );
                    }

                    pickUpAddress.setText ( pickUpAndDrop.getPickupAddress () );
                    dropAddress.setText ( pickUpAndDrop.getDropAddress () );
                    for ( int i = 0; i < pickUpAndDrop.getDeliverObject ().size (); i++ ) {
                        itemDetailsForPickup.append ( pickUpAndDrop.getDeliverObject ().get ( i ) + " , " );
                    }

                    if (orderDetails.getInstructionsToDeliveryBoy () != null && !"".equalsIgnoreCase ( orderDetails.getInstructionsToDeliveryBoy () )) {
                        anyInstructions.setText ( orderDetails.getInstructionsToDeliveryBoy () );
                    }
                    if (orderDetails.getDeliveryType () != null && !"".equals ( orderDetails.getDeliveryType () )) {
                        if (orderDetails.getDeliveryType ().equalsIgnoreCase ( "No Contact Delivery" )) {
                            setCompulsoryAsterisk ();
                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        final Query getOrderDetails = databaseReference.orderByChild ( "orderId" ).equalTo ( getOrderIdValue );

        getOrderDetails.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount () > 0) {
                    for ( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren () ) {
                        OrderDetails orderDetails = dataSnapshot1.getValue ( OrderDetails.class );
                        order_Date.setText (orderDetails.getPaymentDate() );
                        order_Id.setText ( orderDetails.getOrderId () );
                        order_Total.setText ( " ₹" + String.valueOf ( orderDetails.getPaymentamount () ) );
                        order_status.setText ( orderDetails.getOrderStatus () );
                        orderTimeTxt.setText ( orderDetails.getOrderTime () );


                        type_Of_Payment.setText ( orderDetails.getPaymentType () );
                        customerName.setText ( orderDetails.getCustomerName () );
                        customerPhoneNumber.setText ( orderDetails.getCustomerPhoneNumber () );

                        customerName.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_baseline_person_outline_24, 0, 0, 0 );
                        customerPhoneNumber.setCompoundDrawablesWithIntrinsicBounds ( R.drawable.ic_outline_local_phone_24, 0, 0, 0 );

                        amount.setText ( " ₹" + String.valueOf ( orderDetails.getTotalAmount () ) );
                        shipping.setText ( " ₹" + String.valueOf ( orderDetails.getTipAmount () ) );
                        wholeCharge.setText ( " ₹" + String.valueOf ( orderDetails.getPaymentamount () ) );
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        } );
    }
    public void setCompulsoryAsterisk() {
        String txt_name = "No Contact Delivery";
        String colored = "*";
        SpannableStringBuilder strBuilder = new SpannableStringBuilder ();
        strBuilder.append ( txt_name );
        int start = strBuilder.length ();
        strBuilder.append ( colored );
        int end = strBuilder.length ();
        strBuilder.setSpan ( new ForegroundColorSpan ( Color.RED ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        noContactDelivery.setText ( strBuilder );
    }
}