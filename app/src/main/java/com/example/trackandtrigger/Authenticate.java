package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.concurrent.TimeUnit;

public class Authenticate extends AppCompatActivity {
    FirebaseAuth auth;

    Button verify_btn;
    EditText code_entered_by_user;
    String verificationCodeBySystem;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        Bundle bundle=getIntent().getExtras();
        String phno = bundle.getString("PhoneNO");
        String email = bundle.getString("EmailID");
        String name = bundle.getString("Name");
        String Google=bundle.getString("Google");

        verify_btn = findViewById(R.id.Verify);
        code_entered_by_user = findViewById(R.id.PhVerify);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        sendVerificationCodeToUser(phno);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = code_entered_by_user.getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    code_entered_by_user.setError("Wrong OTP...");
                    code_entered_by_user.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });


        
    }

    private void sendVerificationCodeToUser(String phoneNo)  {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNo, "IN");
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164), // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    TaskExecutors.MAIN_THREAD, // Activity (for callback binding)
                    mCallbacks); // OnVerificationStateChangedCallbacks
        } catch (Exception e) {
            // Wrong format
            Toast.makeText(Authenticate.this,  e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //Get the code in global variable
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Authenticate.this,  e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        Bundle bundle=getIntent().getExtras();
        String phno = bundle.getString("PhoneNO");
        String email = bundle.getString("EmailID");
        String name = bundle.getString("Name");
        String Google= bundle.getString("Google");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Authenticate.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Authenticate.this, "Verified", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Profession.class);
                            intent.putExtra("name",name);
                            intent.putExtra("EmailID",email);
                            intent.putExtra("Phone",phno);
                            intent.putExtra("Google",Google);
                        //change later
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Authenticate.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}