package com.example.trackandtrigger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Share extends AppCompatActivity {
    ImageView imageview;
    DatabaseReference mref;
    ArrayList<String> shlist;
    String Name,Quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Intent shareint = getIntent();
        String name = shareint.getStringExtra("name");
        String Item = shareint.getStringExtra("Item");
        shlist = shareint.getStringArrayListExtra("sharemap");
        TextView tvItem = (TextView) findViewById(R.id.itemtext);
        TextView tvquan = (TextView) findViewById(R.id.quantext);
        Button share = (Button) findViewById(R.id.sharebtn);
        Button plus = (Button) findViewById(R.id.plus);
        Button minus = (Button) findViewById(R.id.minus);
        imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setDrawingCacheEnabled(true);
        Name = "Name: "+ shlist.get(1)+"\n"+"Quantity: "+shlist.get(2);
        tvItem.setText(shlist.get(1));
        tvquan.setText(shlist.get(2));
        Uri imageUri = Uri.parse(shlist.get(0).toString());
        Picasso.get().load(imageUri).into(imageview);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(tvquan.getText().toString().trim());
                tvquan.setText(String.valueOf(i + 1));
                if (name != null)
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(shlist.get(1)).child("Quantity").setValue(String.valueOf(i + 1));

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(tvquan.getText().toString().trim());
                tvquan.setText(String.valueOf(i - 1));
                if (name != null)
                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(shlist.get(1)).child("Quantity").setValue(String.valueOf(i - 1));

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image();
            }
        });

    }

    private void image(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File f = new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
        Intent shareint;


         try{
             FileOutputStream outputStream = new FileOutputStream(f);
             bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

             outputStream.flush();
             outputStream.close();
             shareint = new Intent(Intent.ACTION_SEND);
             shareint.setType("*/*");
             shareint.putExtra(Intent.EXTRA_STREAM,Uri.fromFile((f)));
             shareint.putExtra(Intent.EXTRA_TEXT,Name);
             shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

         }catch(Exception e){
             throw new RuntimeException(e);
         }
         startActivity(Intent.createChooser(shareint,"Share Using"));
    }

}