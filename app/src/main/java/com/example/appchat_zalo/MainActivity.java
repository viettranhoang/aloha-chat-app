package com.example.appchat_zalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private LoginButton mLoginFb;
    private TextView mTextRegitser;
    private Button mButtonEmail;

    private String TAG = " MainActivity";
    private Button mButtonFb;

    private LinearLayout mLayputPhone;

    private FirebaseAuth mAuth;

//    private PrefUtils prefUtils;

    private CallbackManager mCallBackManager;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        prefUtils = PrefUtils.getIntance(this);
//        checkLogined();

        initView();
        addListner();
    }

    //    private void checkLogined() {
//        if (prefUtils.getCurrentUid() != null) {
//            Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    private void addListner() {
        mButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginWithEmailActivity.class));
                finish();
            }
        });

        mTextRegitser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }

    private void initView() {

        mTextRegitser = findViewById(R.id.text_sigup);
        mButtonEmail = findViewById(R.id.button_login_email);
        mLoginFb = findViewById(R.id.loginfb_button);
        mButtonFb = findViewById(R.id.button_fb);

    }

    public void onClickFacebookButton(View view) {
        if (view == mButtonFb) {
            mLoginFb.performClick();
        }
    }


}
