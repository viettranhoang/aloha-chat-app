package com.example.appchat_zalo.Message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchat_zalo.Message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.utils.Constants;
import com.example.appchat_zalo.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> mMessageList =  new ArrayList<>();

    private  Users users ;

    public void setUsers(Users users) {
        this.users = users;

    }


    private FirebaseUser user;

    public static final int MESSAGE_LEFT = 1;
    public static final int MESSAGE_RIGHT = 2;

    private int selectedPosition = -100;

    public MessageAdapter() {
    }

    public void setmMessageList(List<Message> mMessageList) {
        this.mMessageList = mMessageList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if(i == MESSAGE_LEFT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
            return new MessageViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);

            return new MessageViewHolder(view) ;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
       holder.bindata(mMessageList.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(mMessageList.get(position).getFrom().equals(user.getUid())){
            return MESSAGE_RIGHT;
        }
        else return MESSAGE_LEFT;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_message)
        ImageView mImageMessage;

        @BindView(R.id.text_seen)
        TextView mTextSeen;

        @BindView(R.id.text_message)
        TextView mTextMessage;

        @BindView(R.id.text_time)
        TextView mTextTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void  bindata(Message message){
            //doan nay crash vi mUser.getAvatar() null. do mUser chua dc set
            // mUser nay la nguoi dang nt cung. c phai set tu activity

            user = FirebaseAuth.getInstance().getCurrentUser();

            Glide.with(itemView)
                    .load(users.getAvatar())
                    .circleCrop()
                    .into(mImageAvatar);

            mTextTime.setText(Utils.getTime(message.getTime()));

            String  seen = message.isSeen() ?  "đã xem" : "đã chuyển";
            mTextSeen.setText(seen);

//            mTextSeen.setText(message.isSeen() ?  : "");
//            boolean seen =  message.isSeen();
//           if(seen){
//               mTextSeen.setText("đã xem");
//           }
//           else {
//               mTextSeen.setText("đã chuyển");
//           }
//            mTextSeen.setText(message.isSeen() ? "Đã xem" : "Đã chuyển");1

            if (message.getType().equals(MessageTypeConfig.TEXT)){
                mTextMessage.setText(message.getMessage());
                mTextMessage.setVisibility(View.VISIBLE);
                mImageMessage.setVisibility(View.GONE);
            }
            else {
                mTextMessage.setVisibility(View.INVISIBLE);
                mImageMessage.setVisibility(View.VISIBLE);
                Glide.with(itemView)
                        .load(message.getMessage())
                        .apply(new RequestOptions().override(200,500))
                        .into(mImageMessage);
            }

            if(selectedPosition != getLayoutPosition()){
                mTextTime.setVisibility(View.GONE);
                mTextSeen.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.text_message)
        void onClickMessage() {
            if (mTextSeen.getVisibility() != View.VISIBLE) {
                selectedPosition = getLayoutPosition();
                mTextSeen.setVisibility(View.VISIBLE);
                mTextTime.setVisibility(View.VISIBLE);
            } else selectedPosition = -100;

            notifyDataSetChanged();
        }
    }
}
