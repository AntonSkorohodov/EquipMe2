package com.example.equipme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.equipme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText user_LBL_name,user_LBL_email,user_LBL_phone,user_LBL_password_1,user_LBL_password_2;
    Button user_BTN_register;
    FirebaseAuth authentication;
    FirebaseFirestore firestore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        authentication = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        /*if(authentication.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/
        user_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_LBL_email.getText().toString().trim();
                String name = user_LBL_name.getText().toString();
                String phone = user_LBL_phone.getText().toString().trim();
                String password_1 = user_LBL_password_1.getText().toString().trim();
                String password_2 = user_LBL_password_2.getText().toString().trim();

                Log.d("Pttt", "Email: " + email);
                Log.d("Pttt", "Name: " + name);
                Log.d("Pttt", "Phone: " + phone);
                Log.d("Pttt", "Password1: " + password_1);
                Log.d("Pttt", "Password2: " + password_2);

                if(TextUtils.isEmpty(email)){
                    user_LBL_email.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password_1)){
                    user_LBL_password_1.setError("Password is required.");
                    return;
                }

                if(password_1.equals(password_2) == false){
                    user_LBL_password_2.setError("Password dont match");
                    return;
                }

                authentication.createUserWithEmailAndPassword(email,password_1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User created succesfully", Toast.LENGTH_LONG).show();
                            userId = authentication.getCurrentUser().getUid();
                            Log.d("Pttt","current userId: " + userId);
                            List<String> dummy_array = new ArrayList<String>();
                            dummy_array.add("");
                            DocumentReference documentReference = firestore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("name",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("equipment_list",dummy_array);
                            user.put("request_list",dummy_array);
                            user.put("profile_img","");
                            Log.d("Pttt","User: "+user);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Pttt","onSuccess" + userId);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Could not create new user, " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void findViews() {
        user_LBL_name = findViewById(R.id.user_LBL_name);
        user_LBL_email = findViewById(R.id.user_LBL_email);
        user_LBL_phone = findViewById(R.id.user_LBL_phone);
        user_LBL_password_1 = findViewById(R.id.user_LBL_password_1);
        user_LBL_password_2 = findViewById(R.id.user_LBL_password_2);
        user_BTN_register = findViewById(R.id.user_BTN_register);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}