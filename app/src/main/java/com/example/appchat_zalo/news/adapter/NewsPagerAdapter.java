package com.example.appchat_zalo.news.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsPagerAdapter extends PagerAdapter {

    @BindView(R.id.image_avatar)
    ImageView mImageAvatar;

    @BindView(R.id.image_news)
    ImageView mImageNews;

    @BindView(R.id.text_name)
    TextView mTextName;

    @BindView(R.id.input_message)
    EditText mInputMessage;

    @BindView(R.id.image_send)
    ImageView mImageSend;

    private int mProccesStatus = 0;

    private Handler mHandler = new Handler();
    private List<Users> mUserList = new ArrayList<>();

    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.news_item, container, false);
        ButterKnife.bind(this, view);
        bindata(mUserList.get(position));

        ProgressBar mProgressBarNews = view.findViewById(R.id.progress_bar_news);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProccesStatus < 100){
                    mProccesStatus++;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarNews.setProgress(mProccesStatus);
                        }
                    });
                }

            }
        }).start();

        return view;
    }

    @SuppressLint("ResourceAsColor")
    void bindata(Users user){
        Glide.with(mImageNews.getContext())
                .load(user.getNews())
                .into(mImageNews);

        Glide.with(mImageAvatar.getContext())
                .load(user.getAvatar())
                .circleCrop()
                .into(mImageAvatar);

        mTextName.setText(user.getName());
        mTextName.setTextColor(R.color.white);
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
