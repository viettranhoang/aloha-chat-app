package com.example.appchat_zalo.add_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.add_user.adapter.SentInviteAdapter;
import com.example.appchat_zalo.add_user.listener.OnclickItemAddUser;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SentInviteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_user)
    Toolbar mToolbarAddUser;

    @BindView(R.id.list_add_user)
    RecyclerView mRcvAddUser;

    private String type = UserRelationshipConfig.SENT;
    private DatabaseReference mRef;


    private SentInviteAdapter mSentInviteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        initToolbar();
        initRcv();
        initFirebase();
        getRequestFriend(Constants.UID, type);
    }

    private void getRequestFriend(String uid, String type) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Users> mUserList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_FRIEND).child(uid).getChildren()) {
                    if (data.getValue(String.class).contains(type)) {
                        String idFriend = data.getKey();
                        mUserList.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));
                    }
                }
                Log.d("aa", "onDataChange: userlist  " + mUserList.toString());
                mSentInviteAdapter.setmUserList(mUserList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcv() {

        mSentInviteAdapter = new SentInviteAdapter(new OnclickItemAddUser() {
            @Override
            public void onclickCancel(Users users) {
                String userId = users.getId();
                mRef.child(Constants.TABLE_FRIEND).child(Constants.UID).child(userId).setValue(UserRelationshipConfig.NOT).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mRef.child(Constants.TABLE_FRIEND).child(userId).child(Constants.UID).setValue(UserRelationshipConfig.NOT).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                });
//                mSentInviteAdapter.deleteUser(users);
            }

            @Override
            public void onclickProfileUser(Users users) {
                Intent intent = new Intent(SentInviteActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", users.getId());
                startActivity(intent);
            }
        });
        mRcvAddUser.setLayoutManager(new LinearLayoutManager(this));
        mRcvAddUser.setHasFixedSize(true);
        mRcvAddUser.setAdapter(mSentInviteAdapter);

    }

    private void initFirebase() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarAddUser);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
