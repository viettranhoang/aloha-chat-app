package com.example.appchat_zalo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.friends.adapter.FriendsOnlineAdapter;
import com.example.appchat_zalo.friends.listener.OnclickItemFriendListener;
import com.example.appchat_zalo.model.Users;
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

public class FriendsFragment extends Fragment {

    @BindView(R.id.list_friend_online)
    RecyclerView mRcvListFriend;

    private FriendsOnlineAdapter friendsOnlineAdapter;
    private List<Users> listUser = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);

        ButterKnife.bind(this, view);
        mRcvListFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvListFriend.setHasFixedSize(true);

        readUser();
        return view;
    }

    private void readUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Users users =  dataSnapshot1.getValue(Users.class);
                    assert  users != null;
                    assert user != null;
                    if(!user.getUid().equals(users.getId())){
                        listUser.add(users);
                    }
                }

                Log.i("aaaaa", "onDataChange: " +listUser.toString());
                friendsOnlineAdapter = new FriendsOnlineAdapter(getContext(), listUser, new OnclickItemFriendListener() {
                    @Override
                    public void onClickFriendOnlineItem(Users users) {
                        Intent intent =  new Intent(getContext(), MessageActivity.class);
                        intent.putExtra("userId",users.getId());
                        startActivity(intent);
                    }
                });
                mRcvListFriend.setAdapter(friendsOnlineAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
