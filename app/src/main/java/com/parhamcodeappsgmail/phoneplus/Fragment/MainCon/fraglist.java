package com.parhamcodeappsgmail.phoneplus.Fragment.MainCon;

import android.support.v4.app.Fragment;

public class fraglist {
    Fragment fraga;
    Fragment fragb;
    Fragment fragc;
    public fraglist(Fragment a,Fragment b,Fragment c){
        this.fraga=a;
        this.fragb=b;
        this.fragc=c;
    }

    public Fragment getFraga(){return fraga;}
    public Fragment getFragb(){return fragb;}
    public Fragment getFragc(){return fragc;}

    public void setFraga(Fragment fraga) {
        this.fraga = fraga;
    }

    public void setFragb(Fragment fragb) {
        this.fragb = fragb;
    }

    public void setFragc(Fragment fragc) { this.fragc = fragc; }
}
