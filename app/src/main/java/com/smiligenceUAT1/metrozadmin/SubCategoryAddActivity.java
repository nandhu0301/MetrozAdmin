package com.smiligenceUAT1.metrozadmin;

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
import com.smiligenceUAT1.metrozadmin.adapter.ExpandableListAdapter;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetails;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.smiligenceUAT1.metrozadmin.common.Constant.*;


public class SubCategoryAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton add_subCategories;
    DatabaseReference categoryRef, userDetailsDataRef, itemDataRef, subcategoryDataRef;
    StorageReference sub_category_storage;
    private Uri mimageuri, mimageuriUpdate;
    private StorageTask mItemStorageTask;
    long maxid = 0;
    ImageView categoryImage_imageview;
    EditText subcategoryName_edittext;
    ImageView updateCategoryImage;
    private ArrayList<CategoryDetails> catagoryList = new ArrayList<> ();
    private ArrayList<CategoryDetails> subCategoryArrayList = new ArrayList<> ();
    private BigDecimal value;
    private NumberFormat nbFmt = NumberFormat.getInstance ();
    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecyclerView;
    Intent intentImage = new Intent ();
    private static int PICK_IMAGE_REQUEST;
    TextView textViewUsername, textStorename;
    public static String saved_username, saved_password;
    String subcatagoryNameString;
    String categoryName;
    int categoryposition;
    Boolean isCategoryPriorityValuePresent;
    ArrayList<String> categoryNameList = new ArrayList<> ();
    List<CategoryDetails> subCategoryList = new ArrayList<> ();
    long subCategorycount = 0;
    int subCategoyIndicator = 1;
    CategoryDetails categoryDetails;
    View mHeaderView;
    NavigationView navigationView;
    String userName;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sub_category_add );

        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        ActionBar actionBar = getSupportActionBar ();
        toolbar.setTitle ( TITLE_SUBCATEGORY );

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener ( toggle );
        toggle.syncState ();
        toggle.getDrawerArrowDrawable ().setColor ( getResources ().getColor ( R.color.White ) );
        navigationView = findViewById ( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener ( SubCategoryAddActivity.this );
        navigationView.setCheckedItem ( R.id.subcategory );

disableAutofill();
        UserRoleActivity.menuNav = navigationView.getMenu ();
        mHeaderView = navigationView.getHeaderView ( 0 );

        navigationView.setCheckedItem ( R.id.subcategory );

        textViewUsername = mHeaderView.findViewById ( R.id.name );
        textStorename = mHeaderView.findViewById ( R.id.roleName );


        userDetailsDataRef = CommonMethods.fetchFirebaseDatabaseReference ( USER_DETAILS_TABLE );
        if(!"".equals(UserRoleActivity.saved_userName)&& !"".equals(UserRoleActivity.saved_userName)) {

            userName=UserRoleActivity.saved_userName;
        }  if (!"".equals(DashBoardActivity.saved_userName)&& !"".equals(DashBoardActivity.saved_userName)){
            userName=DashBoardActivity.saved_userName;
        }

        final Query roleNameQuery = userDetailsDataRef.orderByChild ( PHONE_NUM_COLUMN ).equalTo ( String.valueOf ( userName ) );

        roleNameQuery.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot snap : dataSnapshot.getChildren () ) {
                    UserDetails loginUserDetailslist = snap.getValue ( UserDetails.class );
                    textViewUsername.setText ( loginUserDetailslist.getFirstName ().toUpperCase () );
                    textStorename.setText ( loginUserDetailslist.getRoleName ().toUpperCase () );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        categoryRef = CommonMethods.fetchFirebaseDatabaseReference ( CATEGORY_DETAILS_TABLE );
        subcategoryDataRef = CommonMethods.fetchFirebaseDatabaseReference ( SubCATEGORY_DETAILS );
        sub_category_storage = CommonMethods.fetchFirebaseStorageReference ( SUBCATEGORY_IMAGE_STORAGE );

        add_subCategories = findViewById ( R.id.add_subCatagories );
        categoryRecyclerView = findViewById ( R.id.subcatagoryGrid );

        categoryRecyclerView.setLayoutManager ( new GridLayoutManager ( SubCategoryAddActivity.this, DEFAULT_ROW_COUNT ) );
        categoryRecyclerView.setHasFixedSize ( true );

        categoryRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                catagoryList.clear ();
                categoryNameList.clear ();
                subCategoryArrayList.clear ();
                categoryNameList.add ( "Select Category" );

                for ( DataSnapshot postSnapshot : dataSnapshot.getChildren () ) {
                    categoryDetails = postSnapshot.getValue ( CategoryDetails.class );
                    catagoryList.add ( categoryDetails );
                    categoryNameList.add ( categoryDetails.getCategoryName () );

                    subCategoryArrayList.addAll ( categoryDetails.getSubCategoryList () );
                }

                if (subCategoryArrayList.size () > 0) {
                    subCategoryArrayList.removeAll ( Collections.singleton ( null ) );
                    categoryAdapter = new CategoryAdapter ( SubCategoryAddActivity.this, subCategoryArrayList, subCategoyIndicator );
                    categoryAdapter.notifyDataSetChanged ();
                    categoryRecyclerView.setAdapter ( categoryAdapter );
                }

                if (categoryAdapter != null) {
                    categoryAdapter.setOnItemclickListener (Position -> {

                        final CategoryDetails subcategoryDetails = subCategoryArrayList.get ( Position );


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder ( SubCategoryAddActivity.this );
                        LayoutInflater inflater = getLayoutInflater ();
                        final View dialogView = inflater.inflate ( R.layout.update_sub_category, null );
                        dialogBuilder.setView ( dialogView );

                        final EditText updatesubcategoryname = dialogView.findViewById ( R.id.updatesubcategoryname );
                        final Button imagePick = dialogView.findViewById ( R.id.updateimagepick );
                        updateCategoryImage = dialogView.findViewById ( R.id.image_subcategory );
                        final Button updateCategoryDetails = dialogView.findViewById ( R.id.updatesubcategory );
                        ImageView cancel = dialogView.findViewById ( R.id.Cancel_subcategory );
                        final ProgressBar progressBarUpdate = dialogView.findViewById ( R.id.progressUpdatesubcategory );

                        updatesubcategoryname.setText ( subcategoryDetails.getSubCategoryName () );


                        RequestOptions requestOptions = new RequestOptions ();
                        requestOptions.placeholder ( R.mipmap.ic_launcher );
                        requestOptions.error ( R.mipmap.ic_launcher );
                        Glide.with ( SubCategoryAddActivity.this )
                                .setDefaultRequestOptions ( requestOptions )
                                .load ( subcategoryDetails.getSubCategoryImage () )
                                .fitCenter ().into ( updateCategoryImage );

                        final AlertDialog b = dialogBuilder.create ();
                        b.show ();

                        cancel.setOnClickListener (v -> b.dismiss());
                        imagePick.setOnClickListener (v -> {
                            openFileChooser();
                            startActivityForResult(intentImage, 1);
                            //   mimageuri = null;
                        });


                        updateCategoryDetails.setOnClickListener ( new View.OnClickListener () {
                            @Override
                            public void onClick(View v) {

                                final String nameOfCategory = updatesubcategoryname.getText ().toString ().trim ().toUpperCase ();

                                if (nameOfCategory == null || "".equals ( nameOfCategory )
                                        || android.text.TextUtils.isEmpty ( nameOfCategory )) {
                                    updatesubcategoryname.setError ( REQUIRED_MSG );
                                    return;
                                } else if (!android.text.TextUtils.isEmpty ( nameOfCategory )
                                        && !TextUtils.validateNames_catagoryItems ( nameOfCategory )) {
                                    updatesubcategoryname.setError ( INVALID_CATEGORY_NAME );
                                    return;
                                }

                                Query query = categoryRef.orderByChild ( "categoryid" ).equalTo ( subcategoryDetails.getCategoryid () );

                                query.addListenerForSingleValueEvent ( new ValueEventListener () {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                                        if (!"".equals ( nameOfCategory )) {

                                            updateCategoryDetails.setVisibility ( View.INVISIBLE );
                                            updatesubcategoryname.setTextIsSelectable ( BOOLEAN_FALSE );
                                            updatesubcategoryname.setFocusable ( BOOLEAN_FALSE );

                                            if (mimageuriUpdate != null) {
                                                StorageReference imageFileStorageRef = sub_category_storage.child ( "sub-Categories/"
                                                        + System.currentTimeMillis () + "." + getExtenstion ( mimageuriUpdate ) );

                                                Bitmap bmp = null;
                                                try {
                                                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuriUpdate);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                                                byte[] data = baos.toByteArray();

                                                mItemStorageTask = imageFileStorageRef.putBytes ( data ).addOnSuccessListener (
                                                        taskSnapshot -> {
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(() -> progressBarUpdate.setProgress(0), 5000);
                                                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                            while (!urlTask.isSuccessful())
                                                                ;

                                                            Uri downloadUrl_update = urlTask.getResult();

                                                            uploadImageAndInsertData(updatesubcategoryname,
                                                                    updateCategoryDetails, b, downloadUrl_update);
                                                        }).addOnFailureListener (e -> Toast.makeText(SubCategoryAddActivity.this, e.getMessage(), Toast.LENGTH_LONG).show()).addOnProgressListener (taskSnapshot -> {
                                                    progressBarUpdate.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                    progressBarUpdate.setProgress((int) progress);
                                                });
                                            } else {
                                                final Uri downloadUrl = null;
                                                //  mimageuriUpdate = null;

                                                uploadImageAndInsertData ( updatesubcategoryname, updateCategoryDetails, b, downloadUrl );
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }

                            public void uploadImageAndInsertData(final EditText updateCatagory,
                                                                 final Button updateCategoryDetails,
                                                                 final AlertDialog b, final Uri downloadUrl_update) {

                                final String newCategoryName = updateCatagory.getText ().toString ().toUpperCase ().trim ();

                                //final Query updatequery = categoryRef.orderByChild("subCategoryName").equalTo(newCategoryName);
                                final Query updatequery = categoryRef.orderByChild ( "categoryid" ).equalTo ( subcategoryDetails.getCategoryid () );

                                final String previousCategoryName = subcategoryDetails.getSubCategoryName ();
                                final String previousImageUri = subcategoryDetails.getSubCategoryImage ();


                                updatequery.addListenerForSingleValueEvent ( new ValueEventListener () {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                        if (dataSnapshot1.getChildrenCount () > 0) {

                                            if (!previousCategoryName.equalsIgnoreCase ( newCategoryName )) {

                                                updateCategoryDetails.setVisibility ( View.VISIBLE );
                                                updateCatagory.setTextIsSelectable ( BOOLEAN_TRUE );
                                                updateCatagory.setTextIsSelectable ( BOOLEAN_TRUE );
                                                updateCatagory.setFocusable ( BOOLEAN_TRUE );

                                                Toast.makeText ( SubCategoryAddActivity.this, CATAGORY_EXIST, Toast.LENGTH_SHORT ).show ();

                                            } else if (previousCategoryName.equalsIgnoreCase ( newCategoryName )) {

                                                CategoryDetails updateDetails = new CategoryDetails ();
                                                updateDetails.setSubCategoryId ( subcategoryDetails.getSubCategoryId () );
                                                updateDetails.setSubCategoryName ( newCategoryName );
                                                updateDetails.setCategoryName ( subcategoryDetails.getCategoryName () );
                                                updateDetails.setCategoryid ( subcategoryDetails.getCategoryid () );
                                                updateDetails.setSubCategoryId ( subcategoryDetails.getSubCategoryId () );
                                                if (downloadUrl_update != null) {
                                                    updateDetails.setCategoryImage ( downloadUrl_update.toString () );
                                                } else {
                                                    updateDetails.setSubCategoryImage ( subcategoryDetails.getSubCategoryImage () );
                                                }
                                                updateDetails.setSubCategoryCreatedDate ( subcategoryDetails.getCategoryCreatedDate () );

                                                categoryRef.child ( subcategoryDetails.getCategoryid () ).child ( "subCategoryList" )
                                                        .child ( String.valueOf ( subcategoryDetails.getSubCategoryId () ) ).setValue ( updateDetails );
                                                Toast.makeText ( SubCategoryAddActivity.this, "Category Updated Successfully",
                                                        Toast.LENGTH_LONG ).show ();

                                                // Code to delete the previous image from Storage
                                                deletePreviousCategoryImage ( previousImageUri );

                                                // mimageuriUpdate = null;
                                                b.dismiss ();
                                            }
                                        } else {

                                            CategoryDetails updateDetails = new CategoryDetails ();
                                            updateDetails.setSubCategoryId ( subcategoryDetails.getSubCategoryId () );
                                            updateDetails.setSubCategoryName ( newCategoryName );
                                            updateDetails.setCategoryName ( subcategoryDetails.getCategoryName () );
                                            updateDetails.setCategoryid ( subcategoryDetails.getCategoryid () );

                                            if (downloadUrl_update != null) {
                                                updateDetails.setSubCategoryImage ( downloadUrl_update.toString () );
                                            } else {
                                                updateDetails.setSubCategoryImage ( subcategoryDetails.getSubCategoryImage () );
                                            }
                                            updateDetails.setCategoryCreatedDate ( subcategoryDetails.getCategoryCreatedDate () );
                                            categoryRef.child ( subcategoryDetails.getCategoryid () ).child ( "subCategoryList" ).child ( String.valueOf ( subcategoryDetails.getSubCategoryId () ) ).setValue ( updateDetails );
                                            Toast.makeText ( SubCategoryAddActivity.this, "Category Updated Successfully", Toast.LENGTH_LONG ).show ();

                                            // Code to delete the previous image from the storage
                                            deletePreviousCategoryImage ( previousImageUri );

                                            //   mimageuriUpdate = null;
                                            b.dismiss ();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        } );


                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        add_subCategories.setOnClickListener (v -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder ( SubCategoryAddActivity.this );
            LayoutInflater inflater = getLayoutInflater ();
            final View dialogView = inflater.inflate ( R.layout.add_sub_category_dialog, null );
            dialogBuilder.setView ( dialogView );

            final Spinner dropdown = dialogView.findViewById ( R.id.subcategoryPrioritySpinner );
            final EditText subCategoryNameeditText = dialogView.findViewById ( R.id.subcatagoryname_dialog );
            Button pickImage = dialogView.findViewById ( R.id.subcatgory_image_button_dialog );
            categoryImage_imageview = dialogView.findViewById ( R.id.subcatagoryImage );
            final Button uploadsubCategory = dialogView.findViewById ( R.id.addsubCatagory_dialog );
            final ProgressBar progressBar = dialogView.findViewById ( R.id.progressbar );
            ImageView cancel = dialogView.findViewById ( R.id.Cancel );

            final AlertDialog b = dialogBuilder.create ();
            b.show ();
            cancel.setOnClickListener (v13 -> b.dismiss());

            pickImage.setOnClickListener (v12 -> {
                openFileChooser();
                startActivityForResult(intentImage, 0);
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<> ( SubCategoryAddActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, categoryNameList );

            dropdown.setAdapter ( adapter );

            dropdown.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    if (position == 0) {
                        isCategoryPriorityValuePresent = BOOLEAN_FALSE;
                    } else {

                        isCategoryPriorityValuePresent = BOOLEAN_TRUE;
                        categoryName = (String) parentView.getItemAtPosition ( position );
                        categoryposition = position;

                        Query subCategoryRef = categoryRef.child ( String.valueOf ( categoryposition ) ).child ( "subCategoryList" );

                        subCategoryRef.addValueEventListener ( new ValueEventListener () {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount () <= 0) {
                                    subCategorycount = 0;
                                } else if (dataSnapshot.getChildrenCount () > 0) {
                                    subCategorycount = dataSnapshot.getChildrenCount ();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            } );

            uploadsubCategory.setOnClickListener (v1 -> {
                subcatagoryNameString = subCategoryNameeditText.getText().toString().toUpperCase();

                if (subCategoryNameeditText == null || "".equals(subcatagoryNameString) || android.text.TextUtils.isEmpty(subcatagoryNameString)) {
                    subCategoryNameeditText.setError(REQUIRED_MSG);
                    return;
                } else if (!android.text.TextUtils.isEmpty(subcatagoryNameString) && !TextUtils.validateNames_catagoryItems(subcatagoryNameString)) {
                    subCategoryNameeditText.setError(INVALID_CATAGORY_NAME);
                    return;
                } else {

                    uploadsubCategory.setVisibility(View.INVISIBLE);

                    if (mimageuri != null) {

                        StorageReference imageFileStorageRef = sub_category_storage.child(SUBCATEGORY_IMAGE_STORAGE
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

                                    Query query = categoryRef.child(String.valueOf(categoryposition)).child("subCategoryList")
                                            .orderByChild(SUBCATEGORY_NAME).equalTo(subcatagoryNameString);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.getChildrenCount() > 0) {
                                                uploadsubCategory.setVisibility(View.VISIBLE);
                                                //subcategoryName_edittext.setText("");
                                                Toast.makeText(SubCategoryAddActivity.this, "Sub category exists", Toast.LENGTH_LONG).show();
                                            } else {

                                                uploadsubCategory.setVisibility(View.INVISIBLE);

                                                CategoryDetails subcategoryDeatils = new CategoryDetails();

                                                subcategoryDeatils.setSubCategoryId(String.valueOf(subCategorycount + 1));
                                                subcategoryDeatils.setSubCategoryName(subcatagoryNameString);
                                                subcategoryDeatils.setSubCategoryImage(downloadUrl.toString());

                                                subcategoryDeatils.setCategoryName(categoryName);
                                                subcategoryDeatils.setCategoryid(String.valueOf(categoryposition));
                                                subcategoryDeatils.setCategoryCreatedDate(DateUtils.fetchCurrentDateAndTime());
                                                subCategoryList.add(subcategoryDeatils);

                                                categoryRef.child(String.valueOf(categoryposition)).child("subCategoryList").
                                                        child(String.valueOf(subCategorycount + 1)).setValue(subcategoryDeatils);
                                                Toast.makeText(SubCategoryAddActivity.this, "" +
                                                        "Sub category Added Successfully", Toast.LENGTH_LONG).show();
                                                subCategoryNameeditText.setFocusable(BOOLEAN_FALSE);
                                                subCategoryNameeditText.setTextIsSelectable(BOOLEAN_TRUE);

                                                uploadsubCategory.setVisibility(View.INVISIBLE);
                                                b.dismiss();

                                                mimageuri = null;
                                                subCategoryList.clear();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }).addOnFailureListener(e -> Toast.makeText(SubCategoryAddActivity.this, e.getMessage(), Toast.LENGTH_LONG).show()).addOnProgressListener(taskSnapshot -> {
                            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        });
                    } else {
                        mimageuri = null;
                        Toast.makeText(SubCategoryAddActivity.this, PLEASE_SELECT_IMAGE, Toast.LENGTH_LONG).show();
                        uploadsubCategory.setVisibility(View.VISIBLE);
                    }
                }
            });
        });
    }

    public void deletePreviousCategoryImage(String previousImageUri) {
        sub_category_storage.getFile ( Uri.parse ( previousImageUri ) );
        sub_category_storage.child ( SUBCATEGORY_IMAGE_STORAGE ).getStorage ()
                .getReferenceFromUrl ( previousImageUri ).delete ().addOnSuccessListener (aVoid -> {
                    // File deleted successfully

                }).addOnFailureListener (exception -> {
                    // Uh-oh, an error occurred!

                });
    }

    private void openFileChooser() {

        intentImage = new Intent ();
        intentImage.setType ( "image/*" );
        intentImage.setAction ( Intent.ACTION_GET_CONTENT );
        // startActivityForResult(intentImage, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        switch (requestCode) {



            case 0:
                PICK_IMAGE_REQUEST = 0;
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData () != null) {
                    mimageuri = data.getData ();
                    //Picasso.get().load(mimageuri).into(catagoryImage_imageview);
                    Glide.with ( SubCategoryAddActivity.this ).load ( mimageuri ).into ( categoryImage_imageview );
                }
                break;

            case 1:
                PICK_IMAGE_REQUEST = 1;
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData () != null) {
                    //requestCode == PICK_IMAGE_REQUEST;
                    mimageuriUpdate = data.getData ();
                    // Picasso.get().load(mimageuriUpdate).into(updateCategoryImage);
                    Glide.with ( SubCategoryAddActivity.this ).load ( mimageuriUpdate ).into ( updateCategoryImage );
                }
                break;
        }
    }


    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver ();
        MimeTypeMap mime = MimeTypeMap.getSingleton ();
        return mime.getExtensionFromMimeType ( contentResolver.getType ( uri ) );
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog ( SubCategoryAddActivity.this );
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
                navigationView.setCheckedItem(R.id.subcategory);
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
    protected void onStart() {
        super.onStart ();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent ( SubCategoryAddActivity.this, DashBoardActivity.class );
        intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( intent );
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SubCategoryAddActivity.this);
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
