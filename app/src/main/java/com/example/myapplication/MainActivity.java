package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static  final  int PICK_IMAGE_REQUST = 1;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName, editTextNickName;
    Button buttonSignup;
    Button buttonPhoto;
    TextView textviewSingin;
    TextView textviewMessage;
    ImageView userPhoto;
    RadioButton Man, WoMan;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    String email, name, nickname;
    String gender = "";
    private DatabaseReference mPostReference;
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
        Man = (RadioButton) findViewById(R.id.Man);
        WoMan= (RadioButton) findViewById(R.id.WoMan);
        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        progressDialog = new ProgressDialog(this);
        //button click event
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUST);
            }
        });
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
    }
    private void setInsertMode(){
        editTextEmail.setText("");
        editTextName.setText("");
        editTextNickName.setText("");
        Man.setChecked(false);
        WoMan.setChecked(false);
        buttonSignup.setEnabled(true);
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


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // User생성
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    postFirebaseDatabase(true);
                    setInsertMode();
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "에러유형 : 이미 등록된 이메일이거나 암호가 6자리 미만입니다.", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    userPhoto.setImageBitmap(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
   public void  postFirebaseDatabase (boolean add){
       mPostReference = FirebaseDatabase.getInstance().getReference();
       Map<String, Object> childUpdates = new HashMap<>();
       Map<String, Object> postValues = null;
       if (add){
           FirebasePost post = new FirebasePost(email, name, nickname,gender);
           postValues = post.toMap();
           System.out.println("ㅇㅇ");
       }
       childUpdates.put("/User List/" + email, postValues);
       mPostReference.updateChildren(childUpdates);
   }
}