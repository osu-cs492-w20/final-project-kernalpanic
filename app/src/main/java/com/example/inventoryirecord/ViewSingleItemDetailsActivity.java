package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryirecord.data.InventoryItem;

import java.util.HashMap;
import java.util.Objects;

public class ViewSingleItemDetailsActivity extends AppCompatActivity {
    public static final String INVENTORY_ITEM = "singleInventoryItem";
    public static final String TAG = ViewSingleItemDetailsActivity.class.getSimpleName();

    private HashMap<TextViewKeys, TextView> textViewHashMap;
    private HashMap<TextViewKeys, TextView> editTextViewHashMap;
    private LinearLayout editButton;
    private LinearLayout parentSaveCancelButtonsLayout;
    private LinearLayout saveButton;
    private LinearLayout cancelButton;
    private LinearLayout itemDetailsLayout;
    private LinearLayout editItemDetailsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_item_details_activity);


        Intent intent = getIntent();
        if(Objects.nonNull(intent) && intent.hasExtra(INVENTORY_ITEM)) {
            Log.d(TAG, "has extra");
            initAllViewHash();
            InventoryItem inventoryItem = (InventoryItem) intent.getSerializableExtra(INVENTORY_ITEM);

            setViewText(textViewHashMap, Objects.requireNonNull(inventoryItem));
            setViewText(editTextViewHashMap, Objects.requireNonNull(inventoryItem));

        }
        editButton = findViewById(R.id.edit_single_item_button);
        parentSaveCancelButtonsLayout = findViewById(R.id.save_cancel_button_layout);
        saveButton = findViewById(R.id.save_edits_single_item_button);
        cancelButton = findViewById(R.id.cancel_edits_single_item_button);
        itemDetailsLayout = findViewById(R.id.view_single_item_details_layout);
        editItemDetailsLayout = findViewById(R.id.edit_single_item_details_layout);

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
    }

    private void handleEditForm() {
        editButton.setVisibility(View.INVISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.VISIBLE);
        editItemDetailsLayout.setVisibility(View.VISIBLE);
        itemDetailsLayout.setVisibility(View.INVISIBLE);

    }

    private void handleSaveForm() {
        editButton.setVisibility(View.VISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.INVISIBLE);

        editItemDetailsLayout.setVisibility(View.INVISIBLE);
        itemDetailsLayout.setVisibility(View.VISIBLE);
    }
    private void handleCancelForm() {
        editButton.setVisibility(View.VISIBLE);
        parentSaveCancelButtonsLayout.setVisibility(View.INVISIBLE);

        editItemDetailsLayout.setVisibility(View.INVISIBLE);
        itemDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void initAllViewHash() {
        textViewHashMap = new HashMap<>();
        textViewHashMap.put(TextViewKeys.ITEM_NAME, (TextView) findViewById(R.id.single_item_name_text_view));
        textViewHashMap.put(TextViewKeys.MAKE, (TextView) findViewById(R.id.single_item_make_text_view));
        textViewHashMap.put(TextViewKeys.MODEL, (TextView) findViewById(R.id.single_item_model_text_view));
        textViewHashMap.put(TextViewKeys.SERIAL_NUMBER, (TextView) findViewById(R.id.single_item_serial_text_view));
        textViewHashMap.put(TextViewKeys.VALUE, (TextView) findViewById(R.id.single_item_value_text_view));

        editTextViewHashMap = new HashMap<>();
        editTextViewHashMap.put(TextViewKeys.ITEM_NAME, (EditText) findViewById(R.id.edit_single_item_name_text_view));
        editTextViewHashMap.put(TextViewKeys.MAKE, (EditText) findViewById(R.id.edit_single_item_make_text_view));
        editTextViewHashMap.put(TextViewKeys.MODEL, (EditText) findViewById(R.id.edit_single_item_model_text_view));
        editTextViewHashMap.put(TextViewKeys.SERIAL_NUMBER, (EditText) findViewById(R.id.edit_single_item_serial_text_view));
        editTextViewHashMap.put(TextViewKeys.VALUE, (EditText) findViewById(R.id.edit_single_item_value_text_view));
    }
    private void setViewText(HashMap<TextViewKeys, TextView> hashMap, InventoryItem item) {
        Objects.requireNonNull(hashMap.get(TextViewKeys.ITEM_NAME)).setText(item.itemName);
        Objects.requireNonNull(hashMap.get(TextViewKeys.MAKE)).setText(item.make);
        Objects.requireNonNull(hashMap.get(TextViewKeys.MODEL)).setText(item.model);
        Objects.requireNonNull(hashMap.get(TextViewKeys.SERIAL_NUMBER)).setText(item.serialNumber);
        Objects.requireNonNull(hashMap.get(TextViewKeys.VALUE)).setText(String.valueOf(item.value));
    }

    private enum TextViewKeys {
        ITEM_NAME,
        MAKE,
        MODEL,
        SERIAL_NUMBER,
        VALUE
    }

}
