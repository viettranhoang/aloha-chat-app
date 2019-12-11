package com.example.appchat_zalo.detail_post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.comment.CommentTypeConfig;
import com.example.appchat_zalo.comment.adapter.CommentAdapter;
import com.example.appchat_zalo.comment.listener.OnclickCommentItemListener;
import com.example.appchat_zalo.comment.model.Comment;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.notification.model.Notification;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.appchat_zalo.utils.Constants.UID;

public class DetailPostActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    public static void openDetailActivity(Activity activity, String postId, String userId) {
        Intent intent = new Intent(activity, DetailPostActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        intent.putExtra(EXTRA_USER_ID, userId);
        activity.startActivity(intent);
    }

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
    private static final int IMAGE_CHOOSE = 1;
    private String mSaveCurrentDate;
    private String mSaveCurrentTime;
    private String mPostRandomName;
    private CommentAdapter mDetailPostAdapter;
    private String mPostId, mUserId;
    private List<Comment> mCommentList;

    private DatabaseReference mPostRef, mLikeRef, mCommentRef, mUserRef, mNotiRef;
    StorageReference mStorageReference;
    StorageTask mUpLoadTask;
    private Uri mUrl;
    private String urlDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        ButterKnife.bind(this);
        initFirebase();
        initToolbarDetailPost();
        initRcv();
        getPost(Constants.UID, mPostId);
        checkLike(mPostId);
        numberLike(mPostId);
        numberCommet(mPostId);

        getComment();


//        getLikePost(mPostId);

    }

    @OnClick({R.id.image_picture})
    void onclickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for message"), IMAGE_CHOOSE);
    }

    @OnClick(R.id.image_send)
    void onclickText() {

        if (mInputComment.getText().toString().equals("")) {
            Toast.makeText(this, "you can't send comment", Toast.LENGTH_SHORT).show();
        } else {
            sendComment(mInputComment.getText().toString(), CommentTypeConfig.TEXT);
            if (!UID.equals(mUserId)) {
                sendNotifications(mUserId, mPostId);

            }


//            mInputComment.setText("");
        }
    }


    private void sendNotifications(String userId, String postId) {
        String notiId = mNotiRef.push().getKey();
        mNotiRef.child(userId).child(notiId)
                .setValue(new Notification(Constants.UID, R.string.comment_notificationt + mInputComment.getText().toString(), postId, notiId));

    }

    private void sendComment(String content, String type) {

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());

        String timeComment = mSaveCurrentDate + " -- " + mSaveCurrentTime;
        String commentId = mCommentRef.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("content", content);
        hashMap.put("userId", UID);
        hashMap.put("time", timeComment);
        hashMap.put("postId", mPostId);
        hashMap.put("type", type);
        hashMap.put("CommentId", commentId);

        mCommentRef.child(mPostId).child(commentId).setValue(hashMap);
        Toast.makeText(this, R.string.add_comment_post, Toast.LENGTH_SHORT).show();
//        sendNotifications(mPostId);
        mInputComment.setText("");

    }

    private void uploadImage() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());
        mPostRandomName = mSaveCurrentDate + mSaveCurrentTime;
        Log.i("aa", "uploadImage:  " + mUrl.getLastPathSegment());

        StorageReference filePath = mStorageReference.child(mUrl.getLastPathSegment() + mPostRandomName + "jpg");
        filePath.putFile(mUrl).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = (Uri) task.getResult();
                urlDownload = uri.toString();
                Toast.makeText(DetailPostActivity.this, "Picture is aup loading successful!!!", Toast.LENGTH_SHORT).show();
                sendComment(urlDownload, CommentTypeConfig.IMAGE);

            } else {
                String message = task.getException().getMessage();
                Toast.makeText(DetailPostActivity.this, "Picture update is fail:" + message, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(this, "image  is uploading", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }

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
        mDetailPostAdapter = new CommentAdapter(new OnclickCommentItemListener() {
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
        mNotiRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NOTIFICATION);
        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        mStorageReference = FirebaseStorage.getInstance().getReference().child("upload_comment");

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

    private void getPost(String userId, String mPostId) {
        Log.d("DetailPostActivity", "getPost: id user " + userId + mPostId);
        mPostRef.child(Constants.UID).child(mPostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("DetailPostActivity", "onDataChange: data " + dataSnapshot);
                Posts posts = dataSnapshot.getValue(Posts.class);
                Glide.with(DetailPostActivity.this)
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initToolbarDetailPost() {
        Intent intent = getIntent();
        mPostId = intent.getStringExtra(EXTRA_POST_ID);
        mUserId = intent.getStringExtra(EXTRA_USER_ID);
        setSupportActionBar(mToolbarDetailPost);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String avata = Constants.UAVATAR;
        String name = Constants.UNAME;
        Log.d("DetailPostActivity", "initToolbarDetailPost: avatar " + avata + "name" + name);

        Glide.with(this)
                .load(Constants.UAVATAR)
                .circleCrop()
                .into(mAvatar);

        mName.setText(Constants.UNAME);

//        mUserRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users users = dataSnapshot.getValue(Users.class);
//                Log.d("aa", "onDataChange: avatar  user" + users.getAvatar());
//                mName.setText(users.getName());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
