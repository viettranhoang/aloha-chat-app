package com.example.appchat_zalo.friends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.friends.listener.OnclickItemFriendListener;
import com.example.appchat_zalo.model.Users;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsOnlineAdapter extends RecyclerView.Adapter<FriendsOnlineAdapter.FriendOnlineViewHolder>{

    private List<Users> usersList;
    private Context context;

    private OnclickItemFriendListener listener;



    public FriendsOnlineAdapter(Context context, List<Users> listUser, OnclickItemFriendListener onclick) {

        this.context = context;
        this.usersList =  listUser;
        this.listener= onclick;


    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_online_item, parent, false);
        return new FriendOnlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendOnlineViewHolder holder, int position) {
        holder.bindata(usersList.get(position));
    }

    @Override
    public int getItemCount() {

        return usersList.size();
    }

    public class FriendOnlineViewHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        ImageView mOnline;
        TextView mName;
        public FriendOnlineViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatar =  itemView.findViewById(R.id.image_avatar);
            mOnline = itemView.findViewById(R.id.image_online);
            mName = itemView.findViewById(R.id.text_name);
            ButterKnife.bind(this,itemView);


        }
        void bindata(Users users){
            mName.setText(users.getName());

            if(users.getAvatar().equals("default")){
                mAvatar.setImageResource(R.drawable.background_main);
            }
            else {
                Glide.with(itemView)
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mAvatar);
            }
            Glide.with(itemView)
                    .load(users.getOnline())
                    .into(mOnline);
        }
        @OnClick(R.id.image_avatar)
        void  onClickItem(){
            listener.onClickFriendOnlineItem(usersList.get(getAdapterPosition()));

        }
    }



}
