package edu.ftiuksw.mygallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AlbumAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    ImageView galleryImage;

    public AlbumAdapter(Activity a, ArrayList< HashMap <String, String>> d) {
        activity = a;
        data = d;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.album_row, parent, false);
            galleryImage = convertView.findViewById(R.id.galleryImage);
        }
        HashMap<String, String> images;
        images = data.get(position);
        try{
            Glide.with(activity)
                    .load(new File(images.get(Function.KEY_PATH)))
                    .into(galleryImage);
        }catch (Exception x) {
            x.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
