package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter  extends RecyclerView.Adapter<VideoAdapter.viewholder>   {
    Context context;
    ArrayList<File> videArrayList;
    VideoAdapter videoAdapter = this;
    OnFileSelectedListener onFileSelectedListener;

    public VideoAdapter() {

    }




    public VideoAdapter(Context context, ArrayList<File> videArrayList,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.videArrayList = videArrayList;
        this.onFileSelectedListener=onFileSelectedListener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewvideo,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.textView.setText(videArrayList.get(position).getName());
        holder.imageView.setImageResource(R.drawable.ic_baseline_video_file_24);



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.OnFileLongClicked(videArrayList.get(position),position,videoAdapter);
                Log.d(TAG, "onfileselected called"+videoAdapter);
                return true;
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,videoView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("path",videArrayList.get(position).getAbsolutePath());
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return videArrayList.size();
    }

    public void filteredlist(ArrayList<File> filteredlist){
        videArrayList=filteredlist;
        notifyDataSetChanged();

    }









    public class viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public viewholder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.textView10);
            imageView = itemView.findViewById(R.id.imageView10);
        }
    }


}

