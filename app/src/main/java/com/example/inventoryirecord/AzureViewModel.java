package com.example.inventoryirecord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventoryirecord.data.azure.AzureObjectDetectedRepository;
import com.example.inventoryirecord.data.azure.AzureReceiptAnalyseRepository;
import com.example.inventoryirecord.data.azure.ReceiptResult;

import java.util.Set;

public class AzureViewModel extends ViewModel {
    private AzureReceiptAnalyseRepository mARARepository;
    private LiveData<ReceiptResult> mARAResults;
    private AzureObjectDetectedRepository mAODRepository;
    private LiveData<Set<String>> mObjects;
    private LiveData<String> mBestMatchObject;

    public AzureViewModel() {
        mARARepository = new AzureReceiptAnalyseRepository();
        mARAResults = mARARepository.getAnalyseResults();
        mAODRepository = new AzureObjectDetectedRepository();
        mBestMatchObject = mAODRepository.getBestMatchObject();
        mObjects = mAODRepository.getObjectList();
    }

    public void loadAnalyseResults(String filePath) {
        mARARepository.loadAnalyseResults(filePath);
    }

    public LiveData<ReceiptResult> getSearchResults() {
        return mARAResults;
    }

    public void loadDetectObjects(String filePath) {
        mAODRepository.loadDetectObjects(filePath);
    }

    public LiveData<String> getBestMatchObject() {
        return mBestMatchObject;
    }

    public LiveData<Set<String>> getMatchObjects() {
        return mObjects;
    }

}
