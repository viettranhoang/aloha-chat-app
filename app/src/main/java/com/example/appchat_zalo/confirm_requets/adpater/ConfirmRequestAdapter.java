package com.example.appchat_zalo.confirm_requets.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class ConfirmRequestAdapter extends RecyclerView.Adapter<ConfirmRequestAdapter.ConfirmRequestViewHolder> {

    private List<Users> mUserList =  new ArrayList<>();

    public ConfirmRequestAdapter() {
    }


    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
    }

    @NonNull
    @Override
    public ConfirmRequestAdapter.ConfirmRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_request_item, parent, false);
        return new ConfirmRequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ConfirmRequestAdapter.ConfirmRequestViewHolder holder, int position) {

        holder.bindata(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ConfirmRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.button_accept)
        Button mButtonAccept;

        @BindView(R.id.button_cancel)
        Button mButtonCancel;

        public ConfirmRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Users users){
            mTextName.setText(users.getName());
            Glide.with(itemView)
                    .load(users.getAvatar())
                    .circleCrop()
                    .into(mImageAvatar);
        }
    }
}
