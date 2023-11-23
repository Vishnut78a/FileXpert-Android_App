package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class apkAdapter extends RecyclerView.Adapter<apkAdapter.viewHOlder> {
    ArrayList<File> uriArrayList;
    Context context;
    apkAdapter apkAdapter=this;
    OnFileSelectedListener onFileSelectedListener;
    public apkAdapter( Context context,ArrayList<File> uriArrayList,OnFileSelectedListener onFileSelectedListener) {
        this.uriArrayList =uriArrayList;
        this.onFileSelectedListener=onFileSelectedListener;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.apk_view,parent,false);

        return new viewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHOlder holder, int position) {
        holder.textView.setText(uriArrayList.get(position).getName());
        holder.imageView.setImageResource(R.drawable.ic_baseline_android_24);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.OnFileLongClicked(uriArrayList.get(position),position,apkAdapter);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.example.filemanager.fileprovider", uriArrayList.get(position));

                intent.setDataAndType(contentUri, "application/*");

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class viewHOlder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public viewHOlder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView30);
            imageView = itemView.findViewById(R.id.imageView30);
        }
    }
}
