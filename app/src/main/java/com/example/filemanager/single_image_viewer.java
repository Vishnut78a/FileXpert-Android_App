package com.example.filemanager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class single_image_viewer extends MainActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_viewer);
        ImageView imageView = findViewById(R.id.imageView4);

        String file = getIntent().getStringExtra("imagesrc");
        File file3 = new File(file);
        Uri uri =  Uri.parse("file://"+file);
        Glide.with(getApplicationContext())
                .load(uri)
                .into(imageView);








    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
