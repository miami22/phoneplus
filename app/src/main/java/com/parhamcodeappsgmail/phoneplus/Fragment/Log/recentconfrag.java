package com.parhamcodeappsgmail.phoneplus.Fragment.Log;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.SimpleCursorAdapter;

import com.parhamcodeappsgmail.phoneplus.DI.DaggeractivityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.DaggerfragmentComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityModule;
import com.parhamcodeappsgmail.phoneplus.DI.fragmentModule;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.RecyclerClick_Listener;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Uitem;
import com.parhamcodeappsgmail.phoneplus.RecyclerTouchListener;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Setting;
import com.parhamcodeappsgmail.phoneplus.Tools.CitemTouchHelper;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.Snacktoast;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Tools.shamsiDate;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.parhamcodeappsgmail.phoneplus.Fragment.Log.Toolbar_ActionMode_CallbackLog.menutxtlog;


/**
 * A simple {@link Fragment} subclass.
 */
public class recentconfrag extends Fragment implements RecyclerItemTouchHelperlog.RecyclerItemTouchHelperListener {

    @Inject TinyDB tinyDB;
    @Inject dbAdapter db;
    @Inject Context context;
    private RecyclerView recycle;
    private LinearLayoutManager mLayoutManager;
    private ConstraintLayout bottomlay;
    private  recycleAdapterlog adapter;
    private ActionMode mActionMode;
    CallLogChangeObserverClass callLogchangeevents = null;
    int iscreated=0;
    private TabLayout tabLayout;
    private Calendar now;
    public static boolean swipedlog = false;
    private ConstraintLayout containerM;
    private  List<itemlog> mainlist;



    public recentconfrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaggerfragmentComponent.builder().activityComponent(DaggeractivityComponent.builder().activityModule(new activityModule((AppCompatActivity)context)).build()).fragmentModule(new fragmentModule(this)).build()
                .inject(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recentconfrag, container, false);
        recycle = view.findViewById(R.id.recyclelog);
        bottomlay = ((AppCompatActivity)context).findViewById(R.id.bottomlayout);
        bottomlay.setVisibility(View.VISIBLE);
        tabLayout=((AppCompatActivity)context).findViewById(R.id.tablayoutC);
        containerM=((AppCompatActivity)context).findViewById(R.id.maincontainer);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Message.message(getActivity(),"onCreate",1);
        super.onCreate(savedInstanceState);

