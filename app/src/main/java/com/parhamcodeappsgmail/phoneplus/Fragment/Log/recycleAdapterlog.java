package com.parhamcodeappsgmail.phoneplus.Fragment.Log;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.ConDetail;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.newcontact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class recycleAdapterlog extends RecyclerView.Adapter<recycleAdapterlog.MyViewHolder> {
    private List<itemlog> list;
    private List<itemlog> ListFiltered;
    private Context context;
    private dbAdapter db;
    private int posss = 0;
    private View tempview;
    private SparseIntArray mSelectedItemsIds;
    private SparseBooleanArray marklist;
    private ArrayList<String> namelist;
    private TinyDB tinyDB;
    public RecyclerView recycler;
    Typeface tf;
    String font;
    private int frompdate = 0;

    public recycleAdapterlog(List<itemlog> list, Context context, TinyDB tinyDB, dbAdapter db) {
        this.tinyDB = tinyDB;
        this.db = db;
        this.list = list;
        this.context = context;
        mSelectedItemsIds = new SparseIntArray();
        marklist = new SparseBooleanArray();
        this.ListFiltered = new ArrayList<>();
        this.ListFiltered.addAll(this.list);
        namelist = new ArrayList<>();
        font = tinyDB.getString("font");
        tf = Typeface.createFromAsset(context.getAssets(), font);


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View aview = LayoutInflater.from(parent.getContext()).inflate(R.layout.logitem, parent, false);
        return new MyViewHolder(aview);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final itemlog item = list.get(position);
        String shamsi = item.getShamsi();
        String number = item.getNumber();
        String count = item.getCount();
        String tim = item.getTime();
        String sep = item.getSeperator();
        ArrayList<String> names = tinyDB.getListString("blacklistname");

        if (!sep.equals("")) {
            holder.seperator.setVisibility(View.VISIBLE);
            holder.seperator.setText(sep);
        } else holder.seperator.setVisibility(View.GONE);


        String sim = item.getSimnum();
        if (sim.equals("1"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mc1));
        else if (sim.equals("2"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mc2));
        else if (sim.equals("3"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.iranc1));
        else if (sim.equals("4"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.iranc2));
        else if (sim.equals("5"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mc));
        else if (sim.equals("6"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.iranc));
        else if (sim.equals("7"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim11));
        else if (sim.equals("8"))
            holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim22));


        String timeString = item.getDuration();
        holder.duration.setText(timeString);


        String name;
        String passname;
        name = db.getNamefromContact(number);
        if (name != null) {
            passname = name;
            if (count.equals("1")) holder.name.setText(name);
            else holder.name.setText(name + "   (" + count + ")");
        } else {
            passname = number;
            if (count.equals("1")) holder.name.setText(number);
            else holder.name.setText(number + "   (" + count + ")");
        }


        String avatr = db.getAvatarfromContact(number);
        if (avatr == null) {
            holder.txtavatar.setVisibility(View.VISIBLE);
            holder.txtavatar.setText("#");
            holder.avatar.setVisibility(View.GONE);
        } else if (avatr.equals("")) {
            //assert name != null;
            char a = passname.charAt(0);
            String[] splitStr = name.trim().split("\\s+");
            if (splitStr.length != 1) {
                char b = splitStr[1].charAt(0);
                holder.txtavatar.setVisibility(View.VISIBLE);
                holder.txtavatar.setText(a + " " + b);
                holder.avatar.setVisibility(View.GONE);
            } else {
                holder.txtavatar.setVisibility(View.VISIBLE);
                holder.txtavatar.setText(a + "");
                holder.avatar.setVisibility(View.GONE);
            }
        } else {
            Bitmap avat = Decodeimg.decodeBase64(avatr);
            holder.txtavatar.setVisibility(View.GONE);
            holder.avatar.setImageBitmap(avat);
            holder.avatar.setVisibility(View.VISIBLE);
        }


        String type = item.getType();
        String calltype = "";
        int dircode = Integer.parseInt(type);
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                calltype = "OUTGOING";
                if (tinyDB.getBoolean("lighttheme")) {
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.calloutblack));
                } else
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.callout));
                break;
            case CallLog.Calls.INCOMING_TYPE:
                calltype = "INCOMING";
                if (tinyDB.getBoolean("lighttheme")) {
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.callinblack));
                } else
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.callin));

                break;

            case CallLog.Calls.MISSED_TYPE: {
                calltype = "MISSED";
                holder.type.setBackground(context.getResources().getDrawable(R.drawable.callmissed));

            }


            break;
        }

        if (names.contains(passname) && !calltype.equals("OUTGOING"))
            holder.duration.setText("بلاک شده");
        holder.date.setText(shamsi);
        holder.time.setText(tim);

        holder.logitm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Message.message(context,item.getSimnum(),2);
                if (holder.hv == null) {
                    View inflatedView = holder.viewStub.inflate();
                    // holder.viewStub.setVisibility(View.VISIBLE);

                    holder.detail = inflatedView.findViewById(R.id.Mdetail);
                    holder.whatup = inflatedView.findViewById(R.id.Mwhatsup);
                    holder.insta = inflatedView.findViewById(R.id.Minsta);
                    holder.teleg = inflatedView.findViewById(R.id.Mteleg);
                    holder.mess = inflatedView.findViewById(R.id.Mmess);
                    holder.sim2 = inflatedView.findViewById(R.id.Msim2);
                    holder.sim1 = inflatedView.findViewById(R.id.Msim1);
                    holder.hv = inflatedView.findViewById(R.id.hidelayout);
                    holder.horiz = inflatedView.findViewById(R.id.hhhs);
                    holder.save = inflatedView.findViewById(R.id.Msave);
                    holder.share = inflatedView.findViewById(R.id.Mshare);
                    holder.edit = inflatedView.findViewById(R.id.Medit);
                    holder.lastcall = inflatedView.findViewById(R.id.lastcall);
                }

                if (!tinyDB.getBoolean("isdual")) holder.sim2.setVisibility(View.GONE);
                //if (!tinyDB.getBoolean("sim1ready")) holder.sim1.setVisibility(View.GONE);
                if (name == null) holder.save.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.GONE);
                holder.share.setVisibility(View.GONE);

                if (posss == 0) {
                    if (holder.hv.getVisibility() == View.VISIBLE) {
                        // Its visible
                        holder.hv.setVisibility(View.GONE);

                    } else {
                        holder.hv.setVisibility(View.VISIBLE);
                    }
                    tempview = holder.hv;
                    posss = 1;
                } else {
                    if (tempview == holder.hv) {
                        if (holder.hv.getVisibility() == View.VISIBLE) {
                            // Its visible
                            holder.hv.setVisibility(View.GONE);
                            //hvid=holder.hv;

                        } else {
                            holder.hv.setVisibility(View.VISIBLE);

                        }
                    } else {
                        tempview.setVisibility(View.GONE);
                        if (holder.hv.getVisibility() == View.VISIBLE) {
                            // Its visible
                            holder.hv.setVisibility(View.GONE);
                            tempview = holder.hv;

                        } else {
                            holder.hv.setVisibility(View.VISIBLE);

                            tempview = holder.hv;

                        }
                    }

                    holder.horiz.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                }

                if (name != null) {
                    holder.lastcall.setVisibility(View.VISIBLE);
                    String numb = db.getNumberByName(name);
                    holder.lastcall.setText("شماره :   " + numb);
                }

                holder.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(context, newcontact.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("edit", 1);
                        mBundle.putString("number", number);
                        mBundle.putString("type", "ls");
                        mIntent.putExtras(mBundle);
                        view.getContext().startActivity(mIntent);
                        tinyDB.putBoolean("savefromlog", true);
                    }
                });


                holder.whatup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String num = number;
                        if (num.startsWith("09")) num = num.replaceFirst("0", "+98");
                        whatsup(num);
                    }
                });

                final String finalName3 = name;
                holder.insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalName3 == null) {
                            Message.message(context, "مخاطب ناشناس", 1);
                        } else {
                            String instaid = getcontactitem(finalName3).getInsta();
                            //Message.message(context,teleid+"  id",1);
                            if (instaid.equals("")) {
                                alert("اینستاگرام", getcontactitem(finalName3).getid());
                            } else instageram(instaid);
                        }
                    }
                });

                final String finalName1 = name;
                final String finalName2 = name;
                holder.teleg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalName2 == null) {
                            Message.message(context, "مخاطب ناشناس", 1);
                        } else {
                            String teleid = getcontactitem(finalName1).getTeleg();
                            //Message.message(context,teleid+"  id",1);
                            if (teleid.equals("")) {
                                alert("تلگرام", getcontactitem(finalName1).getid());
                            } else telegram(teleid);
                        }

                    }
                });
                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(context, ConDetail.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("name", passname);
                        mBundle.putInt("type", 2);
                        mIntent.putExtras(mBundle);
                        view.getContext().startActivity(mIntent);
                    }
                });

                final String finalNumber1 = number;
                holder.mess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendsms(finalNumber1);
                    }
                });

                final String finalNumber = number;
                holder.sim1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makecall(finalNumber, 0);
                        if (tinyDB.getBoolean("isdual")) tinyDB.putInt("simid", 0);
                    }
                });

                holder.sim2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makecall(finalNumber, 1);
                        tinyDB.putInt("simid", 1);
                    }
                });

            }
        });


        if (marklist.get(position)) {
            holder.check.setVisibility(View.VISIBLE);
        } else holder.check.setVisibility(View.GONE);


