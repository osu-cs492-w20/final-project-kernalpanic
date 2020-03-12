package com.example.inventoryirecord.data.azure;

import android.os.AsyncTask;
import android.util.Log;

import com.example.inventoryirecord.utils.AzureUtils;
import com.example.inventoryirecord.utils.NetworkUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Response;

public class AzureObjectDetectAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = AzureObjectDetectAsyncTask.class.getSimpleName();
    private AzureObjectDetectAsyncTask.Callback mCallback;

    public interface Callback {
        void onDetectedFinished(Set<String> resultSet, String bestResult);
    }

    public AzureObjectDetectAsyncTask(AzureObjectDetectAsyncTask.Callback callback) {
        mCallback = callback;
    }


    @Override
    protected String doInBackground(String... strings) {
        String filePath = strings[0];
        String url = strings[1];
        String key = strings[2];
        String analyseResults = null;
        try {

            analyseResults = NetworkUtils.doHttpPostObj(filePath, url, key);

            int n = 0;
            while (analyseResults == null && n <= 5) {
                /// analyse failed
                if (analyseResults.contains(AnalyzeResultStatus.Failed.toString())) {
                    return null;
                }
                Log.d(TAG, "analyseStatus" + analyseResults);
                // if analyze is processing, wait for 1 second
                Thread.sleep(1000);
                analyseResults = NetworkUtils.doHttpPostObj(filePath, url, key);
                n++;
            }
        } catch (InterruptedException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return analyseResults;
    }


    @Override
    protected void onPostExecute(String s) {
        Map<String, Double> resultMap = new HashMap<>();
        Set<String> resultSet = new HashSet<>();
        String bestResult = null;
        try {
            if (s != null) {
                resultMap = AzureUtils.parseObjectsJSON(s, 0);
            }
        } catch (Exception e) {
            Log.d(TAG, "Something wrong!! " + s);
        }

        int maxConfidence = 0;
        for (Map.Entry<String, Double> entry : resultMap.entrySet()) {
            if (entry.getValue() > maxConfidence) {
                bestResult = entry.getKey();
            }
            resultSet.add(entry.getKey());
        }


        mCallback.onDetectedFinished(resultSet, bestResult);
    }

}
