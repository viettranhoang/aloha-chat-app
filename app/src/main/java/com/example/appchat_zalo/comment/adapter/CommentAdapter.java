package com.example.appchat_zalo.comment.adapter;

import android.app.admin.DelegatedAdminReceiver;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.comment.model.Comment;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.utils.Constants;
import com.example.appchat_zalo.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mListComment =  new ArrayList<>();

    private Users mUser;

//    private DatabaseReference mUserRef, mCommentRef;

    public void setmUser(Users mUser) {
        this.mUser = mUser;
        notifyDataSetChanged();
    }

    public CommentAdapter() {
    }

    public void setmListComment(List<Comment> mListComment) {
        this.mListComment = mListComment;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.bindata(mListComment.get(position));
    }

    @Override
    public int getItemCount() {
        return mListComment.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView imageAvatar;

        @BindView(R.id.image_online)
        ImageView imageOnline;

        @BindView(R.id.text_name)
        TextView textName;

        @BindView(R.id.text_content_comment)
        TextView textContentComment;

        @BindView(R.id.text_time_comment)
        TextView textTimeComment;

        @BindView(R.id.text_date_comment)
        TextView textDateComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindata(Comment comment){
                textContentComment.setText(comment.getContent());
                textTimeComment.setText(comment.getTime());
                textDateComment.setText(comment.getDate());




        }
    }
}
