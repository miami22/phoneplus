package com.parhamcodeappsgmail.phoneplus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Uitem;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConDetail extends AppCompatActivity {

    ImageView image;
    CollapsingToolbarLayout colaptoolbar;
    int id;
    int typ;
    itema item;
    dbAdapter db;
    String name="",phone1="",phone2="",phone3="",insta="",teleg="",address="",date="",email="",info="",spdate;
    String avatar="";
    Bitmap bitavat;
    TextView num1,num2,num3,xinsta,xteleg,xaddress,xemail,xinfo,xsdate;
    TextView totin,totout;
    ViewStub s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16;
    ArrayList<ViewStub> stublist;
    TinyDB tinyDB;
    String totalout;
    String totalin;
    LinearLayout p1lay,p2lay,p3lay,instaly,emaillay,teleglay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_detail);

        db=new dbAdapter(this);
        tinyDB=new TinyDB(this);

        typ = Objects.requireNonNull(getIntent().getExtras()).getInt("type");
        if (typ==1){
            id = getIntent().getExtras().getInt("id");
            item=db.get‌‌ByIdFromContact(id+"");
            name=item.getFirstName();
            initvalues();
        }
        else if(typ==2){
             name = getIntent().getExtras().getString("name");
            item=db.GetContactByName(name);
            if (item==null){ }
            else {
                initvalues();
            }
        }









        image=findViewById(R.id.toolbarImage);
        if (bitavat!=null) image.setImageBitmap(bitavat);

        colaptoolbar=findViewById(R.id.collapsingToolbar);

        p1lay=findViewById(R.id.Imainconlay);
        p1lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (tinyDB.getBoolean("isdual"))alertcall(phone1);
              else makecall(phone1,0);
            }
        });
        p2lay=findViewById(R.id.Isecnumlay);
        p2lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone2.equals(""))alertsimple("شماره دوم وارد نشده !");
                else if (tinyDB.getBoolean("isdual"))alertcall(phone2);
                else makecall(phone2,0);
            }
        });
        p3lay=findViewById(R.id.Itherdnumlay);
        p3lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone3.equals(""))alertsimple("شماره سوم وارد نشده !");
                else if (tinyDB.getBoolean("isdual"))alertcall(phone3);
                else makecall(phone3,0);

            }
        });
        emaillay=findViewById(R.id.Iemaillay);
        emaillay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.equals(""))alertsimple("ایمیل مخاطب وارد نشده !");
                else emails(email);
            }
        });
        instaly=findViewById(R.id.Iinstalay);
        instaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insta.equals(""))alert("اینستاگرام",id);
                else instagerams(insta);
            }
        });

        teleglay=findViewById(R.id.Iteleglay);
        teleglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insta.equals(""))alert("تلگرام",id);
                else instagerams(teleg);
            }
        });

        num1=findViewById(R.id.Imainnum);
        num2=findViewById(R.id.Isecnum);
        num3=findViewById(R.id.Itherdnum);
        xinfo=findViewById(R.id.Idiscribe);
        xsdate=findViewById(R.id.Ispedate);
        xinsta=findViewById(R.id.Iinstagram);
        xteleg=findViewById(R.id.Itelegram);
        xaddress=findViewById(R.id.Iaddress);
        xemail=findViewById(R.id.Iemail);
        totin=findViewById(R.id.Itotalincall);
        totout=findViewById(R.id.Itotaloutcall);

        s1=findViewById(R.id.ilogstub1);
        s2=findViewById(R.id.ilogstub2);
        s3=findViewById(R.id.ilogstub3);
        s4=findViewById(R.id.ilogstub4);
        s5=findViewById(R.id.ilogstub5);
        s6=findViewById(R.id.ilogstub6);
        s7=findViewById(R.id.ilogstub7);
        s8=findViewById(R.id.ilogstub8);
        s9=findViewById(R.id.ilogstub9);
        s10=findViewById(R.id.ilogstub10);
        s11=findViewById(R.id.ilogstub11);
        s12=findViewById(R.id.ilogstub12);
        s13=findViewById(R.id.ilogstub13);
        s14=findViewById(R.id.ilogstub14);
        s15=findViewById(R.id.ilogstub15);
        s16=findViewById(R.id.ilogstub16);

        stublist=new ArrayList<>();
        stublist.addAll(Arrays.asList(s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16));
        setvalues();
        colaptoolbar.setTitle(name);
        getandsetlog();
        themeset();

    }

    void initvalues(){
         //name=item.getFirstName();
         phone1=item.getMnumber();
         phone2=item.getNumber2();
         phone3=item.getNumber3();
         insta=item.getInsta();
         teleg=item.getTeleg();
         address=item.getAddress();
         date=item.getDate();
         email=item.getEmail();
         info=item.getInfo();
         avatar=item.getAvatar();
         if (!avatar.equals(""))bitavat = Decodeimg.decodeBase64(avatar);
    }

    void setvalues(){
        num1.setText(phone1);
        num2.setText(phone2);
        num3.setText(phone3);
        xinsta.setText(insta);
        xinfo.setText(info);
        xaddress.setText(address);
        xteleg.setText(teleg);
        xsdate.setText(date);
        xemail.setText(email);

    }

    void getandsetlog(){
        ArrayList<Uitem> list=db.GetLogByName(name);
        //Message.message(this,list.size()+" listsize",1);
        int size;
        int totalincomsec=0;
        int totaloutgosec=0;
        if (list.size()<16)size=list.size();
        else size=16;
        for(int i=0;i<size;i++){
            Uitem item=list.get(i);
            ViewStub viewStub=stublist.get(i);
            View inflatedView = viewStub.inflate();
            TextView date=inflatedView.findViewById(R.id.Ilogdate1);
            TextView dur=inflatedView.findViewById(R.id.Ilogdur1);
            TextView time=inflatedView.findViewById(R.id.Ilogtime1);
            TextView type=inflatedView.findViewById(R.id.Ilogtype1);
            if(tinyDB.getBoolean("lighttheme")){
                date.setTextColor(getResources().getColor(R.color.darktgray));
                dur.setTextColor(getResources().getColor(R.color.darktgray));
                time.setTextColor(getResources().getColor(R.color.darktgray));
                type.setTextColor(getResources().getColor(R.color.darktgray));
            }
            ImageView image=inflatedView.findViewById(R.id.Ilogimg1);

            date.setText(item.getDate());
            dur.setText(item.getDur());
            time.setText(item.getTime());


            String ttt=item.getType();
            String calltype="";
            int dircode = Integer.parseInt(ttt);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    calltype = "OUTGOING";
                    type.setText("تماس خروجی");
                    totaloutgosec=totaloutgosec+Integer.parseInt(item.getSec());
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    calltype = "INCOMING";
                    type.setText("تماس ورودی");
                    totalincomsec=totalincomsec+Integer.parseInt(item.getSec());
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    calltype = "MISSED";
                    type.setText("تماس ناموفق");
                    break;
            }


            if (tinyDB.getBoolean("isdual")){
                String sim=item.getSim();
                if (sim.equals("1"))image.setImageResource(R.drawable.mcin1);
                else if (sim.equals("2"))image.setImageResource(R.drawable.mcin2);
                else if (sim.equals("3"))image.setImageResource(R.drawable.irancelln1);
                else if (sim.equals("4"))image.setImageResource(R.drawable.irancelln2);
                else if (sim.equals("5"))image.setImageResource(R.drawable.mcin);
                else if (sim.equals("6"))image.setImageResource(R.drawable.irancelln);
                else if (sim.equals("7"))image.setImageResource(R.drawable.sim11);
                else if (sim.equals("8"))image.setImageResource(R.drawable.sim22);

            }
            else {
                if (calltype.equals("OUTGOING"))image.setImageResource(R.drawable.callout);
                else if (calltype.equals("INCOMING"))image.setImageResource(R.drawable.callin);
                else if (calltype.equals("MISSED"))image.setImageResource(R.drawable.callmissed);
            }
        }

        totalin=formattedDuration(totalincomsec+"");
        totalout=formattedDuration(totaloutgosec+"");
        totin.setText(totalin);
        totout.setText(totalout);
    }

    public  String formattedDuration(String ss) {
        int dur = Integer.parseInt(ss);
        String timeString;
        int hours = dur / 3600;
        int minutes = (dur % 3600) / 60;
        int sec = (dur % 3600) % 60;
        if (dur == 0) {
            timeString = "صفر";
        } else if (hours == 0 && minutes != 0) {
            timeString = minutes + " دقیقه  " + sec + " ثانیه";
        } else if (minutes == 0) {
            timeString = sec + " ثانیه";
        } else {
            timeString = hours + " ساعت  " + minutes + " دقیقه  " + sec + " ثانیه";
        }
        return timeString;
    }

    void emails(String mail){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",mail, null));
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    void telegrams(String id){
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/"+id));
        this.startActivity(telegram);
    }

    void instagerams(String id){
        Uri uri = Uri.parse("http://instagram.com/_u/"+id);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(this, insta)){
            this.startActivity(insta);
        } else{
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+id)));
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    void alert(String title, final int id){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("آی دی "+title+" وارد نشده \n"+"میخوای آی دی وارد کنی؟");
        bld.setCancelable(true);
        //View view = ((DaggerActivity) getActivity()).getLayoutInflater().inflate(layoutResource, null, false);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent mIntent = new Intent(ConDetail.this, newcontact.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", id);
                mBundle.putInt("edit", 1);
                mBundle.putString("type", "li");
                mBundle.putString("cons", title);
                mIntent.putExtras(mBundle);
                ConDetail.this.startActivity(mIntent);

            }
        });
        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        bld.create().show();
    }

    void alertcall(String number){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("سیم کارت برای تماس :");
        bld.setCancelable(true);
        //View view = ((DaggerActivity) getActivity()).getLayoutInflater().inflate(layoutResource, null, false);
        bld.setNeutralButton("سیم 1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makecall(number,0);
            }
        });
        bld.setNegativeButton("سیم 2", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makecall(number,1);
            }
        });

        bld.create().show();
    }


    private void makecall(String number, int simnumber) {
        final String[] simSlotName = {
                "extra_asus_dial_use_dualsim",
                "com.android.phone.extra.slot",
                "slot",
                "simslot",
                "sim_slot",
                "subscription",
                "Subscription",
                "phone",
                "com.android.phone.DialingMode",
                "simSlot",
                "slot_id",
                "simId",
                "simnum",
                "phone_type",
                "slotId",
                "com.android.phone.extra",
                "slotIdx"};
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 && simnumber == 0 && tinyDB.getBoolean("isdual"))
            tinyDB.putString("callsimid", "7");
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1 && simnumber == 1 && tinyDB.getBoolean("isdual"))
            tinyDB.putString("callsimid", "8");

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        /*intent.putExtra("com.android.phone.extra.slot", simnumber); //For sim 1
        intent.putExtra("simSlot", simnumber); //For sim 1
        intent.putExtra("com.android.phone.extra", simnumber); //For sim*/
        intent.putExtra("Cdma_Supp", true);
        for (String s : simSlotName) intent.putExtra(s, number);
