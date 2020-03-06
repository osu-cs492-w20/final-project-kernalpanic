package com.example.inventoryirecord.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.R;
import com.example.inventoryirecord.utils.PhotoLibraryUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PhotoViewHolder> {
    private static final String TAG = PhotoGalleryAdapter.class.getSimpleName();
    private List<String> imageList;
    private OnPhotoClickListener onPhotoClickListener;
    private boolean isReceipt;

    public PhotoGalleryAdapter(OnPhotoClickListener onPhotoClickListener, boolean isForReceipt) {
        this.onPhotoClickListener = onPhotoClickListener;
        this.isReceipt = isForReceipt;
    }

    public void updateImageList(List<String> images) {
        this.imageList = images;
        notifyDataSetChanged();
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
        void onPhotoClicked(String photoLocation, boolean isForReceipt);
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
        void bind(String photoLocation) {
            Log.d(TAG, "In on bind for " + photoLocation);
            try {
                imageView.setImageBitmap(PhotoLibraryUtils.getSavedImage(photoLocation));
            } catch (IOException e) {
                imageView.setImageResource(R.drawable.ic_broken_image_black_24dp);
                e.printStackTrace();
            }
        }
    }
}
