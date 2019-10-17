package com.example.appchat_zalo.home_fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder> {

    private List<Posts> mPostList =  new ArrayList<>();

    public HomePostAdapter() {
    }

    public void setmPostList(List<Posts> mPostList) {
        this.mPostList = mPostList;
        notifyDataSetChanged();
    }

    //
//    public void setmPostList(List<Posts> mPostList) {
//        this.mPostList = mPostList;
//    }

    public void adddPost( Posts posts){
        mPostList.add(posts);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public HomePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_item, parent, false);
        return new HomePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostViewHolder holder, int position) {
        holder.bindata(mPostList.get(position));

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class HomePostViewHolder extends RecyclerView.ViewHolder {
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
        public HomePostViewHolder(@NonNull View itemView) {
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

    }
}
