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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.inventoryirecord.adapters.PhotoGalleryAdapter;
import com.example.inventoryirecord.data.InventoryItem;
import com.example.inventoryirecord.data.ItemPhoto;
import com.example.inventoryirecord.photos.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewEditPhotosActivity extends AppCompatActivity implements PhotoGalleryAdapter.OnPhotoClickListener{
    public static final String EDIT = "editPhotos";
    private static final String TAG = ViewEditPhotosActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 131;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXT_STORAGE = 132;
    private static final int PICK_PHOTO = 133;
    public static final int VIEW_EDIT_PHOTOS_ACTIVITY_CODE = 134;
    public static final int PHOTO_VIEW_ACTIVITY_CODE = 135;

    private boolean storageReadPermissionEnabled;
    private boolean storageWritePermissionEnabled;
    private boolean isForReceipt;
    private boolean previewIsForReceipt;
    private ImageView imageView;
    private PhotoGalleryAdapter itemPhotosAdapter;
    private PhotoGalleryAdapter receiptPhotosAdapter;

    private InventoryItem inventoryItem;
    private Button addItemPhotoButton;
    private Button addReceiptPhotoButton;

    private InventorySaveViewModel inventorySaveViewModel;

    private String currentPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_photos_activity);
        Intent mainIntent = getIntent();
        if (Objects.nonNull(mainIntent) && mainIntent.hasExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM)) {
            inventoryItem = (InventoryItem) mainIntent.getSerializableExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM);
        }
        if (inventoryItem != null && inventoryItem.itemPics == null) {
            inventoryItem.itemPics = new ArrayList<>();
        }
        if (inventoryItem != null && inventoryItem.receiptPics == null) {
            inventoryItem.receiptPics = new ArrayList<>();
        }

        inventorySaveViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(InventorySaveViewModel.class);

        StaggeredGridLayoutManager photoStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        RecyclerView itemPhotosRecyclerView = findViewById(R.id.item_photo_rec_view);
        itemPhotosRecyclerView.setLayoutManager(photoStaggeredLayoutManager);
        itemPhotosRecyclerView.setHasFixedSize(true);
        itemPhotosAdapter = new PhotoGalleryAdapter(this, false, this);
        itemPhotosRecyclerView.setAdapter(itemPhotosAdapter);
//        itemPhotosAdapter.updateImageList(inventoryItem.itemPics);

        inventorySaveViewModel.getItemObjectPhotos(inventoryItem.itemID).observe(this, new Observer<List<ItemPhoto>>() {
            @Override
            public void onChanged(List<ItemPhoto> itemPhotos) {
                itemPhotosAdapter.updateImageList(itemPhotos);
            }
        });

        StaggeredGridLayoutManager receiptStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        RecyclerView receiptPhotosRecyclerView = findViewById(R.id.receipt_photo_rec_view);
        receiptPhotosRecyclerView.setLayoutManager(receiptStaggeredLayoutManager);
        receiptPhotosRecyclerView.setHasFixedSize(true);
        receiptPhotosAdapter = new PhotoGalleryAdapter(this, true, this);
        receiptPhotosRecyclerView.setAdapter(receiptPhotosAdapter);

//        receiptPhotosAdapter.updateImageList(inventoryItem.receiptPics);
        inventorySaveViewModel.getItemReceiptPhotos(inventoryItem.itemID).observe(this, new Observer<List<ItemPhoto>>() {
            @Override
            public void onChanged(List<ItemPhoto> itemPhotos) {
                receiptPhotosAdapter.updateImageList(itemPhotos);
            }
        });


