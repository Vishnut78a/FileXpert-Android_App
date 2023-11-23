package com.example.filemanager;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import javax.crypto.spec.SecretKeySpec;

public class ApkActivity extends AppCompatActivity implements OnFileSelectedListener {
RecyclerView recyclerView;
ArrayList<File> arrayList;
String[] items={"Details","Rename","Move","Copy","Delete","Share","Encrypt"};
boolean backpressed=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apklist);
        recyclerView =findViewById(R.id.recyclerView30);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new apkAdapter(getApplicationContext(),apkarrayList(),this));

    }

    public ArrayList<File> apkarrayList() {
      arrayList = new ArrayList<>();
        Uri apkUri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.DISPLAY_NAME};

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] selectionArgs = new String[]{"application/vnd.android.package-archive"};

        Cursor cursor = getContentResolver().query(
                apkUri,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String apkFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                Uri uri = Uri.parse("file://" + apkFilePath);
                File file = new File(apkFilePath);

                arrayList.add(file);
            } while (cursor.moveToNext());
            cursor.close();


        }
        return arrayList;}

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
    public void OnFileLongClicked(File file, int position, apkAdapter apkAdapter) {
        Dialog optionDialog = new Dialog(this);
        optionDialog.setContentView(R.layout.option_dailog);
        optionDialog.setTitle("Select Options");
        ListView options = (ListView)optionDialog.findViewById(R.id.List);
        apkCustomAdapter customAdapter = new apkCustomAdapter();
        options.setAdapter(customAdapter);
        optionDialog.show();
        backpressed=false;

        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecteditem = adapterView.getItemAtPosition(i).toString();
                 switch (selecteditem){
                     case "Details":
                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(ApkActivity.this);

                         Date lastModified = new Date(file.lastModified());
                         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                         String formatter = simpleDateFormat.format(lastModified);
                         alertDialog.setTitle("Details");
                         alertDialog.setMessage(Html.fromHtml
                                         ("<b>File Name:</b><br>" + file.getName() + "<br>"
                                                 + "<b>Size:</b><br>" + Formatter.formatShortFileSize(ApkActivity.this, file.length()) + "<br>"
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
                         AlertDialog.Builder renameDialog = new AlertDialog.Builder(ApkActivity.this);
                         renameDialog.setTitle("Rename:");
                         EditText editText = new EditText(ApkActivity.this);
                         renameDialog.setView(editText);
                         renameDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                                 String newname = editText.getEditableText().toString();
                                 String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                                 File current = new File(file.getAbsolutePath());
                                 File destination = new File(file.getAbsolutePath().replace(file.getName(),newname)+extension);
                                 if(current.renameTo(destination)){
                                     arrayList.set(position,destination);
                                     apkAdapter.notifyItemChanged(position);
                                     Toast.makeText(ApkActivity.this, "Renamed", Toast.LENGTH_SHORT).show();

                                 }
                                 else{
                                     Toast.makeText(ApkActivity.this, "Couldn't Rename", Toast.LENGTH_SHORT).show();
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
                         Intent move = new Intent(ApkActivity.this,FileListActivity.class);
                         move.putExtra("videopath",file.getAbsolutePath());
                         move.putExtra("videoname",file.getName());
                         move.putExtra("code","apk");

                         move.putExtra("visibility",true);
                         move.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         move.putExtra("path", Environment.getExternalStorageDirectory().getAbsolutePath());
                         startActivity(move);
                         break;
                     case "Copy":
                         Intent copy = new Intent(ApkActivity.this,FileListActivity.class);
                         copy.putExtra("videopath",file.getAbsolutePath());
                         copy.putExtra("videoname",file.getName());
                         copy.putExtra("code","apk");

                         copy.putExtra("copyvisibility",true);
                         copy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         copy.putExtra("path",Environment.getExternalStorageDirectory().getAbsolutePath());
                         startActivity(copy);
                         break;
                     case "Delete":
                         AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ApkActivity.this);
                         deleteDialog.setTitle("Delete"+" "+file.getName()+"?");
                         deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 file.delete();
                                 arrayList.remove(position);
                                 apkAdapter.notifyDataSetChanged();
                                 Toast.makeText(ApkActivity.this, file.getName()+" "+"Deleted", Toast.LENGTH_SHORT).show();

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
                         intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
                         startActivity(Intent.createChooser(intent,"Share"+" "+file.getName()));
                         break;
                     case "Encrypt":
                         AlertDialog.Builder encryptDiaolog = new AlertDialog.Builder(ApkActivity.this);
                         encryptDiaolog.setTitle("Encrypt: Set Password");

                         EditText edit1Text = new EditText(ApkActivity.this);
                         encryptDiaolog.setView(edit1Text);
                         encryptDiaolog.setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 try {
                                     String password = edit1Text.getEditableText().toString();

                                     if(password.isEmpty()){
                                         optionDialog.cancel();
                                         Toast.makeText(ApkActivity.this, "Password Required!", Toast.LENGTH_SHORT).show();
                                     }else {
                                         File encryptfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "EncryptedFiles");

                                         if (!encryptfile.exists()) {
                                             encryptfile.mkdirs();

                                         }
                                         if (new File(encryptfile.getAbsolutePath() + "/" + file.getName() + ".enc").exists()) {

                                             Toast.makeText(ApkActivity.this, "Encrypted File Alread Exist!", Toast.LENGTH_SHORT).show();
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
                                     Toast.makeText(ApkActivity.this, "Can't encrypt", Toast.LENGTH_SHORT).show();
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

    @Override
    public void OnFileLongClicked(File file, int i, Adapter adapter) {

    }

    class apkCustomAdapter extends BaseAdapter {

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
