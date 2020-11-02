package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    final String valid_email = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    final String valid_password = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    final Pattern mail = Pattern.compile(valid_email);
    final Pattern pass = Pattern.compile(valid_password);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail      =  findViewById(R.id.Email);
        mPassword   =  findViewById(R.id.Password);
        progressBar =  findViewById(R.id.progressBar3);
        mLoginBtn   =  findViewById(R.id.Loginbtn);
        mCreateBtn  =  findViewById(R.id.Create);
        fAuth = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,register.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString();

                Matcher mat1=mail.matcher(email);
                Matcher mat2=pass.matcher(password);

                if(!mat1.matches()) {
                    mEmail.setError("Enter valid Email");
                    return;
                }

                else if(!mat2.matches()) {
                    mPassword.setError("Minimum 8 characters, at least 1 letter, 1 number and 1 special character");
                    return;
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    //Authenticate User

                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                                String[] name = new String[4];

                                DatabaseReference reference;
                                reference = FirebaseDatabase.getInstance().getReference();
                                reference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            name[0] = childSnapshot.getKey();
                                        }
                                        Intent intent = new Intent(Login.this, Dashboard.class);
                                        intent.putExtra("name",name[0]);
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else{
                                Toast.makeText(Login.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }
}