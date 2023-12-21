package com.smiligenceUAT1.metrozadmin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.smiligenceUAT1.metrozadmin.adapter.CategoryAdapter;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetails;
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

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADD_ADVERTISEMNETS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_DELIVERY_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_ITEMS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_STORE_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGN_ORDERS_FOR_DELIVERY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BOOLEAN_FALSE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BOOLEAN_TRUE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BULK_UPLOAD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.CATAGORY_EXIST;
import static com.smiligenceUAT1.metrozadmin.common.Constant.CATEGORY_DETAILS_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.CATEGORY_IMAGE_STORAGE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.CATEGORY_NAME;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DEFAULT_ROW_COUNT;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_BOY_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_DETAILS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.INVALID_CATAGORY_NAME;
import static com.smiligenceUAT1.metrozadmin.common.Constant.INVALID_CATEGORY_NAME;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_DELIVERY_FARE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_OFFERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_TIPS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PLEASE_SELECT_IMAGE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.REQUIRED_MSG;
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


public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton add_Categories;
    DatabaseReference categoryRef, userDetailsDataRef, itemDataRef;
    StorageReference category_storage;
    private Uri mimageuri, mimageuriUpdate;
    private StorageTask mItemStorageTask;
    long maxid = 0;
    ImageView categoryImage_imageview;
    EditText categoryName_edittext, minimumCartValue_Edittext, deliveryCharge_Edittext,normalDistanceDelivery,longDistanceDelivery;
    ImageView updateCategoryImage;
    private ArrayList<CategoryDetails> catagoryList = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecyclerView;
    Intent intentImage = new Intent();
    private static int PICK_IMAGE_REQUEST;
    TextView textViewUsername, textStorename;
    //public static String saved_username, saved_password;
    String catagoryName;
    String categoryPrioritySelected;
    Boolean isCategoryPriorityValuePresent;
    View mHeaderView;
    NavigationView navigationView;
    String userName;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);

        disableAutofill();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setTitle(TITLE_CATEGORY);

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
        navigationView.setNavigationItemSelectedListener(CategoryActivity.this);
        navigationView.setCheckedItem(R.id.category);

        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);

        textViewUsername = mHeaderView.findViewById(R.id.name);
        textStorename = mHeaderView.findViewById(R.id.roleName);


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
                    textStorename.setText(loginUserDetailslist.getRoleName().toUpperCase());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        categoryRef = CommonMethods.fetchFirebaseDatabaseReference(CATEGORY_DETAILS_TABLE);
        category_storage = CommonMethods.fetchFirebaseStorageReference(CATEGORY_IMAGE_STORAGE);

        add_Categories = findViewById(R.id.add_Catagories);
        categoryRecyclerView = findViewById(R.id.catagoryGrid);

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(CategoryActivity.this, DEFAULT_ROW_COUNT));
        categoryRecyclerView.setHasFixedSize(true);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                catagoryList.clear();


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CategoryDetails categoryDetails = postSnapshot.getValue(CategoryDetails.class);
                        catagoryList.add(categoryDetails);
                    }

                if (catagoryList.size() > 0) {
                    categoryAdapter = new CategoryAdapter(CategoryActivity.this, catagoryList, 0);
                    categoryAdapter.notifyDataSetChanged();
                    categoryRecyclerView.setAdapter(categoryAdapter);
                }

                if (categoryAdapter != null) {

                    categoryAdapter.setOnItemclickListener(Position -> {

                        final CategoryDetails categoryDetails = catagoryList.get(Position);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.update_category_dialog, null);
                        dialogBuilder.setView(dialogView);

                        final EditText updateCatagoryName = dialogView.findViewById(R.id.updatecatagoryname_dialog);
                        final EditText updateMinimumCartValue = dialogView.findViewById(R.id.updateminimumcartvalue_dialog);
                        final EditText updateDeliveryCharge = dialogView.findViewById(R.id.updatedeliverycharge_dialog);
                        final EditText updateNormalDistanceDeliveryCharge=dialogView.findViewById(R.id.updateminimumdistancedelivery);
                        final EditText updatelongDistanceDeliveryCharge=dialogView.findViewById(R.id.updatelongdistancedelivery);



                        final Button updateCategoryPickImageButton = dialogView.findViewById(R.id.updatecatgory_pickimage_button_dialog);
                        updateCategoryImage = dialogView.findViewById(R.id.updatecatagoryImage);
                        final Button updateCategoryDetails = dialogView.findViewById(R.id.updateaddCatagory_dialog);
                        ImageView cancel = dialogView.findViewById(R.id.Cancel);
                        final ProgressBar progressBarUpdate = dialogView.findViewById(R.id.progressUpdate);
                        updateCatagoryName.setText(categoryDetails.getCategoryName());
                        updateMinimumCartValue.setText(String.valueOf(categoryDetails.getMinimumCartValue()));
                        updateDeliveryCharge.setText(String.valueOf(categoryDetails.getDeliveryChargeValue()));
                        updateNormalDistanceDeliveryCharge.setText(String.valueOf(categoryDetails.getNormalDistanceDelivery()));
                        updatelongDistanceDeliveryCharge.setText(String.valueOf(categoryDetails.getLongDistanceDelivery()));


                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.mipmap.ic_launcher);
                        requestOptions.error(R.mipmap.ic_launcher);
                        Glide.with(CategoryActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(categoryDetails.getCategoryImage())
                                .fitCenter().into(updateCategoryImage);

                        final AlertDialog b = dialogBuilder.create();
                        b.show();

                        cancel.setOnClickListener(v -> b.dismiss());

                        updateCategoryPickImageButton.setOnClickListener(v -> {
                            openFileChooser();
                            startActivityForResult(intentImage, 1);
                            mimageuri = null;
                        });

                        updateCategoryDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String nameOfCategory = updateCatagoryName.getText().toString().trim().toUpperCase();

                                if (nameOfCategory == null || "".equals(nameOfCategory)
                                        || android.text.TextUtils.isEmpty(nameOfCategory)) {
                                    updateCatagoryName.setError(Constant.REQUIRED_MSG);
                                    return;
                                } else if (!android.text.TextUtils.isEmpty(nameOfCategory)
                                        && !TextUtils.validateNames_catagoryItems(nameOfCategory)) {
                                    updateCatagoryName.setError(INVALID_CATEGORY_NAME);
                                    return;
                                }

                                Query query = categoryRef.orderByChild(CATEGORY_NAME).equalTo(nameOfCategory);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                                        if (!"".equals(nameOfCategory)) {

                                            updateCategoryDetails.setVisibility(View.INVISIBLE);
                                            updateCatagoryName.setTextIsSelectable(BOOLEAN_FALSE);
                                            updateCatagoryName.setFocusable(BOOLEAN_FALSE);

                                            if (mimageuriUpdate != null) {
                                                StorageReference imageFileStorageRef = category_storage.child(CATEGORY_IMAGE_STORAGE
                                                        + System.currentTimeMillis() + "." + getExtenstion(mimageuriUpdate));
                                                Bitmap bmp = null;
                                                try {
                                                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuriUpdate);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                                                byte[] data = baos.toByteArray();


                                                mItemStorageTask = imageFileStorageRef.putBytes(data).addOnSuccessListener(
                                                        taskSnapshot -> {
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(() -> progressBarUpdate.setProgress(0), 5000);
                                                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                            while (!urlTask.isSuccessful())
                                                                ;

                                                            Uri downloadUrl_update = urlTask.getResult();

                                                            uploadImageAndInsertData(updateCatagoryName,updateMinimumCartValue,updateDeliveryCharge,updateNormalDistanceDeliveryCharge,

                                                                    updatelongDistanceDeliveryCharge,    updateCategoryDetails, b, downloadUrl_update);
                                                        }).addOnFailureListener(e -> Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show()).addOnProgressListener(taskSnapshot -> {
                                                    progressBarUpdate.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                    progressBarUpdate.setProgress((int) progress);
                                                });
                                            } else {
                                                final Uri downloadUrl = null;
                                                mimageuriUpdate = null;

                                                uploadImageAndInsertData(updateCatagoryName,updateMinimumCartValue,updateDeliveryCharge,updateNormalDistanceDeliveryCharge,updatelongDistanceDeliveryCharge, updateCategoryDetails, b, downloadUrl);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            public void uploadImageAndInsertData(final EditText updateCatagory,final EditText updateMinimumCartValue,
                                                                 final EditText deliveryChargeValue,final EditText normalDistanceDeliveryCharge,
                                                                 final EditText longDistanceDeliiveryCharge,
                                                                 final Button updateCategoryDetails,
                                                                 final AlertDialog b, final Uri downloadUrl_update) {

                                final String newCategoryName = updateCatagory.getText().toString().toUpperCase().trim();
                                final String newMinimumCartValue=updateMinimumCartValue.getText().toString();
                                final String newDeliveryChargeValue=deliveryChargeValue.getText().toString();
                                final String normalDistanceChargeValue=normalDistanceDeliveryCharge.getText().toString();
                                final String longDistanceDeliveryCharge=longDistanceDeliiveryCharge.getText().toString();




                                final Query updatequery = categoryRef.orderByChild(CATEGORY_NAME).equalTo(newCategoryName);
                                final String previousCategoryName = categoryDetails.getCategoryName();
                                final String previousImageUri = categoryDetails.getCategoryImage();

                                updatequery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                        if (dataSnapshot1.getChildrenCount() > 0) {

                                            if (!previousCategoryName.equalsIgnoreCase(newCategoryName)) {

                                                updateCategoryDetails.setVisibility(View.VISIBLE);
                                                updateCatagory.setTextIsSelectable(BOOLEAN_TRUE);
                                                updateCatagory.setTextIsSelectable(BOOLEAN_TRUE);
                                                updateCatagory.setFocusable(BOOLEAN_TRUE);

                                                Toast.makeText(CategoryActivity.this, CATAGORY_EXIST, Toast.LENGTH_SHORT).show();

                                            } else if (previousCategoryName.equalsIgnoreCase(newCategoryName)) {

                                                CategoryDetails updateDetails = new CategoryDetails();
                                                updateDetails.setCategoryid(categoryDetails.getCategoryid());
                                                updateDetails.setCategoryName(newCategoryName);
                                                if (!newMinimumCartValue.equals("")) {
                                                    updateDetails.setMinimumCartValue(Integer.parseInt(newMinimumCartValue));
                                                }else
                                                {
                                                    updateDetails.setMinimumCartValue(0);
                                                }

                                                if (!newDeliveryChargeValue.equals("") )
                                                {
                                                    updateDetails.setDeliveryChargeValue(Integer.parseInt(newDeliveryChargeValue));
                                                }else
                                                {
                                                    updateDetails.setDeliveryChargeValue(0);
                                                }

                                                updateDetails.setCategoryPriority(categoryDetails.getCategoryPriority());
                                                updateDetails.setNormalDistanceDelivery(Integer.parseInt(normalDistanceChargeValue));
                                                updateDetails.setLongDistanceDelivery(Integer.parseInt(longDistanceDeliveryCharge));
                                                updateDetails.setSellerList(categoryDetails.getSellerList());
                                                updateDetails.setSubCategoryList(categoryDetails.getSubCategoryList());

                                                if (downloadUrl_update != null) {
                                                    updateDetails.setCategoryImage(downloadUrl_update.toString());
                                                } else {
                                                    updateDetails.setCategoryImage(categoryDetails.getCategoryImage());
                                                }
                                                updateDetails.setCategoryCreatedDate(categoryDetails.getCategoryCreatedDate());

                                                categoryRef.child(String.valueOf(categoryDetails.getCategoryid())).setValue(updateDetails);
                                                Toast.makeText(CategoryActivity.this, "Category Updated Successfully", Toast.LENGTH_LONG).show();

                                                // Code to delete the previous image from Storage
                                                deletePreviousCategoryImage(previousImageUri);

                                                mimageuriUpdate = null;
                                                b.dismiss();
                                            }
                                        } else {
                                            CategoryDetails updateDetails = new CategoryDetails();
                                            updateDetails.setCategoryid(categoryDetails.getCategoryid());
                                            updateDetails.setCategoryName(newCategoryName);
                                            if (!newMinimumCartValue.equals("")) {
                                                updateDetails.setMinimumCartValue(Integer.parseInt(newMinimumCartValue));
                                            }else
                                            {
                                                updateDetails.setMinimumCartValue(0);
                                            }

                                            if (!newDeliveryChargeValue.equals(""))
                                            {
                                                updateDetails.setDeliveryChargeValue(Integer.parseInt(newDeliveryChargeValue));
                                            }else
                                            {
                                                updateDetails.setDeliveryChargeValue(0);
                                            }
                                            updateDetails.setNormalDistanceDelivery(Integer.parseInt(normalDistanceChargeValue));
                                            updateDetails.setLongDistanceDelivery(Integer.parseInt(longDistanceDeliveryCharge));
                                            updateDetails.setCategoryPriority(categoryDetails.getCategoryPriority());
                                            updateDetails.setSellerList(categoryDetails.getSellerList());
                                            updateDetails.setSubCategoryList(categoryDetails.getSubCategoryList());

                                            if (downloadUrl_update != null) {
                                                updateDetails.setCategoryImage(downloadUrl_update.toString());
                                            } else {
                                                updateDetails.setCategoryImage(categoryDetails.getCategoryImage());
                                            }
                                            updateDetails.setCategoryCreatedDate(categoryDetails.getCategoryCreatedDate());
                                            categoryRef.child(String.valueOf(categoryDetails.getCategoryid())).setValue(updateDetails);
                                            Toast.makeText(CategoryActivity.this, "Category Updated Successfully", Toast.LENGTH_LONG).show();

                                            // Code to delete the previous image from the storage
                                            deletePreviousCategoryImage(previousImageUri);

                                            mimageuriUpdate = null;
                                            b.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    });
                }
            }

            public void deletePreviousCategoryImage(String previousImageUri) {
               /* category_storage.getFile(Uri.parse(previousImageUri));
                category_storage.child(CATEGORY_IMAGE_STORAGE).getStorage()
                        .getReferenceFromUrl(previousImageUri).delete().addOnSuccessListener(aVoid -> {
                    // File deleted successfully

                }).addOnFailureListener(exception -> {
                    // Uh-oh, an error occurred!

                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add_Categories.setOnClickListener(v -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_catagory_dialog, null);
            dialogBuilder.setView(dialogView);

            //get the spinner from the xml.
            final Spinner dropdown = dialogView.findViewById(R.id.categoryPrioritySpinner);
            //create a list of items for the spinner.
            final String[] category_priority_drop_down = {"Select Category Priority", "1", "2", "3"};
            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_spinner_dropdown_item, category_priority_drop_down);
            //set the spinners adapter to the previously created one.
            dropdown.setAdapter(adapter);

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    if (position == 0) {
                        isCategoryPriorityValuePresent = BOOLEAN_FALSE;
                    } else {
                        isCategoryPriorityValuePresent = BOOLEAN_TRUE;
                        categoryPrioritySelected = (String) parentView.getItemAtPosition(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            categoryName_edittext = dialogView.findViewById(R.id.catagoryname_dialog);
            categoryImage_imageview = dialogView.findViewById(R.id.catagoryImage);
            minimumCartValue_Edittext = dialogView.findViewById(R.id.minimumcartvalue_dialog);
            deliveryCharge_Edittext = dialogView.findViewById(R.id.deliverycharge_dialog);
            normalDistanceDelivery=dialogView.findViewById(R.id.addminimumdistancedelivery);
            longDistanceDelivery=dialogView.findViewById(R.id.addlongdistancedelivery);

            Button PickImageCatagory_button = dialogView.findViewById(R.id.catgory_image_button_dialog);
            final Button addCatagory_button = dialogView.findViewById(R.id.addCatagory_dialog);
            ImageView cancel = dialogView.findViewById(R.id.Cancel);
            final ProgressBar progressBar = dialogView.findViewById(R.id.progressbar);

            final AlertDialog b = dialogBuilder.create();
            b.show();

            cancel.setOnClickListener(v13 -> b.dismiss());

            PickImageCatagory_button.setOnClickListener(v12 -> {
                openFileChooser();
                startActivityForResult(intentImage, 0);
            });

            addCatagory_button.setOnClickListener(v1 -> {
                catagoryName = categoryName_edittext.getText().toString().toUpperCase();

                if (catagoryName == null || "".equals(catagoryName) || android.text.TextUtils.isEmpty(catagoryName)) {
                    categoryName_edittext.setError(Constant.REQUIRED_MSG);
                    return;
                } else if (!android.text.TextUtils.isEmpty(catagoryName) && !TextUtils.validateNames_catagoryItems(catagoryName)) {
                    categoryName_edittext.setError(INVALID_CATAGORY_NAME);
                    return;
                } else if(normalDistanceDelivery.getText().toString().equals(""))
                {
                    normalDistanceDelivery.setError(REQUIRED_MSG);
                }else if(longDistanceDelivery.getText().toString().equals(""))
                {
                    longDistanceDelivery.setError(REQUIRED_MSG);
                }else {
                    addCatagory_button.setVisibility(View.INVISIBLE);

                    if (mimageuri != null) {

                        StorageReference imageFileStorageRef = category_storage.child(CATEGORY_IMAGE_STORAGE
                                + System.currentTimeMillis() + "." + getExtenstion(mimageuri));
                        Bitmap bmp = null;
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);

                        byte[] data = baos.toByteArray();
                        mItemStorageTask = imageFileStorageRef.putBytes(data).addOnSuccessListener(
                                taskSnapshot -> {
                                    Handler handler = new Handler();
                                    handler.postDelayed(() -> progressBar.setProgress(0), 5000);

                                    //  @Override
                                    //  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    final Uri downloadUrl = urlTask.getResult();

                                    Toast.makeText(CategoryActivity.this, "catagoryName::" + catagoryName, Toast.LENGTH_LONG).show();

                                    Query query = categoryRef.orderByChild(CATEGORY_NAME).equalTo(catagoryName);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.getChildrenCount() > 0) {
                                                addCatagory_button.setVisibility(View.VISIBLE);
                                                categoryName_edittext.setText("");
                                                Toast.makeText(CategoryActivity.this, CATAGORY_EXIST, Toast.LENGTH_LONG).show();
                                            } else {

                                                addCatagory_button.setVisibility(View.INVISIBLE);
                                                CategoryDetails categoryDetails = new CategoryDetails();

                                                categoryDetails.setCategoryid(String.valueOf(maxid + 1));
                                                categoryDetails.setCategoryName(catagoryName);
                                                categoryDetails.setCategoryImage(downloadUrl.toString());
                                                categoryDetails.setCategoryPriority(categoryPrioritySelected);
                                                if (!minimumCartValue_Edittext.getText().toString().equals("")) {
                                                    categoryDetails.setMinimumCartValue(Integer.parseInt(minimumCartValue_Edittext.getText().toString()));
                                                } else
                                                {
                                                    categoryDetails.setMinimumCartValue(0);
                                                }

                                                if (!deliveryCharge_Edittext.getText().toString().equals("")) {
                                                    categoryDetails.setDeliveryChargeValue(Integer.parseInt(deliveryCharge_Edittext.getText().toString()));
                                                } else
                                                {
                                                    categoryDetails.setDeliveryChargeValue(0);
                                                }
                                                categoryDetails.setCategoryCreatedDate(DateUtils.fetchCurrentDateAndTime());
                                                categoryDetails.setNormalDistanceDelivery(Integer.parseInt(normalDistanceDelivery.getText().toString()));
                                                categoryDetails.setLongDistanceDelivery(Integer.parseInt(longDistanceDelivery.getText().toString()));


                                                categoryRef.child(String.valueOf(maxid + 1)).setValue(categoryDetails);

                                                Toast.makeText(CategoryActivity.this, "Category Added Successfully", Toast.LENGTH_LONG).show();
                                                categoryName_edittext.setFocusable(BOOLEAN_FALSE);
                                                categoryName_edittext.setTextIsSelectable(BOOLEAN_TRUE);
                                                normalDistanceDelivery.setEnabled(false);
                                                longDistanceDelivery.setEnabled(false);
                                                addCatagory_button.setVisibility(View.INVISIBLE);
                                                b.dismiss();

                                                mimageuri = null;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }).addOnFailureListener(e -> Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show()).addOnProgressListener(taskSnapshot -> {
                            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        });
                    } else {
                        mimageuri = null;
                        Toast.makeText(CategoryActivity.this, PLEASE_SELECT_IMAGE, Toast.LENGTH_LONG).show();
                        addCatagory_button.setVisibility(View.VISIBLE);
                    }
                }
            });
        });
    }


    private void openFileChooser() {

        intentImage = new Intent();
        intentImage.setType("image/*");
        intentImage.setAction(Intent.ACTION_GET_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                PICK_IMAGE_REQUEST = 0;
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mimageuri = data.getData();
                    //Picasso.get().load(mimageuri).into(catagoryImage_imageview);
                    Glide.with(CategoryActivity.this).load(mimageuri).into(categoryImage_imageview);
                }
                break;

            case 1:
                PICK_IMAGE_REQUEST = 1;
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mimageuriUpdate = data.getData();
                    Glide.with(CategoryActivity.this).load(mimageuriUpdate).into(updateCategoryImage);
                }
                break;
        }
        //  }
    }


    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                maxid = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( CategoryActivity.this );
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
                navigationView.setCheckedItem(R.id.category);
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
        }else if (id == R.id.viewadvertisement) {
            Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
    public void onBackPressed() {

        Intent intent = new Intent(CategoryActivity.this, DashBoardActivity.class);
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CategoryActivity.this);
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
