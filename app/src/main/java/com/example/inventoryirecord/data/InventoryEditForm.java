package com.example.inventoryirecord.data;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventoryirecord.R;
import com.example.inventoryirecord.ViewSingleItemDetailsActivity;

import java.util.HashMap;
import java.util.Objects;

import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.DATE_PURCH;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.ITEM_NAME;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.ITEM_TYPE;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.MAKE;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.MODEL;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.NEW;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.PRICE_PAID;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.SERIAL_NUMBER;
import static com.example.inventoryirecord.ViewSingleItemDetailsActivity.TextViewKeys.VALUE;

public class InventoryEditForm extends View {
    public HashMap<ViewSingleItemDetailsActivity.TextViewKeys, TextView> getEditTextViewHashMap() {
        return editTextViewHashMap;
    }

    private HashMap<ViewSingleItemDetailsActivity.TextViewKeys, TextView> editTextViewHashMap;
    private CheckBox newCheckBox;


    public InventoryEditForm(Context context) {
        super(context);
        this.editTextViewHashMap = initHash();
        this.newCheckBox = findViewById(NEW.editId);
    }

    public InventoryItem getEditText() {
        return InventoryItem.Builder.newInstance()
                .setItemName(Objects.requireNonNull(editTextViewHashMap.get(ITEM_NAME)).getText().toString())
                .setItemType(Objects.requireNonNull(editTextViewHashMap.get(ITEM_TYPE)).getText().toString())
                .setMake(Objects.requireNonNull(editTextViewHashMap.get(MAKE)).getText().toString())
                .setModel(Objects.requireNonNull(editTextViewHashMap.get(MODEL)).getText().toString())
                .setSerialNumber(Objects.requireNonNull(editTextViewHashMap.get(SERIAL_NUMBER)).getText().toString())
                .setValue(Double.parseDouble(Objects.requireNonNull(editTextViewHashMap.get(VALUE)).getText().toString()))
                .setDatePurchased(Objects.requireNonNull(editTextViewHashMap.get(DATE_PURCH)).getText().toString())
                .setPricePaid(Double.parseDouble(Objects.requireNonNull(editTextViewHashMap.get(PRICE_PAID)).getText().toString()))
                .setNewItem(newCheckBox.isChecked())
                .build();
    }

    private HashMap<ViewSingleItemDetailsActivity.TextViewKeys, TextView> initHash() {
        HashMap<ViewSingleItemDetailsActivity.TextViewKeys, TextView> hashMap = new HashMap<>();
        hashMap.put(ITEM_NAME, (EditText) findViewById(R.id.edit_single_item_name_text_view));
        hashMap.put(ITEM_TYPE, (EditText) findViewById(ITEM_TYPE.editId));
        hashMap.put(MAKE, (EditText) findViewById(MAKE.editId));
        hashMap.put(MODEL, (EditText) findViewById(MODEL.editId));
        hashMap.put(SERIAL_NUMBER, (EditText) findViewById(SERIAL_NUMBER.editId));
        hashMap.put(VALUE, (EditText) findViewById(VALUE.editId));
        hashMap.put(DATE_PURCH, (EditText) findViewById(DATE_PURCH.editId));
        hashMap.put(PRICE_PAID, (EditText) findViewById(PRICE_PAID.editId));

        return hashMap;
    }

}
