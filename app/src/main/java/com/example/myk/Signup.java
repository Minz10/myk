package com.example.myk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class Signup extends AppCompatActivity {
    EditText mName, mEmail, mPassword, meNo;
    Button mSignup;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    private static final String TAG = "Signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName = findViewById(R.id.username_input);
        mEmail = findViewById(R.id.email_input);
        mPassword = findViewById(R.id.pass);
        mSignup = findViewById(R.id.signupbtn);
        meNo = findViewById(R.id.eno_input);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mName.getText().toString();
                String e_number = meNo.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password length is too small.");
                    return;
                }
                if(e_number.length() < 10){
                    meNo.setError("Must be a 10 digit number.");
                    return;
                }
                if(TextUtils.isEmpty(e_number)){
                    meNo.setError("Emergency number is required.");
                    return;
                }

                //Registering the user

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(Signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                           //storing user data into the db
                           userID = fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference = fStore.collection("users").document(userID);
                           Map<String, Object> user = new HashMap<>();
                           user.put("name", name);
                           user.put("email", email);
                           user.put("emergency_no", e_number);
                           user.put("userID", userID);
                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                  Log.d(TAG, "onSuccess: User profile created for "+ userID);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d(TAG, "onFailure: "+ e.toString());
                               }
                           });
                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                       }
                       else{
                           Toast.makeText(Signup.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
        });
    }
}