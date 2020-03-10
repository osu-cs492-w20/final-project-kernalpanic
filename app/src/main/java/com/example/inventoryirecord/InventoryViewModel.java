package com.example.inventoryirecord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.InventoryRepository;

import java.util.List;

public class InventoryViewModel extends ViewModel {
    private LiveData<List<InventoryItem>> listLiveData;
    private InventoryRepository inventoryRepository;

    public InventoryViewModel() {
        this.inventoryRepository = InventoryRepository.getInstance();
        this.listLiveData = inventoryRepository.getInventoryItemList();
    }


    public LiveData<List<InventoryItem>> getInventoryItemList() {
        //listMutableLiveData.setValue(inventoryItemList);
        return listLiveData;
    }

    public void addSingleInventoryItem(InventoryItem addItem) {
        inventoryRepository.addSingleInventoryItem(addItem);
    }

    public void updateSingleInventoryItem(InventoryItem updateItem) {
        inventoryRepository.updateSingleInventoryItem(updateItem);
    }

    public void deleteSingleInventoryItem(InventoryItem deleteItem) {
        inventoryRepository.deleteSingleInventoryItem(deleteItem);
    }

}
