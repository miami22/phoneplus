package com.parhamcodeappsgmail.phoneplus.DI;



import android.content.Context;
import android.support.v7.app.AppCompatActivity;


import com.parhamcodeappsgmail.phoneplus.BuildConfig;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.util.IabHelper;
import com.parhamcodeappsgmail.phoneplus.viewpageradpC;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class activityModule {
    private AppCompatActivity myactivity;
    public activityModule(AppCompatActivity activity){
        myactivity=activity;
    }
    @Provides

     Context provideContext() {
        return myactivity;
    }



    @Provides
    @Singleton
     TinyDB provideTinyDb(Context context){
        return  new TinyDB(context);
    }


    @Provides
    @Singleton
    public dbAdapter providedbadapter( Context context){
        return new dbAdapter(context);
    }


    @Provides
    @Singleton
    viewpageradpC provideAdapter(){
        return  new viewpageradpC(myactivity.getSupportFragmentManager());
    }



    @Provides
    @Singleton
    IabHelper provideIabHelper( Context context){
        String key="";
        if (BuildConfig.FLAVOR.equals("bazar")) {
            key=BuildConfig.bazarkey;
        } else {
            key=BuildConfig.myketkey;
        }
        return  new IabHelper(context,key );
    }





}
