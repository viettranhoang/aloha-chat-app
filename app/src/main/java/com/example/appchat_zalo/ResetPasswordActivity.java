package com.example.appchat_zalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText mInputPassword;

    @BindView(R.id.button_send_mail)
    Button mButtonSendEmail;

    @BindView(R.id.toolbar_reset_password)
    Toolbar mToolbarResetPassword;

    @BindView(R.id.text_back)
    TextView mTextBack;

    @BindView(R.id.text_content_reset_password)
    TextView mTextContentReset;

    @BindView(R.id.text_reset_password)
    TextView mTextResetPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.button_send_mail)
    void onClickSendEmail() {
        resetPassword();

    }

    private void resetPassword() {
        String inputEmail = mInputPassword.getText().toString();
        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(this, R.string.reset_password_invalid_email, Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(inputEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, R.string.reset_password_check_email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginWithEmailActivity.class);
                        startActivity(intent);
                    }

                    else {
                        Toast.makeText(ResetPasswordActivity.this, R.string.reset_password_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
