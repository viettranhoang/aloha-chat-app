package com.example.appchat_zalo.add_posts.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPostAdapter extends RecyclerView.Adapter<AddPostAdapter.AddPostViewholder> {

    private List<Bitmap> listImage;

    public AddPostAdapter(List<Bitmap> listImage) {
        this.listImage = listImage;
    }

    public void setListImage(List<Bitmap> listImage) {
        this.listImage = listImage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddPostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_post_item,parent, false);
        return new AddPostViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPostViewholder holder, int position) {
        holder.bindata(listImage.get(position));

    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    public class AddPostViewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_picture)
        ImageView mImagePicture;

        @BindView(R.id.image_cancel)
        ImageView mImageCancel;

        public AddPostViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bindata(Bitmap bitmap){
            mImagePicture.setImageBitmap(bitmap);
        }

        @OnClick(R.id.image_cancel)
        void OnclcikCancel(){
            listImage.remove(getAdapterPosition());
            notifyDataSetChanged();
        }


    }


}
