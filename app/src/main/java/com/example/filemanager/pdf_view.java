package com.example.filemanager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdf_view extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);
        PDFView pdfView =findViewById(R.id.pdfview);

        File file = new File(getIntent().getStringExtra("data"));
        pdfView
                .fromFile(file)
                .load();

    }
}
