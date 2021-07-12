package com.example.myk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.BreakIterator;

public class Profile extends AppCompatActivity{

    Button logout;
    FirebaseFirestore user;
    TextView FullName, UserEmail;
    FirebaseAuth fAuth;
    String userID;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuth = FirebaseAuth.getInstance();

        FullName = findViewById(R.id.name);
        UserEmail = findViewById(R.id.user_email);
        back_btn = findViewById(R.id.backbutton);

        logout = findViewById(R.id.logout);

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //back to homepage
       back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        //fetching UID of current logged in user
        userID = fAuth.getCurrentUser().getUid();

        user = FirebaseFirestore.getInstance();
        DocumentReference myRef;
        myRef = user.collection("users").document(userID);
        myRef.get().addOnCompleteListener(task -> {
           String name = task.getResult().getString("name");
           String email = task.getResult().getString("email");
             FullName.setText(name);
             UserEmail.setText(email);
        });

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
