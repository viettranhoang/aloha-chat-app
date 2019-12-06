package com.example.appchat_zalo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.my_profile.UserRelationshipConfig;
import com.example.appchat_zalo.my_profile.adapter.ProfilePostsAdapter;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import butterknife.OnClick;

import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.FRIEND;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.NOT;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.RECEIVE;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.SENT;

public class UserProfileActivity extends AppCompatActivity {


    @BindView(R.id.image_avatar)
    ImageView mAvatar;

    @BindView(R.id.image_background)
    ImageView mCover;

    @BindView(R.id.text_name)
    TextView mName;

    @BindView(R.id.text_status)
    TextView mStatus;

    @BindView(R.id.image_add_friend)
    ImageView mImageAddFriend;

    @BindView(R.id.image_message)
    ImageView mImageMessage;

    @BindView(R.id.image_decline_friend)
    ImageView mImageDeclineFriend;

    @BindView(R.id.text_decline_friend)
    TextView mTextDeclineFriend;

    @BindView(R.id.text_add_friend)
    TextView mTextAddFriend;

    @BindView(R.id.text_message)
    TextView mTextAddMessage;

    @BindView(R.id.list_user_posts)
    RecyclerView mRcvUserPost;

    private List<Posts> mUserPostList = new ArrayList<>();
    private ProfilePostsAdapter mUserPostAdapter;

    private String mUserId;

    @UserRelationshipConfig
    private String mCurrentRelative = NOT;

    FirebaseUser mUser;
    DatabaseReference mUserRef, mUserPost, mFriendRequestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        initFirebase();
        initRcv();
        getInforUser();
        getUserPost();

    }

//    private void getRelationship(String fromId, String toId) {
//        mFriendRequestRef.child(fromId).child(toId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                switch (mCurrentRelative) {
//                    case NOT:
//                        mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
//                        mTextAddFriend.setText("Thêm bạn bè");
//                        mTextAddFriend.setTextColor(R.color.black);
//                        break;
//
//                    case FRIEND:
//                        mImageAddFriend.setImageResource(R.drawable.ic_account_check);
//                        mTextAddFriend.setText("Hủy kết bạn");
//                        mTextAddFriend.setTextColor(R.color.black);
//                        break;
//
//                    case SENT:
//                        mImageAddFriend.setImageResource(R.drawable.ic_sent_invite_friend);
//                        mTextAddFriend.setText("Hủy yêu cầu");
//                        mTextAddFriend.setTextColor(R.color.black);
//                        break;
//
//                    case RECEIVE:
//                        mImageAddFriend.setImageResource(R.drawable.ic_receive_invite_friend);
//                        mTextAddFriend.setText("Trả lời");
//                        mTextAddFriend.setTextColor(R.color.black);
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    @OnClick(R.id.image_decline_friend)
    void onClickDeclineFriend() {

        mImageDeclineFriend.setEnabled(false);
        mImageDeclineFriend.setVisibility(View.GONE);
        mTextDeclineFriend.setVisibility(View.GONE);

    }


    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.image_add_friend)
    void onclickAddFriend() {
        if (!Constants.UID.equals(mUserId)) {

            mImageAddFriend.setEnabled(false);
            switch (mCurrentRelative) {
                case NOT:
                    mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
                    mTextAddFriend.setText("Kết bạn");
                    mTextAddFriend.setTextColor(R.color.black);
                    SentInviteToFriend(Constants.UID, mUserId);
                    break;

                case FRIEND:
                    mImageAddFriend.setImageResource(R.drawable.ic_account_check);
                    mTextAddFriend.setText("Bạn bè");
                    mTextAddFriend.setTextColor(R.color.black);
                    Unfriend(Constants.UID, mUserId);
                    break;

                case SENT:
                    mImageAddFriend.setImageResource(R.drawable.ic_sent_invite_friend);
                    mTextAddFriend.setText("Hủy yêu cầu");
                    mTextAddFriend.setTextColor(R.color.black);
                    CancleInviteOfFriend(Constants.UID, mUserId);
                    break;

                case RECEIVE:
                    mImageAddFriend.setImageResource(R.drawable.ic_receive_invite_friend);
                    mTextAddFriend.setText("Trả lời");
                    mTextAddFriend.setTextColor(R.color.black);
                    AcceptInviteFromFriend(Constants.UID, mUserId);
                    break;
            }

        }

//        if (mCurrentRelative == NOT) {
//            mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
//            mTextAddFriend.setText("Thêm bạn bè");
//            mTextAddFriend.setTextColor(R.color.black);
//            SentInviteToFriend(Constants.UID, mUserId);
//
//        } else if (mCurrentRelative == SENT) {
//            mImageAddFriend.setImageResource(R.drawable.ic_sent_invite_friend);
//            mTextAddFriend.setText("Hủy yêu cầu");
//            mTextAddFriend.setTextColor(R.color.black);
//            CancleInviteOfFriend(Constants.UID, mUserId);
//
//        } else if (mCurrentRelative == RECEIVE) {
//            mImageAddFriend.setImageResource(R.drawable.ic_receive_invite_friend);
//            mTextAddFriend.setText("Trả lời");
//            mTextAddFriend.setTextColor(R.color.black);
//            AcceptInviteFromFriend(Constants.UID, mUserId);
//
//        }
    }

    private void Unfriend(String fromId, String toId) {

        mFriendRequestRef.child(fromId).child(toId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mFriendRequestRef.child(toId).child(fromId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mImageAddFriend.setEnabled(true);
                            mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
                            mTextAddFriend.setText("Kết bạn");
                            mTextAddFriend.setTextColor(R.color.black);
                            mImageDeclineFriend.setVisibility(View.GONE);
                            mTextDeclineFriend.setVisibility(View.GONE);
                            mImageDeclineFriend.setEnabled(false);
                        }
                    });
                }
            }
        });

    }

    private void AcceptInviteFromFriend(String fromId, String toId) {

        mFriendRequestRef.child(fromId).child(toId).setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFriendRequestRef.child(toId).child(fromId).setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mImageAddFriend.setEnabled(true);
                            mImageAddFriend.setImageResource(R.drawable.ic_account_check);
                            mTextAddFriend.setText("Bạn bè");
                            mTextAddFriend.setTextColor(R.color.black);
                            mImageDeclineFriend.setVisibility(View.GONE);
                            mTextDeclineFriend.setVisibility(View.GONE);
                            mImageDeclineFriend.setEnabled(false);
                        }
                    });
                }
            }
        });
