package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.util.ArrayList;

public class archieveAdapter extends RecyclerView.Adapter<archieveAdapter.vewholder> {
    Context context;
    ArrayList<File> arrayList;
    archieveAdapter archieveAdapter=this;
    OnFileSelectedListener onFileSelectedListener;
    public archieveAdapter(Context context, ArrayList<File> arrayList,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onFileSelectedListener=onFileSelectedListener;
    }



    @NonNull
    @Override
    public vewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.archievelview,parent,false);

        return new vewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vewholder holder, int position) {
        holder.textView.setText(arrayList.get(position).getName());
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_zip_24);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.OnFileLongClicked(arrayList.get(position),position,archieveAdapter);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory(),arrayList.get(position).getAbsolutePath());
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
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class vewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public vewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView40);
            imageView= itemView.findViewById(R.id.imageView40);
        }
    }
}

