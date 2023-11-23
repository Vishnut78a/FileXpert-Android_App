package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
 List<File> filteredlist;
 Context context;
 Adapter adapter =this;
 boolean copyvisibility;
 boolean visibility;
 String code;
 OnFileSelectedListener onFileSelectedListener;
 String videoname,videopath;
  ArrayList<File> filesandfolders;
  public Adapter(){}


    public Adapter(Context context, boolean visibility, String videoname, String videopath, ArrayList<File> filesandfolders,String code,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.visibility = visibility;
        this.videoname = videoname;
        this.videopath = videopath;
        this.filesandfolders = filesandfolders;
        this.code=code;
        this.onFileSelectedListener=onFileSelectedListener;
    }
    public Adapter(Context context,  String videoname, String videopath,boolean copyvisibility, ArrayList<File> filesandfolders,String code,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.copyvisibility = copyvisibility;
        this.videoname = videoname;
        this.videopath = videopath;
        this.filesandfolders = filesandfolders;
        this.code=code;
        this.onFileSelectedListener=onFileSelectedListener;
    }

    public Adapter(Context context, ArrayList<File> filesandfolders, boolean visibility,String code,OnFileSelectedListener onFileSelectedListener){
        this.context = context;
        this.filesandfolders = filesandfolders;
        this.visibility=visibility;
        this.code=code;
        this.onFileSelectedListener=onFileSelectedListener;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.filelist,parent,false);
    return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
    File selectedFile = filesandfolders.get(position);

    holder.textView.setText(selectedFile.getName());
    if(selectedFile.isDirectory()){
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
    }
    else if(selectedFile.getName().endsWith(".mp4")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_video_file_24);
    }
    else if(selectedFile.getName().endsWith(".mp3")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_music_note_24);
    }
    else if(selectedFile.getName().endsWith(".zip")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_zip_24);

    }
    else if(selectedFile.getName().endsWith("jpg")|selectedFile.getName().endsWith("jpeg")){
         holder.imageView.setImageResource(R.drawable.ic_baseline_image_24);
    }
    else if(selectedFile.getName().endsWith(".apk")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_android_24);
    }
    else if(selectedFile.getName().endsWith(".pdf")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24);
    }
    else if(selectedFile.getName().endsWith(".enc")){
        holder.imageView.setImageResource(R.drawable.ic_baseline_lock_24);
    }
    else {
        holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
    }


     holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View view) {
             Log.d(TAG, "onLongClick: onlongclicked");
             onFileSelectedListener.OnFileLongClicked(filesandfolders.get(position),position, adapter);
             Log.d(TAG, "onLongClick: after onlonglicked before returning");
             return true;
         }
     });



    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(selectedFile.isDirectory()){
                Intent intent = new Intent(context,FileListActivity.class);
                String path = selectedFile.getAbsolutePath();
                intent.putExtra("path",path);
                intent.putExtra("code",code);
                intent.putExtra("position",position);
                intent.putExtra("p",position);

                if(visibility) {
                    intent.putExtra("visibility", true);
                    intent.putExtra("videopath",videopath);
                    intent.putExtra("code",code);
                    Log.d(TAG, "in Adapter"+videopath);
                    intent.putExtra("videoname",videoname);
                    Log.d(TAG, "in Adapter"+videoname);
                    Log.d(TAG, "in adapter class value recieved and setted in put extra is"+visibility );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else if(copyvisibility){
                    intent.putExtra("code",code);
                    intent.putExtra("copyvisibility",true);
                    intent.putExtra("videopath",videopath);
                    intent.putExtra("videoname",videoname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else{
                    intent.putExtra("visibility",false);
                    Log.d(TAG, "in adapter class value recieved and setted in put extra is"+visibility );
                     intent.putExtra("code",code);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

            }else{
                if(selectedFile.getName().endsWith(".jpg")|selectedFile.getName().endsWith(".jpeg")){
                Intent intent = new Intent(context,single_image_viewer.class);
                intent.putExtra("imagesrc",filesandfolders.get(position).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);}
                else if(selectedFile.getName().endsWith(".mp4")){
                    Intent intent = new Intent(context,videoView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("path",filesandfolders.get(position).getAbsolutePath());
                    context.startActivity(intent);
                }
                else if(selectedFile.getName().endsWith(".pdf")|selectedFile.getName().endsWith(".docx")|selectedFile.getName().endsWith(".doc")){
                    if(selectedFile.getName().endsWith("pdf")) {
                        try {
                            Intent intent = new Intent(context, pdf_view.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("data", selectedFile.getAbsolutePath());
                            context.startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(context, "No app to open this file", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        try {


                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedFile.getAbsolutePath()));
                            if (selectedFile.getName().contains(".docx") || selectedFile.getName().contains(".doc")) {
                                intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }catch (Exception e){
                            Toast.makeText(context, "No,app to open this file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if(selectedFile.getName().endsWith(".mp4")){
                    Intent intent = new Intent(context,videoView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("path",selectedFile.getAbsolutePath());
                    context.startActivity(intent);
                }
                else if(selectedFile.getName().endsWith(".mp3")){
                    Intent intent = new Intent(context,Audio_View.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data",selectedFile.getAbsolutePath());
                    context.startActivity(intent);
                }
                else if(selectedFile.getName().endsWith(".zip")){
                    File file = new File(Environment.getExternalStorageDirectory(),selectedFile.getAbsolutePath());
                    Uri fileUri = FileProvider.getUriForFile(context, "com.example.filemanager.fileprovider", file);
                    try {


                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        intent.setDataAndType(fileUri, "application/zip");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);

                    }catch (Exception e){
                        Toast.makeText(context, "No,app to open this file", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(selectedFile.getName().endsWith(".apk")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    Uri contentUri = FileProvider.getUriForFile(context, "com.example.filemanager.fileprovider", selectedFile);
                    intent.setDataAndType(contentUri, "application/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(intent, "Open file with"));

                }
                else{
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    Uri contentUri = FileProvider.getUriForFile(context, "com.example.filemanager.fileprovider", selectedFile);
                    intent.setDataAndType(contentUri, "application/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(intent, "Open file with"));

                }

            }

        }
    });
  }
    @Override
    public int getItemCount() {
        return filesandfolders.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder{
       TextView textView;
       ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView= itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
