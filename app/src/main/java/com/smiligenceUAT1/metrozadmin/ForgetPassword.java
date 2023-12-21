package com.smiligenceUAT1.metrozadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smiligenceUAT1.metrozadmin.bean.UserDetails;
import com.smiligenceUAT1.metrozadmin.common.CommonMethods;
import com.smiligenceUAT1.metrozadmin.common.Constant;

import java.util.ArrayList;
import java.util.Random;

import static com.smiligenceUAT1.metrozadmin.common.Constant.*;


public class ForgetPassword extends AppCompatActivity {

    private TextView login;

    private EditText userName;

    String generatePassword;

    Button resetPasswordButton;
    DatabaseReference resetLoginDetails;
    ArrayList<String> userNameArrayList = new ArrayList<>();
    boolean isUserNamePresent = false;
    boolean check = true;
    String id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        resetLoginDetails = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(Constant.USER_DETAILS_TABLE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        login = findViewById(R.id.fpReturnToLoginTextView);
        userName = findViewById(R.id.fpResetusername);
        resetPasswordButton = findViewById(R.id.fpResetButton);


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(userName.getText().toString())) {
                    userName.setError(Constant.REQUIRED_MSG);
                    return;
                } else if (!(userName.getText().toString().length() == 10)) {
                    userName.setError(Constant.INVALID_PHONENUMBER);
                    return;
                } else {
                    resetLoginDetails.orderByChild("phoneNumber").equalTo(userName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getChildrenCount() > 0) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    UserDetails userDetails = dataSnapshot1.getValue(UserDetails.class);

                                    id=userDetails.getUserId();


                                    try {
                                        DatabaseReference ref = FirebaseDatabase.getInstance("https://testmetrozproject-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                                .getReference("UserDetails").child(id);
                                        generatePassword = generateString(8);
                                        String encryptedpassword = CommonMethods.encrypt(generatePassword);
                                        ref.child("password").setValue(encryptedpassword);
                                        ref.child("confirmPassword").setValue(encryptedpassword);
                                        resetPasswordDialog();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //break;
                                    // Toast.makeText(ForgetPassword.this, "preaset" + userDetails.getUserId(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), Constant.USER_NAME_NOTFOUND, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                  /*  resetLoginDetails.orderByChild("phoneNumber").equalTo(userName.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                DatabaseReference ref = FirebaseDatabase.getInstance()
                                        .getReference(USER_DETAILS_TABLE).child(userDetails.getUserId());
                                generatePassword = generateString(8);

                                try {
                                    String encryptedpassword = CommonMethods.encrypt(generatePassword);
                                    ref.child("password").setValue(encryptedpassword);
                                    ref.child("confirmPassword").setValue(encryptedpassword);
                                    resetPasswordDialog();
                                    userName.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                }
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private String generateString(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!@#$%^&*".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public void resetPasswordDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ForgetPassword.this);
        View mView = getLayoutInflater().inflate(R.layout.create_login_crendentials, null);
        final TextView userName1 = mView.findViewById(R.id.Usernamedialog);
        final TextView loginpassword = mView.findViewById(R.id.Passworddialog);
        final TextView resetHeader = mView.findViewById(R.id.greetings);
        resetHeader.setText("New_Login_Details");
        userName.setText("UserName:  " + userName.getText().toString());
        loginpassword.setText("NewPassword:  " + generatePassword);
        mBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                userName.setText("");
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCancelable ( BOOLEAN_FALSE );


    }
}