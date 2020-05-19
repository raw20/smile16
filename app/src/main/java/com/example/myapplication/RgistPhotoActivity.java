package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.InputStream;


public class RgistPhotoActivity extends AppCompatActivity {
    private static  final  int PICK_IMAGE_REQUST = 1;
    private static  final  int IMAGE_ACTIVITY = 1;

    private  Button btn_photo, btn_save;
    private  ImageView iv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgist_photo);

        iv_result = (ImageView) findViewById(R.id.iv_result);
        btn_photo = (Button) findViewById(R.id.btn_photo);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //갤러리 호출
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUST);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RgistPhotoActivity.this, MainActivity.class);
                intent.putExtra("imageUri", intent);
                startActivityForResult(intent, IMAGE_ACTIVITY);
                if(intent != null ){
                    Toast.makeText(RgistPhotoActivity.this, "사진첨부 완료!!", Toast.LENGTH_SHORT).show();
                }
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
                    iv_result.setImageBitmap(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
