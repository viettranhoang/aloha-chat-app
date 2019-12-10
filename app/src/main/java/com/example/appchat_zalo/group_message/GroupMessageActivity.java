package com.example.appchat_zalo.group_message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.group_message.adapter.GroupMessageAdapter;
import com.example.appchat_zalo.message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Message;
import com.example.appchat_zalo.utils.Constants;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.example.appchat_zalo.utils.Constants.ROW_AVATAR;
import static com.example.appchat_zalo.utils.Constants.ROW_MEMBERS;
import static com.example.appchat_zalo.utils.Constants.ROW_NAME;

public class GroupMessageActivity extends AppCompatActivity {


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

    @BindView(R.id.input_message)
    EditText mInputMessage;

    @BindView(R.id.image_send)
    ImageView mImageSend;

    GroupMessageAdapter adapter;
    List<Message> listMessage = new ArrayList<>();

    DatabaseReference reference;
    private String mGroupId;
    StorageTask mUpLoadTask;
    StorageReference mStorageReference;

    public static final String TAG = "GroupMessageActivity";
    private Intent intent;
    List<Groups> groupsList = new ArrayList<>();
    private String mSaveCurrentDate;
    private String mSaveCurrentTime;
    private String mPostRandomName;
    private Uri mUrl;
    private String urlDownload;

    private static final int IMAGE_CHOOSE = 1;
    private static final int IMAGE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        ButterKnife.bind(this);
        initFirebase();
        initToolbar();
        initRcvMessage();
        getListMessage(mGroupId);

        getGroup(mGroupId);
    }

    private void getGroup(String mGroupId) {

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> members = new ArrayList<>();

                for (DataSnapshot dataMember : dataSnapshot.child(Constants.TABLE_GROUPS).child(mGroupId).child(ROW_MEMBERS).getChildren()) {
                    members.add(dataMember.getValue(String.class));
                }
                String name = (String) dataSnapshot.child(Constants.TABLE_GROUPS).child(mGroupId).child(ROW_NAME).getValue();
                String avatar = (String) dataSnapshot.child(Constants.TABLE_GROUPS).child(mGroupId).child(ROW_AVATAR).getValue();
                Groups group = new Groups(mGroupId, name, avatar, members);
                groupsList.add(group);

                Log.d(TAG, "onDataChange: list group=== " + groupsList.toString());

//                adapter.setUsers(groupsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        reference.child(Constants.TABLE_GROUPS).child(mGroupId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                List<String> listMember = new ArrayList<>();
//
//                for (DataSnapshot dataGroup : dataSnapshot.getChildren()){
//                    String memberId =  dataGroup.getValue(String.class);
//                    Log.d(TAG, "onDataChange: id group ==== "  + memberId);
//                    listMember.add(memberId);
//                }
//
//                String name =
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @OnClick(R.id.image_send)
    void OnClickSend() {
        sendMessgae(Constants.UID, mGroupId, mInputMessage.getText().toString(), MessageTypeConfig.TEXT, Constants.UAVATAR);
        mInputMessage.setText("");
    }


    @OnClick(R.id.image_picture)
    void onclickPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for message"), IMAGE_CHOOSE);
//        sendMessgae(Constants.UID, userId, mInputMessage.getText().toString(), MessageTypeConfig.IMAGE);
//        uploadImage();

    }

    @OnClick(R.id.image_camera)
    void onClickCamera() {
        ImagePicker.Companion.with(this)
                .cameraOnly()
                .compress(500)
                .start();
    }

    private void uploadImage() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());
        mPostRandomName = mSaveCurrentDate + mSaveCurrentTime;
        Log.i("aa", "uploadImage:  " + mUrl.getLastPathSegment());
        StorageReference filePath = mStorageReference.child("upload_message").child(mUrl.getLastPathSegment() + mPostRandomName + "jpg");
        filePath.putFile(mUrl).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filePath.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = (Uri) task.getResult();
                urlDownload = uri.toString();
                Toast.makeText(GroupMessageActivity.this, "Picture is aup loading successful!!!", Toast.LENGTH_SHORT).show();
                sendMessgae(Constants.UID, mGroupId, urlDownload, MessageTypeConfig.IMAGE,  Constants.UAVATAR);

            } else {
                String message = task.getException().getMessage();
                Toast.makeText(GroupMessageActivity.this, "Picture update is fail:" + message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(this, "image  is uploading", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }

    }

    @OnTextChanged(R.id.input_message)
    void onTextChangeMessage() {
        if (!TextUtils.isEmpty(mInputMessage.getText())) {
            mImageCamera.setVisibility(View.GONE);
            mImagePicture.setVisibility(View.GONE);
            mImageSend.setVisibility(View.VISIBLE);
        } else {
            mImageCamera.setVisibility(View.VISIBLE);
            mImagePicture.setVisibility(View.VISIBLE);
            mImageSend.setVisibility(View.INVISIBLE);
        }
    }

    private void sendMessgae(String from, String receive, String message, String type, String mFromAvatar) {

        Message message1 = new Message(message, from, false, System.currentTimeMillis(), type, mFromAvatar);
        HashMap<String, Object> hashMap = new HashMap<>();
        String key = reference.child(Constants.TABLE_MESSAGE).child(from).child(receive).push().getKey();
        hashMap.put("from", from);
        hashMap.put("message", message);
        hashMap.put("seen", message1.isSeen());
        hashMap.put("time", message1.getTime());
        hashMap.put("type", type);
        hashMap.put("fromAvatar", mFromAvatar);

        reference.child(Constants.TABLE_GROUPS).child(receive).child(ROW_MEMBERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String memberId = data.getValue(String.class);
                    Log.d(TAG, "onDataChange: id member ==" + memberId);
                    reference.child(Constants.TABLE_MESSAGE).child(memberId).child(mGroupId).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        reference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void initRcvMessage() {
        adapter = new GroupMessageAdapter();
        mRcvMessage.setLayoutManager(new LinearLayoutManager(GroupMessageActivity.this));
        mRcvMessage.setHasFixedSize(true);
        mRcvMessage.setAdapter(adapter);
    }

    private void initToolbar() {
        intent = getIntent();
        mGroupId = intent.getStringExtra("groupId");
        setSupportActionBar(mToolbarMessage);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reference.child(Constants.TABLE_GROUPS).child(mGroupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Groups groups = dataSnapshot.getValue(Groups.class);
                Log.d(TAG, "onDataChange: name== " + groups.getAvatar());
                Glide.with(getApplicationContext())
                        .load(groups.getAvatar())
                        .circleCrop()
                        .into(mImageAvatar);
                mTextName.setText(groups.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getListMessage(String mGroupId) {
        reference.child(Constants.TABLE_MESSAGE).child(Constants.UID).child(mGroupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMessage.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d("GroupMessageActivity", "onDataChange: data" + data);
                    Message message = data.getValue(Message.class);
                    Log.d("GroupMessageActivity", "onDataChange: mesage group" +message.toString());
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
