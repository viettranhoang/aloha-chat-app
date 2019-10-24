package com.example.appchat_zalo.comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.Message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.all_user.AllUserActivity;
import com.example.appchat_zalo.comment.adapter.CommentAdapter;
import com.example.appchat_zalo.comment.listener.OnclickCommentItemListener;
import com.example.appchat_zalo.comment.model.Comment;
import com.example.appchat_zalo.friends.model.Friend;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends AppCompatActivity {

    @BindView(R.id.image_send)
    ImageView mImageSend;

    @BindView(R.id.image_picture)
    ImageView mImagePicture;

    @BindView(R.id.input_comment)
    EditText mInputComment;

    @BindView(R.id.list_comment)
    RecyclerView mRcvComment;

    @BindView(R.id.toolbar_comment)
    Toolbar mToolbarComment;


    private CommentAdapter mAdapter;
    private List<Comment> mCommentList = new ArrayList<>();

    private DatabaseReference mPostRef, mUserRef, mCommentRef, mRef;
    private String mPostId;
    private String mSaveCurrentDate;
    private String mSaveCurrentTime;
    public static final String TAG = "CommentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPostId = intent.getStringExtra("postId");
        initToolbar();
        initRcv();
//        getUser();
        initFirebase();
        getComment();

    }

    private void getComment() {
        mCommentRef.child(mPostId).addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mCommentList.clear();
                for (DataSnapshot dataComment : dataSnapshot.getChildren()){
                    Comment comment =   dataComment.getValue(Comment.class);
                    Log.d(TAG, "onDataChange:dddd " + comment.getUserId());
                    mCommentList.add(comment);
                }

                mAdapter.setmComentList(mCommentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void getUser() {
//        mUserRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Users> userList = new ArrayList<>();
//               Users users=  dataSnapshot.getValue(Users.class);
//               users.getAvatar();
//               users.getName();
//                userList.add(new Users());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    @OnClick(R.id.image_send)
    void onclickSend() {

        if(mInputComment.getText().toString().equals("")){
            Toast.makeText(this, "you can't send comment", Toast.LENGTH_SHORT).show();
        }
        else {
            sendComment(MessageTypeConfig.TEXT);
//            mInputComment.setText("");
        }



    }

    private void initFirebase() {

        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        mCommentRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_COMMENT);
//        mRef = FirebaseDatabase.getInstance().getReference();

    }

    private void sendComment(String type) {

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());

        String timeComment = mSaveCurrentDate + " -- " +  mSaveCurrentTime;
        String commentId = mCommentRef.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("content", mInputComment.getText().toString());
        hashMap.put("userId", Constants.UID);
        hashMap.put("time", timeComment);
        hashMap.put("postId", mPostId);
        hashMap.put("type", type);
        hashMap.put("CommentId", commentId);

        mCommentRef.child(mPostId).child(commentId).setValue(hashMap);
        Toast.makeText(this, "add comment success full", Toast.LENGTH_SHORT).show();
        mInputComment.setText("");

    }

    private void initRcv() {
        mAdapter = new CommentAdapter(new OnclickCommentItemListener() {
            @Override
            public void onclicCommentItem(Comment comment) {
//                if(Constants.CURRENT_UID.contains(user.getId())){
//                    Intent intent = new Intent(CommentActivity.this, .class);
//                    intent.putExtra("userId", user.getId());
//                    startActivity(intent)
//                }
                Intent intent = new Intent(CommentActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", comment.getUserId());
                startActivity(intent);
            }
        });
        mRcvComment.setLayoutManager(new LinearLayoutManager(this));
        mRcvComment.setHasFixedSize(true);
        mRcvComment.setAdapter(mAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarComment);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}