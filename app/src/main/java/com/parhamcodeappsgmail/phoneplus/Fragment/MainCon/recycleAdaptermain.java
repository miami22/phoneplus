package com.parhamcodeappsgmail.phoneplus.Fragment.MainCon;

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
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.ConDetail;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Uitem;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.SideBar.ContactScrollerAdapter;
import com.parhamcodeappsgmail.phoneplus.SideBar.Section;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.newcontact;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.parhamcodeappsgmail.phoneplus.MainActivity.flist;

public class recycleAdaptermain extends  RecyclerView.Adapter<recycleAdaptermain.MyViewHolder>  {
    private List<itema> listMain;
    private List<itema> ListFiltered;
    private SparseBooleanArray mSelectedItemsIds;
    private SparseBooleanArray marklist;
    private Context context;
    private ArrayList<String> passlist;
    private int pos=0;
    private View hvid;
    private Integer[] indexarray;
    private HashMap<Integer,String> indexes;
   // private SelectionTracker selectionTracker;
    private boolean selectMode=false;

    private int fromsearch=0;
     private String srctext="";
     TinyDB tinyDB;
     List<Integer> intposlist;
     dbAdapter db;
     Typeface tf;
     String font;


    public interface OnClickAction {
        public void onClickAction();
    }


    //@Inject TinyDB tinyDB;
    //@Inject dbAdapter db;


    public  recycleAdaptermain( Context context,TinyDB tinyDB,dbAdapter db) {
        this.tinyDB=tinyDB;
        this.db=db;
        this.context = context;


        listMain =new ArrayList<>();
        ListFiltered = new ArrayList<>();
        intposlist=new ArrayList<>();
        mSelectedItemsIds = new SparseBooleanArray();
        marklist= new SparseBooleanArray();

        OnClickAction receiver = new OnClickAction() {
            @Override
            public void onClickAction() {

            }
        };



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View aview = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactitem, parent, false);
        return new MyViewHolder(aview);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //DecimalFormat form = new DecimalFormat("###,###,###");
        final itema aitem= ListFiltered.get(position);
       // holder.bind(aitem, selectionTracker.isSelected(aitem));

        String name=aitem.getFirstName();




        if (Arrays.asList(indexarray).contains(position)&&fromsearch==0){
            String title=indexes.get(position);
            holder.line.setVisibility(View.VISIBLE);
            holder.chr.setText(title);
        }

        else holder.line.setVisibility(View.GONE);




        /*
         Section s = mContactScrollerAdapter.fromItemIndex(position);
        if (s.getIndex() == position) {
            Message.message(context,s.getIndex()+"",1);
           // holder.title.setText(s.getTitle());
            holder.line.setVisibility(View.VISIBLE);
            holder.chr.setText(s.getTitle());
        } else {
            //holder.title.setText("");
           holder.line.setVisibility(View.GONE);
        }
         */



/*
if (position > lastItemPosition) {
            // Scrolled Down
            char s=name.charAt(0);
            if (firsrchar!=s){
                firsrchar=s;
                holder.line.setVisibility(View.VISIBLE);
                holder.chr.setText(s+"");
            }
            else holder.line.setVisibility(View.GONE);

        }
        else {
            // Scrolled Up
        }
        lastItemPosition = position;
 */
        SpannableStringBuilder sb = new SpannableStringBuilder(name);
        Pattern word = Pattern.compile(srctext.toLowerCase());
        Matcher match = word.matcher(name.toLowerCase());

