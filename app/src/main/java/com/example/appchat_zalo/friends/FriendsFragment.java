package com.example.appchat_zalo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
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
import butterknife.OnClick;

public class FriendsFragment extends Fragment {

    @BindView(R.id.list_friend_online)
    RecyclerView mRcvListFriend;

    @BindView(R.id.image_contact)
    ImageView mImageContact;

    private FriendsOnlineAdapter friendsOnlineAdapter = new FriendsOnlineAdapter(new OnclickItemFriendListener() {
        @Override
        public void onClickFriendOnlineItem(Users users) {
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra("userId", users.getId());
            startActivity(intent);
        }
    });

    private DatabaseReference mRef, mFriendRef, mUserRef;

    private String type = UserRelationshipConfig.FRIEND;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        initFireBase();

        ButterKnife.bind(this, view);

        initRcv();


        getListFriendOnline(Constants.UID, type);
        return view;
    }

    @OnClick(R.id.image_contact)
    void displayAllUser() {
        Intent intent = new Intent(getContext(), AllUserActivity.class);
        startActivity(intent);
    }

    private void initRcv() {
        mRcvListFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvListFriend.setHasFixedSize(true);
        mRcvListFriend.setAdapter(friendsOnlineAdapter);
    }

    private void initFireBase() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mFriendRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_FRIEND);
        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);

    }

    private void getListFriendOnline(String currentId, String type) {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Users> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(Constants.UID).getChildren()) {
                    if (data.getValue(String.class).equals(type)) {
                        String idFriend = data.getKey();
                        list.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));

                    }

                }

                friendsOnlineAdapter.setUsersList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
