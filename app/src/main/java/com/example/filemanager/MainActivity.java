package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
        }
    }
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.switch1);
        Button storagebtn;
        Button imagebtn;
        Button videobtn;
        Button pdfbtn;
        Button apkbtn;
        Button audiobtn;
        Button archievbtn;


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        storagebtn = findViewById(R.id.button8);
        storagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    Intent intent = new Intent(MainActivity.this,FileListActivity.class);
                    String path = Environment.getExternalStorageDirectory().getPath();
                    intent.putExtra("visibility",false);
                    intent.putExtra("code","null");
                    intent.putExtra("path",path);
                    startActivity(intent);
                }
                else{
                    requestPermission();
                }
            }
        });
        imagebtn = findViewById(R.id.button2);
        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                startActivity(intent);


            }
        });

        audiobtn=findViewById(R.id.button3);
        audiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,AudioActivity.class);
            startActivity(intent);
            }
        });

         videobtn=findViewById(R.id.button4);
         videobtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,VideoActivity.class);
               startActivity(intent);

             }
         });

          pdfbtn=findViewById(R.id.button5);
          pdfbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(MainActivity.this,pdf_list_activity.class);
                  startActivity(intent);
              }
          });

          apkbtn=findViewById(R.id.button6);
          apkbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,ApkActivity.class);

               startActivity(intent);
              }
          });

          archievbtn=findViewById(R.id.button7);
          archievbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(MainActivity.this,archiveActivity.class);
                  startActivity(intent);
              }
          });

    }
    public void requestPermission(){
     if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
        }}
    public boolean checkPermission(){
     int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
     if(result== PackageManager.PERMISSION_GRANTED){
         return true;
     }
     else{
         return false;
     }
    }
}