//        Calendar date = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//        String saveCurrentDate = currentDate.format(date.getTime());
//
//        mFriendRef.child(fromId).child(toId).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                mFriendRef.child(toId).child(fromId).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        mFriendRequestRef.child(fromId).child(toId).setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    mFriendRequestRef.child(toId).child(fromId).setValue("friend").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            mImageAddFriend.setEnabled(true);
//                                            mImageAddFriend.setImageResource(R.drawable.ic_account_check);
//                                            mTextAddFriend.setText("Hủy kết bạn");
//                                            mTextAddFriend.setTextColor(R.color.black);
//                                            mImageDeclineFriend.setVisibility(View.GONE);
//                                            mTextDeclineFriend.setVisibility(View.GONE);
//                                            mImageDeclineFriend.setEnabled(false);
//
//                                        }
//                                    });
//                                }
//                            }
//                        });
//
//                    }
//                });
//            }
//        });
    }

    private void CancleInviteOfFriend(String fromId, String toId) {
        mFriendRequestRef.child(fromId).child(toId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFriendRequestRef.child(toId).child(fromId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mImageAddFriend.setEnabled(true);
                            mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
                            mTextAddFriend.setText("Kết bạn");
                            mTextAddFriend.setTextColor(R.color.black);
                            mImageDeclineFriend.setVisibility(View.GONE);
                            mTextDeclineFriend.setVisibility(View.GONE);
                            mImageDeclineFriend.setEnabled(false);

                        }
                    });
                }
            }
        });
