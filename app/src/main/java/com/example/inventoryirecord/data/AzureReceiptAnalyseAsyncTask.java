package com.example.inventoryirecord.data;

import android.os.AsyncTask;

import com.example.inventoryirecord.utils.AzureUtils;
import com.example.inventoryirecord.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import javax.security.auth.callback.Callback;

public class AzureReceiptAnalyseAsyncTask extends AsyncTask<String, Void, String> {

    private Callback mCallback;

    public interface Callback {
        void onAnalyseFinished(ReceiptResult receiptResult);
    }

    public AzureReceiptAnalyseAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String obj = strings[1];
        String url = strings[2];
        String analyseResults = null;
        try {
            String resultUrl = NetworkUtils.doHttpPostObj(type, obj, url);

            analyseResults = NetworkUtils.doHTTPGet(resultUrl);
        } catch (IOException e) {
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
