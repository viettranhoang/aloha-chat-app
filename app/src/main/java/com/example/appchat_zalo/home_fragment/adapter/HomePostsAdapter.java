package com.example.appchat_zalo.home_fragment.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.home_fragment.listner.OnclickItemMyPostListner;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.PostViewHolder> {

    private List<Posts> listHomePost =  new ArrayList<>();
    private OnclickItemMyPostListner onlcickItemPost;

    public HomePostsAdapter(OnclickItemMyPostListner onlcickItemPost) {
        this.onlcickItemPost = onlcickItemPost;
    }

    public HomePostsAdapter() {
    }

    public void setListHomePost(List<Posts> listHomePost) {
        this.listHomePost = listHomePost;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomePostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostsAdapter.PostViewHolder holder, int position) {
        holder.bindata(listHomePost.get(position));

    }

    @Override
    public int getItemCount() {
        return listHomePost.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mAvatar;

        @BindView(R.id.text_name)
        TextView mName;

        @BindView(R.id.text)
        TextView mTextPost;

        @BindView(R.id.text_time_posts)
        TextView mTimePost;

        @BindView(R.id.text_date_posts)
        TextView mDatePost;

        @BindView(R.id.text_content_post)
        TextView mContentPost;

        @BindView(R.id.image_picture_posts)
        ImageView mPicturePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
        void bindata(Posts posts){
            Glide.with(itemView)
                    .load(Constants.UAVATAR)
                    .circleCrop()
                    .into(mAvatar);

            mName.setText(posts.getName());
            mContentPost.setText(posts.getContent_posts());
            mTimePost.setText(posts.getTime());
            mDatePost.setText(posts.getDate());
            Glide.with(itemView)
                    .load(posts.getPicture())
                    .into(mPicturePost);

        }

        @OnClick(R.id.image_picture_posts)
        void onclick(){
            Log.d("hanh", "hahaha"+ listHomePost.toString());
            onlcickItemPost.onClickMyPostItem(listHomePost.get(getAdapterPosition()));
        }
    }
}
