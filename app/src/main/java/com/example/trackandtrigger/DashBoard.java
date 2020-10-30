package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DashBoard extends AppCompatActivity {
    ListView lv;
    TextView catname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        lv= findViewById(R.id.lv);
        catname= findViewById(R.id.catname);
        ArrayList<String> list=new ArrayList<String>();
        list.add("Groceries");
        list.add("Kitchen Appliances");
        list.add("HouseHold maintainence");
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DashBoard.this,list.get(i)+" ", Toast.LENGTH_SHORT).show();
            }
        });
Button btn= findViewById(R.id.addbtn);
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        list.add(catname.getText().toString());
    }
});
    }
}