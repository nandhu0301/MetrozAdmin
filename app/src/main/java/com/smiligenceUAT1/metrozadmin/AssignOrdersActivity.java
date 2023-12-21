package com.smiligenceUAT1.metrozadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.adapter.AssignOrderAdapter;
import com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter;
import com.smiligenceUAT1.metrozadmin.bean.CheckIn_CheckOut;
import com.smiligenceUAT1.metrozadmin.bean.DeliveryBoy;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.OrderDetails;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADD_ADVERTISEMNETS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ADMIN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_DELIVERY_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_ITEMS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_STORE_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGNED_TO_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGN_ORDERS_FOR_DELIVERY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BOOLEAN_FALSE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BOOLEAN_TRUE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BULK_UPLOAD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERED_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_BOY_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_DETAILS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_ON_WAY_STATUS_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_DELIVERY_FARE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_OFFERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_TIPS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ORDER_PLACED_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ORDER_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.READY_FOR_PICKUP_STATUS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.SELECT_STATUS;
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

public class AssignOrdersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DatabaseReference loginDataRef, orderDetailsDataRef, userDetailsDataRef;
    boolean check = true;
    ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();
    String roleNameStr, deliveryBoyNameStr, role_Name;
    ListView orderListView;
    AlertDialog dialog;
    TextView orderNumber, storeName, placedState, orderStatus;
    Spinner assignedTo;
    Button assignRole;
    ImageView cancel;
    OrderDetails billDetails;
    ArrayList<String> assignedToList = new ArrayList<>();
    ArrayList<String> assignedToListNew = new ArrayList<>();
    boolean isSpinnerValuePresent;
    ArrayList<String> referenceArrayList = new ArrayList<>();
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;
    ArrayList<String> IdList = new ArrayList<>();
    ArrayList<String> IdListNew = new ArrayList<>();
    ArrayList<String> image_list = new ArrayList<>();
    ArrayList<String> distance_list = new ArrayList<>();
    ArrayList<String> distance_list_new = new ArrayList<>();
    ArrayList<String> image_list_New = new ArrayList<>();
    ArrayList<Integer> distanceFromCurrentLocationToDeliveryBoy = new ArrayList<>();
    ArrayList<Integer> distanceFromCurrentLocationToDeliveryBoyNew = new ArrayList<>();
    String role_Id;
    String image;
    int distanceTraveled;
    CheckIn_CheckOut checkIn_checkOut;

    Spinner orderGroupSpinner;
    String orderStatusForGrouping;
    ArrayList<CheckIn_CheckOut> checkIn_checkOutArrayList = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    NavigationView navigationView;
    DatabaseReference deliveryBoyAttendanceDataRef;
    String userName;
    boolean ischeck = true;
    double sharedPreferenceLatitude, sharedPreferenceLongtitude;
    String sharedPreferenceLat, sharedPreferenceLong;
    double getStoreLatitude = 0.0, getStoreLongtitude = 0.0, roundOff;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_assign_orders );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        androidx.appcompat.widget.Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle(Constant.ASSIGN_ORDERS_FOR_DELIVERY);


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
        navigationView.setNavigationItemSelectedListener(AssignOrdersActivity.this);

        navigationView.setCheckedItem(R.id.AssignOrders);

        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);

        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);


        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);

        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);
        if (!"".equals(UserRoleActivity.saved_userName) && !"".equals(UserRoleActivity.saved_userName)) {

            userName = UserRoleActivity.saved_userName;
        }
        if (!"".equals(DashBoardActivity.saved_userName) && !"".equals(DashBoardActivity.saved_userName)) {
            userName = DashBoardActivity.saved_userName;
        }

        final Query navQuery = userDetailsDataRef.orderByChild(PHONE_NUM_COLUMN).equalTo(String.valueOf(userName));
        navQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserDetails loginUserDetailslist = snap.getValue(UserDetails.class);
                    String roleNameStr = loginUserDetailslist.getRoleName().toUpperCase();
                    String deliveryBoyNameStr = loginUserDetailslist.getFirstName().toUpperCase();
                    textViewUsername.setText(deliveryBoyNameStr);
                    textViewEmail.setText(roleNameStr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loginDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyLoginDetails");

        //Todo Hardcoded values

        orderDetailsDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("OrderDetails");
        deliveryBoyAttendanceDataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DeliveryBoyAttendanceTable");


        orderListView = findViewById(R.id.orderlistview);
        orderGroupSpinner = findViewById(R.id.spinnerStatus);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_type, ORDER_STATUS);
        orderGroupSpinner.setAdapter(arrayAdapter);


        loadFunction();

        orderGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                orderStatusForGrouping = (String) parentView.getItemAtPosition(position);
                if (orderStatusForGrouping.equals(ORDER_PLACED_STATUS)) {
                    loadFunctionAccordingToStatus(orderStatusForGrouping);
                } else if (orderStatusForGrouping.equals(READY_FOR_PICKUP_STATUS)) {
                    loadFunctionAccordingToStatus(orderStatusForGrouping);
                } else if (orderStatusForGrouping.equals(DELIVERY_ON_WAY_STATUS_STATUS)) {
                    loadFunctionAccordingToStatus(orderStatusForGrouping);
                } else if (orderStatusForGrouping.equals(DELIVERED_STATUS)) {
                    loadFunctionAccordingToStatus(orderStatusForGrouping);
                } else {
                    loadFunction();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        final Query roleNameQuery = loginDataRef.orderByChild(PHONE_NUM_COLUMN).equalTo(String.valueOf(UserRoleActivity.saved_userName));
        roleNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    UserDetails loginUserDetailslist = snap.getValue(UserDetails.class);
                    roleNameStr = loginUserDetailslist.getRoleName().toUpperCase();
                    deliveryBoyNameStr = loginUserDetailslist.getFirstName().toUpperCase();

                }
                if (ADMIN.equals(roleNameStr)) {
                    loadFunction();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        autoLoadFunction();
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AssignOrdersActivity.this);
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
                navigationView.setCheckedItem(R.id.AssignOrders);
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
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void adminFunction(ArrayList<String> arrayList) {
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignOrdersActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View alertdialogView = inflater.inflate(R.layout.assign_order_layout, null);
                alertDialogBuilder.setView(alertdialogView);

                orderNumber = alertdialogView.findViewById(R.id.ordernumbertxt);
                storeName = alertdialogView.findViewById(R.id.storenametxt);
                placedState = alertdialogView.findViewById(R.id.placeddatetxt);
                orderStatus = alertdialogView.findViewById(R.id.statustxt);
                assignedTo = alertdialogView.findViewById(R.id.assignedTospinner);

                assignRole = alertdialogView.findViewById(R.id.AssignRole);
                cancel = alertdialogView.findViewById(R.id.Cancel);


                billDetails = orderDetailsArrayList.get(i);

                SharedPreferences sharedPreferences = getSharedPreferences("STOREDISTANCE", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("STORELATITUDE", String.valueOf(billDetails.getSellerLatitude()));
                editor.putString("STORELONGTITUDE", String.valueOf(billDetails.getSellerLongtitude()));

                editor.commit();
                autoLoadFunction();
                if (billDetails.getOrderStatus().equals("Delivered")) {

                } else {
                    dialog = alertDialogBuilder.create();
                    dialog.show();
                    dialog.setCancelable(BOOLEAN_FALSE);
                    orderNumber.setText(String.valueOf(billDetails.getOrderId()));
                    storeName.setText(billDetails.getStoreName());
                    placedState.setText(billDetails.getPaymentDate());
                    orderStatus.setText(billDetails.getOrderStatus());
                    TextUtils.removeDuplicates(distance_list_new);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.spinner_type, distance_list_new);
                    assignedTo.setAdapter(adapter);

                    if (!"".equals(billDetails.getOrderStatus())) {
                        String role_Name = billDetails.getOrderStatus();
                        assignedTo.setSelection(distance_list_new.indexOf(role_Name));
                    }
                    assignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // your code here
                            if (position == 0) {
                                isSpinnerValuePresent = BOOLEAN_FALSE;
                            } else {
                                role_Name = assignedToListNew.get(position);
                                role_Id = IdListNew.get(position - 1);
                                image = image_list_New.get(position - 1);
                                distanceTraveled = distanceFromCurrentLocationToDeliveryBoyNew.get(position - 1);
                                isSpinnerValuePresent = BOOLEAN_TRUE;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    assignRole.setOnClickListener(v -> {
                        if (isSpinnerValuePresent == BOOLEAN_TRUE) {
                            Intent intent = getIntent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            DatabaseReference ref = orderDetailsDataRef.child(String.valueOf(billDetails.getOrderId()));
                            ref.child(ASSIGNED_TO_STATUS).setValue(role_Name);
                            ref.child("totalDistanceDeliveryBoyFromCurrentLocationToStore").setValue(distanceTraveled);
                            ref.child("notificationStatus").setValue("false");
                            ref.child("deliverboyId").setValue(role_Id);
                            ref.child("deliveryBoyImage").setValue(image);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), SELECT_STATUS, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void loadFunctionAccordingToStatus(String statusString) {
        Query query = orderDetailsDataRef.orderByChild("orderStatus").equalTo(statusString);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (check) {
                    orderDetailsArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        billDetails = dataSnapshot1.getValue(OrderDetails.class);
                        orderDetailsArrayList.add(billDetails);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(orderDetailsArrayList);
                    }
                    AssignOrderAdapter assignOrderAdapter = new AssignOrderAdapter(AssignOrdersActivity.this, orderDetailsArrayList);
                    orderListView.setAdapter(assignOrderAdapter);
                    assignOrderAdapter.notifyDataSetChanged();
                    adminFunction(referenceArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void loadFunction() {

        orderDetailsDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (check) {
                    orderDetailsArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        billDetails = dataSnapshot1.getValue(OrderDetails.class);
                        orderDetailsArrayList.add(billDetails);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(orderDetailsArrayList);
                    }
                    AssignOrderAdapter assignOrderAdapter = new AssignOrderAdapter(AssignOrdersActivity.this, orderDetailsArrayList);
                    orderListView.setAdapter(assignOrderAdapter);
                    assignOrderAdapter.notifyDataSetChanged();
                    adminFunction(referenceArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AssignOrdersActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void autoLoadFunction() {
        final SharedPreferences loginSharedPreferences = getSharedPreferences("STOREDISTANCE", MODE_PRIVATE);

        if (loginSharedPreferences != null && !loginSharedPreferences.equals("")) {
            sharedPreferenceLat = loginSharedPreferences.getString("STORELATITUDE", "");
            sharedPreferenceLong = loginSharedPreferences.getString("STORELONGTITUDE", "");
        }
        if (sharedPreferenceLat != null && !sharedPreferenceLat.equals("")) {
            sharedPreferenceLatitude = Double.parseDouble(sharedPreferenceLat);
        }
        if (sharedPreferenceLong != null && !sharedPreferenceLong.equals("")) {
            sharedPreferenceLongtitude = Double.parseDouble(sharedPreferenceLong);
        }

        loginDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assignedToList.clear();
                IdList.clear();
                image_list.clear();
                assignedToListNew.clear();
                IdListNew.clear();
                image_list_New.clear();
                distance_list.clear();
                distanceFromCurrentLocationToDeliveryBoy.clear();
                if (check) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot valuationDatasnapshot : dataSnapshot.getChildren()) {
                            roundOff = 0.0;
                            DeliveryBoy loginUserDetails = valuationDatasnapshot.getValue(DeliveryBoy.class);
                            assignedToList.add(loginUserDetails.getFirstName());
                            IdList.add(loginUserDetails.getDeliveryBoyId());
                            image_list.add(loginUserDetails.getDeliveryBoyProfile());
                            int Radius = 6371;// radius of earth in Km
                            double lat1 = sharedPreferenceLatitude;
                            double lat2 = loginUserDetails.getDeliveryBoyLatitude();
                            double lon1 = sharedPreferenceLongtitude;
                            double lon2 = loginUserDetails.getDeliveryBoyLongtitude();
                            double dLat = Math.toRadians(lat2 - lat1);
                            double dLon = Math.toRadians(lon2 - lon1);
                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                    + Math.cos(Math.toRadians(lat1))
                                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                                    * Math.sin(dLon / 2);
                            double c = 2 * Math.asin(Math.sqrt(a));
                            double valueResult = Radius * c;
                            double km = valueResult / 1;
                            DecimalFormat newFormat = new DecimalFormat("####");
                            int kmInDec = Integer.valueOf(newFormat.format(km));
                            double meter = valueResult % 1000;
                            int meterInDec = Integer.valueOf(newFormat.format(meter));
                            int resultKiloMeterRoundOff = (int) Math.round(km);
                            roundOff = Math.round(km * 100.0) / 100.0;

                            Location location1 = new Location("");
                            location1.setLatitude(lat1);
                            location1.setLongitude(lon1);
                            Location location2 = new Location("");
                            location2.setLatitude(lat2);
                            location2.setLongitude(lon2);
                            distance_list.add(loginUserDetails.getFirstName() + "[ " + roundOff + "Kms ]");
                            distanceFromCurrentLocationToDeliveryBoy.add(resultKiloMeterRoundOff);
                        }
                        assignedToListNew.clear();
                        IdListNew.clear();
                        image_list_New.clear();
                        distance_list_new.clear();
                        assignedToListNew.add("SELECT");
                        distance_list_new.add("SELECT");
                        distanceFromCurrentLocationToDeliveryBoyNew.clear();
                        for (int i = 0; i < IdList.size(); i++) {
                            Query IdBasedQuery = deliveryBoyAttendanceDataRef.child(IdList.get(i)).child(DateUtils.fetchCurrentDate());
                            final int finalI = i;
                            IdBasedQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (check) {
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            CheckIn_CheckOut checkIn_checkOut = dataSnapshot.getValue(CheckIn_CheckOut.class);
                                            if (checkIn_checkOut.getCheckOutTime() != null && checkIn_checkOut.getCheckInTime() != null) {
                                                if (("".equals(checkIn_checkOut.getCheckOutTime())) && !("".equals(checkIn_checkOut.getCheckInTime()))) {
                                                    assignedToListNew.add(assignedToList.get(finalI));
                                                    IdListNew.add(IdList.get(finalI));
                                                    image_list_New.add(image_list.get(finalI));
                                                    distance_list_new.add(distance_list.get(finalI));
                                                    distanceFromCurrentLocationToDeliveryBoyNew.add(distanceFromCurrentLocationToDeliveryBoy.get(finalI));
                                                    TextUtils.removeDuplicates(distance_list_new);
                                                    TextUtils.removeDuplicates(assignedToListNew);
                                                }
                                            } else {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AssignOrdersActivity.this);
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