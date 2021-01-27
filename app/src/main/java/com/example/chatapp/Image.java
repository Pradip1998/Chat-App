package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Image extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private StorageTask uploadtotask;
    private Uri mImageUri;
    TextView upload;
    Button uploaded;
    ImageView imageview;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference mStoragRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        upload=(TextView)findViewById(R.id.uploadimage);
        uploaded=(Button)findViewById(R.id.upload);
        imageview=(ImageView)findViewById(R.id.profile_image);
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        mStoragRef= FirebaseStorage.getInstance().getReference();
        uploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadtotask!=null && uploadtotask.isInProgress()){
                    Toast.makeText(Image.this, "Upload is in Process", Toast.LENGTH_SHORT).show();

                }else {

                    Fileuploader();
                }

            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }
    private String getExtention(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader() {
        StorageReference storageReference=mStoragRef.child("users/"+fAuth.getCurrentUser().getUid()+"/Profie.jpg");
        uploadtotask=storageReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get a URL to the uploaded content
                       //   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                       startActivity(new Intent(getApplicationContext(),ChatApp.class));

                        Toast.makeText(Image.this, "Image Uploaded Successfylly", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }

    public void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            imageview.setImageURI(mImageUri);

        }

    }
}