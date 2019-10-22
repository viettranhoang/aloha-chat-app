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

import com.example.appchat_zalo.Message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.comment.adapter.CommentAdapter;
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

//    private void getUser() {
//        mUserRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Users> userList = new ArrayList<>();
//               Users users=  dataSnapshot.getValue(Users.class);
//                userList.add(users);
//                mAdapter.setmUser(users);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void getComment() {

//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_COMMENT).child(mPostId).getChildren()){
//                    Comment comment =  data.getValue(Comment.class);
//
//                    Log.d("ss", "onDataChange: commt " + comment.getCommentId());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        mCommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.child(mPostId).getChildren()){
                    String  key = data.getKey();
                    if(key == mPostId ){
                        Log.d(TAG, "onDataChange:  post current" + key);
                    }
                }

//
//                mCommentList.clear();
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//
//                        Comment comment  = data.getValue(Comment.class);
//                        Log.d("dd", "onDataChange: list comment " + comment.getCommentId());
//                        mCommentList.add(comment);
//
//                }
//
//                mAdapter.setmListComment(mCommentList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.image_send)
    void onclickSend() {

        sendComment();
        mInputComment.setText("");

    }

    private void initFirebase() {

        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        mCommentRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_COMMENT);
        mRef = FirebaseDatabase.getInstance().getReference();

    }

    private void sendComment() {

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());
        String commentId = mCommentRef.push().getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("currentId", Constants.UID);
        hashMap.put("avatar", Constants.UAVATAR);
        hashMap.put("name", Constants.UNAME);
        hashMap.put("idComment", commentId);
        hashMap.put("content_comment", mInputComment.getText().toString());
        hashMap.put("date", mSaveCurrentDate);
        hashMap.put("time", mSaveCurrentTime);
        hashMap.put("type", MessageTypeConfig.TEXT);

        mCommentRef.child(mPostId).child(commentId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(CommentActivity.this, "send comment id successfull", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRcv() {
        mAdapter = new CommentAdapter();
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