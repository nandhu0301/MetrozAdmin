package com.smiligenceUAT1.metrozadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetailsNew;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;

import java.util.ArrayList;

import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERYBOY_DETAILS;

public class DeliveryBoyProfileActivity extends AppCompatActivity {
String getDeliveryBoyId;
    DatabaseReference databaseReference;
    EditText storeNameEditText, firstname_storeEditText, lastNameEditText, selectedcategoryEditText, storeAddress,
            email_store, phonenumber_store, pincode_store, bankname, branchName_store, Accountnumber, ifsc_store, vehicleNumber, licenseNumber, aadharnumber, gstnumber_store;
    ImageView storeLogo, aadharImage, fssaiCertificate;
    ArrayList<CategoryDetailsNew> categoryDetailsArrayList = new ArrayList<>();
    String getStoreID;
    ImageView backButton;
    Query query;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_profile);

        firstname_storeEditText = findViewById(R.id.firstname_store);
        lastNameEditText = findViewById(R.id.lastName);
        email_store = findViewById(R.id.email_store);
        phonenumber_store = findViewById(R.id.phonenumber_store);
        pincode_store = findViewById(R.id.pincode_store);
        bankname = findViewById(R.id.bankname_store);
        branchName_store = findViewById(R.id.branchName_store);
        Accountnumber = findViewById(R.id.Accountnumber);
        ifsc_store = findViewById(R.id.ifsc_store);
        aadharnumber = findViewById(R.id.aadharnumber);
        storeLogo = findViewById(R.id.storelogo);
        aadharImage = findViewById(R.id.aadharID);
        fssaiCertificate = findViewById(R.id.fssaicertificate);
        backButton = findViewById(R.id.back_button_deliveryboy);
        licenseNumber=findViewById(R.id.licensenumber);
        vehicleNumber=findViewById(R.id.Vehiclenumber);


        databaseReference=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(DELIVERYBOY_DETAILS);
        getDeliveryBoyId = getIntent().getStringExtra("deliveryBoyID");
        query=databaseReference.child(getDeliveryBoyId);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( DeliveryBoyProfileActivity.this, DeliveryBoyHistory.class );
                intent.putExtra ( "deliveryBoyID",getDeliveryBoyId);
                intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( intent );
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeliveryBoy deliveryBoy=dataSnapshot.getValue(DeliveryBoy.class);

                firstname_storeEditText.setText(deliveryBoy.getFirstName());
                lastNameEditText.setText(deliveryBoy.getLastName());
                email_store.setText(deliveryBoy.getEmailId());
                phonenumber_store.setText(deliveryBoy.getMobileNumber());
                pincode_store.setText(deliveryBoy.getZipcode());
                bankname.setText(deliveryBoy.getBankName());
                branchName_store.setText(deliveryBoy.getBankBranchName());
                Accountnumber.setText(deliveryBoy.getAccountNumber());
                ifsc_store.setText(deliveryBoy.getBankIfscCode());
                aadharnumber.setText(deliveryBoy.getAadharNumber());
                vehicleNumber.setText(deliveryBoy.getBikeNumber());
                licenseNumber.setText(deliveryBoy.getBikeLicenseNumber());


                Glide.with ( DeliveryBoyProfileActivity.this).
                        load( deliveryBoy.getDeliveryBoyProfile () ).into ( storeLogo );

                aadharImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder ( DeliveryBoyProfileActivity.this );
                        View mView = getLayoutInflater ().inflate ( R.layout.seller_aadhar_dialog, null );
                        ImageView AadharImage = mView.findViewById ( R.id.Aadharimage_store );
                        ImageView Cancel=mView.findViewById(R.id.cancelIcon);
                        TextView title = mView.findViewById(R.id.texttitle);

                        title.setText("Aadhar Image");



                        mBuilder.setView ( mView );
                        dialog = mBuilder.create ();
                        dialog.show ();
                        dialog.setCancelable ( false );

                        Cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });



                                Glide.with ( DeliveryBoyProfileActivity.this).
                                        load( deliveryBoy.getAadharIdProof() ).into ( AadharImage );

                    }
                });
                fssaiCertificate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeliveryBoyProfileActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.seller_aadhar_dialog, null);
                        ImageView AadharImage = mView.findViewById(R.id.Aadharimage_store);
                        ImageView Cancel = mView.findViewById(R.id.cancelIcon);
                        TextView title = mView.findViewById(R.id.texttitle);

                        title.setText("Driving License");

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


                        Glide.with(DeliveryBoyProfileActivity.this).
                                load(deliveryBoy.getDrivingLicenseProof()).into(AadharImage);
                    }
                });
                Glide.with ( DeliveryBoyProfileActivity.this).
                        load( deliveryBoy.getAadharIdProof() ).into ( aadharImage );
                Glide.with ( DeliveryBoyProfileActivity.this).
                        load( deliveryBoy.getDrivingLicenseProof() ).into ( fssaiCertificate );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}