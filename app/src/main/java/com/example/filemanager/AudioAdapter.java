package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.vh> {
    Context context;
    ArrayList<File> arrayList;
    OnFileSelectedListener onFileSelectedListener;
    AudioAdapter audioAdapter = this;
    public AudioAdapter(Context context, ArrayList<File> arrayList,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onFileSelectedListener=onFileSelectedListener;
    }


    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_view,parent,false);

        return new vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        holder.imageView.setImageResource(R.drawable.ic_baseline_music_note_24);
        holder.textView.setText(arrayList.get(position).getName());

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
             onFileSelectedListener.OnFileLongClicked(arrayList.get(position),position,audioAdapter);
               return true;
           }
       });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Audio_View.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",arrayList.get(position).getAbsolutePath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class vh extends RecyclerView.ViewHolder {
        ImageView imageView  ;
        TextView textView;

        public vh(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView9);
            textView = itemView.findViewById(R.id.textView9);

        }
    }
}

