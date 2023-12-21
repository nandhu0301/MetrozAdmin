package com.smiligenceUAT1.metrozadmin.common;

import android.graphics.Color;
import android.util.Base64;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smiligenceUAT1.metrozadmin.UserRoleActivity;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CommonMethods extends AppCompatActivity {


    public static DatabaseReference fetchFirebaseDatabaseReference(String FirebaseTableName) {

        DatabaseReference mDataRef = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( FirebaseTableName);
        return mDataRef;
    }

    public static StorageReference fetchFirebaseStorageReference(String FirebaseTableName) {

        StorageReference mStorageRef = FirebaseStorage.getInstance ("gs://testmetrozproject.appspot.com")
                .getReference ( UserRoleActivity.saved_businessName + UserRoleActivity.saved_productKey + FirebaseTableName );
        return mStorageRef;
    }

    public static String decrypt(String Password) throws Exception {
        String outputString = Password;
        SecretKeySpec key = generatekey ( Password );
        Cipher c = Cipher.getInstance ( "AES" );
        c.init ( Cipher.DECRYPT_MODE, key );
        byte[] decodevalue = Base64.decode ( outputString, Base64.DEFAULT );
        byte[] decVal = c.doFinal ( decodevalue );
        String decryptedvalue = new String ( decVal );
        return decryptedvalue;
    }

    public static String encrypt(String Password) throws Exception {
        String Data = Password;
        SecretKeySpec key = generatekey ( Password );
        Cipher c = Cipher.getInstance ( "AES" );
        c.init ( Cipher.ENCRYPT_MODE, key );
        byte[] encVal = c.doFinal ( Data.getBytes () );
        String encryptedValue = Base64.encodeToString ( encVal, Base64.DEFAULT );
        return encryptedValue;
    }

    public static SecretKeySpec generatekey(String password) throws Exception {

        final MessageDigest messageDigest = MessageDigest.getInstance ( "SHA-256" );
        byte[] bytes = password.getBytes ( "UTF-8" );
        messageDigest.update ( bytes, 0, bytes.length );
        byte[] key = messageDigest.digest ();
        SecretKeySpec secretKeySpec = new SecretKeySpec ( key, "AES" );
        return secretKeySpec;

    }

    public static String generateString(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!@#$%^&*".toCharArray ();
        StringBuilder stringBuilder = new StringBuilder ();
        Random random = new Random ();

        for ( int i = 0; i < length; i++ ) {
            char c = chars[random.nextInt ( chars.length )];
            stringBuilder.append ( c );
        }
        return stringBuilder.toString ();
    }

    public static void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView ();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus ();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError ( "error" ); // any name of the error will do
            selectedTextView.setTextColor ( Color.RED ); //text color in which you want your error message to be displayed
            selectedTextView.setText ( error ); // actual error message
            spinner.performClick (); // to open the spinner list if error is found.
        }
    }
    public static void loadSalesBarChart(BarChart salesBarChart, HashMap<String, Integer> billFinalAmountHashMap) {
        ArrayList<String> timeArrayListXAxis = DateUtils.fetchTimeInterval();

        // initialize the Bardata with argument labels and dataSet
        BarData data = new BarData(timeArrayListXAxis, getBillFinalAmount(timeArrayListXAxis,billFinalAmountHashMap));
        salesBarChart.setData(data);
    }

    private static ArrayList getBillFinalAmount(ArrayList<String> timeInterval, HashMap<String, Integer> billFinalAmountHashMap) {
        ArrayList billAmountArrayList = new ArrayList();
        ArrayList salesDataSets = new ArrayList();


        int billAmount = 0;

        for (int i = 0, j = 1; i < timeInterval.size(); ) {

            for(Map.Entry<String, Integer> entry: billFinalAmountHashMap.entrySet()) {
                String startTime = timeInterval.get(i);
                String endTime = timeInterval.get(j);

                if (DateUtils.isHourInInterval(entry.getKey(), startTime, endTime)) {
                    billAmount+= entry.getValue();



                }
            }

            BarEntry value = new BarEntry(billAmount, i);
            billAmountArrayList.add(value);
            billAmount = 0;

            if (j == timeInterval.size() - 1) {
                break;
            } else {
                i++;
                j++;
            }
        }
        BarDataSet salesBarDataSet = new BarDataSet(billAmountArrayList, "Bill Amount");
        // barDataSet.setColor(Color.rgb(0, 155, 0));
        salesBarDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        salesDataSets.add(salesBarDataSet);
        return salesDataSets;
    }

    public static void loadBarChart(BarChart barChart, ArrayList<String> billTimeList){

        ArrayList<String> timeArrayListXAxis  = new ArrayList<>();
        timeArrayListXAxis = DateUtils.fetchTimeInterval();

        BarData data = new BarData(timeArrayListXAxis, getBillCountData(timeArrayListXAxis,billTimeList));
        barChart.setData(data);
    }

    private static ArrayList getBillCountData(ArrayList<String> timeInterval, ArrayList<String> billTimeList) {

        ArrayList billTimeArrayList = new ArrayList();
        ArrayList dataSets = new ArrayList();

        int counter = 0;

        System.out.println("billTimeList" + billTimeList);

        for (int i = 0, j = 1; i < timeInterval.size(); ) {

            for (String billTime : billTimeList) {
                String startTime = timeInterval.get(i);
                String endTime = timeInterval.get(j);
                System.out.println("startTime" + startTime);
                System.out.println("endTime" + billTime);

                if (DateUtils.isHourInInterval(billTime, startTime, endTime)) {
                    counter++;

                }
            }
            System.out.println("countermmmm" + i);
            BarEntry value = new BarEntry(counter, i);
            billTimeArrayList.add(value);
            counter = 0;

            if (j == timeInterval.size() - 1) {
                break;
            } else {
                i++;
                j++;
            }
        }
        BarDataSet barDataSet = new BarDataSet(billTimeArrayList, "Bill");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets.add(barDataSet);

        return dataSets;
    }
}

