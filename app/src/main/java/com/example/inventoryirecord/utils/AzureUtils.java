package com.example.inventoryirecord.utils;

import android.net.Uri;

import com.example.inventoryirecord.data.ReceiptResult;
import com.google.gson.Gson;

import java.util.List;

// Work in progress. Finish camera first.
public class AzureUtils {
    private final static String OBJ__BASE_URL = "https://stuff.cognitiveservices.azure.com/vision/v2.0/analyze?visualFeatures=Description";
    private final static String RECEIPT_BASE_URL = "https://recieptcv.cognitiveservices.azure.com/formrecognizer/v1.0-preview/prebuilt/receipt/asyncBatchAnalyze";
    private final static String TXT_BASE_URL = "https://stuff.cognitiveservices.azure.com/vision/v2.0/recognizeText?mode=Printed";
    private final static String TYPE_JSON = "application/json";
    private final static String TYPE_BINARY = "application/octet-stream";

    static class AnalyseResult {
        String status;
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

        //TODO add other status.

        if (result.status.equals("Succeeded")) {
            receiptResult = result.understandingResults.get(0).fields;
        }

        return receiptResult;
    }


}
