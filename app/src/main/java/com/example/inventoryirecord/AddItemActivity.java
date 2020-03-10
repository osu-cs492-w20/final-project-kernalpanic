package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

public class AddItemActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 200;
    // ID for returning receipt image.
    private static final int RECEIPT_IMAGE = 1;
    // ID for returning object image.
    private static final int OBJECT_IMAGE = 2;

    private String mSavedReceiptURI;
    private String mSavedObjectURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);

        if (checkFilePermission()) {
            // Button listener for add receipt.
            Button addReceiptButton = findViewById(R.id.add_receipt_photo_button);
            // Button listener for add object.
            Button addObjectButton = findViewById(R.id.add_object_photo_button);

            addReceiptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAddReceipt();
                }
            });

            addObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAddObject();
                }
            });
        }else{
            Log.d("AddItemActivity", "User denied permission");
            finish();
        }
    }

    public void goToAddReceipt(){
        Intent intent = new Intent(this, AddImageActivity.class);
        intent.putExtra("CODE", RECEIPT_IMAGE);
        startActivityForResult(intent, RECEIPT_IMAGE);
    }

    public void goToAddObject(){
        Intent intent = new Intent(this, AddImageActivity.class);
        intent.putExtra("CODE", OBJECT_IMAGE);
        startActivityForResult(intent, OBJECT_IMAGE);
    }

    // Method for what to do with returning images.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Returned receipt image.
        if (requestCode == RECEIPT_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Do stuff with receipt image uri here.
                mSavedReceiptURI = data.getStringExtra("IMAGE_URI");
                //Example of URI
                TextView name = findViewById(R.id.add_single_item_name_text_view);
                name.setText(mSavedReceiptURI);
            }
        }
        // Returned object image.
        if (requestCode == OBJECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Do stuff with object image uri here.
                mSavedObjectURI = data.getStringExtra("IMAGE_URI");
                //Example of URI
                TextView name = findViewById(R.id.add_single_item_name_text_view);
                name.setText(mSavedObjectURI);
            }
        }
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
}


