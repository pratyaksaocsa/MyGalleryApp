package edu.ftiuksw.mygallery;

import static android.os.Build.VERSION.SDK_INT;

import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadAlbum extends AsyncTask<String, Void, Void> {

    private final MainActivity mainActivity;
    private final ArrayList<HashMap<String, String>> albumList;

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
    protected Void doInBackground(String... strings) {
        Uri uriExternal;
        Uri uriInternal;
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            uriExternal = android.provider.MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            uriInternal = android.provider.MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
        } else {
            uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        String[] projection = { MediaStore.MediaColumns.DATA,
               MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        String selection = null;
        String sortOrder = null;
        Cursor cursorExternal = mainActivity.getContentResolver().query(uriExternal, projection, selection,
                null, sortOrder);
        Cursor cursorInternal = mainActivity.getContentResolver().query(uriInternal, projection, selection,
                null, sortOrder);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});

        while (cursor.moveToNext()) {
            if(albumList.size() < 10) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                albumList.add(Function.mappingInbox(album, path, timestamp));
            } else break;
        }
        cursor.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
        AlbumAdapter adapter = new AlbumAdapter(mainActivity, albumList);
        mainActivity.setAdapter(adapter);
    }
}
