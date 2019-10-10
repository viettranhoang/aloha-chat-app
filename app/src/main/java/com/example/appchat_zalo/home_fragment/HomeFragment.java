package com.example.appchat_zalo.home_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.home_fragment.adapter.HomePostsAdapter;
import com.example.appchat_zalo.model.Posts;
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

    private List<Posts> listHomePost;
    private HomePostsAdapter mHomePostAdapter;

    DatabaseReference refPost;
    FirebaseUser user;

    @BindView(R.id.list_posts_friends)
    RecyclerView mRcvHomePost;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        innitRcvPost();

//        readPosts();
        

        return view;
    }

    private void readPosts() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        refPost = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_POSTS);
        refPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listHomePost.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Posts posts = data.getValue(Posts.class);
                    listHomePost.add(posts);
                }
                mHomePostAdapter.setListHomePost(listHomePost);
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
