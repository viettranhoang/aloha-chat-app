package com.example.appchat_zalo.message;

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
import com.example.appchat_zalo.chat.APIService;
import com.example.appchat_zalo.message.adapter.MessageAdapter;
import com.example.appchat_zalo.message.adapter.MessageTypeConfig;
import com.example.appchat_zalo.model.Message;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.push_notification.Client;
import com.example.appchat_zalo.push_notification.Data;
import com.example.appchat_zalo.push_notification.MyRespone;
import com.example.appchat_zalo.push_notification.Sender;
import com.example.appchat_zalo.push_notification.Token;
import com.example.appchat_zalo.utils.Constants;
import com.example.appchat_zalo.utils.Utils;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.input_message)
    EditText mInputMessage;

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

    private String userId;
    private String avatar_from = Constants.UAVATAR;
    private Intent intent;
    private String mSaveCurrentDate;
    private String mSaveCurrentTime;
    private String mPostRandomName;
    private Uri mUrl;
    private String urlDownload;

    private APIService apiService;

    boolean notify = false;

    private static final int IMAGE_CHOOSE = 1;
    private static final int IMAGE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        ButterKnife.bind(this);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
//        avatar_from =  intent.getStringExtra("avatar_user");
        initToolbar();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        initRcvMessage();
        initFirebase();
        getUser();
        getListMessage(userId);

    }

    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

    }

    private void getUser() {
        reference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                Log.d("ddd", "onDataChange: users ===" + users.toString());
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
        notify = true;
        String msg = mInputMessage.getText().toString();
        if (!msg.equals("")) {
            sendMessgae(Constants.UID, userId, mInputMessage.getText().toString(), MessageTypeConfig.TEXT, avatar_from);

        } else {
            Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
        }
//        Log.d("MessageActivity", "OnClickSend: avatar users==" + avatar_from);
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


    private void uploadImage() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        mSaveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        mSaveCurrentTime = currentTime.format(calendarDate.getTime());
        mPostRandomName = mSaveCurrentDate + mSaveCurrentTime;
        Log.i("aa", "uploadImage:  " + mUrl.getLastPathSegment());

//        StorageReference filePath = mStorageReference.child("UploadPost").child(mUrl.getLastPathSegment() + mPostRandomName + ".jpg");

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
                Toast.makeText(MessageActivity.this, "Picture is aup loading successful!!!", Toast.LENGTH_SHORT).show();
                sendMessgae(Constants.UID, userId, urlDownload, MessageTypeConfig.IMAGE, avatar_from);

            } else {
                String message = task.getException().getMessage();
                Toast.makeText(MessageActivity.this, "Picture update is fail:" + message, Toast.LENGTH_SHORT).show();
            }
        });

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

    private void getListMessage(final String userId) {

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Constants.TABLE_MESSAGE).child(Constants.UID).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMessage.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    listMessage.add(message);
                }

                Log.i("ha", "onDataChange: " + listMessage.toString());
                adapter.setMessageList(listMessage);
                mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
//                Log.d("a", "onDataChange: lissdfd " + );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
    }

    private void sendMessgae(String from, String receiver, String message, String type, String avatar_from) {

        reference = FirebaseDatabase.getInstance().getReference();
        Message message1 = new Message(message, from, false, System.currentTimeMillis(), type, avatar_from);
        HashMap<String, Object> hashMap = new HashMap<>();
        String key = reference.child(Constants.TABLE_MESSAGE).child(from).child(receiver).push().getKey();
        hashMap.put("from", from);
        hashMap.put("message", message);
        hashMap.put("seen", message1.isSeen());
        hashMap.put("time", message1.getTime());
        hashMap.put("type", type);
        hashMap.put("from_avatar", avatar_from);

        reference.child(Constants.TABLE_MESSAGE).child(from).child(receiver).child(key).updateChildren(hashMap).addOnSuccessListener(aVoid -> {
            mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
        });
        reference.child(Constants.TABLE_MESSAGE).child(receiver).child(from).child(key).updateChildren(hashMap).addOnSuccessListener(aVoid -> {
            mRcvMessage.scrollToPosition(adapter.getItemCount() - 1);
        });


        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS).child(Constants.UID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (notify) {
                    Log.d("MessageActivity", "onDataChange: user sens");
                    sendNotifiaction(receiver, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Constants.TABLE_TOKEN);
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",
                            userId);

                    Log.d("MessageActivity", "onDataChange: token " +token.toString());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender) .enqueue(new Callback<MyRespone>(){
                        @Override
                        public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRespone> call, Throwable t) {

                        }

                    });
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initToolbar() {
        setSupportActionBar(mToolbarMessage);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                Log.d("aa", "onDataChange: avatar  user" + users.getAvatar());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(this, "image  is uploading", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && resultCode == RESULT_OK) {
//            if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                mUrl = data.getData();
//
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(mUrl);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
//                    mImagePicture.setImageBitmap(selectedImage);
//
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
////            } else if (requestCode == IMAGE_PHOTO) {
////                Bundle extras = data.getExtras();
////                Bitmap bitmap = (Bitmap) extras.get("data");
////                mistBitmap.add(bitmap);
////                mAddPostAdapter = new AddPostAdapter(mistBitmap);
////                mRcvAddPosts.setAdapter(mAddPostAdapter);
////            }
//
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//
//    }
}

