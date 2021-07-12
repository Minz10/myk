package com.example.myk;

import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Contact extends AppCompatActivity{

    FloatingActionButton fab_fire_contact, fab_fire_mail, fab_emergency_contact, Fab_emergency_sms;
    ImageButton back_button;
    TextView E_Contact;

    FirebaseFirestore user;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        fab_fire_contact = (FloatingActionButton) findViewById(R.id.call_fire_dept);
        fab_fire_mail = (FloatingActionButton) findViewById(R.id.mail_fire_dept);
        fab_emergency_contact = (FloatingActionButton) findViewById(R.id.call_emergency_contact);
        Fab_emergency_sms = (FloatingActionButton) findViewById(R.id.sms_emergency_contact);
        back_button = findViewById(R.id.Back_btn);
        E_Contact = findViewById(R.id.e_contact);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid(); //fetching uid of current logged in user

        //displaying Emergency Number
        user = FirebaseFirestore.getInstance();
        DocumentReference myRef;
        myRef = user.collection("users").document(userID);
        myRef.get().addOnCompleteListener(task -> {
            String no = task.getResult().getString("emergency_no");
            E_Contact.setText(no);
        });

        fab_fire_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:101"));
                startActivity(intent);
            }
        });

        fab_fire_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mail_intent = new Intent(Intent.ACTION_SEND);
                mail_intent.setData(Uri.parse("firedepartment@gmail.com"));
                mail_intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                mail_intent.putExtra(Intent.EXTRA_SUBJECT, "Request for immediate action for LPG Leakage");
                mail_intent.putExtra(Intent.EXTRA_TEXT, "Address : XXXXXX, XXXXXX, House No. - XXX");
                mail_intent.setType("plain/text");
                startActivity(Intent.createChooser(mail_intent, "Choose an email client"));
            }
        });

        fab_emergency_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference myRef;
                myRef = user.collection("users").document(userID);
                myRef.get().addOnCompleteListener(task -> {
                    String no = task.getResult().getString("emergency_no");
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+no ));
                    startActivity(intent);
                });
            }
        });

        String message="LPG Leakage detected at my house! I'm out of station right now so could you please help me out before any accident occurs.";

        Fab_emergency_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sms_intent = new Intent(Intent.ACTION_VIEW);
                sms_intent.setData(Uri.parse("sms:"));
                sms_intent.putExtra("sms_body", message );
                startActivity(sms_intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //back to home page
            }
        });


    }
}



