package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    ListView lv;
    TextView catname;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent=getIntent();
        name=intent.getStringExtra("profename");
       if(name ==null)
        name=intent.getStringExtra("name");
        lv= findViewById(R.id.lv);
        catname= findViewById(R.id.catname);

        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("Groceries","Groceries");
        map.put("Kitchen Appliances","Kitchen Appliances");
        map.put("HouseHold maintainence","HouseHold maintainence");
        if(name != null) {
            System.out.println("dasgboard"+name);
            FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").setValue("dashboard");
            FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").updateChildren(map);
        }

        ArrayList<String> list=new ArrayList<String>();
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        if(name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard");
System.out.println("list"+name);
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot snap : snapshot.getChildren())
                        list.add(snap.getValue().toString());
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        Button btn= findViewById(R.id.addbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=catname.getText().toString();
                if(s.isEmpty())
                    Toast.makeText(Dashboard.this, "Enter Category", Toast.LENGTH_SHORT).show();
                else {
                    if(name != null)
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(s).setValue(s);
                }
            }
        });






    }

    public void Logout(View view) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();


    }
}