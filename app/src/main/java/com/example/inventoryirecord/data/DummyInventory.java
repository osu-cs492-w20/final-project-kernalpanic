package com.example.inventoryirecord.data;

import java.util.ArrayList;

public class DummyInventory {

    private static final String ITEM_NAME = "Name";
    private static final String ITEM_MAKE = "Make";
    private static final String ITEM_MODEL = "Model";
    private static final String ITEM_SN = "Serial Number";
    private static final String ITEM_DATE_ADD = "Date Added";
    private static final String ITEM_DATE_PURCH = "Date of Purchase";
    private static final String ITEM_DATE_OF_MANUFACTURE = "Date of Manufacture";
    private static final String ITEM_NOTES = "Notes";
    private static final boolean ITEM_NEW = true;
    private static final double ITEM_PRICE_PAID = 124.00;
    private static final double ITEM_VALUE = 33.00;



    public static ArrayList<InventoryItem> generateDummyInventory(int number) {
        ArrayList<InventoryItem> inventoryItemArrayList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            inventoryItemArrayList.add(InventoryItem.Builder.newInstance()
                    .setItemName(ITEM_NAME + i)
                    .setMake(ITEM_MAKE + i)
                    .setModel(ITEM_MODEL + i)
                    .setSerialNumber(ITEM_SN + i)
                    .setDateAdded(ITEM_DATE_ADD + i)
                    .setDatePurchased(ITEM_DATE_PURCH + i)
                    .setDateOfManufacture(ITEM_DATE_OF_MANUFACTURE + i)
                    .setOtherNotes(ITEM_NOTES + i)
                    .setNewItem(ITEM_NEW)
                    .setPricePaid(ITEM_PRICE_PAID + i)
                    .setValue(ITEM_VALUE + i)
                    .build());
        }
        return inventoryItemArrayList;
    }
}
