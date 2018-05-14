package com.example.andoid.chatcomm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mDisplayName = findViewById(R.id.sign_displayname_field);
        mEmail = findViewById(R.id.sign_email_field);
        mPassword = findViewById(R.id.sign_pass_field);
        mCreateBtn = findViewById(R.id.sign_register_button);

        mAuth = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_user();
            }
        });
    }



    public void signup_user()
    {
        final String displayName = mDisplayName.getEditText().getText().toString();
        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        if (!email.isEmpty() && !password.isEmpty() && !displayName.isEmpty()) {

            if (!isValidMail(email)) {
                Toast.makeText(RegisterActivity.this, "Please Enter A Valid E-Mail Address", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password Should Be Atleast 6 Characters Long", Toast.LENGTH_SHORT).show();
            }
             else {
                mProgressDialog.setMessage("Signing Up...");
                mProgressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.hide();

                            //insertInDatabase(name,phone);
                            FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String , String> userMap = new HashMap<>();
                            userMap.put("name" , displayName);
                            userMap.put("status" , "Hi there, I'm using Chat Comm App.");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");

                            mDatabaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            mProgressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Signup Failed::Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            mProgressDialog.hide();
            Toast.makeText(RegisterActivity.this, "Please Enter Complete Details", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
