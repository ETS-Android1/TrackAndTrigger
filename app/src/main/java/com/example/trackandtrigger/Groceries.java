package com.example.trackandtrigger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Groceries extends AppCompatActivity {
    ListView gro;
    Button btn;

    int k = 0;
    adapter ad;
    String name,Item;
    ArrayList<String> progname, progquan;
    String quan[];
    String frname[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);
        gro = (ListView) findViewById(R.id.list);
        btn = (Button) findViewById(R.id.btn);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Item = intent.getStringExtra("Item");
        progname = new ArrayList<String>();
        progquan = new ArrayList<String>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grointent = new Intent(Groceries.this, item.class);
                grointent.putExtra("name", name);
                grointent.putExtra("Item",Item);
                startActivity(grointent);
            }
        });
        if (name != null) {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item);
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progname.clear();
                    progquan.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        progquan.add(snap.getValue().toString());
                        progname.add(snap.getKey().toString());
                    }
                    quan = progquan.toArray(new String[progquan.size()]);
                    frname = progname.toArray(new String[progname.size()]);
                    ad = new adapter(Groceries.this, frname, quan);
                    gro.setAdapter(ad);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ArrayList<String> shItem=new ArrayList<String>();
        gro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (name != null) {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(progname.get(i));

                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shItem.clear();
                            for (DataSnapshot snap : snapshot.getChildren())
                                shItem.add(snap.getValue().toString());
                            ad.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                Intent shareint = new Intent(Groceries.this, Share.class);
                shareint.putStringArrayListExtra("sharemap", shItem);
                shareint.putExtra("name", name);
                shareint.putExtra("Item", Item);
                startActivity(shareint);


            }
        });

    }



    public class adapter extends ArrayAdapter<String> {
        private final Activity context;
        private final String prognames[];
        private final String progquan[];

        public adapter(Activity context, String[] prognames, String[] progquan) {
            super(context, R.layout.activity_category, prognames);
            this.context = context;
            this.prognames = prognames;
            this.progquan = progquan;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View view = inflater.inflate(R.layout.activity_category, null, true);
            Button plus = (Button) view.findViewById(R.id.plus);
            Button minus = (Button) view.findViewById(R.id.minus);
            TextView cat = (TextView) view.findViewById(R.id.cat);
            TextView inc = (TextView) view.findViewById(R.id.inc);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = Integer.parseInt(inc.getText().toString());
                    inc.setText(String.valueOf(i + 1));
                    if (name != null)
                        FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(prognames[position]).child("Quantity").setValue(String.valueOf(i + 1));
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = Integer.parseInt(inc.getText().toString());
                    inc.setText(String.valueOf(i - 1));
                    if (name != null)
                        FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(prognames[position]).child("Quantity").setValue(String.valueOf(i - 1));
                }
            });
            cat.setText(prognames[position]);
            inc.setText(progquan[position]);
            return view;
        }
    }

}


