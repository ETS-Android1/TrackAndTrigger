package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseUser CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth=FirebaseAuth.getInstance();
        CurrentUser=fAuth.getCurrentUser();

        if(CurrentUser == null)
        {
            Intent RegisterIntent = new Intent(MainActivity.this,register.class);
            startActivity(RegisterIntent);
        }
        else{
            Intent ProfIntent = new Intent(MainActivity.this,Profession.class);
            startActivity(ProfIntent);
        }
    }
}