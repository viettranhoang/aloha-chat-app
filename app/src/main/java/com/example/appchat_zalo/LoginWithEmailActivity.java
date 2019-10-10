package com.example.appchat_zalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appchat_zalo.cache.PrefUtils;
import com.example.appchat_zalo.utils.Constants;
import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginWithEmailActivity extends AppCompatActivity {

    private EditText mInputEmail;
    private EditText mInputPasword;
    private Button mButtonLogin;
    private ImageView mImageBack;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

//    private PrefUtils prefUtils;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
//        prefUtils = PrefUtils.getIntance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        initView();
        addListner();


    }

    private void addListner() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginWithEmailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LoginWithEmail();


                String email = mInputEmail.getText().toString();
                String password = mInputPasword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginWithEmailActivity.this, "All  filed  are  requeied", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(LoginWithEmailActivity.this, "password must be at  least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Sign In");
                    loadingBar.setMessage("Please Wait for loading...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginWithEmailActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                prefUtils.setCurrentUid(mAuth.getUid());
//                                Intent intent = new Intent(LoginWithEmailActivity.this, HomeChatActivity.class);
//                                startActivity(intent);
//                                finish();
                                Constants.UID = mAuth.getUid();

                                Intent intent = new Intent(LoginWithEmailActivity.this, HomeChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                loadingBar.dismiss();


                            } else {
                                Toast.makeText(LoginWithEmailActivity.this, "Login  fail", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
                }
            }
        });
    }

    private void LoginWithEmail() {
        String inputEmail = mInputEmail.getText().toString();
        String inputPassword = mInputPasword.getText().toString();

        if (TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(LoginWithEmailActivity.this, "All  filed  are  requeied", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.length() < 6) {
            Toast.makeText(LoginWithEmailActivity.this, "password must be at  least 6 characters", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please Wait for loading...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(LoginWithEmailActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(LoginWithEmailActivity.this, "Login is successful!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    } else {
                        Toast.makeText(LoginWithEmailActivity.this, "Login is Fail!", Toast.LENGTH_SHORT).show();

                        loadingBar.dismiss();
                    }
                }
            });

        }
    }

    private void initView() {
        mInputEmail = findViewById(R.id.input_email);
        mInputPasword = findViewById(R.id.input_password);
        mButtonLogin = findViewById(R.id.button_login);
        mImageBack = findViewById(R.id.image_back);
        loadingBar = new ProgressDialog(this);
        mInputEmail.setText("a@gmail.com");
        mInputPasword.setText("111111");

    }
    @Override
    public void onBackPressed() {
    }
}
