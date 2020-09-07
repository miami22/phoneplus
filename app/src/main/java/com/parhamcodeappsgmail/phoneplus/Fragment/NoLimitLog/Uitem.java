package com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.parhamcodeappsgmail.phoneplus.Contact;

public class Uitem {
    String name;
    String primdate;
    String date;
    String time;
    String dur;
    String type;
    String sim;
    String sec;
    int id;







    public Uitem(int id,String name,String primdate, String date, String time,String dur, String type,String sim,String sec){
        this.name=name;
        this.primdate=primdate;
        this.date =date;
        this.time =time;
        this.dur=dur;
        this.type=type;
        this.sim=sim;
        this.id=id;
        this.sec=sec;

    }
    public String getName(){
        return name;
    }
    public String getDate(){
        return  date;
    }
    public String getTime(){
        return  time;
    }
    public String getType(){
        return  type;
    }
    public String getSim(){
        return  sim;
    }
    public String getDur() { return dur; }

    public String getSec() {
        return sec;
    }

    public String getPrimdate() { return primdate; }

    public int getId(){
        return  id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    private static Contact fromRes(Context c, @DrawableRes int img, @StringRes int fn, @StringRes int ln) {
        return new Contact(ContextCompat.getDrawable(c, img), c.getString(fn), c.getString(ln));
    }



}
