package com.example.inventoryirecord.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "items")
public class Item implements Serializable {
    @PrimaryKey
    @NonNull
    public String itemID;

    public String itemName;
    public String itemType;
    public String dateAdded;
    public String datePurchased;
    public String make;
    public String model;
    public String serialNumber;
    public String dateOfManufacture;
    public String otherNotes;
    public boolean newItem;
    public double pricePaid;
    public double value;

    public Item() {
        super();
    }

    public Item(InventoryItem inventoryItem) {
        this.itemName = inventoryItem.itemName;
        this.itemType = inventoryItem.itemType;
        this.itemID = inventoryItem.itemID;
        this.dateAdded = inventoryItem.dateAdded;
        this.datePurchased = inventoryItem.datePurchased;
        this.make = inventoryItem.make;
        this.model = inventoryItem.model;
        this.serialNumber = inventoryItem.serialNumber;
        this.dateOfManufacture = inventoryItem.dateOfManufacture;
        this.otherNotes = inventoryItem.otherNotes;
        this.newItem = inventoryItem.newItem;
        this.pricePaid = inventoryItem.pricePaid;
        this.value = inventoryItem.value;
    }

}
