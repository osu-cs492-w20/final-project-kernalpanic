package com.example.inventoryirecord.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PhotoLibraryUtils {
    private final static String TAG = PhotoLibraryUtils.class.getSimpleName();
    private final static String DIRECTORY = "/InventoryPhotos";

    public static String saveImage(Context context, Bitmap image) throws IOException {
        String path = context.getFilesDir().getAbsolutePath() + DIRECTORY;
        File directory = new File(path);
        directory.mkdirs();
        File storageFile = new File(directory, UUID.randomUUID().toString() + ".jpeg");
        storageFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(storageFile);
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
        return storageFile.getAbsolutePath();
    }

    public static Bitmap getSavedImage(String fileName, @Nullable BitmapFactory.Options options) throws IOException {
        InputStream storedInputStream = new FileInputStream(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(storedInputStream, null , options);
        storedInputStream.close();
        return bitmap;
    }
}
