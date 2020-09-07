package com.parhamcodeappsgmail.phoneplus.Start;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class startfrag1 extends Fragment {
    RadioGroup radioGroup;
    ImageButton next;
    TinyDB tinyDB;


    public startfrag1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            tinyDB=new TinyDB(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_startfrag1, container, false);
         radioGroup=view.findViewById(R.id.radio);
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 if(i==R.id.day)tinyDB.putBoolean("lighttheme",true);
                 else tinyDB.putBoolean("lighttheme",false);
             }
         });
         tinyDB.putBoolean("themechanged",true);
         next=view.findViewById(R.id.next);
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                     ((start)Objects.requireNonNull(getActivity())).pageview();
             }
         });
         return view;
    }

}
