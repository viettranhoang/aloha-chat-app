package com.example.appchat_zalo.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.appchat_zalo.R;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.news.adapter.NewsPagerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_news)
    ViewPager mViewPagerNews;

    private int possiton ;
    private NewsPagerAdapter mNewsAdapter;
    private List<Users> mUserList =  new ArrayList<>();

    private DatabaseReference mRef;
        public static final String EXTRA_POSITION_NEWS = "EXTRA_POSITION_NEWS";

    public static void moveNewsActivity(Activity activity, int positionNews){
        Intent intent =  new Intent(activity, NewsActivity.class);
        intent.putExtra(EXTRA_POSITION_NEWS, positionNews);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initViewPager();
        initFirebase();

    }

    private void initFirebase() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void initViewPager() {
        possiton = getIntent().getIntExtra("possition",1);
        mViewPagerNews.setAdapter(mNewsAdapter);
//        getListFriend(Constants.UID, type);


    }
}
