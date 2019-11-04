package com.example.appchat_zalo.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchat_zalo.UpdatePostActivity;
import com.example.appchat_zalo.LoginWithEmailActivity;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.cache.PrefUtils;
import com.example.appchat_zalo.my_profile.adapter.ProfilePostsAdapter;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ImageView mImageLogout;
    private ImageView mImageBack;
    private ImageView mImageCover;
    private ImageView mImageAvatar;
    private ImageView mImageEditAvatar;
    private ImageView mImageEditCover;
    private TextView mTextName;
    private TextView mTextStatus;
    private ImageView mImageAvatar1;

    @BindView(R.id.image_news)
    ImageView mImageNews;

    @BindView(R.id.image_my_news)
    ImageView mImageMyNews;

    private TextView mTextPost;

    @BindView(R.id.list_my_posts)
    RecyclerView mRcvListMyPost;

    private FirebaseUser user;
    private StorageReference mStorageReference, mStorgeNews;
    private DatabaseReference refUser, refPosts;

    private static final int IMAGE_CHOOSE = 1;

    private static final int IMAGE_PHOTO = 2;

    private StorageTask mUpLoadTask;
    private Uri mUrl;
    private boolean isUpdateAvatar = true;
    private boolean isUpdateNews = true;

    private PrefUtils prefUtils;
    private ProfilePostsAdapter profilePostsAdapter;
    private List<Posts> listMyPosts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        prefUtils = PrefUtils.getIntance(getActivity());
        initRCVMyPosts();
        initFirebase();
        getMyPost();
        mImageLogout = view.findViewById(R.id.image_more);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        mImageAvatar1 = view.findViewById(R.id.image_avatar1);
        mImageBack = view.findViewById(R.id.image_back);
        mTextPost = view.findViewById(R.id.text_posts);
        mImageCover = view.findViewById(R.id.image_background);
        mImageEditAvatar = view.findViewById(R.id.image_edit_avatar);
        mImageEditCover = view.findViewById(R.id.image_edit_cover);
        mTextName = view.findViewById(R.id.text_name);
        mTextStatus = view.findViewById(R.id.text_status);

//        Bundle bundle = this.getArguments();
//        if (bundle != null){
//
//            bundle.putString("time", "time");
//            String time = bundle.getString("time");
//            String date = bundle.getString("date");
//            String content_posts = bundle.getString("content_posts");
//            String picture = bundle.getString("time");
//            String avatar = bundle.getString("picture");
//            String name = bundle.getString("name");
//            Log.d("aa", "onCreateView: ddd" + time);
//            Log.d("aa", "onCreateView: ddd" + date);
//            Log.d("aa", "onCreateView: ddd" + content_posts);
//            Log.d("aa", "onCreateView: ddd" + picture);
//            Log.d("aa", "onCreateView: ddd" + avatar);
//            Log.d("aa", "onCreateView: ddd" + name);
//        }

        refUser.child(Constants.UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);

                if (Constants.UID.equals(users.getId())) {
                    mTextStatus.setText(users.getStatus());
                    mTextName.setText(users.getName());
                    Constants.UNAME = users.getName();
                    Constants.UAVATAR = users.getAvatar();

                    Glide.with(getActivity())
                            .load(users.getAvatar())
                            .circleCrop()
                            .into(mImageAvatar1);
                    if (users.getCover().equals("default")) {
                        mImageCover.setImageResource(R.drawable.anhbia1);
                    } else {
                        Glide.with(getActivity())
                                .load(users.getCover())
                                .centerCrop()
                                .into(mImageCover);
                    }

                    if (users.getAvatar().equals("default")) {
                        mImageAvatar.setImageResource(R.drawable.background_main);
                    } else {
                        Glide.with(getActivity())
                                .load(users.getAvatar())
                                .circleCrop()
                                .into(mImageAvatar);
                    }


                    if (users.getNews().equals("default")) {
                        mImageNews.setImageResource(R.drawable.boder_news_gray);
                    } else {

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

                        Glide.with(getActivity())
                                .load(users.getNews())
                                .apply(requestOptions)
                                .into(mImageMyNews);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addListner();

        return view;
    }
    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        refUser = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        refPosts = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mStorgeNews = FirebaseStorage.getInstance().getReference("upload_news");
    }

    private void initRCVMyPosts() {
        mRcvListMyPost.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvListMyPost.setHasFixedSize(true);

    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }

    private void addListner() {
        mImageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
                alertdialog.setTitle("Logout");
                alertdialog.setMessage("Are you sure you Want to logOut??");
                alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                    }
                });

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                AlertDialog alert = alertdialog.create();
                alertdialog.show();
            }


        });

        mImageEditCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                isUpdateAvatar = false;
                isUpdateNews =  false;
            }
        });


        mImageEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                isUpdateAvatar = true;
                isUpdateNews = false;
            }
        });


        mTextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostsActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.image_news)
    void onClickNews(){
       chooseImageNews();
       isUpdateNews =  true;

    }

    private void chooseImageNews() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);
    }

