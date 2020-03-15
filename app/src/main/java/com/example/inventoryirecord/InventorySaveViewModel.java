package com.example.inventoryirecord;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.InventorySaveRepository;
import com.example.inventoryirecord.data.ItemPhoto;
import com.example.inventoryirecord.photos.BitmapUtils;

import java.util.List;

public class InventorySaveViewModel extends AndroidViewModel {
    private InventorySaveRepository inventoryRepository;

    public InventorySaveViewModel(Application application) {
        super(application);
        inventoryRepository = new InventorySaveRepository(application);
    }


    public LiveData<Integer> getItemCount() {
        return inventoryRepository.getItemNumbers();
    }

    public LiveData<Double> getTotalValue() {
        return inventoryRepository.getTotalValue();
    }

    public LiveData<List<InventoryItem>> getAllItems() {
        return inventoryRepository.getAll();
    }

    public LiveData<Integer> getObjectPictureNumber() {
        return inventoryRepository.getObjectPicturesNum();
    }

    public LiveData<Integer> getReceiptNumber() {
        return inventoryRepository.getReceiptPicturesNum();
    }

    public void insertInventoryItem(InventoryItem inventoryItem) {
        inventoryRepository.insertNewInventoryItem(inventoryItem);
    }

    public void deleteInventoryItem(InventoryItem inventoryItem) {
        for (String location : inventoryItem.receiptPics) {
            BitmapUtils.deleteImageFile(location);
        }
        for (String location : inventoryItem.itemPics) {
            BitmapUtils.deleteImageFile(location);
        }

        inventoryRepository.deleteInventoryItem(inventoryItem);
    }

    public void updateInventoryItemFields(InventoryItem inventoryItem) {
        inventoryRepository.updateInventoryItemFields(inventoryItem);
    }

    public void deleteSinglePhoto(ItemPhoto itemPhoto) {
        inventoryRepository.deletePhoto(itemPhoto);
    }

    public void insertSinglePhoto(ItemPhoto itemPhoto) {
        inventoryRepository.insertPhoto(itemPhoto);
    }

}
