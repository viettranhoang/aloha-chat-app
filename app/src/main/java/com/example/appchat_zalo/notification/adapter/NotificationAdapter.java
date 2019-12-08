package com.example.appchat_zalo.notification.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.notification.listener.OnclickItemNotifiLikeListener;
import com.example.appchat_zalo.notification.model.Notification;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> mNotiList = new ArrayList<>();
    private OnclickItemNotifiLikeListener mListner;

    private DatabaseReference mUserRef, mPostRef;

    public NotificationAdapter(OnclickItemNotifiLikeListener mListner) {
        this.mListner = mListner;
    }

    public void setmNotiList(List<Notification> mNotiList) {
        this.mNotiList = mNotiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bindata(mNotiList.get(position));

    }

    @Override
    public int getItemCount() {
        return mNotiList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_post)
        ImageView mImagePost;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.text_comment)
        TextView mTextComment;

        @BindView(R.id.layout_notification_item)
        RelativeLayout mLayoutNotifiItem;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bindata(Notification notification) {
            mTextComment.setText(notification.getmText());

            initFiebase();
            String userId = notification.getmUserId();
            getInforUser(userId);
            String postId = notification.getmPostId();
        }

        private void getPostImage(String userId, String postId) {
            mPostRef.child(userId).child(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("NotificationViewHolder", "onDataChange: id post " + postId);
//                    Posts posts = dataSnapshot.getValue(Posts.class);
//                    Log.d("NotificationViewHolder", "onDataChange: image post " + posts.getPicture());
//
//                    Glide.with(itemView)
//                            .load(posts.getPicture())
//                            .into(mImagePost);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void initFiebase() {
            mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
            mPostRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        }

        private void getInforUser(String userId) {
            mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("NotificationViewHolder", "onDataChange: user + " +dataSnapshot.getValue());

                    Users users = dataSnapshot.getValue(Users.class);
                    Log.d("dd", "onDataChange: dd" + users.getName());
                    String avatar = users.getAvatar();
                    String name = users.getName();
                    Glide.with(itemView.getContext())
                            .load(avatar)
                            .circleCrop()
                            .into(mImageAvatar);

                    mTextName.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });

        }
        @OnClick({R.id.layout_notification_item})
        void onclickNotifiItem(){
            mListner.onclickNotifiLikeItem(mNotiList.get(getAdapterPosition()));
        }
    }


}
