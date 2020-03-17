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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryirecord.data.InventoryItem;

import com.example.inventoryirecord.data.Status;
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

    private InventoryItem mInventoryItem;
    private InventoryViewModel inventoryViewModel;
    private InventorySaveViewModel inventorySaveViewModel;

    private LinearLayout editAddItemLayout;
    private LinearLayout saveCancelButton;
    private Button mAddReceiptButton;
    private Button mAddObjectButton;
    private ProgressBar mLoadingIndicatorPB;

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
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        // Build new inventory item. Can be used without any photo data.
        mInventoryItem = new InventoryItem();
        //setValuesForTesting();
        mInventoryItem.receiptPics = new ArrayList<>();
        mInventoryItem.itemPics = new ArrayList<>();

        // Get access to DB view model.
        inventorySaveViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(InventorySaveViewModel.class);

        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        CheckForAnalysisFailure();
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
                    if (tokens[0].equals("")){
                        Toast.makeText(getApplicationContext(),"Failed to ID Object", Toast.LENGTH_SHORT).show();
                    }else {
                        test.setText(tokens[0]);
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
                    if(setReceiptFields(s)){
                        Toast.makeText(getApplicationContext(),"Failed to Analyze Receipt", Toast.LENGTH_SHORT).show();
                        if (mSavedReceiptURI != null){
                            BitmapUtils.deleteImageFile(mSavedReceiptURI);
                            mSavedReceiptURI = null;
                        }
                    }
                }
            }
        });

        if (checkFilePermission()) {
            // Button listener for add receipt.
            mAddReceiptButton = findViewById(R.id.add_receipt_photo_button);
            // Button listener for add object.
            mAddObjectButton = findViewById(R.id.add_object_photo_button);

            LinearLayout saveButton = findViewById(R.id.save_edits_single_item_button);
            LinearLayout cancelButton = findViewById(R.id.cancel_edits_single_item_button);

            mAddReceiptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If user already has an image, overwrite it.
                    // This will be managed differently later.
                    if (mSavedReceiptURI != null){
                        BitmapUtils.deleteImageFile(mSavedReceiptURI);
                    }
                    goToAddReceipt();
                }
            });

            mAddObjectButton.setOnClickListener(new View.OnClickListener() {
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
    private void CheckForAnalysisFailure(){
        showReceiptAnalyseViewModel.getObjectLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    //Disable buttons.
                    mAddObjectButton.setEnabled(false);
                    mAddReceiptButton.setEnabled(false);
                    saveCancelButton.setVisibility(View.GONE);
                    editAddItemLayout.setVisibility(View.GONE);
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                }
                // Want to implement a loading icon? Do it here.
                if (status == Status.SUCCESS) {
                    //Enable buttons.
                    mAddObjectButton.setEnabled(true);
                    mAddReceiptButton.setEnabled(true);
                    saveCancelButton.setVisibility(View.VISIBLE);
                    editAddItemLayout.setVisibility(View.VISIBLE);
                    mLoadingIndicatorPB.setVisibility(View.GONE);
                }
                if (status == Status.ERROR) {
                    //Enable buttons.
                    mAddObjectButton.setEnabled(true);
                    mAddReceiptButton.setEnabled(true);
                    saveCancelButton.setVisibility(View.VISIBLE);
                    editAddItemLayout.setVisibility(View.VISIBLE);
                    mLoadingIndicatorPB.setVisibility(View.GONE);
                }
            }
        });
        showReceiptAnalyseViewModel.getReceiptLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    //Disable buttons.
                    mAddObjectButton.setEnabled(false);
                    mAddReceiptButton.setEnabled(false);
                    saveCancelButton.setVisibility(View.GONE);
                    editAddItemLayout.setVisibility(View.GONE);
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                }
                if (status == Status.SUCCESS){
                    //Enable buttons.
                    mAddObjectButton.setEnabled(true);
                    mAddReceiptButton.setEnabled(true);
                    saveCancelButton.setVisibility(View.VISIBLE);
                    editAddItemLayout.setVisibility(View.VISIBLE);
                    mLoadingIndicatorPB.setVisibility(View.GONE);
                }
                if (status == Status.ERROR){
                    //Enable buttons.
                    mAddObjectButton.setEnabled(true);
                    mAddReceiptButton.setEnabled(true);
                    saveCancelButton.setVisibility(View.VISIBLE);
                    editAddItemLayout.setVisibility(View.VISIBLE);
                    mLoadingIndicatorPB.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Failed to Analyze Receipt", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            //inventoryViewModel.addSingleInventoryItem(mInventoryItem);
            inventorySaveViewModel.insertInventoryItem(mInventoryItem);
            finish();
        }
    }

    private void handleCancelForm(){
        if (mSavedObjectURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        if (mSavedReceiptURI != null){
            BitmapUtils.deleteImageFile(mSavedReceiptURI);
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

    private boolean setReceiptFields(ReceiptResult s) {
        int successes = 0;
        test = findViewById(R.id.edit_single_item_name_text_view);
        if (s.MerchantName != null) {
            test.setText(s.MerchantName.text);
            successes++;
        }
        test = findViewById(R.id.edit_single_item_notes_text_view);
        if (s.MerchantAddress != null){
            test.setText(s.MerchantAddress.text);
            successes++;
        }
        test = findViewById(R.id.edit_single_item_value_text_view);
        if (s.Total != null) {
            test.setText(s.Total.text);
            successes++;
        }
        test = findViewById(R.id.edit_single_item_date_purch_text_view);
        if (s.TransactionDate != null) {
            test.setText(s.TransactionDate.text);
            successes++;
        }

        return (successes <= 0) ? true : false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mSavedObjectURI != null){
            BitmapUtils.deleteImageFile(mSavedObjectURI);
        }
        if (mSavedReceiptURI != null){
            BitmapUtils.deleteImageFile(mSavedReceiptURI);
        }
        finish();
        return true;
    }
}