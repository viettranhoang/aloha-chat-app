package com.example.appchat_zalo.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appchat_zalo.R
import com.example.appchat_zalo.model.Users
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    private lateinit var user: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        user = intent.getParcelableExtra(EXTRA_USER)

        user.also {
            Glide.with(this)
                    .load(it.news)
                    .into(imageNews)
            textName.text = it.name
            Glide.with(this)
                    .load(it.avatar)
                    .circleCrop()
                    .into(imageAvatar)
        }

    }

    companion object {

        const val EXTRA_USER = "EXTRA_USER"

        fun openNewsActivity(activity: Activity, user: Users) {
            val intent = Intent(activity, NewsActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            activity.startActivity(intent)
        }
    }
}
