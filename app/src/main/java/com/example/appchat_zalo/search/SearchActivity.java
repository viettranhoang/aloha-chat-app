package com.example.appchat_zalo.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.UserProfileActivity;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.search.adapter.SearchAdapter;
import com.example.appchat_zalo.search.listener.OnclikItemSearchListener;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.list_search)
    RecyclerView mRcvSearch;

    @BindView(R.id.input_search)
    EditText mInputSearch;

    @BindView(R.id.search_toolbar)
    Toolbar mToolbarSearch;

    private List<Users> mFriendList;
    private SearchAdapter mAdapterSearch;
    private String type = UserRelationshipConfig.FRIEND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        initToolbarSearch();
        iniRcv();
        mInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getFriend(type);
    }

    private void initToolbarSearch() {
        setSupportActionBar(mToolbarSearch);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getFriend(String type) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataFriend : dataSnapshot.child(Constants.TABLE_FRIEND).child(Constants.UID).getChildren()) {
                    Log.d("SearchActivity", "onDataChange: data  -- " + dataFriend);
                    if (dataFriend.getValue(String.class).equals(type)) {
                        String idFriend = dataFriend.getKey();
                        Log.d("SearchActivity", "onDataChange: id friend is " + idFriend);
                        mFriendList.add(dataSnapshot.child(Constants.TABLE_USERS).child(idFriend).getValue(Users.class));

                    }

                    mAdapterSearch.setmUserList(mFriendList);
                }

//                if(mInputSearch.getText().toString().equals("")) {
//
//                    mFriendList.clear();
//                    for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
//                        Users users = dataUser.getValue(Users.class);
//                        if (users.getId() != null) {
//                            mFriendList.add(users);
//
//                        }
//                    }
//                    mAdapterSearch.setmUserList(mFriendList);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void iniRcv() {
        mFriendList = new ArrayList<>();
        mAdapterSearch = new SearchAdapter(new OnclikItemSearchListener() {
            @Override
            public void onClickSearchItem(Users users) {
                Intent intent = new Intent(SearchActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", users.getId());
                startActivity(intent);
                finish();
            }
        });
        mRcvSearch.setLayoutManager(new LinearLayoutManager(this));
        mRcvSearch.setHasFixedSize(true);
        mRcvSearch.setAdapter(mAdapterSearch);
    }

    private void searchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS).orderByChild("name").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFriendList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Users users = data.getValue(Users.class);
                    if (!users.getId().contains(Constants.UID)) {
                        mFriendList.add(users);
                    }
                }
                mAdapterSearch.setmUserList(mFriendList);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
