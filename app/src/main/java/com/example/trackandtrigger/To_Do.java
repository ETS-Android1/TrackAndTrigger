package com.example.trackandtrigger;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class To_Do extends AppCompatActivity {
    ListView to;
    Button btn;

    int k = 0;
    To_do_adapter ad;
    String name;
    ArrayList<String> progname, progtime;
    String quan[];
    String frname[];
    Button mClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__do);
        to = (ListView) findViewById(R.id.list);
        btn = (Button) findViewById(R.id.btn);
        mClear = findViewById(R.id.Clear);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        progname = new ArrayList<String>();
        progtime = new ArrayList<String>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grointent = new Intent(To_Do.this, To_Do_List.class);
                grointent.putExtra("name", name);
                startActivity(grointent);
            }
        });
        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progname.clear();
                    progtime.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        progtime.add(snap.getValue().toString());
                        progname.add(snap.getKey().toString());
                    }
                    quan = progtime.toArray(new String[progtime.size()]);
                    frname = progname.toArray(new String[progname.size()]);
                    ad = new To_do_adapter(To_Do.this, frname, quan);
                    to.setAdapter(ad);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
                mref.removeValue();
                FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List").setValue("To Do List");
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_to__do);
        to = (ListView) findViewById(R.id.list);
        btn = (Button) findViewById(R.id.btn);
        mClear = findViewById(R.id.Clear);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        progname = new ArrayList<String>();
        progtime = new ArrayList<String>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grointent = new Intent(To_Do.this, To_Do_List.class);
                grointent.putExtra("name", name);
                startActivity(grointent);
            }
        });
        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progname.clear();
                    progtime.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        progtime.add(snap.getValue().toString());
                        progname.add(snap.getKey().toString());
                    }
                    quan = progtime.toArray(new String[progtime.size()]);
                    frname = progname.toArray(new String[progname.size()]);
                    ad = new To_do_adapter(To_Do.this, frname, quan);
                    to.setAdapter(ad);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
                mref.removeValue();
                FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List").setValue("To Do List");
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_to__do);
        to = (ListView) findViewById(R.id.list);
        btn = (Button) findViewById(R.id.btn);
        mClear = findViewById(R.id.Clear);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        progname = new ArrayList<String>();
        progtime = new ArrayList<String>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grointent = new Intent(To_Do.this, To_Do_List.class);
                grointent.putExtra("name", name);
                startActivity(grointent);
            }
        });
        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progname.clear();
                    progtime.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        progtime.add(snap.getValue().toString());
                        progname.add(snap.getKey().toString());
                    }
                    quan = progtime.toArray(new String[progtime.size()]);
                    frname = progname.toArray(new String[progname.size()]);
                    ad = new To_do_adapter(To_Do.this, frname, quan);
                    to.setAdapter(ad);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List");
                mref.removeValue();
                FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List").setValue("To Do List");
                onBackPressed();
            }
        });
    }

    public class To_do_adapter extends ArrayAdapter<String> {
        private final Activity context;
        private final String prognames[];
        private final String progtime[];

        public To_do_adapter(Activity context, String[] prognames, String[] progtime) {
            super(context, R.layout.activity_to__do__category, prognames);
            this.context = context;
            this.prognames = prognames;
            this.progtime = progtime;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View view = inflater.inflate(R.layout.activity_to__do__category, null, true);
            TextView rname = (TextView) view.findViewById(R.id.remaname);
            TextView date = (TextView) view.findViewById(R.id.date);
            rname.setText(prognames[position]);
            date.setText(progtime[position]);
            return view;
        }
    }
}