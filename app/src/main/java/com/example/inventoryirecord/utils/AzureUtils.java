package com.example.inventoryirecord.utils;

import com.example.inventoryirecord.data.azure.AnalyzeResultStatus;
import com.example.inventoryirecord.data.azure.ReceiptResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AzureUtils {
    private final static String RECEIPT_BASE_URL = "https://recieptcv.cognitiveservices.azure.com/formrecognizer/v1.0-preview/prebuilt/receipt/asyncBatchAnalyze";
    private final static String RECEIPT_KEY = "9219690c24d24d20964e07c27cfe1b67";

    private final static String OBJECT_BASE_URL = "https://cv492.cognitiveservices.azure.com/vision/v2.0/detect";
    private final static String OBJECT_KEY = "bf3dda215d39406da5489a5006ebfba8";

    static class AnalyseResult {
        AnalyzeResultStatus status;
        List<UnderstandingResults> understandingResults;
    }

    static class UnderstandingResults {
        Integer[] pages;
        ReceiptResult fields;

    }

    static class ObjectDetectedResult {
        List<DetectedObject> objects;
    }

    static class DetectedObject {
        String object;
        Double confidence;
    }

    public static Map<String, String> getURLKeyMap(boolean isReceipt) {
        Map<String, String> result = new HashMap<>();
        if (isReceipt) {
            result.put(RECEIPT_BASE_URL, RECEIPT_KEY);
        } else {
            result.put(OBJECT_BASE_URL, OBJECT_KEY);
        }
        return result;
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

    /**
     * @param objectsJson result json
     * @param threshold   threshold of (confidence for each object), return results with confidence larger than threshold
     * @return a hash map with object name as string and confidence as value.
     */
    public static Map<String, Double> parseObjectsJSON(String objectsJson, double threshold) {
        Gson gson = new Gson();
        Map<String, Double> resultMap = new HashMap<>();

        ObjectDetectedResult result = gson.fromJson(objectsJson, ObjectDetectedResult.class);
        if(result==null){
            return null;
        }
        for (DetectedObject obj : result.objects) {
            String key = obj.object;
            Double value = obj.confidence;
            if (resultMap.containsKey(key)) {
                value = Math.max(value, resultMap.get(key));
            }
            resultMap.put(key, Math.max(obj.confidence, value));

        }
        return resultMap;

    }


}
