package com.parhamcodeappsgmail.phoneplus.DI;

import android.content.Context;
import android.support.v4.app.Fragment;


import dagger.Module;

@Module
public class fragmentModule {
    private Fragment fragmnet;

    public fragmentModule(Fragment fragment) {
        this.fragmnet = fragment;
    }

}
