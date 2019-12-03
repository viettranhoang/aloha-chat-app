package com.example.appchat_zalo.detail_post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.comment.adapter.CommentAdapter;
import com.example.appchat_zalo.comment.listener.OnclickCommentItemListener;
import com.example.appchat_zalo.comment.model.Comment;
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

public class DetailPostActivity extends AppCompatActivity {

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

    @BindView(R.id.toolbar_detail_post)
    Toolbar mToolbarDetailPost;

    @BindView(R.id.list_comment)
    RecyclerView mRcvComment;


    @BindView(R.id.image_send)
    ImageView mImageSend;

    @BindView(R.id.image_picture)
    ImageView mImagePicture;

    @BindView(R.id.input_comment)
    EditText mInputComment;

    private CommentAdapter mDetailPostAdapter;
    private String mPostId;
    private  List<Comment> mCommentList;

    private DatabaseReference mPostRef, mLikeRef, mCommentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPostId = intent.getStringExtra("postId");
        initToolbarDetailPost();
        initRcv();
        initFirebase();
        getPost(mPostId);
        checkLike(mPostId);
        numberLike(mPostId);
        numberCommet(mPostId);

        getComment();



//        getLikePost(mPostId);

    }

    private void getComment() {
        mCommentRef.child(mPostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mCommentList.clear();
                for (DataSnapshot dataComment : dataSnapshot.getChildren()) {
                    Comment comment = dataComment.getValue(Comment.class);
                    Log.d("DetailPostActivity", "onDataChange: comment id" + comment.getUserId());
//                    Log.d(TAG, "onDataChange:dddd " + comment.getUserId());
                    mCommentList.add(comment);
//                    mDetailPostAdapter.setmComentList(mCommentList);
                }

                mDetailPostAdapter.setmComentList(mCommentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcv() {
        mCommentList = new ArrayList<>();
        mDetailPostAdapter =  new CommentAdapter(new OnclickCommentItemListener() {
            @Override
            public void onclicCommentItem(Comment comment) {
                Intent intent = new Intent(DetailPostActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", comment.getUserId());
                startActivity(intent);
            }
        });
        mRcvComment.setLayoutManager(new LinearLayoutManager(this));
        mRcvComment.setHasFixedSize(true);
        mRcvComment.setAdapter(mDetailPostAdapter);
    }

    private void initFirebase() {
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        mLikeRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_LIKE);
        mCommentRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_COMMENT);

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

    private void numberCommet(String postKey) {
        mCommentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTextnumberComment.setText(dataSnapshot.getChildrenCount() + " " + "comment");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPost(String mPostId) {
        mPostRef.child(Constants.UID).child(mPostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Posts> postsList = new ArrayList<>();
                Posts posts = dataSnapshot.getValue(Posts.class);
                Log.d("DetailPostActivity", "onDataChange: id post=== " + posts.getIdPost());
                Glide.with(getApplicationContext())
                        .load(posts.getAvatar())
                        .circleCrop()
                        .into(mAvatar);

                mName.setText(posts.getName());
                mContentPost.setText(posts.getContent_posts());
                mTimePost.setText(posts.getTime());
                mDatePost.setText(posts.getDate());
//            mTextNumberLike.setText(posts.getLike());
                Glide.with(getApplicationContext())
                        .load(posts.getPicture())
                        .into(mPicturePost);
                postsList.add(posts);
//                Log.d("DetailPostActivity", "onDataChange: post ==" + post.toString() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initToolbarDetailPost() {
        setSupportActionBar(mToolbarDetailPost);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
