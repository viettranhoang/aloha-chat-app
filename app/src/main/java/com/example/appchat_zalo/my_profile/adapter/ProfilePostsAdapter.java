package com.example.appchat_zalo.my_profile.adapter;

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
import com.example.appchat_zalo.my_profile.listener.OnclickItemMyPostListner;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.PostViewHolder> {

    private List<Posts> listMyPost =  new ArrayList<>();
    private OnclickItemMyPostListner onlcickItemPost;
    private Posts posts;

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public ProfilePostsAdapter(OnclickItemMyPostListner onlcickItemPost) {
        this.onlcickItemPost = onlcickItemPost;
    }

    public ProfilePostsAdapter() {
    }

    public void setListMyPost(List<Posts> listMyPost) {
        this.listMyPost = listMyPost;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfilePostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostsAdapter.PostViewHolder holder, int position) {
        holder.bindata(listMyPost.get(position)); holder.bindata(listMyPost.get(position));

    }

    @Override
    public int getItemCount() {
        return listMyPost.size();
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

        private DatabaseReference mLikeRef, mPostRef;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
        void bindata(Posts posts){
            if (Constants.UID.equals(Constants.UID)) {
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

        }

        @OnClick(R.id.image_picture_posts)
        void onclick(){
            Log.d("hanh", "hahaha"+ listMyPost.toString());
            onlcickItemPost.onClickMyPostItem(listMyPost.get(getAdapterPosition()));
        }
    }
}
