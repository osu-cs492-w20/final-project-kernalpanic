package com.example.inventoryirecord;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.photos.BitmapUtils;

import java.util.HashMap;
import java.util.Objects;

import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.DATE_MANU;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.DATE_PURCH;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.ITEM_NAME;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.ITEM_TYPE;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.MAKE;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.MODEL;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.NEW;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.NOTES;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.PRICE_PAID;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.SERIAL_NUMBER;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.VALUE;

public class ViewSingleItemDetailsActivity extends AppCompatActivity {
    public static final String INVENTORY_ITEM = "singleInventoryItem";
    public static final String TAG = ViewSingleItemDetailsActivity.class.getSimpleName();

    private AlertDialog.Builder alertDialog;

    private HashMap<TextViewKeys, TextView> textViewHashMap;
    private HashMap<TextViewKeys, TextView> editTextViewHashMap;
    private LinearLayout editDeleteButtonsLayout;
    private LinearLayout parentSaveCancelButtonsLayout;
    private LinearLayout itemDetailsLayout;
    private LinearLayout editItemDetailsLayout;
    private CheckBox newCheckBox;

    private InventoryItem inventoryItem;
    private InventoryViewModel inventoryViewModel;
    private InventorySaveViewModel inventorySaveViewModel;
    private LinearLayout viewPhotosButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_item_details_activity);
        Intent intent = getIntent();
        if (Objects.nonNull(intent) && intent.hasExtra(INVENTORY_ITEM)) {
            Log.d(TAG, "has extra");
            inventoryItem = (InventoryItem) intent.getSerializableExtra(INVENTORY_ITEM);
        }
        //inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        inventorySaveViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(InventorySaveViewModel.class);
        initAllViewHash();
        setViewText(textViewHashMap, Objects.requireNonNull(inventoryItem), false);

        LinearLayout editButton = findViewById(R.id.edit_single_item_button);
        parentSaveCancelButtonsLayout = findViewById(R.id.save_cancel_button_layout);
        LinearLayout saveButton = findViewById(R.id.save_edits_single_item_button);
        LinearLayout cancelButton = findViewById(R.id.cancel_edits_single_item_button);
        LinearLayout deleteButton = findViewById(R.id.delete_single_item_button);
        viewPhotosButton = findViewById(R.id.view_photos_layout_button);
        editDeleteButtonsLayout = findViewById(R.id.edit_delete_buttons_layout);
        itemDetailsLayout = findViewById(R.id.view_single_item_details_layout);
        editItemDetailsLayout = findViewById(R.id.edit_add_item_fragment);

            alertDialog = new AlertDialog.Builder(this);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleEditForm();
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
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDeleteItem();
                }
            });

            viewPhotosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePhotoButtonClick();
                }
            });

    }

    private void handleDeleteItem() {
        alertDialog.setTitle("Delete Item")
                .setMessage("Are you sure that you want to permanently delete this item?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(String location : inventoryItem.itemPics) {
                            BitmapUtils.deleteImageFile(location);
                        }

                        for(String location : inventoryItem.receiptPics) {
                            BitmapUtils.deleteImageFile(location);
                        }
                        //inventoryViewModel.deleteSingleInventoryItem(inventoryItem);
                        inventorySaveViewModel.deleteInventoryItem(inventoryItem);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void handlePhotoButtonClick() {
        viewPhotosButton.setBackground(getDrawable(R.drawable.round_button_clicked));
        Intent photosActivity = new Intent(this, ViewEditPhotosActivity.class);
        photosActivity.putExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM, inventoryItem);
        startActivityForResult(photosActivity, ViewEditPhotosActivity.VIEW_EDIT_PHOTOS_ACTIVITY_CODE);
    }

    private void handleEditForm() {
        editDeleteButtonsLayout.setVisibility(View.INVISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.VISIBLE);
        editItemDetailsLayout.setVisibility(View.VISIBLE);
        itemDetailsLayout.setVisibility(View.INVISIBLE);
        setViewText(editTextViewHashMap, Objects.requireNonNull(inventoryItem), true);
    }

    private void handleSaveForm() {
        editDeleteButtonsLayout.setVisibility(View.VISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.INVISIBLE);
        getEditText(editTextViewHashMap, inventoryItem);
        setViewText(textViewHashMap, Objects.requireNonNull(inventoryItem), false);
        editItemDetailsLayout.setVisibility(View.INVISIBLE);
        itemDetailsLayout.setVisibility(View.VISIBLE);
        inventorySaveViewModel.updateInventoryItemFields(inventoryItem);
    }
    private void handleCancelForm() {
        editDeleteButtonsLayout.setVisibility(View.VISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.INVISIBLE);

        editItemDetailsLayout.setVisibility(View.INVISIBLE);
        itemDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void initAllViewHash() {
        textViewHashMap = new HashMap<>();
        textViewHashMap.put(ITEM_NAME, (TextView) findViewById(ITEM_NAME.viewId));
        textViewHashMap.put(ITEM_TYPE, (TextView) findViewById(ITEM_TYPE.viewId));
        textViewHashMap.put(MAKE, (TextView) findViewById(MAKE.viewId));
        textViewHashMap.put(MODEL, (TextView) findViewById(MODEL.viewId));
        textViewHashMap.put(SERIAL_NUMBER, (TextView) findViewById(SERIAL_NUMBER.viewId));
        textViewHashMap.put(VALUE, (TextView) findViewById(VALUE.viewId));
        textViewHashMap.put(DATE_PURCH, (TextView) findViewById(DATE_PURCH.viewId));
        textViewHashMap.put(PRICE_PAID, (TextView) findViewById(PRICE_PAID.viewId));
        textViewHashMap.put(NEW, (TextView) findViewById(NEW.viewId));
        textViewHashMap.put(NOTES, (TextView) findViewById(NOTES.viewId));
        textViewHashMap.put(DATE_MANU, (TextView) findViewById(DATE_MANU.viewId));

        newCheckBox = findViewById(NEW.editId);

        editTextViewHashMap = new HashMap<>();
        editTextViewHashMap.put(ITEM_NAME, (EditText) findViewById(ITEM_NAME.editId));
        editTextViewHashMap.put(ITEM_TYPE, (EditText) findViewById(ITEM_TYPE.editId));
        editTextViewHashMap.put(MAKE, (EditText) findViewById(MAKE.editId));
        editTextViewHashMap.put(MODEL, (EditText) findViewById(MODEL.editId));
        editTextViewHashMap.put(SERIAL_NUMBER, (EditText) findViewById(SERIAL_NUMBER.editId));
        editTextViewHashMap.put(VALUE, (EditText) findViewById(VALUE.editId));
        editTextViewHashMap.put(DATE_PURCH, (EditText) findViewById(DATE_PURCH.editId));
        editTextViewHashMap.put(PRICE_PAID, (EditText) findViewById(PRICE_PAID.editId));
        editTextViewHashMap.put(NOTES, (EditText) findViewById(NOTES.editId));
        editTextViewHashMap.put(DATE_MANU, (EditText) findViewById(DATE_MANU.editId));

    }
    private void setViewText(HashMap<TextViewKeys, TextView> hashMap, InventoryItem item, boolean edit) {
        Objects.requireNonNull(hashMap.get(ITEM_NAME)).setText(item.itemName);
        Objects.requireNonNull(hashMap.get(ITEM_TYPE)).setText(item.itemType);
        Objects.requireNonNull(hashMap.get(MAKE)).setText(item.make);
        Objects.requireNonNull(hashMap.get(MODEL)).setText(item.model);
        Objects.requireNonNull(hashMap.get(SERIAL_NUMBER)).setText(item.serialNumber);
        Objects.requireNonNull(hashMap.get(VALUE)).setText(String.valueOf(item.value));
        Objects.requireNonNull(hashMap.get(DATE_PURCH)).setText(String.valueOf(item.datePurchased));
        Objects.requireNonNull(hashMap.get(PRICE_PAID)).setText(String.valueOf(item.pricePaid));
        Objects.requireNonNull(hashMap.get(NOTES)).setText(item.otherNotes);
        Objects.requireNonNull(hashMap.get(DATE_MANU)).setText(item.dateOfManufacture);
        if (edit) {
            newCheckBox.setChecked(item.newItem);
        } else {
            Objects.requireNonNull(hashMap.get(NEW)).setText(Boolean.toString(item.newItem));
        }
    }

    private void getEditText(HashMap<TextViewKeys, TextView> hashMap, InventoryItem item) {
        InventoryItem.Builder.newInstance().setNewItem(newCheckBox.isChecked()).build();
        item.itemName = Objects.requireNonNull(hashMap.get(ITEM_NAME)).getText().toString();
        item.itemType = Objects.requireNonNull(hashMap.get(ITEM_TYPE)).getText().toString();
        item.make = Objects.requireNonNull(hashMap.get(MAKE)).getText().toString();
        item.model = Objects.requireNonNull(hashMap.get(MODEL)).getText().toString();
        item.serialNumber = Objects.requireNonNull(hashMap.get(SERIAL_NUMBER)).getText().toString();
        item.value = Double.parseDouble(Objects.requireNonNull(hashMap.get(VALUE)).getText().toString());
        item.datePurchased = Objects.requireNonNull(hashMap.get(DATE_PURCH)).getText().toString();
        item.pricePaid = Double.parseDouble(Objects.requireNonNull(hashMap.get(PRICE_PAID)).getText().toString());
        item.otherNotes = Objects.requireNonNull(hashMap.get(NOTES)).getText().toString();
        item.newItem = newCheckBox.isChecked();
        item.dateOfManufacture = Objects.requireNonNull(hashMap.get(DATE_MANU)).getText().toString();
    }

    public enum TextViewKeys {
        ITEM_NAME(R.id.single_item_name_text_view, R.id.edit_single_item_name_text_view),
        ITEM_TYPE(R.id.single_item_type_text_view, R.id.edit_single_item_type_text_view),
        MAKE(R.id.single_item_make_text_view, R.id.edit_single_item_make_text_view),
        MODEL(R.id.single_item_model_text_view, R.id.edit_single_item_model_text_view),
        SERIAL_NUMBER(R.id.single_item_serial_text_view, R.id.edit_single_item_serial_text_view),
        VALUE(R.id.single_item_value_text_view, R.id.edit_single_item_value_text_view),
        DATE_PURCH(R.id.single_item_date_purch_text_view, R.id.edit_single_item_date_purch_text_view),
        DATE_MANU(R.id.single_item_date_manu_text_view,R.id.edit_single_item_date_manu_text_view),
        //DATE_ADD(1,1),
        NOTES(R.id.single_item_notes_text_view, R.id.edit_single_item_notes_text_view),
        PRICE_PAID(R.id.single_item_price_text_view, R.id.edit_single_item_price_text_view),
        NEW(R.id.single_item_new_text_view, R.id.edit_single_item_new_check_box);

        public int viewId;
        public int editId;

        TextViewKeys(int viewId, int editId) {
            this.viewId = viewId;
            this.editId = editId;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ViewEditPhotosActivity.VIEW_EDIT_PHOTOS_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null && data.hasExtra(ViewEditPhotosActivity.EDIT)) {
                inventoryItem = (InventoryItem) data.getSerializableExtra(ViewEditPhotosActivity.EDIT);
//                inventoryViewModel.updateSingleInventoryItem(inventoryItem);
//                inventorySaveViewModel.updateInventoryItemFields(inventoryItem);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPhotosButton.setBackground(getDrawable(R.drawable.round_button));
    }
}
