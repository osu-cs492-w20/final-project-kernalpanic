package com.example.inventoryirecord;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class addItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_screen);

    }

}
