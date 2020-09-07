package com.parhamcodeappsgmail.phoneplus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.parhamcodeappsgmail.phoneplus.DI.DaggeractivityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityModule;
import com.parhamcodeappsgmail.phoneplus.Fragment.Log.recentconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.fraglist;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.mainconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.NoLimitLog;
import com.parhamcodeappsgmail.phoneplus.Fragment.blacklistitemFragment;
import com.parhamcodeappsgmail.phoneplus.Fragment.dialpad;
import com.parhamcodeappsgmail.phoneplus.Start.start;
import com.parhamcodeappsgmail.phoneplus.Start.startfrag1;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Worker.Myworker;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


import javax.inject.Inject;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @Inject TinyDB tinyDB;
    @Inject viewpageradpC adpter;

    TabLayout tabLayout;
    public   CustomViewPager viewpagerc;
    private  ConstraintLayout bottomlay;
    private ImageView logcheck;
    public static fraglist flist;
    private SearchView searchView;
    private ImageView closadd;
    static boolean active = true;
    private LinearLayout adlay;
    private TapsellAd ad;
    public ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        setContentView(R.layout.activity_main);
        /*
        ((DemoApplication) getApplication()).component()
                .plus(new activityModule(this))
                .inject(this);
         */

        activityComponent component= DaggeractivityComponent.builder().activityModule(new activityModule(this)).build();
        component.inject(this);

        firstrun();
        findviews();
        oncreatetasks();
        themeset();
//        simcheck(this);

//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


