package com.parhamcodeappsgmail.phoneplus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class help extends Fragment {

    TextView text;
    public help() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_help, container, false);
         text=view.findViewById(R.id.txthelp);
         return view;
    }

}
