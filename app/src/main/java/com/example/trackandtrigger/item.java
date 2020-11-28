package com.example.trackandtrigger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class item extends AppCompatActivity {
    Button btn,choose_button;
    String name,Item;
    EditText nameText, quanText;
    private ImageView image;
    private static final int GALLERY_REQUESTS = 1;
    private Uri image_uri = null;
    private StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        reference= FirebaseStorage.getInstance().getReference();
        choose_button=findViewById(R.id.Choose_image);//
        btn = (Button) findViewById(R.id.addbtn);//upload
        image =findViewById(R.id.image);//image
        nameText = (EditText) findViewById(R.id.itemtext);
        quanText = (EditText) findViewById(R.id.quantext);
        Intent itemint = getIntent();
        name = itemint.getStringExtra("name");
        Item = itemint.getStringExtra("Item");

        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title=nameText.getText().toString().trim();
                String Quan = quanText.getText().toString().trim();

                if(Title == null)
                {
                    nameText.setError("Enter Title");
                    return;
                }
                if(Quan == null)
                {
                    quanText.setError("Enter Quantity");
                    return;
                }

                if (name!=null && Title != null&& Quan!=null) {
                    StorageReference filepath = reference.child(image_uri.getLastPathSegment());
                    filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Uri downloaduri = uri;
                                    HashMap<String,Object> Item_Map = new HashMap<>();
                                    Item_Map.put("Item_Name",Title);
                                    Item_Map.put("Quantity",Quan);
                                    Item_Map.put("Image",downloaduri.toString());
                                    System.out.println(downloaduri.toString());
                                    FirebaseDatabase.getInstance().getReference().child(name).child("dashboard").child(Item).child(Title).updateChildren(Item_Map);
                                }
                            });

                        }
                    });
                }
                else
                    Toast.makeText(item.this, "Enter Name and Quantity", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUESTS&&resultCode==RESULT_OK)
        {
            // start picker to get image for cropping and then use the image in cropping activity
            Uri imageUri  = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image_uri = result.getUri();
                image.setImageURI(image_uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void opengallery()
    {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUESTS);
            }
        });
    }
}