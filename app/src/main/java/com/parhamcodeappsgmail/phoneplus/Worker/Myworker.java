package com.parhamcodeappsgmail.phoneplus.Worker;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.target.NotificationTarget;
import com.parhamcodeappsgmail.phoneplus.BuildConfig;
import com.parhamcodeappsgmail.phoneplus.ConDetail;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.GlideApp;
import com.parhamcodeappsgmail.phoneplus.MainActivity;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Setting;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Tools.backupDB;
import com.parhamcodeappsgmail.phoneplus.Tools.shamsiDate;
import com.parhamcodeappsgmail.phoneplus.util.IabHelper;
import com.parhamcodeappsgmail.phoneplus.util.IabResult;
import com.parhamcodeappsgmail.phoneplus.util.Inventory;
import com.parhamcodeappsgmail.phoneplus.util.Purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class Myworker extends Worker {
    private Context context;
    private TinyDB tinyDB;
    private IabHelper mHelper;
    private dbAdapter db;
    private Calendar now;
    private Bitmap logo;
    private String store;
    private String conname;
    private String key;


    public Myworker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        tinyDB=new TinyDB(context);


        if (BuildConfig.FLAVOR.equals("bazar")) {
            store ="com.farsitel.bazaar";
            key=BuildConfig.bazarkey;
        } else {
            key= BuildConfig.myketkey;
            store ="ir.mservices.market";
        }

        db=new dbAdapter(context);
        now = Calendar.getInstance();
        logo= BitmapFactory.decodeResource(context.getResources(), R.drawable.noticon);


    }


    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        Log.i("Test","worker...");
        if(isPackageExisted(store)&&!tinyDB.getBoolean("ispremium"))checksuscribe();
        checkspesialdates();
        mainwork();

        if(tinyDB.getBoolean("autobackup")&&tinyDB.getBoolean("ispremium")) backupDB.exportDb();
        int countday = tinyDB.getInt("daycount") + 1;
        tinyDB.putInt("daycount", countday);
        if (countday >6)tinyDB.putBoolean("addenabled",true);
        if ( countday ==3|| countday ==6)tinyDB.putBoolean("informprim",true);

        return Result.success();
    }



    private void mainwork(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request=new JsonArrayRequest(BuildConfig.api, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // Message.message(context,"connect",1);
                try {
                    JSONObject jsonObject=response.getJSONObject(0);
                    int addver=jsonObject.getInt("hasadd");
                    int appup=jsonObject.getInt("appup");
                    //Message.message(context,tinyDB.getInt("addver")+ addver+"  onlineadd",2);
                    Log.i("Test","addver "+addver +"   appup"+appup);

                    if (addver>tinyDB.getInt("addver") && tinyDB.getInt("addnotrep")<3 ){
                        //Message.message(context,"enter",1);
                        Log.i("MainActivity", "enteradver: ");
                        String title=jsonObject.getString("title");
                        String body=jsonObject.getString("body");
                        String url=jsonObject.getString("imageurl");
                        String intent=jsonObject.getString("intent");
                        imagenotif(title,body,url,intent,1004);

                        int rep=tinyDB.getInt("addnotrep");
                        tinyDB.putInt("addnotrep",rep+1);
                        if (tinyDB.getInt("addnotrep")==2){
                            tinyDB.putInt("addver",addver);
                            tinyDB.putInt("addnotrep",0);
                        }

                    }
                    Log.i("Test","app ver is "+tinyDB.getInt("appver"));
                    if (appup>tinyDB.getInt("appver") && tinyDB.getInt("appuprep")<2){
                        Log.i("Test","entered notif");
                        String title=jsonObject.getString("messtitle");
                        String body=jsonObject.getString("messbody");
                        notifPoint(title,body,1002,logo);
                        int rep=tinyDB.getInt("appuprep");
                        tinyDB.putInt("appuprep",rep+1);

                        if (tinyDB.getInt("appuprep")==2){
                            tinyDB.putInt("appver",appup);
                            tinyDB.putInt("appuprep",0);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Message.message(context,e+"",1);

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    //Message.message(context,"noConnection Error!",1);
                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    //Message.message(context,"AuthFailure Error!",1);

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    //Message.message(context,"Server Error!",1);

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    //Message.message(context,"Network Error!",1);

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    //Message.message(context,"noConnection Error!",1);

                }


            }
        });

        requestQueue.add(request);
    }

    private Notification notifPoint(String title, String text, int id, Bitmap largeicon){
        Intent intent=new Intent();

        if (id==1002){
            intent = new Intent(Intent.ACTION_VIEW);
            if (BuildConfig.FLAVOR.equals("bazar")){
                intent.setData(Uri.parse("bazaar://details?id=com.parhamcodeappsgmail.phoneplus"));
                intent.setPackage("com.farsitel.bazaar");
            }
            else {
                intent.setData(Uri.parse("https://myket.ir/app/com.parhamcodeappsgmail.phoneplus"));
                intent.setPackage("ir.mservices.market");
            }

        }
        else if (id==1005){

            Intent mIntent = new Intent(context, ConDetail.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("name",conname);
            mBundle.putInt("type",2);
            mIntent.putExtras(mBundle);
            context.startActivity(mIntent);
        }




        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "phoneplus")
                .setSmallIcon(R.drawable.notificon2)
                .setLargeIcon(largeicon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
        return builder.build();
    }



    private void imagenotif(String title,String body,String url,String intent,int id){

        Intent intentimg = new Intent(Intent.ACTION_VIEW);
        intentimg.setData(Uri.parse("bazaar://details?id="+intent));
        intentimg.setPackage("com.farsitel.bazaar");
        intentimg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentimg, 0);

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_notification);

        remoteViews.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.icn_round);

        remoteViews.setTextViewText(R.id.remoteview_notification_headline, title);
        remoteViews.setTextViewText(R.id.remoteview_notification_short_message, body);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);}


