package com.smiligenceUAT1.metrozadmin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.adapter.SellerAdapter;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetails;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetailsNew;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_APPROVE_SELLERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.*;

public class ApproveSellersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;

    DatabaseReference SellerloginDetails, categoryDetailsdataref,userDetailsDataRef;
    long sellerCount = 0;
    CategoryDetails categoryDetails = new CategoryDetails();
    ArrayList<CategoryDetailsNew> categoryDetailsNewList = new ArrayList<> ();
    List<UserDetails> sellerList = new ArrayList<> ();
    List<UserDetails> userDetailsList = new ArrayList<> ();
    ListView listView;
    UserDetails sellerDetails = new UserDetails();
    UserDetails userDetails = new UserDetails();
    AlertDialog dialog;
   // long maxid = 0;
    int swipeCount = 0;
    String  s="Null";
    ArrayList<CategoryDetails> categoryList = new ArrayList<> ();
    NavigationView navigationView;
    boolean isChecked=true;
    String userNameString;

    String variableAssBas="false",variableAssSil="false",variableAssGold="false";
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_approve_sellers );

        Toolbar toolbar1 = findViewById ( R.id.toolbar );
        toolbar1.setTitle ( TITLE_APPROVE_SELLERS );
        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener ( toggle );
        toggle.getDrawerArrowDrawable ().setColor ( getResources ().getColor ( R.color.White ) );
        toggle.syncState ();
        navigationView = findViewById ( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener ( ApproveSellersActivity.this );

        navigationView.setCheckedItem(R.id.ApproveSellers);

        UserRoleActivity.menuNav = navigationView.getMenu ();
        mHeaderView = navigationView.getHeaderView ( 0 );

        textViewUsername = mHeaderView.findViewById ( R.id.name );
        textViewEmail = mHeaderView.findViewById ( R.id.roleName );

        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);

        if(!"".equals(UserRoleActivity.saved_userName)&& !"".equals(UserRoleActivity.saved_userName)) {

            userNameString=UserRoleActivity.saved_userName;
        }  if (!"".equals(DashBoardActivity.saved_userName)&& !"".equals(DashBoardActivity.saved_userName)){
            userNameString=DashBoardActivity.saved_userName;
        }

        final Query roleNameQuery = userDetailsDataRef.orderByChild(PHONE_NUM_COLUMN).equalTo((userNameString));

        roleNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserDetails loginUserDetailslist = snap.getValue(UserDetails.class);
                    if (userNameString != null && !"".equals ( userNameString )) {
                        textViewUsername.setText ( loginUserDetailslist.getFirstName().toUpperCase() );
                        textViewEmail.setText(loginUserDetailslist.getRoleName().toUpperCase());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = findViewById ( R.id.sellersList );

        SellerloginDetails = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "SellerLoginDetails" );
        categoryDetailsdataref = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "Category" );

        categoryDetailsdataref.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount () > 0) {
                    categoryList.clear();
                    for ( DataSnapshot categorySnap : dataSnapshot.getChildren () ) {
                        CategoryDetails categoryDetails = categorySnap.getValue ( CategoryDetails.class );
                        categoryList.add ( categoryDetails );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        Query waitingForApprovalQuery = SellerloginDetails.orderByChild ( "approvalStatus" ).equalTo ( "Waiting for approval" );

        waitingForApprovalQuery.addValueEventListener ( new ValueEventListener () {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildrenCount () > 0) {

                    if (isChecked == true) {
                        userDetailsList.clear();
                        for (DataSnapshot deliveryBoySnap : dataSnapshot.getChildren()) {
                            sellerDetails = deliveryBoySnap.getValue(UserDetails.class);
                            userDetailsList.add(sellerDetails);
                        }


                        SellerAdapter sellerAdapter = new SellerAdapter(ApproveSellersActivity.this, userDetailsList);
                        listView.setAdapter(sellerAdapter);
                        sellerAdapter.notifyDataSetChanged();

                        if (listView != null) {
                            int totalHeight = 0;

                            for (int i = 0; i < sellerAdapter.getCount(); i++) {
                                View listItem = sellerAdapter.getView(i, null, listView);
                                listItem.measure(0, 0);
                                totalHeight += listItem.getMeasuredHeight();
                            }
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
                            listView.setLayoutParams(params);
                            listView.requestLayout();
                            listView.setAdapter(sellerAdapter);
                            sellerAdapter.notifyDataSetChanged();


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );






        listView.setOnItemClickListener ((AdapterView.OnItemClickListener) (parent, view, position, id) -> {
            sellerDetails = userDetailsList.get ( position );
            AlertDialog.Builder mBuilder = new AlertDialog.Builder ( ApproveSellersActivity.this );
            View mView = getLayoutInflater ().inflate ( R.layout.seller_details, null );

            final RelativeLayout applicantHeader = mView.findViewById ( R.id.applicantHeader );

            final ImageView storeLogo = mView.findViewById ( R.id.storeProfile );
            final ImageView aadharProof = mView.findViewById ( R.id.aadharProfile );
            final ImageView fssaiCertificate = mView.findViewById ( R.id.fssaiProfile );


            final LinearLayout detailsLayout = mView.findViewById ( R.id.detailsLayout );
            final TextView imageHeaderName = mView.findViewById ( R.id.imageHeader );

            TextView firstName = mView.findViewById ( R.id.firstName1 );
            TextView lastName = mView.findViewById ( R.id.lastname1 );
            TextView mobileNumber = mView.findViewById ( R.id.mobilenumbertxt1 );
            TextView emailid = mView.findViewById ( R.id.emailid1 );
            final TextView storeAddress = mView.findViewById ( R.id.storeaddress1 );
            TextView pincode = mView.findViewById ( R.id.pincode1 );
            TextView bankname = mView.findViewById ( R.id.bankname1 );
            TextView branchName = mView.findViewById ( R.id.branchName1 );
            TextView accountNumber = mView.findViewById ( R.id.accountnumber1 );
            TextView ifscCode = mView.findViewById ( R.id.ifscCode1 );
            TextView businessName = mView.findViewById ( R.id.businessname1 );
            TextView businessType = mView.findViewById ( R.id.businesstype1 );
            TextView fssaiNumber = mView.findViewById ( R.id.fssaiNumber1 );
            final TextView aadharNumber = mView.findViewById ( R.id.aadharNumber1 );
            TextView gstNumber = mView.findViewById ( R.id.gstnumber1 );
            final TextView commentsIfAny = mView.findViewById ( R.id.commentsSectionEdt );


            Button approveButton = mView.findViewById ( R.id.approve );
            Button declineButton = mView.findViewById ( R.id.reject );


            ImageView cancelButton = mView.findViewById ( R.id.canceldetails );


            firstName.setText ( sellerDetails.getFirstName () );
            lastName.setText ( sellerDetails.getLastName () );
            mobileNumber.setText ( sellerDetails.getPhoneNumber () );
            emailid.setText ( sellerDetails.getEmail_Id () );
            storeAddress.setText ( sellerDetails.getAddress () );
            pincode.setText ( sellerDetails.getPincode () );
            bankname.setText ( sellerDetails.getBankName () );
            branchName.setText ( sellerDetails.getBranchName () );
            accountNumber.setText ( sellerDetails.getAccountNumber () );
            ifscCode.setText ( sellerDetails.getIfscCode () );
            businessName.setText ( sellerDetails.getStoreName () );
            businessType.setText ( sellerDetails.getBusinessType () );
            fssaiNumber.setText ( sellerDetails.getFssaiNumber () );
            aadharNumber.setText ( sellerDetails.getAadharNumber () );
            gstNumber.setText ( sellerDetails.getGstNumber () );

            mBuilder.setView ( mView );
            dialog = mBuilder.create ();
            dialog.show ();
            dialog.setCancelable ( false );



            approveButton.setOnClickListener ((View.OnClickListener) v -> {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = ApproveSellersActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.paymenttype_radio_layout, null);
                dialogBuilder.setView(dialogView);

                RadioButton basicradio=dialogView.findViewById(R.id.basicRadio);
                RadioButton silverradio=dialogView.findViewById(R.id.silverRadio);
                RadioButton goldradio=dialogView.findViewById(R.id.goldRadio);
                TextView ok=dialogView.findViewById(R.id.ok_pay);
                TextView cancel=dialogView.findViewById(R.id.cancel_pay);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (basicradio.isChecked()==true)
                        {
                            variableAssBas="Basic";
                        }else if (silverradio.isChecked()==true)
                        {
                            variableAssBas="Silver";
                        }else if (goldradio.isChecked()==true)
                        {
                            variableAssBas="Gold";
                        }
                        if (basicradio.isChecked()==true || silverradio.isChecked()==true || goldradio.isChecked()==true)
                        {
                            Query sellerCategoryDetails = SellerloginDetails.orderByChild("userId").equalTo(sellerDetails.getUserId());

                            sellerCategoryDetails.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        categoryDetailsNewList.clear();


                                        for (DataSnapshot deliveryBoySnap : dataSnapshot.getChildren())
                                        {
                                            sellerDetails = deliveryBoySnap.getValue(UserDetails.class);
                                            categoryDetailsNewList = (ArrayList<CategoryDetailsNew>) sellerDetails.getCategoryList();
                                            sellerList.clear();

                                            for (int sellercategoryIterator = 0; sellercategoryIterator < categoryDetailsNewList.size(); sellercategoryIterator++) {
                                                for (int categoryIterator = 0; categoryIterator < categoryList.size(); categoryIterator++) {

                                                    final CategoryDetailsNew categoryDetailsNew = categoryDetailsNewList.get(sellercategoryIterator);
                                                    String categoryId = categoryList.get(categoryIterator).getCategoryid();

                                                    if (categoryDetailsNew.getCategoryid().equalsIgnoreCase(categoryId)) {
                                                        CategoryDetails updatedCategoryDetails = categoryList.get(categoryIterator);
                                                        sellerList = updatedCategoryDetails.getSellerList();
                                                        sellerList.add(sellerDetails);
                                                        updatedCategoryDetails.setSellerList(sellerList);
                                                        if (!((Activity) ApproveSellersActivity.this).isFinishing()) {
                                                            categoryDetailsdataref.child(categoryId).setValue(updatedCategoryDetails);
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Intent intent = getIntent();
                                    startActivity(intent);
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            new backGroundClass().execute();
                            alertDialog.dismiss();
                        }else
                        {
                            Toast.makeText(ApproveSellersActivity.this, "Please select type of settlement", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
            });

            declineButton.setOnClickListener (v -> {

                commentsIfAny.setVisibility(View.VISIBLE);
                if (commentsIfAny.getVisibility() == View.VISIBLE) {
                    if ("".equals(commentsIfAny.getText().toString())) {
                        commentsIfAny.setError("Required");
                    } else {
                        DatabaseReference deliveryBoyDetailsDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("SellerLoginDetails").child(sellerDetails.getUserId());
                        deliveryBoyDetailsDataRef.child("approvalStatus").setValue("Rejected");
                        deliveryBoyDetailsDataRef.child("commentsIfAny").setValue(commentsIfAny.getText().toString());
                        dialog.dismiss();
                        Intent intent = getIntent();
                        startActivity(intent);
                        Toast.makeText(ApproveSellersActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            mView.setOnTouchListener ( new OnSwipeTouchListener( ApproveSellersActivity.this ) {
                public void onSwipeRight() {
                    swipeCount = swipeCount - 1;
                    if (swipeCount == 0) {
                        detailsLayout.setVisibility ( View.VISIBLE );
                        storeLogo.setVisibility ( View.INVISIBLE );
                        aadharProof.setVisibility ( View.INVISIBLE );
                        fssaiCertificate.setVisibility ( View.INVISIBLE );
                        applicantHeader.setVisibility ( View.VISIBLE );
                        imageHeaderName.setText ( "Seller details" );
                    }
                    if (swipeCount == 1) {

                        detailsLayout.setVisibility ( View.INVISIBLE );
                        storeLogo.setVisibility ( View.VISIBLE );
                        aadharProof.setVisibility ( View.INVISIBLE );
                        applicantHeader.setVisibility ( View.VISIBLE );
                        fssaiCertificate.setVisibility ( View.INVISIBLE );
                        imageHeaderName.setText ( "Store Logo" );
                        Picasso.get ().load ( sellerDetails.getStoreLogo () ).into ( storeLogo );

                    } else if (swipeCount == 2) {

                        detailsLayout.setVisibility ( View.INVISIBLE );
                        storeLogo.setVisibility ( View.INVISIBLE );
                        aadharProof.setVisibility ( View.VISIBLE );
                        applicantHeader.setVisibility ( View.VISIBLE );
                        fssaiCertificate.setVisibility ( View.INVISIBLE );
                        imageHeaderName.setText ( "Aadhar Id" );
                        Picasso.get ().load ( sellerDetails.getAadharImage () ).into ( aadharProof );
                    }
                }

                public void onSwipeLeft() {
                    swipeCount = swipeCount + 1;
                    if (swipeCount == 1) {
                        detailsLayout.setVisibility ( View.INVISIBLE );
                        storeLogo.setVisibility ( View.VISIBLE );
                        aadharProof.setVisibility ( View.INVISIBLE );
                        imageHeaderName.setText ( "Store Logo" );
                        fssaiCertificate.setVisibility ( View.INVISIBLE );
                        Picasso.get ().load ( sellerDetails.getStoreLogo () ).into ( storeLogo );
                    } else if (swipeCount == 2) {
                        detailsLayout.setVisibility ( View.INVISIBLE );
                        storeLogo.setVisibility ( View.INVISIBLE );
                        aadharProof.setVisibility ( View.VISIBLE );
                        fssaiCertificate.setVisibility ( View.INVISIBLE );
                        imageHeaderName.setText ( "Aadhar Id" );
                        Picasso.get ().load ( sellerDetails.getAadharImage () ).into ( aadharProof );
                    } else if (swipeCount == 3) {
                        detailsLayout.setVisibility ( View.INVISIBLE );
                        storeLogo.setVisibility ( View.INVISIBLE );
                        aadharProof.setVisibility ( View.INVISIBLE );
                        fssaiCertificate.setVisibility ( View.VISIBLE );
                        imageHeaderName.setText ( "Fssai Certificate" );
                        Picasso.get ().load ( sellerDetails.getFssaiCertificateImage () ).into ( fssaiCertificate );
                    }
                }
            } );

            cancelButton.setOnClickListener (v -> {
                dialog.dismiss();
                swipeCount = 0;
            });
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.User_role_id) {
            Intent intent = new Intent(getApplicationContext(), UserRoleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.category) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Add_Discounts_id) {
            Intent intent = new Intent(getApplicationContext(), ViewDiscountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Add_Advertisment_id) {
            Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.AssignOrders) {
            Intent intent = new Intent(getApplicationContext(), AssignOrdersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (id == R.id.ApproveSellers) {
            Intent intent = new Intent ( getApplicationContext (), ApproveSellersActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        } else if (id == R.id.View_Orders_id) {
            Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Logout) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( ApproveSellersActivity.this );
            bottomSheetDialog.setContentView ( R.layout.logout_confirmation );
            Button logout = bottomSheetDialog.findViewById ( R.id.logout );
            Button stayinapp = bottomSheetDialog.findViewById ( R.id.stayinapp );

            bottomSheetDialog.show ();
            bottomSheetDialog.setCancelable ( false );

            logout.setOnClickListener (v -> {

                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                bottomSheetDialog.dismiss ();
            });
            stayinapp.setOnClickListener (v -> {
                bottomSheetDialog.dismiss ();
                navigationView.setCheckedItem(R.id.ApproveSellers);
            });

        }
        else if (id == R.id.subcategory) {
            Intent intent = new Intent(getApplicationContext(), SubCategoryAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.Add_tips_id) {
            Intent intent = new Intent ( getApplicationContext (), TipsAddActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }else if (id == R.id.ApproveDeliveryBoys) {
            Intent intent = new Intent ( getApplicationContext (), DeliveryBoyApprovalActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }else if (id == R.id.Add_Delivery_Fair) {
            Intent intent = new Intent(getApplicationContext(), MaintainDeliveryFairActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.Approve_items_id) {
            Intent intent = new Intent ( getApplicationContext (), ItemApprovalActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        } else if (id == R.id.contactus) {
            Intent intent = new Intent ( getApplicationContext (), ContactUsActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }
        else if (id == R.id.uploadcontactdetails) {
            Intent intent = new Intent ( getApplicationContext (), AdminContactUploadingActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }else if (id == R.id. dashboard) {
            Intent intent = new Intent ( getApplicationContext (), DashBoardActivity.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        } else if (id == R.id.storeHistory) {
            Intent intent = new Intent ( getApplicationContext (), StoreHistory.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }else if (id == R.id.deliveryBoyHistory) {
            Intent intent = new Intent ( getApplicationContext (), DeliveryBoyHistory.class );
            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity ( intent );
        }else if (id == R.id.viewadvertisement) {
            Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.bulkupload) {
            Intent intent = new Intent(getApplicationContext(), BulkUploadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (id == R.id.adminWeeklyPayments) {
            Intent intent = new Intent(getApplicationContext(), AdminWeeklyPaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class backGroundClass extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids)
        {
            DatabaseReference deliveryBoyDetailsDataRef = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference ( "SellerLoginDetails" ).child ( sellerDetails.getUserId () );
            deliveryBoyDetailsDataRef.child ( "approvalStatus" ).setValue ( "Approved" );
            deliveryBoyDetailsDataRef.child ( "paymentType" ).setValue ( variableAssBas );
            deliveryBoyDetailsDataRef.child("formattedDate").setValue(DateUtils.fetchFormatedCurrentDate());
            return null;
        }
    }

    private void prepareMenuData() {

        MenuModel menuModel = new MenuModel(Constant.TITLE_DASHBOARD, true, false); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new MenuModel(Constant.ASSIGN_ORDERS_FOR_DELIVERY, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        menuModel = new MenuModel(Constant.Approvals, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(Constant.APPROVE_STORE_PARTNER, false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel(Constant.APPROVE_DELIVERY_PARTNER, false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel(Constant.APPROVE_ITEMS, false, false);
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(Constant.ordersAndOffers, true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(Constant.MAINTAIN_OFFERS, false, false);
        childModelsList.add(childModel);

        childModel = new MenuModel(Constant.VIEW_ORDERS_ADD_COUPON, false, false);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(Constant.advertisments, true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(Constant.ADD_ADVERTISEMNETS, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(TITLE_VIEW_ADVERTISEMENTS, false, false);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(Constant.MaintainingInputs, true, true); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(Constant.TITLE_CATEGORY, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(Constant.TITLE_SUBCATEGORY, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(Constant.MAINTAIN_TIPS, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(Constant.MAINTAIN_DELIVERY_FARE, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(Constant.DELIVERY_DETAILS, false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(Constant.BULK_UPLOAD, false, false);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(Constant.DELIVERY_BOY_HISTORY, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(Constant.STORE_HISTORY, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new MenuModel(Constant.TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(Constant.TITLE_USER, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(Constant.TITLE_CONTACT_US, true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("Logout", true, true); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                MenuModel model = headerList.get(groupPosition);
                if (model.menuName.equals(TITLE_DASHBOARD)) {
                    Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(ASSIGN_ORDERS_FOR_DELIVERY)) {
                    Intent intent = new Intent(getApplicationContext(), AssignOrdersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(DELIVERY_BOY_HISTORY)) {
                    Intent intent = new Intent(getApplicationContext(), DeliveryBoyHistory.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(STORE_HISTORY)) {
                    Intent intent = new Intent(getApplicationContext(), StoreHistory.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN)) {
                    Intent intent = new Intent(getApplicationContext(), AdminWeeklyPaymentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(TITLE_USER)) {
                    Intent intent = new Intent(getApplicationContext(), UserRoleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals(TITLE_CONTACT_US)) {
                    Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (model.menuName.equals("Logout")) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ApproveSellersActivity.this);
                    bottomSheetDialog.setContentView(R.layout.logout_confirmation);
                    Button logout = bottomSheetDialog.findViewById(R.id.logout);
                    Button stayinapp = bottomSheetDialog.findViewById(R.id.stayinapp);

                    bottomSheetDialog.show();
                    bottomSheetDialog.setCancelable(false);

                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    stayinapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                            navigationView.setCheckedItem(R.id.dashboard);
                        }
                    });

                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.menuName.equals(APPROVE_STORE_PARTNER)) {
                        Intent intent = new Intent(getApplicationContext(), ApproveSellersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(APPROVE_DELIVERY_PARTNER)) {
                        Intent intent = new Intent(getApplicationContext(), DeliveryBoyApprovalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(APPROVE_ITEMS)) {
                        Intent intent = new Intent(getApplicationContext(), ItemApprovalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(MAINTAIN_OFFERS)) {
                        Intent intent = new Intent(getApplicationContext(), ViewDiscountActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(VIEW_ORDERS_ADD_COUPON)) {
                        Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(ADD_ADVERTISEMNETS)) {
                        Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else if (model.menuName.equals(TITLE_VIEW_ADVERTISEMENTS)) {
                        Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else if (model.menuName.equals(TITLE_CATEGORY)) {
                        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(TITLE_SUBCATEGORY)) {
                        Intent intent = new Intent(getApplicationContext(), SubCategoryAddActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(MAINTAIN_TIPS)) {
                        Intent intent = new Intent(getApplicationContext(), TipsAddActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(MAINTAIN_DELIVERY_FARE)) {
                        Intent intent = new Intent(getApplicationContext(), MaintainDeliveryFairActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(DELIVERY_DETAILS)) {
                        Intent intent = new Intent(getApplicationContext(), AdminContactUploadingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (model.menuName.equals(BULK_UPLOAD)) {
                        Intent intent = new Intent(getApplicationContext(), BulkUploadActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent ( ApproveSellersActivity.this, DashBoardActivity.class );
        intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( intent );
    }
}