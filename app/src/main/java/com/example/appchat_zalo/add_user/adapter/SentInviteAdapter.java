package com.example.appchat_zalo.add_user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.add_user.listener.OnclickItemAddUser;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SentInviteAdapter extends RecyclerView.Adapter<SentInviteAdapter.AddUserViewHolder> {

    private List<Users> mUserList = new ArrayList<>();

    private OnclickItemAddUser mListener;

    public SentInviteAdapter(OnclickItemAddUser mListener) {
        this.mListener = mListener;
    }

    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    public void deleteUser(Users users){
        mUserList.remove(users);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AddUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_item, parent, false);
        return new AddUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserViewHolder holder, int position) {
        holder.bindata(mUserList.get(position));

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class AddUserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.button_cancel)
        Button mButtonCancel;

        @BindView(R.id.layout_add_user)
        RelativeLayout mLayoutAdd;

        public AddUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Users users) {
            mTextName.setText(users.getName());
            Glide.with(itemView)
                    .load(users.getAvatar())
                    .circleCrop()
                    .into(mImageAvatar);
        }

        @OnClick(R.id.image_avatar)
        void  onClickRecive(){
            mListener.onclickProfileUser(mUserList.get(getAdapterPosition()));
        }

        @OnClick(R.id.button_cancel)
        void onClickCancel(){
            mListener.onclickCancel(mUserList.get(getAdapterPosition()));
        }
    }
}
