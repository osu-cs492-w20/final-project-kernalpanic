package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryirecord.data.InventoryItem;

import java.util.Locale;
import java.util.Objects;

public class ViewSingleItemDetailsActivity extends AppCompatActivity {
    public static final String INVENTORY_ITEM = "singleInventoryItem";
    public static final String TAG = ViewSingleItemDetailsActivity.class.getSimpleName();
    private InventoryItem inventoryItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_item_details_activity);

        Intent intent = getIntent();
        if(Objects.nonNull(intent) && intent.hasExtra(INVENTORY_ITEM)) {
            Log.d(TAG, "has extra");
            inventoryItem = (InventoryItem) intent.getSerializableExtra(INVENTORY_ITEM);
            TextView itemNameTextView = findViewById(R.id.single_item_name_text_view);
            itemNameTextView.setText(String.format("Name: %s", inventoryItem.itemName));
            TextView itemMakeTextView = findViewById(R.id.single_item_make_text_view);
            itemMakeTextView.setText(String.format("Make: %s", inventoryItem.make));
            TextView itemModelTextView = findViewById(R.id.single_item_model_text_view);
            itemModelTextView.setText(String.format("Model: %s", inventoryItem.model));
            TextView itemSerialTextView = findViewById(R.id.single_item_serial_text_view);
            itemSerialTextView.setText(String.format("Serial Number: %s", inventoryItem.serialNumber));
            TextView itemValueTextView = findViewById(R.id.single_item_value_text_view);
            itemValueTextView.setText(String.format(Locale.US, "Value: $%.2f", inventoryItem.value));
        }
    }
}
