package com.harshvardhanbajpai;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewComplainFragment extends Fragment {


    public NewComplainFragment() {
        // Required empty public constructor
    }
    Spinner spi_create,spi_create1;
    String items[]={"Select Area ","Civil Lines","Civil Lines","Civil Lines","Civil Lines","Civil Lines","Civil Lines"};
    String items1[]={"Select Sub Area ","Civil Lines","Civil Lines","Civil Lines","Civil Lines","Civil Lines","Civil Lines"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_complain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        spi_create=view.findViewById(R.id.UR_spinner_area);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewComplainFragment.this.getContext(), R.layout.item_spinner, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spi_create.setAdapter(adapter);


        spi_create1=view.findViewById(R.id.UR_spinner_subarea);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(NewComplainFragment.this.getContext(), R.layout.item_spinner1, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spi_create1.setAdapter(adapter1);
    }
}
