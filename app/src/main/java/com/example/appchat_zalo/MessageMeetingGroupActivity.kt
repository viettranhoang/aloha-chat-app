package com.example.appchat_zalo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.appchat_zalo.group_message.adapter.GroupMessageAdapter
import com.example.appchat_zalo.model.Groups
import com.example.appchat_zalo.utils.Constants
import com.google.firebase.database.*
import java.security.acl.Group
import java.util.*

class MessageMeetingGroupActivity : AppCompatActivity() {

    @BindView(R.id.toolbar_message)
    internal var mToolbarMessage: Toolbar? =  null

    @BindView(R.id.text_name)
    internal var mTextName: TextView? = null

    @BindView(R.id.list_message)
    internal var mRcvMessage: RecyclerView? = null

    @BindView(R.id.image_avatar)
    internal var mImageAvatar: ImageView? = null

    @BindView(R.id.image_camera)
    internal var mImageCamera: ImageView? = null

    @BindView(R.id.image_picture)
    internal var mImagePicture: ImageView? = null

    @BindView(R.id.input_message)
    internal var mInputMessage: EditText? = null

    @BindView(R.id.image_send)
    internal var mImageSend: ImageView? = null

//    @BindView(R.id.image_pin)
//    internal var mImagePin: ImageView? = null
//
//    @BindView(R.id.input_pin)
//    internal var mInputPin: EditText? = null

    lateinit var mRef: DatabaseReference
    private var mGroupId: String? = null

    private lateinit var adapter: MessageMeetinGroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_meeting_group_activity)
        ButterKnife.bind(this)
        initToolbar()
        initFirebase()
    }

    fun initFirebase() {
//        mRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_GROUPS).child(mGroupId!!.toString())
    }

    fun initToolbar() {
        var intent = getIntent()
        mGroupId = intent.getStringExtra("groupId")
        setSupportActionBar(mToolbarMessage)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_GROUPS).child(mGroupId!!)
        mRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    val group : Groups? = p0.getValue(Groups ::class.java)
                    Toast.makeText(applicationContext, " dddd" +group!!.name, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Faillllllll====!", Toast.LENGTH_SHORT).show()

            }

        })
//        val messageListener = object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//                Toast.makeText(applicationContext, "Faillllllll====!", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                Toast.makeText(applicationContext, "Successfulll-=====!", Toast.LENGTH_SHORT).show()
//                val group = p0.getValue(Groups::class.java)
//                Log.d("ha" ,"avatart group : "   + group?.avatar)
//
//            }
//
//        }
//        mRef!!.addValueEventListener(messageListener)
//        mRef = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_GROUPS).child(mGroupId.toString())
//
//        val postListener = object : ValueEventListener {
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
////                val group : Groups? = dataSnapshot.getValue(Groups:: class.java)
//                val  group : Groups? = dataSnapshot.getValue() as Groups
//                Log.d("haha", "onDataChange: avata== " + group?.avatar)
//
//                if (group != null) {
////                    Log.d("haha", "onDataChange: avata== " + group.avatar)
//
//                    mImageAvatar?.let {
//                        Glide.with(applicationContext)
//                                .load(group.getAvatar())
//                                .circleCrop()
//                                .into(it)
//                    }
//                    mTextName?.setText(group.getName())
//            }
//
//                // ...
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("a", "loadPost:onCancelled", databaseError.toException())
//                // ...
//            }
//        }
//        mRef?.addValueEventListener(postListener)

    }

}
