package com.smiligenceUAT1.metrozadmin;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smiligenceUAT1.metrozadmin.adapter.DiscountAdapter;
import com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter;
import com.smiligenceUAT1.metrozadmin.bean.Discount;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.*;


public class ViewDiscountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton add_Discount_Button;


    EditText discountName, e_Price, min_cash_amount, maxCash;
    RadioGroup percentPrice;
    Button b_choosefile, b_upload;
    String price_percentString;
    ImageView image_discount;

    final static int PICK_IMAGE_REQUEST = 10;
    ProgressBar progressBar;
    RadioButton radioButton;
    private Uri mimageuri;
    String discount_Name;
    int maxid = 0;
    AlertDialog dialog;
    TextInputLayout max_price_text;
    DatabaseReference discountDatabaseRefs, logindataRef, userDetailsDataRef;
    StorageReference discountStorageRef;

    UploadTask storageTask;
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;
    String saved_username;
    DiscountAdapter discountAdapter;

    ListView dis_grid;
    NavigationView navigationView;
    String userName;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();



    private ArrayList<Discount> discountArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_discount);

        add_Discount_Button = findViewById(R.id.fab);
        dis_grid = findViewById(R.id.dis_mainGridView);
        disableAutofill();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        discountDatabaseRefs = CommonMethods.fetchFirebaseDatabaseReference(DISCOUNT_DETAILS_FIREBASE_TABLE);
        discountStorageRef = CommonMethods.fetchFirebaseStorageReference(DISCOUNT_DETAILS_FIREBASE_TABLE);
        logindataRef = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(USER_DETAILS_TABLE);
        androidx.appcompat.widget.Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle(MAINTAIN_OFFERS);
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
        navigationView.setNavigationItemSelectedListener(ViewDiscountActivity.this);
        navigationView.setCheckedItem(R.id.Add_Discounts_id);
        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);

        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);

        if (!"".equals(UserRoleActivity.saved_userName) && !"".equals(UserRoleActivity.saved_userName)) {

            userName = UserRoleActivity.saved_userName;
        }
        if (!"".equals(DashBoardActivity.saved_userName) && !"".equals(DashBoardActivity.saved_userName)) {
            userName = DashBoardActivity.saved_userName;
        }

        final Query roleNameQuery = logindataRef.orderByChild(PHONE_NUM_COLUMN).equalTo(String.valueOf(userName));
        roleNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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


        discountDatabaseRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = (int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        add_Discount_Button.setOnClickListener((View.OnClickListener) v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDiscountActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertdialogView = inflater.inflate(R.layout.activity_add_discounts, null);
            alertDialogBuilder.setView(alertdialogView);


            discountName = alertdialogView.findViewById(R.id.d_name);
            e_Price = alertdialogView.findViewById(R.id.text_priceper);
            percentPrice = alertdialogView.findViewById(R.id.pricepercent);
            min_cash_amount = alertdialogView.findViewById(R.id.min_amount);
            progressBar = alertdialogView.findViewById(R.id.progressBar);
            max_price_text = alertdialogView.findViewById(R.id.max_price_text);


            maxCash = alertdialogView.findViewById(R.id.max_amount);
            b_choosefile = alertdialogView.findViewById(R.id.pick);
            image_discount = alertdialogView.findViewById(R.id.imageupload);
            b_upload = alertdialogView.findViewById(R.id.upload);


            ImageView cancel = alertdialogView.findViewById(R.id.iv_cancel);


            dialog = alertDialogBuilder.create();
            dialog.show();

            cancel.setOnClickListener(v13 -> dialog.dismiss());

            b_choosefile.setOnClickListener(v12 -> openFileChooser());

            percentPrice.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
                checkedId = percentPrice.getCheckedRadioButtonId();
                radioButton = (RadioButton) dialog.findViewById(checkedId);
                price_percentString = radioButton.getText().toString();

                if ("price".equals(price_percentString)) {
                    int maxTextLength = 4;
                    e_Price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxTextLength)});
                    if (e_Price.getText().toString().length() > 4) {
                        e_Price.setText("");
                    }
                    maxCash.setTextIsSelectable(BOOLEAN_FALSE);
                    maxCash.setFocusable(BOOLEAN_FALSE);
                    maxCash.setVisibility(View.INVISIBLE);
                    max_price_text.setVisibility(View.INVISIBLE);
                } else if ("percent".equals(price_percentString)) {
                    int maxTextLength = 2;
                    e_Price.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxTextLength)});
                    if (e_Price.getText().toString().length() > 2) {
                        e_Price.setText("");
                    }
                    maxCash.setTextIsSelectable(BOOLEAN_TRUE);
                    maxCash.setFocusable(BOOLEAN_TRUE);
                    maxCash.setVisibility(View.VISIBLE);
                    max_price_text.setVisibility(View.VISIBLE);
                }
            });

            b_upload.setOnClickListener(v1 -> {
                String amount_min_cash_amount=min_cash_amount.getText().toString().trim();
                String amount_maxCash=maxCash.getText().toString().trim();

                if (discountName == null || "".equalsIgnoreCase(discountName.getText().toString())) {
                    discountName.setError("Enter Discount Name");
                    return;
                } else if (!"".equalsIgnoreCase(discountName.getText().toString()) && !TextUtils.validDiscountName(discountName.getText().toString())) {
                    discountName.setError("Enter valid Discount Name");
                    return;
                } else if (price_percentString == null || "".equalsIgnoreCase(price_percentString)) {
                    Toast.makeText(ViewDiscountActivity.this, "Please select the Discount Type.", Toast.LENGTH_SHORT).show();
                } else if ("price".equals(price_percentString)) {
                    maxCash.setText(TEXT_BLANK);

                    if (e_Price == null || "".equalsIgnoreCase(e_Price.getText().toString())) {
                        e_Price.setError("Enter valid Discount Price");
                        return;
                    } else if (min_cash_amount == null || "".equalsIgnoreCase(min_cash_amount.getText().toString())) {
                        min_cash_amount.setError(REQUIRED_MSG);
                        return;
                    } else if (!"".equalsIgnoreCase(min_cash_amount.getText().toString()) && !TextUtils.isValidPrice(min_cash_amount.getText().toString())) {
                        min_cash_amount.setError("Enter valid minimum Bill amount");
                        return;
                    }else if (Integer.parseInt(e_Price.getText().toString())>=Integer.parseInt(min_cash_amount.getText().toString()))
                    {
                        e_Price.setError("Discount amount must be less than minimum bill amount");
                        return;
                    }
                } else if ("percent".equals(price_percentString)) {

                    if (e_Price == null || "".equalsIgnoreCase(e_Price.getText().toString())) {
                        e_Price.setError("Enter valid Discount Percentage");
                        return;
                    } else if (min_cash_amount == null || "".equalsIgnoreCase(min_cash_amount.getText().toString())) {
                        min_cash_amount.setError(REQUIRED_MSG);
                        return;
                    } else if (!"".equalsIgnoreCase(min_cash_amount.getText().toString()) && !TextUtils.isValidPrice(min_cash_amount.getText().toString())) {
                        min_cash_amount.setError("Enter valid minimum Bill amount");
                        return;
                    }else if (Integer.parseInt(e_Price.getText().toString())>=Integer.parseInt(min_cash_amount.getText().toString()))
                    {
                        e_Price.setError("Discount amount must be less than minimum bill amount");
                        return;
                    }
                    else if (amount_maxCash.startsWith("0")) {
                        maxCash.setError("Minimum Bill amount should not starts with (0)");
                        if (amount_maxCash.length() > 0) {
                            maxCash.setText(amount_maxCash.substring(1));
                            return;
                        } else {
                            maxCash.setText("");
                            return;
                        }
                    }
                    else if (maxCash == null || "".equalsIgnoreCase(maxCash.getText().toString())) {
                        maxCash.setError(REQUIRED_MSG);
                        return;
                    }
                    else if (!"".equalsIgnoreCase(maxCash.getText().toString()) && !TextUtils.isValidPrice(maxCash.getText().toString())) {
                        maxCash.setError("Enter valid maximum discount amount");
                        return;
                    }
                    else if (amount_min_cash_amount.startsWith("0")) {
                        min_cash_amount.setError("Maximum Discount should not starts with (0)");
                        if (amount_min_cash_amount.length() > 0) {
                            min_cash_amount.setText(amount_min_cash_amount.substring(1));
                            return;
                        } else {
                            min_cash_amount.setText("");
                            return;
                        }
                    }
                    else  if(!"".equalsIgnoreCase(maxCash.getText().toString())&& !(Integer.parseInt(maxCash.getText().toString())< Integer.parseInt(min_cash_amount.getText().toString()))){
                        Toast.makeText(this, "Minimum Bill Amount cannot less then Discount Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!"".equals(discountName.getText().toString().trim())
                        && !"".equals(e_Price.getText().toString().trim())) {

                    UploadFile();
                }
            });
        });

        discountDatabaseRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discountArrayList.clear();

                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Discount discount = postSnapshot.getValue(Discount.class);
                        discountArrayList.add(discount);
                    }

                    discountAdapter = new DiscountAdapter(ViewDiscountActivity.this, discountArrayList);
                    discountAdapter.notifyDataSetChanged();
                    dis_grid.setAdapter(discountAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dis_grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Discount displayDiscount = discountArrayList.get(position);
                final String discountId = String.valueOf(displayDiscount.getDiscountId());
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDiscountActivity.this);
                builder.setMessage("Select the state of Discount");
                builder.setCancelable(false);
                builder.setNegativeButton(INACTIVE_STATUS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        discountDatabaseRefs.child(discountId).child(DISCOUNT_STATUS_COLUMN).setValue(INACTIVE_STATUS);
                    }

                });
                builder.setPositiveButton("Active", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        discountDatabaseRefs.child(discountId).child(DISCOUNT_STATUS_COLUMN).setValue(ACTIVE_STATUS);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mimageuri = data.getData();

            Glide.with(ViewDiscountActivity.this).load(mimageuri).into(image_discount);
        }
    }

    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadFile() {

        if (mimageuri != null) {
            StorageReference fileRef = discountStorageRef.child(System.currentTimeMillis() + "." + getExtenstion(mimageuri));

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            storageTask = (UploadTask) fileRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 5000);

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                final Uri downloadUrl = urlTask.getResult();

                discount_Name = discountName.getText().toString().trim().toUpperCase();
                Query query = discountDatabaseRefs.orderByChild(DISCOUNT_NAME_COLUMN).equalTo(discount_Name);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getChildrenCount() > 0) {

                            Toast.makeText(ViewDiscountActivity.this, "Discount Name Already Exists", Toast.LENGTH_LONG).show();
                            discountName.setText(TEXT_BLANK);
                            b_upload.setVisibility(View.VISIBLE);
                            e_Price.setTextIsSelectable(BOOLEAN_TRUE);
                            e_Price.setFocusable(BOOLEAN_TRUE);
                            min_cash_amount.setTextIsSelectable(BOOLEAN_TRUE);
                            min_cash_amount.setFocusable(BOOLEAN_TRUE);
                            maxCash.setTextIsSelectable(BOOLEAN_TRUE);
                            maxCash.setFocusable(BOOLEAN_TRUE);
                        } else {

                            Discount discount = new Discount();
                            discount.setDiscountId(maxid + 1);
                            discount.setDiscountName(discount_Name);
                            discount.setDiscountType(BILL_DISCOUNT);

                            if (price_percentString.equals("price")) {
                                discount.setDiscountPrice(e_Price.getText().toString());
                                discount.setDiscountPercentageValue("");
                            } else if (price_percentString.equals("percent")) {
                                discount.setDiscountPercentageValue(e_Price.getText().toString());
                                discount.setDiscountPrice("");
                            } else {
                                discount.setDiscountPercentageValue("");
                                discount.setDiscountPrice("");
                            }

                            discount.setDiscountImage(downloadUrl.toString());
                            discount.setDiscountStatus(ACTIVE_STATUS);
                            discount.setBuydiscountItem("");
                            discount.setDiscountGivenBy("Admin");
                            discount.setGetdiscountItem("");
                            discount.setBuyOfferCount(0);
                            discount.setGetOfferCount(0);
                            discount.setDiscountCoupon("");
                            discount.setMinmumBillAmount("");

                            if ("price".equals(price_percentString))
                            {
                                discount.setTypeOfDiscount("Price");
                            }
                            else
                            {
                                discount.setTypeOfDiscount("Percent");
                            }
                            if (!(maxCash.getText().toString().isEmpty())) {
                                discount.setMaxAmountForDiscount(maxCash.getText().toString());
                            } else {
                                discount.setMaxAmountForDiscount("");
                            }

                            if (!(min_cash_amount.getText().toString().isEmpty())) {
                                //   min_cash_amount.getText().toString()<
                                discount.setMinmumBillAmount(min_cash_amount.getText().toString());
                                min_cash_amount.setText("");
                            } else {
                                discount.setMinmumBillAmount("");
                            }

                            discount.setCreateDate(DateUtils.fetchCurrentDateAndTime());
                            discountDatabaseRefs.child(String.valueOf(maxid + 1)).setValue(discount);

                            dialog.dismiss();
                            Toast.makeText(ViewDiscountActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();

                            clearData();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }).addOnFailureListener(e -> Toast.makeText(ViewDiscountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show()).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(ViewDiscountActivity.this, IMAGE_SELECT_MSG, Toast.LENGTH_LONG).show();
            b_upload.setVisibility(View.VISIBLE);
        }
    }

    public void clearData() {
        discountName.setText(TEXT_BLANK);
        e_Price.setText(TEXT_BLANK);
        min_cash_amount.setText(TEXT_BLANK);
        maxCash.setText(TEXT_BLANK);
        mimageuri = null;
        image_discount.setImageResource(R.drawable.b_chooseimage);
        b_upload.setVisibility(View.VISIBLE);
        e_Price.setTextIsSelectable(BOOLEAN_TRUE);
        e_Price.setFocusable(BOOLEAN_TRUE);
        min_cash_amount.setTextIsSelectable(BOOLEAN_TRUE);
        min_cash_amount.setFocusable(BOOLEAN_TRUE);
        maxCash.setTextIsSelectable(BOOLEAN_TRUE);
        maxCash.setFocusable(BOOLEAN_TRUE);

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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewDiscountActivity.this);
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
                navigationView.setCheckedItem(R.id.Add_Discounts_id);
            });

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

        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewDiscountActivity.this);
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