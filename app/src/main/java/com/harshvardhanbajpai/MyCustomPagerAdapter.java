package com.harshvardhanbajpai;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;


public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<GetSetImageSilders> images;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, ArrayList<GetSetImageSilders> images) {
        this.context = context;
        this.images = images;
        try {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_imageadapter_shop, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        Picasso.with(MyCustomPagerAdapter.this.context).load(images.get(position).getImage()).fit().into(imageView);
        container.addView(itemView);

        //listening to image click
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, SubCategories.class);
////                intent.putExtra("catId", images.get(position).getId());
////                intent.putExtra("catName", images.get(position).getCatName());
////                intent.putExtra("nextpage","");
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                context.startActivity(intent);
////                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
//            }
//        });

        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}
