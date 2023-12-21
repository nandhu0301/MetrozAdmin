package com.smiligenceUAT1.metrozadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.adapter.DeliveryBoysOrderDetailsAdapter;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;

import java.util.ArrayList;

public class DeliveryBoyOrderedDetails extends AppCompatActivity {

    DatabaseReference databaseReference;
    String getDeliveryBoyId;
    ArrayList<OrderDetails> orderDetailsList=new ArrayList<>();
    ArrayList<String> orderDetailsArrayList=new ArrayList<>();
    Query query;
    DeliveryBoysOrderDetailsAdapter deliveryBoysOrderDetailsAdapter;
    ListView deliveryboyOrderhistory;
    ImageView backicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_ordered_details);
        deliveryboyOrderhistory=findViewById(R.id.deliveryboyOrderhistory);
        backicon=findViewById(R.id.backicon);

        getDeliveryBoyId = getIntent().getStringExtra("deliveryBoyID");
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( DeliveryBoyOrderedDetails.this, DeliveryBoyHistory.class );
                intent.putExtra ( "deliveryBoyID",getDeliveryBoyId );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        });

        databaseReference= FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderDetails");
        query=databaseReference.orderByChild("deliverboyId").equalTo(getDeliveryBoyId);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    orderDetailsList.clear();
                    for(DataSnapshot orderSnap:dataSnapshot.getChildren()){
                        OrderDetails orderDetails=orderSnap.getValue(OrderDetails.class);
                        if (orderDetails.getOrderStatus().equals("Delivered")) {
                            orderDetailsList.add(orderDetails);
                            orderDetailsArrayList.add(orderDetails.getDeliverboyId());
                        }

                    }

                    if (!orderDetailsList.isEmpty()) {
                        deliveryBoysOrderDetailsAdapter = new DeliveryBoysOrderDetailsAdapter(DeliveryBoyOrderedDetails.this, orderDetailsList);
                        deliveryBoysOrderDetailsAdapter.notifyDataSetChanged();
                        deliveryboyOrderhistory.setAdapter(deliveryBoysOrderDetailsAdapter);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}