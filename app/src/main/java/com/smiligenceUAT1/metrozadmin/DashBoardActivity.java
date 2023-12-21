package com.smiligenceUAT1.metrozadmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADD_ADVERTISEMNETS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_DELIVERY_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_ITEMS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_STORE_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGN_ORDERS_FOR_DELIVERY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BILLED_DATE_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BULK_UPLOAD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_BOY_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_DETAILS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_DELIVERY_FARE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_OFFERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_TIPS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ORDER_DETAILS_FIREBASE_TABLE;
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
import static com.smiligenceUAT1.metrozadmin.common.TextUtils.removeDuplicatesList;


public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView  sales,items, quantity, customer, bill_report, storeNameText;
    Button viewSalesReport, viewItemsReport;
    DatabaseReference billdataref, SellerloginDetails;
    BarChart barChart, salesBarChart;
    final ArrayList<OrderDetails> billDetailsArrayList = new ArrayList<>();
    final ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
    final ArrayList<ItemDetails> productDetailsArrayList = new ArrayList<>();
    List<String> customerList = new ArrayList<>();
    List<String> billTimeArrayList = new ArrayList<>();
    List<String> itemList = new ArrayList<>();
    List<String> storeList = new ArrayList<>();
    List<String> shippingPincodeList = new ArrayList<>();
    final ArrayList<String> itemName = new ArrayList<>();
    ArrayList<String> billList = new ArrayList<>();
    int uniqueItemCount = 0;
    int todaysTotalSalesAmt = 0;
    int todaysTotalQty = 0;
    int uniqueCustomerCount = 0;
    int totalItemCount = 0;
    final boolean[] onDataChangeCheck = {false};
    public static String saved_username
            ;
    HashMap<String, Integer> billAmountHashMap = new HashMap<>();
    Query query;
    NavigationView navigationView;
    View mHeaderView;
    TextView textViewUsername, textViewEmail;
    public static String saved_productKey, saved_businessName, saved_userName;
    DatabaseReference userDetailsDataRef;
    TextView categoryText, locationText, deliveryBoyText;
    ArrayList<String> categoryList = new ArrayList<>();
    DatabaseReference deliveryBoyRef, itemDataRef, deliveryBoyAttendanceDataRef;
    List<UserDetails> sellerList = new ArrayList<>();
    List<UserDetails> userDetailsList = new ArrayList<>();
    ArrayList<String> IdList = new ArrayList<>();
    ArrayList<String> IdListNew = new ArrayList<>();
    ArrayList<String> image_list = new ArrayList<>();
    ArrayList<String> image_list_New = new ArrayList<>();
    ArrayList<DeliveryBoy> refinedList = new ArrayList<>();
    boolean check = true;
    UserDetails sellerDetails = new UserDetails();
    boolean isChecked = true;
    TextView sellerListtxt, deliveryListtxt, approveItemListtxt;
    ArrayList<DeliveryBoy> deliveryBoys = new ArrayList<>();
    DeliveryBoy deliveryBoy;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    Button generateExcel;
    DatabaseReference orderDetailsDataref;
    ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
    ArrayList<ItemDetails> itemDetailsArrayListExcel = new ArrayList<>();
    HSSFWorkbook hssfWorkbook;
    HSSFSheet hssfSheet;
    HSSFRow headerRow;
    HSSFRow headerRow1;
    private EditText editTextExcel;
    private File filePath;
    int defaultStartingCellCount = 1;
    String TIME_SERVER;
    NTPUDPClient timeClient;
    private SmoothProgressBar mProgressBar;
    String date=DateUtils.fetchFormatedCurrentDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        builder1.detectFileUriExposure();

        filePath=new File(Environment.getExternalStorageDirectory() + "/Demo"+date+".xls");
        orderDetailsDataref = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderDetails");
        mProgressBar = (SmoothProgressBar) findViewById(R.id.gradient);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        viewSalesReport = findViewById(R.id.viewreport);
        sales = findViewById(R.id.sales1);
        items = findViewById(R.id.items);
        quantity = findViewById(R.id.quantity);
        customer = findViewById(R.id.customer);
        bill_report = findViewById(R.id.bill);
        storeNameText = findViewById(R.id.storeName);
        barChart = findViewById(R.id.barChart);
        viewItemsReport = findViewById(R.id.itemReports);
        salesBarChart = findViewById(R.id.salesBarChart);
        categoryText = findViewById(R.id.category);
        locationText = findViewById(R.id.location);
        deliveryBoyText = findViewById(R.id.deliveryboyvalue_text);
        generateExcel=findViewById(R.id.generateExcel);
        // Utils.getDatabase();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitle(TITLE_DASHBOARD);

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.White));
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(DashBoardActivity.this);
        navigationView.setCheckedItem(R.id.dashboard);

        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);

        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);
        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);

        SharedPreferences loginSharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
        saved_userName = loginSharedPreferences.getString("userName", "");
        saved_productKey = loginSharedPreferences.getString("productkeyStr", "");
        saved_businessName = loginSharedPreferences.getString("businessNameStr", "");
        SellerloginDetails = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SellerLoginDetails");
        deliveryBoyRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyLoginDetails");
        deliveryBoyAttendanceDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyAttendanceTable");
        itemDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("ProductDetails");
        final Query roleNameQuery = userDetailsDataRef.orderByChild(PHONE_NUM_COLUMN).equalTo(String.valueOf(saved_userName));

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


        onStart();

        billdataref = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(ORDER_DETAILS_FIREBASE_TABLE);

        Query waitingForApprovalQuery = SellerloginDetails.orderByChild("approvalStatus").equalTo("Waiting for approval");
        waitingForApprovalQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildrenCount() > 0) {

                    if (isChecked ) {
                        userDetailsList.clear();
                        for (DataSnapshot deliveryBoySnap : dataSnapshot.getChildren()) {
                            sellerDetails = deliveryBoySnap.getValue(UserDetails.class);
                            userDetailsList.add(sellerDetails);
                        }
                        sellerListtxt = findViewById(R.id.storeName);
                        if (userDetailsList.size() > 0) {
                            sellerListtxt.setText(String.valueOf(userDetailsList.size()));
                            sellerListtxt.setVisibility(View.VISIBLE);
                        } else {
                            sellerListtxt.setVisibility(View.INVISIBLE);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Query waitingForApprovalQuerydel = deliveryBoyRef.orderByChild("deliveryboyApprovalStatus").equalTo("Waiting for approval");
        waitingForApprovalQuerydel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    deliveryBoys.clear();
                    for (DataSnapshot deliveryBoySnap : dataSnapshot.getChildren()) {
                        deliveryBoy = deliveryBoySnap.getValue(DeliveryBoy.class);
                        deliveryBoys.add(deliveryBoy);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        itemDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (check) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        productDetailsArrayList.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            ItemDetails itemDetails = dataSnapshot1.getValue(ItemDetails.class);
                            if ("Waiting for approval".equalsIgnoreCase(itemDetails.getItemApprovalStatus())) {
                                productDetailsArrayList.add(itemDetails);
                            }
                        }
                        approveItemListtxt = findViewById(R.id.items);
                        if (productDetailsArrayList.size() > 0) {
                            approveItemListtxt.setText(String.valueOf(productDetailsArrayList.size()));
                            approveItemListtxt.setVisibility(View.VISIBLE);
                        } else {
                            approveItemListtxt.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        deliveryBoyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refinedList.clear();
                IdList.clear();
                image_list.clear();
                IdListNew.clear();
                image_list_New.clear();

                if (check = true) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot valuationDatasnapshot : dataSnapshot.getChildren()) {
                            DeliveryBoy loginUserDetails = valuationDatasnapshot.getValue(DeliveryBoy.class);

                            IdList.add(loginUserDetails.getDeliveryBoyId());
                            image_list.add(loginUserDetails.getDeliveryBoyProfile());
                        }


                        IdListNew.clear();
                        image_list_New.clear();
                        refinedList.clear();
                        for (int i = 0; i < IdList.size(); i++) {
                            Query IdBasedQuery = deliveryBoyAttendanceDataRef.child(IdList.get(i)).child(DateUtils.fetchCurrentDate());
                            final int finalI = i;
                            IdBasedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    IdListNew.clear();
                                    if (check = true) {
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            IdListNew.add(IdList.get(finalI));
                                            image_list_New.add(image_list.get(finalI));
                                        }

                                        removeDuplicatesList(IdListNew);
                                        for (int i = 0; i < IdListNew.size(); i++) {

                                            Query childQuery = deliveryBoyRef.child(IdListNew.get(i));
                                            childQuery.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (check = true) {
                                                        DeliveryBoy deliveryBoy = dataSnapshot.getValue(DeliveryBoy.class);
                                                        refinedList.add(deliveryBoy);

                                                    }

                                                    deliveryBoyText.setText(String.valueOf(refinedList.size()));

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
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Query billDetailsQuery = billdataref.orderByChild(BILLED_DATE_COLUMN).equalTo(DateUtils.fetchCurrentDate());


        billDetailsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearData();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot billSnapShot : dataSnapshot.getChildren()) {


                        billDetailsArrayList.add(billSnapShot.getValue(OrderDetails.class));
                        itemDetailsArrayList.add(billSnapShot.getValue(ItemDetails.class));

                        onDataChangeCheck[0] = true;
                    }


                    //  if (onDataChangeCheck[0] == true) {

                    Iterator billIterator = billDetailsArrayList.iterator();

                    while (billIterator.hasNext()) {

                        OrderDetails orderDetails = (OrderDetails) billIterator.next();
                        if (orderDetails.getOrderStatus().equals("Delivered")) {
                            customerList.add(orderDetails.getCustomerName());
                            storeList.add(orderDetails.getStoreName());
                            shippingPincodeList.add(orderDetails.getShippingPincode());
                            todaysTotalSalesAmt += (orderDetails.getTotalAmount());
                            sales.setText(String.valueOf(todaysTotalSalesAmt));
                            billList.add(orderDetails.getOrderStatus());
                            billTimeArrayList.add(DateUtils.fetchTime(orderDetails.getOrderCreateDate()));
                            billAmountHashMap.put(DateUtils.fetchTimewithSeconds(orderDetails.getOrderCreateDate()), orderDetails.getTotalAmount());

                            Iterator itemIterator = orderDetails.getItemDetailList().iterator();

                            while (itemIterator.hasNext()) {

                                ItemDetails itemDetails = (ItemDetails) itemIterator.next();
                                itemName.add(itemDetails.getItemName());
                                categoryList.add(itemDetails.getCategoryName());
                                todaysTotalQty = todaysTotalQty + itemDetails.getItemBuyQuantity();
                                itemList.add(itemDetails.getItemName());


                            }
                            quantity.setText(String.valueOf(todaysTotalQty));
                            removeDuplicatesList(categoryList);
                            removeDuplicatesList(shippingPincodeList);


                            ArrayList<String> newItemList = TextUtils.removeDuplicates((ArrayList<String>) itemList);
                            ArrayList<String> newCustomerList = TextUtils.removeDuplicates((ArrayList<String>) customerList);
                            ArrayList<String> newItemNameList = TextUtils.removeDuplicates((ArrayList<String>) itemName);
                            ArrayList<String> newStoreNameList = TextUtils.removeDuplicates((ArrayList<String>) storeList);

                            uniqueItemCount = uniqueItemCount + newItemList.size();
                            uniqueCustomerCount = uniqueCustomerCount + newCustomerList.size();
                            items.setText(String.valueOf(uniqueItemCount));


                            int noOfBills = billList.size();
                            bill_report.setText(String.valueOf(noOfBills));
                            customer.setText(String.valueOf(uniqueCustomerCount));
                            storeNameText.setText(String.valueOf(newStoreNameList.size()));
                            categoryText.setText(String.valueOf(categoryList.size()));
                            locationText.setText(String.valueOf(shippingPincodeList.size()));

                            CommonMethods.loadBarChart(barChart, (ArrayList<String>) billTimeArrayList);
                            barChart.animateXY(7000, 5000);
                            barChart.invalidate();
                            barChart.getDrawableState();
                            barChart.setPinchZoom(true);


                            CommonMethods.loadSalesBarChart(salesBarChart, billAmountHashMap);

                            salesBarChart.animateXY(7000, 5000);
                            salesBarChart.invalidate();
                            salesBarChart.getDrawableState();

                        }
                    }

                }
            }

            private void clearData() {

                billDetailsArrayList.clear();
                customerList.clear();
                itemList.clear();
                itemDetailsArrayList.clear();
                billTimeArrayList.clear();
                storeList.clear();
                billList.clear();
                itemName.clear();
                shippingPincodeList.clear();
                uniqueItemCount = 0;
                uniqueCustomerCount = 0;
                todaysTotalSalesAmt = 0;
                todaysTotalQty = 0;
                totalItemCount = 0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, ReportGenerationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        viewItemsReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, ItemsReportGenerationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        generateExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                File file = new File(Environment.getExternalStorageDirectory() + "/Demo"+date+".xls");
                if(file.exists()) {
                    file.delete();
                }

                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.progressiveStart();
                //code to view excel
        /*File file = new File(Environment.getExternalStorageDirectory()+ "/Demo.xls");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
        startActivity(intent);*/
                hssfWorkbook = new HSSFWorkbook();
                hssfSheet = hssfWorkbook.createSheet("Custom Sheet");
                headerRow1 = hssfSheet.createRow(0);

                //Header cells
                HSSFCell orderNumber = headerRow1.createCell(0);
                HSSFCell orderDate=headerRow1.createCell(1);
                HSSFCell productNumber = headerRow1.createCell(2);
                HSSFCell productName = headerRow1.createCell(3);
                HSSFCell productQty=headerRow1.createCell(4);
                HSSFCell productAmount = headerRow1.createCell(5);
                HSSFCell orderTotal = headerRow1.createCell(6);
                HSSFCell customerId = headerRow1.createCell(7);
                HSSFCell customerName = headerRow1.createCell(8);
                HSSFCell customerLocation = headerRow1.createCell(9);
                HSSFCell sellerId = headerRow1.createCell(10);
                HSSFCell sellerName = headerRow1.createCell(11);
                HSSFCell sellerLocation = headerRow1.createCell(12);
                HSSFCell orderPlacedStatus = headerRow1.createCell(13);
                HSSFCell readyForPickupStatus = headerRow1.createCell(14);
                HSSFCell deliveryIsOnTheWay = headerRow1.createCell(15);
                HSSFCell delivered = headerRow1.createCell(16);
                HSSFCell categoryName=headerRow1.createCell(17);
                HSSFCell subCategoryName=headerRow1.createCell(18);
                HSSFCell sellerPincode=headerRow1.createCell(19);
                HSSFCell distanceFromDeliveryLocationToStore=headerRow1.createCell(20);
                HSSFCell distanceFromStoreToCurrentLocation=headerRow1.createCell(21);


                orderNumber.setCellValue("Order Number");
                orderDate.setCellValue("Order Date");
                productNumber.setCellValue("Product Number");
                productName.setCellValue("Product Name");
                productQty.setCellValue("Product Qty");
                productAmount.setCellValue("Product Amount");
                orderPlacedStatus.setCellValue("Order Placed");
                readyForPickupStatus.setCellValue("Ready for pickup");
                deliveryIsOnTheWay.setCellValue("Delivery is on the way");
                delivered.setCellValue("Delivered");
                orderTotal.setCellValue("Order Total");
                customerLocation.setCellValue("Customer Location");
                customerId.setCellValue("Customer Id");
                customerName.setCellValue("Customer Name");
                sellerId.setCellValue("Seller Id");
                sellerName.setCellValue("Seller Name");
                sellerLocation.setCellValue("Seller Location");
                categoryName.setCellValue("Category Name");
                subCategoryName.setCellValue("SubCategory Name");
                sellerPincode.setCellValue("Seller Pincode");
                distanceFromDeliveryLocationToStore.setCellValue("Distance from Deliveryboy location to Store");
                distanceFromStoreToCurrentLocation.setCellValue("Distance from Store to currentLocation");

                orderDetailsDataref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                OrderDetails orderDetails = dataSnapshot1.getValue(OrderDetails.class);

                                if(orderDetails.getFormattedDate()!=null) {
                                    if (orderDetails.getFormattedDate().equals(date)) {
                                        orderDetailsArrayList.add(orderDetails);
                                        itemDetailsArrayListExcel = (ArrayList<ItemDetails>) orderDetails.getItemDetailList();
                                        for (int i = 0; i < itemDetailsArrayListExcel.size(); i++) {
                                            defaultStartingCellCount = defaultStartingCellCount + 1;
                                            headerRow = hssfSheet.createRow(defaultStartingCellCount);
                                            HSSFCell orderNumber = headerRow.createCell(0);
                                            HSSFCell orderDate = headerRow.createCell(1);


                                            HSSFCell productNumber = headerRow.createCell(2);
                                            HSSFCell productName = headerRow.createCell(3);
                                            HSSFCell productQty = headerRow.createCell(4);
                                            HSSFCell productAmount = headerRow.createCell(5);
                                            HSSFCell orderTotal = headerRow.createCell(6);
                                            HSSFCell customerId = headerRow.createCell(7);
                                            HSSFCell customerName = headerRow.createCell(8);
                                            HSSFCell customerLocation = headerRow.createCell(9);
                                            HSSFCell sellerId = headerRow.createCell(10);
                                            HSSFCell sellerName = headerRow.createCell(11);
                                            HSSFCell sellerLocation = headerRow.createCell(12);
                                            HSSFCell orderPlacedStatus = headerRow.createCell(13);
                                            HSSFCell readyForPickupStatus = headerRow.createCell(14);
                                            HSSFCell deliveryIsOnTheWay = headerRow.createCell(15);
                                            HSSFCell delivered = headerRow.createCell(16);
                                            HSSFCell categoryName = headerRow.createCell(17);
                                            HSSFCell subCategoryName = headerRow.createCell(18);
                                            HSSFCell sellerPincode = headerRow.createCell(19);
                                            HSSFCell distanceFromDeliveryLocationToStore = headerRow.createCell(20);
                                            HSSFCell distanceFromStoreToCurrentLocation = headerRow.createCell(21);

                                            orderNumber.setCellValue(orderDetails.getOrderId());
                                            orderDate.setCellValue(orderDetails.getOrderCreateDate());
                                            orderPlacedStatus.setCellValue(orderDetails.getOrderTime());
                                            readyForPickupStatus.setCellValue(orderDetails.getReadyForPickupTiming());
                                            deliveryIsOnTheWay.setCellValue(orderDetails.getDeliveryIsOnTheWayTiming());
                                            delivered.setCellValue(orderDetails.getDeliveredTiming());
                                            orderTotal.setCellValue(orderDetails.getTotalAmount());
                                            customerLocation.setCellValue(orderDetails.getShippingaddress());
                                            customerId.setCellValue(orderDetails.getCustomerId());
                                            customerName.setCellValue(orderDetails.getCustomerName());
                                            sellerName.setCellValue(orderDetails.getStoreName());
                                            sellerLocation.setCellValue(orderDetails.getStoreAddress());
                                            productAmount.setCellValue(String.valueOf(itemDetailsArrayListExcel.get(i).getItemPrice()));
                                            sellerId.setCellValue(itemDetailsArrayListExcel.get(i).getSellerId());
                                            productNumber.setCellValue(itemDetailsArrayListExcel.get(i).getItemId());
                                            productName.setCellValue(itemDetailsArrayListExcel.get(i).getItemName());
                                            productQty.setCellValue(itemDetailsArrayListExcel.get(i).getItemBuyQuantity());
                                            categoryName.setCellValue(itemDetailsArrayListExcel.get(i).getCategoryName());
                                            subCategoryName.setCellValue(itemDetailsArrayListExcel.get(i).getSubCategoryName());
                                            sellerPincode.setCellValue(itemDetailsArrayListExcel.get(i).getStorePincode());
                                            distanceFromStoreToCurrentLocation.setCellValue(orderDetails.getTotalDistanceTraveled());
                                            distanceFromDeliveryLocationToStore.setCellValue(orderDetails.getTotalDistanceDeliveryBoyFromCurrentLocationToStore());


                                            try {
                                                if (!filePath.exists()) {
                                                    filePath.createNewFile();
                                                }

                                                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                                                hssfWorkbook.write(fileOutputStream);

                                                if (fileOutputStream != null) {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                            }
                            if (orderDetailsArrayList!=null)
                            {
                                if (orderDetailsArrayList.size()>0)
                                {
                                    mProgressBar.progressiveStop();
                                    File file = new File(Environment.getExternalStorageDirectory()+ "/Demo"+date+".xls");
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                                    try {
                                        startActivity(intent);
                                    }
                                    catch (ActivityNotFoundException e) {
                                        new SweetAlertDialog(DashBoardActivity.this, SweetAlertDialog.ERROR_TYPE)

                                                .setContentText("No Application Available to View generated Excel.")
                                                .show();

                                    }
                                }else if (orderDetailsArrayList.size()==0)
                                {
                                    new SweetAlertDialog(DashBoardActivity.this, SweetAlertDialog.ERROR_TYPE)

                                            .setContentText("No more data available to generate excel.")
                                            .show();
                                    mProgressBar.progressiveStop();

                                }
                            }

                        }
                        else {

                            new SweetAlertDialog(DashBoardActivity.this, SweetAlertDialog.ERROR_TYPE)

                                    .setContentText("No more data available to generate excel.")
                                    .show();
                            mProgressBar.progressiveStop();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
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
        } else if (id == R.id.ApproveSellers) {
            Intent intent = new Intent(getApplicationContext(), ApproveSellersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.View_Orders_id) {
            Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Logout) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DashBoardActivity.this);
            bottomSheetDialog.setContentView(R.layout.logout_confirmation);
            Button logout = bottomSheetDialog.findViewById(R.id.logout);
            Button stayinapp = bottomSheetDialog.findViewById(R.id.stayinapp);

            bottomSheetDialog.show();
            bottomSheetDialog.setCancelable(false);

            logout.setOnClickListener(v -> {

                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            });
            stayinapp.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                navigationView.setCheckedItem(R.id.dashboard);
            });

        } else if (id == R.id.viewadvertisement) {
            Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.subcategory) {
            Intent intent = new Intent(getApplicationContext(), SubCategoryAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Add_tips_id) {
            Intent intent = new Intent(getApplicationContext(), TipsAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.ApproveDeliveryBoys) {
            Intent intent = new Intent(getApplicationContext(), DeliveryBoyApprovalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Add_Delivery_Fair) {
            Intent intent = new Intent(getApplicationContext(), MaintainDeliveryFairActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.Approve_items_id) {
            Intent intent = new Intent(getApplicationContext(), ItemApprovalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.contactus) {
            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.uploadcontactdetails) {
            Intent intent = new Intent(getApplicationContext(), AdminContactUploadingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.dashboard) {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.storeHistory) {
            Intent intent = new Intent(getApplicationContext(), StoreHistory.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.deliveryBoyHistory) {
            Intent intent = new Intent(getApplicationContext(), DeliveryBoyHistory.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.bulkupload) {
            Intent intent = new Intent(getApplicationContext(), BulkUploadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.adminWeeklyPayments) {
            Intent intent = new Intent(getApplicationContext(), AdminWeeklyPaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DashBoardActivity.this);
        bottomSheetDialog.setContentView(R.layout.application_exiting_dialog);
        Button quit = bottomSheetDialog.findViewById(R.id.quit_dialog);
        Button cancel = bottomSheetDialog.findViewById(R.id.cancel_dialog);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        quit.setOnClickListener(v -> {
            moveTaskToBack(true);
            bottomSheetDialog.dismiss();
        });
        cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

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
            Log.d("API123", "here");
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

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                v.setSelected(true);
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DashBoardActivity.this);
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