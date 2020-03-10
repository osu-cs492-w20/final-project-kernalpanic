package com.example.inventoryirecord.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class, ItemPhotos.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract InventoryItemDao inventoryItemDao();
    public abstract InventoryItemPhotosDao inventoryItemPhotosDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "inventory_items_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
