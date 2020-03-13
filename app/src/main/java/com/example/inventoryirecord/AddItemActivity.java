package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.InventoryRepository;
import com.example.inventoryirecord.data.azure.ReceiptResult;
import com.example.inventoryirecord.photos.BitmapUtils;
import com.google.gson.Gson;


public class AddItemActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 200;
    // ID for returning receipt image.
    private static final int RECEIPT_IMAGE = 1;
    // ID for returning object image.
    private static final int OBJECT_IMAGE = 2;

    // For recycling the test textView object.
    private boolean receipt_update;
    private boolean object_update;

    private String mSavedReceiptURI;
    private String mSavedObjectURI;
    private TextView test;

    private LinearLayout editAddItemLayout;
    private LinearLayout saveCancelButton;

    private InventoryItem mInventoryItem;
    private InventoryViewModel inventoryViewModel;

    private AzureViewModel showReceiptAnalyseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);

        showReceiptAnalyseViewModel = new ViewModelProvider(this).get(AzureViewModel.class);

        editAddItemLayout = findViewById(R.id.edit_add_item_fragment);
        editAddItemLayout.setVisibility(View.VISIBLE);
        saveCancelButton = findViewById(R.id.save_cancel_button_layout);
        saveCancelButton.setVisibility(View.VISIBLE);

        // Build new inventory item. Can be used without any photo data.
        mInventoryItem = new InventoryItem();
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        //observe the change of best match object
        showReceiptAnalyseViewModel.getBestMatchObject().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) {
                    return;
                }
                if (object_update) {
                    String tokens[] = s.split(":");
                    test = findViewById(R.id.edit_single_item_type_text_view);
                    test.setText(tokens[0]);
                }
            }
        });

        //observe the change of the receipt result
        showReceiptAnalyseViewModel.getSearchResults().observe(this, new Observer<ReceiptResult>() {
            @Override
            public void onChanged(ReceiptResult s) {
                if (s == null) {
                    return;
                }

                if (receipt_update) {
                    test = findViewById(R.id.edit_single_item_name_text_view);
                    test.setText(s.MerchantName.text);

                    test = findViewById(R.id.edit_single_item_notes_text_view);
                    test.setText(s.MerchantAddress.text);

                    test = findViewById(R.id.edit_single_item_value_text_view);
                    test.setText(s.Total.text);

                    test = findViewById(R.id.edit_single_item_date_purch_text_view);
                    test.setText(s.TransactionDate.text);
                }
            }
        });

        if (checkFilePermission()) {
            // Button listener for add receipt.
            Button addReceiptButton = findViewById(R.id.add_receipt_photo_button);
            // Button listener for add object.
            Button addObjectButton = findViewById(R.id.add_object_photo_button);

            LinearLayout saveButton = findViewById(R.id.save_edits_single_item_button);
            LinearLayout cancelButton = findViewById(R.id.cancel_edits_single_item_button);

            addReceiptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If user already has an image, overwrite it.
                    // This will be managed differently later.
                    if (mSavedReceiptURI != null){
                        BitmapUtils.deleteImageFile(mSavedObjectURI);
                    }
                    goToAddReceipt();
                }
            });

            addObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If user already has an image, overwrite it.
                    // This will be managed differently later.
                    if (mSavedObjectURI != null){
                        BitmapUtils.deleteImageFile(mSavedObjectURI);
                    }
                    goToAddObject();
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSaveForm();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleCancelForm();
                }
            });
        } else {
            Log.d("AddItemActivity", "User denied permission");
            finish();
        }
    }
    // Open AddImageActivity to get a receipt photo.
    public void goToAddReceipt() {
        Intent intent = new Intent(this, AddImageActivity.class);
        intent.putExtra("CODE", RECEIPT_IMAGE);
        startActivityForResult(intent, RECEIPT_IMAGE);
    }
    // Open AddImageActivity to get an object photo.
    public void goToAddObject() {
        Intent intent = new Intent(this, AddImageActivity.class);
        intent.putExtra("CODE", OBJECT_IMAGE);
        startActivityForResult(intent, OBJECT_IMAGE);
    }

    // Method for what to do with returning images.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Returned receipt image.
        Log.d("requestCode", "CODE: " + requestCode);
        if (requestCode == RECEIPT_IMAGE) {
            if (resultCode == RESULT_OK) {
                test = null;
                // Do stuff with receipt image uri here.
                mSavedReceiptURI = data.getStringExtra("IMAGE_URI");
                if (mSavedReceiptURI != null) {
                    receipt_update = true;
                    object_update = false;
                    showReceiptAnalyseViewModel.loadAnalyseResults(mSavedReceiptURI);
                }
            }
        }
        // Returned object image.
        if (requestCode == OBJECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                test = null;
                // Do stuff with object image uri here.
                mSavedObjectURI = data.getStringExtra("IMAGE_URI");
                if (mSavedObjectURI != null) {
                    object_update = true;
                    receipt_update = false;
                    showReceiptAnalyseViewModel.loadDetectObjects(mSavedObjectURI);
                }
            }
        }
    }

    // Make sure the user has file permissions.
    private boolean checkFilePermission() {
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

    private void handleSaveForm(){
        buildInventory();
        inventoryViewModel.addSingleInventoryItem(mInventoryItem);
        finish();
    }

    private void handleCancelForm(){
        if (mSavedObjectURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        if (mSavedReceiptURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        finish();
        return;
    }

    private void buildInventory(){
        TextView save = findViewById(R.id.edit_single_item_name_text_view);
        mInventoryItem.itemName = save.getText().toString();

        save = findViewById(R.id.edit_single_item_notes_text_view);
        mInventoryItem.otherNotes = save.getText().toString();

        save = findViewById(R.id.edit_single_item_value_text_view);
        mInventoryItem.value = Double.valueOf(save.getText().toString());

        save = findViewById(R.id.edit_single_item_type_text_view);
        mInventoryItem.itemType = save.getText().toString();

        save = findViewById(R.id.edit_single_item_date_purch_text_view);
        mInventoryItem.dateAdded = save.getText().toString();
        //if (mSavedObjectURI != null)
        //    mInventoryItem.itemPics.add(mSavedObjectURI);
        //if (mSavedReceiptURI != null)
        //    mInventoryItem.receiptPics.add(mSavedReceiptURI);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mSavedObjectURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        if (mSavedReceiptURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        finish();
        return true;
    }
}


