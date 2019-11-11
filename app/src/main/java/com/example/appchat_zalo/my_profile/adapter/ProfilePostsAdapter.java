package com.example.appchat_zalo.my_profile.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.my_profile.listener.OnclickItemMyPostListner;
import com.example.appchat_zalo.model.Posts;
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

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.PostViewHolder> {

    private List<Posts> listMyPost =  new ArrayList<>();
    private OnclickItemMyPostListner onlcickItemPost;
    private Posts posts;
    private DatabaseReference mLikeRef, mPostRef, mRefCommet;

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public ProfilePostsAdapter(OnclickItemMyPostListner onlcickItemPost) {
        this.onlcickItemPost = onlcickItemPost;
    }

    public ProfilePostsAdapter() {
    }

    public void setListMyPost(List<Posts> listMyPost) {
        this.listMyPost = listMyPost;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfilePostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_item, parent, false);
        return new PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProfilePostsAdapter.PostViewHolder holder, int position) {
        holder.bindata(listMyPost.get(position)); holder.bindata(listMyPost.get(position));

    }

    @Override
    public int getItemCount() {
        return listMyPost.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mAvatar;

        @BindView(R.id.text_name)
        TextView mName;

        @BindView(R.id.text)
        TextView mTextPost;

        @BindView(R.id.text_time_posts)
        TextView mTimePost;

        @BindView(R.id.text_date_posts)
        TextView mDatePost;

        @BindView(R.id.text_content_post)
        TextView mContentPost;

        @BindView(R.id.image_picture_posts)
        ImageView mPicturePost;

        @BindView(R.id.image_like)
        ImageView mImageLike;

        @BindView(R.id.image_comment)
        ImageView mImageComment;

        @BindView(R.id.text_number_like)
        TextView mTextNumberLike;

        @BindView(R.id.text_number_comment)
        TextView mTextnumberComment;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
        void bindata(Posts posts){
            if (Constants.UID.equals(Constants.UID)) {
                Glide.with(itemView)
                        .load(Constants.UAVATAR)
                        .circleCrop()
                        .into(mAvatar);

                mName.setText(posts.getName());
                mContentPost.setText(posts.getContent_posts());
                mTimePost.setText(posts.getTime());
                mDatePost.setText(posts.getDate());
                Glide.with(itemView)
                        .load(posts.getPicture())
                        .into(mPicturePost);

                final String postKey = posts.getIdPost();
                initFirebase();
                checkLike(postKey);
                numberLike(postKey);
                numberCommet(postKey);
                mImageLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mImageLike.getTag().equals("like")) {
                            FirebaseDatabase.getInstance().getReference(Constants.TABLE_LIKE).child(postKey).child(Constants.UID).setValue("true");
                        } else {
                            FirebaseDatabase.getInstance().getReference(Constants.TABLE_LIKE).child(postKey).child(Constants.UID).removeValue();

                        }
                    }
                });

            }

        }

        private void initFirebase() {
            mPostRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
            mLikeRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_LIKE);
            mRefCommet = FirebaseDatabase.getInstance().getReference(Constants.TABLE_COMMENT);

        }
        private void numberCommet(String postKey) {
            mRefCommet.child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mTextnumberComment.setText(dataSnapshot.getChildrenCount() + " " + "comment");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        private void numberLike(String postKey) {
            mLikeRef.child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mTextNumberLike.setText(dataSnapshot.getChildrenCount() + " " + "like");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        private void checkLike(String postKey) {

            mLikeRef.child(postKey).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Constants.UID).exists()) {

                        mImageLike.setImageResource(R.drawable.icliked);
                        mImageLike.setTag("liked");
                        mTextNumberLike.setTextColor(R.color.red);
                    } else {
                        mImageLike.setImageResource(R.drawable.icon_like);
                        mImageLike.setTag("like");
                        mTextNumberLike.setTextColor(R.color.pinkLight);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        @OnClick(R.id.image_comment)
        void onclickComment(){
            onlcickItemPost.onClickMyPostComment(listMyPost.get(getAdapterPosition()));

        }
        @OnClick(R.id.image_picture_posts)
        void onclickUpdatePost(){
            Log.d("hanh", "hahaha"+ listMyPost.toString());
            onlcickItemPost.onClickMyPostItem(listMyPost.get(getAdapterPosition()));
        }

    }

}
