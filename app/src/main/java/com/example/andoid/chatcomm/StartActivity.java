package com.example.andoid.chatcomm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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

public class StartActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mLoginBtn;
    private Button mSignUpBtn;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.login_email_field);
        mPassword = findViewById(R.id.login_pass_field);

        mLoginBtn = findViewById(R.id.login_button);
        mSignUpBtn = findViewById(R.id.signup_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_user();
            }
        });

        mSignUpBtn.setOnClickListener(view -> {
                Intent mainintent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(mainintent);
                //finish();
            
        });

    }


    public void login_user()
    {
        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (!isValidMail(email)) {
                Toast.makeText(StartActivity.this, "Invalid E-mail Addrress", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Logging In...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            progressDialog.hide();
                            Toast.makeText(StartActivity.this, "Incorrect E-mail/Password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        } else {

            Toast.makeText(StartActivity.this, "Please Enter E-mail & Password", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
