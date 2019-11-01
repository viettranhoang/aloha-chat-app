package com.example.appchat_zalo.choose_friends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.group_message.GroupMessageActivity;
import com.example.appchat_zalo.Message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.Message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.choose_friends.adapter.ChooseVerticalAdapter;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.search.SearchActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseActivity extends AppCompatActivity {

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

    private DatabaseReference mRef, mGroupRef, mMessRef;

    private ChooseVerticalAdapter mVerticalAdapter;

    private String type = UserRelationshipConfig.FRIEND;

    private StorageReference mStorageReference;

    private static final int IMAGE_CHOOSE = 1;

    private StorageTask mUpLoadTask;
    private Uri mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);
        ButterKnife.bind(this);
        initToolbar();
        initRcv();
        initFirebase();
        getFriendList(Constants.UID, type);
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
//        mStorageReference = FirebaseStorage.getInstance().getReference().child("upload_avatar_group");
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
        Users users = new Users(Constants.UID, Constants.UNAME,"xv",Constants.UAVATAR,Constants.UCOVER,"","",12);
        usersList.add(users);
        String name = mInputNameGroupp.getText().toString();
        String idGroup = String.format("%s_%s", Constants.GROUP_ID, mGroupRef.push().getKey());
        String avatar = "data:image/jpeg;base64,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";

        if(name.isEmpty()){
            Toast.makeText(this, R.string.choose_friend_invalid_name, Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> member = new ArrayList<>();
        for (Users user : usersList) {
            member.add(user.getId());
        }
        Log.d("vbb", "createGroups: name" + name);
        Groups groups = new Groups(idGroup, name, avatar, member);
        mGroupRef.child(idGroup).setValue(groups);

        Message message = new Message("Hi", Constants.UID, true, System.currentTimeMillis(), MessageTypeConfig.TEXT);
        String key = mMessRef.child(Constants.UID).child(groups.getId()).push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        for (String mem : groups.getMembers()) {
            hashMap.put(String.format("/%s/%s/%s", mem, groups.getId(), key), message);
        }
        mMessRef.updateChildren(hashMap);
        Intent intent = new Intent(ChooseActivity.this, GroupMessageActivity.class);
        intent.putExtra("groupId", groups.getId());
        startActivity(intent);
        finish();
    }


}
