package com.harshvardhanbajpai;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomGalleryAdapter extends BaseAdapter {
    private Context context;
    ArrayList<GetSetImageGallery> images;

    public CustomGalleryAdapter(Context c, ArrayList<GetSetImageGallery> images) {
        context = c;
        this.images = images;
    }

    // returns the number of images
    public int getCount() {
        return images.size();
    }

    // returns the ID of an item
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {

        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);
        Picasso.with(CustomGalleryAdapter.this.context).load(images.get(position).getImage()).fit().into(imageView);
        //imageView.setImageResource(Integer.parseInt(images.get(position).getImage())); // set image in ImageView
       imageView.setLayoutParams(new Gallery.LayoutParams(250, 250)); // set ImageView param

        return imageView;
    }
}