//        mFriendRequestRef.child(fromId).child(toId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//
//                    mFriendRequestRef.child(toId).child(fromId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()){
//                                mImageAddFriend.setEnabled(true);
//                                mCurrentRelative = NOT;
//                                mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
//                                mTextAddFriend.setText("Thêm bạn bè");
//                                mTextAddFriend.setTextColor(R.color.black);
//
//                            }
//
//                        }
//                    });
//                }
//
//            }
//        });

    }

    private void SentInviteToFriend(String fromId, String toId) {
        mFriendRequestRef.child(fromId).child(toId).setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFriendRequestRef.child(toId).child(fromId).setValue("receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mImageAddFriend.setEnabled(true);
                            mCurrentRelative = SENT;
                            mImageAddFriend.setImageResource(R.drawable.ic_sent_invite_friend);
                            mTextAddFriend.setText("Hủy yêu cầu");
                            mTextAddFriend.setTextColor(R.color.black);

                            mImageDeclineFriend.setVisibility(View.GONE);
                            mTextDeclineFriend.setVisibility(View.GONE);
                            mImageDeclineFriend.setEnabled(false);

                        }
                    });
                }
            }
        });

    }

    private void getUserPost() {
        if(!mUserId.equals(Constants.UID)){
            Log.d("UserProfileActivity", "getUserPost: u");
            mUserPost.child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUserPostList.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Posts posts = data.getValue(Posts.class);
                        mUserPostList.add(posts);
                    }
                    Log.d("aa", "listUSerPost" + mUserPostList);
                    mUserPostAdapter.setListMyPost(mUserPostList);
                    mRcvUserPost.setAdapter(mUserPostAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    private void initRcv() {
        mUserPostAdapter = new ProfilePostsAdapter();
        mRcvUserPost.setLayoutManager(new LinearLayoutManager(this));
        mRcvUserPost.setHasFixedSize(true);
    }

    private void getInforUser() {
        mUserRef.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);
                mUserPostAdapter.setUser(users);
                mName.setText(users.getName());
                mStatus.setText(users.getStatus());
                Glide.with(getApplicationContext())
                        .load(users.getAvatar())
                        .circleCrop()
                        .into(mAvatar);

                Glide.with(getApplicationContext())
                        .load(users.getCover())
                        .centerCrop()
                        .into(mCover);

                mFriendRequestRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(mUserId)) {
                            if (dataSnapshot.exists()) {

                                mCurrentRelative = dataSnapshot.child(mUserId).getValue().toString();

                                if (mCurrentRelative.equals(SENT)) {
//                                mCurrentRelative = SENT;
                                    mImageAddFriend.setImageResource(R.drawable.ic_sent_invite_friend);
                                    mTextAddFriend.setText("Hủy yêu cầu");
                                    mTextAddFriend.setTextColor(R.color.black);
                                    mImageDeclineFriend.setVisibility(View.GONE);
                                    mTextDeclineFriend.setVisibility(View.GONE);
                                    mImageDeclineFriend.setEnabled(false);

                                }
                                else  if (mCurrentRelative.equals(NOT)) {

//                                mCurrentRelative = SENT;
                                    mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
                                    mTextAddFriend.setText("Kết bạn");
                                    mTextAddFriend.setTextColor(R.color.black);
                                    mImageDeclineFriend.setVisibility(View.GONE);
                                    mTextDeclineFriend.setVisibility(View.GONE);
                                    mImageDeclineFriend.setEnabled(false);

                                }

                                else if (mCurrentRelative.equals(RECEIVE)) {
                                    mImageAddFriend.setImageResource(R.drawable.ic_receive_invite_friend);
                                    mTextAddFriend.setText("Trả lời");
                                    mTextAddFriend.setTextColor(R.color.black);
                                    mImageDeclineFriend.setVisibility(View.VISIBLE);
                                    mTextDeclineFriend.setVisibility(View.VISIBLE);
                                    mImageDeclineFriend.setEnabled(true);
                                    mImageDeclineFriend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DeclineFriendRequest(Constants.UID, mUserId);

                                        }
                                    });
                                } else if (mCurrentRelative.equals(FRIEND)) {
                                    mImageAddFriend.setImageResource(R.drawable.ic_account_check);
                                    mTextAddFriend.setText("Bạn bè");
                                    mTextAddFriend.setTextColor(R.color.black);
                                    mImageDeclineFriend.setVisibility(View.GONE);
                                    mTextDeclineFriend.setVisibility(View.GONE);
                                    mImageDeclineFriend.setEnabled(false);
//                                    mImageDeclineFriend.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            DeclineFriendRequest(Constants.UID, mUserId);
//
//                                        }
//                                    });
                                }
                            } else {

                                mFriendRequestRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(mUserId)) {
                                            mCurrentRelative = FRIEND;
                                            mImageAddFriend.setImageResource(R.drawable.ic_account_check);
                                            mTextAddFriend.setText("Hủy kết bạn");
                                            mTextAddFriend.setTextColor(R.color.black);
                                            mImageDeclineFriend.setVisibility(View.GONE);
                                            mTextDeclineFriend.setVisibility(View.GONE);
                                            mImageDeclineFriend.setEnabled(false);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DeclineFriendRequest(String fromId, String toId) {

        mFriendRequestRef.child(fromId).child(toId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFriendRequestRef.child(toId).child(fromId).setValue("not").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mImageAddFriend.setEnabled(true);
                            mImageAddFriend.setImageResource(R.drawable.ic_add_friends);
                            mTextAddFriend.setText("Kết bạn");
                            mTextAddFriend.setTextColor(R.color.black);
                            mImageDeclineFriend.setVisibility(View.GONE);
                            mTextDeclineFriend.setVisibility(View.GONE);
                            mImageDeclineFriend.setEnabled(false);

                        }
                    });
                }
            }
        });
    }

    private void initFirebase() {

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserPost = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS);
        mUserRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_USERS);
        mFriendRequestRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_FRIEND);

    }
}
