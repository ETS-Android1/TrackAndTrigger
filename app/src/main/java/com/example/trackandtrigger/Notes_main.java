package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notes_main extends AppCompatActivity {
    ListView lv;
    String name;
    TextView catname;
    ArrayAdapter adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        lv = (ListView) findViewById(R.id.list);
        Intent notesint = getIntent();
        name = notesint.getStringExtra("name");
        catname = findViewById(R.id.catname);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("Notes");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot snap : snapshot.getChildren())
                        list.add(snap.getKey().toString());
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        lv.setAdapter(adapter);
        Button btn = findViewById(R.id.addbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = catname.getText().toString();
                if (s.isEmpty())
                    Toast.makeText(Notes_main.this, "Enter Notes name", Toast.LENGTH_SHORT).show();
                else {
                    if (name != null) {
                        FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("Notes").child(s).setValue(s);
                    }
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent Groceries = new Intent(Notes_main.this, Notes.class);
                Groceries.putExtra("name", name);
                Groceries.putExtra("Item", list.get(i));
                startActivity(Groceries);
            }
        });
    }
}