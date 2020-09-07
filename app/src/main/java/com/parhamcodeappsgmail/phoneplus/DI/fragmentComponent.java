package com.parhamcodeappsgmail.phoneplus.DI;

import com.parhamcodeappsgmail.phoneplus.Fragment.Log.recentconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.mainconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.NoLimitLog;
import com.parhamcodeappsgmail.phoneplus.Fragment.dialpad;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@ActivityScope
@Component(dependencies = activityComponent.class,modules = fragmentModule.class)
public interface fragmentComponent {
    void inject(mainconfrag mainconfrag);
    void inject(recentconfrag recentconfrag);
    void inject(NoLimitLog noLimitLog);
    void inject(dialpad dialpad);
}
