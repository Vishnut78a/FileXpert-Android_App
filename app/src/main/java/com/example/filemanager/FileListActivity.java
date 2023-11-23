package com.example.filemanager;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileListActivity extends AppCompatActivity implements OnFileSelectedListener {

ArrayList<File> filesandfolders ;
Button movebutton;
Button copybutton;
Button cancebutton;

String code;
OnFileSelectedListener onFileSelectedListener=this;
boolean backpressed=false;
boolean button;
boolean decrypt = false;
boolean secondtime = false;
boolean copybuttonvalue;
    String[] items={"Details","Rename","Move","Copy","Delete","Share","Encrypt","Decrypt"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);




        copybuttonvalue=getIntent().getBooleanExtra("copyvisibility",false);
        copybutton=findViewById(R.id.copybutton);
        movebutton=findViewById(R.id.movebutton);
        cancebutton =findViewById(R.id.cancel_button);
        code=getIntent().getStringExtra("code");
        decrypt =getIntent().getBooleanExtra("decrypt",false);
        secondtime=getIntent().getBooleanExtra("secondtime",false);
        Log.d(TAG, "onCreate: "+code);
        button=getIntent().getBooleanExtra("visibility",false);
        Log.d(TAG, "Registered value of button in filelistactivity is "+button);

        if(button){
            movebutton.setVisibility(View.VISIBLE);
            cancebutton.setVisibility(View.VISIBLE);


        }
        else if(copybuttonvalue){
            copybutton.setVisibility(View.VISIBLE);
            cancebutton.setVisibility(View.VISIBLE);
        }
        else{
            movebutton.setVisibility(View.INVISIBLE);
            cancebutton.setVisibility(View.INVISIBLE);
        }





        cancebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(code.equals("video")){
                Intent intent = new Intent(FileListActivity.this,VideoActivity.class);
                startActivity(intent);
                finishAffinity();}
               else if(code.equals("audio")){
                   Intent intent = new Intent(FileListActivity.this,AudioActivity.class);
                   startActivity(intent);
                   finishAffinity();}

               else if(code.equals("images")){
                Intent intent = new Intent(FileListActivity.this,ImageActivity.class);
                startActivity(intent);
                finishAffinity();}

               else if(code.equals("archive")){

                   Intent intent = new Intent(FileListActivity.this,archiveActivity.class);
                   startActivity(intent);
                   finishAffinity();}
               else if (code.equals("doc")) {
                   Intent intent = new Intent(FileListActivity.this,pdf_list_activity.class);
                   startActivity(intent);
                   finishAffinity();}

               else if(code.equals("apk")){
                   Intent intent = new Intent(FileListActivity.this,ApkActivity.class);
                   startActivity(intent);
                   finishAffinity();}

            }
        });


        filesandfolders= new ArrayList<>();
        String path = getIntent().getStringExtra("path");
        File root = new File(path);
        File[] filesandfolderss = root.listFiles();

        for(File file:filesandfolderss){
            filesandfolders.add(file);
        }
        FloatingActionButton floatingActionButton =findViewById(R.id.floatingActionButton2);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(copybuttonvalue){
            Log.d(TAG, "onCreate: copy adapter"+code);
            recyclerView.setAdapter(new Adapter(FileListActivity.this, getIntent().getStringExtra("videoname"),getIntent().getStringExtra("videopath"),true,filesandfolders,code,this));

        }
        else if(button) {
            Log.d(TAG, "In FileListActivity setting the adapter as boolean value is : "+button+code);
            recyclerView.setAdapter(new Adapter(FileListActivity.this, true,getIntent().getStringExtra("videoname"),getIntent().getStringExtra("videopath"),filesandfolders,code,this));
        }
         else{
            Log.d(TAG, "In FileListActivity setting the adapter as boolean value is : "+button+code);
             recyclerView.setAdapter(new Adapter(FileListActivity.this,filesandfolders,false,code,onFileSelectedListener));
        }



        movebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String srcfile =getIntent().getStringExtra("videopath");
                Log.d(TAG, "String srcfile =  "+srcfile);
                String name = getIntent().getStringExtra("videoname");
                Log.d(TAG, "String name =  "+name);
                File source = new File(srcfile);
                Log.d(TAG, "File source name= "+ source.getName()+" "+"path:"+source.getAbsolutePath());
                File dest = new File(path+"/"+name);
                Log.d(TAG, "File dest name ="+dest.getName()+" "+"path: "+dest.getAbsolutePath());
                if(source.exists()){
                    boolean ismoved =source.renameTo(dest);
                    if(ismoved){
                        Toast.makeText(FileListActivity.this, "Moved", Toast.LENGTH_SHORT).show();
                        filesandfolders.add(dest);
                        recyclerView.setAdapter(new Adapter(FileListActivity.this,filesandfolders,false,code,onFileSelectedListener));
                        movebutton.setVisibility(View.INVISIBLE);
                        cancebutton.setVisibility(View.INVISIBLE);
                       backpressed=true;


                    }else{
                        Toast.makeText(FileListActivity.this, "Can't Moved", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FileListActivity.this, "Doesn't Exists", Toast.LENGTH_SHORT).show();
                }

            }
        });


         copybutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String name = getIntent().getStringExtra("videoname");
                 String videopath =getIntent().getStringExtra("videopath");
                 File file = new File(path+"/"+name);
                 if(file.exists()){

                   String onlyname =name.substring(0,name.lastIndexOf("."));
                   String extension = videopath.substring(videopath.lastIndexOf("."));
                   int i =1;
                    file = new File(path+"/"+onlyname+i+extension);

                   while(file.exists()){
                       i++;
                       file = new File(path+"/"+onlyname+i+extension);
                   }
                  File srcfile =new File(videopath);
                   try{
                   FileInputStream fileInputStream = new FileInputStream(srcfile);
                   FileOutputStream fileOutputStream = new FileOutputStream(file);
                   byte[] buffer = new byte[1024];
                   int length;
                   while((length=fileInputStream.read(buffer))>0){
                       fileOutputStream.write(buffer,0,length);
                   }
                   fileInputStream.close();
                   fileOutputStream.close();
                   filesandfolders.add(file);
                       recyclerView.setAdapter(new Adapter(FileListActivity.this,filesandfolders,false,code,onFileSelectedListener));

                       Toast.makeText(FileListActivity.this, "Copied!!!", Toast.LENGTH_SHORT).show();
                 }catch (IOException e){
                       e.printStackTrace();
                       Toast.makeText(FileListActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                   }

                 }
                 else{
                File srcfile = new File(videopath);
                File destfile = new File(path+"/"+name);

                  try {
                      FileInputStream fileInputStream = new FileInputStream(srcfile);
                      FileOutputStream fileOutputStream = new FileOutputStream(destfile);
                     byte[] buffer = new byte[1024];
                     int length;
                     while((length= fileInputStream.read(buffer))>0){
                        fileOutputStream.write(buffer,0,length);
                     }
                     fileInputStream.close();
                     fileOutputStream.close();
                     filesandfolders.add(destfile);
                      recyclerView.setAdapter(new Adapter(FileListActivity.this, getIntent().getStringExtra("videoname"),getIntent().getStringExtra("videopath"),true,filesandfolders,code,onFileSelectedListener));

                      Toast.makeText(FileListActivity.this, "Copied!!!", Toast.LENGTH_SHORT).show();
                  }
                  catch (IOException e) {
                      e.printStackTrace();
                      Toast.makeText(FileListActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                  }

                 }


             }
         });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(FileListActivity.this);
                alertdialog.setTitle("Create New Folder");
                EditText editText = new EditText(FileListActivity.this);
                alertdialog.setView(editText);

                alertdialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filename = editText.getEditableText().toString();


                        File folder = new File(path+"/"+filename);

                        if(!folder.exists()){
                           if(folder.mkdirs()) {


                               filesandfolders.add(folder);

                               recyclerView.setAdapter(new Adapter(getApplicationContext(),filesandfolders,false,code,onFileSelectedListener));
                               Toast.makeText(FileListActivity.this, filename +" "+ " created", Toast.LENGTH_SHORT).show();
                           }
                        }
                        else{
                            Toast.makeText(FileListActivity.this, "Can't Create", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertdialog.show();
            }

        });

    }




    @Override
    public void OnFileLongClicked(File file, int i, VideoAdapter videoAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int i, ImageAdapter imageAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int i, DocumentAdapter documentAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int i, AudioAdapter AudioAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int i, archieveAdapter archieveAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int i, apkAdapter apkAdapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int position, Adapter adapter) {
        Log.d(TAG, "OnFileLongClicked: filelist entered with adapter"+adapter);
        Dialog optionDialog = new Dialog(this);
        optionDialog.setContentView(R.layout.option_dailog);
        optionDialog.setTitle("Select Options");
        ListView options = (ListView)optionDialog.findViewById(R.id.List);
        fileCustomAdapter customAdapter = new fileCustomAdapter();
        options.setAdapter(customAdapter);
        optionDialog.show();
        backpressed=false;

        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecteditem = adapterView.getItemAtPosition(i).toString();

                switch (selecteditem){

                    case "Details":
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FileListActivity.this);

                        Date lastModified = new Date(file.lastModified());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String formatter = simpleDateFormat.format(lastModified);
                        alertDialog.setTitle("Details");
                        alertDialog.setMessage(Html.fromHtml
                                        ("<b>File Name:</b><br>" + file.getName() + "<br>"
                                                + "<b>Size:</b><br>" + Formatter.formatShortFileSize(FileListActivity.this, file.length()) + "<br>"
                                                + "<b>Path:</b><br>" + file.getAbsolutePath() + "<br>"
                                                + "<b>Last Modified:</b><br>" + formatter))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Perfotrm action when "Yes" button is clicked

                                    }
                                });
                        alertDialog.show();
                        break;
                    case "Rename":
                        AlertDialog.Builder renameDialog = new AlertDialog.Builder(FileListActivity.this);
                        renameDialog.setTitle("Rename:");
                        EditText editText = new EditText(FileListActivity.this);
                        renameDialog.setView(editText);
                        renameDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String newname = editText.getEditableText().toString();
                                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                                File current = new File(file.getAbsolutePath());
                                File destination = new File(file.getAbsolutePath().replace(file.getName(),newname)+extension);
                                if(current.renameTo(destination)){
                                    filesandfolders.set(position,destination);
                                    adapter.notifyItemChanged(position);
                                    Toast.makeText(FileListActivity.this, "Renamed", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(FileListActivity.this, "Couldn't Rename", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        renameDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                optionDialog.cancel();
                            }
                        });
                        renameDialog.show();
                        break;
                    case "Move":
                        Intent move = new Intent(FileListActivity.this,FileListActivity.class);

                        move.putExtra("videopath",file.getAbsolutePath());
                        move.putExtra("videoname",file.getName());


                        move.putExtra("visibility",true);
                        move.putExtra("code","file");
                        move.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        move.putExtra("path", Environment.getExternalStorageDirectory().getAbsolutePath());
                        startActivity(move);
                        break;
                    case "Copy":
                        Intent copy = new Intent(FileListActivity.this,FileListActivity.class);
                        copy.putExtra("videopath",file.getAbsolutePath());
                        copy.putExtra("videoname",file.getName());
                        copy.putExtra("code","file");
                        copy.putExtra("copyvisibility",true);
                        copy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        copy.putExtra("path",Environment.getExternalStorageDirectory().getAbsolutePath());
                        startActivity(copy);
                        break;
                    case "Delete":
                        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(FileListActivity.this);
                        deleteDialog.setTitle("Delete"+" "+file.getName()+"?");
                        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteDirectory(file);
                                filesandfolders.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(FileListActivity.this, file.getName()+" "+"Deleted", Toast.LENGTH_SHORT).show();

                            }
                        });
                        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                optionDialog.cancel();
                            }
                        });
                        deleteDialog.show();
                        break;
                    case "Share":
                        if(!file.isDirectory()) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setType("image/jpeg");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
                            startActivity(Intent.createChooser(intent, "Share" + " " + file.getName()));
                        }
                        else{
                            Toast.makeText(FileListActivity.this, "Can't Share", Toast.LENGTH_SHORT).show();
                            optionDialog.cancel();
                        }

                        break;
                    case "Encrypt":
                        if(!file.isDirectory()){
                        if(!(file.getName().endsWith(".enc"))) {
                            AlertDialog.Builder encryptDiaolog = new AlertDialog.Builder(FileListActivity.this);
                            encryptDiaolog.setTitle("Encrypt: Set Password");

                            EditText edit1Text = new EditText(FileListActivity.this);
                            encryptDiaolog.setView(edit1Text);
                            encryptDiaolog.setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        String password = edit1Text.getEditableText().toString();

                                        if(password.isEmpty()){
                                            optionDialog.cancel();
                                            Toast.makeText(FileListActivity.this, "Password Required!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            File encryptfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "EncryptedFiles");

                                            if (!encryptfile.exists()) {
                                                encryptfile.mkdirs();

                                            }
                                            if (new File(encryptfile.getAbsolutePath() + "/" + file.getName() + ".enc").exists()) {

                                                Toast.makeText(FileListActivity.this, "Encrypted File Alread Exist!", Toast.LENGTH_SHORT).show();
                                                optionDialog.cancel();
                                            }
                                            int keylenght = 128;

                                            byte[] keybyte = Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16);
                                            Key secretkey = new SecretKeySpec(keybyte, 0, keylenght / 8, "AES");

                                            Cipher cipher = Cipher.getInstance("AES");
                                            cipher.init(Cipher.ENCRYPT_MODE, secretkey);

                                            FileInputStream fileInputStream = new FileInputStream(file);

                                            Log.d(TAG, "onClick: encrypt file directory created!!!");

                                            byte[] inputBytes = new byte[(int) file.length()];
                                            fileInputStream.read(inputBytes);

                                            byte[] outputBytes = cipher.doFinal(inputBytes);
                                            FileOutputStream fileOutputStream = new FileOutputStream(encryptfile.getAbsolutePath() + "/" + file.getName() + ".enc");

                                            fileOutputStream.write(outputBytes);

                                            fileInputStream.close();
                                            fileOutputStream.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(FileListActivity.this, "Can't encrypt", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            encryptDiaolog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    optionDialog.cancel();
                                }
                            });
                            encryptDiaolog.show();
                        }
                        else{
                            Toast.makeText(FileListActivity.this, "Already Encrypted!", Toast.LENGTH_SHORT).show();
                            optionDialog.cancel(); 
                        }}
                        else{
                            Toast.makeText(FileListActivity.this, "Select A Specific File To Encrypt", Toast.LENGTH_SHORT).show();
                            optionDialog.cancel();
                        }
                        break;
                    case "Decrypt":
                        if(file.getName().endsWith(".enc")) {
                            AlertDialog.Builder decryptDiaolog = new AlertDialog.Builder(FileListActivity.this);
                            decryptDiaolog.setTitle("Decrypt: Enter Password ");

                            EditText edit2Text = new EditText(FileListActivity.this);
                            decryptDiaolog.setView(edit2Text);
                            decryptDiaolog.setPositiveButton("Decrypt", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        String password = edit2Text.getEditableText().toString();

                                        File decryptfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DecryptedFiles");
                                        if (!decryptfile.exists()) {
                                            decryptfile.mkdirs();

                                            adapter.notifyDataSetChanged();

                                        }

                                        int keylenght = 128;
                                        byte[] keybyte = Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), 16);
                                        Key secretkey = new SecretKeySpec(keybyte, 0, keylenght / 8, "AES");

                                        Cipher cipher = Cipher.getInstance("AES");
                                        cipher.init(Cipher.DECRYPT_MODE, secretkey);

                                        FileInputStream fileInputStream = new FileInputStream(file);

                                        byte[] inputBytes = new byte[(int) file.length()];
                                        fileInputStream.read(inputBytes);

                                        byte[] outputBytes = cipher.doFinal(inputBytes);
                                        File filede = new File(decryptfile.getAbsolutePath() + "/" + file.getName().substring(0, file.getName().length() - 4));
                                        FileOutputStream fileOutputStream = new FileOutputStream(filede);
                                        Log.d(TAG, "onClick: " + file.getName().substring(0, file.getName().length() - 4));

                                        fileOutputStream.write(outputBytes);


                                        fileInputStream.close();
                                        fileOutputStream.close();
                                        Intent intent = new Intent(FileListActivity.this, FileListActivity.class);
                                        String path = decryptfile.getAbsolutePath();
                                        intent.putExtra("decrypt", true);
                                        intent.putExtra("visibility", false);
                                        intent.putExtra("code", "null");
                                        intent.putExtra("path", path);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(FileListActivity.this, "Can't Decrypt", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            decryptDiaolog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    optionDialog.cancel();
                                }
                            });
                            decryptDiaolog.show();
                        }
                        else{
                            Toast.makeText(FileListActivity.this, "Can't Decrypt, Not An Encrypted File", Toast.LENGTH_SHORT).show();
                            optionDialog.cancel();
                        }
                        break;
                }

            }
        });
    }


    class fileCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myview = getLayoutInflater().inflate(R.layout.option_layout,null);
            TextView txtOptions = myview.findViewById(R.id.txtOption);
            ImageView imgOptions = myview.findViewById(R.id.imgOption);
            txtOptions.setText(items[i]);
            if(items[i].equals("Details")){
                imgOptions.setImageResource(R.drawable.ic_baseline_info_24);
            }

            else if(items[i].equals("Rename")){
                imgOptions.setImageResource(R.drawable.ic_baseline_drive_file_rename_outline_24);
            }
            else if(items[i].equals("Move")){
                imgOptions.setImageResource(R.drawable.ic_baseline_drive_file_move_24);
            }
            else if(items[i].equals("Copy")){
                imgOptions.setImageResource(R.drawable.ic_baseline_content_copy_24);
            }
            else if(items[i].equals("Delete")){
                imgOptions.setImageResource(R.drawable.ic_baseline_delete_24);
            }
            else if(items[i].equals("Share")){
                imgOptions.setImageResource(R.drawable.ic_baseline_share_24);
            }
            else if(items[i].equals("Encrypt")){
                imgOptions.setImageResource(R.drawable.ic_baseline_key_24);
            }
            else if(items[i].equals("Decrypt")){
                imgOptions.setImageResource(R.drawable.ic_baseline_key_off_24);
            }

            return myview;
        }
    }

    @Override
    public void onBackPressed() {
       if(secondtime){
           Intent intent = new Intent(this, MainActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);
       }
        else if(decrypt){
            Log.d(TAG, "onBackPressed: prrreesssed");
            Intent intent = new Intent(this, FileListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            String path = Environment.getExternalStorageDirectory().getPath();
            intent.putExtra("secondtime",true);
            intent.putExtra("visibility",false);
            intent.putExtra("code","null");
            intent.putExtra("path",path);
            startActivity(intent);
        }

        else if(code.contains("video")){
            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, VideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }

        }
        else if(code.equals("audio")){

            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, AudioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }
        }
        else if(code.equals("doc")){

            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, pdf_list_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }
        }
        else if(code.equals("apk")){

            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, ApkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }
        }
        else if(code.equals("archive")){

            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, archiveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }
        }
        else if(code.equals("images")){

            if (backpressed) {
                Log.d(TAG, "onBackPressed: "+backpressed);
                Intent intent = new Intent(this, ImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }

        }else if(code.equals("file")) {
            if(backpressed){
                Log.d(TAG, "onBackPressed: " + backpressed);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Log.d(TAG, "onBackPressed: "+backpressed);
                super.onBackPressed();
            }

        }
        else if(code.equals("null")){
            Log.d(TAG, "onBackPressed: "+backpressed);
            super.onBackPressed();
        }






    }
    public void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);

                }
            }
        }
        directory.delete();
    }

    }


