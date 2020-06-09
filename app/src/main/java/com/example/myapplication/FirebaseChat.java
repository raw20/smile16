package com.example.myapplication;

public class FirebaseChat {
    private String nickname;
    private String message;


    public  FirebaseChat() {}
    public  FirebaseChat(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public void  setMessage (String message) {
        this.message = message;
    }

    public  String getMessage () {
        return  message;
    }

    public void  setNickname (String nickname) {
        this.nickname = nickname;
    }

    public String getNickname () {
        return  nickname;
    }
}
