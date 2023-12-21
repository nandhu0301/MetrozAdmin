package com.smiligenceUAT1.metrozadmin.common;

public class Constant {
    public static String ADMIN_REVENUE_TABLE="AdminRevenueDetails";
    public static String TITLE_ADMIN_WEEKLY_PAYMENT_SCREEN =  "Weekly Payment Settlements";
    public static String DATE_TIME_FORMAT_NEW1= "HH:mm:ss";
    public static String TITLE_USER =  "User Administration";
    public static String TITLE_CONTACT_US="Contact Support";
    public  static  String TITILE_ADMIN_CONTACT_DETAILS="Delivery Details";
    public static String TITLE_CATEGORY="Maintain Categories";
    public static String TITLE_DASHBOARD="DashBoard";
    public static String TITLE_SUBCATEGORY="Maintain Sub Categories";
    public static String TITLE_VIEW_ADVERTISEMENTS="View Advertisements";
    public static  String CATEGORY_DETAILS_TABLE="Category";
    public  static  String SubCATEGORY_DETAILS="subCategoryList";
    public  static  String  CATEGORY_NAME="categoryName";
    public  static  String  SUBCATEGORY_NAME="SubcategoryName";
    public static String CATEGORY_ID="categoryId";
    public static String CATEGORY_IMAGE_STORAGE = "Categories/";
    public static String SUBCATEGORY_IMAGE_STORAGE = "sub-Categories/";
    public static String APPROVE_STORE_PARTNER="Approve Store Partner";
    public static String APPROVE_DELIVERY_PARTNER="Approve Delivery Partner";
    public static String APPROVE_ITEMS="Approve Items";
    public static String ASSIGN_ORDERS_FOR_DELIVERY="Assign Orders for delivery";
    public static String VIEW_ORDERS_ADD_COUPON="View Orders and Add Coupon";
    public static String MAINTAIN_OFFERS="Maintain Offers";
    public static String MAINTAIN_TIPS="Maintain Tips";
    public static String MAINTAIN_DELIVERY_FARE="Maintain Delivery Fare";
    public static String ADD_ADVERTISEMNETS="Add Advertisements";
    public static String BULK_UPLOAD="Bulk Upload";
    public static String STORE_HISTORY="Store History";
    public static String DELIVERY_BOY_HISTORY="Delivery Boy History";
    public static String DELIVERY_DETAILS="Delivery Details";

    public static String SELECT_ORDER_STATUS = "Select order Status";
    public static String ORDER_PLACED_STATUS = "Order Placed";
    public static String READY_FOR_PICKUP_STATUS = "Ready For PickUp";
    public static String DELIVERY_ON_WAY_STATUS_STATUS = "Delivery is on the way";
    public static String DELIVERED_STATUS = "Delivered";
    public static String TIPS_TABLE = "Tips";
    public static String REQUIRED_MSG="Required";
    public  static  String METROZ_STORE_TIMINGS="MetrozstoreTiming";

    public static int DEFAULT_ROW_COUNT = 3;
    public  static  String STOREDEAILS_STORAGE="StoreProfile";
    public static  String ORDER_DETAILS_FIREBASE_TABLE="OrderDetails";
    public static String BILLED_DATE_COLUMN="paymentDate";
    public static String FORMATED_BILLED_DATE="formattedDate";

    public static String USER_DETAILS_TABLE = "UserDetails";
    public static String SELLER_DETAILS_TABLE = "SellerLoginDetails";
    public static String DELIVERY_BOY_DETAILS_TABLE = "DeliveryBoyLoginDetails";
    public static String DISCOUNT_DETAILS_FIREBASE_TABLE = "Discount";
    public static String ADVERTISEMT_DETAILS_FIREBASE_TABLE = "Advertisements";
    public static String SELLER_CATEGORY_MAPPING_TABLE = "SellerCategoryMapping";
    public static String THIRD_PARTY_DETAILS_TABLE = "ThirdPartyDetails";
    public static  String PRODUCT_DETAILS_TABLE="ProductDetails";
    public static String ITEM_NAME_COLUMN = "itemName";

    public static String LOGIN_SUCCESS = "Login Successfull";

    public static String SELLER = "Seller";
    public static String DELIVERY_BOY = "Delivery Boy";
    public static String ADMIN = "Administration";
    public static String THIRD_PARTY = "Third Party";

    public static final int BASIC_FAIR = 25;
    public static final int MINIMUM_FAIR = 35;
    public static final int PER_Km = 7;


    public static String[] ORDER_STATUS = {"All Status", "Order Placed", "Ready For PickUp", "Delivery is on the way", "Delivered"};

    public static String DISCOUNT_NAME_COLUMN = "discountName";
    public static String BILL_DISCOUNT = "Bill Discount";
    public static String ROLE_NAME = "roleName";


    //Header
    public static String Approvals="Approvals";
    public static String ordersAndOffers="Orders and Offers";
    public static String advertisments="Advertisements";
    public static String MaintainingInputs="Maintaining Inputs";