        while (match.find()) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.confirm)); //specify color here
            sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        holder.name.setText(sb);

        String number=aitem.getMnumber();
        SpannableStringBuilder sb2 = new SpannableStringBuilder(number);
        Pattern word2 = Pattern.compile(srctext.toLowerCase());
        Matcher match2 = word2.matcher(number.toLowerCase());

        while (match2.find()) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.confirm)); //specify color here
            sb2.setSpan(fcs, match2.start(), match2.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        holder.fcontact.setText(sb2);


        //////////////set avater
        String stavat=aitem.getAvatar();
        if (stavat.equals("")){
            char a=name.charAt(0);
            String[] splitStr = name.trim().split("\\s+");
            if (splitStr.length!=1){
                char b=splitStr[1].charAt(0);
                holder.txtavatar.setVisibility(View.VISIBLE);
                holder.txtavatar.setText(a+" "+b);
                holder.avatar.setVisibility(View.GONE);
            }
            else {
                holder.txtavatar.setVisibility(View.VISIBLE);
                holder.txtavatar.setText(a+"");
                holder.avatar.setVisibility(View.GONE);
            }
        }
        else {

            Bitmap avat=Decodeimg.decodeBase64(aitem.getAvatar());
            holder.txtavatar.setVisibility(View.GONE);
            holder.avatar.setImageBitmap(avat);
            holder.avatar.setVisibility(View.VISIBLE);

        }


        final Animation hideanim = AnimationUtils.loadAnimation(context, R.anim.hide_anim);
        final Animation showanim = AnimationUtils.loadAnimation(context, R.anim.show_anim);


        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //com.parhamcodeappsgmail.phoneplus.Tools.Message.message(context,"position= "+position+"  id="+aitem.getid(),1);
               // holder.viewStub.setLayoutResource(R.layout.conitemprop);


                    if (holder.hv==null){
                        View inflatedView = holder.viewStub.inflate();
                       // holder.viewStub.setVisibility(View.VISIBLE);

                    holder.detail=inflatedView.findViewById(R.id.Mdetail);
                    holder.edit=inflatedView.findViewById(R.id.Medit);
                    holder.whatup=inflatedView.findViewById(R.id.Mwhatsup);
                    holder.insta=inflatedView.findViewById(R.id.Minsta);
                    holder.teleg=inflatedView.findViewById(R.id.Mteleg);
                    holder.mess=inflatedView.findViewById(R.id.Mmess);
                    holder.sim2=inflatedView.findViewById(R.id.Msim2);
                    holder.sim1=inflatedView.findViewById(R.id.Msim1);
                    holder.share=inflatedView.findViewById(R.id.Mshare);
                    holder.hv=inflatedView.findViewById(R.id.hidelayout);
                    holder.horiz=inflatedView.findViewById(R.id.hhhs);

                    if (!tinyDB.getBoolean("isdual")) holder.sim2.setVisibility(View.GONE);
                       // if (!tinyDB.getBoolean("sim1ready")) holder.sim1.setVisibility(View.GONE);

                        holder.lastcall=inflatedView.findViewById(R.id.lastcall);
                        ArrayList<Uitem> list=db.GetLogByName(name);
                        if (list.size()>0){
                            Uitem uitem=list.get(0);
                            String date=uitem.getDate();
                            String time=uitem.getTime();
                            holder.lastcall.setVisibility(View.VISIBLE);
                            holder.lastcall.setText("آخرین تماس :    "+ date+"   "+time);
                        }
                    }

                if (pos==0){
                    if (holder.hv.getVisibility() == View.VISIBLE) {

                        // Its visible
                        holder.hv.setVisibility(View.GONE);

                    } else {
                        holder.hv.setVisibility(View.VISIBLE);

                    }
                    hvid=holder.hv;
                    pos=1;
                }
                else {
                    if (hvid==holder.hv){
                        if (holder.hv.getVisibility() == View.VISIBLE) {
                            // Its visible
                            holder.hv.setVisibility(View.GONE);
                            //hvid=holder.hv;

                        } else {
                            holder.hv.setVisibility(View.VISIBLE);

                        }

                    }
                    else {
                        hvid.setVisibility(View.GONE);
                        if (holder.hv.getVisibility() == View.VISIBLE) {
                            // Its visible
                            holder.hv.setVisibility(View.GONE);
                            hvid=holder.hv;

                        } else {
                            holder.hv.setVisibility(View.VISIBLE);

                            hvid=holder.hv;

                        }
                    }
                    if(holder.hv.getVisibility()==View.VISIBLE)selectMode=true;
                    else selectMode=false;
                    holder.horiz.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                }



                    if (!tinyDB.getBoolean("isdual"))holder.sim2.setVisibility(View.GONE);

                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(context, ConDetail.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("id", aitem.getid());
                        mBundle.putInt("type", 1);
                        mIntent.putExtras(mBundle);
                        view.getContext().startActivity(mIntent);
                    }
                });

                holder.whatup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String num=number;
                        if (num.startsWith("09"))num =num.replaceFirst("0", "+98");
                        whatsup(num);
                    }
                });
                String instag=aitem.getInsta();
                holder.insta.setOnClickListener(new View.OnClickListener() {
                    @       Override
                    public void onClick(View view) {

                        if (instag.equals("")){
                            alert("اینستاگرام",aitem.getid());
                        }
                        else instageram(instag);
                    }
                });

                String telegram=aitem.getTeleg();
                holder.teleg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (telegram.equals("")){
                            alert("تلگرام",aitem.getid());
                        }
                        else telegram(telegram);
                    }
                });

               holder.mess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendsms(number);
                    }
                });

                holder.sim1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makecall(number,0);
                        if (tinyDB.getBoolean("isdual"))tinyDB.putInt("simid",0);

                    }
                });

                holder.sim2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makecall(number,1);
                        tinyDB.putInt("simid",1);
                    }
                });

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(context, newcontact.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putInt("id", aitem.getid());
                        mBundle.putInt("edit", 1);
                        mBundle.putString("type", "mu");
                        mIntent.putExtras(mBundle);
                        view.getContext().startActivity(mIntent);
                    }
                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment recyclerFragment=flist.getFraga();
                        if (recyclerFragment != null)
                            //If recycler fragment not null
                            ((mainconfrag) recyclerFragment).sharecontact(aitem.getid()+"");
                    }
                });

            ////////satrtclicklisteners


            }
        });

