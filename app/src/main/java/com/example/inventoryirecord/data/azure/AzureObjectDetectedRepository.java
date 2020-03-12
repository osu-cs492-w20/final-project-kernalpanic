package com.example.inventoryirecord.data.azure;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventoryirecord.data.Status;
import com.example.inventoryirecord.utils.AzureUtils;

import java.util.Map;
import java.util.Set;

public class AzureObjectDetectedRepository implements AzureObjectDetectAsyncTask.Callback {
    private static final String TAG = AzureObjectDetectedRepository.class.getSimpleName();
    private MutableLiveData<Set<String>> mObjects;
    private MutableLiveData<String> mBestMatchObject;
    private MutableLiveData<Status> mLoadingStatus;
    private String mCurrentQuery;

    public AzureObjectDetectedRepository() {
        mObjects = new MutableLiveData<>();
        mObjects.setValue(null);

        mBestMatchObject = new MutableLiveData<>();
        mBestMatchObject.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(null);
        mCurrentQuery = null;
    }

    @Override
    public void onDetectedFinished(Set<String> resultSet, String bestResult) {
        mObjects.setValue(resultSet);
        mBestMatchObject.setValue(bestResult);
        if (mObjects != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    public LiveData<String> getBestMatchObject() {
        return mBestMatchObject;

    }

    public LiveData<Set<String>> getObjectList() {
        return mObjects;
    }


    private boolean shouldExecuteAnalyse(String query) {
        return !TextUtils.equals(query, mCurrentQuery);
    }

    public void loadDetectObjects(String filePath) {
        if (shouldExecuteAnalyse(filePath)) {
            mCurrentQuery = filePath;
            Map.Entry<String, String> entry = (AzureUtils.getURLKeyMap(false)).entrySet().iterator().next();
            String subscriptionKey = entry.getValue();
            String url = entry.getKey();
            mBestMatchObject.setValue(null);
            mObjects.setValue(null);
            Log.d(TAG, "executing Analyse with url: " + url + "and file path in:" + filePath);
            mLoadingStatus.setValue(Status.LOADING);
            new AzureObjectDetectAsyncTask(this).execute(filePath, url, subscriptionKey);
        } else {
            Log.d(TAG, "using cached Azure Object Detection results");
        }
    }

}
