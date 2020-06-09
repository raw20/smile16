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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private String CHAT_NAME;
    private String USER_NAME;
    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("nickname");

        openChat(CHAT_NAME);

        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat_edit.getText().toString().equals(""))
                    return;
                FirebaseChat chat = new FirebaseChat(USER_NAME, chat_edit.getText().toString());
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 채팅한 내역들이 데이터베이스로 넘어간다.
                chat_edit.setText(""); // 입력창 초기화
            }
        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        FirebaseChat firebaseChat = dataSnapshot.getValue(FirebaseChat.class);
        adapter.add(firebaseChat.getNickname() + " : " + firebaseChat.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        FirebaseChat firebaseChat = dataSnapshot.getValue(FirebaseChat.class);
        adapter.remove(firebaseChat.getNickname() + " : " + firebaseChat.getMessage());
    }

    private  void openChat(String chatName) { //현재 참여하고 있는 방에서 대화를 할 수 있게하고 대화 내용을 데이터베이스에 넘기는 함수
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1); //대화 내용 나열
        chat_view.setAdapter(adapter);
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
