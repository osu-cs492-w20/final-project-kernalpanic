package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.inventoryirecord.photos.BitmapUtils;

import java.io.File;
import java.io.IOException;


public class AddImageActivity extends AppCompatActivity {
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.inventoryirecord.fileprovider";
    // ID for receiving image.
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 200;

    private String mTempPhotoPath;
    private Uri mPhotoURI;
    private Bitmap mResultsBitmap;
    private String mSavedPhotoURI;

    private int CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_receipt_image_activity);

        if (checkCameraPermission() && checkFilePermission()) {
            Intent intent = getIntent();
            if (intent != null) {
                CODE = intent.getIntExtra("CODE", CODE);

                Button addReceiptButton = findViewById(R.id.add_ai_button);
                Button addButton = findViewById(R.id.add_manually_button);
                // Add text based on image code.
                setButtonText(addReceiptButton, addButton);
                if (CODE == 1 || CODE == 2) {
                    // If adding using AI.
                    addReceiptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPhoto();
                        }
                    });
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPhoto();
                        }
                    });
                }
            }
        }else{
            finish();
        }
    }

    private void setButtonText(Button ai, Button man){
        switch (CODE){
            case 1:
                ai.setText("Add Receipt Photo using AI");
                man.setText("Add Receipt Photo Manually");
                break;
            case 2:
                ai.setText("Add Object Photo using AI");
                man.setText("Add Object Photo Manually");
                break;
            default:
                ai.setText("ERROR");
                man.setText("ERROR");
        }
    }

    // Make sure the user has camera permissions.
    private boolean checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_IMAGE_CAPTURE);
            return false;
        }
        return true;
    }
    // Make sure the user has file permissions.
    private boolean checkFilePermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return false;
        }
        return true;
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
                mPhotoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);
                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
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
            BitmapUtils.deleteImageFile(mTempPhotoPath);
        }
    }

    private void processAndSetImage(){
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
        mSavedPhotoURI = BitmapUtils.saveImage(this, mResultsBitmap, true);

        Intent data = new Intent();
        data.putExtra("IMAGE_URI", mSavedPhotoURI);
        setResult(RESULT_OK, data);
        finish();
    }
}
