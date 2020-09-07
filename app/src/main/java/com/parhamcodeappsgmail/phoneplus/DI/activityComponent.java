package com.parhamcodeappsgmail.phoneplus.DI;


import android.content.Context;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.mainconfrag;
import com.parhamcodeappsgmail.phoneplus.MainActivity;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = activityModule.class)
public interface activityComponent {
    void inject(MainActivity mainActivity);
    Context provideContext();
    TinyDB provideTinydb();
    dbAdapter providedbAdapter();

    @Component.Builder
    interface Builder {
        activityComponent build();
        Builder activityModule(activityModule activityModule);
    }
}


