package com.smiligenceUAT1.metrozadmin;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.smiligenceUAT1.metrozadmin.adapter.DeliveryBoyAdapter;
import com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADD_ADVERTISEMNETS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_DELIVERY_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_ITEMS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_STORE_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGN_ORDERS_FOR_DELIVERY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BULK_UPLOAD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_BOY_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_DETAILS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_DELIVERY_FARE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_OFFERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_TIPS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.STORE_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_APPROVE_DELIVERYBOY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_CATEGORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_CONTACT_US;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_DASHBOARD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_SUBCATEGORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_USER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_VIEW_ADVERTISEMENTS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.USER_DETAILS_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.VIEW_ORDERS_ADD_COUPON;


public class DeliveryBoyApprovalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;
    DatabaseReference deliveryBoyLoginDetails,userDetailsDataRef;
    ArrayList<DeliveryBoy> deliveryBoys = new ArrayList<>();
    ListView listView;
    DeliveryBoy deliveryBoy;
    AlertDialog dialog;
    int swipeCount = 0;
    NavigationView navigationView;
    String userName;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_approval);

        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle(TITLE_APPROVE_DELIVERYBOY);
        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.White));
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(DeliveryBoyApprovalActivity.this);

        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);
        navigationView.setCheckedItem(R.id.ApproveDeliveryBoys);

        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);

        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);
        if(!"".equals(UserRoleActivity.saved_userName)&& !"".equals(UserRoleActivity.saved_userName)) {

            userName=UserRoleActivity.saved_userName;
        }  if (!"".equals(DashBoardActivity.saved_userName)&& !"".equals(DashBoardActivity.saved_userName)){
            userName=DashBoardActivity.saved_userName;
        }

        final Query roleNameQuery = userDetailsDataRef.orderByChild(PHONE_NUM_COLUMN).equalTo(String.valueOf(userName));

        roleNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserDetails loginUserDetailslist = snap.getValue(UserDetails.class);
                    textViewUsername.setText(loginUserDetailslist.getFirstName().toUpperCase());
                    textViewEmail.setText(loginUserDetailslist.getRoleName().toUpperCase());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = findViewById(R.id.deliveryboylist);


        deliveryBoyLoginDetails = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyLoginDetails");

        Query waitingForApprovalQuery = deliveryBoyLoginDetails.orderByChild("deliveryboyApprovalStatus").equalTo("Waiting for approval");


        waitingForApprovalQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    deliveryBoys.clear();
                    for (DataSnapshot deliveryBoySnap : dataSnapshot.getChildren()) {
                        deliveryBoy = deliveryBoySnap.getValue(DeliveryBoy.class);
                        deliveryBoys.add(deliveryBoy);
                    }
                    DeliveryBoyAdapter deliveryBoyAdapter = new DeliveryBoyAdapter(DeliveryBoyApprovalActivity.this, deliveryBoys);
                    deliveryBoyAdapter.notifyDataSetChanged();
                    listView.setAdapter(deliveryBoyAdapter);

                    if (listView != null) {
                        int totalHeight = 0;

                        for (int i = 0; i < deliveryBoyAdapter.getCount(); i++) {
                            View listItem = deliveryBoyAdapter.getView(i, null, listView);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
                        listView.setLayoutParams(params);
                        listView.requestLayout();
                        listView.setAdapter(deliveryBoyAdapter);
                        deliveryBoyAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            deliveryBoy = deliveryBoys.get(position);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeliveryBoyApprovalActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.delivery_boy_details, null);

            final RelativeLayout applicantHeader = mView.findViewById(R.id.applicantHeader);

            final ImageView deliveryBoyProfile = mView.findViewById(R.id.deliveryBoyProfile);
            final ImageView bikeLicense = mView.findViewById(R.id.drivingLicenseProof);
            final ImageView aadharId = mView.findViewById(R.id.aadharProof);


            final LinearLayout detailsLayout = mView.findViewById(R.id.detailsLayout);
            final TextView imageHeaderName = mView.findViewById(R.id.imageHeader);

            TextView firstName = mView.findViewById(R.id.firstName1);
            TextView lastName = mView.findViewById(R.id.lastname1);
            TextView mobileNumber = mView.findViewById(R.id.mobilenumbertxt);
            TextView emailid = mView.findViewById(R.id.emailid1);
            TextView pincode = mView.findViewById(R.id.pincode1);
            TextView bankname = mView.findViewById(R.id.bankname1);
            TextView branchName = mView.findViewById(R.id.branchName1);
            TextView accountNumber = mView.findViewById(R.id.accountnumber1);
            TextView ifscCode = mView.findViewById(R.id.ifscCode1);
            TextView aadharNumber = mView.findViewById(R.id.aadharNumber1);
            TextView bikeNumber = mView.findViewById(R.id.bikenumber1);
            final TextView commentsIfAny = mView.findViewById(R.id.commentsSectionEdt);

            final TextView bikeLicenseNumber = mView.findViewById(R.id.bikeLicenseNumber1);

            Button approveButton = mView.findViewById(R.id.approve);
            Button declineButton = mView.findViewById(R.id.reject);

            approveButton.setOnClickListener(v -> {

                DatabaseReference deliveryBoyDetailsDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("DeliveryBoyLoginDetails").child(deliveryBoy.getDeliveryBoyId());
                deliveryBoyDetailsDataRef.child("deliveryboyApprovalStatus").setValue("Approved");
                dialog.dismiss();
                Intent intent = getIntent();
                startActivity(intent);
                Toast.makeText(DeliveryBoyApprovalActivity.this, "Approved", Toast.LENGTH_SHORT).show();

            });

            declineButton.setOnClickListener(v -> {
                commentsIfAny.setVisibility(View.VISIBLE);
                if (commentsIfAny.getVisibility() == View.VISIBLE) {
                    if ("".equals(commentsIfAny.getText().toString())) {
                        commentsIfAny.setError("Required");
                    } else {
                        DatabaseReference deliveryBoyDetailsDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("DeliveryBoyLoginDetails").child(deliveryBoy.getDeliveryBoyId());
                        deliveryBoyDetailsDataRef.child("deliveryboyApprovalStatus").setValue("Rejected");
                        deliveryBoyDetailsDataRef.child("commentsIfAny").setValue(commentsIfAny.getText().toString());
                        dialog.dismiss();
                        Intent intent = getIntent();
                        startActivity(intent);
                        Toast.makeText(DeliveryBoyApprovalActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            ImageView cancelButton = mView.findViewById(R.id.canceldetails);

            firstName.setText(deliveryBoy.getFirstName());
            lastName.setText(deliveryBoy.getLastName());
            mobileNumber.setText(deliveryBoy.getMobileNumber());
            emailid.setText(deliveryBoy.getEmailId());
            pincode.setText(deliveryBoy.getZipcode());
            bankname.setText(deliveryBoy.getBankName());
            branchName.setText(deliveryBoy.getBankBranchName());
            accountNumber.setText(deliveryBoy.getAccountNumber());
            ifscCode.setText(deliveryBoy.getBankIfscCode());
            aadharNumber.setText(deliveryBoy.getAadharNumber());
            bikeNumber.setText(deliveryBoy.getBikeNumber());
            bikeLicenseNumber.setText(deliveryBoy.getBikeLicenseNumber());

            mBuilder.setView(mView);
            dialog = mBuilder.create();
            dialog.show();
            dialog.setCancelable(false);


            mView.setOnTouchListener(new OnSwipeTouchListener(DeliveryBoyApprovalActivity.this) {
                public void onSwipeRight() {
                    swipeCount = swipeCount - 1;

                    if (swipeCount == 0) {
                        detailsLayout.setVisibility(View.VISIBLE);
                        deliveryBoyProfile.setVisibility(View.INVISIBLE);
                        aadharId.setVisibility(View.INVISIBLE);
                        bikeLicense.setVisibility(View.INVISIBLE);
                        applicantHeader.setVisibility(View.VISIBLE);
                        imageHeaderName.setText("Delivery Boy details");
                    }
                    if (swipeCount == 1) {

                        detailsLayout.setVisibility(View.INVISIBLE);
                        deliveryBoyProfile.setVisibility(View.VISIBLE);
                        aadharId.setVisibility(View.INVISIBLE);
                        applicantHeader.setVisibility(View.VISIBLE);
                        bikeLicense.setVisibility(View.INVISIBLE);
                        imageHeaderName.setText("Delivery Boy Profile");

                        Picasso.get().load(deliveryBoy.getDeliveryBoyProfile()).into(deliveryBoyProfile);

                    } else if (swipeCount == 2) {

                        detailsLayout.setVisibility(View.INVISIBLE);
                        deliveryBoyProfile.setVisibility(View.INVISIBLE);
                        aadharId.setVisibility(View.VISIBLE);
                        applicantHeader.setVisibility(View.VISIBLE);
                        bikeLicense.setVisibility(View.INVISIBLE);
                        imageHeaderName.setText("Aadhar Id");
                        Picasso.get().load(deliveryBoy.getAadharIdProof()).into(aadharId);
                    }
                }

                public void onSwipeLeft() {

                    swipeCount = swipeCount + 1;
                    if (swipeCount == 1) {
                        detailsLayout.setVisibility(View.INVISIBLE);
                        deliveryBoyProfile.setVisibility(View.VISIBLE);
                        aadharId.setVisibility(View.INVISIBLE);
                        imageHeaderName.setText("Delivery Boy Profile");
                        bikeLicense.setVisibility(View.INVISIBLE);
                        Picasso.get().load(deliveryBoy.getDeliveryBoyProfile()).into(deliveryBoyProfile);
                    } else if (swipeCount == 2) {
                        detailsLayout.setVisibility(View.INVISIBLE);
                        deliveryBoyProfile.setVisibility(View.INVISIBLE);
                        aadharId.setVisibility(View.VISIBLE);
                        bikeLicense.setVisibility(View.INVISIBLE);
                        imageHeaderName.setText("Aadhar Id");
                        Picasso.get().load(deliveryBoy.getAadharIdProof()).into(aadharId);

                    } else if (swipeCount == 3) {
                        detailsLayout.setVisibility(View.INVISIBLE);
                        deliveryBoyProfile.setVisibility(View.INVISIBLE);
                        aadharId.setVisibility(View.INVISIBLE);
                        bikeLicense.setVisibility(View.VISIBLE);
                        imageHeaderName.setText("Driving License");
                        Picasso.get().load(deliveryBoy.getDrivingLicenseProof()).into(bikeLicense);
                    }
                }

            });
            cancelButton.setOnClickListener(v -> {
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( DeliveryBoyApprovalActivity.this );
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
                navigationView.setCheckedItem(R.id.ApproveDeliveryBoys);
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
        }else  if(id==R.id.bulkupload){
            Intent intent = new Intent(getApplicationContext(), BulkUploadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.adminWeeklyPayments) {
            Intent intent = new Intent(getApplicationContext(), AdminWeeklyPaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(DeliveryBoyApprovalActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DeliveryBoyApprovalActivity.this);
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
}