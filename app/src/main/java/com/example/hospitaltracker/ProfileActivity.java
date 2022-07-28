package com.example.hospitaltracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button submit, home;
    EditText e1, e2, e3;

    String name, no, email;
    String keys;
    int option = 0;

    //TIMESTAMP
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = s.format(new Date());
    //TIMESTAMP

    DatabaseReference userdb = FirebaseDatabase.getInstance("https://location-f9de5-default-rtdb.firebaseio.com/").getReference("Users");
    //setup database
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_profile);

        e1 = (EditText) findViewById(R.id.etName);
        e2 = (EditText)findViewById(R.id.etmail);
        //e3 = (EditText) findViewById(R.id.etPhone);

        submit = (Button) findViewById(R.id.btnsubmit);


        email = user.getEmail();
        // get email

        e2.setText(user.getEmail());

        home = (Button) findViewById(R.id.btnH);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userdashboard();
            }
        });


        userdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot keyID : datasnapshot.getChildren()) {

                    if (keyID.child("email").getValue().equals(email)) {
                        name = keyID.child("name").getValue().toString();
                        //no = keyID.child("no").getValue().toString();
                        option = 1;
                    }
                }
                e1.setText(name);
                //e3.setText(no);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (option == 0) {
                    insertUserData();
                } else if (option == 1) {
                    updatedata();
                }
            }
        });
    }


    public void updatedata() {

        String name = e1.getText().toString();
        String email = e2.getText().toString();
        String ts = time;
        com.example.hospitaltracker.Users users = new com.example.hospitaltracker.Users(name, email, ts);

        userdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot keyID : datasnapshot.getChildren()) {

                    if (keyID.child("email").getValue().equals(email)) {
                        Intent i = new Intent(getApplicationContext(), com.example.hospitaltracker.UserDashboard.class);
                        startActivity(i);
                        keys = keyID.getKey();
                        DatabaseReference userdb1 = userdb.child(keys);
                        userdb1.setValue(users);
                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insertUserData() {
        String name = e1.getText().toString();
        String email = e2.getText().toString();
        String ts = time;
        com.example.hospitaltracker.Users users = new com.example.hospitaltracker.Users(name, email, ts);
        String id = userdb.push().getKey();
        userdb.child(id).setValue(users);
        //another call update method
        //userdb.push().setValue(users);
        Toast.makeText(this, "User Data Added ", Toast.LENGTH_SHORT).show();
    }

    public void userdashboard() {
        Intent intent = new Intent(this, UserDashboard.class);
        startActivity(intent);
    }
}