//        receiptPhotosAdapter.updateImageList(inventorySaveViewModel.getItemObjectPhotos(inventoryItem.itemID));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);

        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXT_STORAGE);

        }else {
            addItemPhotoButton = findViewById(R.id.add_item_photo_button);
            addItemPhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemPhotoButton.setBackground(getDrawable(R.drawable.round_button_clicked));

                    isForReceipt = false;
                    launchPickImageIntent();
                }
            });
            addReceiptPhotoButton = findViewById(R.id.add_receipt_photo_button);
            addReceiptPhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addReceiptPhotoButton.setBackground(getDrawable(R.drawable.round_button_clicked));
                    isForReceipt = true;
                    launchPickImageIntent();
                }
            });
            imageView = findViewById(R.id.kitten);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onPreviewPhotoClicked();
                }
            });
        }

    }

    private void onPreviewPhotoClicked() {
        Intent photoViewIntent = new Intent(this,PhotoViewActivity.class);
        photoViewIntent.putExtra(ViewSingleItemDetailsActivity.INVENTORY_ITEM, inventoryItem);
        photoViewIntent.putExtra(PhotoViewActivity.PHOTO, currentPhoto);
        startActivityForResult(photoViewIntent,PHOTO_VIEW_ACTIVITY_CODE);
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
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    if (isForReceipt) {
                        String path = BitmapUtils.saveImage(getApplicationContext(), image, false);
//                        inventoryItem.receiptPics.add(path);
                        inventorySaveViewModel.insertSinglePhoto(new ItemPhoto(path,inventoryItem.itemID, isForReceipt));
//                        receiptPhotosAdapter.updateImageList(inventoryItem.receiptPics);
                    } else {
                        String path = BitmapUtils.saveImage(getApplicationContext(), image, false);
//                        inventoryItem.itemPics.add(path);
                        inventorySaveViewModel.insertSinglePhoto(new ItemPhoto(path,inventoryItem.itemID, isForReceipt));
//                        itemPhotosAdapter.updateImageList(inventoryItem.itemPics);
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PHOTO_VIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK){
            if(data != null && data.hasExtra(ViewEditPhotosActivity.EDIT)) {
                inventoryItem = (InventoryItem) data.getSerializableExtra(ViewEditPhotosActivity.EDIT);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addReceiptPhotoButton.setBackground(getDrawable(R.drawable.round_button));
        addItemPhotoButton.setBackground(getDrawable(R.drawable.round_button));
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
    public void onPhotoClicked(final ItemPhoto photoLocation, final boolean isForReceipt) {
        try {
            final Button deletePhotoButton = findViewById(R.id.delete_photo);
            if((photoLocation.path).equals(currentPhoto)) {
                //options.inSampleSize = 4;
                int visibility = imageView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
                imageView.setVisibility(visibility);
                deletePhotoButton.setVisibility(visibility);
                deletePhotoButton.setEnabled(true);
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                imageView.setImageBitmap(BitmapUtils.getSavedImage(photoLocation.path, options));
                imageView.setVisibility(View.VISIBLE);
                deletePhotoButton.setVisibility(View.VISIBLE);
                deletePhotoButton.setEnabled(true);
                currentPhoto = photoLocation.path;
            }
            deletePhotoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BitmapUtils.deleteImageFile(photoLocation.path);
                    if(isForReceipt) {
                        inventorySaveViewModel.deleteSinglePhoto(new ItemPhoto(photoLocation.path, inventoryItem.itemID, true));
//                        inventoryItem.receiptPics.remove(photoLocation.path);
//                        receiptPhotosAdapter.updateImageList(inventoryItem.receiptPics);
                    } else {
                        inventorySaveViewModel.deleteSinglePhoto(new ItemPhoto(photoLocation.path, inventoryItem.itemID, false));
//                        inventoryItem.itemPics.remove(photoLocation.path);
//                        itemPhotosAdapter.updateImageList(inventoryItem.itemPics);
                    }
                    imageView.setVisibility(View.INVISIBLE);
                    deletePhotoButton.setVisibility(View.INVISIBLE);
                }
            });
            previewIsForReceipt = isForReceipt;

        } catch (IOException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.ic_broken_image_black_24dp);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_OK,
                new Intent().putExtra(EDIT, inventoryItem));
        finish();
        return true;
    }
}
