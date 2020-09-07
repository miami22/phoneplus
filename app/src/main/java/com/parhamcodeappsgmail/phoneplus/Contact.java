package com.parhamcodeappsgmail.phoneplus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Contact {

    private Drawable mProfileImage;
    private String mFirstName;
    private String mLastName;

    private Contact() {
        //
    }

    public Contact(Drawable profileImage, String firstName, String lastName) {
        mProfileImage = profileImage;
        mFirstName = firstName;
        mLastName = lastName;
    }

    public Drawable getProfileImage() {
        return mProfileImage;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }



    public static final Comparator<Contact> COMPARATOR = new Comparator<Contact>() {
        @Override
        public int compare(Contact o1, Contact o2) {
            return o1.getFirstName().compareTo(o2.getFirstName());
        }
    };

    private static Contact fromRes(Context c, @DrawableRes int img, @StringRes int fn, @StringRes int ln) {
        return new Contact(ContextCompat.getDrawable(c, img), c.getString(fn), c.getString(ln));
    }
}