//    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//        mUrl = Uri.fromFile(photo);
//        ProfileFragment.this.startActivityForResult(intent, IMAGE_PHOTO);
//
//    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        prefUtils.clearAllKey();
        Intent intent = new Intent(getActivity().getApplication(), LoginWithEmailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private String getFileImage(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageNews() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(mUrl != null){
            final  StorageReference storageReference = mStorgeNews.child(System.currentTimeMillis() + "." + getFileImage(mUrl));
            mUpLoadTask = storageReference.putFile(mUrl);
            mUpLoadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
//                        Toast.makeText(getContext(), "upload image fail", Toast.LENGTH_SHORT).show();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri urlDowlaod = (Uri) task.getResult();
                        String mUriDowload = urlDowlaod.toString();
                        refUser = FirebaseDatabase.getInstance().getReference("Users").child(Constants.UID);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        if (isUpdateNews)
                        hashMap.put("news", mUriDowload);
                        refUser.updateChildren(hashMap);

                        Toast.makeText(getContext(), "get news  succes full", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            });

        }
    }

    private void upLoadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (mUrl != null) {
            final StorageReference storageFile = mStorageReference.child(System.currentTimeMillis() + "." + getFileImage(mUrl));
            mUpLoadTask = storageFile.putFile(mUrl);
            mUpLoadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
//                        Toast.makeText(getContext(), "upload image fail", Toast.LENGTH_SHORT).show();
                }
                return storageFile.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri urlDowlaod = (Uri) task.getResult();
                    String mUriDowload = urlDowlaod.toString();
                    refUser = FirebaseDatabase.getInstance().getReference("Users").child(Constants.UID);
                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("news", mUriDowload);
                    if (isUpdateAvatar == true && isUpdateNews == false) hashMap.put("avatar", mUriDowload);
                    else if (isUpdateNews == false && isUpdateAvatar == false)hashMap.put("cover", mUriDowload);
//                    if(isAddNews) hashMap.put("news", mUriDowload);
                    refUser.updateChildren(hashMap);
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });

        } else {
            Toast.makeText(getContext(), "Not Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(getContext(), "image  is uploading", Toast.LENGTH_SHORT).show();
            } else  if(isUpdateNews) {
                uploadImageNews();
            }
            else {

                upLoadImage();
            }
        }

//        else if(resultCode == Activity.RESULT_OK){
//
//            Uri selectedImage = mUrl;
//            getActivity().getContentResolver().notifyChange(selectedImage, null);
//            ContentResolver cr = getActivity().getContentResolver();
//            Bitmap bitmap;
//            try {
//                bitmap = android.provider.MediaStore.Images.Media
//                        .getBitmap(cr, selectedImage);
//            mImageNews.setImageBitmap(bitmap);
//                Toast.makeText(getActivity(), selectedImage.toString(),
//                        Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
//                        .show();
//                Log.e("Camera", e.toString());
//            }
//
//        }

    }



    private void getMyPost() {
        refPosts.child(Constants.UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMyPosts.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Posts posts = data.getValue(Posts.class);
                    listMyPosts.add(posts);
                }

                Log.i("ha", "onDataChangePost: " + listMyPosts.toString());
                profilePostsAdapter = new ProfilePostsAdapter(post -> {
                    Intent intent = new Intent(getContext(), UpdatePostActivity.class);
                    intent.putExtra("idPost",post.getIdPost());
                    startActivity(intent);
                });
                profilePostsAdapter.setListMyPost(listMyPosts);
                mRcvListMyPost.setAdapter(profilePostsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

