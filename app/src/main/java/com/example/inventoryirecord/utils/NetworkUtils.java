package com.example.inventoryirecord.utils;
// Work in progress. Finish camera first.

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String TYPE = "application/octet-stream";
    private static final String CT = "Content-Type";
    private static final String OCP = "Ocp-Apim-Subscription-Key";

    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHttpPostObj(String filePath, String url, String key) throws FileNotFoundException {
        Request request = buildRequest(filePath, url, key);

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Response geHttpPostResponse(String filePath, String url, String key) throws FileNotFoundException {
        Request request = buildRequest(filePath, url, key);
        // Add post to this object for binary image.

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Request buildRequest(String filePath, String url, String key) throws FileNotFoundException {
        //default header key
        Request.Builder builder = new Request.Builder()
                .header(OCP, key)
                .header(CT, TYPE);

//        FileInputStream fis = new FileInputStream(filePath);
//        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        Bitmap bitmap = BitmapFactory.decodeStream(NetworkUtils.class.getResourceAsStream(filePath));
        MediaType mediaType = MediaType.parse(TYPE);
        RequestBody formBody = RequestBody.create(BitMapToByteArray(bitmap), mediaType);

        return builder.url(url)
                .post(formBody)
                .build();
    }

    public static String doHTTPGet(String url, String key) throws IOException {
        Log.d(TAG, "doing http GET, url is " + url);
        Request.Builder builder = new Request.Builder().header(OCP, key);
        Request request = builder.url(url)
                .build();

        try (Response response = mHTTPClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static byte[] BitMapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();

    }


}
