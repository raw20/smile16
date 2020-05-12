package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class gps {
    public class MainActivity extends AppCompatActivity {

        private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,"권한 승인이 필요합니다", Toast.LENGTH_LONG).show();

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                    Toast.makeText(this,"000부분 사용을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();

                }
            }
        }
        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_CAMERA: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                    }
                    return;
                }

            }
        }
    }


}
