package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ImageActivity extends AppCompatActivity implements OnFileSelectedListener{
RecyclerView recyclerView3;
ArrayList<File> listofallimages;
    boolean backpressed=true;

String[] items={"Details","Rename","Move","Copy","Delete","Share","Encrypt"};

    ArrayList<Uri> imagesuri = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageactivity);
        recyclerView3 = findViewById(R.id.recyclerView3);


        listofimages();
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this,3));
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(),listofimages(),this);
        recyclerView3.setAdapter(imageAdapter);

    }


//Fetching and adding files from mediastore to he arraylist:
    public ArrayList<File> listofimages(){
         listofallimages = new ArrayList<>();
        Uri uri;
        File file;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
        int id= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if(cursor!=null){

            while (cursor.moveToNext()){
                String path = cursor.getString(id);
                File file1 = new File(path);
                listofallimages.add(file1);
            }
            cursor.close();

        }


        return listofallimages;}
//Fetching Completed..




    @Override
    public void OnFileLongClicked(File file, int i, VideoAdapter videoAdapter) {

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
    public void OnFileLongClicked(File file, int i, Adapter adapter) {

    }

    @Override
    public void OnFileLongClicked(File file, int position, ImageAdapter imageAdapter) {
        Log.d(TAG, "OnFileLongClicked: entered");
        Dialog optionDialog = new Dialog(this);
        optionDialog.setContentView(R.layout.option_dailog);
        optionDialog.setTitle("Select Options");
        ListView options = (ListView)optionDialog.findViewById(R.id.List);
        ImageCustomAdapter icustomAdapter = new ImageCustomAdapter();
        options.setAdapter(icustomAdapter);
        optionDialog.show();
        backpressed=false;

        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecteditem = adapterView.getItemAtPosition(i).toString();

                switch (selecteditem) {

                    case "Details":
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImageActivity.this);
                        Date lastModified = new Date(file.lastModified());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String formatter = simpleDateFormat.format(lastModified);
                        alertDialog.setTitle("Details");
                        alertDialog.setMessage(Html.fromHtml
                                ("<b>File Name:</b><br>" + file.getName() + "<br>"
                                        + "<b>Size:</b><br>" + Formatter.formatShortFileSize(ImageActivity.this, file.length()) + "<br>"
                                        + "<b>Path:</b><br>" + file.getAbsolutePath() + "<br>"
                                        + "<b>Last Modified:</b><br>" + formatter))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        alertDialog.show();


                        break;
                    case "Rename":
                        AlertDialog.Builder renameDialog = new AlertDialog.Builder(ImageActivity.this);
                        renameDialog.setTitle("Rename:");
                        EditText editText = new EditText(ImageActivity.this);
                        renameDialog.setView(editText);
                        renameDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String newname = editText.getEditableText().toString();
                                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                                File current = new File(file.getAbsolutePath());
                                File destination = new File(file.getAbsolutePath().replace(file.getName(),newname)+extension);
                                if(current.renameTo(destination)){
                                    listofallimages.set(position,destination);
                                    imageAdapter.notifyItemChanged(position);
                                    Toast.makeText(ImageActivity.this, "Renamed", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(ImageActivity.this, "Couldn't Rename", Toast.LENGTH_SHORT).show();
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
                        Intent move = new Intent(ImageActivity.this,FileListActivity.class);
                        Log.d(TAG, "onItemClick: "+file.getAbsolutePath()+" "+file.getName());
                        move.putExtra("videopath",file.getAbsolutePath());
                        move.putExtra("videoname",file.getName());
                        move.putExtra("code","images");
                        Log.d(TAG, file.getAbsolutePath()+" value passed in intent"+move.getStringExtra("vp"));
                        Log.d(TAG, file.getName()+"value passed in intent"+move.getStringExtra("vn"));
                        move.putExtra("visibility",true);
                        Log.d(TAG, "sending from video activity"+move.getBooleanExtra("visibility",false));
                        move.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        move.putExtra("path", Environment.getExternalStorageDirectory().getAbsolutePath());
                        startActivity(move);

                        break;
                    case "Copy":
                        Intent copy = new Intent(ImageActivity.this,FileListActivity.class);
                        copy.putExtra("videopath",file.getAbsolutePath());
                        copy.putExtra("videoname",file.getName());
                        copy.putExtra("copyvisibility",true);
                        copy.putExtra("code","images");

                        copy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        copy.putExtra("path",Environment.getExternalStorageDirectory().getAbsolutePath());
                        startActivity(copy);

                        break;
                    case "Delete":
                        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ImageActivity.this);
                        deleteDialog.setTitle("Delete"+" "+file.getName()+"?");
                        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                file.delete();
                                listofallimages.remove(position);
                                imageAdapter.notifyDataSetChanged();
                                Toast.makeText(ImageActivity.this, file.getName()+" "+"Deleted", Toast.LENGTH_SHORT).show();

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
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(file.getAbsolutePath()));
                        startActivity(Intent.createChooser(intent,"Share"+" "+file.getName()));
                        break;
                    case "Encrypt":
                        AlertDialog.Builder encryptDiaolog = new AlertDialog.Builder(ImageActivity.this);
                        encryptDiaolog.setTitle("Encrypt: Set Password");

                        EditText edit1Text = new EditText(ImageActivity.this);
                        encryptDiaolog.setView(edit1Text);
                        encryptDiaolog.setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    String password = edit1Text.getEditableText().toString();

                                    if(password.isEmpty()){
                                        optionDialog.cancel();
                                        Toast.makeText(ImageActivity.this, "Password Required!", Toast.LENGTH_SHORT).show();
                                    }else {

                                        File encryptfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "EncryptedFiles");

                                        if (!encryptfile.exists()) {
                                            encryptfile.mkdirs();

                                        }
                                        if (new File(encryptfile.getAbsolutePath() + "/" + file.getName() + ".enc").exists()) {

                                            Toast.makeText(ImageActivity.this, "Encrypted File Alread Exist!", Toast.LENGTH_SHORT).show();
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

                                }catch(Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(ImageActivity.this, "Can't encrypt", Toast.LENGTH_SHORT).show();
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

                        break;
                }

            }
        });


    }
    class ImageCustomAdapter extends BaseAdapter {

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
        if (backpressed) {
            Log.d(TAG, "onBackPressed: "+backpressed);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } else {
            Log.d(TAG, "onBackPressed: "+backpressed);
            super.onBackPressed();
        }
    }
}
