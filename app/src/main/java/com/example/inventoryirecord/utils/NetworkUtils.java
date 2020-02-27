package com.example.inventoryirecord.utils;
// Work in progress. Finish camera first.

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Work in progress Need data to test.
public class NetworkUtils {
    private static final String KEY = "9219690c24d24d20964e07c27cfe1b67";
    private static final String TYPE = "application/json";
    private static final String CT = "Content-Type";
    private static final String OCP = "Ocp-Apim-Subscription-Key";

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHttpPostObj(String type, String obj, String url) throws IOException {
        //default header key
        Request.Builder builder = new Request.Builder()
                .header(OCP, KEY)
                .header(CT, TYPE);

        RequestBody formBody = new FormBody.Builder()
                .add(type, obj)
                .build();
//                new FormBody.Builder().add(type, obj).build();


        Request request = builder.url(url)
                .post(formBody)
                .build();// Add post to this object for binary image.

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.header("Operation-Location");
        }
    }

    public static String doHTTPGet(String url) throws IOException {
        Request.Builder builder = new Request.Builder().header(OCP, KEY);

        Request request = builder.url(url)
                .build();

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
