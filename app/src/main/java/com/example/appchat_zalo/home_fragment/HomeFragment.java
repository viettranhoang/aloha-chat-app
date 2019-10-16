package com.example.appchat_zalo.home_fragment;

import android.os.Bundle;
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


public class HomeFragment extends Fragment {

    private List<Posts> listHomePost = new ArrayList<>();
    private HomePostAdapter mHomePostAdapter = new HomePostAdapter();

    @BindView(R.id.list_posts_friends)
    RecyclerView mRcvHomePost;

    @BindView(R.id.image_avatar1)
    ImageView mImageAvatar;

    @BindView(R.id.text_posts)
    TextView mTextPost;

    private DatabaseReference mUserRef, mPostRef;
    private String mUserId;

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
        mUserRef.addValueEventListener(new ValueEventListener() {
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = user.getUid();
        mPostRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_POSTS);
        mUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_USERS);

    }

    private void getListPosts() {
        mPostRef.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listHomePost.clear();
                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    for (DataSnapshot postData : userData.getChildren()) {
                        Posts posts = postData.getValue(Posts.class);
                        listHomePost.add(posts);
                    }
                }
                mHomePostAdapter.setmPostList(listHomePost);
                mRcvHomePost.setAdapter(mHomePostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void innitRcvPost() {
        listHomePost = new ArrayList<>();
        mRcvHomePost.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvHomePost.setHasFixedSize(true);
    }

}
