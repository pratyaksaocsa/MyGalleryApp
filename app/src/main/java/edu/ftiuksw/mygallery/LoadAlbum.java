package edu.ftiuksw.mygallery;

import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LoadAlbum extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private ArrayList<HashMap<String, String>> albumList;

    public LoadAlbum(MainActivity mainActivity, ArrayList<HashMap<String, String>> album) {
        this.mainActivity = mainActivity;
        this.albumList = album;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        albumList.clear();
    }

    @Override
    protected String doInBackground(String... strings) {
        String ret = "";

        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
               MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        String selection = null;
        Cursor cursorExternal = mainActivity.getContentResolver().query(uriExternal, projection, selection,
                null, null);
        Cursor cursorInternal = mainActivity.getContentResolver().query(uriInternal, projection, selection,
                null, null);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});

        while (cursor.moveToNext()) {

            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));

            albumList.add(Function.mappingInbox(album, path, timestamp));
        }
        cursor.close();
        return ret;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        AlbumAdapter adapter = new AlbumAdapter(mainActivity, albumList);
        mainActivity.setAdapter(adapter);
    }
}
