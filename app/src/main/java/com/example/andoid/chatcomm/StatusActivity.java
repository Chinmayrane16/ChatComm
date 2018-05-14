package com.example.andoid.chatcomm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mStatus;
    private Button mSaveBtn;

    private DatabaseReference mStatusReference;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mStatusReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mToolbar = findViewById(R.id.status_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mStatus = findViewById(R.id.status_input);
        mSaveBtn = findViewById(R.id.status_savebtn);

        String status_value = getIntent().getStringExtra("status_value");
        mStatus.getEditText().setText(status_value);
        mStatus.getEditText().setSelection(status_value.length());

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("Saving Changes");
                mProgressDialog.show();
                String status = mStatus.getEditText().getText().toString();
                mStatusReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mProgressDialog.hide();
                        }
                        else
                        {
                            mProgressDialog.hide();
                            Toast.makeText(getApplicationContext(), "Error Occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
