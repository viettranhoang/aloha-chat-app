package com.example.appchat_zalo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.example.appchat_zalo.fragment.ProfileFragment;
import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClickPostActivity extends AppCompatActivity {


    @BindView(R.id.text_content_post)
    TextView mContentPost;

    @BindView(R.id.image_picture_posts)
    ImageView mPicturePost;

    @BindView(R.id.button_edit_post)
    Button mEditPost;

    @BindView(R.id.button_delete_post)
    Button mDeletePost;

    private Intent intent;

    private DatabaseReference refPost;
    Posts posts;
    String postId;
    String contnetPost, picturePosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        ButterKnife.bind(this);

        intent = getIntent();
        postId = intent.getStringExtra("idPost");
        getMyPost();

    }

    private void getMyPost() {

        refPost = FirebaseDatabase.getInstance().getReference(Constants.TABLE_POSTS).child(Constants.UID).child(postId);

        refPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Posts posts = dataSnapshot.getValue(Posts.class);
                    mContentPost.setText(posts.getContent_posts());
                    Glide.with(getApplicationContext())
                            .load(posts.getPicture())
                            .into(mPicturePost);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.button_delete_post)
    void deldePost(){
        refPost.removeValue();
        Toast.makeText(this, "Posts is deleted!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeChatActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_edit_post)
    void editPost(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Edit Post:");

        final EditText inputContentPost = new EditText(this);
        inputContentPost.setText(contnetPost);
        builder.setView(inputContentPost);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                refPost.child("content_posts").setValue(inputContentPost.getText().toString());
                Toast.makeText(ClickPostActivity.this, "posts updated successful...", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });
        Dialog  dialog =  builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.blue);
    }
}
