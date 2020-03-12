package com.example.inventoryirecord;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.InventorySaveRepository;

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

}
