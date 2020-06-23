package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private  static final int MY_PERMISSION_STORAGE = 1111;
    private  static  final String TAG = "RegisterActivity";
    private TextView textViewUserEmail, textViewName, photoURL;
    private Button buttonLogout, buttonstart;
    private TextView textivewDelete;
    private FirebaseAuth firebaseAuth;
    private FirebasePost firebasePost;
    private ImageView userProfilePhoto;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = firebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = mRootRef.child("UserList");

    @Override
    protected void onCreate(final Bundle savedInstanceState) { // 액티비티 처음오로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        textViewName = (TextView) findViewById(R.id.textViewName);
        photoURL = (TextView) findViewById(R.id.photoURL);
        buttonstart = (Button) findViewById(R.id.buttonstart);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);
        userProfilePhoto = (ImageView) findViewById(R.id.userProfilePhoto);
        textivewDelete.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance(); // 유저가 로그인 하지 않은 상태라면 null 상태이고 액티비리 종료하고 LoginActivity로 이동
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = firebaseAuth.getCurrentUser().getUid();
        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.child("name").getValue();
                String userImage = (String) dataSnapshot.child("photo").getValue();
                StorageReference getStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://smile16-201621220.appspot.com/Images/" + userImage);
                Glide.with(RegisterActivity.this).load(getStorageReference).into(userProfilePhoto);
                textViewName.setText(userName+"님 어서오세요");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        textViewUserEmail.setText(user.getEmail()+"으로 로그인하였습니다.");
        buttonLogout.setOnClickListener(this);
        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }if (v == textivewDelete){
            AlertDialog.Builder alter_confirm = new AlertDialog.Builder(RegisterActivity.this);
                    alter_confirm.setMessage("정말 삭제할거?? ㅜㅜ").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                               dataSnapshot1.getRef().removeValue();
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {}
                   });
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this,"계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            });
                }
            });
            alter_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Toast.makeText(RegisterActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alter_confirm.show();
        }
    }
}

