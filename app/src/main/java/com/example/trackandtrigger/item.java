package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class item extends AppCompatActivity {
    Button btn;
    String name;
    EditText nameText, quanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        btn = (Button) findViewById(R.id.addbtn);
        nameText = (EditText) findViewById(R.id.itemtext);
        quanText = (EditText) findViewById(R.id.quantext);
        Intent itemint = getIntent();
        name = itemint.getStringExtra("name");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(nameText.getText().toString());
                System.out.println(quanText.getText().toString());
                System.out.println(name);
                if (name != null)
                    FirebaseDatabase.getInstance().getReference().child(name).child("InGroceries").child(nameText.getText().toString()).setValue(quanText.getText().toString());
                onBackPressed();
            }
        });


    }
}