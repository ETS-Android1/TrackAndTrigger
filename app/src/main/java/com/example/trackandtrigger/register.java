package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    final String valid_email = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    final String valid_password = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";//Minimum eight characters, at least one letter, one number and one special character
    final String valid_phone = "^\\d{10}$";
    final Pattern mail = Pattern.compile(valid_email);
    final Pattern pass = Pattern.compile(valid_password);
    final Pattern phno = Pattern.compile(valid_phone);
    EditText mFullName,mEmail,mPassword,mPhone;
    TextView mVerifyText;
    private Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;

    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName   =   findViewById(R.id.FullName);
        mEmail      =   findViewById(R.id.Email);
        mPassword   =   findViewById(R.id.Password);
        mPhone      =   findViewById(R.id.Phone);
        mRegisterBtn=   findViewById(R.id.Regbtn);
        mLoginBtn   =   findViewById(R.id.Login);
        mVerifyText =   findViewById(R.id.VerifyText);

        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,Login.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString();
                String phone=mPhone.getText().toString();
                String name = mFullName.getText().toString().trim();

                Matcher mat1=mail.matcher(email);
                Matcher mat2=pass.matcher(password);
                Matcher mat3=phno.matcher(phone);

                if(!mat1.matches()) {
                    mEmail.setError("Enter valid Email");
                    return;
                }

                else if(!mat2.matches()) {
                    mPassword.setError("Minimum 8 characters, at least 1 letter, 1 number and 1 special character");
                    return;
                }

                else if(!mat3.matches()) {
                    mPhone.setError("Enter 10 digit phone no.");
                    return;
                }
                else {

                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                FirebaseUser user = fAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(register.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                        mVerifyText.setVisibility(View.VISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(register.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child(name).setValue(name);
                                HashMap<String,Object> map=new HashMap<String,Object>();
                                map.put("password",password);
                                map.put("Email",email);
                                map.put("PhoneNo",phone);
                                FirebaseDatabase.getInstance().getReference().child(name).updateChildren(map);



                                Toast.makeText(register.this, "Successfull!!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(register.this, "An error has occured. Try after sometime.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent auth = new Intent(register.this,Authenticate.class);
                    auth.putExtra("PhoneNO",phone);
                    auth.putExtra("EmailID",email);
                    auth.putExtra("Password",password);
                    auth.putExtra("Name",name);
                    startActivity(auth);
                    finish();
                }
            }
        });
    }
}