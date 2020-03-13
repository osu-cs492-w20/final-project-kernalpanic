package com.example.inventoryirecord.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "photos",
        foreignKeys = @ForeignKey(entity = InventoryItem.class,
                parentColumns = "itemID",
                childColumns = "itemID",
                onDelete = CASCADE))
public class ItemPhoto implements Serializable {
    @PrimaryKey
    @NotNull
    public String photoID;

    public String itemID;

    // true for receipt, false for object
    public Boolean isReceipt;
    public String path;

    public static List<ItemPhoto> buildPhotosFromInventoryItem(InventoryItem inventoryItem) {
        List<ItemPhoto> itemPhotos = new ArrayList<>();

        for (String path : inventoryItem.itemPics) {
            ItemPhoto photo = new ItemPhoto();
            photo.isReceipt = false;
            photo.itemID = inventoryItem.itemID;
            photo.path = path;
            photo.photoID = UUID.randomUUID().toString();
            itemPhotos.add(photo);
        }

        for (String path : inventoryItem.receiptPics) {
            ItemPhoto photo = new ItemPhoto();
            photo.isReceipt = true;
            photo.itemID = inventoryItem.itemID;
            photo.path = path;
            photo.photoID = UUID.randomUUID().toString();

        }
        return itemPhotos;

    }
}
