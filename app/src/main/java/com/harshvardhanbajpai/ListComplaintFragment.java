package com.harshvardhanbajpai;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListComplaintFragment extends Fragment {

Button show_descrpiton;
    public ListComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_complaint, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        show_descrpiton=view.findViewById(R.id.show_descrpiton);


        show_descrpiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ListComplaintFragment.this.getContext());
                builder.setCancelable(true);
                builder.setTitle("Description");
                builder.setMessage("Today I would like to inform you about the recent incident where after heavy rain the entire road was broken and swept away.The residents of our area are in big trouble.We cannot use our vehicles whereas we need to walk a long distance To reach public transport.We did complain about it to the conceded officer but no action was taken.Kindly help us to get the road repaired and train police with disaster management situations." );
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });

                AlertDialog alert=builder.create();
                alert.show();
            }
        });
    }
}
