package com.example.appchat_zalo.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.notification.adapter.NotificationAdapter;
import com.example.appchat_zalo.notification.model.Notification;
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

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_notification)
    Toolbar mToolbarNotification;

    @BindView(R.id.text_notification)
    TextView mTextNotification;

    @BindView(R.id.list_notification)
    RecyclerView mRcvNotification;

    private List<Notification> mNotiList;
    private NotificationAdapter mNotiAdapter;

    private DatabaseReference mNotiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        initRcv();
        initToolbarNotifications();
        getNotification();


    }

    private void initToolbarNotifications() {
        setSupportActionBar(mToolbarNotification);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getNotification() {
        mNotiRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NOTIFICATION).child(Constants.UID);
        mNotiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotiList.clear();
                for (DataSnapshot dataNoti : dataSnapshot.getChildren()){
                    Notification noti = dataNoti.getValue(Notification.class);
                    Log.d("NotificationActivity", "onDataChange: notication" + noti.toString());

                    mNotiList.add(noti);
                }
                mNotiAdapter.setmNotiList(mNotiList);
//                Collections.reverse(mNotiList);
//                mNotiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcv() {
        mNotiList = new ArrayList<>();
        mNotiAdapter =  new NotificationAdapter();
        mRcvNotification.setLayoutManager(new LinearLayoutManager(this));
        mRcvNotification.setHasFixedSize(true);
        mRcvNotification.setAdapter(mNotiAdapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
