package com.example.appchat_zalo.choose_friends.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseVerticalAdapter extends RecyclerView.Adapter<ChooseVerticalAdapter.ChooseVerticalViewHolder> {

    private List<Users> mUserList = new ArrayList<>();

    private List<Users> mChoosedList = new ArrayList<>();

    public List<Users> getChoosedList() {
        return mChoosedList;
    }

    public ChooseVerticalAdapter() {
    }

    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChooseVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_friend_vertical_item, parent, false);
        return new ChooseVerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseVerticalViewHolder holder, int position) {

        holder.bindata(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ChooseVerticalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.checkbox_choose_friend)
        CheckBox mCheckChooseFriend;

        @BindView(R.id.layout_choose)
        RelativeLayout mLayoutChoose;


        public ChooseVerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Users users) {
            mTextName.setText(users.getName());
            Glide.with(itemView)
                    .load(users.getAvatar())
                    .circleCrop()
                    .into(mImageAvatar);

            Glide.with(itemView)
                    .load(users.getOnline())
                    .circleCrop()
                    .into(mImageOnline);
        }

        @OnClick(R.id.layout_choose)
        void onclickItem() {
            mCheckChooseFriend.setChecked(!mCheckChooseFriend.isChecked());
            if (mCheckChooseFriend.isChecked()) {
                mChoosedList.add(mUserList.get(getLayoutPosition()));
                Log.d("aaa", "add: chooseList " + mChoosedList.size());
            } else {
                mChoosedList.remove(mUserList.get(getLayoutPosition()));
                Log.d("aaa", "remove: chooseList " + mChoosedList.size());
            }
        }


    }


}
