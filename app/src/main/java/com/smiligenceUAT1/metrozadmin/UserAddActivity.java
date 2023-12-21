package com.smiligenceUAT1.metrozadmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetails;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.MultiSelectionSpinner;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.CommonDropDown.Role_drop_down;
import static com.smiligenceUAT1.metrozadmin.common.Constant.*;


public class UserAddActivity extends AppCompatActivity {
    EditText fssaiNumberEdt, gstNumberEdt, firsNameEdt, lastNameEdt, phoneNumberEdt, addressEdt,
            bankNameEdt, accountNumberEdt, ifscCodeEdt, aadharNumberEdt, locationEdt, bikeNumberEdt, storeNameEdt;
    Button saveUserAndCreateCrendentials;

    UserDetails userDetails = new UserDetails ();
    DatabaseReference userDetailsDataRef, sellerDetailsDataRef, deliveryBoydetailsDataRef,
            categoryDataRef, sellerCategoryMappingDataRef, thirdPartyDataRef, subCategoryRef;

    ArrayAdapter roleNameAdapter;
    Spinner roleSpinner;
    MultiSelectionSpinner categorySpinner;
    LinearLayout sellerLayout;
    RelativeLayout deliverBoyLayout;
    boolean isRoleValuePresent;
    String role_Name, generatePasswordStr;
    long sellerMaxid = 0, deliveryBoyMaxId = 0, thirdMaxId;
    AlertDialog dialog;
    ArrayList<UserDetails> userDetailsArrayList = new ArrayList<> ();
    ListView listView;
    ImageView cancel;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ArrayList<CategoryDetails> catagoryList = new ArrayList<> ();
    Button pickimage;
    ImageView storeLogo;
    Uri mimageuri;
    private StorageTask mItemStorageTask;
    StorageReference storeReference;
    Uri downloadUrl_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_user_add );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics ( displayMetrics );
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow ().setLayout ( (int) (width * 0.9), (int) (height * 0.8) );
        WindowManager.LayoutParams params = getWindow ().getAttributes ();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 15;
        getWindow ().setAttributes ( params );
        this.setFinishOnTouchOutside ( false );

        sellerLayout = findViewById ( R.id.SellersSeparateDetails );
        deliverBoyLayout = findViewById ( R.id.deliveryBoysSeparateDetails );
        cancel = findViewById ( R.id.Cancel );


        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference ( USER_DETAILS_TABLE );
        sellerDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference ( SELLER_DETAILS_TABLE );
        deliveryBoydetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference ( DELIVERY_BOY_DETAILS_TABLE );
        categoryDataRef = CommonMethods.fetchFirebaseDatabaseReference ( CATEGORY_DETAILS_TABLE );
        subCategoryRef = CommonMethods.fetchFirebaseDatabaseReference ( SubCATEGORY_DETAILS );

        sellerCategoryMappingDataRef = CommonMethods.fetchFirebaseDatabaseReference ( SELLER_CATEGORY_MAPPING_TABLE );
        thirdPartyDataRef = CommonMethods.fetchFirebaseDatabaseReference ( THIRD_PARTY_DETAILS_TABLE );
        storeReference = CommonMethods.fetchFirebaseStorageReference ( STOREDEAILS_STORAGE );

        fssaiNumberEdt = findViewById ( R.id.EditTextFssai );
        gstNumberEdt = findViewById ( R.id.EditTextGst );
        firsNameEdt = findViewById ( R.id.Edittextfirstname );
        lastNameEdt = findViewById ( R.id.Edittextlastname );
        phoneNumberEdt = findViewById ( R.id.Edittextphnnumber );
        addressEdt = findViewById ( R.id.EditTextAddress );
        bankNameEdt = findViewById ( R.id.EditTextBankName );
        accountNumberEdt = findViewById ( R.id.EditTextAccountNumber );
        ifscCodeEdt = findViewById ( R.id.EditTextIfscCode );
        aadharNumberEdt = findViewById ( R.id.EditTextAadharnumber );
        locationEdt = findViewById ( R.id.EditTextLocation );
        bikeNumberEdt = findViewById ( R.id.EditTextBikenumber );
        saveUserAndCreateCrendentials = findViewById ( R.id.Saveuser );
        roleSpinner = findViewById ( R.id.roleSpinner );
        storeNameEdt = findViewById ( R.id.EditTextStorename );
        categorySpinner = findViewById ( R.id.categorySpinner );
        pickimage = findViewById ( R.id.pickimagestore );
        storeLogo = findViewById ( R.id.storeimage );


        categoryDataRef.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot postSnapshot : dataSnapshot.getChildren () ) {
                    CategoryDetails categoryDetails = postSnapshot.getValue ( CategoryDetails.class );
                    catagoryList.add ( categoryDetails );

                }
                categorySpinner.setItems ( catagoryList );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        roleNameAdapter = new ArrayAdapter<>(UserAddActivity.this, R.layout.spinner_type, Role_drop_down);
        roleSpinner.setAdapter ( roleNameAdapter );

        roleSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    isRoleValuePresent = BOOLEAN_FALSE;
                } else {
                    isRoleValuePresent = BOOLEAN_TRUE;
                    role_Name = (String) parentView.getItemAtPosition ( position );
                    userDetails.setRoleName ( role_Name );

                    if (SELLER.equals ( role_Name )) {
                        fssaiNumberEdt.setEnabled ( true );
                        gstNumberEdt.setEnabled ( true );
                        locationEdt.setEnabled ( true );
                        sellerLayout.setVisibility ( View.VISIBLE );
                        bikeNumberEdt.setEnabled ( false );
                        storeNameEdt.setEnabled ( true );
                        bikeNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        fssaiNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                        locationEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                        gstNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                        storeNameEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );

                    } else if (DELIVERY_BOY.equals ( role_Name )) {
                        bikeNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                        fssaiNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        storeNameEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        fssaiNumberEdt.setEnabled ( false );
                        storeNameEdt.setEnabled ( false );
                        gstNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        gstNumberEdt.setEnabled ( false );
                        locationEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        locationEdt.setEnabled ( false );
                        bikeNumberEdt.setEnabled ( true );
                    } else if (THIRD_PARTY.equals ( role_Name )) {
                        fssaiNumberEdt.setEnabled ( false );
                        gstNumberEdt.setEnabled ( false );
                        locationEdt.setEnabled ( true );
                        sellerLayout.setVisibility ( View.VISIBLE );
                        bikeNumberEdt.setEnabled ( false );
                        storeNameEdt.setEnabled ( true );
                        bikeNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        fssaiNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        locationEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                        gstNumberEdt.setBackgroundColor ( getResources ().getColor ( R.color.Gradient ) );
                        storeNameEdt.setBackgroundColor ( getResources ().getColor ( R.color.White ) );
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        } );

        cancel.setOnClickListener (v -> finish ());
        pickimage.setOnClickListener (v -> openFileChooser ());

        saveUserAndCreateCrendentials.setOnClickListener (v -> {

            if ("".equals ( firsNameEdt.getText ().toString () )) {
                firsNameEdt.setError ( REQUIRED_MSG );
                return;
            } else if (!TextUtils.isValidFirstName ( firsNameEdt.getText ().toString () )) {
                firsNameEdt.setError ( INVALID_FIRSTNAME_SPECIFICATION );
                return;
            } else if ("".equals ( lastNameEdt.getText ().toString () )) {
                lastNameEdt.setError ( REQUIRED_MSG );
                return;
            } else if (!TextUtils.isValidlastName ( lastNameEdt.getText ().toString () )) {
                lastNameEdt.setError ( INVALID_LASTNAME_SPECIFICATION );
                return;
            } else if ("".equals ( phoneNumberEdt.getText ().toString () )) {
                phoneNumberEdt.setError ( REQUIRED_MSG );
                return;
            } else if (!TextUtils.validatePhoneNumber ( phoneNumberEdt.getText ().toString () )) {
                phoneNumberEdt.setError ( INVALID_PHONENUMBER );
                return;
            } else if (!TextUtils.validatePhoneNumber ( phoneNumberEdt.getText ().toString () )) {
                phoneNumberEdt.setError ( INVALID_PHONENUMBER );
                return;
            } else if (!TextUtils.isValidAlphaCharacters ( bankNameEdt.getText ().toString () )) {
                bankNameEdt.setError ( INVALID_NAME_SPECIFICATION );
                return;
            } else if (!TextUtils.isValidnumeric ( accountNumberEdt.getText ().toString () )) {
                accountNumberEdt.setError ( NUMERIC );
                return;
            } else if (!"".equals ( ifscCodeEdt.getText ().toString () )) {
                if (!TextUtils.isValidnumeric ( ifscCodeEdt.getText ().toString () )) {
                    ifscCodeEdt.setError ( ALPHA_NUMERIC );
                }
            } else if (!"".equals ( aadharNumberEdt.getText ().toString () ) && !TextUtils.isValidAadharNumber ( aadharNumberEdt.getText ().toString () )) {
                aadharNumberEdt.setError ( "Aadhar number accepts follwing pattern Eg(3675 9834 6015)" );
                return;
            } else if (!"".equals ( aadharNumberEdt.getText ().toString () ) && !TextUtils.validateFssaiNumber ( fssaiNumberEdt.getText ().toString () )) {
                fssaiNumberEdt.setError ( FSSAI_NUMBER_VALIDATION );
                return;
            } else if (!"".equals ( gstNumberEdt.getText ().toString () ) && !TextUtils.validateGstNumber ( gstNumberEdt.getText ().toString () )) {
                gstNumberEdt.setError ( GST_NUMBER_VALIDATION );
                return;

            } else if (!"".equals ( locationEdt.getText ().toString () ) && !TextUtils.validatePincode ( locationEdt.getText ().toString () )) {
                locationEdt.setError ( PINCODE_VALIDATION );
                return;
            } else if (isRoleValuePresent == BOOLEAN_FALSE) {
                Toast.makeText ( UserAddActivity.this, SELECT_ROLE_ERROR_MSG, Toast.LENGTH_SHORT ).show ();
                return;
            } else {
                generatePasswordStr = CommonMethods.generateString ( 8 );
                try {
                    String encryptedPassword = CommonMethods.encrypt ( generatePasswordStr );

                    String creationDate = DateUtils.fetchCurrentDateAndTime ();
                    userDetails.setCreationDate ( creationDate );
                    userDetails.setFirstName ( firsNameEdt.getText ().toString () );
                    userDetails.setLastName ( lastNameEdt.getText ().toString () );
                    userDetails.setPhoneNumber ( phoneNumberEdt.getText ().toString () );
                    userDetails.setAddress ( addressEdt.getText ().toString () );
                    userDetails.setPincode ( locationEdt.getText ().toString () );

                    userDetails.setPassword ( encryptedPassword );
                    userDetails.setConfirmPassword ( encryptedPassword );
                    userDetails.setEmail_Id ( "" );

                    userDetails.setGstNumber ( gstNumberEdt.getText ().toString () );
                    userDetails.setFssaiNumber ( fssaiNumberEdt.getText ().toString () );
                    userDetails.setSgstCharge ( 0 );
                    userDetails.setCgstCharge ( 0 );
                    userDetails.setDeliveryCharge ( 0 );
                    userDetails.setParcelCharge ( 0 );

                    userDetails.setAadharNumber ( aadharNumberEdt.getText ().toString () );
                    userDetails.setBankName ( bankNameEdt.getText ().toString () );
                    userDetails.setAccountNumber ( accountNumberEdt.getText ().toString () );
                    userDetails.setIfscCode ( ifscCodeEdt.getText ().toString () );
                    userDetails.setBranchName ( "" );

                    userDetails.setBikeNumber ( bikeNumberEdt.getText ().toString () );
                    userDetails.setBikeLisenceNumber ( "" );
                    userDetails.setPhoto ( "" );

                    userDetails.setStoreName ( storeNameEdt.getText ().toString () );
                    userDetails.setStoreLogo ( "" );
                    userDetails.setProductKey ( "" );
                    userDetails.setBusinessName ( "" );
                    userDetails.setBusinessType ( "" );


                    if (SELLER.equalsIgnoreCase ( userDetails.getRoleName () )) {

                        if (mimageuri != null) {
                            StorageReference imageFileStorageRef = storeReference.child ( STOREDEAILS_STORAGE
                                    + System.currentTimeMillis () + "." + (mimageuri) );

                            mItemStorageTask = imageFileStorageRef.putFile ( mimageuri ).addOnSuccessListener (
                                    taskSnapshot -> {

                                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful()) ;

                                        downloadUrl_update = urlTask.getResult();
                                        userDetails.setStoreLogo(downloadUrl_update.toString());

                                        userDetails.setUserId(String.valueOf(sellerMaxid + 1));
                                        sellerDetailsDataRef.child(String.valueOf(sellerMaxid + 1)).setValue(userDetails);
                                        ArrayList<CategoryDetails> selectedCategorydetails = categorySpinner.getSelectedItems();

                                        if (selectedCategorydetails != null && selectedCategorydetails.size() > 0) {

                                            Iterator selectedCategoryIterator = selectedCategorydetails.iterator();

                                            while (selectedCategoryIterator.hasNext()) {
                                                CategoryDetails categoryDetails = (CategoryDetails) selectedCategoryIterator.next();
                                                List<UserDetails> sellerList = categoryDetails.getSellerList();
                                                UserDetails sellerUserDetails = new UserDetails();

                                                sellerUserDetails.setUserId(String.valueOf(sellerMaxid + 1));
                                                sellerUserDetails.setStoreLogo("");
                                                sellerUserDetails.setStoreName("");

                                                sellerList.add(userDetails);
                                                categoryDetails.setSellerList(sellerList);

                                                categoryDataRef.child(String.valueOf(categoryDetails.getCategoryid())).setValue(categoryDetails);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText ( UserAddActivity.this, "Please select the image", Toast.LENGTH_SHORT ).show ();
                        }


                    } else if (DELIVERY_BOY.equalsIgnoreCase ( userDetails.getRoleName () )) {

                        userDetails.setUserId ( String.valueOf ( deliveryBoyMaxId + 1 ) );
                        deliveryBoydetailsDataRef.child ( String.valueOf ( deliveryBoyMaxId + 1 ) ).setValue ( userDetails );
                    } else if (THIRD_PARTY.equalsIgnoreCase ( userDetails.getRoleName () )) {

                        userDetails.setUserId ( String.valueOf ( thirdMaxId + 1 ) );
                        thirdPartyDataRef.child ( String.valueOf ( thirdMaxId + 1 ) ).setValue ( userDetails );
                    }

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder ( UserAddActivity.this );
                    View mView = getLayoutInflater ().inflate ( R.layout.create_login_crendentials, null );
                    final TextView userName = mView.findViewById ( R.id.Usernamedialog );
                    final TextView loginpassword = mView.findViewById ( R.id.Passworddialog );
                    userName.setText ( "UserName:  " + phoneNumberEdt.getText ().toString () );
                    loginpassword.setText ( "Password:  " + generatePasswordStr );
                    mBuilder.setNegativeButton ( "OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        firsNameEdt.setText(TEXT_BLANK);
                        lastNameEdt.setText(TEXT_BLANK);
                        phoneNumberEdt.setText(TEXT_BLANK);
                        fssaiNumberEdt.setText(TEXT_BLANK);
                        gstNumberEdt.setText(TEXT_BLANK);
                        addressEdt.setText(TEXT_BLANK);
                        bankNameEdt.setText(TEXT_BLANK);
                        accountNumberEdt.setText(TEXT_BLANK);
                        ifscCodeEdt.setText(TEXT_BLANK);
                        aadharNumberEdt.setText(TEXT_BLANK);
                        locationEdt.setText(TEXT_BLANK);
                        bikeNumberEdt.setText(TEXT_BLANK);
                        roleSpinner.setSelection(0);
                        storeNameEdt.setText(TEXT_BLANK);
                        dialog.dismiss();
                        finish();
                    });
                    mBuilder.setView ( mView );
                    dialog = mBuilder.create ();
                    dialog.show ();
                    dialog.setCancelable ( BOOLEAN_FALSE );
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        });

        sellerDetailsDataRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()) {
                    sellerMaxid = (dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );


        deliveryBoydetailsDataRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()) {
                    deliveryBoyMaxId = (dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        thirdPartyDataRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()) {
                    thirdMaxId = (dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }

    private void openFileChooser() {
        Intent intent = new Intent ();
        intent.setType ( "image/*" );
        intent.setAction ( Intent.ACTION_GET_CONTENT );
        startActivityForResult ( intent, PICK_IMAGE_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData () != null) {
            mimageuri = data.getData ();
            Glide.with ( UserAddActivity.this ).load ( mimageuri ).into ( storeLogo );
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(UserAddActivity.this, UserRoleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}