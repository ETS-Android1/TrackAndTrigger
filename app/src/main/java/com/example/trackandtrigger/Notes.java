package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notes extends AppCompatActivity {
    EditText notes;
    Button btn, share;
    String name, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        notes = (EditText) findViewById(R.id.editTextTextMultiLine);
        btn = (Button) findViewById(R.id.update);
        share = (Button) findViewById(R.id.sharebtn);
        Intent notesint = getIntent();
        name = notesint.getStringExtra("name");
        title = notesint.getStringExtra("Item");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name != null)
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("Notes").child(title).setValue(notes.getText().toString());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareint = new Intent(Intent.ACTION_SEND);
                shareint.setType("*/*");
                shareint.putExtra(Intent.EXTRA_TEXT, notes.getText().toString());
                startActivity(Intent.createChooser(shareint, "Share using"));
            }
        });

        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("Notes");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (title.equals(snap.getKey()))
                            notes.setText(snap.getValue().toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}