package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class Profession extends AppCompatActivity {
    RadioGroup radiogroup;
    RadioButton hm,wk,bh,js;
    Button btn;
    String prof = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession);
        btn= findViewById(R.id.next);
        hm= findViewById(R.id.hm);
        js= findViewById(R.id.jk);
        bh= findViewById(R.id.bc);
        wk= findViewById(R.id.Wk);
        final RadioGroup rg = findViewById(R.id.radiogroup);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hm.isChecked())
                    prof="Home Maker";
                else if (js.isChecked())
                    prof="Job Seeker";
                else if(bh.isChecked())
                    prof="Bachelor";
                else
                    prof="Working Professional";
             Intent intent=getIntent();
            String name=intent.getStringExtra("name");
            if(name !=null) {
                FirebaseDatabase.getInstance().getReference().child(name).child("profession").setValue(prof);
            }
                Intent profint=new Intent(Profession.this,Login.class);
                profint.putExtra("profename",name);

                profint.putExtra("profession",prof);

                startActivity(profint);
            }
        });




    }
}