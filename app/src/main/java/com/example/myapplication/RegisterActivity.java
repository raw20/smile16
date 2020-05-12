package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private  static  int PICK_IMAGE_REQUST = 1;
    private EditText et_id, et_pw, et_name, et_age, et_username, et_school, et_major, et_area, et_mail;
    private ImageView view_image;
    private Button btn_photo, btn_regist;
    static  final String TAG = "MainActivity";

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 처음오로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_username = findViewById(R.id.et_username);
        et_school = findViewById(R.id.et_shcool);
        et_major = findViewById(R.id.et_major);
        et_area = findViewById(R.id.et_area);
        et_mail = findViewById(R.id.et_mail);

        //프사첨부 및 회원가입
        view_image = findViewById(R.id.view_image);
        btn_photo = findViewById(R.id.btn_photo);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUST);
            }
        });

        btn_regist = findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = et_id.getText().toString();
                String userPW = et_pw.getText().toString();
                String userNAME = et_name.getText().toString();
                String userUSERNAME = et_username.getText().toString();
                String userSCHOOL = et_school.getText().toString();
                String userMAJOR = et_major.getText().toString();
                String userAREA = et_area.getText().toString();
                String userEMAIL = et_mail.getText().toString();
                int userPhoto = view_image.getImageAlpha();
                int userAGE = Integer.parseInt(et_age.getText().toString());
                JSONObject js = new JSONObject();
                try{
                    js.accumulate("Uid", userID);
                    js.accumulate("Username", userNAME);
                    js.accumulate("Username1", userUSERNAME);
                    js.accumulate("Email", userEMAIL);
                    js.accumulate("Password", userPW);
                    js.accumulate("Photo", userPhoto);
                    js.accumulate("Age", userAGE);
                    js.accumulate("School", userSCHOOL);
                    js.accumulate("Major", userMAJOR);
                    js.accumulate("Area", userAREA);
                    String str;
                    str = js.toString();
                    HttpPost hp = new HttpPost();
                    hp.execute(str);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success){ //회원가입 성공
                                Toast.makeText(getApplicationContext(),"회원가입 성공!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"회원가입 실패!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    view_image.setImageBitmap(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
