package com.parhamcodeappsgmail.phoneplus.Tools;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.R;

public class Snacktoast {

    public static void inform(Activity mView, String string, int text_color) {
        Snackbar snack = Snackbar.make(mView.findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(mView.getResources().getColor(text_color));

        //change background color too ?
        view.setBackgroundColor(mView.getResources().getColor(R.color.snackback));

        snack.show();
    }
}
