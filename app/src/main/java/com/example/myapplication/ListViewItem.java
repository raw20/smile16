package com.example.myapplication;

public class ListViewItem {
    private String uImage;
    private String uNick;
    private String uSchool;
    private String uMajor;
    private String uChat;

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
        return uChat;
    }

    public ListViewItem( String uImage, String uNick, String uSchool, String uMajor, String uChat){
        this.uImage = uImage;
        this.uNick=uNick;
        this.uSchool=uSchool;
        this.uMajor=uMajor;
        this.uChat=uChat;
    }

}
