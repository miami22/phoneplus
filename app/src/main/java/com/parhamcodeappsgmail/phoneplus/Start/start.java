package com.parhamcodeappsgmail.phoneplus.Start;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.viewpageradpC;

public class start extends AppCompatActivity {

    ViewPager viewPager;
    public viewpageradpC adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        viewPager=findViewById(R.id.pagerstart);

        adpter = new viewpageradpC(getSupportFragmentManager());
        adpter.addfrg(new startfrag1(), "انتخاب تم");
        adpter.addfrg(new start3(), "راهنما");
        adpter.addfrg(new start2(), "مجوزهای برنامه");
        viewPager.setAdapter(adpter);
    }
    void pageview(){
        int pos=viewPager.getCurrentItem();
        viewPager.setCurrentItem(pos+1);
    }
}
