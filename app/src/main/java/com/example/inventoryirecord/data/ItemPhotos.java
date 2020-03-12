package com.example.inventoryirecord.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "photos",
        foreignKeys = @ForeignKey(entity = InventoryItem.class,
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
}
