package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private  static  final String TAG = "RegisterActivity";
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private TextView textivewDelete;
    private FirebaseAuth firebaseAuth;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 처음오로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);
        firebaseAuth = FirebaseAuth.getInstance(); // 유저가 로그인 하지 않은 상태라면 null 상태이고 액티비리 종료하고 LoginActivity로 이동
        textViewUserEmail.setText("반갑습니다.\n"+textViewUserEmail+"으로 로그인하였습니다.");
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }if (v == textivewDelete){
            AlertDialog.Builder alter_confirm = new AlertDialog.Builder(RegisterActivity.this);
            alter_confirm.setMessage("정말 삭제할거?? ㅜㅜ").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this,"계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            });
                }
            });
            alter_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Toast.makeText(RegisterActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alter_confirm.show();
        }

        }
}
