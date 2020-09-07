package com.parhamcodeappsgmail.phoneplus.Tools;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parhamcodeappsgmail.phoneplus.R;

public class Message {
    public static void message(Context context, String message, int lenght) {
        if (lenght==1){
            Toast.makeText( context, message, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }


    }
}

