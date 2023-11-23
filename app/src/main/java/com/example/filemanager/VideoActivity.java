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
import android.widget.SearchView;
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
import java.nio.file.Files;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class VideoActivity extends AppCompatActivity implements OnFileSelectedListener {
    RecyclerView recyclerView4;
    SearchView searchView;
    ArrayList<File> videoModelArrayList ;
    boolean backpressed=true;
    String[] items={"Details","Rename","Move","Copy","Delete","Share","Encrypt"};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);


        recyclerView4 =findViewById(R.id.recyclerView4);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this));
          VideoAdapter videoAdapter= new VideoAdapter(getApplicationContext(),arrayList(),this);
        recyclerView4.setAdapter(videoAdapter);



    }








    public ArrayList<File> arrayList(){
        VideoAdapter videoAdapter = new VideoAdapter();
        videoModelArrayList =  new ArrayList<>();
        String projection[] = { MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if(cursor!=null){

            int dataid = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);


            while(cursor.moveToNext()){


                String data = cursor.getString(dataid);
                File file = new File(data);


                videoModelArrayList.add(file);
            }
            cursor.close();
        }

    return videoModelArrayList;}






    @Override
    public void OnFileLongClicked(File file,int position,VideoAdapter videoAdapter) {
        Dialog optionDialog = new Dialog(this);
        optionDialog.setContentView(R.layout.option_dailog);
        optionDialog.setTitle("Select Options");
        ListView options = (ListView)optionDialog.findViewById(R.id.List);
         CustomAdapter customAdapter = new CustomAdapter();
         options.setAdapter(customAdapter);
         optionDialog.show();
         String file1 =file.getAbsolutePath();
         String file2 =file.getName();
         backpressed=false;

         options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 String selecteditem = adapterView.getItemAtPosition(i).toString();

                 switch (selecteditem){
                 case "Details":
                     AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideoActivity.this);

                     Date lastModified = new Date(file.lastModified());
                     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                     String formatter = simpleDateFormat.format(lastModified);
                     alertDialog.setTitle("Details");
                     alertDialog.setMessage(Html.fromHtml
                                     ("<b>File Name:</b><br>" + file.getName() + "<br>"
                                     + "<b>Size:</b><br>" + Formatter.formatShortFileSize(VideoActivity.this, file.length()) + "<br>"
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
                         AlertDialog.Builder renameDialog = new AlertDialog.Builder(VideoActivity.this);
                         renameDialog.setTitle("Rename:");
                         EditText editText = new EditText(VideoActivity.this);
                         renameDialog.setView(editText);
                         renameDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                            String newname = editText.getEditableText().toString();
                            String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                            File current = new File(file.getAbsolutePath());
                            File destination = new File(file.getAbsolutePath().replace(file.getName(),newname)+extension);
                            if(current.renameTo(destination)){
                                videoModelArrayList.set(position,destination);
                                 videoAdapter.notifyItemChanged(position);
                                Toast.makeText(VideoActivity.this, "Renamed", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(VideoActivity.this, "Couldn't Rename", Toast.LENGTH_SHORT).show();
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
                         Intent move = new Intent(VideoActivity.this,FileListActivity.class);
                         Log.d(TAG, "onItemClick: "+file1+" "+file2);
                        move.putExtra("videopath",file1);
                        move.putExtra("videoname",file2);

                         Log.d(TAG, file.getAbsolutePath()+" value passed in intent"+move.getStringExtra("vp"));
                         Log.d(TAG, file.getName()+"value passed in intent"+move.getStringExtra("vn"));
                         move.putExtra("visibility",true);
                         move.putExtra("code","video");
                         Log.d(TAG, "sending from video activity"+move.getBooleanExtra("visibility",false));
                          move.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          move.putExtra("path", Environment.getExternalStorageDirectory().getAbsolutePath());
                          startActivity(move);

                          break;
                     case "Copy":
                         Intent copy = new Intent(VideoActivity.this,FileListActivity.class);
                         copy.putExtra("videopath",file1);
                         copy.putExtra("videoname",file2);
                         copy.putExtra("code","video");
                         copy.putExtra("copyvisibility",true);
                         copy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         copy.putExtra("path",Environment.getExternalStorageDirectory().getAbsolutePath());
                         startActivity(copy);

                          break;
                     case "Delete":
                     AlertDialog.Builder deleteDialog = new AlertDialog.Builder(VideoActivity.this);
                       deleteDialog.setTitle("Delete"+" "+file.getName()+"?");
                       deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               file.delete();
                               videoModelArrayList.remove(position);
                               videoAdapter.notifyDataSetChanged();
                               Toast.makeText(VideoActivity.this, file.getName()+" "+"Deleted", Toast.LENGTH_SHORT).show();

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
                         AlertDialog.Builder encryptDiaolog = new AlertDialog.Builder(VideoActivity.this);
                         encryptDiaolog.setTitle("Encrypt: Set Password");

                         EditText edit1Text = new EditText(VideoActivity.this);
                         encryptDiaolog.setView(edit1Text);
                         encryptDiaolog.setPositiveButton("Encrypt", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                               try {
                                   String password = edit1Text.getEditableText().toString();

                                   if(password.isEmpty()){
                                       optionDialog.cancel();
                                       Toast.makeText(VideoActivity.this, "Password Required!", Toast.LENGTH_SHORT).show();
                                   }else {

                                       File encryptfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "EncryptedFiles");

                                       if (!encryptfile.exists()) {
                                           encryptfile.mkdirs();

                                       }
                                       if (new File(encryptfile.getAbsolutePath() + "/" + file.getName() + ".enc").exists()) {

                                           Toast.makeText(VideoActivity.this, "Encrypted File Alread Exist!", Toast.LENGTH_SHORT).show();
                                           optionDialog.cancel();
                                       }
                                       int iterations = 1000;
                                       int keylenght = 128;
                                       PBEKeySpec spec = new PBEKeySpec(password.toCharArray());

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
                                   Toast.makeText(VideoActivity.this, "Can't encrypt", Toast.LENGTH_SHORT).show();
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
    public void OnFileLongClicked(File file, int i, ImageAdapter imageAdapter) {
    }

    class CustomAdapter extends BaseAdapter{

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
