package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment {
    private static  final  int PICK_IMAGE_REQUST = 1;
    private static final String TAG = "UserProfileFragment";
    private EditText user_nick_ed;
    private TextView user_name, user_gender, user_email, user_school,user_major, textViewLogout;
    private Button buttonPhoto_ed, buttonEdit;
    private ImageView user_photo_ed;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Uri filePath;
    private StorageTask uploadTask;
    StorageReference mStorageRef;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);
        user_photo_ed =view.findViewById(R.id.user_photo_ed);
        user_nick_ed = view.findViewById(R.id.user_nick_ed);
        buttonPhoto_ed = view.findViewById(R.id.buttonPhoto_ed);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        user_name = view.findViewById(R.id.user_name);
        user_gender = view.findViewById(R.id.user_gender);
        user_email = view.findViewById(R.id.user_email);
        user_school = view.findViewById(R.id.user_school);
        user_major = view.findViewById(R.id.user_major);
        textViewLogout = view.findViewById(R.id.textViewLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = firebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = mRootRef.child("UserList");
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userNick = (String) dataSnapshot.child("nickname").getValue();
                String userImage = (String) dataSnapshot.child("photo").getValue();
                String userEmail = (String) dataSnapshot.child("email").getValue();
                String userName = (String) dataSnapshot.child("name").getValue();
                String userGender = (String) dataSnapshot.child("gender").getValue();
                String userSchool = (String) dataSnapshot.child("school").getValue();
                String userMajor = (String) dataSnapshot.child("major").getValue();
                user_nick_ed.setText(userNick);
                user_name.setText(userName);
                user_email.setText(userEmail);
                user_gender.setText(userGender);
                user_school.setText(userSchool);
                user_major.setText(userMajor);
                StorageReference getStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://smile16-201621220.appspot.com/Images/" + userImage);
                Glide.with(getActivity()).load(getStorageReference).into(user_photo_ed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        buttonPhoto_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUST);
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthSuccess(user);
                uploadFile();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                filePath = data.getData();
                Log.d(TAG, "uri:" + String.valueOf(filePath));
                try {
                    //InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    user_photo_ed.setImageBitmap(bitmap);
                    //Bitmap img = BitmapFactory.decodeStream(in);
                    //in.close();
                    //userPhoto.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private  String getExtension (Uri uri){ // 유저 프로필 사진을 문자열로 변환
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void onAuthSuccess(FirebaseUser user) { //Firebase Realtime에 이름과 닉네임 사진 등 다른 값들을 입력하기위한 함수
        String nickname = user_nick_ed.getText().toString().trim();
        String name = user_name.getText().toString();
        String gender = user_gender.getText().toString();
        String school = user_school.getText().toString();
        String major = user_major.getText().toString();
        String photo;
        photo = System.currentTimeMillis() + "." + getExtension(filePath);
        StorageReference Ref = mStorageRef.child(photo);
        uploadTask = Ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "프로필 사진 업로드 성공", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "프로필 사진 업로드 실패", Toast.LENGTH_LONG).show();
                    }
                });

        // Write new user
        writeNewUser(user.getUid(), name, user.getEmail(), nickname, gender, photo, school, major);
        startActivity(new Intent(getActivity(), RegisterActivity.class));
        getActivity().finish();

    }

    private void writeNewUser(String userId, String name, String email, String nickname, String genger, String photo, String school, String major) { //Firebase Realtime에 이름과 닉네임 사진 등 다른 값들을 저장하기위한 함수
        FirebasePost user = new FirebasePost(name, email, nickname, genger, photo, school, major);
        mDatabase.child("UserList").child(userId).setValue(user);
    }

    private void uploadFile(){ // Firebase Storage에 프로필 사진 저장하는 함수
        if(filePath != null){

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
                            Toast.makeText(getActivity(),"사진 업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"사진 업로드를 실패했습니다.",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), " 사진을 선택해주세요!!", Toast.LENGTH_SHORT).show();
        }
    }
}
