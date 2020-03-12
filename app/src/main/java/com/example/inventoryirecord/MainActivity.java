package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryirecord.data.InventoryItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private TextView mStoredItemNumberTV;
    private TextView mItemTotalValueTV;
    private TextView mTotalReceiptNumberTV;
    private TextView mTotalPictureNumbersTV;
    private InventorySaveViewModel inventoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button viewInventoryButton = findViewById(R.id.btn_view_edit_inventory);
        intent = new Intent(this, ViewInventoryActivity.class);
        viewInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        mStoredItemNumberTV = findViewById(R.id.tv_item_stored_number);
        mItemTotalValueTV = findViewById(R.id.tv_item_total_value);
        mTotalPictureNumbersTV = findViewById(R.id.tv_pictures_number);
        mTotalReceiptNumberTV = findViewById(R.id.tv_receipts_number);
        // Button for adding an item.
        final Button addItemButton = findViewById(R.id.btn_add_inventory);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddItemScreen();
            }
        });

        inventoryViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(InventorySaveViewModel.class);

        inventoryViewModel.getObjectPictureNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String nop = getString(R.string.number_of_pictures, integer);
                mTotalPictureNumbersTV.setText(nop);
            }
        });

        inventoryViewModel.getReceiptNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String nor = getString(R.string.numbers_of_receipts, integer);
                mTotalReceiptNumberTV.setText(nor);
            }
        });

        inventoryViewModel.getAllItems().observe(this, new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                double d = 0.0;
                int count = 0;

                for (InventoryItem item : inventoryItems) {
                    d += item.value;
                    count++;
                }

                String totalItem = getString(R.string.number_of_item_stored, count);
                mStoredItemNumberTV.setText(totalItem);

                String totalValue = getString(R.string.total_value_of_item, d);
                mItemTotalValueTV.setText(totalValue);

            }
        });

    }

    private void goToAddItemScreen() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void acquirePermission() {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }
}
