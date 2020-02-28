package com.example.inventoryirecord.utils;
// Work in progress. Finish camera first.

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String KEY = "9219690c24d24d20964e07c27cfe1b67";
    private static final String TYPE = "application/octet-stream";
    private static final String CT = "Content-Type";
    private static final String OCP = "Ocp-Apim-Subscription-Key";

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHttpPostObj(String filePath, String url) {
        //default header key
        Request.Builder builder = new Request.Builder()
                .header(OCP, KEY)
                .header(CT, TYPE);

        Bitmap bitmap = BitmapFactory.decodeStream(NetworkUtils.class.getResourceAsStream(filePath));

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody formBody = RequestBody.create(BitMapToByteArray(bitmap), mediaType);

        Request request = builder.url(url)
                .post(formBody)
                .build();// Add post to this object for binary image.

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.header("Operation-Location");
        } catch (Exception e) {
            return "null";
        }
    }

    public static String doHTTPGet(String url) throws IOException {
        Log.d(TAG, "doing http GET, url is " + url);
        Request.Builder builder = new Request.Builder().header(OCP, KEY);
        Request request = builder.url(url)
                .build();

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static byte[] BitMapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();

    }
}
