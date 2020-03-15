package com.example.inventoryirecord.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class InventorySaveRepository {
    private InventoryItemDao mItemsDAO;
    private InventoryItemPhotosDao mPhotosDAO;


    public InventorySaveRepository(Application application) {
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

    public LiveData<List<ItemPhoto>> getItemReceiptsPhotos(String itemID) {
        return mPhotosDAO.getItemReceiptPhotos(itemID);
    }

    public LiveData<List<ItemPhoto>> getItemObjectPhotos(String itemID) {
        return mPhotosDAO.getItemObjectPhotos(itemID);
    }

    public LiveData<Integer> getReceiptPicturesNum() {
        return mPhotosDAO.getReceiptNumbers();
    }

    public LiveData<Integer> getObjectPicturesNum() {
        return mPhotosDAO.getObjectPhotoNumbers();
    }

    public void deleteInventoryItem(InventoryItem inventoryItem) {
        new DeleteInventoryItemAsyncTask(mItemsDAO, mPhotosDAO).execute(inventoryItem);
    }

    public void updateInventoryItemFields(InventoryItem inventoryItem) {
        new UpdateAsyncTask(mItemsDAO).doInBackground(inventoryItem);
    }

    public void deletePhoto(ItemPhoto itemPhoto) {
        new DeletePhotoAsyncTask(mPhotosDAO).execute(itemPhoto);
    }

    public void insertPhoto(ItemPhoto itemPhoto) {
        new InsertPhotoAsyncTask(mPhotosDAO).execute(itemPhoto);
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
            mPhotosDAO.insert(itemPhotos);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<InventoryItem, Void, Void> {
        private InventoryItemDao mAsyncTaskDAO;

        UpdateAsyncTask(InventoryItemDao inventoryItemDao) {
            mAsyncTaskDAO = inventoryItemDao;
        }

        @Override
        protected Void doInBackground(InventoryItem... inventoryItems) {
            mAsyncTaskDAO.update(inventoryItems[0]);
            return null;
        }
    }

    private static class DeleteInventoryItemAsyncTask extends AsyncTask<InventoryItem, Void, Void> {
        private InventoryItemDao mAsyncTaskDAO;
        private InventoryItemPhotosDao mPhotosDAO;

        DeleteInventoryItemAsyncTask(InventoryItemDao mAsyncTaskDAO, InventoryItemPhotosDao mPhotosDAO) {
            this.mAsyncTaskDAO = mAsyncTaskDAO;
            this.mPhotosDAO = mPhotosDAO;
        }

        @Override
        protected Void doInBackground(InventoryItem... inventoryItems) {
            mAsyncTaskDAO.delete(inventoryItems[0]);
            List<ItemPhoto> itemPhotos = ItemPhoto.buildPhotosFromInventoryItem(inventoryItems[0]);
            mPhotosDAO.deleteAllPhotos(itemPhotos);
            return null;
        }
    }

    private static class DeletePhotoAsyncTask extends AsyncTask<ItemPhoto, Void, Void> {
        private InventoryItemPhotosDao mPhotosDAO;

        DeletePhotoAsyncTask(InventoryItemPhotosDao mPhotosDAO) {
            this.mPhotosDAO = mPhotosDAO;
        }

        @Override
        protected Void doInBackground(ItemPhoto... itemPhotos) {
            mPhotosDAO.delete(itemPhotos[0]);
            return null;
        }
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<ItemPhoto, Void, Void> {
        private InventoryItemPhotosDao mPhotosDAO;

        InsertPhotoAsyncTask(InventoryItemPhotosDao mPhotosDAO) {
            this.mPhotosDAO = mPhotosDAO;
        }

        @Override
        protected Void doInBackground(ItemPhoto... itemPhotos) {
            mPhotosDAO.insertOnePhoto(itemPhotos[0]);
            return null;
        }
    }
}
