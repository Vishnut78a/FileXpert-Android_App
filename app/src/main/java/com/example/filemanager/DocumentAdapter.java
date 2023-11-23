package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class DocumentAdapter extends  RecyclerView.Adapter<DocumentAdapter.Viewholder> {
    Context context;
    List<File> files;
     OnFileSelectedListener onFileSelectedListener;
     DocumentAdapter documentAdapter=this;
    public DocumentAdapter(Context context, List<File> files,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.files = files;

        this.onFileSelectedListener=onFileSelectedListener;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpdf,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView.setText(files.get(position).getName());
        if(files.get(position).getName().endsWith(".pdf")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24);
        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_2342);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.OnFileLongClicked(files.get(position),position,documentAdapter);
                return true;
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(files.get(position).getName().endsWith("pdf")) {
                    try {
                        Intent intent = new Intent(context, pdf_view.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("data", files.get(position).getAbsolutePath());
                        context.startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(context, "No app to open this file", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    try {


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(files.get(position).getAbsolutePath()));
                        if (files.get(position).getName().contains(".docx") || files.get(position).getName().contains(".doc")) {
                            intent.setDataAndType(Uri.parse(files.get(position).getAbsolutePath()), "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }catch (Exception e){
                        Toast.makeText(context, "No,app to open this file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView textView ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView2);
            textView=itemView.findViewById(R.id.textView3);
        }
    }
}