/*
 holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);
 */

        if (marklist.get(position)){holder.check.setVisibility(View.VISIBLE);}
        else holder.check.setVisibility(View.GONE);

///////////////////END OF ONBIND
    }






    @Override
    public int getItemCount() {
        return (null != ListFiltered ? ListFiltered.size() : 0);
    }

    //private MultiSelector mMultiSelector = new MultiSelector();


    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout itemlayout;


        public LinearLayout line,viewForeground ;
        public  RelativeLayout swipeleft,swiperight;
        TextView chr;
        ImageView avatar,check;
        public TextView name;
        TextView fcontact;
        TextView txtavatar;
        TextView lastcall,lineg;
        ViewStub viewStub;
        public LinearLayout hv;
        LinearLayout detail;
        LinearLayout whatup;
        LinearLayout insta;
        LinearLayout teleg;
        LinearLayout mess;
        LinearLayout sim2;
        LinearLayout sim1;
        LinearLayout edit;
        LinearLayout share;
        HorizontalScrollView horiz;

        public MyViewHolder(View itemView) {

            super(itemView); // (2)


            // itemView.setLongClickable(true);
            //itemlayout =itemView.findViewById(R.id.);
            swipeleft = itemView.findViewById(R.id.view_background);
            swiperight = itemView.findViewById(R.id.view_background1);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            avatar = itemView.findViewById(R.id.Mavatar);
            name = itemView.findViewById(R.id.Mname);
            name.setTypeface(tf);
            avatar = itemView.findViewById(R.id.Mavatar);

            //email=itemView.findViewById(R.id.Memail);

            line = itemView.findViewById(R.id.line);
            lineg = itemView.findViewById(R.id.lineg);
            fcontact = itemView.findViewById(R.id.Mnumber);
            fcontact.setTypeface(tf);


            itemlayout = itemView.findViewById(R.id.itemlay);
            chr = itemView.findViewById(R.id.chr);
            txtavatar = itemView.findViewById(R.id.txtavatar);
            check = itemView.findViewById(R.id.check);
            viewStub = itemView.findViewById(R.id.conviewstub);

            /*

             */




                    switch (font) {

                        case "font/casablanca.ttf": {
                            name.setTextSize(16);
                            fcontact.setTextSize(15);
                        }
                        case "font/dastnevis.ttf": {
                            name.setTextSize(21);
                            fcontact.setTextSize(21);
                        }
                        case "font/free.ttf": {
                            name.setTextSize(22);
                            fcontact.setTextSize(21);
                        }
                        case "font/byek.TTF": {
                            name.setTextSize(14);
                            fcontact.setTextSize(13);
                        }
                        case "font/davat.ttf": {
                            name.setTextSize(24);
                            fcontact.setTextSize(23);
                        }
                        case "font/traffic.ttf": {
                            name.setTextSize(16);
                            fcontact.setTextSize(15);
                        }
                        case "font/rezvan.ttf": {
                            name.setTextSize(18);
                            fcontact.setTextSize(17);
                        }
                    }
                    if (tinyDB.getBoolean("lighttheme")) {
                        name.setTextColor(context.getResources().getColor(R.color.likeblack));
                        fcontact.setTextColor(context.getResources().getColor(R.color.darktgray));
                        txtavatar.setBackground(context.getResources().getDrawable(R.drawable.backcirclelight));
                        lineg.setBackgroundColor(context.getResources().getColor(R.color.darktgray));
                        viewForeground.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
                    }


                }



        }