//////////////////////END OF ONCREATE   /////////////////////////


    }




    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        Typeface tf1;
        tf1 = Typeface.createFromAsset(getAssets(), "font/byek.TTF");
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);

                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tf1);
                    ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.white));
                }


            }
        }
    }


    public Fragment getFragment(int pos) {
        return adpter.getItem(pos);
    }

    void createtabs() {
        adpter.addfrg(new mainconfrag(), "مخاطبین");
        adpter.addfrg(new recentconfrag(), "اخیر");
        adpter.addfrg(new blacklistitemFragment(), "لیست سیاه");
//        adpter.addfrg(new NoLimitLog(), "گزارش");
        viewpagerc.setAdapter(adpter);
        viewpagerc.setPagingEnabled(false);
        //viewPager.setPagingEnabled(false);
        tabLayout.setupWithViewPager(viewpagerc);
        flist = new fraglist(adpter.getItem(0), adpter.getItem(1), adpter.getItem(2));
        changeTabsFont();

        viewpagerc.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 2) {
                    bottomlay.setVisibility(View.GONE);
                    logcheck.setVisibility(View.GONE);
                }
                else if (i==1){
                    bottomlay.setVisibility(View.VISIBLE);
                    logcheck.setVisibility(View.VISIBLE);
                    //get tab from tablayout
                    //View mainTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(3);

                    if (tinyDB.getBoolean("first5")){
                        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                .setTarget(logcheck)
                                .setPrimaryText("لیست تماس نامحدود")
                                .setSecondaryText("سابقه تماس به صورت نامحدود در اینجا نمایش داده می شود")
                                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                                {
                                    @Override
                                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                                    {
                                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                                        {
                                        }
                                    }
                                })
                                .show();
                        tinyDB.putBoolean("first5",false); /////nolilmit TARGET

                    }

                }
                else {
                    bottomlay.setVisibility(View.VISIBLE);
                    logcheck.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    void alert() {
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("بدون دادن مجوز قادر به استفاده از برنامه نخواهید بود.");
        bld.setCancelable(false);
        //View view = ((DaggerActivity) getActivity()).getLayoutInflater().inflate(layoutResource, null, false);
        bld.setNeutralButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();

            }
        });
        bld.setNegativeButton("دادن مجوز", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getpermissions();

            }
        });

        bld.create().show();
    }   ////getpermissiondialog


    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("جستجو");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("DIALPAD");
                    if (fragment != null && fragment.isAdded()){
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager
                                .beginTransaction()
                                .disallowAddToBackStack()
                                .setCustomAnimations(0, R.anim.slidedown)
                                .remove(fragment)
                                .commitNow();
                    }
                }
            }
        });
    }

    public boolean onQueryTextChange(String newText) {

        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("NOLIMIT");
        if(!newText.isEmpty())tinyDB.putBoolean("searchmod",true);
        else tinyDB.putBoolean("searchmod",false);

        if (fragment2 != null && fragment2.isAdded()) NoLimitLog.adapter.filter(newText);
        else if (viewpagerc.getCurrentItem() == 1) {

            viewpagerc.setCurrentItem(0);
            mainconfrag.adapter.filter(newText);


        } else if (flist.getFraga().isResumed()) mainconfrag.adapter.filter(newText);


        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public  void getpermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CONTACTS
                        , Manifest.permission.WRITE_CONTACTS,Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {


                            if (!tinyDB.getBoolean("simcheck")) {
                                simcheck(MainActivity.this);
                                tinyDB.putBoolean("simcheck", true);
                            }
                            createtabs();
                            setupSearchView();
                            workmanger();
                        } else alert();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }


                }).check();


    }

    @Override
    public void onResume() {

        if(tinyDB.getBoolean("themechanged")){
            themeset();
            changeTabsFont();
            if(tinyDB.getBoolean("first2")){
                getpermissions();
                tinyDB.putBoolean("first2",false);
            }
            tinyDB.putBoolean("themechanged",false);
            tinyDB.putBoolean("informnolimit",true);
        }


        super.onResume();

    }

    @Override
    public void onBackPressed() {
      /*
      if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
       */


        Fragment fragment = getSupportFragmentManager().findFragmentByTag("DIALPAD");
        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("NOLIMIT");

        if (fragment != null && fragment.isAdded()) {
            // ok, we got the fragment instance, but should we manipulate its view?
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(0, R.anim.slidedown)
                    .remove(fragment)
                    .commitNow();
        }
        else if(fragment2 != null && fragment2.isAdded()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(0, R.anim.slidedown)
                    .remove(fragment2)
                    .commitNow();
            bottomlay.setVisibility(View.VISIBLE);
        }
        else if (tinyDB.getBoolean("searchmod")) {
            searchView.setQuery("",false);
            if (viewpagerc.getCurrentItem()==3){
                NoLimitLog.adapter.filter("");
            }
            else {
                mainconfrag.adapter.filter("");
            }

            //NoLimitLog.adapter.filter("");
            tinyDB.putBoolean("searchmod", false);
        } else super.onBackPressed();


        //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }


    public static void simcheck(Context context) {
        Log.i("MainActivity", "simcheckcalled ");
        TinyDB tDB = new TinyDB(context);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);
            tDB.putBoolean("sim1ready", telephonyInfo.isSIM1Ready());
            String serailSIM1 = telephonyInfo.getSerialSIM1();
            if (serailSIM1 != null) tDB.putString("sim1serial", telephonyInfo.getSerialSIM1());
            tDB.putString("sim1op",telephonyInfo.getOpSIM1());

            String serailSIM2 = telephonyInfo.getSerialSIM2();
            if (serailSIM2 != null) tDB.putString("sim2serial", serailSIM2);
            tDB.putBoolean("sim2ready", telephonyInfo.isSIM2Ready());
            tDB.putString("sim2op",telephonyInfo.getOpSIM2());

            tDB.putBoolean("isdual",telephonyInfo.isDualSIM());

            Log.i("Test", "serial SIM1: "+serailSIM1);
            Log.i("Test", "serial SIM2: "+serailSIM2);
        }

        else isDualSim(context);

        tDB.putBoolean("pendingsim", false);


        /*Log.i("MainActivity", "api<23");
        AlertDialog.Builder bld=null;
        if (tDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);            bld.setMessage("لطفا تعداد سیم کارت دستگاه را مشخص کنید");
        bld.setNeutralButton("تک سیم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tDB.putBoolean("isdual", false);
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                String simop = tm.getNetworkOperatorName();
                if (simop != null) tDB.putString("sim1op", simop);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.


                    String serial = tm.getSimSerialNumber();
                    if (serial != null) tDB.putString("sim1serial", serial);
                    if (tm.getSimState() == tm.SIM_STATE_READY)
                        tDB.putBoolean("sim1ready", true);
                    else tDB.putBoolean("sim1ready", false);
                    Log.i("MainActivity", "simop: " + simop + "   simserail:" + serial);
                }

            }
        });
        bld.setNegativeButton("دو سیم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tDB.putBoolean("isdual", true);
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                String simop = tm.getNetworkOperatorName();
                if (simop != null) tDB.putString("sim1op", simop);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.


                    String serial = tm.getSimSerialNumber();
                    if (serial != null) tDB.putString("sim1serial", serial);
                    if (tm.getSimState() == tm.SIM_STATE_READY)
                        tDB.putBoolean("sim1ready", true);
                    else tDB.putBoolean("sim1ready", false);
                    Log.i("MainActivity", "simop: " + simop + "   simserail:" + serial);

                }

            }
        });
        bld.create().show();*/
    }


    public static  void  isDualSim(final Context context) {
        TinyDB tinyDB=new TinyDB(context);
        int count=0;
        String firstOP="";
        String secOP="";
        String firstID="";
        String secID="";
        String firstSLOT="";
        String secSLOT="";
        String opSIM1;
        String opSIM2;
        try {
            SubscriptionManager sm=null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                sm = SubscriptionManager.from(context);
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                int activeSubscriptionInfoCount=0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    activeSubscriptionInfoCount = sm.getActiveSubscriptionInfoCount();
                    count= activeSubscriptionInfoCount;
                    Log.i("Test", "sim count = " + count);
                    if(count>1)tinyDB.putBoolean("isdual",true);
                    else tinyDB.putBoolean("isdual",false);
                    Log.i("Test","is dual?   "+tinyDB.getBoolean("isdual"));
                    List<SubscriptionInfo> subsInfoList = sm.getActiveSubscriptionInfoList();



                    for (SubscriptionInfo subscriptionInfo : subsInfoList) {
                        Log.i("Test", "Current list = " + subscriptionInfo);
                        Integer index=subscriptionInfo.getSimSlotIndex();
                        if(index==0)
                        {
                            firstSLOT=subscriptionInfo.getSimSlotIndex()+"";
                            firstID=subscriptionInfo.getIccId()+"";
                            firstOP=subscriptionInfo.getCarrierName()+"";
                            Log.i("Test", "first op = " + firstOP);
                            Log.i("Test", "first id = " + firstID);

                        }
                        else{
                            secSLOT=subscriptionInfo.getSimSlotIndex()+"";
                            secID=subscriptionInfo.getIccId()+"";
                            secOP=subscriptionInfo.getCarrierName()+"";
                            Log.i("Test", "sec op = " + secOP);
                            Log.i("Test", "sec id = " + secID);

                        }

                    }

                    if(count>1){
                        if (firstSLOT.equals("0")){
                            Log.i("Test", "first slot = 0");
                            tinyDB.putString("sim1serial",firstID);
                            tinyDB.putString("sim1op",firstOP);
                            tinyDB.putString("sim2serial",secID);
                            tinyDB.putString("sim2op",secOP);
                            opSIM1=firstOP;
                            opSIM2=secOP;
                        }
                        else {
                            Log.i("Test", "first slot != 0");
                            tinyDB.putString("sim1serial",secID);
                            tinyDB.putString("sim1op",secOP);
                            tinyDB.putString("sim2serial",firstID);
                            tinyDB.putString("sim2op",firstOP);
                            opSIM1=secOP;
                            opSIM2=firstOP;
                        }

                        if(validop(opSIM1)&&validop(opSIM2)){
                            tinyDB.putBoolean("simopok",true);
                            Log.i("Test", "sim op is valid ");
                            if (opSIM1.equals(opSIM2)) tinyDB.putBoolean("sameop", true);
                            else tinyDB.putBoolean("sameop", false);
                        }
                        else tinyDB.putBoolean("simopok",false);
                    }
                    else {
                        tinyDB.putString("sim1op",firstOP);
                        tinyDB.putString("sim1serial",firstID);
                        tinyDB.putBoolean("isdual",false);
                    }
                }

            }

            //System.out.println("active subscription count: " + activeSubscriptionInfoCount);

        }
        catch (Throwable t)
        {
            //t.printStackTrace();

        }

    }




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
             */


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            //notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tinyDB.putBoolean("startwithlog",true);
        tinyDB.putBoolean("appisactive",false);

    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }





    public static  Boolean validop(String op){
        if(op.equals("IR-MCI")||op.equals("IR-TCI")||op.equals("Irancell"))return  true;
        else return false;
    }

    void themeset(){

        if(tinyDB.getBoolean("lighttheme")){
            View rootview=findViewById(R.id.maincontainer);
            rootview.setBackgroundColor(getResources().getColor(R.color.lightgray));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.nafti));
            //bottomlay.setBackgroundColor(getResources().getColor(R.color.transgray));
            //downlay.setBackground(getResources().getDrawable(R.drawable.gradbacklight));
        }
        else  {
            View rootview=findViewById(R.id.maincontainer);
            rootview.setBackgroundColor(getResources().getColor(R.color.mainback));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
            //downlay.setBackground(getResources().getDrawable(R.drawable.gradback2));
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void loadadd(){
        Tapsell.initialize(this, "ejkfkmtkjsrpepmpteslphkjghbrrbghiqsarnrqrminipmnenlorctdrfehljafcsciie");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (tinyDB.getBoolean("addenabled")){
                    loadAd("5d040bebfbb7070001597a38", TapsellAdRequestOptions.CACHE_TYPE_STREAMED);
                    int day=tinyDB.getInt("daycount");
                    tinyDB.putInt("dayholder",day);
                }


                ////////bannerAdd
                if ( tinyDB.getBoolean("addenabled")){

                    adlay=findViewById(R.id.adlay);
                    closadd=findViewById(R.id.clearadd);
                    if(tinyDB.getBoolean("lighttheme"))closadd.setImageResource(R.drawable.clear);
                    else closadd.setImageResource(R.drawable.clear2);

                    TapsellBannerView bannerView = findViewById(R.id.banner1);
                    bannerView.loadAd(MainActivity.this, "5d04d33dffe1ef0001e107b3", TapsellBannerType.BANNER_320x50);
                    bannerView.setEventListener(new TapsellBannerViewEventListener() {

                        @Override
                        public void onNoAdAvailable() {
                            //Do sth
                            adlay.setVisibility(View.GONE);
                            // Message.message(MainActivity.this,"noAdAvailable",1);
                        }

                        @Override
                        public void onNoNetwork() {
                            //Do sth
                            adlay.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(String s) {
                            //Do sth
                            adlay.setVisibility(View.GONE);

                        }

                        @Override
                        public void onRequestFilled() {
                            //Do sth
                            adlay.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onHideBannerView() {
                            //Do sth
                            adlay.setVisibility(View.GONE);
                        }
                    });

                    closadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adlay.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }, 3000);
    }

    private void loadAd(final String zoneId, final int catchType) {

        ProgressDialog progressDialog = new ProgressDialog(this);

        if (ad == null) {

            TapsellAdRequestOptions options = new TapsellAdRequestOptions(catchType);

            Tapsell.requestAd(MainActivity.this, zoneId, options, new TapsellAdRequestListener() {

                @Override

                public void onError(String error) {

                    //Toast.makeText(MainActivity.this, "ERROR:\n" + error, Toast.LENGTH_SHORT).show();

                    Log.e("Tapsell", "ERROR:" + error);

                    progressDialog.dismiss();

                }



                @Override

                public void onAdAvailable(TapsellAd ad) {



                    MainActivity.this.ad = ad;
                    ad.show(MainActivity.this, null, new TapsellAdShowListener() {

                        @Override

                        public void onOpened(TapsellAd ad) {

                            Log.e("MainActivity", "on ad opened");

                        }



                        @Override

                        public void onClosed(TapsellAd ad) {

                            Log.e("MainActivity", "on ad closed");

                        }
                    });



                    progressDialog.dismiss();

//                new AlertDialog.Builder(MainActivity.this).setTitle("Title").setMessage("Message").show();

                }



                @Override

                public void onNoAdAvailable() {

                    // Toast.makeText(MainActivity.this, "No Ad Available", Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();

                    Log.e("Tapsell", "No Ad Available");

                }



                @Override

                public void onNoNetwork() {

                    // Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    Log.e("Tapsell", "No Network Available");

                }



                @Override

                public void onExpiring(TapsellAd ad) {


                    MainActivity.this.ad = null;

                    loadAd(zoneId, catchType);

                }

            });

        }

    }

    void firstrun(){
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).
                getBoolean("isFirstRun", true);
        if (isFirstRun) {
            tinyDB.putBoolean("firstreadlog",true);
            tinyDB.putInt("missedlogcount", -1);
            tinyDB.putInt("readconper", 0);
            tinyDB.putInt("readconper", 0);
            tinyDB.putInt("readlogper", 0);
            tinyDB.putInt("contactinputed", 0);
            tinyDB.putInt("loginputed", 0);
            tinyDB.putInt("fromnew", 0);
            tinyDB.putInt("uplog", 0);
            tinyDB.putBoolean("searchmod", false);
            tinyDB.putBoolean("isdual", false);
            tinyDB.putBoolean("callstate", false);
            tinyDB.putBoolean("sound", true);
            tinyDB.putBoolean("simcheck", false);
            tinyDB.putBoolean("sim1ready", true);
            tinyDB.putBoolean("sim2ready", true);
            tinyDB.putBoolean("autobackup", false);
            tinyDB.putBoolean("infinatelog", true);
            tinyDB.putInt("saveconloc", 2);
            tinyDB.putInt("dialpadsound", 2);
            tinyDB.putBoolean("firstrunfrag", true);
            tinyDB.putBoolean("ispremium", false);
            tinyDB.putInt("addver", 0);
            tinyDB.putInt("addnotrep", 0);
            tinyDB.putInt("appver", 0);
            tinyDB.putInt("appuprep", 0);
            tinyDB.putString("callsimid", "0");
            tinyDB.putBoolean("startwithlog", false);
            tinyDB.putBoolean("savefromlog", false);
            tinyDB.putString("sim1serial", "");
            tinyDB.putString("sim2serial", "");
            tinyDB.putString("sim1op", "");
            tinyDB.putString("sim2op", "");
            tinyDB.putBoolean("sameop", false);
            tinyDB.putBoolean("firstback", true);
            tinyDB.putBoolean("firstrestore", true);
            tinyDB.putString("lastcalltime", "");
            tinyDB.putBoolean("pendingsim", false);
            tinyDB.putBoolean("appisactive", false);
            tinyDB.putInt("daycount", 0);
            tinyDB.putInt("dayholder", 0);
            tinyDB.putBoolean("addenabled", false);
            tinyDB.putBoolean("informprim", false);
            tinyDB.putBoolean("simopok", false);
            tinyDB.putString("font","font/vazir.ttf");
            tinyDB.putBoolean("fontchange",false);
            tinyDB.putBoolean("fontchange2",false);
            tinyDB.putBoolean("lighttheme",false);
            tinyDB.putBoolean("themechanged",false);
            tinyDB.putBoolean("permissok",false);
            tinyDB.putBoolean("first1",true);
            tinyDB.putBoolean("first2",true);
            tinyDB.putBoolean("first3",true);
            tinyDB.putBoolean("first4",true);
            tinyDB.putBoolean("first5",true);
            tinyDB.putBoolean("first7",true);
            tinyDB.putListString("blacklist",new ArrayList<>());
            tinyDB.putListString("blacklistname",new ArrayList<>());
            tinyDB.putBoolean("newsession",false);
            tinyDB.putBoolean("firstlog",true);
            tinyDB.putBoolean("logdelete",false);
            tinyDB.putBoolean("labasync",false);



        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                putBoolean("isFirstRun", false).apply();
    }


    void findviews(){
        bottomlay = findViewById(R.id.bottomlayout);
        tabLayout = findViewById(R.id.tablayoutC);
        progressBar=findViewById(R.id.mainprogress);
        viewpagerc=findViewById(R.id.viewpagercc);
        searchView = findViewById(R.id.searchtxt);
        //transback=findViewById(R.id.bottimage);
        logcheck=findViewById(R.id.logcheck);

        logcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("NOLIMIT");
                if (fragment != null && fragment.isAdded()) {
                    // ok, we got the fragment instance, but should we manipulate its view?
                    fragmentManager
                            .beginTransaction()
                            .disallowAddToBackStack()
                            .setCustomAnimations(0, R.anim.slidedown)
                            .remove(fragment)
                            .commitNow();
                    bottomlay.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    NoLimitLog noLimitLog = new NoLimitLog();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
                    fragmentTransaction.add(R.id.tabcontainer, noLimitLog, "NOLIMIT");
                    fragmentTransaction.commitNow();
                    bottomlay.setVisibility(View.GONE);
                }
            }
        });




        tinyDB.putBoolean("newsession",true);
        final ImageView imageView = findViewById(R.id.add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                imageView.startAnimation(animFadein);
                Intent mIntent = new Intent(MainActivity.this, newcontact.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", 0);
                mBundle.putInt("edit", 0);
                mBundle.putString("type", "m");
                mIntent.putExtras(mBundle);
                startActivity(mIntent);

            }
        });

        ImageView dialler = findViewById(R.id.dial);
        dialler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialpad fragment = new dialpad();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
                fragmentTransaction.add(R.id.maincontainer, fragment, "DIALPAD");
                fragmentTransaction.commit();


                // transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);


            }
        });

        ImageView setting = findViewById(R.id.sett);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this, Setting.class);
                Bundle mBundle = new Bundle();
                mBundle.putBoolean("fromMain", false);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);

            }
        });

        //set the ontouch listener
        PushDownAnim.setPushDownAnimTo(dialler, setting,logcheck);

    }


    void oncreatetasks(){
        tinyDB.putBoolean("appisactive",true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String NOTIFICATION_CHANNEL_ID = "phoneplus";
            String channelName = "phone+";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setSound( Uri.parse("android.resource://com.parhamcodeappsgmail.phoneplus/" + R.raw.notifa),null);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

        }




        if(tinyDB.getBoolean("first2")){
            startActivity(new Intent(MainActivity.this, start.class));
        }
        else  getpermissions();


        if (tinyDB.getBoolean("startwithlog")) viewpagerc.setCurrentItem(1);
        if (tinyDB.getBoolean("pendingsim")) simcheck(this);
        //tinyDB.putBoolean("addenabled",true);




        if (!tinyDB.getBoolean("ispremium")&&tinyDB.getBoolean("informprim")){
            tinyDB.putBoolean("informprim",false);
            AlertDialog.Builder bld=null;
            if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            else bld= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);            bld.setCancelable(false);
            bld.setMessage("برنامه قادر به پشتیبان گیری خودکار از شماره ها نیست. \n " +
                    "لطفا برای فعال شدن پشتیبان گیری، برنامه را فعال کنید");
            bld.setNeutralButton("فعالسازی", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mIntent = new Intent(MainActivity.this, Setting.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("fromMain", true);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                }
            });
            bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            bld.create().show();
        }
        if(tinyDB.getBoolean("first4")&&tinyDB.getBoolean("addenabled")&&!tinyDB.getBoolean("ispremium")){

            ArrayList<String> black=tinyDB.getListString("blacklist");
            tinyDB.putListString("savelist",black);
            ArrayList<String> empty=new ArrayList<>();
            tinyDB.putListString("blacklist",empty);

            AlertDialog.Builder bld=null;
            if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            else bld= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);            bld.setCancelable(false);
            bld.setMessage("تبلیغات فعال شد. \n " +
                    "لطفا برای حذف تبلیغات برنامه را از قسمت تنظیمات فعال کنید");
            bld.setNeutralButton("فعالسازی", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mIntent = new Intent(MainActivity.this, Setting.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean("fromMain", true);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                }
            });


            bld.create().show();
            tinyDB.putBoolean("first4",false);

        }
        if(isNetworkAvailable()&&!tinyDB.getBoolean("ispremium"))loadadd();
    }

    void workmanger(){

        Constraints constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest.Builder myWorkBuilder =
                new PeriodicWorkRequest.Builder(Myworker.class, 1, TimeUnit.DAYS);

        PeriodicWorkRequest myWork = myWorkBuilder
                .setConstraints(constraint)
                .build();
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("GetData", ExistingPeriodicWorkPolicy.KEEP, myWork);

        WorkManager.getInstance().getWorkInfoByIdLiveData(myWork.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo info) {
                        if (info != null && info.getState().isFinished()) {
                            boolean myResult = info.getOutputData().getBoolean("premium",
                                    false);
                            Log.d("OBS",myResult+"....result");
                            if (myResult){

                            }

                        }
                    }
                });


    }


}



