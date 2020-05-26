package com.example.myapplication;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    public String email;
    public String name;
    public String nickname;
    public String gender;

    public FirebasePost(){}

    public FirebasePost(String email, String name, String nickname, String gender){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("nickname", nickname);
        result.put("gender", gender);
        return result;
    }
}
