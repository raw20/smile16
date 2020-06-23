package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListFragment extends Fragment implements RewardedVideoAdListener {
    private EditText user_chat;
    private TextView user_edit, user_school, user_major, user_gender, user_photo, user_AD, user_Coin;
    private Button user_next;
    private ListView chat_list;
    private int coinCount;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_chat_start, container, false);
        user_chat = (EditText) view.findViewById(R.id.user_chat);
        user_school = (TextView) view.findViewById(R.id.user_school);
        user_major = (TextView) view.findViewById(R.id.user_major);
        user_gender = (TextView) view.findViewById(R.id.user_gender);
        user_next = (Button) view.findViewById(R.id.user_next);
        user_edit = (TextView) view.findViewById(R.id.user_edit);
        chat_list = (ListView) view.findViewById(R.id.chat_list);
        user_photo = (TextView) view.findViewById(R.id.user_photo);
        user_AD = (TextView) view.findViewById(R.id.user_AD);
        user_Coin = (TextView) view.findViewById(R.id.user_Coin);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRootRef = firebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mRootRef.child("UserList");
        MobileAds.initialize(getActivity(), "ca-app-pub-7076823123428841~2017453479");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadReWardedVideoAd();
        coinCount = 999;
        user_Coin.setText("남은 코인 : " + coinCount);

        user_AD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        userRef.child(uid).addValueEventListener(new ValueEventListener() { //유저의 닉네임을 데이터베이스로 부터 가져온다
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userNickName = (String) dataSnapshot.child("nickname").getValue();
                String userGender = (String) dataSnapshot.child("gender").getValue();
                String userSchool = (String) dataSnapshot.child("school").getValue();
                String userMajor = (String) dataSnapshot.child("major").getValue();
                String userPhoto = (String) dataSnapshot.child("photo").getValue();
                user_edit.setText(userNickName);
                user_gender.setText(userGender);
                user_school.setText(userSchool);
                user_major.setText(userMajor);
                user_photo.setText(userPhoto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        user_next.setOnClickListener(new View.OnClickListener() { //방만들기
            @Override
            public void onClick(View v) { // 방만들기를 생성 후 방 제목과 유저의 제목을 chatActivity로 보내준다.
                if(coinCount < 5){
                    Toast.makeText(getActivity(), "코인이 부족합니다. 광고시청후 입장해주세요.", Toast.LENGTH_SHORT).show();
                }else if(user_chat.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "방제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    coinCount = coinCount -5;
                    user_Coin.setText("남은 코인 : " + coinCount);
                    intent.putExtra("chatName", user_chat.getText().toString());
                    intent.putExtra("nickname", user_edit.getText().toString());
                    intent.putExtra("gender", user_gender.getText().toString());
                    intent.putExtra("school", user_school.getText().toString());
                    intent.putExtra("major", user_major.getText().toString());
                    intent.putExtra("photo", user_photo.getText().toString());
                    startActivity(intent);
                }
            }
        });
        showChatList();
        return view;
    }

    private void loadReWardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    private void showChatList() { //현재 생성된 방 목록을 보여준다.
        // 리스트 어댑터 생성 및 세팅
        final ArrayList<ChatListViewItem> data = new ArrayList<>();
        final ChatListviewAdapter adapter = new ChatListviewAdapter(getActivity(), R.layout.chatitem, data);
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                ChatListViewItem user = new ChatListViewItem(user_photo.getText().toString(), user_edit.getText().toString(), user_school.getText().toString(), user_major.getText().toString(), dataSnapshot.getKey());
                data.add(user);
                chat_list.setAdapter(null);
                chat_list.setAdapter(adapter);
                chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(coinCount<5){
                            Toast.makeText(getActivity(), "코인이 부족합니다. 광고시청후 입장해주세요.", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            coinCount = coinCount -5;
                            user_Coin.setText("남은 코인 : " + coinCount);
                            intent.putExtra("chatName",dataSnapshot.getKey());
                            intent.putExtra("nickname", user_edit.getText().toString());
                            intent.putExtra("school", user_school.getText().toString());
                            intent.putExtra("major", user_major.getText().toString());
                            intent.putExtra("photo", user_photo.getText().toString());
                            startActivity(intent);
                        }
                    }
                });

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
    private void addCoins(final int coins){
        coinCount += coins;
        user_Coin.setText("남은 코인 : " + coinCount);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(getActivity(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(getActivity(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(getActivity(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(getActivity(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        loadReWardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(getActivity(), "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
        addCoins(reward.getAmount());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(getActivity(), "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(getActivity(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(getActivity(), "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(getActivity());
        super.onDestroy();
    }

}
