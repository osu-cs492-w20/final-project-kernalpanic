package com.example.inventoryirecord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventoryirecord.data.AzureReceiptAnalyseRepository;
import com.example.inventoryirecord.data.ReceiptResult;

public class ShowReceiptAnalyseViewModel extends ViewModel {
    private AzureReceiptAnalyseRepository mRepository;
    private LiveData<ReceiptResult> mResults;

    public ShowReceiptAnalyseViewModel() {
        mRepository = new AzureReceiptAnalyseRepository();
        mResults = mRepository.getAnalyseResults();
    }

    public void loadAnalyseResults( String filePath) {
        mRepository.loadAnalyseResults(filePath);
    }

    public LiveData<ReceiptResult> getSearchResults() {
        return mResults;
    }

}
