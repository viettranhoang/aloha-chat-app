package com.example.appchat_zalo.Message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.Message.adapter.MessageAdapter;
import com.example.appchat_zalo.Message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.Message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.utils.Constants;
import com.example.appchat_zalo.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_message)
    Toolbar mToolbarMessage;

    @BindView(R.id.text_name)
    TextView mTextName;

    @BindView(R.id.list_message)
    RecyclerView mRcvMessage;

    @BindView(R.id.image_avatar)
    ImageView mImageAvatar;

    @BindView(R.id.image_camera)
    ImageView mImageCamera;

    @BindView(R.id.image_picture)
    ImageView mImagePicture;

    @BindView(R.id.image_mic)
    ImageView mImageMic;

    @BindView(R.id.input_message)
    EditText mInputMessage;

    @BindView(R.id.image_icon)
    ImageView mImageIcon;

    @BindView(R.id.image_online)
    ImageView mImageOnline;

    @BindView(R.id.text_online)
    TextView mTextOnline;

    @BindView(R.id.image_send)
    ImageView mImageSend;


    MessageAdapter adapter;
    List<Message> listMessage = new ArrayList<>();
    List<Users> listUsers = new ArrayList<>();

    FirebaseUser user;
    DatabaseReference reference;

    StorageTask mUpLoadTask;
    StorageReference mStorageReference;

    String userId;
    Intent intent;


    private static final int IMAGE_CHOOSE = 1;
    private static final int IMAGE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        ButterKnife.bind(this);
        initToolbar();
        initRcvMessage();
        getUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        getListMessage(userId);

    }

    private void getUser() {
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                listUsers.add(users);
                adapter.setUsers(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRcvMessage() {
        adapter = new MessageAdapter();
        mRcvMessage.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        mRcvMessage.setHasFixedSize(true);
        mRcvMessage.setAdapter(adapter);

    }

    @OnClick(R.id.image_send)
    void OnClickSend() {
        sendMessgae(Constants.UID, userId, mInputMessage.getText().toString(), MessageTypeConfig.TEXT);
        mInputMessage.setText("");
    }

    @OnClick(R.id.image_icon)
    void onClickIcon() {
        sendMessgae(Constants.UID, userId, mInputMessage.getText().toString(), MessageTypeConfig.TEXT);
        mInputMessage.setText("");

    }

    @OnClick(R.id.image_picture)
   void onclickPicture(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);
    }

    @OnTextChanged(R.id.input_message)
    void onTextChangeMessage() {
        if (!TextUtils.isEmpty(mInputMessage.getText())) {
            mImageIcon.setVisibility(View.INVISIBLE);
            mImageSend.setVisibility(View.VISIBLE);
        } else {
            mImageIcon.setVisibility(View.VISIBLE);
            mImageSend.setVisibility(View.INVISIBLE);
        }
    }

    private void getListMessage(final String userId) {

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Message").child(Constants.UID).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMessage.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    listMessage.add(message);
                }
                Log.i("ha", "onDataChange: " + listMessage.toString());
                adapter.setmMessageList(listMessage);
                mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
//                Log.d("a", "onDataChange: lissdfd " + );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
    }

    private void sendMessgae(String from, String receiver, String message, String type) {

        reference = FirebaseDatabase.getInstance().getReference();
        Message message1 = new Message(message, from, false, System.currentTimeMillis(), type);
        HashMap<String, Object> hashMap = new HashMap<>();
        String key = reference.child("Message").child(from).child(receiver).push().getKey();
        hashMap.put("from", from);
        hashMap.put("message", message);
        hashMap.put("seen", message1.isSeen());
        hashMap.put("time", message1.getTime());
        hashMap.put("type", type);

        reference.child("Message").child(from).child(receiver).child(key).updateChildren(hashMap).addOnSuccessListener(aVoid -> {
            mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
        });
        reference.child("Message").child(receiver).child(from).child(key).updateChildren(hashMap).addOnSuccessListener(aVoid -> {
            mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
        });

    }


    private void initToolbar() {
        setSupportActionBar(mToolbarMessage);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);

                Glide.with(getApplicationContext())
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mImageAvatar);

                mTextName.setText(users.getName());

                if (users.getOnline() == 1) {
                    mImageOnline.setVisibility(View.VISIBLE);
                    mTextOnline.setText("đang hoạt động");
                } else {
                    mImageOnline.setVisibility(View.INVISIBLE);
                    mTextOnline.setText(Utils.getTime(users.getOnline()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        
    }
}

