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

public class Temperature extends AppCompatActivity{
    ImageButton mBack;
    TextView temperature, state;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        mBack = findViewById(R.id.backbtn);
        temperature = findViewById(R.id.temp_level);
        state = findViewById(R.id.lpg_state);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getInstance().getReference();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notification1", "My notification1", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //reading temperature
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.child("TEMPERATURE").getValue().toString();
                temperature.setText(value);
                int temperature_level = Integer.parseInt(value);
                if (temperature_level >= 40){
                    state.setText("Its getting Hot");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Temperature.this, "Notification1");
                    builder.setContentTitle("High Temperature Detected (<40 degrees)");
                    builder.setContentText("Please look into it ASAP");
                    builder.setSmallIcon(R.drawable.logo_myk);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Temperature.this);
                    managerCompat.notify(2, builder.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String value2 = "disconnected";
                temperature.setText(value2);
            }
        });
    }
}
