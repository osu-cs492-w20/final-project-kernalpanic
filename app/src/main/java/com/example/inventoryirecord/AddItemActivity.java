package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryirecord.data.InventoryItem;

import com.example.inventoryirecord.data.azure.ReceiptResult;
import com.example.inventoryirecord.photos.BitmapUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


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

    private boolean azureFlag;

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
        //setValuesForTesting();
        mInventoryItem.receiptPics = new ArrayList<>();
        mInventoryItem.itemPics = new ArrayList<>();

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
                    try {
                        test = findViewById(R.id.edit_single_item_type_text_view);
                        test.setText(tokens[0]);
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
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
                    setReceiptFields(s);
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
                azureFlag = data.getExtras().getBoolean("BUTTON_CODE");
                mSavedReceiptURI = data.getStringExtra("IMAGE_URI");
                if (mSavedReceiptURI != null && azureFlag) {
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
                azureFlag = data.getExtras().getBoolean("BUTTON_CODE");
                mSavedObjectURI = data.getStringExtra("IMAGE_URI");
                if (mSavedObjectURI != null && azureFlag) {
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
        if(buildInventory()) {
            inventoryViewModel.addSingleInventoryItem(mInventoryItem);
            finish();
        }
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
    // Going to be a messy method of checks. Maybe clean up later?
    private boolean buildInventory(){
        CheckBox newItem = findViewById(R.id.edit_single_item_new_check_box);
        TextView save = findViewById(R.id.edit_single_item_name_text_view);
        String timeCode = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        mInventoryItem.itemID = Integer.toString(textSum(timeCode));

        mInventoryItem.itemName = save.getText().toString();
        if (mInventoryItem.itemName.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter a Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        mInventoryItem.newItem = newItem.isChecked();

        save = findViewById(R.id.edit_single_item_notes_text_view);
        mInventoryItem.otherNotes = save.getText().toString();

        save = findViewById(R.id.edit_single_item_make_text_view);
        mInventoryItem.make = save.getText().toString();

        save = findViewById(R.id.edit_single_item_model_text_view);
        mInventoryItem.model = save.getText().toString();

        save = findViewById(R.id.edit_single_item_date_purch_text_view);
        mInventoryItem.datePurchased = save.getText().toString();

        save = findViewById(R.id.edit_single_item_value_text_view);
        try {
            mInventoryItem.value = Double.valueOf(save.getText().toString());
        } catch(NumberFormatException e){
            mInventoryItem.value = 0.0;
        }

        save = findViewById(R.id.edit_single_item_type_text_view);
        mInventoryItem.itemType = save.getText().toString();

        save = findViewById(R.id.edit_single_item_date_purch_text_view);
        mInventoryItem.dateAdded = save.getText().toString();

        save = findViewById(R.id.edit_single_item_date_manu_text_view);
        mInventoryItem.dateOfManufacture = save.getText().toString();

        save = findViewById(R.id.edit_single_item_serial_text_view);
        mInventoryItem.serialNumber = save.getText().toString();

        if (mSavedObjectURI != null)
            mInventoryItem.itemPics.add(mSavedObjectURI);
        if (mSavedReceiptURI != null)
            mInventoryItem.receiptPics.add(mSavedReceiptURI);

        return true;
    }
    private int textSum(String sCode){
        int iCode = 0;

        for(int i = 0; i < sCode.length();i++)
            iCode += sCode.charAt(i);

        return iCode;
    }

    private void setReceiptFields(ReceiptResult s){
        try {
            test = findViewById(R.id.edit_single_item_name_text_view);
            test.setText(s.MerchantName.text);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            test = findViewById(R.id.edit_single_item_notes_text_view);
            test.setText(s.MerchantAddress.text);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            test = findViewById(R.id.edit_single_item_value_text_view);
            test.setText(s.Total.text);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            test = findViewById(R.id.edit_single_item_date_purch_text_view);
            test.setText(s.TransactionDate.text);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
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


