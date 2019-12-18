package com.example.appchat_zalo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appchat_zalo.cache.PrefUtils;
import com.example.appchat_zalo.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private LoginButton mLoginFb;
    private TextView mTextRegitser;
    private Button mButtonEmail;

    private String TAG = " MainActivity";
    private Button mButtonFb;
    private Button mButtonGg;
    private SignInButton mSigInGg;
    private FirebaseAuth mAuth;
    private PrefUtils prefUtils;
    private CallbackManager mCallBackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefUtils = PrefUtils.getIntance(this);
        checkLogined();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        };
        initView();
//        mSigInGg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn();
//            }
//        });
        initFbLogin();
        addListner();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initFbLogin() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Constants.UID = mAuth.getUid();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("MainActivity", "onComplete:  email  user" + user.getEmail());
                            createNewUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG, "signInWithCredential" + task.isSuccessful());

                if (task.isSuccessful()) {
                    Constants.UID = mAuth.getUid();

                    Toast.makeText(MainActivity.this, "login with facebook successful===== ", Toast.LENGTH_SHORT).show();
                    createNewUser();

                }
            }
        });
    }

    private void createNewUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.TABLE_USERS).child(idUser);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", idUser);
        hashMap.put("name", user.getDisplayName());
        hashMap.put("status", "Yeu bom nhat");
        hashMap.put("avatar", "default");
        hashMap.put("cover", "default");
        hashMap.put("news", "default");
        hashMap.put("posts", "default");
        hashMap.put("online", 0);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
//                    Constants.UID = mAuth.getUid();
                    prefUtils.setCurrentUid(Constants.UID);
                    Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Đã có lỗi đéo j xảy ra, vui lòng đửng thử lần 2 nhé , ok?", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkLogined() {

        if (prefUtils.getCurrentUid() != null) {
            Constants.UID = prefUtils.getCurrentUid();
            Constants.UAVATAR = prefUtils.getCurrentUAvatar();
            Intent intent = new Intent(MainActivity.this, HomeChatActivity.class);
            startActivity(intent);
            finish();
        }
    }

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
        mButtonGg = findViewById(R.id.button_gg);
        mSigInGg = findViewById(R.id.login_gg_button);

    }

    public void onClickFacebookButton(View view) {
        if (view == mButtonFb) {
            mLoginFb.performClick();
        }
    }

    public void onClickGoogleButton(View view) {
        if (view == mButtonGg) {
            mSigInGg.performClick();
            signIn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
}
