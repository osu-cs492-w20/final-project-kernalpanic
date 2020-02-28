package com.example.inventoryirecord.utils;

import android.net.Uri;

import com.example.inventoryirecord.data.AnalyzeResultStatus;
import com.example.inventoryirecord.data.ReceiptResult;
import com.google.gson.Gson;

import java.util.List;

public class AzureUtils {
    private final static String RECEIPT_BASE_URL = "https://recieptcv.cognitiveservices.azure.com/formrecognizer/v1.0-preview/prebuilt/receipt/asyncBatchAnalyze";

    static class AnalyseResult {
        AnalyzeResultStatus status;
        List<UnderstandingResults> understandingResults;
    }

    static class UnderstandingResults {
        Integer[] pages;
        ReceiptResult fields;

    }

    public static String getReceiptAnalysePOSTURL() {
        return RECEIPT_BASE_URL;
    }

    public static ReceiptResult parseReceiptJSON(String receiptJson) {
        Gson gson = new Gson();

        AnalyseResult result = gson.fromJson(receiptJson, AnalyseResult.class);
        ReceiptResult receiptResult = new ReceiptResult();

        if (result.status.equals(AnalyzeResultStatus.Succeeded)) {
            receiptResult = result.understandingResults.get(0).fields;
        }

        return receiptResult;
    }


}
