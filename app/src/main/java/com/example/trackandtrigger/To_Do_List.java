package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class To_Do_List extends AppCompatActivity {
    TextView Timer;
    int Hour, Min;
    EditText Title;
    String time,name,heading;
    private Button add;
    DatePickerDialog.OnDateSetListener setListener;
    NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__do__list);
        mNotificationHelper = new NotificationHelper(this);

        Title = findViewById(R.id.Title);
        name = getIntent().getStringExtra("name");
        add = findViewById(R.id.add);
        Timer = findViewById(R.id.Time);
        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(To_Do_List.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Hour=hourOfDay;
                        Min=minute;
                        time = Hour+":"+Min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12hours = new SimpleDateFormat("hh:mm aa");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                },12,0,false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(Hour,Min);
                timePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time_date = time;


                heading = Title.getText().toString().trim();
                if(heading == null)
                {
                    Title.setError("Enter Title");
                    return;
                }
                if(time_date == null)
                {
                    Timer.setError("Enter Time");
                    return;
                }
                FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child("To Do List").child(heading).setValue(time_date);



                onBackPressed();
            }
        });

    }



}



