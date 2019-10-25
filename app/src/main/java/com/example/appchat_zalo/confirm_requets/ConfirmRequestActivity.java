package com.example.appchat_zalo.confirm_requets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.confirm_requets.adpater.ConfirmRequestAdapter;
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

public class ConfirmRequestActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_confirm_request)
    Toolbar mToolbarConfirm;

    @BindView(R.id.list_requets_friend)
    RecyclerView mRcvConfrimRequest;

    private String type = UserRelationshipConfig.RECEIVE;
    DatabaseReference mRef;

    private List<Users> mUserList = new ArrayList<>();
    private ConfirmRequestAdapter mConfirmAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_requets);
        ButterKnife.bind(this);
        initToolbar();
        initRcv();
        initFirebase();
        getRequestFriend(Constants.UID, type);

    }

    private void getRequestFriend(String currentId, String type) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataFriend :  dataSnapshot.child(Constants.TABLE_FRIEND).child(currentId).getChildren()){
                    if(dataFriend.getValue(String.class).equals(type)){
                        String idFriendSend = dataFriend.getKey();
                        Log.d("aa", "onDataChange: ..id  friend requets receive " + idFriendSend);
                        mUserList.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriendSend).getValue(Users.class));
                    }
                }
                mConfirmAdapter.setmUserList(mUserList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void initRcv() {

        mConfirmAdapter =  new ConfirmRequestAdapter();
        mRcvConfrimRequest.setLayoutManager(new LinearLayoutManager(this));
        mRcvConfrimRequest.setHasFixedSize(true);;
        mRcvConfrimRequest.setAdapter(mConfirmAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarConfirm);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
