package com.example.appchat_zalo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.appchat_zalo.message.adapter.MessageTypeConfig
import com.example.appchat_zalo.model.Message
import com.example.appchat_zalo.utils.Constants
import com.example.appchat_zalo.utils.Utils

class MessageMeetinGroupAdapter(val listMeesage: ArrayList<Message>) : RecyclerView.Adapter<MessageMeetinGroupAdapter.MessageMettingGroupViewHolder>(){

    val MESSAGE_LEFT = 1
    val MESSAGE_RIGHT = 2

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MessageMettingGroupViewHolder {
        val  view  : View
        if (i == MESSAGE_LEFT ){
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_left, parent, false)
            return MessageMettingGroupViewHolder(view)
        }
        else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_right, parent, false)
            return MessageMettingGroupViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listMeesage.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listMeesage.get(position).getFrom() == Constants.UID) {
            MESSAGE_RIGHT
        } else
            MESSAGE_LEFT
    }

    override fun onBindViewHolder(holder: MessageMettingGroupViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class MessageMettingGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.image_avatar)
        lateinit var mImageAvatar : ImageView

        @BindView(R.id.image_message)
        internal var mImageMessage: ImageView? = null

        @BindView(R.id.text_seen)
        internal var mTextSeen: TextView? = null

        @BindView(R.id.text_message)
        internal var mTextMessage: TextView? = null

        @BindView(R.id.text_time)
        internal var mTextTime: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindata( message : Message){

            Glide.with(itemView)
                    .load(message.fromAvatar)
                    .circleCrop()
                    .into(mImageAvatar)

            mTextTime?.setText(Utils.getTime(message.time))


            if (message.type == MessageTypeConfig.TEXT) {
                mTextMessage?.setText(message.message)
                mTextMessage?.setVisibility(View.VISIBLE)
                mImageMessage?.setVisibility(View.GONE)
            } else {
                mTextMessage?.setVisibility(View.INVISIBLE)
                mImageMessage?.setVisibility(View.VISIBLE)
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10))
                mImageMessage?.let {
                    Glide.with(itemView)
                            .load(message.message)
                            .apply(requestOptions)
                            .into(it)
                }
            }
        }

    }

}