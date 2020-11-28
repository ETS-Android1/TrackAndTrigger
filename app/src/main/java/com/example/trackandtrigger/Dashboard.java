package com.example.trackandtrigger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {
    ListView lv;
    TextView catname;
    String name,email;
    String name1;
    FirebaseAuth Auth;
    String[] Username = new String[4];
    String[] Username1 = new String[4];
    ArrayAdapter adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        name = intent.getStringExtra("profename");
        Auth = FirebaseAuth.getInstance();
        if (name == null)
            name = intent.getStringExtra("name");
        lv = findViewById(R.id.lv);
        catname = findViewById(R.id.catname);
        if(Auth.getCurrentUser().getEmail()!=null)
        email = Auth.getCurrentUser().getEmail().toString();

        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Username[0] = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (name == null)
            name = Username[0];
        System.out.println(name);


        list = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard");
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (list.get(i).equals("To Do List")) {
                    Intent ToDo = new Intent(Dashboard.this, To_Do.class);
                    ToDo.putExtra("name", name);
                    startActivity(ToDo);
                }
                else
                {
                    Intent Groceries = new Intent(Dashboard.this,Groceries.class);
                    Groceries.putExtra("name",name);
                    Groceries.putExtra("Item",list.get(i));
                    startActivity(Groceries);
                }

            }
        });
        Button btn = findViewById(R.id.addbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = catname.getText().toString();
                if (s.isEmpty())
                    Toast.makeText(Dashboard.this, "Enter Category", Toast.LENGTH_SHORT).show();
                else {
                    if (name != null) {
                        FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(s).setValue(s);
                    }
                }
            }
        });
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,20);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        startAlarm(c);

        lv.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        name = intent.getStringExtra("profename");
        Auth = FirebaseAuth.getInstance();
        if (name == null)
            name = intent.getStringExtra("name");
        lv = findViewById(R.id.lv);
        catname = findViewById(R.id.catname);
        if(Auth.getCurrentUser().getEmail()!=null)
            email = Auth.getCurrentUser().getEmail().toString();

        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Username[0] = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (name == null)
            name = Username[0];
        System.out.println(name);


        list = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard");
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (list.get(i).equals("To Do List")) {
                    Intent ToDo = new Intent(Dashboard.this, To_Do.class);
                    ToDo.putExtra("name", name);
                    startActivity(ToDo);
                }
                else
                {
                    Intent Groceries = new Intent(Dashboard.this,Groceries.class);
                    Groceries.putExtra("name",name);
                    Groceries.putExtra("Item",list.get(i));
                    startActivity(Groceries);
                }

            }
        });
        Button btn = findViewById(R.id.addbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = catname.getText().toString();
                if (s.isEmpty())
                    Toast.makeText(Dashboard.this, "Enter Category", Toast.LENGTH_SHORT).show();
                else {
                    if (name != null) {
                        FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(s).setValue(s);
                    }
                }
            }
        });
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,20);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        startAlarm(c);

        lv.setAdapter(adapter);

    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RemainderBroadcast.class);
        intent.putExtra("Title","Remainder: Please update your Dashboard and To Do List");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(Auth.getCurrentUser().getEmail()!=null)
        email = Auth.getCurrentUser().getEmail().toString();
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Username1[0] = childSnapshot.getKey();
                    name1 = Username1[0];
                }
                if (name1 != null) {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name1).child("dashboard");
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
                if (list != null && list.size() > 0) {
                    adapter = new ArrayAdapter(Dashboard.this, android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        System.out.println(name);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (list.get(i).equals("To Do List")) {
                    Intent ToDo = new Intent(Dashboard.this, To_Do.class);
                    ToDo.putExtra("name", name);
                    startActivity(ToDo);
                }
                else
                {
                    Intent Groceries = new Intent(Dashboard.this,Groceries.class);
                    Groceries.putExtra("name",name);
                    Groceries.putExtra("Item",list.get(i));
                    startActivity(Groceries);
                }

            }
        });
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,20);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        startAlarm(c);

    }


    public void Logout(View view) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();


    }

}