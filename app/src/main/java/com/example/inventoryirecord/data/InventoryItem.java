package com.example.inventoryirecord.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class InventoryItem implements Serializable {
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
    //image location??
    public ArrayList<String> itemPics;
    public ArrayList<String> receiptPics;

    //to use the builder : InventoryItem inventoryItem =
    // InventoryItem.Builder.newInstance().setItemName("name").build();

    public InventoryItem(Builder builder) {
        this.itemName = builder.itemName;
        this.itemType = builder.itemType;
        this.dateAdded = builder.dateAdded;
        this.datePurchased = builder.datePurchased;
        this.make = builder.make;
        this.model = builder.model;
        this.serialNumber = builder.serialNumber;
        this.dateOfManufacture = builder.dateOfManufacture;
        this.otherNotes = builder.otherNotes;
        this.newItem = builder.newItem;
        this.pricePaid = builder.pricePaid;
        this.value = builder.value;
        this.itemPics = builder.itemPics;
        this.receiptPics = builder.receiptPics;
        this.itemID = UUID.randomUUID().toString();
    }

    public static class Builder {
        private String itemName;
        private String itemType;
        private String dateAdded;
        private String datePurchased;
        private String make;
        private String model;
        private String serialNumber;
        private String dateOfManufacture;
        private String otherNotes;
        private boolean newItem;
        private double pricePaid;
        private double value;
        //image location??
        private ArrayList<String> itemPics;
        private ArrayList<String> receiptPics;

        public static Builder newInstance() {
            return new Builder();
        }
        private Builder() {
        }

        public InventoryItem build(){
            return new InventoryItem(this);
        }

        public Builder setItemName(String name) {
            this.itemName = name;
            return this;
        }

        public Builder setItemType(String type) {
            this.itemType = type;
            return this;
        }

        public Builder setDateAdded(String dateAdd) {
            this.dateAdded = dateAdd;
            return this;
        }

        public Builder setDatePurchased(String dateOfPurchase) {
            this.datePurchased = dateOfPurchase;
            return this;
        }

        public Builder setMake(String brandName) {
            this.make = brandName;
            return this;
        }

        public Builder setModel(String itemModel) {
            this.model = itemModel;
            return this;
        }

        public Builder setSerialNumber(String sn) {
            this.serialNumber = sn;
            return this;
        }

        public Builder setDateOfManufacture(String dateMade) {
            this.dateOfManufacture = dateMade;
            return this;
        }

        public Builder setNewItem(boolean itemNew) {
            this.newItem = itemNew;
            return this;
        }

        public Builder setOtherNotes(String notes) {
            this.otherNotes = notes;
            return this;
        }

        public Builder setPricePaid(Double paid) {
            this.pricePaid = paid;
            return this;
        }

        public Builder setValue(Double value) {
            this.value = value;
            return this;
        }

        public Builder setItemPics(ArrayList<String> pics) {
            this.itemPics = pics;
            return this;
        }

        public Builder setReceiptPics(ArrayList<String> receipts) {
            this.receiptPics = receipts;
            return this;
        }
    }
}
