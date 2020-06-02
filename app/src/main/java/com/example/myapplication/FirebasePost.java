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

    public FirebasePost(){}


    public FirebasePost(String name,String email, String nickname, String gender, String photo) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.photo = photo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("nickname", nickname);
        result.put("gender", gender);
        result.put("photo", photo);
        return result;
    }

    public String getProfilePic(){
        return  photo;
    }

    public  void setPhotoUrl (String photo){
        this.photo = photo;
    }
}
