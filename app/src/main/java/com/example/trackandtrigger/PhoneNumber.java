package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber extends AppCompatActivity {
    final String valid_phone = "^\\d{10}$";
    final Pattern phno = Pattern.compile(valid_phone);
    EditText mphno;
    private Button mgenerate;
    ProgressBar mOTP_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        String Name = getIntent().getStringExtra("Name");
        String mail = getIntent().getStringExtra("EmailID");
        String Google="Yes";
        mphno  =  findViewById(R.id.phno);
        mgenerate = findViewById(R.id.generate);
        mOTP_bar = findViewById(R.id.OTP_bar);

        mgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = mphno.getText().toString().trim();
                Matcher mat3=phno.matcher(phone);
                if(!mat3.matches()){
                    mphno.setError("Enter 10 digit phone no.");
                    return;
                }
                 else {
                     mOTP_bar.setVisibility(View.VISIBLE);
                     Intent Auth = new Intent(PhoneNumber.this, Authenticate.class);
                     Auth.putExtra("PhoneNO", phone);
                     Auth.putExtra("EmailID", mail);
                     Auth.putExtra("Name", Name);
                     Auth.putExtra("Google","Yes");
                     startActivity(Auth);
                }
            }
        });


    }
}