package com.example.inventoryirecord.data;

import android.app.Application;

public class ItemPhotosRepository {
    private InventoryItemPhotosDao mPhotosDAO;


    public ItemPhotosRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPhotosDAO = db.inventoryItemPhotosDao();

    }
}
