package com.example.myk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.components.Lazy;
//import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    //FloatingActionButton fab1, fab2, fab3;
    //Animation fabOpen, fabCLose, rotateForward, rotateBackward;
    //boolean isOpen = false;

    Button mTemp, mLPG, button;
    FloatingActionButton  contact;
    ImageButton profile;
    TextView Name;

    FirebaseFirestore user;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTemp = findViewById(R.id.temp_btn);
        mLPG = findViewById(R.id.lpg_btn);
        Name = findViewById(R.id.Name);
        profile =  findViewById(R.id.profile);
        contact =  findViewById(R.id.contact);
        button = findViewById(R.id.ebutton);


        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid(); //fetching uid of current logged in user



        //displaying Logged in user name as "Hello <username>"
        user = FirebaseFirestore.getInstance();
        DocumentReference myRef;
        myRef = user.collection("users").document(userID);
        myRef.get().addOnCompleteListener(task -> {
            String name = task.getResult().getString("name");
            Name.setText(name);
        });

        mTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Temperature.class));
            }
        });

        mLPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LPG.class));
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Contact.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tips.class));
            }
        });


        /*
        //animations
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        fabCLose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.from_right_anim);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.to_right_anim);

        //set the click listener on the FAB1 (menu)
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        }); */

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    /*
    private void animateFab() {
        if (isOpen){
            fab1.startAnimation(rotateForward);
            fab2.startAnimation(fabCLose);
            fab3.startAnimation(fabCLose);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isOpen=false;
        }
        else {
            fab1.startAnimation(rotateBackward);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen=true;
        }
    }   */


   /* public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    } */
}