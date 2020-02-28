package com.example.inventoryirecord.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class InventoryRepository {
    private static InventoryRepository REPOSITORY_INSTANCE;
    private List<InventoryItem> inventoryItemList;
    private MutableLiveData<List<InventoryItem>> listMutableLiveData;

    private InventoryRepository() {
        this.inventoryItemList = DummyInventory.generateDummyInventory(15);
        this.listMutableLiveData = new MutableLiveData<>();
    };

    public static InventoryRepository getInstance() {
        if(REPOSITORY_INSTANCE == null) {
            REPOSITORY_INSTANCE = new InventoryRepository();
        }
        return REPOSITORY_INSTANCE;
    }

    public LiveData<List<InventoryItem>> getInventoryItemList() {
        listMutableLiveData.setValue(inventoryItemList);
        return listMutableLiveData;
    }

    public void addSingleInventoryItem(InventoryItem addItem) {
        inventoryItemList.add(addItem);
    }

    public void updateSingleInventoryItem(InventoryItem updateItem) {
        for (InventoryItem item: inventoryItemList) {
            if(item.itemID.equals(updateItem.itemID)) {
                inventoryItemList.set(inventoryItemList.indexOf(item), updateItem);
                break;
            }
        }
    }

    public void deleteSingleInventoryItem(InventoryItem deleteItem) {
        for (InventoryItem item: inventoryItemList) {
            if(item.itemID.equals(deleteItem.itemID)) {
                inventoryItemList.remove(item);
                break;
            }
        }
    }
}
