package com.example.myapplication;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    public String name;
    public String email;
    public String nickname;
    public String gender;
    public String photo;
    public String school;
    public String major;

    public FirebasePost(){}

    public FirebasePost(String name,String email, String nickname, String gender, String photo, String school, String major) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.photo = photo;
        this.school = school;
        this.major = major;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("nickname", nickname);
        result.put("gender", gender);
        result.put("photo", photo);
        result.put("school", school);
        result.put("major", major);
        return result;
    }

    public  void setNickname (String nickname){
        this.nickname = nickname;
    }

    public  String getNickname () {
        return nickname;
    }

    public  void setPhoto (String photo){
        this.photo = photo;
    }

    public  String getPhoto () {
        return photo;
    }

}
