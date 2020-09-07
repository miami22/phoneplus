package com.parhamcodeappsgmail.phoneplus.DI;

import android.app.Application;


import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

public class DemoApplication extends Application {

    @Singleton
    @Component(modules = ApplicationModule.class)

    public interface ApplicationComponent {

        void inject(DemoApplication application);
//        activityComponent plus(activityModule module);
//        fragmentComponent plusf1(fragmentModule module);

        //void inject(DemoActivity demoActivity);

    }



//    @Inject
//    TinyDB tinyDB; // for some reason.
//
//    @Inject
//    dbAdapter dbAdapter;



    private ApplicationComponent component;

    @Override public void onCreate() {

        super.onCreate();

        component = DaggerDemoApplication_ApplicationComponent.builder()

                .applicationModule(new ApplicationModule(this))

                .build();

        component().inject(this); // As of now, LocationManager should be injected into this.

    }

    public ApplicationComponent component() {

        return component;

    }

}