/*
 @Override
        public boolean onLongClick(View myview) {
            Message.message(context,"clicked",1);
            if (!mMultiSelector.isSelectable()) { // (3)
                mMultiSelector.setSelectable(true); // (4)
                mMultiSelector.setSelected(MyViewHolder.this, true); // (5)
                return true;
            }

            return false;
        }
 */



    ////این قسمت باعث می شود با اسکرول کردن لیست پوزیشن آیتمها عوض نشود







    @Override
    public long getItemId(int position) {
        return position;
    }




    /*
    public SelectionTracker getSelectionTracker() {
        return selectionTracker;

    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {

        this.selectionTracker = selectionTracker;

    }
     */





    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !marklist.get(position));


    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        marklist= new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value){

            if (position==-1){ }
            else {
                int id=ListFiltered.get(position).getid();
                mSelectedItemsIds.put(id, value);
                marklist.put(position, value);
            }
            }
        else{
            mSelectedItemsIds.delete(listMain.get(position).getid());
             marklist.delete(position);}

        notifyDataSetChanged();
    }

    public void selectall(){

        int size=ListFiltered.size();
        removeSelection();
        for (int i=0;i<size;i++){
            int id=ListFiltered.get(i).getid();
            mSelectedItemsIds.put(id, true);
            marklist.put(i, true);
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() { return mSelectedItemsIds; }
    public List<Integer> getSelectedPoses() {
        List<Integer> posss=new ArrayList<>();
        for (int g=0;g<marklist.size();g++){
            posss.add(marklist.keyAt(g));
        }
        Collections.sort(posss, Collections.reverseOrder());
        return posss; }


    public  void filter(final String text) {
        srctext=text;
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                ListFiltered.clear();

                if (TextUtils.isEmpty(text)) {

                    ListFiltered.addAll(listMain);
                    fromsearch=0;

                } else {
                    // Iterate in the original List and add it to filter listMain...
                    for (itema item : listMain) {
                        if (item.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getMnumber().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            ListFiltered.add(item);
                        }
                        else if (item.getNumber2().toLowerCase().contains(text.toLowerCase())){
                            item.setMnumber(item.getNumber2());
                            ListFiltered.add(item);
                        }

                        else if (item.getNumber3().toLowerCase().contains(text.toLowerCase())){
                            item.setMnumber(item.getNumber3());
                            ListFiltered.add(item);
                        }
                    }
                    fromsearch=1;
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

    }

    void alert(String title, final int id){
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("آی دی "+title+" وارد نشده \n"+"میخوای آی دی وارد کنی؟");
        bld.setCancelable(true);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent mIntent = new Intent(context, newcontact.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", id);
                mBundle.putInt("edit", 1);
                mBundle.putString("cons", title);
                mBundle.putString("type", "mi");
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//            for (String s : simSlotName) intent.putExtra(s, number);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", (Parcelable) " here You have to get phone account handle list by using telecom manger for both sims:- using this method getCallCapablePhoneAccounts()");*/
            context.startActivity(intent);
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
                context.startActivity(intent);
            }}

    }


    void sendsms(String number){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + number));
        //sendIntent.putExtra("sms_body", "default content");
        //sendIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendIntent);
    }

    void telegram(String id){
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/"+id));
        context.startActivity(telegram);
    }

    void instageram(String id){
        Uri uri = Uri.parse("http://instagram.com/_u/"+id);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(context, insta)){
            context.startActivity(insta);
        } else{
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+id)));
        }
    }
    void whatsup(String number) {
        if(isAppAvailable(context,"com.whatsapp")){
            Uri uri = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(i, ""));
        }
        else Message.message(context,"برنامه whatsup در دستگاه شما نصب نیست",1);

    }
    public static boolean isAppAvailable(Context context, String appName)
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

    @Override
    public int getItemViewType(int position){
        return position;
    }

 public void removeitem(List<Integer> pos){
        for (int i=0;i<pos.size();i++){
            int k=pos.get(i);
            ListFiltered.remove(k);
            notifyItemRemoved(k);
            notifyItemRangeChanged(k, listMain.size());
        }

 }
 public List<itema> getlist(){

        return ListFiltered;
 }

 public void setlist(List<itema> listt){

        listMain =listt;
        ListFiltered.clear();
        ListFiltered.addAll(listt);
        notifyDataSetChanged();
        setindexes(listt);
    }


 private void setindexes(List<itema> list){
     ContactScrollerAdapter contactScrollerAdapter1=new ContactScrollerAdapter(list);
     List<Section> seclist= contactScrollerAdapter1.getsectionlist();
     String[] sectitle=new String[seclist.size()];
     indexarray=new Integer[seclist.size()];
     font=tinyDB.getString("font");
     tf=Typeface.createFromAsset(context.getAssets(),font );
     indexes=new HashMap<>();
     //db=new dbAdapter(context);
     //
     for (int i=0;i<seclist.size();i++){
         Section sec=seclist.get(i);
         //Message.message(getActivity(),sec.getTitle()+"    "+ sec.getIndex(),1);
         sectitle[i]=sec.getTitle();
         int pos=sec.getIndex();
         indexes.put(pos,sec.getTitle());
         indexarray[i]=pos;
         //Message.message(context,pos+"",1);
     }
 }



}