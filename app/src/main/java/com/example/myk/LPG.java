package com.example.myk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LPG extends AppCompatActivity {

    ImageButton mBack1;
    TextView LPG_level, State;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getInstance().getReference();

        mBack1 = findViewById(R.id.backbtn1);
        LPG_level = findViewById(R.id.lpg_level);
        State = findViewById(R.id.lpg_state);

        //back button
        mBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //back to home page
            }
        });

        //reading from realtime database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.child("LPG_LEVEL").getValue().toString();
                LPG_level.setText(value); //displaying real time data in the app
                int level = Integer.parseInt(value);
                if (level >= 500) {
                    State.setText("Immediate Action Required");//displaying emergency text when lpg level rises above 500
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(LPG.this, "Notification");
                    builder.setContentTitle("LPG Leakage Detected");
                    builder.setContentText("Take immediate action");
                    builder.setSmallIcon(R.drawable.logo_myk);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(LPG.this);
                    managerCompat.notify(1, builder.build());

                }
                else{
                    State.setText("Normal"); //displaying normal state.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String value2 = "disconnected";
                LPG_level.setText(value2);
            }
        });

    }
}