// build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.notificon2)
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                // .setSound( Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notifa))
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setPriority( NotificationCompat.PRIORITY_MIN);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
        Notification notification=builder.build();
// set big content view for newer androids

        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                R.id.remoteview_notification_icon,
                remoteViews,
                notification,
                id);

        GlideApp
                .with(context.getApplicationContext())
                .asBitmap()
                .load(url)
                .into(notificationTarget);
    }

    private void checksuscribe() {
        mHelper=new IabHelper(context,key);
        String key="";
        if (BuildConfig.FLAVOR.equals("bazar"))key=BuildConfig.bazarkey;
        else if (BuildConfig.FLAVOR.equals("myket"))key=BuildConfig.myketkey;

        mHelper = new IabHelper(context, key);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.

                if (mHelper == null) return;
                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                // dialog.dismiss();
                if(!tinyDB.getBoolean("labasync")){
                    tinyDB.putBoolean("labasync",true);
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            }

        });

    }

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            tinyDB.putBoolean("labasync",false);
            if (result.isFailure()) {
            }
            else {
                // does the user have the premium upgrade?


                if (inventory.hasPurchase("premium")) {
                    tinyDB.putBoolean("ispremium",true);
                }
                else {
                    tinyDB.putBoolean("ispremium",false);
                }





                // Message.message(context,"subscribe calculated",2);
                // update UI accordingly

            }

        }
    };


    private void checkspesialdates(){
        List<itema> mylist= db.getDataContact2();
        int size=mylist.size();
        int year=now.get(Calendar.YEAR);
        int mounth=now.get(Calendar.MONTH)+1;
        int day=now.get(Calendar.DAY_OF_MONTH);
        shamsiDate dd=new shamsiDate();
        dd.GregorianToPersian(year,mounth,day);
        String shamsinow=dd.toString();
        shamsinow = shamsinow.replace("-", "");
        int shamsnowint=Integer.parseInt(shamsinow);
        for (int i=0;i<size;i++){
            itema item=mylist.get(i);
            String date=item.getDate();
            int dateint=0;
            if (!date.equals("")){
                date = date.replace("-", "");
                dateint=Integer.parseInt(date);
            }

            if (dateint==shamsnowint){
                String name=item.getFirstName();
                conname=name;
                String title="تلفن+ :"+item.getDatetitle();

                String avat=item.getAvatar();
                Bitmap avatar=null;
                if (!avat.equals(""))avatar= Decodeimg.decodeBase64(avat);
                Bitmap lastbit=null;
                if (avatar==null)lastbit=BitmapFactory.decodeResource(context.getResources(), R.drawable.personflat);
                else lastbit=avatar;
                notifPoint(title,"یادآوری روز خاص برای مخاطب: "+name,i+1005,lastbit);
            }
        }
    }



    private boolean isPackageExisted(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }




}


