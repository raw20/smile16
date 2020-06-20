package com.example.myapplication;

public class FirebaseChat {
    private String nickname;
    private String gender;
    private String school;
    private String major;
    private String message;

    public  FirebaseChat() {}
    public  FirebaseChat( String nickname, String gender, String school, String major, String message) {

        this.nickname = nickname;
        this.gender = gender;
        this.school = school;
        this.major = major;
        this.message = message;

    }

    public void setMessage (String message) {
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

    public void  setGender (String gender) {
        this.gender = gender;
    }

    public String getGender () {
        return  gender;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

}
