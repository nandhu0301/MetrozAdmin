package com.smiligenceUAT1.metrozadmin.common;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.smiligenceUAT1.metrozadmin.common.Constant.*;


public class TextUtils {

    public static boolean validateNames_catagoryItems(String name) {

        Pattern pattern = Pattern.compile(NAME_PATTERNS_CATAGORY_ITEM);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean isValidUserName(final String userName) {
        Pattern pattern = Pattern.compile ( USER_NAME_PATTERN );
        Matcher matcher = pattern.matcher ( userName );
        return matcher.matches ();
    }

    public static boolean validateAlphaNumericCharcters(final String bankName) {

        Pattern pattern = Pattern.compile(ITEM_NAME_PATTERN);
        Matcher matcher = pattern.matcher(bankName);
        return matcher.matches();

    }
    public static boolean isValidFirstName(final String firstName) {
        Pattern pattern = Pattern.compile ( FIRST_NAME_PATTERN );
        Matcher matcher = pattern.matcher ( firstName );
        return matcher.matches ();
    }

    public static boolean isValidlastName(final String lastName) {
        Pattern pattern = Pattern.compile ( LAST_NAME_PATTERN );
        Matcher matcher = pattern.matcher ( lastName );
        return matcher.matches ();
    }

    public static boolean isValidAddress(final String Address) {
        Pattern pattern = Pattern.compile ( ADDRESS_PATTERN );
        Matcher matcher = pattern.matcher ( Address );
        return matcher.matches ();
    }


    public static boolean isValidnumeric(final String number) {
        Pattern pattern = Pattern.compile ( NUMERIC_PATTERN );
        Matcher matcher = pattern.matcher ( number );
        return matcher.matches ();
    }

    public static boolean isValidPrice(String ValidatePrice) {
        Pattern pattern_validPrice = Pattern.compile ( ITEM_PRICE_PATTERN );
        Matcher matcher = pattern_validPrice.matcher ( ValidatePrice );
        return matcher.matches ();
    }

    public static boolean isValidAlphaCharacters(final String bankName) {
        Pattern pattern = Pattern.compile ( BANK_NAME_PATTERN );
        Matcher matcher = pattern.matcher ( bankName );
        return matcher.matches ();
    }

    public static boolean isValidAlphaNumeric(final String alphaPattern) {
        Pattern pattern = Pattern.compile ( ALPHA_NUMERIC_PATTERN );
        Matcher matcher = pattern.matcher ( alphaPattern );
        return matcher.matches ();
    }

    public static boolean isValidPassword(final String password) {
        if (password.length () >= PASSWORD_LENGTH) {
            Pattern pattern = Pattern.compile ( PASSWORD_PATTERN );
            Matcher matcher = pattern.matcher ( password );
            return matcher.matches ();
        } else {
            return BOOLEAN_FALSE;
        }
    }

    public static boolean isValidEmail(final String email) {

        Pattern pattern = Pattern.compile ( EMAIL_PATTERN );
        Matcher matcher = pattern.matcher ( email );
        return matcher.matches ();
    }

    public static boolean isValidAadharNumber(String str) {
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";
        Pattern p = Pattern.compile ( regex );
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher ( str );
        return m.matches ();
    }
    public static boolean validateLoginForm(final String userNameStr, final String passwordStr,
                                            EditText userName, EditText password) {

        if ("".equals(userNameStr)) {
            userName.setError(REQUIRED_MSG);
            return false;
        }
        if ("".equals(passwordStr)) {
            password.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }


    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("^[7689]\\d{9}$")) return true;
        else return false;
    }


    public static boolean validateFssaiNumber(String fssaiNumber) {

        if (fssaiNumber.matches ( "\\d{14}" )) return true;
        else return false;
    }

    public static boolean validateGstNumber(String gstNumber) {

        if (gstNumber.matches ( "\\d{15}" )) return true;
        else return false;
    }
    public static boolean validatePincode(String gstNumber) {

        if (gstNumber.matches ( "\\d{6}" )) return true;
        else return false;
    }

    public static boolean validateName(String name) {
        return name.matches ( "[aA-zZ]*" );
    }


    public static boolean validateNumber(String number) {
        return number.matches ( "[0-9]*" );
    }


    public static boolean isValidAlphaNumericSpecialCharacters(final String data) {
        Pattern pattern = Pattern.compile ( ALPHA_NUMERIC_STRING_PATTERN );
        Matcher matcher = pattern.matcher ( data );
        return matcher.matches ();
    }

    public static boolean isValidNamePattern(final String data) {
        Pattern pattern = Pattern.compile ( NAME_PATTERN );
        Matcher matcher = pattern.matcher ( data );
        return matcher.matches ();
    }

    public static boolean validDiscountName(String name) {

        Pattern pattern = Pattern.compile ( DISCOUNT_NAME_PATTERN );
        Matcher matcher = pattern.matcher ( name );
        return matcher.matches ();
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }

    public static <T> List<T> removeDuplicatesList(List<T> list) {


        Set<T> set = new LinkedHashSet<>(list);

        list.clear ();


        list.addAll ( set );

        return list;
    }

}
