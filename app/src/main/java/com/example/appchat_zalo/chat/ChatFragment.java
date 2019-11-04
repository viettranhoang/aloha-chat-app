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
import com.example.appchat_zalo.message.MessageActivity;
import com.example.appchat_zalo.message.model.Message;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.all_user.AllUserActivity;
import com.example.appchat_zalo.chat.adapter.ChatAdapter;
import com.example.appchat_zalo.chat.listner.OnclickChatItemListner;
import com.example.appchat_zalo.chat.model.Chat;
import com.example.appchat_zalo.group_message.GroupMessageActivity;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.search.SearchActivity;
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

import static com.example.appchat_zalo.utils.Constants.ROW_AVATAR;
import static com.example.appchat_zalo.utils.Constants.ROW_MEMBERS;
import static com.example.appchat_zalo.utils.Constants.ROW_NAME;

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
        public void onClickUserChatItem(Users user) {

            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        }

        @Override
        public void onClickGroupItem(Groups group) {
            Intent intent = new Intent(getContext(), GroupMessageActivity.class);
            intent.putExtra("groupId", group.getId());
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
                    Log.i("a", "idSender " + idSender);
                    List<Message> list = new ArrayList<>();
                    for (DataSnapshot dataMessage : data.getChildren()) {
                        list.clear();
                        list.add(dataMessage.getValue(Message.class));
                    }
                    Log.i("b", "onDataChange: setLastMessage" + list.get(list.size() - 1).toString());  // lay duoc tin nhan  cuoi  cung
                    chat.setLastMessage(list.get(list.size() - 1));


                    if (idSender.contains(Constants.GROUP_ID)) {
                        List<String> members = new ArrayList<>();

                        for (DataSnapshot  dataMember : dataSnapshot.child(Constants.TABLE_GROUPS).child(idSender).child(ROW_MEMBERS).getChildren()){
                            members.add(dataMember.getValue(String.class));
                        }
                        String name = (String) dataSnapshot.child(Constants.TABLE_GROUPS).child(idSender).child(ROW_NAME).getValue();
                        String avatar =(String) dataSnapshot.child(Constants.TABLE_GROUPS).child(idSender).child(ROW_AVATAR).getValue();
                        Groups group = new Groups(idSender,name,avatar,members);
                        Log.d("ChatFragment", "onDataChange:group " + group.toString());
                        chat.setGroups(dataSnapshot.child(Constants.TABLE_GROUPS).child(idSender).getValue(Groups.class));
                    }
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
