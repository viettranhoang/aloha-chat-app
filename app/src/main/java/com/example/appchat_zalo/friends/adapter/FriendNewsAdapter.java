package com.example.appchat_zalo.friends.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendNewsAdapter extends RecyclerView.Adapter<FriendNewsAdapter.FriendNewsViewHolder> {

    private List<Users> mUserNewsList = new ArrayList<>();

    public static final int NEWS_FIRST = 1;

    public void addUser(Users user) {
        mUserNewsList.add(user);
        notifyDataSetChanged();
    }

    public FriendNewsAdapter() {
    }

    public void setmUserNewsList(List<Users> mUserNewsList) {
        this.mUserNewsList = mUserNewsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_friends, parent, false);
        return new FriendNewsViewHolder(view);
//        View view;
//        if (i == NEWS_FIRST) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_first, parent, false);
//            return new FriendNewsViewHolder(view);
//        } else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_friends, parent, false);
//            return new FriendNewsViewHolder(view);
//        }

    }

    @Override
    public void onBindViewHolder(@NonNull FriendNewsViewHolder holder, int position) {
        holder.bindata(mUserNewsList.get(position));
    }

    @Override
    public int getItemCount() {

        return (mUserNewsList != null ? mUserNewsList.size() : 0);
    }

//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) return  NEWS_FIRST;
//        else
//            return 2;
//    }

    public class FriendNewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_news_friend)
        ImageView mImageNews;

        @BindView(R.id.image_avatar_friend)
        ImageView mImageAvatar;

        @BindView(R.id.text_name)
        TextView mTextName;

        public FriendNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Users users) {

            if (!TextUtils.isEmpty(users.getNews())) {

                mTextName.setText(users.getName());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

                Glide.with(itemView)
                        .load(users.getNews())
                        .apply(requestOptions)
                        .into(mImageNews);
            }
            if (users.getAvatar().equals("default")) {
                mImageAvatar.setImageResource(R.drawable.background_main);
            } else {
                Glide.with(itemView)
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mImageAvatar);
            }

        }

    }

}