    public static String TEXT_BLANK = "";
    public static String DATE_FORMAT = "MMMM dd, yyyy";
    public static String DATE_AND_TIME= "MMMM dd, yyyy HH:MM:SS";
    public static String DATE_TIME_FORMAT = "MMMM dd, yyyy";
    public static String  DATE_FORMAT_YYYYMD = "dd-MM-yyyy";
    public static String  FORMATED_DATE = "yyyy-M-d";
    public static final int PASSWORD_LENGTH = 8;
    public static boolean BOOLEAN_FALSE = false;
    public static boolean BOOLEAN_TRUE = true;
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@%^&+=!;'])(?=\\S+$)[^\\s$.#{}$`,.\\\\\\\"|]{4,}$";
    public static String DATE_TIME_FORMAT_NEW = "MMMM dd, yyyy HH:mm:ss";
    public  static  final String NAME_PATTERNS_CATAGORY_ITEM="^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$";
    public static String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+(\\.+[a-z]+)?";
    public static String USER_NAME_PATTERN = "^[a-zA-Z0-9_-]{3,15}$";
    public static String FIRST_NAME_PATTERN = "[a-zA-Z ]+\\.?";
    public static String NAME_PATTERN = "[a-zA-Z .@/]+";
    public static String ITEM_NAME_PATTERN = "^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$";
    public static String BANK_NAME_PATTERN = "[a-zA-Z][a-zA-Z ]+[a-zA-Z]$";
    public static String LAST_NAME_PATTERN = "[a-zA-Z ]+\\.?";
    public static String ALPHA_NUMERIC_PATTERN = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[0-9a-zA-Z]+$";
    public static String ADDRESS_PATTERN = "^[a-zA-Z0-9\\s,'-]*$/";
    public static String NUMERIC_PATTERN = "[0-9]*";
    public static final String ITEM_PRICE_PATTERN = "[0-9]*$";
    public static String ALPHA_NUMERIC_STRING_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[/])[^\\s$`,.\\\\\\;:'\"|]{3,40}$";
    public static String DISCOUNT_NAME_PATTERN = "^[a-zA-Z0-9]+([\\s][a-zA-Z0-9]+)*$";
    public static String PHONE_NUM_COLUMN = "phoneNumber";
    public static String EMAIL_COLUMN = "emailId";
    public static String DELIVERYBOY_DETAILS="DeliveryBoyLoginDetails";


    public static String LOGIN_UNSUCCESS = "UnSuccessfull Login";
    public static String INVALID_PASSWORD = "Invalid Password";
    public static String INVALID_USERNAME = "Invalid UserName";
    public static String INVALID_PHONENUMBER = "Phone Number accepts 10 digit numeric value only!";
    public static String INVALID_FIRSTNAME_SPECIFICATION = "First Name accepts Alphabet and WhiteSpaces only!";
    public static String ALPHA_NUMERIC = "Accepts Alphanumeric values!";
    public static String NUMERIC = "Accepts numeric values!";
    public static String PINCODE_VALIDATION = "Pincode accepts 6 digits number value only!";
    public static String INVALID_NAME_SPECIFICATION = "Accepts Alphabets Only(Minimum 3 characters!";
    public static String INVALID_LASTNAME_SPECIFICATION = "LastName allows alphabets or whitespaces";
    public static String INVALID_PASSWORD_SPECIFICATION = "Password must be minimum 8 charaters and should be a combination of AlphaNumeric and Specifical characters (?=.*[@#$%^&+=!]) !!!";
    public static String RE_ENTER_PASSWORD = "Re-Enter Password";
    public static String TITLE_APPROVE_SELLERS = "Approve Store Partner";
    public static String TITLE_APPROVE_DELIVERYBOY = "Approve Delivery Partner";

    public static String PASSWORD_LENGTH_TOO_SHORT = "Password length should be minimum 8 charater!";
    public static String FSSAI_NUMBER_VALIDATION = "Fssai Number accepts 14 digit numeric value only!";
    public static String GST_NUMBER_VALIDATION = "Gst Number accepts 15 digit numeric value only!";
    public static String EMAIL_EXIST = "Email already exists!";
    public static String PHONE_NUMBER_EXIST = "PhoneNumber already exists!";
    public static String INVALID_EMAIL = "Invalid email address";
    public static String DETAILS_INSERTED = "Inserted Successfully";
    public static String SELECT_ROLE_ERROR_MSG = "Please Select Role";
    public static String SELECT_STATUS = "Please Select Delivery Boy";
    public static String USER_NAME_NOTFOUND = "Please Enter correct username";
    public static String INVALID_PRODUCT_KEy = "Please enter valid productKey";
    public static String RESISTRATION_SUCCESS = "Successfully Registerd";
    public static String ASSIGNED_TO_STATUS = "assignedTo";
    public static String IMAGE_SELECT_MSG = "Please Select an image";
    public  static  String DISCOUNT_STATUS_COLUMN="discountStatus";

    public static String PLEASE_SELECT_IMAGE = "Please select an Image!!";
    public static String INVALID_CATAGORY_NAME = "Please enter valid item Catagory.";

    public static String INVALID_CATEGORY_NAME = "Please enter valid category name.";
    public  static String CATAGORY_EXIST="Catagory already Exists";


    public static String ACTIVE_STATUS = "Active";
    public static String INACTIVE_STATUS = "Inactive";

    public static String PRICE_DISCOUNT = "Price";
    public static String PERCENT_DISCOUNT = "Percent";

}
