package com.example.appchat_zalo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private LoginButton mLoginFb;
    private TextView mTextRegitser;
    private TextView mTextPhoneNumber;
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

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if ((user != null)) {
                    Log.d(TAG, "" + user.getUid());
                } else {
                    Log.d(TAG, "logout");
                }
            }
        };
        initLoginWithFb();
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
        mButtonFb = findViewById(R.id.btn_fb);

    }

    private void initLoginWithFb() {
        mCallBackManager = CallbackManager.Factory.create();
        mLoginFb.setReadPermissions("email", "public_profile");
        mLoginFb.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });

    }

    public void onClickFacebookButton(View view) {
        if (view == mButtonFb) {
            mLoginFb.performClick();
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG, "signInWithCredential" + task.isSuccessful());

                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Login with  facebook successful", Toast.LENGTH_SHORT).show();
//                    createNewUser();

                }
            }
        });
    }

    private void createNewUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", idUser);
        hashMap.put("username", user.getDisplayName());
        hashMap.put("", "default");
        hashMap.put("state", "off");
        hashMap.put("search", "");
        hashMap.put("", "default");


        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
//                    prefUtils.setCurrentUid(mAuth.getUid());
                    Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Đã có lỗi đéo j xảy ra, vui lòng đửng thử lần 2 nhé , ok?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
