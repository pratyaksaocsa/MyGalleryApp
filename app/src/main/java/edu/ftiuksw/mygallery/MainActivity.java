package edu.ftiuksw.mygallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int READ_WRITE_EXTERNAL_STORAGE = 1;
    private GridView galleryGridView;
    private final ArrayList<HashMap<String, String>> albumList = new ArrayList<>();
    LoadAlbum loadAlbumTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galleryGridView = findViewById(R.id.galleryGridView);

        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            loadAlbumTask = new LoadAlbum(this, albumList);
                            loadAlbumTask.execute();
                        } else {
                            Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    activityResultLaunch.launch(intent);
                } else {
                    loadAlbumTask = new LoadAlbum(this, albumList);
                    loadAlbumTask.execute();
                }
            } catch (Exception e) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    activityResultLaunch.launch(intent);
                }
            }
        } else {
            //below android 11
            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if(!Function.hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, READ_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_WRITE_EXTERNAL_STORAGE) {
                int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED) {
                    loadAlbumTask = new LoadAlbum(this, albumList);
                    loadAlbumTask.execute();
                } /*else {
                    Toast.makeText(this, "You must give permission", Toast.LENGTH_LONG).show();
                }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, READ_WRITE_EXTERNAL_STORAGE);
        }else{
            loadAlbumTask = new LoadAlbum(this, albumList);
            loadAlbumTask.execute();
        }*/
    }

    public void setAdapter(AlbumAdapter adapter) {
        galleryGridView.setAdapter(adapter);
        galleryGridView.setOnItemClickListener((adapterView, view, i, l) -> {

        });
    }
}
