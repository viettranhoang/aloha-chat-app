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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appchat_zalo.cache.PrefUtils;
import com.example.appchat_zalo.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText mInputEmail;
    private EditText mInputName;
    private EditText mInputPasword;
    private EditText mInputConfirmPassword;
    private Button mButtonRegister;
    private ImageView mImageBack;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;

    private long  online = Constants.ONLINE;
//    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        prefUtils = PrefUtils.getIntance(this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mInputEmail =  findViewById(R.id.input_email);
        mInputName =  findViewById(R.id.input_name);
        mInputPasword =  findViewById(R.id.input_password);
        mInputConfirmPassword =  findViewById(R.id.input_cofirm_password);
        mButtonRegister =  findViewById(R.id.button_register);
        mImageBack =  findViewById(R.id.image_back);
        loadingBar =  new ProgressDialog(this);
        addListner();


    }

    private void addListner() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName = mInputName.getText().toString();
                String inputEmail =  mInputEmail.getText().toString();
                String inputPassword =  mInputPasword.getText().toString();
                String  inputConfirmPass =  mInputConfirmPassword.getText().toString();
                if(TextUtils.isEmpty(inputName) || TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(inputPassword) || TextUtils.isEmpty(inputConfirmPass)){
                    Toast.makeText(RegisterActivity.this, "All  filed  are  requeied", Toast.LENGTH_SHORT).show();
                }
                else if(inputPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password must be at  least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    Register(inputEmail, inputName, inputPassword, inputConfirmPass);

                }
            }
        });
    }

    private void Register(String inputEmail, final String name, String inputPassword, String inputConfirmPass) {
        loadingBar.setTitle("New created Acount");
        loadingBar.setMessage("Please Wait for loading...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user =  mAuth.getCurrentUser();
                    String idUser = user.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id",idUser);
                    hashMap.put("name",name);
                    hashMap.put("status","Yeu bom nhat");
                    hashMap.put("avatar","default");
                    hashMap.put("cover","default");
                    hashMap.put("news","default");
                    hashMap.put("posts","default");
                    hashMap.put("online","");
                    reference.setValue(hashMap).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent =  new Intent(RegisterActivity.this, HomeChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "Register is succesful", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });


                }
                else {
                    Toast.makeText(RegisterActivity.this, "Register is fail", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }
        });
    }

//    private  void  Register(String email, final String  username, String password,  String inputConfirmPass){
//
//
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(RegisterActivity.this, "Register is successful", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
////                    FirebaseUser user =  mAuth.getCurrentUser();
////                    String idUser = user.getUid();
////                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
////                    HashMap<String, String> hashMap =  new HashMap<>();
////                    hashMap.put("id", idUser);
////                    hashMap.put("username",username);
//////                    hashMap.put("avatar", "default");
////                    hashMap.put("state","off");
//////                    hashMap.put("search",username.toLowerCase());
////                    hashMap.put("cover", "default");
//
////                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
////                        @Override
////                        public void onComplete(@NonNull Task<Void> task) {
////
////                            if (task.isSuccessful()){
//////                                prefUtils.setCurrentUid(mAuth.getUid());
////                                Toast.makeText(RegisterActivity.this, "register successful", Toast.LENGTH_SHORT).show();
////                                Intent intent = new Intent(RegisterActivity.this, HomeChatActivity.class);
////                                startActivity(intent);
////                                finish();
////                            }
////
////                            else {
////                                Toast.makeText(RegisterActivity.this, "you can't register with email, password", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    });
//
//
//                }
//                else {
//                    Toast.makeText(RegisterActivity.this, "Register is Fail", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                }
//
//            }
//        });

//    }
    }