        callLogchangeevents = new CallLogChangeObserverClass(new Handler());
       context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true,
                callLogchangeevents);
        now = Calendar.getInstance();
        if (!tinyDB.getBoolean("firstreadlog"))writelogtoDB(context);
        else tinyDB.putBoolean("firstreadlog",false);
    }


    @Override
    public void onPause() {
        super.onPause();
        //Message.message(getActivity(),"OnPause",1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.getContentResolver().
                unregisterContentObserver(callLogchangeevents);
    }


    @Override
    public void onResume() {
        super.onResume();
        //Message.message(getActivity(),"onResume",1);

        if (tinyDB.getInt("uplog")==1){
            tinyDB.putInt("uplog",0);
            iscreated=0;
        }
         if(iscreated==0)  {
            //getCallDetails(context,0);
        }
        else if (iscreated==1)iscreated=0;
/*

 */
        if(tinyDB.getBoolean("savefromlog")){
            tinyDB.putBoolean("savefromlog",false);
            getCallDetails(context,0);
        }
        if(tinyDB.getBoolean("fontchange2")){
            tinyDB.putBoolean("fontchange2",false);
            getCallDetails(context,0);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implementRecyclerViewClickListeners();
        getCallDetails(context,0);
        iscreated=1;
    }


    public  void getCallDetails(Context context,int tipo) {

         if (tipo==0){

             new Thread(new Runnable() {
            @Override
            public void run() {
                mainlist=new ArrayList<>();
                Cursor cursor = context.getContentResolver().query(
                        CallLog.Calls.CONTENT_URI,
                        null, null, null, CallLog.Calls.DATE + " DESC");
//                CursorLoader cl = new CursorLoader(context, CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

                assert cursor != null;

                int simid=0;
                int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    simid = cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
                }

                int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                int id = cursor.getColumnIndex(CallLog.Calls._ID);
                String tempnum="";
                int counter = 1;
                String tempdate="";
                String predate=now.get(Calendar.DATE)+"";
                String tsim="0";
                String temptype="0";
                String temptime="0";
                String tempdur="0";

                while (cursor.moveToNext()) {
                    itemlog item;
                    int idd=cursor.getInt(id);
                    String phNumber = cursor.getString(number);
                    if (phNumber.startsWith("+98")){phNumber =phNumber.replace("+98", "0");}
                    String callType = cursor.getString(type);
                    String callDate = cursor.getString(date);
                    String sid="0";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String tempsim=cursor.getString(simid);
                        if (tempsim!=null) sid=getcallingsim(tempsim);
                       /* Log.i("MainActivity", "logsimid: "+tempsim);
                        Log.i("MainActivity", "sim1 serial: "+ tinyDB.getString("sim1serial"));
                        Log.i("MainActivity", "sim1 serial: "+ tinyDB.getString("sim2serial"));*/

                    }
                    else sid="0";

                    long seconds=Long.parseLong(callDate);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String tim = formatter.format(new Date(seconds));
                    String dateString=tim.substring(0,10);

                    Date callDayTime = new Date(Long.parseLong(callDate));
                    String callDuration = cursor.getString(duration);

                    // String Newshdate=shamsidate(callDate);
                    String Newweekday=dayofweek(callDayTime);
                    String Newduration=formattedDuration(callDuration);
//                    Log.i("Test","new duration is "+Newduration);
                    ArrayList res=setseperator(callDate,predate,Newweekday,false);
                    String SHAMSI= (String) res.get(1);
                    String SEPERATOR= (String) res.get(0);
                    String TIME= (String) res.get(2);



///////////////////////////////
                    /*String name=db.getNamefromContact(phNumber);
                    if(name==null)name=phNumber;
                    String durr="";
                    int caldurint=Integer.parseInt(callDuration);
                    if (caldurint<60)durr=caldurint+" ثانیه ";
                    else durr= caldurint/60+" دقیقه ";
                    if (nolimit)db.insertdataLog(name,callDate,SHAMSI,TIME,durr,callType,sid,callDuration);*/
///////////////////
//                    Log.i("Test","time is "+TIME);
//                    Log.i("Test","number is "+phNumber);

//&&temptype.equals(callType)
                    item = new itemlog(idd, phNumber, callDate, Newweekday, Newduration, callType,counter+"",sid,SEPERATOR,SHAMSI,TIME);

                    if (phNumber.equals(tempnum)&&dateString.equals(tempdate)&&tsim.equals(sid)&&mainlist.size()>0){
                        counter++;
                        mainlist.get(mainlist.size()-1).setCount(counter+"");
                        mainlist.get(mainlist.size()-1).setDuration(tempdur);
                        mainlist.get(mainlist.size()-1).setType(temptype);
                        mainlist.get(mainlist.size()-1).setTime(temptime);

                        /*mainlist.get(mainlist.size()-1).setSimnum(sid);
                        mainlist.get(mainlist.size()-1).setDuration(Newduration);
                        mainlist.get(mainlist.size()-1).setType(callType);
                        mainlist.get(mainlist.size()-1).setCount(counter+"");
                        mainlist.get(mainlist.size()-1).setTime(TIME);
                        if (phNumber.equals("09191423299")){
                            Log.i("Test","item num is "+(mainlist.size()-1));
                            Log.i("Test","time is "+TIME);
                        }
                        item = new itemlog(idd, phNumber, callDate, Newweekday, tempdur, temptype,counter+"",tsim,SEPERATOR,SHAMSI,temptime);
                        mainlist.remove(mainlist.size()-1);
                        mainlist.add(item);*/

                        // Message.message(getActivity(),dateString,1);

                    }
//if(!phNumber.equals(tempnum)||!dateString.equals(tempdate))
                    else {

//                        item = new itemlog(idd, phNumber, callDate, Newweekday, Newduration, callType,counter+"",sid,SEPERATOR,SHAMSI,TIME);
//                        item.setCount(counter+"");
                        mainlist.add(item);
                        counter=1;
                        tsim=sid;
                        temptype=callType;
                        tempnum=phNumber;
                        tempdate=dateString;
                        temptime=TIME;
                        tempdur=Newduration;
                        predate=callDate;
                        // if (cursor.isLast())mylist.add(item);

                    }


                    //db.insertdataNLog(mainlist);
                }


                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        createRecyclelist();
                    }
                });

                cursor.close();

            }
        }).start();}




        else if (tipo==1){
                 new Thread(new Runnable() {
                     @Override
                     public void run() {


                         Cursor cursor = context.getContentResolver().query(
                                 CallLog.Calls.CONTENT_URI,
                                 null, null, null, CallLog.Calls.DATE + " DESC");

                         assert cursor != null;
                         int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                         int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
                         int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                         int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                         int id = cursor.getColumnIndex(CallLog.Calls._ID);
                         int counter = 1;

                         itemlog item1;
                         itemlog item2;
                             cursor.moveToFirst();

                                 int idd=cursor.getInt(id);
                                 String phNumber = cursor.getString(number);
                         if (phNumber.startsWith("+98")){phNumber =phNumber.replace("+98", "0");}
                         String callType = cursor.getString(type);
                                 String callDate = cursor.getString(date);

                         String sid="";
                         String tsim="";
                         String temptype="";
                         String tempdate="0";
                         String temptime="0";
                         String tempdur="0";
                         int simid=0;
                         long seconds=Long.parseLong(callDate);
                                 @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                 String tim = formatter.format(new Date(seconds));
                                 String dateString=tim.substring(0,10);
                                 String day1=tim.substring(0,2);
                                 String timee=tim.substring(10,16);
                                 Date callDayTime = new Date(Long.parseLong(callDate));
                                 String callDuration = cursor.getString(duration);
                                 String Newshdate=shamsidate(callDate);
                                 String Newweekday=dayofweek(callDayTime);
                                 String Newduration=formattedDuration(callDuration);
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                             simid = cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
                         }

                                 item1=mainlist.get(0);
                         temptype=item1.type;
                         tsim=item1.simnum;
                         temptime=item1.time;
                         tempdur=Newduration;

                         Log.i("MainActivity", "dur:"+duration+"     newdur:"+Newduration);
                         ArrayList res=setseperator(callDate,item1.getdate(),Newweekday,true);
                         String SHAMSI= (String) res.get(1);
                         String SEPERATOR= (String) res.get(0);
                         Log.e("log",SEPERATOR);

                         String TIME= (String) res.get(2);
                         String exacttime=(String) res.get(3);
                         String lasttime=tinyDB.getString("lastcalltime");

                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                             String tempsim=cursor.getString(simid);
                             if (tempsim!=null) sid=getcallingsim(tempsim);
                             /*Log.i("MainActivity", "logsimid: "+tempsim);
                             Log.i("MainActivity", "sim1 serial: "+ tinyDB.getString("sim1serial"));
                             Log.i("MainActivity", "sim1 serial: "+ tinyDB.getString("sim2serial"));*/


                         }
                         else sid="0";

                                 item2=new itemlog(idd, phNumber, callDate, Newweekday, Newduration, callType,counter+"",sid,SEPERATOR,SHAMSI,TIME);

                         Log.i("Test", "call type: "+ callType);
                         @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                         long seconds2=Long.parseLong(item1.getdate());
                         long nowsec=Calendar.getInstance().getTime().getTime();
                         String timnow = formatter2.format(new Date(nowsec));
                         dateString=timnow.substring(0,10);

                         String tim2 = formatter2.format(new Date(seconds2));
                         String dateString2=tim2.substring(0,10);
                         String day2=tim2.substring(0,2);




                             if(!lasttime.equals(exacttime)){
//                                 Log.i("Test", "lastTIME: "+lasttime+"     exaxtTIME: "+exacttime);
                                 Log.i("Test", "dateString: "+dateString+"     dateString 2: "+dateString2);
                                 Log.i("Test", "tsim : "+tsim+"     sid: "+sid);
//                                 phNumber.equals(tempnum)&&dateString.equals(tempdate)&&temptype.equals(callType)&&tsim.equals(sid)&&mainlist.size()>0
//                                 &&temptype.equals(callType)
                                 if (phNumber.equals(item1.getNumber())&&dateString.equals(dateString2)&&tsim.equals(sid)){
                                     Log.i("Test","entered if ");
                                     int count=Integer.parseInt(item1.getCount())+1;
                                     item2.setCount(count+"");
                                     /*item2.setType(temptype);
                                     item2.setSimnum(tsim);
                                     item2.setTime(temptime);
                                     item2.setDuration(tempdur);*/
                                     ((Activity) context).runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             mainlist.remove(0);
                                             mainlist.add(0,item2);
                                             adapter.changeitem();
                                             recycle.smoothScrollToPosition(0);

                                         }
                                     });

                                 }

                                 else {
                                     ((Activity) context).runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                            if (day1.equals(day2)) mainlist.get(0).setSeperator("");
                                             mainlist.add(0, item2);
                                             adapter.notifyItemInserted(0);
                                             recycle.smoothScrollToPosition(0);

                                         }
                                     });

                                 }

                                 String newname;
                                 newname=db.getNamefromContact(phNumber);
                                 String p="";
                                 if (newname==null)p=phNumber;
                                 else p=newname;

                                 String durr="";
                                 int caldurint=Integer.parseInt(callDuration);
                                 if (caldurint<60)durr=caldurint+" ثانیه ";
                                 else durr= caldurint/60+" دقیقه ";
                                 db.insertdataLog(p,callDate,Newshdate,timee,durr,callType,sid,callDuration);
                                 tinyDB.putString("lastcalltime",exacttime);
                             }
                            cursor.close();

                         }

                 }).start();
             }
    }




     void createRecyclelist() {
        // Message.message(getActivity(),mylist.size()+"   size",1);
        recycle.setHasFixedSize(true);
        adapter = new recycleAdapterlog(mainlist, recycle.getContext(),tinyDB,db);
        mLayoutManager = new LinearLayoutManager(context);
        recycle.setLayoutManager(mLayoutManager);
        recycle.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.devider));
        recycle.addItemDecoration(dividerItemDecoration);

        recycle.setItemAnimator(new DefaultItemAnimator());
        CitemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperlog(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);

        new CitemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycle);
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist = 0;
            boolean isVisible = true;
            static final int MINIMUM = 25;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isVisible && scrollDist > MINIMUM) {
                    // Scrolling up
                    bottomlay.animate().translationY(bottomlay.getHeight())
                            .setInterpolator(new AccelerateInterpolator(2)).start();
                    scrollDist = 0;
                    isVisible = false;

                } else if (!isVisible && scrollDist < -MINIMUM) {
                    // Scrolling down
                    bottomlay.animate().translationY(0).setInterpolator(new
                            DecelerateInterpolator(2)).start();
                    scrollDist = 0;
                    isVisible = true;

                }
                if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                    scrollDist += dy;
                }
                final int firstVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                }
            }
        });

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        List<itemlog> srchlist=adapter.getlist();
        if (direction == ItemTouchHelper.RIGHT) {
            String mnumber=srchlist.get(position).getNumber();
            makecall(mnumber ,0);
            swipedlog=true;
//            createRecyclelist();
        }
        else {
            if (tinyDB.getBoolean("addenabled")&&!tinyDB.getBoolean("ispremium")){
                Snackbar snackbar = Snackbar
                        .make(containerM,  " برنامه غیر فعال است، لطفا فعال کنید", Snackbar.LENGTH_LONG);
                snackbar.setAction("فعالسازی", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(context, Setting.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putBoolean("fromMain", true);
                        mIntent.putExtras(mBundle);
                        startActivity(mIntent);

                    }
                });
                snackbar.setActionTextColor(context.getResources().getColor(R.color.confirm));
                snackbar.show();
                createRecyclelist();
            }
            else if (viewHolder instanceof recycleAdapterlog.MyViewHolder) {

                final String[] mnumber = {srchlist.get(position).getNumber()};
                final String[] name = {db.getNamefromContact(mnumber[0])};
                String nname;
                if (name[0]==null)nname=mnumber[0];
                else nname=name[0];
                ArrayList<String> blacklistname=tinyDB.getListString("blacklistname");

                 AlertDialog.Builder bld=null;
                if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                bld.setMessage("آیا از افزودن  "+ nname +"  به لیست سیاه مطمئن هستید؟");
                bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String snumber="";
                        String tnumber="";
                        if (name[0] !=null){
                            itema item=db.GetContactByName(name[0]);
                            snumber=item.getNumber2();
                            tnumber=item.getNumber3();
                        }
                        else name[0] = mnumber[0];
                        ArrayList<String> blacklist=tinyDB.getListString("blacklist");
                        mnumber[0] = mnumber[0].substring(1);


                        if (blacklistname.contains(name[0])){
                            Snacktoast.inform((Activity) context,"شماره قبلا به لیست سیاه اضافه شده",R.color.snacktext);
                            createRecyclelist();
                        }
                        else {
                            int count=0;
                            blacklistname.add(name[0]);
                            blacklist.add(mnumber[0]);
                            count++;
                            if(!snumber.equals("")){
                                snumber = snumber.substring(1);
                                blacklist.add(snumber);
                                count++;
                            }
                            if(!tnumber.equals("")){
                                tnumber = tnumber.substring(1);
                                blacklist.add(tnumber);
                                count++;
                            }
                            tinyDB.putListString("blacklist",blacklist);
                            tinyDB.putListString("blacklistname",blacklistname);
                            adapter.removeitem2(position);
                        }
                    }
                });
                bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        createRecyclelist();

                    }
                });

                bld.create().show();














                    //Message.message(getActivity(),name+"   "+number,1);
                  /*  Snackbar snackbar = Snackbar
                            .make(containerM, name[0] + "  به لیست سیاه اضافه شد "+"!", Snackbar.LENGTH_LONG);
                    int finalCount = count;
                    snackbar.setAction("واگرد", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i< finalCount; i++){
                                blacklist.remove(blacklist.size()-i-1);
                            }
                            blacklistname.remove(blacklistname.size()-1);
                            tinyDB.putListString("blacklist",blacklist);
                            tinyDB.putListString("blacklistname",blacklistname);
                            //getCallDetails(context,0);
                            createRecyclelist();
                            // undo is selected, restore the deleted item
                            // mAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

*/
                swipedlog=true;


            }

        }

    }


    private void implementRecyclerViewClickListeners() {
        recycle.addOnItemTouchListener(new RecyclerTouchListener(context, recycle, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                if(swipedlog)swipedlog=false;
                else onListItemSelect(position);
            }
        }));
    }


    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) context).startSupportActionMode(new Toolbar_ActionMode_CallbackLog(context,adapter,mainlist,false));
            if (tabLayout!=null)tabLayout.setVisibility(View.GONE);
        }
            // there are some selected items, start the mActionMode

        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the mActionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            //mActionMode.setTitle(String.valueOf(adapter.getSelectedCount()) + "  انتخاب شده");
        menutxtlog.setText(String.valueOf(adapter.getSelectedCount()));



    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        if (tabLayout!=null) tabLayout.setVisibility(View.VISIBLE);
    }
    public void selectall(){
        adapter.selectall();
        menutxtlog.setText(String.valueOf(adapter.getSelectedCount()));
    }
    public void deselectall(){
        adapter.removeSelection();
        menutxtlog.setText("");
    }

    public void deleteRows() {

        final SparseIntArray selected = adapter
                .getSelectedIds();//Get selected id

        for (int j = (selected.size() - 1); j >= 0; j--) {

            if (selected.valueAt(j)>1){
                int count=selected.valueAt(j);
                for (int h=0;h<count;h++){
                    deleteCall(selected.keyAt(j)-h);

                }

            }
            else {
                //If current id is selected remove the item via key
                deleteCall(selected.keyAt(j));

            }

        }
        List<Integer> poslist=adapter.getSelectedPoses();
        adapter.removeitem(poslist);

    }


    public void sharecontact(){
        String sharetxt="";

            final ArrayList<String> names = adapter.getnames();
            //Message.message(getActivity(),size+"size",1);
            for (int j = (names.size() - 1); j >= 0; j--) {
                //Message.message(getActivity(),names.get(j),1);
                    String namee=db.getNamefromContact(names.get(j));
                    if(namee!=null){
                        //Message.message(getActivity(),"first if",1);
                        itema item=db.GetContactByName(namee);
                        sharetxt=sharetxt+"نام: "+item.getFirstName()+"\nشماره تماس: "+item.getMnumber();
                        if (!item.getNumber2().equals(""))sharetxt=sharetxt+"\n شماره تماس دوم: "+item.getNumber2();
                        if (!item.getNumber3().equals(""))sharetxt=sharetxt+"\n شماره تماس سوم: "+item.getNumber3();
                    }
                    else {
                        sharetxt=sharetxt+"نام: "+"\nشماره تماس: "+names.get(j);
                        //Message.message(getActivity(),"sec if",1);
                    }
                sharetxt=sharetxt+"\n\n";
            }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharetxt);
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    private void deleteCall(int number) {
        Uri CALLLOG_URI = Uri.parse("content://call_log/calls");
        context.getContentResolver().delete(CALLLOG_URI, CallLog.Calls._ID +"=?", new String[] { String.valueOf(number)});

    }


    public class CallLogChangeObserverClass extends ContentObserver
    {

         CallLogChangeObserverClass(Handler
                                            handler) {
            super(handler);
// TODO Auto-generated constructor stub
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }


        public void onChange(boolean selfChange) {

            getCallDetails(context,1);

        }
    }

    public static String shamsidate(String date){
        long seconds=Long.parseLong(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = formatter.format(new Date(seconds));
        String tim=dateString.substring(10,16);
        int day=Integer.parseInt(dateString.substring(0,2));
        int mounth=Integer.parseInt(dateString.substring(3,5));
        int year=Integer.parseInt(dateString.substring(6,10));
        shamsiDate dd=new shamsiDate();
        dd.GregorianToPersian(year,mounth,day);

        return dd.toString();
    }

    public String dayofweek(Date s){
        Calendar c = Calendar.getInstance();
        c.setTime(s); // yourdate is an object of type Date

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //int dayOfweek=now.get(Calendar.DAY_OF_WEEK);
        String days[]={"یکشنبه","دوشنبه","سه شنبه","چهارشنبه","پنجشنبه","جمعه","شنبه"};
        String dayNow=days[dayOfWeek-1];
        return dayNow;
    }


    public  String formattedDuration(String ss) {
        int dur = Integer.parseInt(ss);
        String timeString;
        int hours = dur / 3600;
        int minutes = (dur % 3600) / 60;
        int sec = (dur % 3600) % 60;
        if (dur == 0) {
            timeString = "ناموفق";
        } else if (hours == 0 && minutes != 0) {
            timeString = minutes + " دقیقه  " + sec + " ثانیه";
        } else if (minutes == 0) {
            timeString = sec + " ثانیه";
        } else {
            timeString = hours + " ساعت  " + minutes + " دقیقه  " + sec + " ثانیه";
        }
        return timeString;
    }

    public  ArrayList<String> setseperator(String date,String predate, String weekday,Boolean update){
        ArrayList<String> results=new ArrayList<>();
        long seconds=Long.parseLong(date);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = formatter.format(new Date(seconds));
        String dateString3=formatter2.format(new Date(seconds));
        String exacttime=dateString3.substring(10,18);
        String d1=dateString.substring(0,10);
        int day=Integer.parseInt(dateString.substring(0,2));
        int mounth=Integer.parseInt(dateString.substring(3,5));
        int year=Integer.parseInt(dateString.substring(6,10));
        String time=dateString.substring(10,16);


        long sec=Long.parseLong(predate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString2 = format.format(new Date(sec));
        String d2=dateString2.substring(0,10);


        shamsiDate dd=new shamsiDate();
        dd.GregorianToPersian(year,mounth,day);
        String shamsi=dd.toString();
        String dayno;

        int mmm = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int ddd = now.get(Calendar.DAY_OF_MONTH);


        // Message.message(context,mounth+" "+mmm+"        "+day+"  "+ddd,2);
        if (mounth==mmm && day==ddd){
             dayno="امروز";
        }
        else if ((mounth==mmm && day==ddd-1)||(mounth+1==mmm&&day==ddd+29)||(mounth+1==mmm&&day==ddd+30))dayno="دیروز";
        else dayno=shamsi;

         if (!d1.equals(d2)||update) results.add(weekday+"   "+dayno);
        else results.add("");

//        Log.e("log","mounth= "+mounth+"\n curmount= "+mmm+
//                "\nday= "+day+"\ncurday= "+ddd);

        results.add(shamsi);

        results.add(time);
        results.add(exacttime);
        return results;
    }



    ////////////////////////   writetoDB method
    public  void writelogtoDB(Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                int counter=tinyDB.getInt("missedlogcount");

                if (counter!=0){
                    Uri uri = Uri.parse("content://logs/call");
                    Cursor cursor = context.getContentResolver().query(
                            CallLog.Calls.CONTENT_URI,
                            null, null, null, CallLog.Calls.DATE + " DESC");
//                CursorLoader cl = new CursorLoader(context, CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

                    assert cursor != null;
                    String  slotId = null;
                    // String deviceMan = android.os.Build.MANUFACTURER;
                    // Message.message(getActivity(),deviceMan,1);

                    int simid=0;
                    int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        simid = cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
                    }

                    int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
                    int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                    int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                    int id = cursor.getColumnIndex(CallLog.Calls._ID);


                    Log.i("Test","log count is "+counter);
                    while (cursor.moveToNext()) {
                        String phNumber = cursor.getString(number);
                        if (phNumber.startsWith("+98")){phNumber =phNumber.replace("+98", "0");}
                        String callType = cursor.getString(type);
                        String callDate = cursor.getString(date);
                        String sid;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String tempsim=cursor.getString(simid);
                            if (tempsim!=null) sid=getcallingsim(tempsim);
                            else sid="0";
                        }
                        else sid="0";

                        long seconds=Long.parseLong(callDate);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String dateString = formatter.format(new Date(seconds));
                        String time=dateString.substring(10,16);

                        Date callDayTime = new Date(Long.parseLong(callDate));
                        String callDuration = cursor.getString(duration);
                        String Newshdate=shamsidate(callDate);
                        //String Newweekday=dayofweek(callDayTime);
                        //String Newduration=formattedDuration(callDuration);
                        String newname;
                        newname=db.getNamefromContact(phNumber);
                        String p="";
                        if (newname==null)p=phNumber;
                        else p=newname;

                        String durr="";
                        int caldurint=Integer.parseInt(callDuration);
                        if (caldurint<60)durr=caldurint+" ثانیه ";
                        else durr= caldurint/60+" دقیقه ";
                        if (counter==-1) db.insertdataLog(p,callDate,Newshdate,time,durr,callType,sid,callDuration);
                        else if (counter!=0){
                            db.insertdataLog(p,callDate,Newshdate,time,durr,callType,sid,callDuration);
                            counter--;
                        }
                    }
                    cursor.close();
                    tinyDB.putInt("missedlogcount",0);
                    // Message.message(getActivity(),"new list added",1);
                }



            }
        }).start();
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
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
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





}