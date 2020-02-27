package com.example.inventoryirecord.data;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventoryirecord.utils.AzureUtils;

public class AzureReceiptAnalyseRepository implements AzureReceiptAnalyseAsyncTask.Callback {
    private static final String TAG = AzureReceiptAnalyseRepository.class.getSimpleName();
    private MutableLiveData<ReceiptResult> mAnalyseResults;
    private MutableLiveData<Status> mLoadingStatus;
    private String mCurrentQuery;


    public AzureReceiptAnalyseRepository() {
        mAnalyseResults = new MutableLiveData<>();
        mAnalyseResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCurrentQuery = null;
    }

    public LiveData<ReceiptResult> getAnalyseResults() {
        return mAnalyseResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onAnalyseFinished(ReceiptResult receiptResult) {
        mAnalyseResults.setValue(receiptResult);
        if (mAnalyseResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteAnalyse(String query) {
        return !TextUtils.equals(query, mCurrentQuery);
    }

    public void loadAnalyseResults(String type, String obj) {
        if (shouldExecuteAnalyse(type + obj)) {
            mCurrentQuery = type + obj;
            String url = AzureUtils.getReceiptAnalysePOSTURL();
            mAnalyseResults.setValue(null);
            Log.d(TAG, "executing Analyse with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new AzureReceiptAnalyseAsyncTask(this).execute(type, obj, url);
        } else {
            Log.d(TAG, "using cached Analyse results");
        }
    }
}
