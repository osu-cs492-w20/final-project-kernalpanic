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
        foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "itemID",
                childColumns = "itemID",
                onDelete = CASCADE))
public class ItemPhotos implements Serializable {
    @PrimaryKey
    @NotNull
    public String photoID;
    public String itemID;

    // true for receipt, false for object
    public Boolean isReceipt;
    public String path;

    public List<ItemPhotos> buildItemPhotosFromInventoryItem(InventoryItem item) {
        List<ItemPhotos> itemPhotos = new ArrayList<>();
        for (String s : item.receiptPics) {
            ItemPhotos ip = new ItemPhotos();
            ip.photoID = UUID.randomUUID().toString();
            ip.isReceipt = true;
            ip.itemID = item.itemID;
            ip.path = s;
            itemPhotos.add(ip);
        }

        for (String s : item.itemPics) {
            ItemPhotos ip = new ItemPhotos();
            ip.photoID = UUID.randomUUID().toString();
            ip.isReceipt = false;
            ip.itemID = item.itemID;
            ip.path = s;
            itemPhotos.add(ip);
        }
        return itemPhotos;


    }
}
