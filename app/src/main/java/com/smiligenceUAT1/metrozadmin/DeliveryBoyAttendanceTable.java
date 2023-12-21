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
import com.smiligenceUAT1.metrozadmin.adapter.AttendanceAdapter;
import com.smiligenceUAT1.metrozadmin.bean.CheckIn_CheckOut;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyAttendanceTable extends AppCompatActivity {

    String getDeliveryBoyId;
    DatabaseReference databaseReference;
    Query query;
    List<CheckIn_CheckOut> checkInList=new ArrayList<>();
    AttendanceAdapter attendanceAdapter;
    ListView attendanceList;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_attendance_table);
        attendanceList=findViewById(R.id.attendanceList);
        back=findViewById(R.id.back);

        getDeliveryBoyId = getIntent().getStringExtra("deliveryBoyID");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( DeliveryBoyAttendanceTable.this, DeliveryBoyHistory.class );
                intent.putExtra ( "deliveryBoyID",getDeliveryBoyId );
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        });

        databaseReference= FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyAttendanceTable");
        query=databaseReference.child(getDeliveryBoyId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){

                    for(DataSnapshot deliveryBoyAttendance:dataSnapshot.getChildren()){

                        CheckIn_CheckOut checkIn_checkOut=deliveryBoyAttendance.getValue(CheckIn_CheckOut.class);
                        checkInList.add(checkIn_checkOut);

                    }

                    if (!checkInList.isEmpty()) {
                        attendanceAdapter = new AttendanceAdapter(DeliveryBoyAttendanceTable.this, checkInList);
                        attendanceAdapter.notifyDataSetChanged();
                        attendanceList.setAdapter(attendanceAdapter);

                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}