package com.example.filemanager;

import java.io.File;

public interface OnFileSelectedListener {

     void OnFileLongClicked(File file,int i,VideoAdapter videoAdapter);
     void OnFileLongClicked(File file,int i,ImageAdapter imageAdapter);
     void OnFileLongClicked(File file,int i,DocumentAdapter documentAdapter);
     void OnFileLongClicked(File file,int i,AudioAdapter AudioAdapter);
     void OnFileLongClicked(File file,int i,archieveAdapter archieveAdapter);
     void OnFileLongClicked(File file,int i,apkAdapter apkAdapter);
     void OnFileLongClicked(File file,int i,Adapter adapter);


}
