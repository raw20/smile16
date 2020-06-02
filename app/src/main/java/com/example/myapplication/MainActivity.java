package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static  final  int PICK_IMAGE_REQUST = 1;
    String strGender;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName, editTextNickName;
    Button buttonSignup;
    Button buttonPhoto;
    TextView textviewSingin;
    TextView textviewMessage;
    ImageView userPhoto;
    RadioGroup GenderGroup;
    RadioButton GenderOption;
    StorageReference mStorageRef;
    ProgressDialog progressDialog;

    //define firebase object
    FirebaseAuth firebaseAuth; //Firebase에 이메일과 비밀번호를 저장하기 위한 변수
    private ArrayAdapter adapter;
    private Spinner spinner;
    private DatabaseReference mDatabase; // Firebase Realtime에 다른 변수들을 저장하기위한 변수
    private Uri filePath;
    private StorageTask uploadTask;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinnerMajor); // 과 선택할 때 과들을 나열하게하는 레이아웃
        adapter = ArrayAdapter.createFromResource(this, R.array.Major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
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
        GenderGroup = findViewById(R.id.GenderGroup);
        GenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // 성별 체크 후 값을 문자로 얻어와 데이터베이스에 넣기 위한 코드.
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                GenderOption = GenderGroup.findViewById(i);
                switch (i) {
                    case R.id.Man :
                        strGender = GenderOption.getText().toString();
                        break;
                    case R.id.WoMan :
                        strGender = GenderOption.getText().toString();
                        break;
                    default:
                }
            }
        });

        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        progressDialog = new ProgressDialog(this);
        //button click event
        buttonPhoto.setOnClickListener(new View.OnClickListener() { //버튼을 누르면 갤러리로 이동해서 사진선택
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

    private void registerUser(){ // 회원가입 후 데이터베이스에 정보를 저장하기 위한 함수
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
                    uploadFile();
                    onAuthSuccess(task.getResult().getUser());
                } else {
                    Toast.makeText(MainActivity.this, "에러유형 : 이미 등록된 이메일이거나 암호가 6자리 미만입니다.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private  String getExtension ( Uri uri){ // 유저 프로필 사진을 문자열로 변환
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void onAuthSuccess(FirebaseUser user) { //Firebase Realtime에 이름과 닉네임 사진 등 다른 값들을 입력하기위한 함수
        String name = editTextName.getText().toString().trim();
        String nickname = editTextNickName.getText().toString().trim();
        String gender = GenderOption.getText().toString().trim();
        String photo;
        photo = System.currentTimeMillis() + "." + getExtension(filePath);
        StorageReference Ref = mStorageRef.child(photo);
        uploadTask = Ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "프로필 사진 업로드 성공", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "프로필 사진 업로드 실패", Toast.LENGTH_LONG).show();
                    }
                });

        // Write new user
        writeNewUser(user.getUid(), name, user.getEmail(), nickname, gender, photo);

        // Go to MainActivity
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
    }

    private void writeNewUser(String userId, String name, String email, String nickname, String genger, String photo) { //Firebase Realtime에 이름과 닉네임 사진 등 다른 값들을 저장하기위한 함수
        FirebasePost user = new FirebasePost(name, email, nickname, genger, photo);

        mDatabase.child("UserList").child(userId).setValue(user);
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
                filePath = data.getData();
                Log.d(TAG, "uri:" + String.valueOf(filePath));
                try{
                    //InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    userPhoto.setImageBitmap(bitmap);
                    //Bitmap img = BitmapFactory.decodeStream(in);
                    //in.close();
                    //userPhoto.setImageBitmap(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private  void uploadFile(){ // Firebase Storage에 프로필 사진 저장하는 함수
        if(filePath != null){
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("프로필 사진 업로드중..");
            //progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".PNG";   //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference mStorageRef = storage.getReferenceFromUrl("gs://smile16-201621220.appspot.com").child("images/" + filename);
            mStorageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); // 업로드 진행 Dialog 상자닫기
                            Toast.makeText(getApplicationContext(),"사진 업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"사진 업로드를 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //@SuppressWarnings("VisibleForTests") // 이걸 넣어야 에러가 사라진다고 함
                            //double progress = (100* taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog.setMessage("사진업로드중.." + ((int) progress) + "%...");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), " 사진을 선택해주세요!!", Toast.LENGTH_SHORT).show();
        }

    }
}