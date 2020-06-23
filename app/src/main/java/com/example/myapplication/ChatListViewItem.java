package com.example.myapplication;

public class ChatListViewItem {
    private String uImage;
    private String uNick;
    private String uSchool;
    private String uMajor;
    private String uChatName;


    public String getuImage(){return uImage;}

    public String getUnick() {
        return uNick;
    }

    public String getUschool() {
        return uSchool;
    }

    public String getuMajor() {
        return uMajor;
    }

    public String getuChat() {
        return uChatName;
    }

    public ChatListViewItem ( String uImage, String uNick, String uSchool, String uMajor, String uChatName){
        this.uImage = uImage;
        this.uNick=uNick;
        this.uSchool=uSchool;
        this.uMajor=uMajor;
        this.uChatName=uChatName;
    }
}
