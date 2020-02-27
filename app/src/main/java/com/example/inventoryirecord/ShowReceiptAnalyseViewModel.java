package com.example.inventoryirecord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventoryirecord.data.AzureReceiptAnalyseRepository;
import com.example.inventoryirecord.data.ReceiptResult;
import com.example.inventoryirecord.data.Status;


public class ShowReceiptAnalyseViewModel extends ViewModel {
    private AzureReceiptAnalyseRepository mRepository;
    private LiveData<ReceiptResult> mResults;
    private LiveData<Status> mLoadingStatus;

    public ShowReceiptAnalyseViewModel() {
        mRepository = new AzureReceiptAnalyseRepository();
        mResults = mRepository.getAnalyseResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadAnalyseResults(String type, String obj) {
        mRepository.loadAnalyseResults(type, obj);
    }

    public LiveData<ReceiptResult> getSearchResults() {
        return mResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
