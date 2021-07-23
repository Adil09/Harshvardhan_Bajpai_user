package com.harshvardhanbajpai.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harshvardhanbajpai.FilterArea;
import com.harshvardhanbajpai.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Area_adapter extends RecyclerView.Adapter<Area_adapter.Dataholder>{

    Context context;
    private int item;
    String student_img_url,subdomain, user_id, company_id;
    ArrayList<String> selected_id= new ArrayList<>();
    ArrayList<FilterArea> cartcateory;


    public Area_adapter(){

    }

    public Area_adapter(Context context, ArrayList<FilterArea> mArrayList) {
        this.cartcateory = mArrayList;
        this.company_id = company_id;


    }

    public Area_adapter(Context context, int item, ArrayList<FilterArea> cartcateory) {
        this.context = context;
        this.item = item;
        this.cartcateory = cartcateory;
        this.company_id = company_id;

    }

    public ArrayList<String> getlist(){
        return selected_id;
    }

    @NonNull
    @Override
    public Area_adapter.Dataholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        context = viewGroup.getContext();
        View view = layoutInflater.inflate(R.layout.filtertext_ui, viewGroup, false);

        return new Dataholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Area_adapter.Dataholder dataholder, final int i) {


        try {
            dataholder.fullnametv.setText(cartcateory.get(i).getYear());
            if (cartcateory.get(i).isIs_select()){
                dataholder.rootlay.setBackground(ContextCompat.getDrawable(context, R.drawable.selectedfilter));
                dataholder.fullnametv.setTypeface(null, Typeface.BOLD);
            }else {
                dataholder.rootlay.setBackground(ContextCompat.getDrawable(context, R.drawable.whitebackroundnorder));
                dataholder.fullnametv.setTypeface(null, Typeface.NORMAL);
            }
        } catch (Exception e) {        }



        dataholder.rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*if (!selected_id.contains(cartcateory.get(i).getAction_id())){
                    selected_id.add(cartcateory.get(i).getAction_id());
                    dataholder.rootlay.setBackground(ContextCompat.getDrawable(context, R.drawable.selectedfilter));
                    dataholder.fullnametv.setTypeface(null, Typeface.BOLD);

                }else {
                    selected_id.remove(cartcateory.get(i).getAction_id());
                    dataholder.rootlay.setBackground(ContextCompat.getDrawable(context, R.drawable.whitebackroundnorder));
                    dataholder.fullnametv.setTypeface(null, Typeface.NORMAL);
                }*/

                if (cartcateory.get(i).isIs_select()){

                    cartcateory.get(i).setIs_select(false);
                }else {
                    cartcateory.get(i).setIs_select(true);
                }
                notifyDataSetChanged();

            }
        });
       /*
        try {
            dataholder.noticeheadword.setText(cartcateory.get(i).getSubject_name().trim().toUpperCase().substring(0,1));
        } catch (Exception e) {
            e.printStackTrace();
        }*/


       /* dataholder.attachmentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //
                Intent intent= new Intent(context, ElearninglistparentActivity.class);
                intent.putExtra("id", cartcateory.get(i).getId());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_in, R.anim.side_out);


            }
        });*/

    }



    @Override
    public int getItemCount() {
        return cartcateory.size();
    }

    public class Dataholder extends RecyclerView.ViewHolder {
        TextView fullnametv;
        LinearLayout rootlay;


        public Dataholder(View itemView) {
            super(itemView);
            fullnametv = itemView.findViewById(R.id.fullnametv);
            rootlay = itemView.findViewById(R.id.rootlay);



        }
    }


}
