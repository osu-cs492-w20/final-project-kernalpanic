package com.example.inventoryirecord.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class InventorySavedRepository {
    private InventoryItemDao mItemsDAO;
    private InventoryItemPhotosDao mPhotosDAO;


    public InventorySavedRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mItemsDAO = db.inventoryItemDao();
        mPhotosDAO = db.inventoryItemPhotosDao();
    }


    public void insertNewInventoryItem(InventoryItem inventoryItem) {
        new InsertAsyncTask(mItemsDAO, mPhotosDAO).execute(inventoryItem);
    }


    public LiveData<Integer> getItemNumbers() {
        return mItemsDAO.getTotalItemsNumber();
    }

    public LiveData<Double> getTotalValue() {
        return mItemsDAO.getTotalValue();
    }

    public LiveData<List<InventoryItem>> getAll(){
        return mItemsDAO.getAll();
    }

    public LiveData<Integer> getReceiptPicturesNum() {
        return mPhotosDAO.getReceiptNumbers();
    }

    public LiveData<Integer> getObjectPicturesNum() {
        return mPhotosDAO.getObjectPhotoNumbers();
    }

    private static class InsertAsyncTask extends AsyncTask<InventoryItem, Void, Void> {
        private InventoryItemDao mAsyncTaskDAO;
        private InventoryItemPhotosDao mPhotosDAO;

        InsertAsyncTask(InventoryItemDao dao, InventoryItemPhotosDao pDao) {
            mAsyncTaskDAO = dao;
            mPhotosDAO = pDao;
        }

        @Override
        protected Void doInBackground(InventoryItem... inventoryItems) {
            mAsyncTaskDAO.insert(inventoryItems[0]);
            List<ItemPhoto> itemPhotos = ItemPhoto.buildPhotosFromInventoryItem(inventoryItems[0]);
            mPhotosDAO.insert((ItemPhoto) itemPhotos);
            return null;
        }
    }
}
