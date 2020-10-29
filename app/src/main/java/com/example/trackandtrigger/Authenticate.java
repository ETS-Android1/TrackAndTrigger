package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authenticate extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);


        final String[] verificationCode = new String[1];
        String phno= getIntent().getStringExtra("PhoneNO");
        String email=getIntent().getStringExtra("EmailID");
        final String[] otp = new String[1];
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = null;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno,60, TimeUnit.SECONDS,Authenticate.this,mCallback);
        EditText mPhVerify=findViewById(R.id.PhVerify);
        Button mVerify=findViewById(R.id.Verify);
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Authenticate.this,"Verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Authenticate.this,"verification failed",Toast.LENGTH_SHORT).show();
            }
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode[0] = s;
                Toast.makeText(Authenticate.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp[0] =mPhVerify.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode[0], otp[0]);

                SigninWithPhone(credential);
            }
        });

    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Authenticate.this,Profession.class));
                            finish();

                        } else {
                            Toast.makeText(Authenticate.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}