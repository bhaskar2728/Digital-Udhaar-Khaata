package com.example.digitaludhaarkhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText txt1,txt2;
    ImageView img1,img2;
    TextView txtSigUp;
    Button btnLogin,btnOTP;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();

        txtSigUp = findViewById(R.id.txtSignUp);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        btnLogin = findViewById(R.id.btnLogin);
        btnOTP = findViewById(R.id.btnOTP);

        txtSigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        txt1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                txt1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                txt2.setTextColor(getResources().getColor(R.color.Black));
                txt2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Black),PorterDuff.Mode.SRC_ATOP);
                img1.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.Black), PorterDuff.Mode.SRC_IN);
                return false;
            }
        });


        txt2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                txt2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                txt1.setTextColor(getResources().getColor(R.color.Black));
                txt1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Black),PorterDuff.Mode.SRC_ATOP);
                img2.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                img1.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.Black), PorterDuff.Mode.SRC_IN);
                return false;
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn(txt1.getText().toString(),txt2.getText().toString());
            }
        });

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if(firebaseUser!=null){
//            mAuth.signOut();
//        }
//    }

    public void startSignIn(String email, String password){
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this,AfterLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid Credentials or Server Problem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