/////////////////end of onbind
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout swipelogleft, swipelogright;
        LinearLayout logitm;
        TextView time, duration, txtavatar;
        TextView name, seperator;
        TextView date;
        ImageView avatar, check;
        ImageView sim;
        ImageView type;
        ViewStub viewStub;
        LinearLayout hv;
        LinearLayout detail;
        LinearLayout whatup;
        LinearLayout insta;
        LinearLayout teleg;
        LinearLayout mess;
        LinearLayout sim2;
        LinearLayout sim1;
        LinearLayout edit;
        LinearLayout share;
        LinearLayout save;
        HorizontalScrollView horiz;
        TextView lastcall;

        MyViewHolder(View itemView) {
            super(itemView);
            swipelogleft = itemView.findViewById(R.id.view_backgroundlog);
            swipelogright = itemView.findViewById(R.id.view_background1log);
            logitm = itemView.findViewById(R.id.loggitemlay);
            avatar = itemView.findViewById(R.id.logavatar);
            name = itemView.findViewById(R.id.logsimnum);
            name.setTypeface(tf);
            date = itemView.findViewById(R.id.logdates);
            time = itemView.findViewById(R.id.logtimes);
            duration = itemView.findViewById(R.id.logdurations);
            type = itemView.findViewById(R.id.imglogtype);
            sim = itemView.findViewById(R.id.imglogsim);
            txtavatar = itemView.findViewById(R.id.logtxtavatar);
            check = itemView.findViewById(R.id.logcheck);
            seperator = itemView.findViewById(R.id.logseperator);


            viewStub = itemView.findViewById(R.id.logviewstub);


            switch (font) {

                case "font/casablanca.ttf": {
                    name.setTextSize(16);
                }
                case "font/dastnevis.ttf": {
                    name.setTextSize(21);
                }
                case "font/free.ttf": {
                    name.setTextSize(22);
                }
                case "font/byek.TTF": {
                    name.setTextSize(14);
                }
                case "font/davat.ttf": {
                    name.setTextSize(24);
                }
                case "font/traffic.ttf": {
                    name.setTextSize(16);
                }
                case "font/rezvan.ttf": {
                    name.setTextSize(18);
                }
            }
            if (tinyDB.getBoolean("lighttheme")) {
                name.setTextColor(context.getResources().getColor(R.color.likeblack));
                duration.setTextColor(context.getResources().getColor(R.color.darktgray));
                time.setTextColor(context.getResources().getColor(R.color.darktgray));
                date.setTextColor(context.getResources().getColor(R.color.darktgray));
                txtavatar.setBackground(context.getResources().getDrawable(R.drawable.backcirclelight));
                seperator.setTextColor(context.getResources().getColor(R.color.sidebarred));
                logitm.setBackgroundColor(context.getResources().getColor(R.color.lightgray));


            }


        }

    }


    void toggleSelection(int position) {
        selectView(position, !marklist.get(position));


    }


    //Remove selected selections
    void removeSelection() {
        mSelectedItemsIds = new SparseIntArray();
        marklist = new SparseBooleanArray();
        namelist = new ArrayList<>();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    private void selectView(int position, boolean value) {
        if (value) {

            if (position == -1) {
            } else {
                int id = list.get(position).getId();
                int count = Integer.parseInt(list.get(position).getCount());
                String name = list.get(position).getNumber();
                namelist.add(name);
                mSelectedItemsIds.put(id, count);
                marklist.put(position, value);

            }
        } else {
            mSelectedItemsIds.delete(list.get(position).getId());
            marklist.delete(position);
            namelist.remove(list.get(position).getNumber());
        }


        notifyDataSetChanged();
    }

    void selectall() {

        int size = ListFiltered.size();
        removeSelection();
        for (int j = 0; j < size; j++) {
            int id = list.get(j).getId();
            int count = Integer.parseInt(list.get(j).getCount());
            mSelectedItemsIds.put(id, count);
            marklist.put(j, true);
            namelist.add(list.get(j).getNumber());
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    ArrayList<String> getnames() {
        return namelist;
    }

    //Return all selected ids
    SparseIntArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    List<Integer> getSelectedPoses() {
        List<Integer> posss = new ArrayList<>();
        for (int g = 0; g < marklist.size(); g++) {
            posss.add(marklist.keyAt(g));
        }
        Collections.sort(posss, Collections.reverseOrder());
        return posss;
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
            context.startActivity(intent);
            Log.i("Test","first if  ");
        }
        else {
            Log.i("Test","main sec ");
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
                context.startActivity(intent);
            }}

    }





    private void sendsms(String number){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + number));
        //sendIntent.putExtra("sms_body", "default content");
        //sendIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendIntent);
    }

    private void telegram(String id){
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/"+id));
        context.startActivity(telegram);
    }
    private itema getcontactitem(String name){
        return db.GetContactByName(name);
    }

    private void alert(String title, final int id){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("آی دی "+title+" وارد نشده \n"+"میخوای آی دی وارد کنی؟");
        bld.setCancelable(true
        );
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent mIntent = new Intent(context, newcontact.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", id);
                mBundle.putInt("edit", 1);
                mBundle.putString("cons", title);
                mBundle.putString("type", "li");
                mIntent.putExtras(mBundle);
                context.startActivity(mIntent);

            }
        });
        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        bld.create().show();
    }

    private void instageram(String id){
        Uri uri = Uri.parse("http://instagram.com/_u/"+id);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(context, insta)){
            context.startActivity(insta);
        } else{
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+id)));
        }
    }

    private void whatsup(String number) {
        if(isAppAvailable(context,"com.whatsapp")){
            Uri uri = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(i, ""));
        }
        else Message.message(context,"برنامه whatsup در دستگاه شما نصب نیست",1);

    }

    private static boolean isAppAvailable(Context context, String appName)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }



    ////این قسمت باعث می شود با اسکرول کردن لیست پوزیشن آیتمها عوض نشود
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

     void removeitem(List<Integer> pos){

        for(int x =0; x <pos.size(); x++)
        {
            int v=pos.get(x);
            list.remove(v);
            notifyItemRemoved(v);
            notifyItemRangeChanged(v,list.size());
        }

    }

     void removeitem2(int pos){
            list.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,list.size());
    }


     void changeitem(){

        notifyItemChanged(0);
        frompdate=1;
    }



    public static class MyObject implements Comparable<MyObject> {

        private Date dateTime;

         Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date datetime) {
            this.dateTime = datetime;
        }

        @Override
        public int compareTo(MyObject o) {
            return getDateTime().compareTo(o.getDateTime());
        }
    }

    //////////////////filter Method
    /*public void filter(final String text) {
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                ListFiltered.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    ListFiltered.addAll(list);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (itemlog item : list) {
                        String name=db.getNamefromContact(item.getNumber());
                        if (item.getNumber().toLowerCase().contains(text.toLowerCase())||
                                name.toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            ListFiltered.add(item);
                        }

                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...

                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }*/

      List<itemlog> getlist(){
        return  list;
    }

    public void setlist(List<itemlog> listt){

        list =listt;
        ListFiltered.clear();
        ListFiltered.addAll(listt);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recycler=recyclerView;
    }
    public RecyclerView getrecycler(){
          return  recycler;
    }

}