package com.example.appchat_zalo.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.add_user.SentInviteActivity;
import com.example.appchat_zalo.confirm_requets.ConfirmRequestActivity;
import com.example.appchat_zalo.friends.adapter.FriendNewsAdapter;
import com.example.appchat_zalo.friends.adapter.FriendsOnlineAdapter;
import com.example.appchat_zalo.message.MessageActivity;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.news.NewsActivity;
import com.example.appchat_zalo.search.SearchActivity;
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


    @BindView(R.id.list_news)
    RecyclerView mRcvListNews;

    @BindView(R.id.image_add)
    ImageView mImageAdd;

    @BindView(R.id.input_search)
    EditText mInputSearch;

    @OnClick(R.id.image_confirm)
    void displayAllUser() {
        Intent intent = new Intent(getContext(), ConfirmRequestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_add)
    void displayAddUser() {
        Intent intent = new Intent(getContext(), SentInviteActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.input_search})
    void clickSearch() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    private FriendNewsAdapter mNewsAdapter = new FriendNewsAdapter(users -> {
        NewsActivity.Companion.openNewsActivity(getActivity(), users);
    });

    private FriendsOnlineAdapter mFriendAdapter = new FriendsOnlineAdapter(users -> {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("userId", users.getId());
        intent.putExtra("avatar_user", users.getAvatar());
        startActivity(intent);
    });

    private DatabaseReference mRef;

    private String type = UserRelationshipConfig.FRIEND;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        initFireBase();

        ButterKnife.bind(this, view);

        initRcv();

        getListFriend(Constants.UID, type);

        getListNews(type);
        return view;
    }

    private void getListNews(String type) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Users> listNewsFriend = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(Constants.UID).getChildren()) {
                    Log.d("FriendsFragment", "onDataChange: data "  +  data );
                    if (data.getValue(String.class).equals(type)) {
                        String idFriend = data.getKey();
                        listNewsFriend.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));
                    }
                }

                mNewsAdapter.setmUserNewsList(listNewsFriend);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcv() {
        mRcvListFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvListFriend.setHasFixedSize(true);
        mRcvListFriend.setAdapter(mFriendAdapter);

        mRcvListNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRcvListNews.setHasFixedSize(true);
        mRcvListNews.setAdapter(mNewsAdapter);

    }

    private void initFireBase() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void getListFriend(String currentId, String type) {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Users> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(Constants.UID).getChildren()) {
//                    Log.d("FriendsFragment", "onDataChange: data" +data);
                    Log.d("FriendsFragment", "onDataChange: id" + Constants.UID);
                    Log.d("FriendsFragment", "onDataChange: data" +data.getValue());

                    if (data.getValue(String.class).equals(type)) {
                        String idFriend = data.getKey();
                        list.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));
                    }
                }

                mFriendAdapter.setUsersList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
