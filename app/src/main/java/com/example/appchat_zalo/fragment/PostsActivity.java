package com.example.appchat_zalo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.add_posts.adapter.AddPostAdapter;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostsActivity extends AppCompatActivity {

    private List<Bitmap> mistBitmap;
    private AddPostAdapter mAddPostAdapter;
    private static final int IMAGE_CHOOSE = 1;
    private static final int IMAGE_PHOTO = 2;
    private Uri mUrl;

    private String mSaveCurrentDate;
    private String mSaveCurrentTime;
    private String mPostRandomName;
    private String urlDownload;
    private ProgressDialog progressDialog;

    FirebaseUser user;
    DatabaseReference refUser, refPost;
    private StorageReference storageReference;

    @BindView(R.id.toolbar_post)
    Toolbar mToolbarPosts;

    @BindView(R.id.image_avatar)
    ImageView mAvatar;

    @BindView(R.id.text_name)
    TextView mName;

    @BindView(R.id.text_add_content)
    TextView mTextAddContent;

    @BindView(R.id.image_picture)
    ImageView mPicture;

    @BindView(R.id.image_camera)
    ImageView mCamera;

    @BindView(R.id.image_video)
    ImageView mVideo;

    @BindView(R.id.input_content_posts)
    EditText mInputsContentPost;

    @BindView(R.id.list_add_post)
    RecyclerView mRcvAddPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        initToolbarr();
        intiRcv();
        initFirebase();
    }

    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        refUser = FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.UID);
        refPost = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_POSTS).child(Constants.UID);

        storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(this)
                .load(Constants.UAVATAR)
                .circleCrop()
                .into(mAvatar);
        mName.setText(Constants.UNAME);
//    }
    }

    private void intiRcv() {
        mistBitmap = new ArrayList<>();
        mRcvAddPosts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRcvAddPosts.setHasFixedSize(true);

    }

    private void initToolbarr() {
        setSupportActionBar(mToolbarPosts);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.image_picture)
    void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);
    }

    @OnClick(R.id.image_camera)
    void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IMAGE_PHOTO);
        }
    }

    @OnClick({R.id.text_posts})
    void post() {
        ValidatePostInfo();
    }

    private void ValidatePostInfo() {

        String textContentPost = mInputsContentPost.getText().toString();
        if (mPicture == null) {
            Toast.makeText(this, "Please choose Picture...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(textContentPost)) {
            Toast.makeText(this, "Please say something about....", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("Add New Posts");
            progressDialog.setMessage("Please  wait for minute!!!...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);
            storagePictureToFirebase();
        }
    }

    private void storagePictureToFirebase() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());

        mPostRandomName = mSaveCurrentDate + mSaveCurrentTime;
        Log.i("aa", "storagePictureToFirebase:  " +mUrl.getLastPathSegment());


        StorageReference filePath = storageReference.child("UploadPost").child(mUrl.getLastPathSegment() + mPostRandomName + ".jpg");


        filePath.putFile(mUrl).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri urlDowlosd = (Uri) task.getResult();
                urlDownload = urlDowlosd.toString();
                Toast.makeText(PostsActivity.this, "Picture is aup loading successful!!!", Toast.LENGTH_SHORT).show();
                savePostsInfoToDatabase();
            } else {
                String message = task.getException().getMessage();
                Toast.makeText(PostsActivity.this, "Picture update is fail:" + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePostsInfoToDatabase() {

        String postId = refPost.push().getKey();

        refPost.child(postId).setValue(new Posts(mSaveCurrentDate, mSaveCurrentTime,
                mInputsContentPost.getText().toString(), urlDownload, Constants.UAVATAR, Constants.UNAME, postId))
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("time", "time_post");
//                        bundle.putString("date", "date_post");
//                        bundle.putString("content_posts", "content_post");
//                        bundle.putString("picture", "picture_post");
//                        bundle.putString("avatar", "avatar_post");
//                        bundle.putString("name", "name_post");
//
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        ProfileFragment profileFragment =  new ProfileFragment();
//                        profileFragment.setArguments(bundle);
//                        fragmentManager.beginTransaction().replace(R.id.frame_layout_post, profileFragment).commit();
                        Toast.makeText(PostsActivity.this, "New posts is updated successful!!!", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    } else {

                        Toast.makeText(PostsActivity.this, "New posts is updated Fail!!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                mUrl = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(mUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mistBitmap.add(bitmap);
                    mAddPostAdapter = new AddPostAdapter(mistBitmap);
                    mRcvAddPosts.setAdapter(mAddPostAdapter);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_PHOTO) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                mistBitmap.add(bitmap);
                mAddPostAdapter = new AddPostAdapter(mistBitmap);
                mRcvAddPosts.setAdapter(mAddPostAdapter);
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

    }


//    private String getFileImage(Uri uri) {
//        ContentResolver contentResolver = getApplicationContext().getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }

}
