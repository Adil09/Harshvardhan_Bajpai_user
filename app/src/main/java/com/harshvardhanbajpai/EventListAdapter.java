package com.harshvardhanbajpai;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends ArrayAdapter<Event> {
Context context;
    public EventListAdapter(Context context, List<Event> feeds) {
        super(context,0,feeds);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Event feed = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_feed_item_row, parent, false);
        }

        ImageView eventName = (ImageView) convertView.findViewById(R.id.event_list_dataText);
        TextView eventDay = (TextView) convertView.findViewById(R.id.event_list_dayTextView);
        TextView eventMonth = (TextView) convertView.findViewById(R.id.event_list_monthTextView);

        SimpleDateFormat simple_month = new SimpleDateFormat("MMM", Locale.US);
        SimpleDateFormat simple_day = new SimpleDateFormat("dd", Locale.US);
       // eventName.setText((String) feed.getData());
        Picasso.with(EventListAdapter.this.getContext()).load("http://mla-admin.sitsald.co.in/"+feed.getData()).into(eventName);

        eventMonth.setText(simple_month.format(feed.getTimeInMillis()));
        eventDay.setText(simple_day.format(feed.getTimeInMillis()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TempListViewClickedValue = "http://mla-admin.sitsald.co.in/"+feed.getData();

                Intent intent = new Intent(getContext(), ViewFullImage.class);

                // Sending value to another activity using intent.
                intent.putExtra("image", TempListViewClickedValue);
                Log.d("check_image",TempListViewClickedValue);
               getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
