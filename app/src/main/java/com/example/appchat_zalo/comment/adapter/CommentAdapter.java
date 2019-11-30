package com.example.appchat_zalo.comment.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.comment.CommentTypeConfig;
import com.example.appchat_zalo.comment.listener.OnclickCommentItemListener;
import com.example.appchat_zalo.comment.model.Comment;
import com.example.appchat_zalo.model.Users;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mComentList = new ArrayList<>();
    private DatabaseReference mUserRef, mLikeRef, mRef;

    private OnclickCommentItemListener mListner;

    public void setmComentList(List<Comment> mComentList) {
        this.mComentList = mComentList;
        notifyDataSetChanged();
    }

    public CommentAdapter(OnclickCommentItemListener mListner) {
        this.mListner = mListner;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindata(mComentList.get(position));

    }

    @Override
    public int getItemCount() {
        return mComentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.image_comment)
        ImageView mImageComment;

        @BindView(R.id.text_name)
        TextView mTextName;

        @BindView(R.id.text_time_comment)
        TextView mTextTime;

        @BindView(R.id.text_content_comment)
        TextView mTextContent;

        @BindView(R.id.text_like)
        TextView mTextLike;

        @BindView(R.id.text_number_like)
        TextView mTextnumberlike;

        @BindView(R.id.layput_comment)
        LinearLayout mLayputComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bindata(Comment comment) {

            mTextTime.setText(comment.getTime());

            if(comment.getType().equals(CommentTypeConfig.TEXT)){
                mTextContent.setText(comment.getContent());
                mTextContent.setVisibility(View.VISIBLE);
                mImageComment.setVisibility(View.GONE);

            }
            else  {
                mTextContent.setVisibility(View.INVISIBLE);
                mImageComment.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));
                Glide.with(itemView)
                        .load(comment.getContent())
                        .apply(requestOptions)
                        .into(mImageComment);

            }

            initFirbase();
            String userIdComment = comment.getUserId();
            getInforUser(userIdComment);

            String idComment = comment.getCommentId();
            String idPost = comment.getPostId();
            mRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_COMMENT).child(idPost);
            checkLike(idComment);
            numberlike(idComment);
//            numberComment(idComment);

            mTextLike.setOnClickListener(v -> {
                if (mTextLike.getText().equals("thích")) {
                    FirebaseDatabase.getInstance().getReference("Like_Comment").child(idComment).child(Constants.UID).setValue("true");
                } else {
                    FirebaseDatabase.getInstance().getReference("Like_Comment").child(idComment).child(Constants.UID).removeValue();

                }
            });

            mLayputComment.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Delete Comment");
                builder.setMessage("Are you sure to delete this comment?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userIdComment.equals(Constants.UID)){
                            deleteComment(idComment);

                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            });


        }

        @OnClick(R.id.image_avatar)
        void onClickItem() {

            mListner.onclicCommentItem(mComentList.get(getAdapterPosition()));
        }

        private void deleteComment(String commentId) {
            mRef.child(commentId).removeValue();
            Toast.makeText(itemView.getContext(), "comment is deleted!!", Toast.LENGTH_SHORT).show();

        }


        private void numberlike(String idComment) {
            mLikeRef.child(idComment).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mTextnumberlike.setText(dataSnapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void initFirbase() {
            mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
            mLikeRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_LIKE_COMMENT);
        }


        private void checkLike(String idComment) {
            mLikeRef.child(idComment).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Constants.UID).exists()) {
                        mTextLike.setText("đã thích");
                        mTextLike.setTextColor(R.color.red);

                    } else {
                        mTextLike.setText("thích");
                        mTextLike.setTextColor(R.color.gray);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void getInforUser(String userId) {

            mUserRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Log.d("dd", "onDataChange: dd" + users.getName());
                    String avatar = users.getAvatar();
                    String name = users.getName();
                    Glide.with(itemView)
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
    }




}
