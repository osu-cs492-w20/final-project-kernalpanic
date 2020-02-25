package com.example.inventoryirecord.utils;
// Work in progress. Finish camera first.
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
// Work in progress Need data to test.
public class NetworkUtils {
    private static String KEY = "981aba774209401d8fe70b28a0d371b3";
    private static String TYPE = "application/octet-stream";
    private static String OCP = "Ocp-Apim-Subscription-Key";

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHttpPostObj(String url) throws IOException{
        Request request = new Request.Builder()
                .header(OCP, KEY)
                .header("Content-Type", "application/octet-stream")
                .url(url)
                .build();// Add post to this object for binary image.
        Response response = mHTTPClient.newCall(request).execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
