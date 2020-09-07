package com.parhamcodeappsgmail.phoneplus.Start;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parhamcodeappsgmail.phoneplus.R;

import java.util.Objects;


public class start2 extends Fragment {

    Button button;

    public start2() {
        // Required empty public constructor
    }

    public static start2 newInstance(String param1, String param2) {
        start2 fragment = new start2();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_start2, container, false);
         button=view.findViewById(R.id.giveper);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Objects.requireNonNull(getActivity()).finish();



             }
         });
        return view;
    }




}
