package com.example.appchat_zalo.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat_zalo.choose_friends.ChooseActivity;
import com.example.appchat_zalo.Message.MessageActivity;
import com.example.appchat_zalo.Message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.all_user.AllUserActivity;
import com.example.appchat_zalo.chat.adapter.ChatAdapter;
import com.example.appchat_zalo.chat.listner.OnclickChatItemListner;
import com.example.appchat_zalo.chat.model.Chat;
import com.example.appchat_zalo.group_message.GroupMessageActivity;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.search.SearchActivity;
import com.example.appchat_zalo.search.adapter.SearchAdapter;
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
import butterknife.OnClick;

public class ChatFragment extends Fragment {

    @BindView(R.id.list_chat)
    RecyclerView mRcvChat;

    @BindView(R.id.input_search)
    EditText mInputSearch;

    private DatabaseReference mRef;

    @OnClick({R.id.input_search})
    void clickSearch() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_contact)
    void displayAllUser() {
        Intent intent = new Intent(getContext(), AllUserActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_create_group)
    void onclickCreatGroup() {

        Intent intent = new Intent(getContext(), ChooseActivity.class);
        startActivity(intent);
    }

    private ChatAdapter mChatAdapter = new ChatAdapter(new OnclickChatItemListner() {

        @Override
        public void onClickChatItem(Chat chat) {
//            if(checkUser){
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra("userId", chat.getUsers().getId());
            startActivity(intent);

        }
    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chatfragment, container, false);
        ButterKnife.bind(this, view);
        initRcv();
        initFirebase();
        getChatList();
        return view;
    }

    private void getChatList() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chat> listChat = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.child(Constants.TABLE_MESSAGE).child(Constants.UID).getChildren()) {
                    Chat chat = new Chat();
                    String idSender = data.getKey();
                    Log.i("a","idSender " + idSender);
                    List<Message> list = new ArrayList<>();
                    for (DataSnapshot dataMessage : data.getChildren()) {
                        list.clear();
                        list.add(dataMessage.getValue(Message.class));
                    }
                    Log.i("b", "onDataChange: setLastMessage" + list.get(list.size() - 1).toString());  // lay duoc tin nhan  cuoi  cung
                    chat.setLastMessage(list.get(list.size() - 1));
                    chat.setUsers(dataSnapshot.child(Constants.TABLE_USERS).child(idSender).getValue(Users.class));

                    listChat.add(chat);
                    Log.d("a", "onDataChange: list chat " + chat.toString());
                }
                mChatAdapter.setListUser(listChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mMessageRef.child(Constants.UID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()){
//                    Chat chat =  new Chat();
//                    String idSender = data.getKey();
//                    Log.i("a","idSender " + idSender);
//
//
//
//                    // get  lastMessage
//                    chat.setLastMessage(list.get(list.size() -1));
//
//                    //get user theo  idSender
//                    mUserRef.child(idSender).addValueEventListener(new ValueEventListener() {
//
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Users users = dataSnapshot.getValue(Users.class);
//                            Log.d("a", "onDataChange: " + users.toString());
//                            chat.setUsers(users);
//                            Log.i("aa", "onDataChange: chat" + chat.toString());
//                            mChatAdapter.addChat(chat);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void initRcv() {
        mRcvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvChat.setHasFixedSize(true);
        mRcvChat.setAdapter(mChatAdapter);

    }

    private void initFirebase() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }


}
