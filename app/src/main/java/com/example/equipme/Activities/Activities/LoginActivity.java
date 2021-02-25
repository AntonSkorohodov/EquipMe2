package com.example.equipme.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.equipme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;

public class LoginActivity extends AppCompatActivity {

    EditText user_LBL_email,user_LBL_password;
    Button sign_BTN_in;
    FirebaseAuth authentication;
    TextView register_LBL_new;
    ProgressBar register_PRB_progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        findViews();
        authentication = FirebaseAuth.getInstance();

        register_LBL_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        sign_BTN_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_LBL_email.getText().toString().trim();
                String password = user_LBL_password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    user_LBL_email.setError("Email is Required");
                }

                if(TextUtils.isEmpty(password)){
                    user_LBL_password.setError("Password is required");
                }

                register_PRB_progress.setVisibility(View.VISIBLE);

                authentication.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            register_PRB_progress.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(LoginActivity.this,"Could not signin, " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            register_PRB_progress.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });


    }

    private void findViews() {
        user_LBL_email = findViewById(R.id.user_LBL_email);
        user_LBL_password = findViewById(R.id.user_LBL_password);
        sign_BTN_in = findViewById(R.id.sign_BTN_in);
        register_LBL_new = findViewById(R.id.register_LBL_new);
        register_PRB_progress =findViewById(R.id.register_PRB_progress);
    }
}