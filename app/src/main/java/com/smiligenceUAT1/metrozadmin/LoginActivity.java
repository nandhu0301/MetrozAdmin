package com.smiligenceUAT1.metrozadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.TextUtils;

import static com.smiligenceUAT1.metrozadmin.common.Constant.ADMIN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.INVALID_PASSWORD;
import static com.smiligenceUAT1.metrozadmin.common.Constant.INVALID_USERNAME;
import static com.smiligenceUAT1.metrozadmin.common.Constant.LOGIN_SUCCESS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.LOGIN_UNSUCCESS;
import static com.smiligenceUAT1.metrozadmin.common.Constant.PHONE_NUM_COLUMN;
import static com.smiligenceUAT1.metrozadmin.common.Constant.TEXT_BLANK;
import static com.smiligenceUAT1.metrozadmin.common.Constant.USER_DETAILS_TABLE;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    private TextView createAccount, forgetPassword;
    Button login;
    ImageView viewPassword, hidePassword;
    public static String usernameStr;
    static String passwordStr;
    public static String productKey;
    public static String businessName;
    public static String role_name, passwordStrDB;
    SharedPreferences.Editor editor;
    DatabaseReference logindatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        logindatabase = FirebaseDatabase.getInstance ("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference ( USER_DETAILS_TABLE );
        createAccount = findViewById ( R.id.createAccTextView );
        forgetPassword = findViewById ( R.id.resetPwTextView );
        login = findViewById ( R.id.loginbutton );
        viewPassword = findViewById ( R.id.LoginViewPassword );
        hidePassword = findViewById ( R.id.LoginHidePassword );

        username = findViewById ( R.id.UsernameEditText );
        password = findViewById ( R.id.passwordEditText );

        viewPassword.setOnClickListener (v -> {
            password.setTransformationMethod ( PasswordTransformationMethod.getInstance () );
            hidePassword.setVisibility ( View.VISIBLE );
            viewPassword.setVisibility ( View.INVISIBLE );
        });

        hidePassword.setOnClickListener (v -> {
            password.setTransformationMethod ( HideReturnsTransformationMethod.getInstance () );
            hidePassword.setVisibility ( View.INVISIBLE );
            viewPassword.setVisibility ( View.VISIBLE );

        });

        login.setOnClickListener (v -> {
            usernameStr = username.getText ().toString ().trim ();
            passwordStr = password.getText ().toString ().trim ();
            try {
                final String encrtptPassword = CommonMethods.encrypt ( passwordStr );
                if (TextUtils.validateLoginForm ( usernameStr, passwordStr, username, password ) == true) {

                    final Query userNameQuery = logindatabase.orderByChild ( PHONE_NUM_COLUMN ).equalTo ( usernameStr );

                    userNameQuery.addListenerForSingleValueEvent ( new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount () > 0) {
                                for ( DataSnapshot snap : dataSnapshot.getChildren () ) {
                                    UserDetails loginUserDetailslist = snap.getValue ( UserDetails.class );
                                    productKey = loginUserDetailslist.getProductKey ();
                                    businessName = loginUserDetailslist.getBusinessName ();
                                    role_name = loginUserDetailslist.getRoleName ();
                                    passwordStrDB = loginUserDetailslist.getPassword ();
                                    if (passwordStrDB.equals ( encrtptPassword )) {
                                        if (role_name.equals ( ADMIN )) {
                                            SharedPreferences sharedPreferences = getSharedPreferences ( "LOGIN", MODE_PRIVATE );
                                            editor = sharedPreferences.edit ();
                                            editor.putString ( "userName", usernameStr );
                                            editor.putString ( "businessNameStr", businessName );
                                            editor.putString ( "productkeyStr", productKey );
                                            editor.commit ();
                                            Toast.makeText ( LoginActivity.this, LOGIN_SUCCESS, Toast.LENGTH_SHORT ).show ();
                                            username.setText ( TEXT_BLANK );
                                            password.setText ( TEXT_BLANK );
                                            Intent intent = new Intent ( getApplicationContext (), OnBoardScreenActivity.class );
                                            intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                            startActivity ( intent );
                                        } else {
                                            Toast.makeText ( LoginActivity.this, LOGIN_UNSUCCESS, Toast.LENGTH_SHORT ).show ();
                                        }
                                    } else {
                                        Toast.makeText ( LoginActivity.this, INVALID_PASSWORD, Toast.LENGTH_SHORT ).show ();
                                    }
                                }


                            } else {
                                Toast.makeText ( LoginActivity.this, INVALID_USERNAME, Toast.LENGTH_SHORT ).show ();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText ( LoginActivity.this, LOGIN_UNSUCCESS, Toast.LENGTH_SHORT ).show ();
                        }
                    } );

                }
            } catch (Exception e) {
                e.printStackTrace ();
            }

        });
        createAccount.setOnClickListener (v -> {
            Intent intent = new Intent ( LoginActivity.this, RegisterActivity.class );
            startActivity ( intent );
        });
        forgetPassword.setOnClickListener (v -> {

            Intent intent = new Intent ( LoginActivity.this, ForgetPassword.class );
            startActivity ( intent );
        });
    }
}