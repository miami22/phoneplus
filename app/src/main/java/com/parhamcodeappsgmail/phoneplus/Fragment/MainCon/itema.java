package com.parhamcodeappsgmail.phoneplus.Fragment.MainCon;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.parhamcodeappsgmail.phoneplus.Contact;

import java.util.Comparator;

public class itema {
    public  int id;
    String name;
    String mnumber;
    String number2;
    String number3;
    String address;
    String email;
    String insta;
    String teleg;
    String whatsup;
    String avatar;
    String info;
    String date;
    String datetitle;


    int pos;


    public itema(int id,String name, String mnumber, String number2, String number3, String address,
                 String email, String insta, String teleg, String whatsup, String avatar,String info,String date,String datetitle){
        this.id=id;
        this.mnumber =mnumber;
        this.name =name;
        this.number2=number2;
        this.number3=number3;
        this.address=address;
        this.email=email;
        this.insta=insta;
        this.teleg=teleg;
        this.whatsup=whatsup;
        this.avatar=avatar;
        this.info=info;
        this.date=date;
        this.datetitle=datetitle;


        this.pos=pos;
    }
    public String getFirstName(){
        return name;
    }
    public String getMnumber(){ return mnumber; }
    public String getNumber2(){
        return  number2;
    }
    public String getNumber3(){
        return  number3;
    }
    public String getAddress(){
        return  address;
    }
    public String getEmail(){
        return  email;
    }
    public String getInsta(){
        return  insta;
    }
    public String getTeleg(){
        return  teleg;
    }
    public String getWhatsup(){
        return  whatsup;
    }
    public String getAvatar(){
        return  avatar;
    }
    public String getInfo(){
        return  info;
    }
    public String getDate(){
        return  date;
    }
    public int getid(){
        return  id;
    }

    public String getDatetitle() {
        return datetitle;
    }

    public void setMnumber(String mnumber) {
        this.mnumber = mnumber;
    }

    public static final Comparator<itema> COMPARATOR = new Comparator<itema>() {
        @Override
        public int compare(itema o1, itema o2) {
            return o1.getFirstName().compareTo(o2.getFirstName());
        }

    };

    private static Contact fromRes(Context c, @DrawableRes int img, @StringRes int fn, @StringRes int ln) {
        return new Contact(ContextCompat.getDrawable(c, img), c.getString(fn), c.getString(ln));
    }



}
