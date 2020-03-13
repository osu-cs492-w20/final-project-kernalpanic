package com.example.inventoryirecord.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InventoryItemDao {

    @Insert
    void insert(InventoryItem inventoryItem);

    @Delete
    void delete(InventoryItem inventoryItem);

    @Update
    void update(InventoryItem inventoryItem);

    @Query("SELECT * FROM items where itemID=:itemID LIMIT 1")
    LiveData<InventoryItem> getItemByID(String itemID);

    @Query("SELECT SUM(value) FROM items")
    LiveData<Double> getTotalValue();

    @Query("SELECT * FROM items")
    LiveData<List<InventoryItem>> getAll();

    @Query("SELECT COUNT(*) FROM items")
    LiveData<Integer> getTotalItemsNumber();
}
