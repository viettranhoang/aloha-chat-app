package com.example.appchat_zalo.choose_friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appchat_zalo.group_message.GroupMessageActivity;
import com.example.appchat_zalo.message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.choose_friends.adapter.ChooseVerticalAdapter;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.search.SearchActivity;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        String avatar = "https://images.penguinrandomhouse.com/cover/9781632368324";

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
