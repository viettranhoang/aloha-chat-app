package com.example.appchat_zalo.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
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

    private List<Users> mUserList;
    private SearchAdapter mAdapterSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
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
        getUser();

    }

    private void getUser() {
        DatabaseReference  userRef =  FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mInputSearch.getText().toString().equals("")) {

                    mUserList.clear();
                    for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
                        Users users = dataUser.getValue(Users.class);
                        if (!users.getId().contains(Constants.UID)) {
                            mUserList.add(users);

                        }
                    }
                    mAdapterSearch.setmUserList(mUserList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void iniRcv() {
        mUserList = new ArrayList<>();
        mAdapterSearch =  new SearchAdapter(new OnclikItemSearchListener() {
            @Override
            public void onClickSearchItem(Users users) {
                Intent intent = new Intent(SearchActivity.this, MessageActivity.class);
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
        Query query = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS).orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Users users =  data.getValue(Users.class);
                    if(!users.getId().contains(Constants.UID)){
                        mUserList.add(users);
                    }
                }
                mAdapterSearch.setmUserList(mUserList);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
