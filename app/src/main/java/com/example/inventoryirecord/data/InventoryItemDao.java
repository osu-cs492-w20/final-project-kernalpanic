package com.example.inventoryirecord.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
}
