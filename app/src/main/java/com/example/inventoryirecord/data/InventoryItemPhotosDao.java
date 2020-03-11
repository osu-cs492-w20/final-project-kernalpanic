package com.example.inventoryirecord.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InventoryItemPhotosDao {

    @Insert
    void insert(ItemPhotos itemPhotos);

    @Delete
    void delete(ItemPhotos itemPhotos);

    @Query("SELECT * FROM photos where itemID=:itemID and isReceipt=0")
    List<ItemPhotos> getItemReceiptPhotos(String itemID);

    @Query("SELECT * FROM photos where itemID=:itemID and isReceipt=1")
    List<ItemPhotos> getItemObjectPhotos(String itemID);

    @Query("SELECT * FROM photos where itemID=:itemID")
    List<ItemPhotos> getItemPhotos(String itemID);
}
