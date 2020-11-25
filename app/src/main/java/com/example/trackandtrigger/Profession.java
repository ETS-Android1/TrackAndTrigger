package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
            String email=intent.getStringExtra("EmailID");
            String phone=intent.getStringExtra("Phone");
            String Google=intent.getStringExtra("Google");
            if(name !=null) {
                FirebaseDatabase.getInstance().getReference().child(name).setValue(name);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("Email", email);
                map.put("PhoneNo", phone);
                map.put("Name", name);
                FirebaseDatabase.getInstance().getReference().child(name).updateChildren(map);
                HashMap<String, Object> map1 = new HashMap<String, Object>();
                map1.put("Groceries", "Groceries");
                map1.put("Kitchen Appliances", "Kitchen Appliances");
                map1.put("Household maintainence", "HouseHold maintainence");
                map1.put("To Do List","To Do List");
                if (name != null) {
                    System.out.println("dashboard" + name);
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").setValue("dashboard");
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").updateChildren(map1);
                }
                FirebaseDatabase.getInstance().getReference().child(name).child("profession").setValue(prof);
            }
            if(Google.equals("Yes"))
            {
                 Intent Dashboard = new Intent(Profession.this, Dashboard.class);
                 Dashboard.putExtra("profename",name);
                 startActivity(Dashboard);
                 finish();
            }
            else {
                Intent profint = new Intent(Profession.this, Login.class);
                profint.putExtra("profename", name);
                startActivity(profint);
                finish();
            }
            }
        });




    }
}