package com.example.inventoryirecord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventoryirecord.data.DummyInventory;
import com.example.inventoryirecord.data.InventoryItem;

import java.util.List;

public class InventoryViewModel extends ViewModel {
    private List<InventoryItem> inventoryItemList;
    private MutableLiveData<List<InventoryItem>> listMutableLiveData;

    public InventoryViewModel() {
        this.inventoryItemList = DummyInventory.generateDummyInventory(15);
        this.listMutableLiveData = new MutableLiveData<>();
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
