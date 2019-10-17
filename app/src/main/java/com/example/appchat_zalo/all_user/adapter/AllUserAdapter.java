package com.example.appchat_zalo.all_user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.all_user.listener.OnclickItemUserListener;
import com.example.appchat_zalo.friends.listener.OnclickItemFriendListener;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.AllUserViewHolder> {
    private List<Users> mListUSer = new ArrayList<>();

    private OnclickItemUserListener listener;

    public AllUserAdapter(OnclickItemUserListener listener) {
        this.listener = listener;
    }

    public void setmListUSer(List<Users> mListUSer) {
        this.mListUSer = mListUSer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_item, parent, false);
        return new AllUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserViewHolder holder, int position) {
        holder.bindata(mListUSer.get(position));

    }

    @Override
    public int getItemCount() {
        return mListUSer.size();
    }

    public class AllUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.text_name)
        TextView mTextName;

        public AllUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void bindata(Users users){
            mTextName.setText(users.getName());

            if(users.getAvatar().equals("default")){
                mImageAvatar.setImageResource(R.drawable.background_main);
            }
            else {
                Glide.with(itemView)
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mImageAvatar);
            }
            Glide.with(itemView)
                    .load(users.getOnline())
                    .into(mImageOnline);
        }

        @OnClick(R.id.layout_user)
        void  onClickItem(){
            listener.onClickFriendOnlineItem(mListUSer.get(getAdapterPosition()));

        }
    }
}