//        context.startActivity(intent);


        Log.i("Test","Build Version is "+Build.VERSION.SDK_INT);
        Log.i("Test","M Build Version is "+Build.VERSION_CODES.M);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//            for (String s : simSlotName) intent.putExtra(s, number);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", (Parcelable) " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");*/
            startActivity(intent);
        }
        else {
            TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
                if (simnumber == 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));

                } else {
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

                }
                startActivity(intent);
            }}

    }


    void alertsimple(String txt){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(ConDetail.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(ConDetail.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage(txt);
        bld.setCancelable(true);
        //View view = ((DaggerActivity) getActivity()).getLayoutInflater().inflate(layoutResource, null, false);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        bld.create().show();
    }

    void themeset(){
        TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;
        CoordinatorLayout container=findViewById(R.id.condetcontainer);
        t1=findViewById(R.id.tt1);
        t2=findViewById(R.id.tt2);
        t3=findViewById(R.id.tt3);
        t4=findViewById(R.id.tt4);
        t5=findViewById(R.id.tt5);
        t6=findViewById(R.id.tt6);
        t7=findViewById(R.id.tt7);
        t8=findViewById(R.id.tt8);
        t9=findViewById(R.id.tt9);
        t10=findViewById(R.id.tt10);
        t11=findViewById(R.id.tt11);
        t12=findViewById(R.id.tt12);
        if(tinyDB.getBoolean("lighttheme")){
            //TextView num1,num2,num3,xinsta,xteleg,xaddress,xemail,xinfo,xsdate;
            //    TextView totin,totout;
            num1.setTextColor(getResources().getColor(R.color.darktgray));
            num2.setTextColor(getResources().getColor(R.color.darktgray));
            num3.setTextColor(getResources().getColor(R.color.darktgray));
            xinfo.setTextColor(getResources().getColor(R.color.darktgray));
            xinsta.setTextColor(getResources().getColor(R.color.darktgray));
            xemail.setTextColor(getResources().getColor(R.color.darktgray));
            xaddress.setTextColor(getResources().getColor(R.color.darktgray));
            xteleg.setTextColor(getResources().getColor(R.color.darktgray));
            xinsta.setTextColor(getResources().getColor(R.color.darktgray));
            totin.setTextColor(getResources().getColor(R.color.darktgray));
            totout.setTextColor(getResources().getColor(R.color.darktgray));
            t1.setTextColor(getResources().getColor(R.color.likeblack));
            t2.setTextColor(getResources().getColor(R.color.likeblack));
            t3.setTextColor(getResources().getColor(R.color.likeblack));
            t4.setTextColor(getResources().getColor(R.color.likeblack));
            t5.setTextColor(getResources().getColor(R.color.likeblack));
            t6.setTextColor(getResources().getColor(R.color.likeblack));
            t7.setTextColor(getResources().getColor(R.color.likeblack));
            t8.setTextColor(getResources().getColor(R.color.likeblack));
            t9.setTextColor(getResources().getColor(R.color.likeblack));
            t10.setTextColor(getResources().getColor(R.color.likeblack));
            t11.setTextColor(getResources().getColor(R.color.likeblack));
            t12.setTextColor(getResources().getColor(R.color.likeblack));
            container.setBackgroundColor(getResources().getColor(R.color.whitetrans));

        }
        else {
            num1.setTextColor(getResources().getColor(R.color.white));
            num2.setTextColor(getResources().getColor(R.color.white));
            num3.setTextColor(getResources().getColor(R.color.white));
            xinfo.setTextColor(getResources().getColor(R.color.white));
            xinsta.setTextColor(getResources().getColor(R.color.white));
            xemail.setTextColor(getResources().getColor(R.color.white));
            xaddress.setTextColor(getResources().getColor(R.color.white));
            xteleg.setTextColor(getResources().getColor(R.color.white));
            xinsta.setTextColor(getResources().getColor(R.color.white));
            totin.setTextColor(getResources().getColor(R.color.white));
            totout.setTextColor(getResources().getColor(R.color.white));
            t1.setTextColor(getResources().getColor(R.color.white));
            t2.setTextColor(getResources().getColor(R.color.white));
            t3.setTextColor(getResources().getColor(R.color.white));
            t4.setTextColor(getResources().getColor(R.color.white));
            t5.setTextColor(getResources().getColor(R.color.white));
            t6.setTextColor(getResources().getColor(R.color.white));
            t7.setTextColor(getResources().getColor(R.color.white));
            t8.setTextColor(getResources().getColor(R.color.white));
            t9.setTextColor(getResources().getColor(R.color.white));
            t10.setTextColor(getResources().getColor(R.color.white));
            t11.setTextColor(getResources().getColor(R.color.white));
            t12.setTextColor(getResources().getColor(R.color.white));
            container.setBackgroundColor(getResources().getColor(R.color.mainback));
        }
    }
}
