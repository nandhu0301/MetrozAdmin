package com.smiligenceUAT1.metrozadmin;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryPartnerCharge;
import com.smiligenceUAT1.metrozadmin.bean.MaintainFairDetails;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.SellerPaymentDetailsConstant;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;

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
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_CATEGORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_CONTACT_US;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_DASHBOARD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_SUBCATEGORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_USER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TITLE_VIEW_ADVERTISEMENTS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.USER_DETAILS_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.VIEW_ORDERS_ADD_COUPON;

public class MaintainDeliveryFairActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;

    //Pickup and drop category details

    Button addpickupAndDropCategory;
    TextView pickUpBaseFair, pickUpMinimumFair, pickUpPerKm;

    //Other Categories
    TextView otherCategoryPerKm, OtherCategoryMinFair;
    Button addOtherCateory;

    DatabaseReference DeliveryBoyCharges,DeliveryBoyChargesQuery;
    DatabaseReference maintainFairDetailsDataref,userDetailsDataRef,sellerPaymentsDataRef;
    public static String pickup_and_drop_String = "PickupAndDrop";
    public static String other_category_String = "OtherCategory";


    EditText baseFair, minimumFair, perKmAmount;
    EditText minimumFairotherCategory, perKmAmountotherCategory;
    Button add, cancel;
    Button addotherCategory, cancelotherCategory;
    NavigationView navigationView;

    MaintainFairDetails maintainFairDetailsForPickupAndDrop = new MaintainFairDetails();
    MaintainFairDetails maintainFairDetailsForOtherCategory = new MaintainFairDetails();
    // MaintainFairDetails maintainFairDetailsForDeliveryBoy = new MaintainFairDetails();
    DeliveryPartnerCharge deliveryPartnerCharge=new DeliveryPartnerCharge();
    String creationDate = DateUtils.fetchCurrentDate ();

    Query otherCategoriesQuery;
    Query pickupOnLoadQuery;
    Query deliveryBoyCharges;

    String userName;
    TextView startingPayment,weeklyPayment;
    Spinner paymentSpinner;
    Button addSellerPayments;
    SellerPaymentDetailsConstant sellerPaymentDetailsConstant=new SellerPaymentDetailsConstant();
    CardView basicCard,silverCard,goldenCard;
    Button addDeliveryboypartnerCharge;

    EditText perKmAmountForDeliveryBoySC,perKmAmountForDeliveryBoyDS;
    TextView perKmAmountForDeliveryBoySCTxt,perKmAmountForDeliveryBoyDSTxt;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maintain_delivery_fair );

        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        Toolbar toolbar1 = findViewById ( R.id.toolbar );
        toolbar1.setTitle (Constant.MAINTAIN_DELIVERY_FARE);
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
        navigationView.setNavigationItemSelectedListener ( MaintainDeliveryFairActivity.this );


        UserRoleActivity.menuNav = navigationView.getMenu ();
        mHeaderView = navigationView.getHeaderView ( 0 );

        navigationView.setCheckedItem(R.id.Add_Delivery_Fair);

        textViewUsername = mHeaderView.findViewById ( R.id.name );
        textViewEmail = mHeaderView.findViewById ( R.id.roleName );


        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);
        if(!"".equals(UserRoleActivity.saved_userName)&& !"".equals(UserRoleActivity.saved_userName)) {

            userName= UserRoleActivity.saved_userName;
        }  if (!"".equals(DashBoardActivity.saved_userName)&& !"".equals(DashBoardActivity.saved_userName)){
            userName= DashBoardActivity.saved_userName;
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



        addpickupAndDropCategory = findViewById ( R.id.addPickup );
        addOtherCateory = findViewById ( R.id.addothercateogory );

        pickUpBaseFair = findViewById ( R.id.base_fair1 );
        pickUpMinimumFair = findViewById ( R.id.minimum_fair1 );
        pickUpPerKm = findViewById ( R.id.Per_Km1 );

        otherCategoryPerKm = findViewById ( R.id.Per_km_othercategory1 );
        OtherCategoryMinFair = findViewById ( R.id.minimum_fair_othercategory1 );
        startingPayment=findViewById(R.id.startingPayment);
        weeklyPayment=findViewById(R.id.weeklypayment);
        paymentSpinner=findViewById(R.id.typeOfSettlment);
        addSellerPayments=findViewById(R.id.addSellerPayments);
        addDeliveryboypartnerCharge=findViewById(R.id.addDeliveryboypartnerCharge);
        perKmAmountForDeliveryBoySCTxt = findViewById(R.id.perKmAmountForDeliveryBoysc);
        perKmAmountForDeliveryBoyDSTxt = findViewById(R.id.perKmAmountForDeliveryBoyds);
        basicCard=findViewById(R.id.basicCard);
        silverCard=findViewById(R.id.silverCard);
        goldenCard=findViewById(R.id.goldenCard);

        String payments[] = {"Select Payment Type","Basic","Silver","Gold"};


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        payments); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(spinnerArrayAdapter);

        maintainFairDetailsDataref = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( "MaintainFairDetails" );
        DeliveryBoyCharges=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyChargeDetails");
        DeliveryBoyChargesQuery=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyChargeDetails").child("DeliveryBoyCharges");
        otherCategoriesQuery = maintainFairDetailsDataref.child ( String.valueOf ( other_category_String ) );
        pickupOnLoadQuery = maintainFairDetailsDataref.child ( String.valueOf ( pickup_and_drop_String ) );
        deliveryBoyCharges=maintainFairDetailsDataref.child(String.valueOf("DeliveryBoyCharges"));
        sellerPaymentsDataRef=FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SellerPayments");
        onLoadFunction ();


        addpickupAndDropCategory.setOnClickListener (v -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( MaintainDeliveryFairActivity.this );
            LayoutInflater inflater = getLayoutInflater ();
            final View alertdialogView = inflater.inflate ( R.layout.pickup_drop_layout, null );
            alertDialogBuilder.setView ( alertdialogView );


            final Dialog dialog = alertDialogBuilder.create ();
            dialog.show ();
            dialog.setCancelable ( false );

            baseFair = alertdialogView.findViewById ( R.id.basefairedt );
            minimumFair = alertdialogView.findViewById ( R.id.minfairedt );
            perKmAmount = alertdialogView.findViewById ( R.id.perkmedt );

            add = alertdialogView.findViewById ( R.id.okview );

            pickupOnLoadQuery.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount () > 0) {
                        add.setText ( "Update" );

                        pickupOnLoadQuery.addValueEventListener ( new ValueEventListener () {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount () > 0) {
                                    MaintainFairDetails pickUpDropFairDetails = dataSnapshot.getValue ( MaintainFairDetails.class );
                                    if (pickUpDropFairDetails != null) {
                                        if (!"".equals ( pickUpDropFairDetails.getBaseFairforPickUpAndDrop () )) {
                                            baseFair.setText ( String.valueOf ( pickUpDropFairDetails.getBaseFairforPickUpAndDrop () ) );
                                        }
                                        if (!"".equals ( pickUpDropFairDetails.getMinimumFairPickUpAndDrop () )) {
                                            minimumFair.setText ( String.valueOf ( pickUpDropFairDetails.getMinimumFairPickUpAndDrop () ) );
                                        }
                                        if (!"".equals ( pickUpDropFairDetails.getPerKmPickUpAndDrop () )) {
                                            perKmAmount.setText ( String.valueOf ( pickUpDropFairDetails.getPerKmPickUpAndDrop () ) );
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        add.setText ( "Add" );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            add.setOnClickListener (v14 -> {
                if(baseFair.getText().toString().equals(""))
                {
                    baseFair.setError("Required");
                    return;
                }else if(minimumFair.getText().toString().equals(""))
                {
                    minimumFair.setError("Required");
                    return;
                }else if(perKmAmount.getText().toString().equals(""))
                {
                    perKmAmount.setError("Required");
                    return;
                }else {
                    maintainFairDetailsForPickupAndDrop.setCreationDate(creationDate);
                    maintainFairDetailsForPickupAndDrop.setPerKmPickUpAndDrop(0);
                    maintainFairDetailsForPickupAndDrop.setBaseFairforPickUpAndDrop(Integer.parseInt(baseFair.getText().toString()));
                    maintainFairDetailsForPickupAndDrop.setMinimumFairPickUpAndDrop(Integer.parseInt(minimumFair.getText().toString()));
                    maintainFairDetailsForPickupAndDrop.setPerKmPickUpAndDrop(Integer.parseInt(perKmAmount.getText().toString()));
                    maintainFairDetailsDataref.child(String.valueOf(pickup_and_drop_String)).setValue(maintainFairDetailsForPickupAndDrop);
                    Toast.makeText(MaintainDeliveryFairActivity.this, "Details Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    onLoadFunction();
                }
            });

            cancel = alertdialogView.findViewById ( R.id.cancelview );

            cancel.setOnClickListener (v13 -> dialog.dismiss());

        });

        addDeliveryboypartnerCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( MaintainDeliveryFairActivity.this );
                LayoutInflater inflater = getLayoutInflater ();
                final View alertdialogView = inflater.inflate ( R.layout.deliveryboy_perkm_layout, null );
                alertDialogBuilder.setView ( alertdialogView );

                perKmAmountForDeliveryBoyDS=alertdialogView.findViewById ( R.id.perKmAmountForDeliveryBoyds );
                perKmAmountForDeliveryBoySC=alertdialogView.findViewById ( R.id.perKmAmountForDeliveryBoysc );

                add = alertdialogView.findViewById ( R.id.okview );
                cancel = alertdialogView.findViewById ( R.id.cancelview );
                final Dialog dialog = alertDialogBuilder.create ();
                dialog.show ();
                dialog.setCancelable ( false );

                DeliveryBoyChargesQuery.addValueEventListener ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount () > 0) {
                            addDeliveryboypartnerCharge.setText ( "Update" );

                            DeliveryPartnerCharge otherCategoryDetails = dataSnapshot.getValue ( DeliveryPartnerCharge.class );
                            if (otherCategoryDetails != null) {
                                if (!"".equals ( otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore () )) {
                                    perKmAmountForDeliveryBoyDSTxt.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore () ) );
                                    perKmAmountForDeliveryBoyDS.setText( String.valueOf(otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore () ));
                                }

                                if (!"".equals ( otherCategoryDetails.getDeliveryChargeFromStoreToCustomer () )) {
                                    perKmAmountForDeliveryBoySCTxt.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getDeliveryChargeFromStoreToCustomer () ) );
                                    perKmAmountForDeliveryBoySC.setText (  String.valueOf(otherCategoryDetails.getDeliveryChargeFromStoreToCustomer () ));
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (perKmAmountForDeliveryBoyDS.getText().toString().equals("")) {
                            perKmAmountForDeliveryBoyDS.setError("Required");
                            return;
                        }else if (perKmAmountForDeliveryBoySC.getText().toString().equals("")) {
                            perKmAmountForDeliveryBoySC.setError("Required");
                            return;
                        }  else {
                            deliveryPartnerCharge.setCreationDate(creationDate);
                            deliveryPartnerCharge.setDeliveryChargeFromDeliveryBoyToStore(Integer.parseInt(perKmAmountForDeliveryBoyDS.getText().toString()));
                            deliveryPartnerCharge.setDeliveryChargeFromStoreToCustomer(Integer.parseInt(perKmAmountForDeliveryBoySC.getText().toString()));
                            DeliveryBoyCharges.child(String.valueOf("DeliveryBoyCharges")).setValue(deliveryPartnerCharge);
                            Toast.makeText(MaintainDeliveryFairActivity.this, "Details Inserted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            onLoadFunction();
                        }
                    }
                });

            }
        });
        basicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MaintainDeliveryFairActivity.this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = MaintainDeliveryFairActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_layout, null);
                dialogBuilder.setView(dialogView);

                RelativeLayout border=dialogView.findViewById(R.id.backGroundLayout);
                border.setBackgroundColor(getResources().getColor(R.color.face));
                TextView header=dialogView.findViewById(R.id.typeText);
                header.setText("Basic");
                EditText payPrice=dialogView.findViewById(R.id.startingPayment);
                EditText percentPrice=dialogView.findViewById(R.id.PercentagePayment);

                sellerPaymentsDataRef.child("Basic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0)
                        {
                            SellerPaymentDetailsConstant sellerPaymentDetailsConstant=dataSnapshot.getValue(SellerPaymentDetailsConstant.class);
                            payPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPayment()));
                            percentPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPercentage()));
                        }else
                        {
                            payPrice.setText(String.valueOf(0));
                            percentPrice.setText(String.valueOf(0));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        silverCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MaintainDeliveryFairActivity.this);
                LayoutInflater inflater = MaintainDeliveryFairActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_layout, null);
                dialogBuilder.setView(dialogView);

                RelativeLayout border=dialogView.findViewById(R.id.backGroundLayout);
                border.setBackgroundColor(getResources().getColor(R.color.silver));
                TextView header=dialogView.findViewById(R.id.typeText);
                header.setText("Silver");
                EditText payPrice=dialogView.findViewById(R.id.startingPayment);
                EditText percentPrice=dialogView.findViewById(R.id.PercentagePayment);

                sellerPaymentsDataRef.child("Silver").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0)
                        {
                            SellerPaymentDetailsConstant sellerPaymentDetailsConstant=dataSnapshot.getValue(SellerPaymentDetailsConstant.class);
                            payPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPayment()));
                            percentPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPercentage()));
                        }else
                        {
                            payPrice.setText(String.valueOf(0));
                            percentPrice.setText(String.valueOf(0));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        goldenCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MaintainDeliveryFairActivity.this);

                LayoutInflater inflater = MaintainDeliveryFairActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_layout, null);
                dialogBuilder.setView(dialogView);

                RelativeLayout border=dialogView.findViewById(R.id.backGroundLayout);
                border.setBackgroundColor(getResources().getColor(R.color.gold));
                TextView header=dialogView.findViewById(R.id.typeText);
                header.setText("Gold");
                EditText payPrice=dialogView.findViewById(R.id.startingPayment);
                EditText percentPrice=dialogView.findViewById(R.id.PercentagePayment);
                sellerPaymentsDataRef.child("Gold").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0)
                        {
                            SellerPaymentDetailsConstant sellerPaymentDetailsConstant=dataSnapshot.getValue(SellerPaymentDetailsConstant.class);
                            payPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPayment()));
                            percentPrice.setText(String.valueOf(sellerPaymentDetailsConstant.getPercentage()));
                        }else
                        {
                            payPrice.setText(String.valueOf(0));
                            percentPrice.setText(String.valueOf(0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        addSellerPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int price;

                if (!"".equals(weeklyPayment.getText().toString()) && weeklyPayment.getText().toString() != null) {
                    price =Integer.parseInt(weeklyPayment.getText().toString());
                } else {
                    price = 0;
                }

                if (paymentSpinner.getSelectedItemPosition()==0)
                {
                    Toast.makeText(MaintainDeliveryFairActivity.this, "Please select payment type", Toast.LENGTH_SHORT).show();
                }else if (startingPayment.getText().toString().equals(""))
                {
                    startingPayment.setError("Required");
                }else if (weeklyPayment.getText().toString().equals(""))
                {
                    weeklyPayment.setError("Required");
                }else if (price>100 || price==0)
                {
                    weeklyPayment.setError("Please enter valid percentage");
                }
                else
                {

                    sellerPaymentDetailsConstant.setShareType(String.valueOf(paymentSpinner.getSelectedItem()));
                    sellerPaymentDetailsConstant.setPayment(Integer.parseInt(startingPayment.getText().toString()));
                    sellerPaymentDetailsConstant.setPercentage(Integer.parseInt(weeklyPayment.getText().toString()));
                    sellerPaymentsDataRef.child(String.valueOf(paymentSpinner.getSelectedItem())).setValue(sellerPaymentDetailsConstant);

                    paymentSpinner.setSelection(0);
                    startingPayment.setText("");
                    weeklyPayment.setText("");
                }
            }
        });

        addOtherCateory.setOnClickListener (v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( MaintainDeliveryFairActivity.this );
            LayoutInflater inflater = getLayoutInflater ();
            final View alertdialogView = inflater.inflate ( R.layout.other_category, null );
            alertDialogBuilder.setView ( alertdialogView );

            final Dialog dialog = alertDialogBuilder.create ();
            dialog.show ();
            dialog.setCancelable ( false );

            minimumFairotherCategory = alertdialogView.findViewById ( R.id.basefairedt1 );
            perKmAmountotherCategory = alertdialogView.findViewById ( R.id.mainfairedt1 );
            addotherCategory = alertdialogView.findViewById ( R.id.okview );

            onLoadFunction ();

            otherCategoriesQuery.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount () > 0) {
                        addotherCategory.setText ( "Update" );
                        MaintainFairDetails otherCategoryDetails = dataSnapshot.getValue ( MaintainFairDetails.class );
                        if (otherCategoryDetails != null) {
                            if (!"".equals ( otherCategoryDetails.getMinimumFairOtherCategory () )) {
                                minimumFairotherCategory.setText ( String.valueOf ( otherCategoryDetails.getMinimumFairOtherCategory () ) );
                            }
                            if (!"".equals ( otherCategoryDetails.getPerKmPickUpAndDrop () )) {
                                perKmAmountotherCategory.setText ( String.valueOf ( otherCategoryDetails.getPerKmOtherCategory () ) );
                            }
                        }
                    } else {
                        addotherCategory.setText ( "Add" );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );


            addotherCategory.setOnClickListener (v12 -> {
                if (minimumFairotherCategory.getText().toString().equals(""))
                {
                    minimumFairotherCategory.setError("Required");
                }else if (perKmAmountotherCategory.getText().toString().equals(""))
                {
                    perKmAmountotherCategory.setError("Required");

                }else {
                    maintainFairDetailsForOtherCategory.setCreationDate(creationDate);
                    maintainFairDetailsForOtherCategory.setPerKmForDeliveryBoy(0);
                    maintainFairDetailsForOtherCategory.setMinimumFairOtherCategory(Integer.parseInt(minimumFairotherCategory.getText().toString()));
                    maintainFairDetailsForOtherCategory.setPerKmOtherCategory(Integer.parseInt(perKmAmountotherCategory.getText().toString()));
                    maintainFairDetailsDataref.child(String.valueOf(other_category_String)).setValue(maintainFairDetailsForOtherCategory);
                    Toast.makeText(MaintainDeliveryFairActivity.this, "Details Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    onLoadFunction();
                }

            });

            cancelotherCategory = alertdialogView.findViewById ( R.id.cancelview );

            cancelotherCategory.setOnClickListener (v1 -> dialog.dismiss());
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
        } else if (id == R.id.viewadvertisement) {
            Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.Logout) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( MaintainDeliveryFairActivity.this );
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
                navigationView.setCheckedItem(R.id.Add_Delivery_Fair);
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
        }else  if(id== R.id.bulkupload){
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

    public void onLoadFunction() {
        pickupOnLoadQuery.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount () > 0) {
                    addpickupAndDropCategory.setText ( "Update" );
                    MaintainFairDetails pickUpDropFairDetails = dataSnapshot.getValue ( MaintainFairDetails.class );
                    if (pickUpDropFairDetails != null) {
                        if (!"".equals ( pickUpDropFairDetails.getBaseFairforPickUpAndDrop () )) {
                            pickUpBaseFair.setText ( "₹ " + String.valueOf ( pickUpDropFairDetails.getBaseFairforPickUpAndDrop () ) );
                        }
                        if (!"".equals ( pickUpDropFairDetails.getMinimumFairPickUpAndDrop () )) {
                            pickUpMinimumFair.setText ( "₹ " + String.valueOf ( pickUpDropFairDetails.getMinimumFairPickUpAndDrop () ) );
                        }
                        if (!"".equals ( pickUpDropFairDetails.getPerKmPickUpAndDrop () )) {
                            pickUpPerKm.setText ( "₹ " + String.valueOf ( pickUpDropFairDetails.getPerKmPickUpAndDrop () ) );
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        otherCategoriesQuery.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount () > 0) {
                    addOtherCateory.setText ( "Update" );
                    MaintainFairDetails otherCategoryDetails = dataSnapshot.getValue ( MaintainFairDetails.class );
                    if (otherCategoryDetails != null) {
                        if (!"".equals ( otherCategoryDetails.getMinimumFairOtherCategory () )) {
                            OtherCategoryMinFair.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getMinimumFairOtherCategory () ) );
                        }
                        if (!"".equals ( otherCategoryDetails.getPerKmPickUpAndDrop () )) {
                            otherCategoryPerKm.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getPerKmOtherCategory () ) );
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        DeliveryBoyChargesQuery.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount () > 0) {
                    addDeliveryboypartnerCharge.setText ( "Update" );

                    DeliveryPartnerCharge otherCategoryDetails = dataSnapshot.getValue ( DeliveryPartnerCharge.class );
                    if (otherCategoryDetails != null) {
                        if (!"".equals ( otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore () )) {
                            perKmAmountForDeliveryBoyDSTxt.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getDeliveryChargeFromDeliveryBoyToStore () ) );
                        }

                        if (!"".equals ( otherCategoryDetails.getDeliveryChargeFromStoreToCustomer () )) {
                            perKmAmountForDeliveryBoySCTxt.setText ( "₹ " + String.valueOf ( otherCategoryDetails.getDeliveryChargeFromStoreToCustomer () ) );
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent ( getApplicationContext (), DashBoardActivity.class );
        intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( intent );
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MaintainDeliveryFairActivity.this);
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