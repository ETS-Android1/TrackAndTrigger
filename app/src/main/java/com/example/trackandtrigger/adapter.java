/**package com.example.trackandtrigger;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

public class adapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String prognames[];
    private final String progquan[];
    String name;

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
                    FirebaseDatabase.getInstance().getReference().child(name).child("InGroceries").child(prognames[position]).setValue(String.valueOf(i + 1));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(inc.getText().toString());
                inc.setText(String.valueOf(i - 1));
            }
        });
        cat.setText(prognames[position]);
        inc.setText(progquan[position]);
        return view;
    }
}
**/