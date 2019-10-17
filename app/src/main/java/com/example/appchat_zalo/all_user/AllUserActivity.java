package com.example.appchat_zalo.all_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.all_user.adapter.AllUserAdapter;
import com.example.appchat_zalo.all_user.listener.OnclickItemUserListener;
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

public class AllUserActivity extends AppCompatActivity {

    @BindView(R.id.list_all_user)
    RecyclerView mRcvAllUser;

    @BindView(R.id.toolbar_all_user)
    Toolbar mToolbarAllUser;

    private AllUserAdapter mAdapter;

    private List<Users> mListUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_user_activity);
        ButterKnife.bind(this);
        initToolbar();
        initRcv();

        getListAllUser();

    }


    private void initToolbar() {
        setSupportActionBar(mToolbarAllUser);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getListAllUser() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListUser.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Users users = data.getValue(Users.class);
                    assert users != null;
                    assert user != null;
                    if (!user.getUid().equals(users.getId())) {
                        mListUser.add(users);
                    }
                }
                mAdapter = new AllUserAdapter(new OnclickItemUserListener() {
                    @Override
                    public void onClickFriendOnlineItem(Users users) {

                        Intent intent = new Intent(AllUserActivity.this, UserProfileActivity.class);
                        intent.putExtra("userId", users.getId());
                        startActivity(intent);
                    }
                });
                mAdapter.setmListUSer(mListUser);
                mRcvAllUser.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcv() {
        mRcvAllUser.setLayoutManager(new LinearLayoutManager(this));
        mRcvAllUser.setHasFixedSize(true);
    }

}
