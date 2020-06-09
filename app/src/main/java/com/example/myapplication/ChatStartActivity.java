package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatStartActivity extends AppCompatActivity {
    private EditText user_chat;
    private TextView user_edit;
    private Button user_next;
    private ListView chat_list;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public  ChatStartActivity() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_start);

        user_chat = (EditText) findViewById(R.id.user_chat);
        user_next = (Button) findViewById(R.id.user_next);
        user_edit = (TextView) findViewById(R.id.user_edit);
        chat_list = (ListView) findViewById(R.id.chat_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = firebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mRootRef.child("UserList");
        userRef.child(uid).addValueEventListener(new ValueEventListener() { //유저의 닉네임을 데이터베이스로 부터 가져온다
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userNickName = (String) dataSnapshot.child("nickname").getValue();
                user_edit.setText(userNickName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        user_next.setOnClickListener(new View.OnClickListener() { //방만들기
            @Override
            public void onClick(View v) { // 방만들기를 생성 후 방 제목과 유저의 제목을 chatActivity로 보내준다.
                Intent intent = new Intent(ChatStartActivity.this, ChatActivity.class);
                intent.putExtra("chatName", user_chat.getText().toString());
                intent.putExtra("nickname", user_edit.getText().toString());
                startActivity(intent);
            }
        });

        showChatList();
    }

    private void showChatList() { //현재 생성된 방 목록을 보여준다.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
