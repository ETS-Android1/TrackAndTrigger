package com.example.trackandtrigger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Share extends AppCompatActivity {
    ImageView imageview;
    DatabaseReference mref;
    ArrayList<String> shlist;

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
        imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setDrawingCacheEnabled(true);
        tvItem.setText(shlist.get(1));
        tvquan.setText(shlist.get(2));
        Uri imageUri = Uri.parse(shlist.get(0).toString());
        Picasso.get().load(imageUri).into(imageview);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setPackage("com.package");
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });

    }

}