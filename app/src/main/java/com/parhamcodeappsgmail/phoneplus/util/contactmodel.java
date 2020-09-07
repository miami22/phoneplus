package com.parhamcodeappsgmail.phoneplus.util;

import android.graphics.Bitmap;
import android.net.Uri;

public class contactmodel {
    public String id;
    public String name;
    public String mobileNumber;
    public Bitmap photo;
    public Uri photoURI;

    public String getname(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getMobileNumber(){
        return mobileNumber;
    }
    public Bitmap getPhoto(){
        return photo;
    }
    public Uri getPhotoURI(){
        return photoURI;
    }

/*
 public contactmodel (String id,String name,String mobileNumber,Bitmap photo,Uri photoURI){
    this.id=id;
    this.name=name;
    this.mobileNumber=mobileNumber;
    this.photo=photo;
    this.photoURI=photoURI;
}

    public String getFirstName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getMobileNumber(){
        return mobileNumber;
    }
    public Bitmap getPhoto(){
        return photo;
    }
    public Uri getPhotoURI(){
        return photoURI;
    }

 */

}