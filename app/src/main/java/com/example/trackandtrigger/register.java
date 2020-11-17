package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
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
    private Button mRegisterBtn,mgoogle_signIn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    private final static int RC_SIGN_IN=123;

    ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;



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
        mgoogle_signIn =findViewById(R.id.google_signIn);

        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        createRequest();

        mgoogle_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

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
                                Toast.makeText(register.this, "Successfull!!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(register.this, "An error has occured. Try after sometime.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent auth = new Intent(register.this,Authenticate.class);
                    auth.putExtra("PhoneNO",phone);
                    auth.putExtra("EmailID",email);
                    auth.putExtra("Google","No");
                    //auth.putExtra("Password",password);
                    auth.putExtra("Name",name);
                    startActivity(auth);
                    finish();
                }
            }
        });
    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(register.this,"Verified", Toast.LENGTH_SHORT).show();
                            Intent PhoneIntent = new Intent(register.this,PhoneNumber.class);
                            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(register.this);
                            String name = signInAccount.getDisplayName();
                            String mail = signInAccount.getEmail();
                            PhoneIntent.putExtra("EmailID",mail);
                            PhoneIntent.putExtra("Name",name);
                            startActivity(PhoneIntent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(register.this,"Verification Failed", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


}