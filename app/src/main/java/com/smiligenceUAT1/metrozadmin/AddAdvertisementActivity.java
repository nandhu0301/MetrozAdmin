package com.smiligenceUAT1.metrozadmin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.smiligenceUAT1.metrozadmin.bean.AdvertisementDetails;
import com.smiligenceUAT1.metrozadmin.bean.CategoryDetailsNew;
import com.smiligenceUAT1.metrozadmin.bean.ItemDetails;
import com.smiligenceUAT1.metrozadmin.bean.MenuModel;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;
import com.smiligenceUAT1.metrozadmin.common.DateUtils;
import com.smiligenceUAT1.metrozadmin.common.MultiSelectionSpinnerforStore;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADD_ADVERTISEMNETS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ADVERTISEMT_DETAILS_FIREBASE_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_DELIVERY_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_ITEMS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.APPROVE_STORE_PARTNER;
import static com.smiligenceUAT1.metrozadmin.common.Constant.ASSIGN_ORDERS_FOR_DELIVERY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.BULK_UPLOAD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.CATEGORY_DETAILS_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_BOY_HISTORY;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DELIVERY_DETAILS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.DETAILS_INSERTED;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_DELIVERY_FARE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_OFFERS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.MAINTAIN_TIPS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PRODUCT_DETAILS_TABLE;
import static com.smiligenceUAT1.metrozadmin.common.Constant.SELLER_DETAILS_TABLE;
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

