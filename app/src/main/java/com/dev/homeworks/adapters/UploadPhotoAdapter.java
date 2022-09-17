package com.dev.homeworks.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.homeworks.R;
import com.dev.homeworks.model.Photo;

import java.util.List;

public class UploadPhotoAdapter extends RecyclerView.Adapter<UploadPhotoAdapter.PhotoViewHolder> {
    private List<Photo> photos;

    public UploadPhotoAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.uploadedPhoto.setImageURI(photos.get(position).getPath());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView uploadedPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            uploadedPhoto = itemView.findViewById(R.id.uploaded_photo);
            uploadedPhoto.setOnLongClickListener(view -> {
                photos.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                return true;
            });
            uploadedPhoto.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(photos.get(getAdapterPosition()).getPath(), "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
