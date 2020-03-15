package com.example.inventoryirecord.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.R;
import com.example.inventoryirecord.data.ItemPhoto;
import com.example.inventoryirecord.photos.BitmapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoViewHolder> {
    private static final String TAG = PhotoGalleryAdapter.class.getSimpleName();
    private List<ItemPhoto> imageList;
    private HashMap<String, Bitmap> images;
    private OnPhotoClickListener onPhotoClickListener;
    private boolean isReceipt;
    private Context context;

    public PhotoGalleryAdapter(OnPhotoClickListener onPhotoClickListener, boolean isForReceipt, Context parentContext) {
        this.onPhotoClickListener = onPhotoClickListener;
        this.isReceipt = isForReceipt;
        this.context = parentContext;

    }

    public void updateImageList(List<ItemPhoto> images) {
        this.imageList = images;
        this.images = loadImagesFromMemory(images);
        notifyDataSetChanged();
    }

    private HashMap<String, Bitmap> loadImagesFromMemory(List<ItemPhoto> images) {
        List<String> locationStringList = new ArrayList<>();
        for (ItemPhoto singleItemPhoto: images) {
            locationStringList.add(singleItemPhoto.path);
        }
        HashMap<String, Bitmap> hashMap = new HashMap<>();
        for (String image : locationStringList) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                hashMap.put(image, BitmapUtils.getSavedImage(image, options));
            } catch (IOException e) {
                e.printStackTrace();
                hashMap.put(image, BitmapFactory.decodeResource(this.context.getResources(),R.drawable.ic_broken_image_black_24dp));
            }
        }
        return hashMap;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View photoView = layoutInflater.inflate(R.layout.photo_layout, parent, false);
        return new PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return Objects.isNull(imageList) ? 0 : imageList.size();
    }

    public interface OnPhotoClickListener {
        void onPhotoClicked(ItemPhoto photoLocation, boolean isForReceipt);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.single_photo_image_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhotoClickListener.onPhotoClicked(imageList.get(getAdapterPosition()), isReceipt);
                }
            });
        }
        void bind(ItemPhoto photoLocation) {
            Log.d(TAG, "In on bind for " + photoLocation);
            //imageView.setImageBitmap(PhotoLibraryUtils.getSavedImage(photoLocation));
            imageView.setImageBitmap(images.get(photoLocation.path));
        }
    }
}
