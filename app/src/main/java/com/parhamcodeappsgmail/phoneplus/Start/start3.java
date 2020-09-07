package com.parhamcodeappsgmail.phoneplus.Start;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.parhamcodeappsgmail.phoneplus.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class start3 extends Fragment {

ImageButton next;

    public start3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View view =inflater.inflate(R.layout.fragment_start3, container, false);
         next=view.findViewById(R.id.next3);
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(Build.VERSION.SDK_INT < 23){
                     Objects.requireNonNull(getActivity()).finish();
                 }
                 else {
                     ((start)Objects.requireNonNull(getActivity())).pageview();
                     //  Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(startfrag1.this).commit();
                 }

             }
         });
        return view;
    }

}
