package com.example.inventoryirecord.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.inventoryirecord.utils.AzureUtils;
import com.example.inventoryirecord.utils.NetworkUtils;

import java.io.IOException;

public class AzureReceiptAnalyseAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = AnalyzeResultStatus.class.getSimpleName();
    private Callback mCallback;

    public interface Callback {
        void onAnalyseFinished(ReceiptResult receiptResult);
    }

    public AzureReceiptAnalyseAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String filePath = strings[0];
        String url = strings[1];
        String analyseResults = null;
        try {

            String resultUrl = NetworkUtils.doHttpPostObj(filePath, url);

            analyseResults = NetworkUtils.doHTTPGet(resultUrl);

            while (!analyseResults.contains(AnalyzeResultStatus.Succeeded.toString())) {
                /// analyse failed
                if (analyseResults.contains(AnalyzeResultStatus.Failed.toString())) {
                    return null;
                }
                Log.d(TAG, "analyseStatus" + analyseResults);
                // if analyze is processing, wait for 1 second
                Thread.sleep(1000);
                analyseResults = NetworkUtils.doHTTPGet(resultUrl);

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return analyseResults;
    }


    @Override
    protected void onPostExecute(String s) {
        ReceiptResult receiptResult = null;
        if (s != null) {
            receiptResult = AzureUtils.parseReceiptJSON(s);
        }
        mCallback.onAnalyseFinished(receiptResult);
    }
}
