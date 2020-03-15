package com.example.inventoryirecord.data;

import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InventoryItemPhotosDao {

    @Insert
    void insert(List<ItemPhoto> itemPhotos);

    @Insert
    void insertOnePhoto(ItemPhoto itemPhoto);

    @Delete
    void delete(ItemPhoto itemPhoto);

    @Delete
    void deleteAllPhotos(List<ItemPhoto> itemPhotos);

    @Query("SELECT * FROM photos where itemID=:itemID and isReceipt=1")
    LiveData<List<ItemPhoto>> getItemReceiptPhotos(String itemID);

    @Query("SELECT * FROM photos where itemID=:itemID and isReceipt=0")
    LiveData<List<ItemPhoto>> getItemObjectPhotos(String itemID);

    @Query("SELECT * FROM photos where itemID=:itemID")
    LiveData<List<ItemPhoto>> getItemPhotos(String itemID);

    @Query("SELECT COUNT(*) FROM photos where isReceipt=1")
    LiveData<Integer> getReceiptNumbers();

    @Query("SELECT COUNT(*) FROM photos where isReceipt=0")
    LiveData<Integer> getObjectPhotoNumbers();
}
