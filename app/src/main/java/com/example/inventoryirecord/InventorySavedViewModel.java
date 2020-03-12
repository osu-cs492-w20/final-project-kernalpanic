package com.example.inventoryirecord;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.InventoryRepository;
import com.example.inventoryirecord.data.InventorySavedRepository;

import java.util.List;

public class InventorySavedViewModel extends AndroidViewModel {
    private InventorySavedRepository inventoryRepository;

    public InventorySavedViewModel(Application application) {
        super(application);
        inventoryRepository = new InventorySavedRepository(application);
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

}
