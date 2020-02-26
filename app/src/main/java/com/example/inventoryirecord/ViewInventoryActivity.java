package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.adapters.ViewItemAdapter;
import com.example.inventoryirecord.data.DummyInventory;
import com.example.inventoryirecord.data.InventoryItem;

import java.util.ArrayList;

public class ViewInventoryActivity extends AppCompatActivity implements ViewItemAdapter.OnInventoryItemClickListener {
    public final static String TAG = ViewInventoryActivity.class.getSimpleName();

    private ArrayList<InventoryItem> inventoryItems;
    private ViewItemAdapter viewItemAdapter;
    private RecyclerView inventoryItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_inventory_activity);

        inventoryItemsRecyclerView = findViewById(R.id.inventory_rec_view);
        inventoryItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryItemsRecyclerView.setHasFixedSize(true);

        viewItemAdapter = new ViewItemAdapter(this);
        inventoryItemsRecyclerView.setAdapter(viewItemAdapter);
        inventoryItems = DummyInventory.generateDummyInventory(12);
        viewItemAdapter.updateInventoryItems(inventoryItems);
    }

    @Override
    public void onInventoryItemClicked(InventoryItem item) {
        Log.d(TAG, "item Clicked"+ item.itemName);
        Intent itemDetailsIntent = new Intent(this, ViewSingleItemDetailsActivity.class);
        itemDetailsIntent.putExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM, item);
        startActivity(itemDetailsIntent);
    }
}
