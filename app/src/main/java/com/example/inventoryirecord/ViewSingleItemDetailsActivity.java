package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryirecord.data.InventoryItem;

import java.util.Locale;
import java.util.Objects;

public class ViewSingleItemDetailsActivity extends AppCompatActivity {
    public static final String INVENTORY_ITEM = "singleInventoryItem";
    public static final String TAG = ViewSingleItemDetailsActivity.class.getSimpleName();

    private TextView itemNameTextView;
    private TextView itemMakeTextView;
    private TextView itemModelTextView;
    private TextView itemSerialTextView;
    private TextView itemValueTextView;
    private LinearLayout editButton;
    private LinearLayout parentSaveCancelButtonsLayout;
    private LinearLayout saveButton;
    private LinearLayout cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_item_details_activity);

        Intent intent = getIntent();
        if(Objects.nonNull(intent) && intent.hasExtra(INVENTORY_ITEM)) {
            Log.d(TAG, "has extra");
            InventoryItem inventoryItem = (InventoryItem) intent.getSerializableExtra(INVENTORY_ITEM);
            itemNameTextView = findViewById(R.id.single_item_name_text_view);
            itemNameTextView.setText(String.format("Name: %s", inventoryItem != null ? inventoryItem.itemName : ""));
            itemMakeTextView = findViewById(R.id.single_item_make_text_view);
            itemMakeTextView.setText(String.format("Make: %s", inventoryItem != null ? inventoryItem.make : ""));
            itemModelTextView = findViewById(R.id.single_item_model_text_view);
            itemModelTextView.setText(String.format("Model: %s", inventoryItem != null ? inventoryItem.model : ""));
            itemSerialTextView = findViewById(R.id.single_item_serial_text_view);
            itemSerialTextView.setText(String.format("Serial Number: %s", inventoryItem != null ? inventoryItem.serialNumber : ""));
            itemValueTextView = findViewById(R.id.single_item_value_text_view);
            itemValueTextView.setText(String.format(Locale.US, "Value: $%.2f", inventoryItem != null ? inventoryItem.value : 0.00));
        }
        editButton = findViewById(R.id.edit_single_item_button);
        parentSaveCancelButtonsLayout = findViewById(R.id.save_cancel_button_layout);
        saveButton = findViewById(R.id.save_edits_single_item_button);
        cancelButton = findViewById(R.id.cancel_edits_single_item_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditForm();
            }
        });
    }

    private void handleEditForm() {

    }
}
