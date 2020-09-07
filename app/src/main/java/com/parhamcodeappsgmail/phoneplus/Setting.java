package com.parhamcodeappsgmail.phoneplus;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Fragment.help;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Tools.backupDB;
import com.parhamcodeappsgmail.phoneplus.util.IabHelper;
import com.parhamcodeappsgmail.phoneplus.util.IabResult;
import com.parhamcodeappsgmail.phoneplus.util.Inventory;
import com.parhamcodeappsgmail.phoneplus.util.Purchase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.omidh.liquidradiobutton.LiquidRadioButton;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class Setting extends AppCompatActivity {
    LiquidRadioButton savedevice,saveboth,sound1,sound2,sound3,sound4,nosound;
    LiquidRadioButton vazir,yekan,casabelan,dast,davat,free,rezvan,traffic,themel,themed;
    RadioGroup radioGroup,radioGroup2,radiogroupfont,radiogrouptheme;
    LinearLayout activationlay,contactmelay,backreslay,shareapplay,insta;
    Button sharefile,restore,shareapp,backup,help,writetodev;
    ImageView play1,play2,play3,play4,hand;
    TinyDB tinyDB;
    MediaPlayer mp1,mp2,mp3,mp4;
    Switch inflog,abackup;
    Typeface tf;
    FirebaseAnalytics firebaseAnalytics;

    LinearLayout backres;
    TextView txtactive,txthide,progtxt,fonttxt;
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8;

    static final String TAG = "inappbilling";
    static final String SKU_PREMIUM = "premium";
    static final int RC_REQUEST =10003;
    IabHelper mHelper;
    boolean mIsPremium = false;
    Boolean connected=false;
    ConstraintLayout container;
    String store;
    String storename;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //settheme(getResources().getR.style.Theme_AppCompat_Translucentlight);
        super.onCreate(savedInstanceState);

        if (BuildConfig.FLAVOR.equals("bazar")){
            store="برنامه بازار";
            storename="com.farsitel.bazaar";
        }
        else {
            store="برنامه مایکت";
            storename="ir.mservices.market";
        }
        setContentView(R.layout.activity_setting);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tinyDB=new TinyDB(this);            ///ir.mservices.market      com.farsitel.bazaar   ir.tgbs.android.iranapp
        if (!tinyDB.getBoolean("ispremium")&&isPackageExisted(storename))connect();
        mp1=MediaPlayer.create(this,R.raw.beep);
        mp2=MediaPlayer.create(this,R.raw.beep2);
        mp3=MediaPlayer.create(this,R.raw.clickglass);
        mp4=MediaPlayer.create(this,R.raw.drop);

        hand=findViewById(R.id.hand);

        progtxt=findViewById(R.id.Sprogtxt);
        container=findViewById(R.id.settingcontainer);

        activationlay=findViewById(R.id.activationlay);
        activationlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activate();
            }
        });

        backup=findViewById(R.id.Sbackupbutt);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tinyDB.getBoolean("ispremium"))getperandreadwrite("write");
                else alert2();


            }
        });

        inflog=findViewById(R.id.Sinflog);
        inflog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)tinyDB.putBoolean("infinatelog",true);
                else tinyDB.putBoolean("infinatelog",false);
            }
        });
        abackup=findViewById(R.id.Sbackup);
        if(tinyDB.getBoolean("first3")){
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(abackup)
                    .setPrimaryText("پشتیبان گیر خودکار")
                    .setSecondaryText("برای پشتیبان گیری اتوماتیک روزانه فعال کنید")
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
            tinyDB.putBoolean("first3",false);
        }

        sharefile=findViewById(R.id.Ssharebackbutt);
        sharefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tinyDB.getBoolean("ispremium"))sharefilemeth();
                else alert2();
            }
        });

        restore=findViewById(R.id.Sresbutt);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tinyDB.getBoolean("ispremium"))getperandreadwrite("read");
                else alert2();

            }
        });

        savedevice=findViewById(R.id.Ssavedevicerad);
        saveboth=findViewById(R.id.savedevandrad);
        radioGroup=findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.savedevandrad){tinyDB.putInt("saveconloc",2);}
                else {tinyDB.putInt("saveconloc",1);}
            }
        });


        sound1=findViewById(R.id.Ssound1);
        sound2=findViewById(R.id.Ssound2);
        sound3=findViewById(R.id.Ssound3);
        sound4=findViewById(R.id.Ssound4);

        nosound=findViewById(R.id.Ssoundoff);
        radioGroup2=findViewById(R.id.radiogroup2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.Ssound1)tinyDB.putInt("dialpadsound",1);
                else if (i==R.id.Ssound2)tinyDB.putInt("dialpadsound",2);
                else if (i==R.id.Ssound3)tinyDB.putInt("dialpadsound",3);
                else if (i==R.id.Ssound4)tinyDB.putInt("dialpadsound",4);
                else tinyDB.putInt("dialpadsound",0);
            }
        });

        vazir=findViewById(R.id.vazir);
        yekan=findViewById(R.id.yekan);
        dast=findViewById(R.id.dastnevis);
        davat=findViewById(R.id.davat);
        casabelan=findViewById(R.id.arsoo);
        free=findViewById(R.id.hamid);
        rezvan=findViewById(R.id.mahsa);
        traffic=findViewById(R.id.hemmat);

        fonttxt=findViewById(R.id.fonttxt);
        radiogroupfont=findViewById(R.id.fontgroup);
        radiogroupfont.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i==R.id.vazir){
                    tinyDB.putString("font","font/vazir.ttf");
                    tf=Typeface.createFromAsset(getAssets(),"font/vazir.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(16);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.hemmat){
                    tinyDB.putString("font","font/traffic.ttf");
                    tf=Typeface.createFromAsset(getAssets(),"font/traffic.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(21);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.dastnevis) {
                    tinyDB.putString("font","font/dastnevis.ttf");
                    tf=Typeface.createFromAsset(getAssets(), "font/dastnevis.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(18);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.davat){
                    tinyDB.putString("font","font/davat.ttf");
                    tf=Typeface.createFromAsset(getAssets(), "font/davat.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(19);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.yekan){
                    tinyDB.putString("font","font/byek.TTF");
                    tf=Typeface.createFromAsset(getAssets(), "font/byek.TTF");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(15);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه ");
                }
                else if (i==R.id.hamid){
                    tinyDB.putString("font","font/free.ttf");
                    tf=Typeface.createFromAsset(getAssets(), "font/free.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(22);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.mahsa){
                    tinyDB.putString("font","font/rezvan.ttf");
                    tf=Typeface.createFromAsset(getAssets(), "font/rezvan.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(20);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }
                else if (i==R.id.arsoo){
                    tinyDB.putString("font","font/casablanca.ttf");
                    tf=Typeface.createFromAsset(getAssets(), "font/casablanca.ttf");
                    fonttxt.setTypeface(tf);
                    fonttxt.setTextSize(16);
                    tinyDB.putBoolean("fontchange",true);
                    tinyDB.putBoolean("fontchange2",true);
                    fonttxt.setText("انتخاب فونت مخاطبین برنامه");
                }

            }
        });

        themel=findViewById(R.id.themel);
        themed=findViewById(R.id.themed);
        radiogrouptheme=findViewById(R.id.grouptheme);
        radiogrouptheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.themel){
                    tinyDB.putBoolean("lighttheme",true);
                    tinyDB.putBoolean("themechanged",true);
                    settheme();
                }
                else {
                    tinyDB.putBoolean("lighttheme",false);
                    tinyDB.putBoolean("themechanged",true);
                    settheme();
                }
            }
        });

        shareapp=findViewById(R.id.Sshareapp);
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   String link;
                   if (BuildConfig.FLAVOR.equals("bazar"))link="https://cafebazaar.ir/app/com.parhamcodeappsgmail.phoneplus/?l=fa";
                   else link="myket.ir/app/parhamcodeappsgmail.phoneplus";


                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");            //myket.ir/app/parhamcodeappsgmail.phoneplus......  iranapps.ir/app/parhamcodeappsgmail.phoneplus
                shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));




            }
        });

        shareapplay=findViewById(R.id.Sshareaplay);
        shareapplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","ParhamCodeApps@gmail.com", null));
                //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        play1=findViewById(R.id.play1);
        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp1.start();
            }
        });
        play2=findViewById(R.id.play2);
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
            }
        });
        play3=findViewById(R.id.play3);
        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp3.start();
            }
        });
        play4=findViewById(R.id.play4);
        play4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp4.start();
            }
        });
        help=findViewById(R.id.Shelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new help();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.add(R.id.settingcontainer,newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        writetodev=findViewById(R.id.Swritetodevice);
        writetodev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bld=null;
                if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                else bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                bld.setMessage("در صورت موجود بودن مخاطبین برنامه در مخاطبین دستگاه، از این گزینه استفاده نکنید\n" +
                        "مخاطبین کپی شوند؟");
                bld.setNeutralButton("کپی مخاطبین", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        writecontactsTASK task=new writecontactsTASK(Setting.this);
                        task.execute();
                    }
                });
                bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                bld.create().show();
            }
        });
        txtactive=findViewById(R.id.txtactive);
        txthide=findViewById(R.id.txtactivhide);
        if(tinyDB.getBoolean("ispremium")){
            txthide.setVisibility(View.GONE);
            txtactive.setTextColor(Color.GREEN);
            txtactive.setText("برنامه فعال است");
            activationlay.setEnabled(false);
            txtactive.setTextColor(getResources().getColor(R.color.green));
        }
        if (!tinyDB.getBoolean("ispremium")&&tinyDB.getBoolean("addenabled"))txthide.setText("فعالسازی و حذف تبلیغات");

        PushDownAnim.setPushDownAnimTo(play1,play2,shareapp,restore,sharefile,backup,help);

        setstatus();

        abackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(tinyDB.getBoolean("ispremium")){
                        tinyDB.putBoolean("autobackup",true);
                        if(tinyDB.getBoolean("firstback")){
                            getperandreadwrite("write");
                            tinyDB.putBoolean("firstback",false);
                        }
                    }
                    else {
                        alert2();
                        abackup.setChecked(false);
                    }



                }
                else tinyDB.putBoolean("autobackup",false);
            }

        });

        insta=findViewById(R.id.Sinstageram);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/parhamcode");
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.instagram.android");

                if (isIntentAvailable(Setting.this, insta)){
                    startActivity(insta);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/parhamcode")));
                }
            }
        });

       boolean fromMain = Objects.requireNonNull(getIntent().getExtras()).getBoolean("fromMain");

        if(fromMain) {


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()  {
                    activate();
                }}, 1000);

        }




        ////////////////////////////////END OF ONCREATE


    }

    void setstatus(){


        if (tinyDB.getInt("saveconloc")==1)savedevice.setChecked(true);
        else saveboth.setChecked(true);

        if (tinyDB.getBoolean("infinatelog"))inflog.setChecked(true);
        else inflog.setChecked(false);

        if(tinyDB.getBoolean("autobackup"))abackup.setChecked(true);
        else abackup.setChecked(false);
        String font=tinyDB.getString("font");

        if(font.equals("font/byek.TTF"))yekan.setChecked(true);
        else if(font.equals("font/vazir.ttf"))vazir.setChecked(true);
        else if(font.equals("font/rezvan.ttf"))rezvan.setChecked(true);
        else if(font.equals("font/traffic.ttf"))traffic.setChecked(true);
        else if(font.equals("font/casablanca.ttf"))casabelan.setChecked(true);
        else if(font.equals("font/free.ttf"))free.setChecked(true);
        else if(font.equals("font/davat.ttf"))davat.setChecked(true);
        else if(font.equals("font/dastnevis.ttf"))dast.setChecked(true);

        if(tinyDB.getBoolean("lighttheme"))themel.setChecked(true);
        else themed.setChecked(true);

        if (tinyDB.getInt("dialpadsound")==0)nosound.setChecked(true);
        else if (tinyDB.getInt("dialpadsound")==1)sound1.setChecked(true);
        else if(tinyDB.getInt("dialpadsound")==2)sound2.setChecked(true);
        else if(tinyDB.getInt("dialpadsound")==3)sound3.setChecked(true);
        else if(tinyDB.getInt("dialpadsound")==4)sound4.setChecked(true);
    }

    void getperandreadwrite(String type){
        Dexter.withActivity(Setting.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            if(type.equals("write")){
                                if (backupDB.SdIsPresent()){
                                    if (backupDB.exportDb())alert("پشتیبان گیری انجام شد\n" +
                                            "آدرس فایل پشتیبان : حافظه خارجی / phoneplus");
                                }
                                else alert("حافظه خارجی موجود نیست \n امکان پشتیبان گیری وجود ندارد");

                            }
                            else if(type.equals("read")) if (backupDB.restoreDb()) alert("دفتر تلفن و سابقه تماس بازیابی شد.");
                        }
                        else {
                            alert("بدون دادن مجوز قادر به پشتیبان گیری و بازیابی نخواهید بود");
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    void alert(String txt){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage(txt);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        bld.create().show();
    }


    public void sharefilemeth(){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(Environment.getExternalStorageDirectory(),"PhonePlus/Phone+BackUp.db");

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileWithinMyDir.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "اشتراک فایل پشتیبان...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
        else Message.message(this,"فایل پشتیبان وجود ندارد",1);
    }

    public void connect(){


            String key="";
            if (BuildConfig.FLAVOR.equals("bazar"))key=BuildConfig.bazarkey;
            else if (BuildConfig.FLAVOR.equals("myket"))key=BuildConfig.myketkey;

            Log.d(TAG, "Creating IAB helper.");
            mHelper = new IabHelper(this, key);

            // enable debug logging (for a production application, you should set this to false).
            mHelper.enableDebugLogging(false);

            // Start setup. This is asynchronous and the specified listener
            // will be called once setup completes.
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {

                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;
                    // Message.message(Setting.this,"setup succsesfull",1);
                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    connected=true;
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    // dialog.dismiss();
                    /*if (!tinyDB.getBoolean("labasync")){
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    }*/
                }
            });}



    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
               // Message.message(Setting.this,"Query faild",1);
                return;
            }
            else {
               // Message.message(Setting.this,"Query succsesfull",1);
                Log.d(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

                // update UI accordingly

                Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                if (mIsPremium){

                    tinyDB.putBoolean("ispremium",true);
                    Message.message(Setting.this,"برنامه فعال شد",1);

                    if(backupDB.fileexists()){
                        AlertDialog.Builder bld=null;
                        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        else bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                    bld.setCancelable(false);
                        bld.setMessage("فایل پشتیبان پیدا شد. \n " +
                                "آیا مایل به بازیابی اطلاعات می باشید؟\n");
                        bld.setNeutralButton("بازیابی", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        bld.create().show();
                    }

                    else   abackup.setChecked(true);

                    ArrayList<String> restore=tinyDB.getListString("savelist");
                    tinyDB.putListString("blacklist",restore);
                    txtactive.setText("برنامه فعال است");
                    txtactive.setTextColor(getResources().getColor(R.color.green));

                }

            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    void activate(){


        if(connected){       // ir.mservices.market    com.farsitel.bazaar
            if(isPackageExisted(storename)){
                ProgressBar progressBar=new ProgressBar(this);

                if(!tinyDB.getBoolean("labasync")){
                    tinyDB.putBoolean("labasync",true);
                    String payload = "1234";
                    mHelper.launchPurchaseFlow(Setting.this, SKU_PREMIUM, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                }
                else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run()  {
                            tinyDB.putBoolean("labasync",true);
                            String payload = "1234";
                            mHelper.launchPurchaseFlow(Setting.this, SKU_PREMIUM, RC_REQUEST,
                                    mPurchaseFinishedListener, payload);
                        }}, 2000);
                }

            }

            else Message.message(Setting.this,store+" در دستگاه شما نصب نمی باشد.\n" +
                    "برای فعالسازی باید "+store+" را نصب کنید",2);
        }
        else {
            connect();

        }

    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            int res=0;
            tinyDB.putBoolean("labasync",false);
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                alert("خرید انجام نشد");
                return;
            }
            else if (purchase.getSku().equals(SKU_PREMIUM)) {

                tinyDB.putBoolean("ispremium",true);
                alert("برنامه فعال شد\n" +
                        "از خرید شما سپاس گذاریم");

                if(backupDB.fileexists()){
                    AlertDialog.Builder bld=null;
                    if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    else bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                    bld.setCancelable(false);
                    bld.setMessage("فایل پشتیبان پیدا شد. \n " +
                            "آیا مایل به بازیابی اطلاعات می باشید؟\n");
                    bld.setNeutralButton("بازیابی", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    bld.create().show();
                }

                else   abackup.setChecked(true);

                         ArrayList<String> restore=tinyDB.getListString("savelist");
                         tinyDB.putListString("blacklist",restore);
                txtactive.setText("برنامه فعال است");
                txtactive.setTextColor(getResources().getColor(R.color.green));

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
    }

    public boolean isPackageExisted(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    void alert2(){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(Setting.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("برنامه غیر فعال است \n " +
                "لطفا برای پشتیبان گیری و بازیابی برنامه را فعال کنید");
        bld.setNeutralButton("فعالسازی", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activate();
            }
        });
        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        bld.create().show();
    }



    private class writecontactsTASK extends AsyncTask<Void , Integer, Void> {
        private dbAdapter db;
        private Context mContext;
        ProgressBar progressBar=findViewById(R.id.progressBarsett);

        private writecontactsTASK(Context context){
            mContext=context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            db=new dbAdapter(Setting.this);
            List<itema> list=db.getDataContact2();
            int size=list.size();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    progressBar.setMax(size);

                }
            });

            for (int i=0;i<size;i++){
                itema item=list.get(i);
                String name=item.getFirstName();
                String number=item.getMnumber();
                String avatar=item.getAvatar();
                Bitmap bitmap=null;
                if (!avatar.equals(""))bitmap= Decodeimg.decodeBase64(avatar);
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                int rawContactID = ops.size();

                // Adding insert operation to operations list
                // to insert a new raw contact in the table
                // ContactsContract.RawContacts
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
                                null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                name).build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (bitmap!= null) { // If an image is selected
                    // successfully
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75,
                            stream);

                    // Adding insert operation to operations list
                    // to insert Photo in the table ContactsContract.Data
                    ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,
                                    rawContactID)
                            .withValue(ContactsContract.Data.IS_SUPER_PRIMARY,
                                    1)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(
                                    ContactsContract.CommonDataKinds.Photo.PHOTO,
                                    stream.toByteArray()).build());

                    try {
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // Executing all the insert operations as a single database
                    // transaction
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY,
                            ops);

                } catch (RemoteException e) {

                    Message.message(Setting.this,"ذخیره در دفتر تلفن دستگاه موفقیت آمیز نبود",1);
                } catch (OperationApplicationException e) {
                    Message.message(Setting.this,"ذخیره در دفتر تلفن دستگاه موفقیت آمیز نبود",1);
                }
                publishProgress(i);
            }


        return  null;
        }

        @Override
        protected void onPostExecute(Void param)
        {

            alert("مخاطبین کپی شد");
            progressBar.setVisibility(View.GONE);
            progtxt.setVisibility(View.GONE);

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            progtxt.setVisibility(View.VISIBLE);
            progtxt.setText("مخاطب کپی شده ..."+ values[0]);

        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    void settheme(){
        txt1=findViewById(R.id.txtone);
        txt2=findViewById(R.id.txttwo);
        txt3=findViewById(R.id.txtthree);
        txt4=findViewById(R.id.txtfour);
        txt5=findViewById(R.id.txtfive);
        txt6=findViewById(R.id.txtsix);
        txt7=findViewById(R.id.txtseven);
        txt8=findViewById(R.id.txteight);

        if(tinyDB.getBoolean("ispremium"))hand.setVisibility(View.GONE);





        if(tinyDB.getBoolean("lighttheme")){
            //LiquidRadioButton savedevice,saveboth,sound1,sound2,sound3,sound4,nosound;
            //    LiquidRadioButton vazir,yekan,casabelan,dast,davat,free,rezvan,traffic,themel,themed;
            // txtactive,txthide,progtxt,fonttxt;
            container.setBackgroundColor(getResources().getColor(R.color.whitetrans));
            container.animate();
            txthide.setTextColor(getResources().getColor(R.color.likeblack));
            progtxt.setTextColor(getResources().getColor(R.color.likeblack));
            fonttxt.setTextColor(getResources().getColor(R.color.likeblack));
            savedevice.setTextColor(getResources().getColor(R.color.darktgray));
            saveboth.setTextColor(getResources().getColor(R.color.darktgray));
            sound2.setTextColor(getResources().getColor(R.color.darktgray));
            sound1.setTextColor(getResources().getColor(R.color.darktgray));
            sound3.setTextColor(getResources().getColor(R.color.darktgray));
            sound4.setTextColor(getResources().getColor(R.color.darktgray));
            nosound.setTextColor(getResources().getColor(R.color.darktgray));
            vazir.setTextColor(getResources().getColor(R.color.darktgray));
            yekan.setTextColor(getResources().getColor(R.color.darktgray));
            casabelan.setTextColor(getResources().getColor(R.color.darktgray));
            dast.setTextColor(getResources().getColor(R.color.darktgray));
            davat.setTextColor(getResources().getColor(R.color.darktgray));
            free.setTextColor(getResources().getColor(R.color.darktgray));
            rezvan.setTextColor(getResources().getColor(R.color.darktgray));
            traffic.setTextColor(getResources().getColor(R.color.darktgray));
            themed.setTextColor(getResources().getColor(R.color.darktgray));
            themel.setTextColor(getResources().getColor(R.color.darktgray));
            txt1.setTextColor(getResources().getColor(R.color.likeblack));
            txt2.setTextColor(getResources().getColor(R.color.darktgray));
            txt3.setTextColor(getResources().getColor(R.color.darktgray));
            txt4.setTextColor(getResources().getColor(R.color.likeblack));
            txt5.setTextColor(getResources().getColor(R.color.likeblack));
            txt6.setTextColor(getResources().getColor(R.color.likeblack));
            txt7.setTextColor(getResources().getColor(R.color.likeblack));
            txt8.setTextColor(getResources().getColor(R.color.likeblack));
            shareapp.setTextColor(getResources().getColor(R.color.darktgray));
            help.setTextColor(getResources().getColor(R.color.darktgray));
            //sharefile,restore,shareapp,backup,help,writetodev;
            sharefile.setBackground(getResources().getDrawable(R.drawable.backbuttontranslight));
            restore.setBackground(getResources().getDrawable(R.drawable.backbuttontranslight));
            backup.setBackground(getResources().getDrawable(R.drawable.backbuttontranslight));
            writetodev.setBackground(getResources().getDrawable(R.drawable.backbuttontranslight));
            sharefile.setTextColor(getResources().getColor(R.color.likeblack));
            restore.setTextColor(getResources().getColor(R.color.likeblack));
            backup.setTextColor(getResources().getColor(R.color.likeblack));
            writetodev.setTextColor(getResources().getColor(R.color.likeblack));
            hand.setImageResource(R.drawable.hand);

        }

        else {
            container.setBackgroundColor(getResources().getColor(R.color.mainback));
            container.animate();
            txthide.setTextColor(getResources().getColor(R.color.white));
            progtxt.setTextColor(getResources().getColor(R.color.white));
            fonttxt.setTextColor(getResources().getColor(R.color.white));
            savedevice.setTextColor(getResources().getColor(R.color.white));
            saveboth.setTextColor(getResources().getColor(R.color.white));
            sound2.setTextColor(getResources().getColor(R.color.white));
            sound1.setTextColor(getResources().getColor(R.color.white));
            sound3.setTextColor(getResources().getColor(R.color.white));
            sound4.setTextColor(getResources().getColor(R.color.white));
            nosound.setTextColor(getResources().getColor(R.color.white));
            vazir.setTextColor(getResources().getColor(R.color.white));
            yekan.setTextColor(getResources().getColor(R.color.white));
            casabelan.setTextColor(getResources().getColor(R.color.white));
            dast.setTextColor(getResources().getColor(R.color.white));
            davat.setTextColor(getResources().getColor(R.color.white));
            free.setTextColor(getResources().getColor(R.color.white));
            rezvan.setTextColor(getResources().getColor(R.color.white));
            traffic.setTextColor(getResources().getColor(R.color.white));
            themed.setTextColor(getResources().getColor(R.color.white));
            themel.setTextColor(getResources().getColor(R.color.white));
            txt1.setTextColor(getResources().getColor(R.color.white));
            txt2.setTextColor(getResources().getColor(R.color.white));
            txt3.setTextColor(getResources().getColor(R.color.white));
            txt4.setTextColor(getResources().getColor(R.color.white));
            txt5.setTextColor(getResources().getColor(R.color.white));
            txt6.setTextColor(getResources().getColor(R.color.white));
            txt7.setTextColor(getResources().getColor(R.color.white));
            txt8.setTextColor(getResources().getColor(R.color.white));
            shareapp.setTextColor(getResources().getColor(R.color.white));
            help.setTextColor(getResources().getColor(R.color.white));
            sharefile.setBackground(getResources().getDrawable(R.drawable.backbuttontrans));
            restore.setBackground(getResources().getDrawable(R.drawable.backbuttontrans));
            backup.setBackground(getResources().getDrawable(R.drawable.backbuttontrans));
            writetodev.setBackground(getResources().getDrawable(R.drawable.backbuttontrans));
            sharefile.setTextColor(getResources().getColor(R.color.white));
            restore.setTextColor(getResources().getColor(R.color.white));
            backup.setTextColor(getResources().getColor(R.color.white));
            writetodev.setTextColor(getResources().getColor(R.color.white));
            hand.setImageResource(R.drawable.handwhite);


        }
    }

}
