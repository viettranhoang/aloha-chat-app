package com.example.appchat_zalo.confirm_requets.adpater;

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
import com.example.appchat_zalo.confirm_requets.listenser.OnclickItemConfirmRequestListener;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmRequestAdapter extends RecyclerView.Adapter<ConfirmRequestAdapter.ConfirmRequestViewHolder> {

    private List<Users> mUserList = new ArrayList<>();

    private OnclickItemConfirmRequestListener mListener;

    public ConfirmRequestAdapter(OnclickItemConfirmRequestListener mListener) {
        this.mListener = mListener;
    }

    public  void  addUser(Users users){
        mUserList.add(users);
    }
    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConfirmRequestAdapter.ConfirmRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_request_item, parent, false);
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

        @BindView(R.id.layput_request_invite)
        RelativeLayout mLayoutRequest;


        public ConfirmRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Users users) {
            if (users != null) {
                mTextName.setText(users.getName());

                if("default".equalsIgnoreCase(users.getAvatar())){

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

        @OnClick(R.id.image_avatar)
        void onClickRecive() {
            mListener.onclickRecive(mUserList.get(getAdapterPosition()));
        }

        @OnClick(R.id.button_accept)
        void onClickAccept() {
            mListener.onclickAccpet(mUserList.get(getAdapterPosition()));
        }

        @OnClick(R.id.button_cancel)
        void onClickCancel() {
            mListener.onclickCancel(mUserList.get(getAdapterPosition()));
        }

    }


}

