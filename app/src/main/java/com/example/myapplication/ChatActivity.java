package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String CHAT_NAME;
    private String USER_NAME, USER_GENDER, USER_SCHOOL, USER_MAJOR, USER_PHOTO;
    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send, map_sent;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);
        map_sent = (Button) findViewById(R.id.map_sent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("nickname");
        USER_GENDER = intent.getStringExtra("gender");
        USER_SCHOOL = intent.getStringExtra("school");
        USER_MAJOR =  intent.getStringExtra("major");
        USER_PHOTO =  intent.getStringExtra("photo");

        openChat(CHAT_NAME);
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat_edit.getText().toString().equals(""))
                    return;
                FirebaseChat chat = new FirebaseChat(USER_PHOTO, USER_NAME, USER_GENDER, USER_SCHOOL, USER_MAJOR, chat_edit.getText().toString());
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 채팅한 내역들이 데이터베이스로 넘어간다.
                chat_edit.setText(""); // 입력창 초기화
            }
        });
        map_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChatActivity.this, MapActivity.class);
                startActivity(intent1);
            }
        });
    }


    private void addMessage(DataSnapshot dataSnapshot, ArrayList<ListViewItem> data) {
        FirebaseChat firebaseChat = dataSnapshot.getValue(FirebaseChat.class);
        ListViewItem user = new ListViewItem(firebaseChat.getPhoto(), firebaseChat.getNickname(), firebaseChat.getSchool(), firebaseChat.getMajor(), firebaseChat.getMessage());
        data.add(user);
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayList<ListViewItem> data) {
        FirebaseChat firebaseChat = dataSnapshot.getValue(FirebaseChat.class);
        ListViewItem user = new ListViewItem(firebaseChat.getPhoto(), firebaseChat.getNickname(), firebaseChat.getSchool(), firebaseChat.getMajor(), firebaseChat.getMessage());
        data.remove(user);
    }

    private void openChat(String chatName) {
        final ArrayList<ListViewItem> data=new ArrayList<>();//현재 참여하고 있는 방에서 대화를 할 수 있게하고 대화 내용을 데이터베이스에 넘기는 함수
        ListViewItem user = new ListViewItem(USER_PHOTO, USER_NAME, USER_SCHOOL, USER_MAJOR, chat_edit.getText().toString());
        data.add(user);
        chat_view.setAdapter(null);
        final ListviewAdapter adapter = new ListviewAdapter(this, R.layout.item, data); //대화 내용 나열
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addMessage(dataSnapshot, data);
                chat_view.setAdapter(adapter);
                chat_view.setSelection(adapter.getCount()-1);
                adapter.notifyDataSetChanged();
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                removeMessage(dataSnapshot, data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
