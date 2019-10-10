package com.example.appchat_zalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.appchat_zalo.chat.ChatFragment;
import com.example.appchat_zalo.friends.FriendsFragment;
import com.example.appchat_zalo.home_fragment.HomeFragment;
import com.example.appchat_zalo.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeChatActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavi;
    private ViewPager viewPager;

    ChatFragment chatFragment;
    FriendsFragment friendsFragment;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBottomNavi = findViewById(R.id.view_bottom_navigation);
        viewPager = findViewById(R.id.viewpager);

        initBottonNavi();


//        viewPager.setCurrentItem(0); //Set Currrent Item When Activity Start
//        viewPager.setOnPageChangeListener(new PageChange()); //Listeners For Viewpager When Page Changed

//        setupFm(getSupportFragmentManager(), viewPager);
//        loadFragment(new ChatFragment());
    }

    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager){
        ViewPagerAdapter Adapter = new ViewPagerAdapter(fragmentManager);
//
//        //Add All Fragment To List
//        Adapter.add(new ChatFragment(), "Chat");
//        Adapter.add(new FriendsFragment(), "Friends");
//        Adapter.add(new HomeFragment(), "Home");
//        Adapter.add(new ProfileFragment(), "Profile");
//
//        viewPager.setAdapter(Adapter);
    }

    private void initBottonNavi() {

        mBottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_chat:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_friends:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_home:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.menu_profile:
                         viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    mBottomNavi.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                mBottomNavi.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNavi.getMenu().getItem(position);

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        chatFragment = new ChatFragment();
        friendsFragment = new FriendsFragment();
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        viewPagerAdapter.addFragment(chatFragment);
        viewPagerAdapter.addFragment(friendsFragment);
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPager.setAdapter(viewPagerAdapter);
    }

//    private void loadFragment(Fragment fragment) {
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.layout_fragment, fragment);
////        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }

    public class PageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    mBottomNavi.setSelectedItemId(R.id.menu_chat);
                    break;
                case 1:
                    mBottomNavi.setSelectedItemId(R.id.menu_friends);
                    break;
                case 2:
                    mBottomNavi.setSelectedItemId(R.id.menu_home);
                    break;

                case 3:
                    mBottomNavi.setSelectedItemId(R.id.menu_profile);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
