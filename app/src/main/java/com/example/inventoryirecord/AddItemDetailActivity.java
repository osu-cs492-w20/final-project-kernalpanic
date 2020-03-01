package com.example.inventoryirecord;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.inventoryirecord.photos.AppExecutor;
import com.example.inventoryirecord.photos.BitmapUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.IOException;

public class AddItemDetailActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.inventoryirecord.fileprovider";


    public static final String EXTRA = "data";

    private AppExecutor mAppExcutor;
    private ImageView mImageView;
    private Button mStartCamera;
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;
    private FloatingActionButton mSave, mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_screen);
        final Context context = this;

        mAppExcutor = new AppExecutor();
        mImageView = findViewById(R.id.imageView);
        mImageView.setVisibility(View.GONE);

        mSave = (FloatingActionButton) findViewById(R.id.Save);
        mSave.setVisibility(View.GONE);

        mCancel = (FloatingActionButton) findViewById(R.id.cancel);
        mCancel.setVisibility(View.GONE);

        Button addReceiptButton = findViewById(R.id.AddItemReceiptPhoto);
        //Button addPhotoButton = findViewById(R.id.AddItemReceiptPhoto);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            addReceiptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Log.d("button", "LAdd Receipt Photo");

                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,REQUEST_STORAGE_PERMISSION);
                    getPhoto();
                }
            });

        }
        mSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BitmapUtils.deleteImageFile(context, mTempPhotoPath);
                BitmapUtils.saveImage(context, mResultsBitmap);
                mSave.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BitmapUtils.deleteImageFile(context, mTempPhotoPath);
                mSave.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
            }
        });
    }

    public void getPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void processAndSetImage(){
        mSave.setVisibility(View.VISIBLE);
        mCancel.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
        mImageView.setImageBitmap(mResultsBitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
