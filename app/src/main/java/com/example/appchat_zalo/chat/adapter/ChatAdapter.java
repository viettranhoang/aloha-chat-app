package com.example.appchat_zalo.chat.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.chat.listner.OnclickChatItemListner;
import com.example.appchat_zalo.chat.model.Chat;
import com.example.appchat_zalo.utils.Constants;
import com.example.appchat_zalo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> listUser = new ArrayList<>();

    private OnclickChatItemListner mListner;

    public ChatAdapter(OnclickChatItemListner mListner) {
        this.mListner = mListner;
    }

    public void addChat(Chat chat) {
        listUser.add(chat);
        notifyDataSetChanged();
    }

    public void setListUser(List<Chat> listUser) {
        this.listUser = listUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bindata(listUser.get(position));

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.text_message)
        TextView mTextLastMessage;

        @BindView(R.id.text_time_seen)
        TextView mTextTimeSeen;

        @BindView(R.id.image_seen)
        ImageView mImageSeen;

        @BindColor(R.color.black87)
        int mBlack87;

        @BindColor(R.color.black40)
        int mBlack40;

        boolean checkUser = true;

        public ChatViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bindata(Chat chat) {

//            if(chat.getUsers() == null){

            if (chat.getUsers() == null)
                checkUser = false;
            else if (chat.getGroups() == null)
                checkUser = true;
            else return;

            String avatar = checkUser ? chat.getUsers().getAvatar() : chat.getGroups().getAvatar();
            String name = checkUser ? chat.getUsers().getName() : chat.getGroups().getName();

            Glide.with(itemView)
                    .load(avatar)
                    .circleCrop()
                    .into(mAvatar);

            mTextName.setText(name);
//
//                Glide.with(itemView)
//                        .load(chat.getUsers().getAvatar())
//                        .circleCrop()
//                        .into(mAvatar);
//                mTextName.setText(chat.getUsers().getName());

            Glide.with(itemView)
                    .load(R.drawable.roun_conner_seen)
                    .circleCrop()
                    .into(mImageSeen);

            mTextLastMessage.setText(chat.getLastMessage().getMessage());
            mTextTimeSeen.setText(Utils.getTime(chat.getLastMessage().getTime()));
            mImageOnline.setVisibility(View.VISIBLE);
            mImageSeen.setVisibility(View.INVISIBLE);

//            if (chat.getUsers().getOnline() == Constants.ONLINE) {
//                mImageOnline.setVisibility(View.VISIBLE);
//            }
//            else {
//                mImageOnline.setVisibility(View.INVISIBLE);
//            }

            if (Constants.CURRENT_UID != null) {
                if (!chat.getLastMessage().isSeen() && !chat.getLastMessage().getFrom().equals(Constants.CURRENT_UID)) {
                    mImageSeen.setVisibility(View.VISIBLE);
                    mTextName.setTypeface(mTextName.getTypeface(), Typeface.DEFAULT_BOLD.getStyle());
                    mTextLastMessage.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    mTextLastMessage.setTextColor(mBlack87);
                } else {

                    mImageSeen.setVisibility(View.GONE);
                    mTextName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    mTextLastMessage.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                    mTextLastMessage.setTextColor(mBlack40);
                }

            }

        }

        @OnClick(R.id.layput_chat)
        void onclickItem() {
            if (checkUser) {
                mListner.onClickUserChatItem(listUser.get(getAdapterPosition()).getUsers());
                mImageSeen.setVisibility(View.GONE);
                mTextName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                mTextLastMessage.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                mTextLastMessage.setTextColor(mBlack40);
            } else {
                mListner.onClickGroupItem(listUser.get(getAdapterPosition()).getGroups());
                mImageSeen.setVisibility(View.GONE);
                mTextName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                mTextLastMessage.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                mTextLastMessage.setTextColor(mBlack40);
            }
        }

    }
}
