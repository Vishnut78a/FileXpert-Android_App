package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Viewholder> {
    Context context;

    OnFileSelectedListener onFileSelectedListener;
    ImageAdapter imageAdapter = this;
    ArrayList<File> listofimages ;


    public  ImageAdapter(Context context,ArrayList<File> listofimages,OnFileSelectedListener onFileSelectedListener){
        this.context =context;
        this.listofimages = listofimages;
        this.onFileSelectedListener=onFileSelectedListener;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        Viewholder viewholder = new Viewholder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Glide.with(context)
                .load(listofimages.get(position))
                .error(R.drawable.ic_baseline_android_24)
                .into(holder.imageView);

       
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.OnFileLongClicked(listofimages.get(position),position,imageAdapter);
                return true;
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,single_image_viewer.class);
                intent.putExtra("imagesrc",listofimages.get(position).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listofimages.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public Viewholder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
        }
    }
}
