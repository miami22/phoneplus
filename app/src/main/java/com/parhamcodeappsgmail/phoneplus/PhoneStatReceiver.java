package com.parhamcodeappsgmail.phoneplus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.Log.recentconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Uitem;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Tools.shamsiDate;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.parhamcodeappsgmail.phoneplus.MainActivity.active;

public class PhoneStatReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStatReceiver";

    private static boolean incomingFlag = false;

    private static String incoming_number = null;

    private static final String EXTRA_SIM_STATE = "ss";
    private static  String callingsim="0";
    boolean logcounted=false;
    TinyDB tinyDB;
    dbAdapter db;
    ArrayList<String> blacklist;
    ITelephony telephonyService;
    TelephonyManager tm;
    Context ctx;
    AudioManager audioManager;
    private Calendar now;


    @Override

    public void onReceive(Context context, Intent intent) {
        tinyDB = new TinyDB(context);
        db=new dbAdapter(context);
        ctx=context;
        now = Calendar.getInstance();
        Handler mainHandler = new Handler(getMainLooper());
        tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        blacklist=tinyDB.getListString("blacklist");
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            mainHandler.post(new Runnable() {
                @Override

                public void run() {
                    if(tinyDB.getBoolean("appisactive"))MainActivity.simcheck(context);
                    else tinyDB.putBoolean("pendingsim",true);

                }
            });


        }


        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            mainHandler.post(new Runnable() {
                @SuppressLint("HardwareIds")
                @Override

                public void run() {
                    // Do your stuff here related to UI, e.g. show toast
                    // Message.message(context, "state changed", 2);

                    String serial="";
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        serial = tm.getSimSerialNumber();
                    }

                    Log.i("MainActivity", "serial: "+serial);

                   // Message.message(context,serial,2);
                    //MainActivity.detectcallingsim(serial,context);
                    if (serial!=null)callingsim=getcallingsim(serial);
                    else callingsim="";
                    tinyDB.putString("callsimid",callingsim);


                 }
             });
             switch (tm.getCallState()) {

                 case TelephonyManager.CALL_STATE_RINGING:{

                     incomingFlag = true;
                     if (!logcounted){
                         tinyDB.putInt("missedlogcount",tinyDB.getInt("missedlogcount")+1);
                         logcounted=true;
                     }
                     incoming_number = intent.getStringExtra("incoming_number");
                     if (incoming_number==null)incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                     if(incoming_number==null)incoming_number="000000000000";
                     boolean isblack=false;
                     if (blacklist!=null && !blacklist.isEmpty()){
                         for (int i=0;i<blacklist.size();i++){
                             if (incoming_number!=null){
                                 if (incoming_number.contains(blacklist.get(i))){
                                     //audioManager.setStreamMute(AudioManager.STREAM_RING,  true);
                                     isblack=true;
                                     rejectCall();
                                 }
                             }

                         }
                     }

                     /*if (incoming_number.contains("9196112711")){
                         mainHandler.postDelayed(new Runnable() {
                             @Override

                             public void run() {
                                 rejectCall();
                                 new CountDownTimer(4000,1000){

                                     @Override
                                     public void onTick(long l) {

                                     }

                                     @Override
                                     public void onFinish() {
                                         Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "09196112711"));
                                         intent.putExtra("com.android.phone.extra.slot", 0); //For sim 1
                                         intent.putExtra("simSlot", 0); //For sim 1
                                         context.startActivity(intent);
                                     }
                                 }.start();


                             }},5000);
                     }
*/
                     Log.i(TAG, "RINGING :" + incoming_number);

                     if (incoming_number!=null&&incoming_number.startsWith("+98")){incoming_number =incoming_number.replace("+98", "0");}
                     String name=db.getNamefromContact(incoming_number);
                     if (name==null)name=incoming_number;
                     ArrayList<Uitem> list=new ArrayList<>();
                            if (name!=null) list=db.GetLogByName(name);

                     if(list!=null && !list.isEmpty()){
                         Uitem item=list.get(0);

                         String exdate=item.getPrimdate();
                         Date now= Calendar.getInstance().getTime();
                         long nowsec=now.getTime();
                         long exsec=Long.parseLong(exdate);

                         Log.i("Test","no sec= "+nowsec+"\n exse = "+exsec);
                         String show="";

                         long def=(nowsec-exsec)/1000;

                         if(exdate.equals(""))show="شماره جدید";
                         else if (def<3600)   show="آخرین تماس "+def/60+" دقیقه پیش";
                         else if (def<86400)   show="آخرین تماس "+def/3600+" ساعت پیش";
                         else     show="آخرین تماس "+def/86400+" روز پیش";


                         /*int shamsiYear=0;
                         int shamsiMounth=0;
                         int shamsiDay=0;

                         if (exdate==""){
                             shamsiYear = Integer.parseInt(exdate.substring(0, 4));
                             shamsiMounth = Integer.parseInt(exdate.substring(5, 7));
                             shamsiDay = Integer.parseInt(exdate.substring(8, 10));
                         }
*/
//                         List<Integer> datelist=returnnow();

                        /* if(exdate.equals(""))show="شماره جدید";
                         else if(shamsiYear!=datelist.get(0))show="آخرین تماس "+(datelist.get(0)-shamsiYear)+" سال پیش";
                         else if (shamsiDay!=datelist.get(2)){
                             if (datelist.get(2)-shamsiDay==1){
                                 show="آخرین تماس دیروز";
                             }
                             else {
                                 int a=((datelist.get(1)-shamsiMounth)*30)+(datelist.get(2)-shamsiDay);
                                 show="آخرین تماس "+a+" روز پیش";
                             }

                         }
                         else show="آخرین تماس امروز";*/

                         /*Log.d("rec",shamsiYear+"   shamsiyear \n"+shamsiMounth+"  shamsimounth\n"+shamsiDay+"  shamsiday");
                         Log.d("rec","\n\n\n"+datelist.get(0)+"   listyear \n"+datelist.get(1)+"  listmounth\n"+datelist.get(2)+"  listday");*/

                         String black="";
                         if(isblack)black="\n در لیست سیاه";
                         String finalShow = show;
                         String finalBlack = black;
                         mainHandler.postDelayed(new Runnable() {
                             @SuppressLint("SetTextI18n")
                             @Override

                             public void run() {
                                 LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                 View layout = inflater.inflate(R.layout.custom_toast, null);
                                 TextView tv = (TextView) layout.findViewById(R.id.txtvw);
                                 tv.setText(finalShow+ finalBlack);
                                 Toast toast = new Toast(context);
                                 toast.setDuration(Toast.LENGTH_LONG);
                                 toast.setView(layout);
                                 toast.show();
                                 //Message.message(context, finalShow+ finalBlack,2);

                             }},1000);
                     }






                 }


                     break;

                 case TelephonyManager.CALL_STATE_OFFHOOK:

                     if (incomingFlag) {

                         Log.i(TAG, "incoming ACCEPT :" + incoming_number);

                     }

                     break;


                 case TelephonyManager.CALL_STATE_IDLE:

                     if (incomingFlag) {

                         Log.i(TAG, "incoming IDLE");

                     }

                     break;
        }
            }

    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    private String getcallingsim(String id){
        String res="0";
        String sim1id=tinyDB.getString("sim1serial");
        String sim1op=tinyDB.getString("sim1op");
        if (tinyDB.getBoolean("isdual")&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&tinyDB.getBoolean("simopok")){

            String sim2id=tinyDB.getString("sim2serial");
            String sim2op=tinyDB.getString("sim2op");
            if(tinyDB.getBoolean("sameop")){
                if(sim1op.equals("IR-MCI")||sim1op.equals("IR-TCI")){
                    if(id.contains(sim1id))res="1";
                    else res="2";
                }
                else if (sim1op.equals("Irancell")){
                    if(id.contains(sim1id))res="3";
                    else res="4";
                }
            }
            else {
                if (id.contains(sim1id)){
                    //  Log.i("MainActivity", "id:"+id+"    sim1id"+sim1id);
                    if(sim1op.equals("IR-MCI")||sim1op.equals("IR-TCI"))res="1";
                    else res="3";
                }
                else if (id.contains(sim2id)){
                    // Log.i("MainActivity", "id:"+id+"    sim1id"+sim2id);
                    if(sim2op.equals("IR-MCI")||sim1op.equals("IR-TCI"))res="2";
                    else res="4";
                }
            }
        }
        else if(tinyDB.getBoolean("isdual")&&!tinyDB.getBoolean("simopok")&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            String sim2id=tinyDB.getString("sim2serial");
            if(id.contains(sim1id))res="7";
            else if(id.contains(sim2id))res="8";

        }

        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1&&tinyDB.getBoolean("isdual")){
            res=tinyDB.getString("callsimid");
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1&&!tinyDB.getBoolean("isdual")) {
            if(sim1op.equals("IR-MCI")||sim1op.equals("IR-TCI"))res="5";
            else if (sim1op.equals("Irancell"))res="6";
        }
        //Log.i("MainActivity", "result"+res);
        return res;
    }

    public String getsimnumber(){
        return callingsim;
    }

    private void rejectCall(){

        Handler mainHandler = new Handler(getMainLooper());
        try {

            // Get the getITelephony() method
            Class<?> classTelephony = Class.forName(tm.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            // Disable access check
            method.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(tm);
            // Get the endCall method from ITelephony
            Class<?> telephonyInterfaceClass =Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }



    public List<Integer> returnnow() {
        int dayOfweek=now.get(Calendar.DAY_OF_WEEK);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);

        String days[]={"یکشنبه","دوشنبه","سه شنبه","چهارشنبه","پنجشنبه","جمعه","شنبه"};
        String dayNow=days[dayOfweek-1];

        shamsiDate dd=new shamsiDate();
        dd.GregorianToPersian(year,month,day);
        String shamsi=dd.toString();
        String shamsiYear = shamsi.substring(0, 4);
        String shamsiMounth = shamsi.substring(5,7);
        String shamsiDay = shamsi.substring(8,10);
        List<Integer> res=new ArrayList<>();
        res.add(Integer.parseInt(shamsiYear));
        res.add(Integer.parseInt(shamsiMounth));
        res.add(Integer.parseInt(shamsiDay));
        return res;

    }


}



