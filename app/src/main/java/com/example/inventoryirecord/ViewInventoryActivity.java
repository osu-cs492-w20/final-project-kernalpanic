package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.adapters.ViewItemAdapter;
import com.example.inventoryirecord.data.InventoryItem;

import java.util.List;

public class ViewInventoryActivity extends AppCompatActivity implements ViewItemAdapter.OnInventoryItemClickListener {
    public final static String TAG = ViewInventoryActivity.class.getSimpleName();
    private ViewItemAdapter viewItemAdapter;
    private RecyclerView inventoryItemsRecyclerView;
    private InventoryViewModel inventoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_inventory_activity);
        inventoryItemsRecyclerView = findViewById(R.id.inventory_rec_view);
        inventoryItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryItemsRecyclerView.setHasFixedSize(true);

        viewItemAdapter = new ViewItemAdapter(this);
        inventoryItemsRecyclerView.setAdapter(viewItemAdapter);

        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        inventoryViewModel.getInventoryItemList().observe(this, new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                viewItemAdapter.updateInventoryItems(inventoryItems);
            }
        });
    }

    @Override
    public void onInventoryItemClicked(InventoryItem item) {
        Log.d(TAG, "item Clicked"+ item.itemName);
        Intent itemDetailsIntent = new Intent(this, ViewSingleItemDetailsActivity.class);
        itemDetailsIntent.putExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM, item);
        startActivity(itemDetailsIntent);
    }
}