public class AddAdvertisementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference advertisementDataRef, logindataRef;
    StorageReference advertisementStorageRef;
    EditText addAdvertisementName;
    Button pickImage, uploadData;
    ImageView imageView;
    final static int PICK_IMAGE_REQUEST = 100;
    Uri mimageuri;
    AlertDialog alertDialog;
    Drawable drawable;
    StorageTask mUploadTask;
    AdvertisementDetails advertisementDetails = new AdvertisementDetails();
    long addMaxId = 0;
    View mHeaderView;
    public static TextView textViewUsername;
    public static TextView textViewEmail;
    ImageView advertisementImage;
    String selectedStoreString;
    String selectedBrandString, selectedCategoryString;
    String adv_TypeSelectedString;
    String adv_PricingSelectedString, advertisementSelectedString;
    int adv_Priority, adv_SelectedDuration;
    String timecal;
    String hourcal;
    Button setTimeHour, setTimeoneWeekButton, setTime24hoursButton;
    String advertisementScheduleDate, advertisementScheduledTime;
    static int year, month, day;
    EditText  advertisingAmount;
    RadioGroup ad_TypeRadioGroup, ad_MediaRadioGroup;
    String adv_TypeString, adv_MediaSelectedString, adv_DurationSelectedString;
    String dateDayCal, monthCal;
    Spinner durationspinner;
    DatabaseReference sellerDetailRef, productDetailRef, categoryRef;
    SearchableSpinner adv_Pricing_spinner;
    //Spinner brandSpinner;
    MultiSelectionSpinnerforStore storespinner;
    ArrayList<UserDetails> storeNameList = new ArrayList<>();
    ArrayList<String> brandList = new ArrayList<>();
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayAdapter<String> storeListAdapter;
    ArrayAdapter<String> brandAdapter;
    ArrayAdapter<String> categoryAdapter;
    Button pickMedia, uploadAdvertisement;
    LinearLayout mediaImage, mediaGIF, mediaVideo;
    EditText brandText;
    //Spinner categorySpinner;
    Thread thread;
    ArrayList<String> storeNamearray = new ArrayList<String>();
    ArrayAdapter<CharSequence> adv_type_spinner_adapter, adv_pricing_spinner_adapter, adv_duration_spinner;
    String advertismentStartDurtionstring, advertisementExpiringDuration, adv_StartingDurationDate, adv_EndingDurationDate;
    UserDetails userDetails;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    String startTime, endTime;
    TextView scheduleHourText, scheduleDayText, scheduleWeekText;

    Button setDate, setTime, setDateoneweek;
    TextView dateText, hourText, datetextoneWeek;
    AdvertisementDetails advertisementDetailsStatus;
    private DatePickerDialog.OnDateSetListener purchaseDateSetListener;

    static String oneDayAfterScheduledDate, scheduledsevendays;
    private MenuItem item;
    DatabaseReference userDetailsDataRef;
    String userName;
    NavigationView navigationView;
    String payingStatus="";
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advertisement);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        androidx.appcompat.widget.Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle(Constant.ADD_ADVERTISEMNETS);
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
        navigationView.setNavigationItemSelectedListener(AddAdvertisementActivity.this);
        UserRoleActivity.menuNav = navigationView.getMenu();
        mHeaderView = navigationView.getHeaderView(0);
        navigationView.setCheckedItem(R.id.Add_Advertisment_id);


        textViewUsername = mHeaderView.findViewById(R.id.name);
        textViewEmail = mHeaderView.findViewById(R.id.roleName);
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


        adv_Pricing_spinner = findViewById(R.id.ad_pricing_spinner);
        ad_TypeRadioGroup = findViewById(R.id.ad_typeradioGroup);
        storespinner = findViewById(R.id.storespinner);
       // brandSpinner = findViewById(R.id.brandspinner);
        ad_MediaRadioGroup = findViewById(R.id.admedia);
        setDate = findViewById(R.id.setdate);
        setTime = findViewById(R.id.settime);
        dateText = findViewById(R.id.setdatetext);
        hourText = findViewById(R.id.hourText);
        advertisingAmount = findViewById(R.id.amountForAdvertisements);
        pickMedia = findViewById(R.id.pickMedia);
        advertisementImage = findViewById(R.id.imageView);
        uploadAdvertisement = findViewById(R.id.uploadadvertisement);
        //brands_storesEditText = findViewzById(R.id.invisbletext);
        brandText = findViewById(R.id.invisbletext1);
        durationspinner = findViewById(R.id.durationspinner);

        //categorySpinner = findViewById(R.id.categorySpinner);


        mediaImage = findViewById(R.id.imagelayout);
        mediaGIF = findViewById(R.id.mediagiflayout);
        mediaVideo = findViewById(R.id.mediavideolayout);

        mediaImage.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);


        sellerDetailRef = CommonMethods.fetchFirebaseDatabaseReference(SELLER_DETAILS_TABLE);
        productDetailRef = CommonMethods.fetchFirebaseDatabaseReference(PRODUCT_DETAILS_TABLE);
        advertisementDataRef = CommonMethods.fetchFirebaseDatabaseReference(ADVERTISEMT_DETAILS_FIREBASE_TABLE);
        advertisementStorageRef = CommonMethods.fetchFirebaseStorageReference(ADVERTISEMT_DETAILS_FIREBASE_TABLE);
        categoryRef = CommonMethods.fetchFirebaseDatabaseReference(CATEGORY_DETAILS_TABLE);


        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                categoryList.add("select Category");
                for (DataSnapshot categorysnap : dataSnapshot.getChildren()) {
                    CategoryDetailsNew categoryDetails = categorysnap.getValue(CategoryDetailsNew.class);
                    categoryList.add(categoryDetails.getCategoryName());


                }

                if (categoryList != null) {
                    categoryAdapter = new ArrayAdapter<>
                            (AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, categoryList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   // categorySpinner.setAdapter(categoryAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adv_type_spinner_adapter = ArrayAdapter
                .createFromResource(AddAdvertisementActivity.this, R.array.advertismentype_spinner,
                        R.layout.support_simple_spinner_dropdown_item);
        adv_type_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        adv_duration_spinner = ArrayAdapter
                .createFromResource(AddAdvertisementActivity.this, R.array.advertisement_duration,
                        R.layout.support_simple_spinner_dropdown_item);
        adv_duration_spinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        durationspinner.setAdapter(adv_duration_spinner);


        adv_pricing_spinner_adapter = ArrayAdapter
                .createFromResource(AddAdvertisementActivity.this, R.array.advertisement_price,
                        R.layout.support_simple_spinner_dropdown_item);
        adv_pricing_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adv_Pricing_spinner.setAdapter(adv_pricing_spinner_adapter);
        adv_Pricing_spinner.setTitle("Select Advertisement Placing");

        storespinner.setVisibility(View.INVISIBLE);
        //brandSpinner.setVisibility(View.INVISIBLE);
        //categorySpinner.setVisibility(View.INVISIBLE);


        productDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {

                    brandList.clear();
                    brandList.add("Select Brands");


                    for (DataSnapshot productSnap : dataSnapshot.getChildren()) {


                        ItemDetails itemDetails = productSnap.getValue(ItemDetails.class);
                        if (!itemDetails.getItemBrand().equalsIgnoreCase("No Brand")) {
                            brandList.add(itemDetails.getItemBrand());
                        }


                    }

                    TextUtils.removeDuplicatesList(brandList);
                    if (brandList != null) {
                        brandAdapter = new ArrayAdapter<>
                                (AddAdvertisementActivity.this, android.R.layout.simple_spinner_item, brandList);
                        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       // brandSpinner.setAdapter(brandAdapter);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sellerDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    storeNameList.clear();

                    for (DataSnapshot storeNameSnap : dataSnapshot.getChildren()) {
                        userDetails = storeNameSnap.getValue(UserDetails.class);
                        if (userDetails.getApprovalStatus().equals("Approved")) {
                            storeNameList.add(userDetails);
                        }
                    }
                    storespinner.setItems(storeNameList);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ad_TypeRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            checkedId = ad_TypeRadioGroup.getCheckedRadioButtonId();


            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            adv_TypeString = radioButton.getText().toString();

            if (adv_TypeString.equals("Metroz")) {
                //brandSpinner.setVisibility(View.INVISIBLE);
                storespinner.setVisibility(View.INVISIBLE);
                //categorySpinner.setVisibility(View.VISIBLE);
                //categorySpinner.performClick();


            } else if (adv_TypeString.equals("Brands")) {
                ////brandSpinner.setVisibility(View.VISIBLE);
                storespinner.setVisibility(View.INVISIBLE);
                //categorySpinner.setVisibility(View.INVISIBLE);
               // brandSpinner.performClick();


            } else if (adv_TypeString.equals("Stores")) {
                storespinner.setVisibility(View.VISIBLE);
               // brandSpinner.setVisibility(View.INVISIBLE);
                //categorySpinner.setVisibility(View.INVISIBLE);

                storespinner.performClick();


            } else if (adv_TypeString.equals("Instruction Ad")) {
                storespinner.setVisibility(View.INVISIBLE);
               // brandSpinner.setVisibility(View.INVISIBLE);
                //categorySpinner.setVisibility(View.INVISIBLE);


            }


        });

        mediaImage.setVisibility(View.VISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        pickMedia.setOnClickListener(v -> openFileChooser());
        RadioButton imageButton = findViewById(R.id.images);
        imageButton.setChecked(true);
        mediaImage.setVisibility(View.VISIBLE);
        mediaVideo.setVisibility(View.INVISIBLE);
        mediaGIF.setVisibility(View.INVISIBLE);
        pickMedia.setOnClickListener(v -> openFileChooser());


        ad_MediaRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            checkedId = ad_MediaRadioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            adv_MediaSelectedString = radioButton.getText().toString();


            if (adv_MediaSelectedString.equals("Image")) {
                mediaImage.setVisibility(View.VISIBLE);
                mediaVideo.setVisibility(View.INVISIBLE);
                mediaGIF.setVisibility(View.INVISIBLE);
                pickMedia.setOnClickListener(v -> openFileChooser());


            } else if (adv_MediaSelectedString.equals("GIF")) {
                mediaImage.setVisibility(View.INVISIBLE);
                mediaVideo.setVisibility(View.INVISIBLE);
                mediaGIF.setVisibility(View.VISIBLE);

            } else if (adv_MediaSelectedString.equals("Video")) {
                mediaImage.setVisibility(View.INVISIBLE);
                mediaVideo.setVisibility(View.VISIBLE);
                mediaGIF.setVisibility(View.INVISIBLE);

            }

        });

        storespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStoreString = parent.getItemAtPosition(position).toString();

                //brands_storesEditText.setText(selectedStoreString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrandString = parent.getItemAtPosition(position).toString();

                brands_storesEditText.setText(selectedBrandString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        /*categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryString = parent.getItemAtPosition(position).toString();

               // brands_storesEditText.setText(selectedCategoryString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        adv_Pricing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adv_PricingSelectedString = parent.getItemAtPosition(position).toString();
                adv_Priority = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        durationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                advertisementSelectedString = parent.getItemAtPosition(position).toString();
                adv_SelectedDuration = position;


                if (adv_SelectedDuration == 0) {
                    Toast.makeText(AddAdvertisementActivity.this, "Select the Advertiement duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adv_SelectedDuration == 1) {


                    SetDateTimePicker();

                    purchaseDateSetListener = (datePicker, year, month, day) -> {

                        final long millisToAdd = 3_600_000; //two hours
                        calculateDate(year, month, day, millisToAdd);

                    };
                    return;
                } else if (adv_SelectedDuration == 2) {

                    SetDateTimePicker();

                    purchaseDateSetListener = (datePicker, year, month, day) -> {

                        month = month + 1;
                        if (day == 0 || day < 10) {
                            dateDayCal = "0" + day;
                        } else if (day >= 10) {
                            dateDayCal = String.valueOf(day);
                        }
                        if (month == 0 || month < 10) {
                            monthCal = "0" + month;
                        } else if (month >= 10) {
                            monthCal = String.valueOf(month);
                        }


                        String date = dateDayCal + "-" + monthCal + "-" + year;

                        dateText.setText(date);


                        advertisementScheduleDate = dateText.getText().toString();
                        advertisementScheduledTime = hourText.getText().toString();


                        String dt = advertisementScheduleDate;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(dt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 1);  // number of days to add
                        dt = sdf.format(c.getTime());

                        oneDayAfterScheduledDate = dt;


                        String time = advertisementScheduledTime;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");


                        final String dateString = time;
                        final long millisToAdd = 86_400_000 - 1; //23 hours 59 sec

                        DateFormat format = new SimpleDateFormat("hh:mm a");

                        Date d = null;
                        try {
                            d = format.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        d.setTime(d.getTime() + millisToAdd);

                        String hoursAgo24 = timeformat.format(d.getTime());


                        advertismentStartDurtionstring = advertisementScheduleDate + "," + time;
                        advertisementExpiringDuration = oneDayAfterScheduledDate + "," + hoursAgo24;

                    };
                    return;
                } else if (adv_SelectedDuration == 3) {
                    SetDateTimePicker();

                    purchaseDateSetListener = (datePicker, year, month, day) -> {


                        month = month + 1;
                        if (day == 0 || day < 10) {
                            dateDayCal = "0" + day;
                        } else if (day >= 10) {
                            dateDayCal = String.valueOf(day);
                        }
                        if (month == 0 || month < 10) {
                            monthCal = "0" + month;
                        } else if (month >= 10) {
                            monthCal = String.valueOf(month);
                        }


                        String date = dateDayCal + "-" + monthCal + "-" + year;
                        dateText.setText(date);


                        advertisementScheduleDate = dateText.getText().toString();
                        advertisementScheduledTime = hourText.getText().toString();


                        String dt = advertisementScheduleDate;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(dt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 7);  // number of days to add
                        dt = sdf.format(c.getTime());


                        oneDayAfterScheduledDate = dt;


                        String time = advertisementScheduledTime;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");


                        final String dateString = time;
                        final long millisToAdd = 604_800_000; //23 hours 59 sec

                        DateFormat format = new SimpleDateFormat("hh:mm a");

                        Date d = null;
                        try {
                            d = format.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        d.setTime(d.getTime() + millisToAdd);

                        String hoursAgo24 = timeformat.format(d.getTime());


                        advertismentStartDurtionstring = advertisementScheduleDate + "," + time;
                        advertisementExpiringDuration = oneDayAfterScheduledDate + "," + hoursAgo24;
                    };
                }


            }

            private void calculateDate(int year, int month, int day, long millisToAdd) {
                month = month + 1;
                if (day == 0 || day < 10) {
                    dateDayCal = "0" + day;
                } else if (day >= 10) {
                    dateDayCal = String.valueOf(day);
                }
                if (month == 0 || month < 10) {
                    monthCal = "0" + month;
                } else if (month >= 10) {
                    monthCal = String.valueOf(month);
                }


                String date = dateDayCal + "-" + monthCal + "-" + year;

                dateText.setText(date);
                advertisementScheduleDate = dateText.getText().toString();
                advertisementScheduledTime = hourText.getText().toString();


                String dtStart = advertisementScheduledTime;

                SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");

                final String dateString = dtStart;


                DateFormat format = new SimpleDateFormat("hh:mm a");

                Date d = null;
                try {
                    d = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                d.setTime(d.getTime() + millisToAdd);

                String hourago = timeformat.format(d.getTime());


                advertismentStartDurtionstring = advertisementScheduledTime;
                advertisementExpiringDuration = hourago;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        uploadAdvertisement.setOnClickListener(v -> {
            if (ad_TypeRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Select advertising agent", Toast.LENGTH_SHORT).show();
            }
            else if (adv_TypeString.equals("Stores")) {
                if (storespinner.getSelectedItem().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select store for advertisment", Toast.LENGTH_SHORT).show();
                }
                else if (ad_MediaRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Select advertisement type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mimageuri == null) {
                    Toast.makeText(getApplicationContext(), "Select advertising Media", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adv_SelectedDuration == 0) {
                    Toast.makeText(getApplicationContext(), "Select advertisement Duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (hourText.getText().toString().equals("") || hourText.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Set Time Duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateText.getText().toString().equals("") || dateText.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Set Date Duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (adv_Priority == 0) {
                    Toast.makeText(getApplicationContext(), "Select advertisement Priority", Toast.LENGTH_SHORT).show();
                    return;
                } else if (advertisingAmount.getText().toString().equals("") || advertisingAmount.getText().toString() == null) {
                    advertisingAmount.setError("Required");
                    return;
                } else if (mimageuri != null) {
                    UploadImage();
                    return;
                }

            } else if (adv_TypeString.equals("Instruction Ad")) {
                if (ad_MediaRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Select advertisement type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mimageuri == null) {
                    Toast.makeText(getApplicationContext(), "Select advertising Media", Toast.LENGTH_SHORT).show();
                    return;
                } /*else if (adv_SelectedDuration == 0) {
                    Toast.makeText(getApplicationContext(), "Select advertisement Duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (hourText.getText().toString().equals("") || hourText.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Set Time Duration", Toast.LENGTH_SHORT).show();
                    return;
                } else if (dateText.getText().toString().equals("") || dateText.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Set Date Duration", Toast.LENGTH_SHORT).show();
                    return;
                } */else if (adv_Priority == 0) {
                    Toast.makeText(getApplicationContext(), "Select advertisement Priority", Toast.LENGTH_SHORT).show();
                    return;
                }/* else if (advertisingAmount.getText().toString().equals("") || advertisingAmount.getText().toString() == null) {
                    advertisingAmount.setError("Required");
                    return;
                }*/ else if (mimageuri != null) {
                    UploadImage();
                    return;
                }

            }


        });


        advertisementDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addMaxId = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetDateTimePicker() {
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                                if (hourOfDay < 12) {
                                    hourText.setText(pad(hourOfDay) + ":" + pad(minute) + " AM");

                                } else {
                                    hourText.setText(pad(hourOfDay) + ":" + pad(minute) + " PM");

                                }
                            }
                        }, hour, minute, true);
                tpd.show(getFragmentManager(), "TimePickerDialog");
            }
        });

        setDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    AddAdvertisementActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    purchaseDateSetListener,
                    year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
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
            Picasso.get().load(mimageuri).fit().into(advertisementImage);
        }
    }

    private String getExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddAdvertisementActivity.this);
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
                navigationView.setCheckedItem(R.id.Add_Advertisment_id);
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
        } else if (id == R.id.deliveryBoyHistory) {
            Intent intent = new Intent(getApplicationContext(), DeliveryBoyHistory.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.viewadvertisement) {
            Intent intent = new Intent(getApplicationContext(), ViewAdvertisementActivity.class);
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

    public void UploadImage() {
        // Toast.makeText(this, ""+adv_TypeString, Toast.LENGTH_SHORT).show();
        if (adv_TypeString.equals("Instruction Ad"))
        {
            if (mimageuri != null) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(AddAdvertisementActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#08C5F4"));
                pDialog.setTitleText("Uploading Advertisement.....");
                pDialog.setCancelable(false);
                pDialog.show();
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                StorageReference fileRef = advertisementStorageRef.child("Advertisement/" + System.currentTimeMillis());
                mUploadTask = fileRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    final Uri downloadUrl = urlTask.getResult();
                    String creationDate = DateUtils.fetchCurrentDate();
                    advertisementDetails.setImage(String.valueOf(downloadUrl));
                    advertisementDetails.setId(String.valueOf(addMaxId + 1));
                    advertisementDetails.setCreationdate(creationDate);
                    advertisementDetails.setAdvertisingAgent(adv_TypeString);
                    advertisementDetails.setAdvertisingBrandName("");
                    advertisementDetails.setAdvertisingMedia(adv_MediaSelectedString);
                    advertisementDetails.setAdvertisingType(adv_TypeSelectedString);
                    advertisementDetails.setAdvertisementPlacing((adv_PricingSelectedString));
                    advertisementDetails.setAdvertisementpriority(String.valueOf(adv_Priority));
                    advertisementDetails.setAdvertisingDuration(advertisementSelectedString);
                    advertisementDetails.setScheduledDate("");
                    advertisementDetails.setScheduledTime("");
                    advertisementDetails.setAdvertisementstatus("Active");
                    advertisementDetails.setAdvertisingStoreList(storespinner.getSelectedItems());
                    advertisementDetails.setAdvertisementAmount(0);
                    advertisementDetails.setAdvertisingCategoryName("");
                    advertisementDetails.setAdvertisementPaymentType(payingStatus);
                    advertisementDetails.setAdvertisementEndingDurationDate("");
                    advertisementDetails.setAdvertisementExpiringDuration("");
                    advertisementDataRef.child(String.valueOf(addMaxId + 1)).setValue(advertisementDetails);
                    Toast.makeText(AddAdvertisementActivity.this, DETAILS_INSERTED, Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    advertisementImage.setImageDrawable(null);
                    mimageuri = null;

                });
            }
        }
        else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddAdvertisementActivity.this);

            LayoutInflater inflater = AddAdvertisementActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.amount_for_advertisment, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);

            RadioButton prePaid = dialogView.findViewById(R.id.prePaid);
            RadioButton postPaid = dialogView.findViewById(R.id.postPaid);

            Button CancelAd = dialogView.findViewById(R.id.CancelAd);
            Button okAd = dialogView.findViewById(R.id.OkAd);


            okAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (prePaid.isChecked() == true) {
                        payingStatus = "PrePaid";
                    } else if (postPaid.isChecked() == true) {
                        payingStatus = "PostPaid";
                    }
                    if (prePaid.isChecked() == true || postPaid.isChecked() == true) {
                        if (mimageuri != null) {
                            final SweetAlertDialog pDialog = new SweetAlertDialog(AddAdvertisementActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#08C5F4"));
                            pDialog.setTitleText("Uploading Advertisement.....");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            Bitmap bmp = null;
                            try {
                                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mimageuri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                            byte[] data = baos.toByteArray();
                            StorageReference fileRef = advertisementStorageRef.child("Advertisement/" + System.currentTimeMillis());
                            mUploadTask = fileRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                final Uri downloadUrl = urlTask.getResult();
                                String creationDate = DateUtils.fetchCurrentDate();
                                advertisementDetails.setImage(String.valueOf(downloadUrl));
                                advertisementDetails.setId(String.valueOf(addMaxId + 1));
                                advertisementDetails.setCreationdate(creationDate);
                                advertisementDetails.setAdvertisingAgent(adv_TypeString);
                                advertisementDetails.setAdvertisingBrandName("");
                                advertisementDetails.setAdvertisingMedia(adv_MediaSelectedString);
                                advertisementDetails.setAdvertisingType(adv_TypeSelectedString);
                                advertisementDetails.setAdvertisementPlacing((adv_PricingSelectedString));
                                advertisementDetails.setAdvertisementpriority(String.valueOf(adv_Priority));
                                advertisementDetails.setAdvertisingDuration(advertisementSelectedString);
                                advertisementDetails.setScheduledDate(advertisementScheduleDate);
                                advertisementDetails.setScheduledTime(advertisementScheduledTime);
                                advertisementDetails.setAdvertisementstatus("Active");
                                advertisementDetails.setAdvertisingStoreList(storespinner.getSelectedItems());
                                advertisementDetails.setAdvertisementAmount(Integer.parseInt(advertisingAmount.getText().toString()));
                                advertisementDetails.setAdvertisingCategoryName("");
                                advertisementDetails.setAdvertisementPaymentType(payingStatus);

                                if (adv_TypeString.equals("Stores")) {
                                    if (advertisementSelectedString.equals("One Hour")) {
                                        final String dateString = hourText.getText().toString();
                                        final long millisToAdd = 3_600_000;
                                        SimpleDateFormat format = new SimpleDateFormat("kk:mm a");
                                        Date d = null;
                                        try {
                                            d = format.parse(dateString);
                                            d.setTime(d.getTime() + millisToAdd);
                                            System.out.println("New value: " + d);
                                            DateFormat date = new SimpleDateFormat("kk:mm a");
                                            advertisementDetails.setAdvertisementExpiringDuration(date.format(d));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        advertisementDetails.setAdvertisementstatus("Active");
                                        advertisementDetails.setAdvertisementEndingDurationDate(advertisementScheduleDate);
                                        advertisementDetails.setAdvertisementStartingDuration(advertismentStartDurtionstring);
                                    } else if (advertisementSelectedString.equals("One Day")) {
                                        String dt = advertisementScheduleDate;  // Start date
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        Calendar c = Calendar.getInstance();
                                        try {
                                            c.setTime(sdf.parse(dt));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        c.add(Calendar.DATE, 1);
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                                        advertisementDetails.setAdvertisementEndingDurationDate(sdf1.format(c.getTime()));
                                        advertisementDetails.setAdvertisementExpiringDuration(advertisementScheduledTime);
                                    } else if (advertisementSelectedString.equals("One Week")) {
                                        String dt = advertisementScheduleDate;  // Start date
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        Calendar c = Calendar.getInstance();
                                        try {
                                            c.setTime(sdf.parse(dt));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        c.add(Calendar.DATE, 6);
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                                        advertisementDetails.setAdvertisementEndingDurationDate(sdf1.format(c.getTime()));
                                        advertisementDetails.setAdvertisementExpiringDuration(advertisementScheduledTime);
                                    }
                                }
                                advertisementDataRef.child(String.valueOf(addMaxId + 1)).setValue(advertisementDetails);
                                Toast.makeText(AddAdvertisementActivity.this, DETAILS_INSERTED, Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), AddAdvertisementActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                advertisementImage.setImageDrawable(null);
                                mimageuri = null;

                            });
                        }
                    } else {
                        Toast.makeText(AddAdvertisementActivity.this, "Please select Payment ttype", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            CancelAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog = dialogBuilder.create();
            if (!((Activity) AddAdvertisementActivity.this).isFinishing()) {
                alertDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddAdvertisementActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public String pad(int input) {
        String str = "";
        if (input >= 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddAdvertisementActivity.this);
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