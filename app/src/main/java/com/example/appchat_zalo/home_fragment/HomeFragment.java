package com.example.appchat_zalo.home_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.home_fragment.adapter.HomePostAdapter;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    private HomePostAdapter mHomePostAdapter = new HomePostAdapter();


    List<Posts> postList = new ArrayList<>();

    @BindView(R.id.list_posts_friends)
    RecyclerView mRcvHomePost;

    @BindView(R.id.image_avatar1)
    ImageView mImageAvatar;

    @BindView(R.id.text_posts)
    TextView mTextPost;

    private DatabaseReference mUserRef, mPostRef, mFriendRef, mRef, mLikeRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);

        initFirebase();
        innitRcvPost();
        getListPosts();

        getUser();
        return view;
    }

    private void getUser() {
        mUserRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);
                Glide.with(getActivity())
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mImageAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        mPostRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_POSTS);
        mUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_USERS);
        mFriendRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_FRIEND);
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void getListPosts() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Posts> listPost = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(Constants.UID).getChildren()) {

                    if (data.getValue(String.class).equals(UserRelationshipConfig.FRIEND)) {
                        String idFriend = data.getKey();

                        for (DataSnapshot PostData : dataSnapshot.child(Constants.TABLE_POSTS).child(idFriend).getChildren()){
                            listPost.add(PostData.getValue(Posts.class));
                            Log.d("a", "onDataChange: post" +listPost.toString());
                        }
                    }

                }
                mHomePostAdapter.setmPostList(listPost);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mFriendRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String idFriend = data.getKey();
//                    Log.d("a", "idFriend:===" + idFriend);
//
//                    mPostRef.child(idFriend).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot  friendData : dataSnapshot.getChildren()){
//                                Posts posts =  friendData.getValue(Posts.class);
//                                mHomePostAdapter.adddPost(posts);
////                                postList.add(posts);
////                                Log.d("a", "listpost ===" + postList.toString());
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
////                    mPostRef.child(idFriend).addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            for (DataSnapshot friendPost : dataSnapshot.getChildren()) {
////
////                                Posts posts = friendPost.getValue(Posts.class);
////                                postList.add(posts);
////
////                                Log.d("a", "listPost==" + postList.toString());
////
//////                                mHomePostAdapter.adddPost(posts);
////                            }
////                            Log.d("a", "listPost==" + postList.toString());
////
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                        }
////                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//

    }


    private void innitRcvPost() {
        mRcvHomePost.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvHomePost.setHasFixedSize(true);
        mRcvHomePost.setAdapter(mHomePostAdapter);
    }

}
