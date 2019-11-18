package com.example.appchat_zalo.choose_friends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.MessageMeetingGroupActivity;
import com.example.appchat_zalo.add_posts.adapter.AddPostAdapter;
import com.example.appchat_zalo.fragment.PostsActivity;
import com.example.appchat_zalo.group_message.GroupMessageActivity;
import com.example.appchat_zalo.message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.choose_friends.adapter.ChooseVerticalAdapter;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.search.SearchActivity;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseActivity extends AppCompatActivity {

    private static final String TAG = " ChooseAcitivity" ;
    @BindView(R.id.choose_toolbar)
    Toolbar mToolbarChoose;

    @BindView(R.id.input_search)
    EditText mInputSearch;

    @BindView(R.id.image_create_group)
    ImageView mImageCreateGroup;

    @BindView(R.id.list_choose_friend_vertical)
    RecyclerView mRcvVertical;

    @BindView(R.id.image_upload_avatar)
    ImageView mImageUploadAvatar;

    @BindView(R.id.input_name_group)
    EditText mInputNameGroupp;

    @BindView(R.id.spinner_group)
    Spinner mSpinnerGroup;

    private String[] typeGroup = {"meeting group", "learning group", "dating group"};

    private DatabaseReference mRef, mGroupRef, mMessRef;

    private ChooseVerticalAdapter mVerticalAdapter;

    private String type = UserRelationshipConfig.FRIEND;

    private StorageReference mStorageReference;

    private Uri mUrl;
    private String urlDownload;
    private String mSaveTime;
    private String mSaveDate;
    private String mPostRandomName;
    private ProgressDialog progressDialog;

    private static final int IMAGE_CHOOSE = 1;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);
        ButterKnife.bind(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,typeGroup);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGroup.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        initToolbar();
        initRcv();
        initFirebase();
        getFriendList(Constants.UID, type);
        Glide.with(this).load(mUrl).into(mImageCreateGroup);
    }

    @OnClick(R.id.input_search)
    void onClickSearch() {
        Intent intent = new Intent(ChooseActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_create_group)
    void onClickCreateGroup() {
        System.out.println("abcd " + mVerticalAdapter.getChoosedList());
        if (mVerticalAdapter.getChoosedList().isEmpty()) {
            Toast.makeText(this, getString(R.string.choose_friend_error), Toast.LENGTH_SHORT).show();
        } else {
            createGroups(mVerticalAdapter.getChoosedList());
        }
    }

    @OnClick(R.id.image_upload_avatar)
    void onclickUploadAvatar() {
        chooseImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            progressDialog.setMessage("Uploding image...");
            progressDialog.show();
            mUrl = data.getData();
            if (mUrl == null) {

                Toast.makeText(this, "YOUR URI IS NULL", Toast.LENGTH_LONG).show();
            }

            Calendar calendarDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            mSaveDate = currentDate.format(calendarDate.getTime());

            Calendar calendarTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            mSaveTime = currentTime.format(calendarDate.getTime());
            mPostRandomName = mSaveDate + mSaveTime;
            StorageReference filePath = mStorageReference.child("upload_avatar_group").child(mUrl.getLastPathSegment() + mPostRandomName + ".jpg");

            filePath.putFile(mUrl).continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {
                        Uri urlDowlosd = (Uri) task.getResult();
                        urlDownload = urlDowlosd.toString();
                        Toast.makeText(ChooseActivity.this, "Picture is aup loading successful!!!", Toast.LENGTH_SHORT).show();
                        createGroups(mVerticalAdapter.getChoosedList());
                        progressDialog.dismiss();
                    }
                }
            });
//
        } else {
            Toast.makeText(ChooseActivity.this, "Upload Failed...Check your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);
    }

    private void getFriendList(String currentId, String type) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Users> list = new ArrayList<>();
                for (DataSnapshot dataFriend : dataSnapshot.child(Constants.TABLE_FRIEND).child(currentId).getChildren()) {
                    if (dataFriend.getValue(String.class).equals(type)) {
                        String idFriend = dataFriend.getKey();
                        list.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));
                    }
                }
                mVerticalAdapter.setmUserList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initFirebase() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mGroupRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_GROUPS);
        mMessRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_MESSAGE);
        mStorageReference = FirebaseStorage.getInstance().getReference();

    }

    private void initRcv() {
        mVerticalAdapter = new ChooseVerticalAdapter();
        mRcvVertical.setLayoutManager(new LinearLayoutManager(this));
        mRcvVertical.setHasFixedSize(true);
        mRcvVertical.setAdapter(mVerticalAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarChoose);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void createGroups(List<Users> usersList) {
        Users users = new Users(Constants.UID, Constants.UNAME, "xv", Constants.UAVATAR, Constants.UCOVER, "", "", 12);
        Constants.UAVATAR = users.getAvatar();
        Constants.UNAME = users.getName();
        Log.d("ChooseActivity", "createGroups: name avatar" + Constants.UNAME + Constants.UAVATAR);
        usersList.add(users);
        String name = mInputNameGroupp.getText().toString();
        String idGroup = String.format("%s_%s", Constants.GROUP_ID, mGroupRef.push().getKey());

        Glide.with(this)
                .load(urlDownload)
                .circleCrop()
                .into(mImageUploadAvatar);

        String avatar = "https://images.penguinrandomhouse.com/cover/9781632368324";

        if (name.isEmpty() && avatar == null) {
            Toast.makeText(this, R.string.choose_friend_invalid_name, Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> member = new ArrayList<>();
        for (Users user : usersList) {
            member.add(user.getId());
        }

        Log.d("vbb", "createGroups: name" + name);
        String groupType = ChooseTypeConfig.MEETING_GROUP;

        Groups groups = new Groups(idGroup, name, urlDownload, member);
        mGroupRef.child(idGroup).setValue(groups);
        Log.d("ChooseActivity", "createGroups: avaatrt " + Constants.UAVATAR);

        Message message = new Message("Hi", Constants.UID, true, System.currentTimeMillis(), MessageTypeConfig.TEXT, Constants.UAVATAR);
        String key = mMessRef.child(Constants.UID).child(groups.getId()).push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        for (String mem : groups.getMembers()) {
            hashMap.put(String.format("/%s/%s/%s", mem, groups.getId(), key), message);
        }
        mMessRef.updateChildren(hashMap);
//        Intent intent1 = new Intent(ChooseActivity.this, MessageMeetingGroupActivity.class);
//        intent1.putExtra("groupId", groups.getId());
//        startActivity(intent1);
//        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
