package com.example.appchat_zalo.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.search.listener.OnclikItemSearchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Users> mUserList =  new ArrayList<>();
    private OnclikItemSearchListener mListener;

    public SearchAdapter(OnclikItemSearchListener mListener) {
        this.mListener = mListener;
    }

    public void setmUserList(List<Users> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bindata(mUserList.get(position));

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.text_name)
        TextView mTextName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void bindata(Users users){
            mTextName.setText(users.getName());
            if (users.getId().equals("default")){
                Glide.with(itemView)
                        .load(R.drawable.anhbia)
                        .circleCrop()
                        .into(mImageAvatar);
            }
            Glide.with(itemView)
                    .load(users.getAvatar())
                    .circleCrop()
                    .into(mImageAvatar);

            Glide.with(itemView)
                    .load(users.getOnline())
                    .circleCrop()
                    .into(mImageOnline);
        }

        @OnClick(R.id.image_avatar)
        void OnclickItem(){
            mListener.onClickSearchItem(mUserList.get(getAdapterPosition()));
        }

    }
}
