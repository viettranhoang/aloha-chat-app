package com.example.appchat_zalo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.all_user.AllUserActivity;
import com.example.appchat_zalo.friends.adapter.FriendsOnlineAdapter;
import com.example.appchat_zalo.friends.listener.OnclickItemFriendListener;
import com.example.appchat_zalo.friends.model.Friend;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsFragment extends Fragment {

    @BindView(R.id.list_friend_online)
    RecyclerView mRcvListFriend;

    @BindView(R.id.image_contact)
    ImageView mImageContact;

    private FriendsOnlineAdapter friendsOnlineAdapter =  new FriendsOnlineAdapter();

    private String mOnlineUserId;

    private DatabaseReference mFriendRef, mUserRef;

    private FirebaseAuth mAthu;

    private String type = UserRelationshipConfig.FRIEND;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        initFireBase();

        ButterKnife.bind(this, view);

        initRcv();


        getListFriendOnline(Constants.UID, type );
        return view;
    }

    @OnClick(R.id.image_contact)
    void displayAllUser(){
        Intent intent =  new Intent(getContext(), AllUserActivity.class);
        startActivity(intent);
    }

    private void initRcv() {
        mRcvListFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvListFriend.setHasFixedSize(true);
        mRcvListFriend.setAdapter(friendsOnlineAdapter);
    }

    private void initFireBase() {
        mAthu = FirebaseAuth.getInstance();
        mOnlineUserId =  mAthu.getCurrentUser().getUid();
        mFriendRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_FRIEND);
        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);

    }

    private void getListFriendOnline(String currentId, String type) {
        mFriendRef.child(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getValue(String.class).equals(type)){
                        String  idFriend =  data.getKey(); // lay đc id friend
                        Log.i("aaaaa", "onDataChange: " + idFriend);//dung

                        //get user theo id
                        mUserRef.child(idFriend).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Users users =  dataSnapshot.getValue(Users.class);
                                friendsOnlineAdapter.addUser(users);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//                        mUserRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()){
//                                    Users users = data.getValue(Users.class);
//                                    if(users.getId() == idFriend){
//                                        listUser.add(users);
//                                    }
//                                }
//                                friendsOnlineAdapter.setUsersList(listUser);
//                                mRcvListFriend.setAdapter(friendsOnlineAdapter);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
                    }

//        mFriendRef.child(currentId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    if(data.getValue(String.class).contains(type)){
////                        Users users =  dataSnapshot.getValue(Users.class);
////                        listUser.add(users);
//                        Log.d("aaa","onDataChange" +listUser);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        mFriendRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listUser.clear();
//                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(currentId).getChildren()){
//                    if(data.getValue(String.class).contains(type)){
//                        String  idFriend = data.getKey();
//                        listUser.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));
//                    }
////                    Users users =  data.getValue(Users.class);
//////                    assert  users != null;
//////                    assert user != null;
////                    listUser.add(users);
//
//                }
//
//                Log.i("aaaaa", "onDataChange: " +listUser.toString());
//                friendsOnlineAdapter = new FriendsOnlineAdapter(getContext(), listUser, new OnclickItemFriendListener() {
//                    @Override
//                    public void onClickFriendOnlineItem(Users users) {
//                        Intent intent =  new Intent(getContext(), MessageActivity.class);
//                        intent.putExtra("userId",users.getId());
//                        startActivity(intent);
//
//                    }
//                });
//                friendsOnlineAdapter.setUsersList(listUser);
//                mRcvListFriend.setAdapter(friendsOnlineAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




}
