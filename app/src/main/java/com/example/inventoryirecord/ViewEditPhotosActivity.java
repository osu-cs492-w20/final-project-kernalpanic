package com.example.inventoryirecord;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.adapters.PhotoGalleryAdapter;
import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.utils.PhotoLibraryUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ViewEditPhotosActivity extends AppCompatActivity implements PhotoGalleryAdapter.OnPhotoClickListener{
    public static final String EDIT = "editPhotos";
    public static final String RECEIPT_BOOL = "receiptBool";
    private static final String TAG = ViewEditPhotosActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 131;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXT_STORAGE = 132;
    private static final int PICK_PHOTO = 133;

    private boolean storageReadPermissionEnabled;
    private boolean storageWritePermissionEnabled;
    private boolean isForReceipt;
    private boolean previewIsForReceipt;
    private ImageView imageView;
    private PhotoGalleryAdapter itemPhotosAdapter;
    private PhotoGalleryAdapter receiptPhotosAdapter;

    private InventoryItem inventoryItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_photos_activity);
        Intent mainIntent = getIntent();

        if (Objects.nonNull(mainIntent) && mainIntent.hasExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM)) {
            inventoryItem = (InventoryItem) mainIntent.getSerializableExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM);
            //editPhotos = (boolean) mainIntent.getSerializableExtra(EDIT);
        }
        if (inventoryItem != null && inventoryItem.itemPics == null) {
            inventoryItem.itemPics = new ArrayList<>();
        }
        if (inventoryItem != null && inventoryItem.receiptPics == null) {
            inventoryItem.receiptPics = new ArrayList<>();
        }

        LinearLayoutManager photoLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView itemPhotosRecyclerView = findViewById(R.id.item_photo_rec_view);
        itemPhotosRecyclerView.setLayoutManager(photoLinearLayoutManager);
        itemPhotosRecyclerView.setHasFixedSize(true);
        itemPhotosAdapter = new PhotoGalleryAdapter(this, false, this);
        itemPhotosRecyclerView.setAdapter(itemPhotosAdapter);
        itemPhotosAdapter.updateImageList(inventoryItem.itemPics);

        LinearLayoutManager receiptLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView receiptPhotosRecyclerView = findViewById(R.id.receipt_photo_rec_view);
        receiptPhotosRecyclerView.setLayoutManager(receiptLinearLayoutManager);
        receiptPhotosRecyclerView.setHasFixedSize(true);
        receiptPhotosAdapter = new PhotoGalleryAdapter(this, true, this);
        receiptPhotosRecyclerView.setAdapter(receiptPhotosAdapter);
        receiptPhotosAdapter.updateImageList(inventoryItem.receiptPics);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.


        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXT_STORAGE);

        }else {
            Button addItemPhotoButton = findViewById(R.id.add_item_photo_button);
            addItemPhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isForReceipt = false;
                    launchPickImageIntent();
                }
            });
            Button addReceiptPhotoButton = findViewById(R.id.add_receipt_photo_button);
            addReceiptPhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isForReceipt = true;
                    launchPickImageIntent();
                }
            });
            imageView = findViewById(R.id.kitten);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent photoPickIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    photoPickIntent.setType("image/*");
//                    startActivityForResult(photoPickIntent, PICK_PHOTO);
//                }
//            });
        }


    }

    private void launchPickImageIntent() {
        Intent photoPickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if(data != null && data.getData() != null) {
                try {
                    InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
//                    String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/InventoryPhotos";
//                    File file = new File(path);
//                    file.mkdirs();
//                    File actualFile = new File(file, UUID.randomUUID().toString() + ".jpeg");
//                    actualFile.createNewFile();
//                    File path = new File(Environment.get + "/images");
//                    if(!path.exists()) {
//                        path.mkdirs();
//                    }
//                    File file = new File(path, "cat.jpeg");
//                    boolean fileCreate = file.createNewFile();

//                    inventoryItem.itemPics.add(actualFile.getAbsolutePath());
//                    OutputStream outputStream = getApplicationContext().openFileOutput("cat.jpeg", Context.MODE_PRIVATE);
//                    OutputStream outputStream = new FileOutputStream(actualFile);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    if (isForReceipt) {
                        inventoryItem.receiptPics.add(PhotoLibraryUtils.saveImage(getApplicationContext(), image));
                        receiptPhotosAdapter.updateImageList(inventoryItem.receiptPics);
                        //imageView.setImageBitmap(PhotoLibraryUtils.getSavedImage(inventoryItem.receiptPics.get(0)));
                    } else {
                        inventoryItem.itemPics.add(PhotoLibraryUtils.saveImage(getApplicationContext(), image));
                        itemPhotosAdapter.updateImageList(inventoryItem.itemPics);
                        //imageView.setImageBitmap(PhotoLibraryUtils.getSavedImage(inventoryItem.itemPics.get(0)));
                    }
//                    image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    outputStream.flush();
//                    outputStream.close();


                    //InputStream storedInputStream = getApplicationContext().openFileInput("cat.jpeg");
//                    InputStream storedInputStream = new FileInputStream(inventoryItem.itemPics.get(0));
                    if (inputStream != null) {
                        inputStream.close();
//                        storedInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storageReadPermissionEnabled = true;
                    Log.d(TAG, "permission granted: " + storageReadPermissionEnabled);
                } else {
                    storageReadPermissionEnabled = false;
                    Log.d(TAG, "permission granted: " + storageReadPermissionEnabled);
                }
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXT_STORAGE : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storageWritePermissionEnabled = true;
                    Log.d(TAG, "permission granted: " + storageWritePermissionEnabled);
                } else {
                    storageWritePermissionEnabled = false;
                    Log.d(TAG, "permission granted: " + storageWritePermissionEnabled);
                }
            }
        }
    }

    @Override
    public void onPhotoClicked(String photoLocation, boolean isForReceipt) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            imageView.setImageBitmap(PhotoLibraryUtils.getSavedImage(photoLocation, options));
            int visibility = imageView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            imageView.setVisibility(visibility);
            Button deletePhotoButton = findViewById(R.id.delete_photo);
            deletePhotoButton.setVisibility(visibility);
            deletePhotoButton.setEnabled(true);
            deletePhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            previewIsForReceipt = isForReceipt;

        } catch (IOException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.ic_broken_image_black_24dp);
        }
    }
}
