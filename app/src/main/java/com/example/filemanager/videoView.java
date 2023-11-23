package com.example.filemanager;

import android.os.Bundle;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class videoView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        android.widget.VideoView videoView =findViewById(R.id.videoView3);
        String path = getIntent().getStringExtra("path");
        videoView.setVideoPath(path);

        videoView.start();
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}
