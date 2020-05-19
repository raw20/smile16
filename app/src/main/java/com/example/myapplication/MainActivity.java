package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName, editTextNickName;
    Button buttonSignup;
    Button buttonPhoto;
    TextView textviewSingin;
    TextView textviewMessage;
    ImageView userPhoto;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;
    Uri uri;
    private ArrayAdapter adapter;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinnerMajor); // 과 선택할 때 과목들 나열하게하는 레이아웃
        adapter = ArrayAdapter.createFromResource(this, R.array.Major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNickName = (EditText) findViewById(R.id.editTextNickName);
        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        progressDialog = new ProgressDialog(this);
        uri = getIntent().getParcelableExtra("imageUri");
        //button click event
        buttonSignup.setOnClickListener(this);
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RgistPhotoActivity.class);
                startActivity(intent);
            }
        });
        textviewSingin.setOnClickListener(this);
    }
    private  void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String nickname = editTextNickName.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "이름를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(nickname)){
            Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // User생성
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   finish();
                   startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
               }else{
                   textviewMessage.setText("에러유형\n : 이미 등록된 이메일입니다. \\n -암호 최소 6자리 이상 \\n - 서버에러");
                   Toast.makeText(MainActivity.this, "등록에러!!", Toast.LENGTH_SHORT).show();
               }
               progressDialog.dismiss();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == buttonSignup){
            registerUser();
        }if (v == textviewSingin){
            startActivity(new Intent(this, LoginActivity.class)); // 추가해줄 LoginActivity
        }
    }
}