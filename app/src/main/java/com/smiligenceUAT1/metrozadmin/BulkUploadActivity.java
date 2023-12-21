package com.smiligenceUAT1.metrozadmin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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

public class BulkUploadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "BulkUploadActivity";
    NavigationView navigationView;
    View mHeaderView;
    DatabaseReference userDetailsDataRef;
    TextView textViewUsername, textViewEmail;
    String userName;
    Button backButton, browseButton;
    DatabaseReference databaseReference;
    long maxid = 0;
    int temp = 0;
    ListView lvInternalStorage;
    ArrayList<String> pathHistory;
    ArrayAdapter<String> adapter;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    String lastDirectory;
    int count = 0;
    File file;
    EditText searchView;
    Button btnUpDirectory, btnSDCard;
    ItemDetails itemDetails = new ItemDetails();
    ArrayList<String> categoryNameArrayList = new ArrayList<>();
    ArrayList<String> subCategoryNameArrayList = new ArrayList<>();
    ArrayList<String> itemNameArrayList = new ArrayList<>();
    ArrayList<Integer> itemQuantityArrayList = new ArrayList<>();
    ArrayList<String> QuantityUnitsArrayList = new ArrayList<>();
    ArrayList<Integer> itemPriceArrayList = new ArrayList<>();
    ArrayList<Integer> mrpPriceArrayList = new ArrayList<>();
    ArrayList<Integer> itemMaxLimitationArrayList = new ArrayList<>();
    ArrayList<String> itemBrand = new ArrayList<>();
    ArrayList<String> itemDescriptionArrayList = new ArrayList<>();
    ArrayList<String> itemFeautureArrayList = new ArrayList<>();
    ArrayList<String> itemTypeArrayList = new ArrayList<>();

    ArrayList<String> sellerIdArrayList = new ArrayList<>();
    ArrayList<String> sellerAddressArrayList = new ArrayList<>();
    ArrayList<String> storeNameArrayList = new ArrayList<>();
    ArrayList<String> storePincodeArrayList = new ArrayList<>();
    AlertDialog.Builder builder;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    ArrayList<String> validationErrorList = new ArrayList<>();
    String sellerIdString, storeNameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_upload);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle(Constant.BULK_UPLOAD);
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
        navigationView.setNavigationItemSelectedListener(BulkUploadActivity.this);
        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);
        navigationView.setCheckedItem(R.id.bulkupload);
        btnUpDirectory = findViewById(R.id.btnUpDirectory);
        btnSDCard = findViewById(R.id.btnViewSDCard);
        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);
        lvInternalStorage = findViewById(R.id.lvInternalStorage);
        searchView = findViewById(R.id.searchview);
        builder = new AlertDialog.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkFilePermissions();
        }
        databaseReference = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ProductDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                maxid = snapshot.getChildrenCount();
                temp = (int) (maxid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference(USER_DETAILS_TABLE);

        if (!"".equals(UserRoleActivity.saved_userName) && !"".equals(UserRoleActivity.saved_userName)) {
            userName = UserRoleActivity.saved_userName;
        }
        if (!"".equals(DashBoardActivity.saved_userName) && !"".equals(DashBoardActivity.saved_userName)) {
            userName = DashBoardActivity.saved_userName;
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

        //Goes up one directory level
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();

                }
            }
        });

        //Opens the SDCard or phone memory
        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvInternalStorage.setVisibility(View.VISIBLE);
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
                checkInternalStorage();
            }
        });
        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if (lastDirectory.equals(adapterView.getItemAtPosition(i))) {

                    //Execute method for reading the excel data.
                    categoryNameArrayList.clear();
                    itemBrand.clear();
                    itemDescriptionArrayList.clear();
                    itemFeautureArrayList.clear();
                    itemPriceArrayList.clear();
                    itemMaxLimitationArrayList.clear();
                    itemQuantityArrayList.clear();
                    mrpPriceArrayList.clear();
                    itemTypeArrayList.clear();
                    QuantityUnitsArrayList.clear();
                    sellerIdArrayList.clear();
                    storeNameArrayList.clear();
                    storePincodeArrayList.clear();
                    subCategoryNameArrayList.clear();
                    validationErrorList.clear();
                    String result = lastDirectory.substring(lastDirectory.length() - 3);
                    if (result.equals("xls")) {
                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to write this excel data in to database ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        try {
                                            readExcelData(lastDirectory);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (BiffException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.cancel();

                                        if (!(validationErrorList.size() > 0) && itemNameArrayList.size()>0) {
                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(BulkUploadActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.setTitleText("Data added successfully").setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    lvInternalStorage.setVisibility(View.VISIBLE);
                                                    count = 0;
                                                    pathHistory = new ArrayList<String>();
                                                    pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
                                                    checkInternalStorage();
                                                }
                                            });
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(BulkUploadActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.setTitleText("Invalid excel data").show();
                                        }

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Confirm Message");
                        alert.show();
                    } else {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(BulkUploadActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setTitleText("Invalid file format ." + result).show();
                    }

                } else {
                    count++;
                    pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                }
            }
        });

        //Goes up one directory level
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();

                }
            }
        });


        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (adapter != null) {
                    if (adapter.getCount() > 0) {
                        BulkUploadActivity.this.adapter.getFilter().filter("/sdcard/" + cs);
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        //Opens the SDCard or phone memory
        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvInternalStorage.setVisibility(View.VISIBLE);
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
                checkInternalStorage();
            }
        });
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */

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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BulkUploadActivity.this);
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
                navigationView.setCheckedItem(R.id.bulkupload);
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

        Intent intent = new Intent(BulkUploadActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void readExcelData(String filePath) throws IOException, BiffException {
        Log.d(TAG, "readExcelData: Reading Excel File.");
        String result = filePath.substring(filePath.length() - 3);
        File inputFile = new File(filePath);
        InputStream inputStream = new FileInputStream(inputFile);
        Workbook workbook = Workbook.getWorkbook(inputStream);

        Sheet s = workbook.getSheet(0);
        int row = s.getRows();

        try {
            //CATEGORYNAME row 1
            for (int i = 0; i < row; i++) {
                for (int c = 0; c < 1; c++) {
                    jxl.Cell z = s.getCell(c, i);
                    if (z.getContents() == null || "".equals(z.getContents()) || android.text.TextUtils.isEmpty(z.getContents())) {
                        validationErrorList.add("Category Name"+ "( Row" + (i+1)+" )"+z.getContents()+"her");
                    } else if (!android.text.TextUtils.isEmpty(z.getContents()) && !TextUtils.validateNames_catagoryItems(z.getContents())) {
                        validationErrorList.add("Category Name"+ "( Row" + (i+1)+" )"+z.getContents()+"here");
                    } else {
                        categoryNameArrayList.add(z.getContents());
                    }
                }
            }
            //SUB categoryName roe 2

            for (int i = 0; i < row; i++) {
                for (int c = 1; c < 2; c++) {
                    Cell z = s.getCell(c, i);

                    if (!z.getContents().equals("")&& !TextUtils.validateNames_catagoryItems(z.getContents())) {
                        validationErrorList.add("SubCategory Name" + "( Row" + (i+1)+" )"+z.getContents());
                    } else {
                        subCategoryNameArrayList.add(z.getContents());
                    }
                }
            }
            //itemname  row 3
            for (int i = 0; i < row; i++) {
                for (int c = 2; c < 3; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents().trim())) {
                        validationErrorList.add("Item Name" + "( Row" + (i+1)+" )"+z.getContents());
                    } else if ((!android.text.TextUtils.isEmpty(z.getContents().trim())
                            && !TextUtils.validateAlphaNumericCharcters(z.getContents().trim()))) {
                        validationErrorList.add("Item Name" + "( Row" + (i+1)+" )"+z.getContents());
                    } else {
                        itemNameArrayList.add(z.getContents());
                    }
                }
            }
            //item quantity row 4

            for (int i = 0; i < row; i++) {
                for (int c = 3; c < 4; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Item Quantity" + "( Row" + (i+1)+" )");
                    } else if (z.getContents().startsWith("0")) {
                        validationErrorList.add("Item Quantity" + "( Row" + (i+1)+" )");
                    } else {
                        itemQuantityArrayList.add(Integer.parseInt(z.getContents()));
                    }
                }
            }

            //item qty unit row 5
            for (int i = 0; i < row; i++) {
                for (int c = 4; c < 5; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Item Units" + "( Row" + (i+1)+" )");
                    } else {
                        QuantityUnitsArrayList.add(z.getContents());
                    }
                }
            }


            //fixed price row 6
            for (int i = 0; i < row; i++) {
                for (int c = 5; c < 6; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Fixed Price" + " ( Row" + (i+1)+" )");
                    } else if ("0".equalsIgnoreCase(z.getContents()) || !TextUtils.isValidPrice(z.getContents())) {
                        validationErrorList.add("Fixed Price" + "( Row" + (i+1)+" )");
                    } else if (z.getContents().startsWith("0")) {
                        validationErrorList.add("Fixed Price"  + "( Row" + (i+1)+" )");
                    } else {
                        itemPriceArrayList.add(Integer.parseInt(z.getContents()));
                    }
                }
            }


            //mrp price row 7
            for (int i = 0; i < row; i++) {
                for (int c = 6; c < 7; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Mrp Price" + " ( Row" + (i+1)+" )");
                    } else if ("0".equalsIgnoreCase(z.getContents()) || !TextUtils.isValidPrice(z.getContents())) {
                        validationErrorList.add("Mrp Price"+ " ( Row" + (i+1)+" )");
                    } else if (z.getContents().startsWith("0")) {
                        validationErrorList.add("Mrp Price" +" ( Row" + (i+1)+" )");
                    } else {
                        mrpPriceArrayList.add(Integer.parseInt(z.getContents()));
                    }
                }
            }


            //item max limitation row 8
            for (int i = 0; i < row; i++) {
                for (int c = 7; c < 8; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Item Limitation"+ " ( Row " + (i+1) +" )");
                    } else if ("0".equalsIgnoreCase(z.getContents()) || !TextUtils.isValidPrice(z.getContents())) {
                        validationErrorList.add("Item Limitation" + " (Row" + (i+1)+" )");
                    } else if (z.getContents().startsWith("0")) {
                        validationErrorList.add("Item Limitation" + " ( Row" + (i+1)+" )");
                    } else {
                        itemMaxLimitationArrayList.add(Integer.valueOf(z.getContents()));
                    }
                }
            }
            //item brand row 9
            for (int i = 0; i < row; i++) {
                for (int c = 8; c < 9; c++) {
                    Cell z = s.getCell(c, i);
                    itemBrand.add(z.getContents());
                }
            }

            //item description row 10
            for (int i = 0; i < row; i++) {
                for (int c = 9; c < 10; c++) {
                    Cell z = s.getCell(c, i);
                    itemDescriptionArrayList.add(z.getContents());
                }
            }
            //ityem features row 11
            for (int i = 0; i < row; i++) {
                for (int c = 10; c < 11; c++) {
                    Cell z = s.getCell(c, i);
                    itemFeautureArrayList.add(z.getContents());
                }
            }
            //item type row 12

            for (int i = 0; i < row; i++) {
                for (int c = 11; c < 12; c++) {
                    Cell z = s.getCell(c, i);
                     if (z.getContents().equals("NA") || z.getContents().equals("Veg") || z.getContents().equalsIgnoreCase("NonVeg")) {
                        itemTypeArrayList.add(z.getContents());
                    }else if (!z.getContents().equals(""))
                    {
                        validationErrorList.add("Item Type"+"( Row"+(i+1)+" ) ");
                    }

                }
            }
            //seller id row 13
            for (int i = 0; i < row; i++) {
                for (int c = 12; c < 13; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Seller Id" +"( Row" + (i+1)+" )");
                    } else {
                        sellerIdArrayList.add(z.getContents());
                    }
                }
            }
            //seller address row 14

            for (int i = 0; i < row; i++) {
                for (int c = 13; c < 14; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Store Address" + "( Row" + (i+1)+" )");
                    } else {
                        sellerAddressArrayList.add(z.getContents());
                    }
                }
            }
            //store name row 15
            for (int i = 0; i < row; i++) {
                for (int c = 14; c < 15; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Store Name" + "( Row" + (i+1)+" )");
                    } else {
                        storeNameArrayList.add(z.getContents());
                    }
                }
            }
            //store pincode 16
            for (int i = 0; i < row; i++) {
                for (int c = 15; c < 16; c++) {
                    Cell z = s.getCell(c, i);
                    if ("".equals(z.getContents())) {
                        validationErrorList.add("Store Pincode" + "( Row" + (i+1)+" )");
                    } else if (!((z.getContents().length()) == 6)) {
                        validationErrorList.add("Store Pincode" + "( Row" + (i+1)+" )");
                    } else {
                        storePincodeArrayList.add(z.getContents());
                    }
                }
            }

        } catch (Exception e) {

        }


        if (validationErrorList.size() > 0) {
            if (sellerIdArrayList != null) {
                if (sellerIdArrayList.size() > 0) {
                    sellerIdString = sellerIdArrayList.get(0);
                }
            }
            if (storeNameArrayList != null) {
                if (storeNameArrayList.size() > 0) {
                    storeNameString = storeNameArrayList.get(0);
                }
            }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BulkUploadActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.error_layout, null);
            dialogBuilder.setView(dialogView);


            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            TextView storeName = dialogView.findViewById(R.id.storeName);


            final ListView errorListview = dialogView.findViewById(R.id.mainListView);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, validationErrorList);
            storeName.setText(storeNameString + "( " + sellerIdString + " )");

            // Set the ArrayAdapter as the ListView's adapter.
            errorListview.setAdapter(listAdapter);
            final AlertDialog b = dialogBuilder.create();
            b.show();
            b.setCancelable(false);

        } else {

            for (int i = 0; i < row; i++) {

                System.out.println("RESULT" + itemBrand.size());
                itemDetails.setCategoryName(categoryNameArrayList.get(i).trim().toUpperCase());
                itemDetails.setItemApprovalStatus("Waiting for approval");
                itemDetails.setItemAvailableQuantity(0);
                itemDetails.setItemBrand(itemBrand.get(i).trim());
                itemDetails.setItemBuyQuantity(0);
                itemDetails.setItemCounter(0);
                itemDetails.setItemDescription(itemDescriptionArrayList.get(i).trim());
                itemDetails.setItemFeatures(itemFeautureArrayList.get(i).trim());
                itemDetails.setItemId(temp + 1);
                itemDetails.setItemImage("");
                itemDetails.setItemPrice(itemPriceArrayList.get(i));
                itemDetails.setItemQuantity(itemQuantityArrayList.get(i));
                itemDetails.setItemStatus("Active");
                if (itemTypeArrayList.get(i).equals("")) {
                    itemDetails.setItemType("NA");
                }else {
                    itemDetails.setItemType(itemTypeArrayList.get(i).trim());
                }
                itemDetails.setMRP_Price(mrpPriceArrayList.get(i));
                itemDetails.setQuantityUnits(QuantityUnitsArrayList.get(i).trim());
                itemDetails.setSellerId(sellerIdArrayList.get(i).trim());
                itemDetails.setStoreAdress(sellerAddressArrayList.get(i).trim());
                itemDetails.setStoreLogo("");
                itemDetails.setStoreName(storeNameArrayList.get(i).trim());
                itemDetails.setStorePincode(storePincodeArrayList.get(i).trim());
                if (subCategoryNameArrayList.get(i).equals(""))
                {
                    itemDetails.setSubCategoryName("GENERAL CATEGORY");
                }else {
                    itemDetails.setSubCategoryName(subCategoryNameArrayList.get(i).trim().toUpperCase());
                }
                itemDetails.setTotalItemQtyPrice(0);
                itemDetails.setWishList(false);
                itemDetails.setItemMaxLimitation(itemMaxLimitationArrayList.get(i));
                itemDetails.setItemMinLimitation(0);
                itemDetails.setItemName(itemNameArrayList.get(i).trim());
                itemDetails.setGiftAmount(0);
                itemDetails.setGiftWrapOption("No");
                databaseReference.child(String.valueOf(temp + 1)).setValue(itemDetails);
                temp = temp + 1;
            }
        }
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");

            } else {
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++) {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BulkUploadActivity.this);
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