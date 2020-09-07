package com.parhamcodeappsgmail.phoneplus.DI;

import android.content.Context;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module

public class ApplicationModule {

    private final DemoApplication application;



    public ApplicationModule(DemoApplication application) {

        this.application = application;

    }



    /**

     * Allow the application context to be injected but require that it be annotated with

     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.

     */

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {

        return application;

    }



//    @Provides
//    @Singleton
//    TinyDB provideTinyDb(@ForApplication Context context){
//        return  new TinyDB(context);
//    }
//
//    @Provides
//    @Singleton
//    dbAdapter provideDB(@ForApplication Context context){
//        return new dbAdapter(context);
//    }






    /*
    @Provides
    @Singleton
    IabHelper.OnIabSetupFinishedListener providesetuppListener(){
        return new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {

            }
        };
    }

    @Provides
    @Singleton
    IabHelper.QueryInventoryFinishedListener provideQueryListener(){
        return new IabHelper.QueryInventoryFinishedListener() {

            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {

            }
        };
    }

    @Provides
    @Singleton
    IabHelper.OnIabPurchaseFinishedListener providePurchaseListener(){
        return new IabHelper.OnIabPurchaseFinishedListener() {


            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {

            }
        };
    }
     */


    /*
     @Provides
    @Singleton
    DataBase provideDB(@ForApplication Context context)
    {
        return  Room.databaseBuilder(context, DataBase.class, "buldpriceDB").build();

    }
     */


}
