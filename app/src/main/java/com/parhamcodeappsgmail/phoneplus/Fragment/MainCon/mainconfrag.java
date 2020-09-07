package com.parhamcodeappsgmail.phoneplus.Fragment.MainCon;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gjiazhe.wavesidebar.WaveSideBar;
import com.parhamcodeappsgmail.phoneplus.DI.DaggeractivityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.DaggerfragmentComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityModule;
import com.parhamcodeappsgmail.phoneplus.DI.fragmentModule;
import com.parhamcodeappsgmail.phoneplus.Tools.Encodeimg;
import com.parhamcodeappsgmail.phoneplus.Fragment.Log.recentconfrag;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.RecyclerTouchListener;
import com.parhamcodeappsgmail.phoneplus.Setting;
import com.parhamcodeappsgmail.phoneplus.SideBar.ContactScrollerAdapter;
import com.parhamcodeappsgmail.phoneplus.SideBar.Section;
import com.parhamcodeappsgmail.phoneplus.Tools.Snacktoast;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Tools.backupDB;
import com.parhamcodeappsgmail.phoneplus.contactmodel;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import javax.inject.Inject;

import static android.os.Looper.getMainLooper;
import static com.parhamcodeappsgmail.phoneplus.MainActivity.flist;
import static com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.Toolbar_ActionMode_Callback.menutxt;


public class mainconfrag extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    //
    @Inject TinyDB tinyDB;
    @Inject dbAdapter db;
    @Inject Context context;

    public static recycleAdaptermain adapter;
    private RecyclerView recycle;
    private List<itema> mylist;
    ConstraintLayout bottomlay;
    WaveSideBar sideBar ;
    private LinearLayoutManager mLayoutManager;
    public static boolean swiped = true;

    TextView progtxt;
    private ActionMode mActionMode;
    View myview;
    ConstraintLayout containerM;
    ImageView dialler;
    TabLayout tabLayout;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback ;
    ItemTouchHelper itemTouchHelper;


    public mainconfrag() {
        // Required empty public constructor
    }


    public static mainconfrag newInstance(String param1, String param2) {
        mainconfrag fragment = new mainconfrag();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

         DaggerfragmentComponent.builder().activityComponent(DaggeractivityComponent.builder().activityModule(new activityModule((AppCompatActivity)getActivity())).build()).fragmentModule(new fragmentModule(this)).build()
        .inject(this);

        adapter=new recycleAdaptermain(context,tinyDB,db);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mylist = new ArrayList<>();

       // setHasOptionsMenu(true);




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mainconfrag,container,false);
        myview=view;

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        recycle=view.findViewById(R.id.recyclemain);
        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);
        itemTouchHelper=new ItemTouchHelper(itemTouchHelperCallback);
        sideBar=view.findViewById(R.id.side_bar);

        progtxt=view.findViewById(R.id.progtxt);
        bottomlay = (Objects.requireNonNull(getActivity())).findViewById(R.id.bottomlayout);
        bottomlay.setVisibility(View.VISIBLE);
        dialler = ((AppCompatActivity)context).findViewById(R.id.dial);
        tabLayout =((AppCompatActivity)context).findViewById(R.id.tablayoutC);
        containerM=((AppCompatActivity)context).findViewById(R.id.maincontainer);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


if(tinyDB.getBoolean("newsession")){
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run()  {
            checkreaded();
            sidebarmanager();
        }}, 300);
    tinyDB.putBoolean("newsession",false);
}
else {
    checkreaded();
    sidebarmanager();
}


    }

    @Override
    public void onResume() {

        if (tinyDB.getInt("fromnew")==1){createRecyclelist();
             tinyDB.putInt("fromnew",0); }
        if(tinyDB.getBoolean("fontchange")){
            tinyDB.putBoolean("fontchange",false);
            createRecyclelist();
        }

        if(tinyDB.getBoolean("lighttheme")){
            sideBar.setTextColor(context.getResources().getColor(R.color.sidebarred));
        }
        else sideBar.setTextColor(context.getResources().getColor(R.color.orange));
        super.onResume();
    }

    void  createRecyclelist() {
        mylist=db.getDataContact2();
        recycle.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recycle.setLayoutManager(mLayoutManager);
        recycle.setAdapter(adapter);
        adapter.setlist(mylist);


        itemTouchHelper.attachToRecyclerView(recycle);
        recycle.setItemAnimator(new DefaultItemAnimator());

        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist=0;
            boolean isVisible=true;
            static final int MINIMUM=25;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isVisible&&scrollDist>MINIMUM) {
                    // Scrolling up
                    bottomlay.animate().translationY(bottomlay.getHeight())
                            .setInterpolator(new AccelerateInterpolator(2)).start();
                    scrollDist=0;
                    isVisible=false;

                } else if (!isVisible&&scrollDist<-MINIMUM) {
                    // Scrolling down
                    bottomlay.animate().translationY(0).setInterpolator(new
                            DecelerateInterpolator(2)).start();
                    scrollDist=0;
                    isVisible=true;

                }
                if ((isVisible&&dy>0)||(!isVisible&&dy<0)) {
                    scrollDist+=dy;
                }
                final int firstVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();

            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {

                }
            }
        });
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        List<itema> srchlist=adapter.getlist();
        if (direction == ItemTouchHelper.RIGHT) {
            String mnumber=srchlist.get(position).getMnumber();
            makecall(mnumber ,0);
            swiped=true;
//            createRecyclelist();
            adapter.notifyItemChanged(position);
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
                adapter.notifyItemChanged(position);

            }
            else if (viewHolder instanceof recycleAdaptermain.MyViewHolder) {

                // get the removed item name to display it in snack bar
                ;

                final String[] name={srchlist.get(position).getFirstName()};
                final String[] mnumber={srchlist.get(position).getMnumber()};
                String snumber=srchlist.get(position).getNumber2();
                String tnumber=srchlist.get(position).getNumber3();
                ArrayList<String> blacklist=tinyDB.getListString("blacklist");
                ArrayList<String> blacklistname=tinyDB.getListString("blacklistname");
                mnumber[0] = mnumber[0].substring(1);

                AlertDialog.Builder bld=null;
                if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                bld.setMessage("آیا از افزودن  "+ name[0] +"  به لیست سیاه مطمئن هستید؟");
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
                        }
                        else {
                            blacklistname.add(name[0]);
                            blacklist.add(mnumber[0]);
                            if(!snumber.equals("")){
                                snumber = snumber.substring(1);
                                blacklist.add(snumber);
                            }
                            if(!tnumber.equals("")){
                                tnumber = tnumber.substring(1);
                                blacklist.add(tnumber);
                            }
                            tinyDB.putListString("blacklist",blacklist);
                            tinyDB.putListString("blacklistname",blacklistname);
                            adapter.notifyItemChanged(position);
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

                swiped=true;


            }
        }

    }

    //Implement item click and long click over recycler myview
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
                if(swiped)swiped=false;
                else onListItemSelect(position);
            }


        }));

    }


    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) context).startSupportActionMode(new Toolbar_ActionMode_Callback(context,adapter, mylist, false));
            if (tabLayout!=null)tabLayout.setVisibility(View.GONE);
        }
            // there are some selected items, start the mActionMode

        else if (!hasCheckedItems && mActionMode != null){
            mActionMode.finish();
            if (tabLayout!=null) tabLayout.setVisibility(View.VISIBLE);

        }
            // there no selected items, finish the mActionMode


        if (mActionMode != null){
            //set action mode title on item selection
            //mActionMode.setTitle(String.valueOf(adapter.getSelectedCount()) + "  انتخاب شده");
        menutxt.setText(String.valueOf(adapter.getSelectedCount()));
        }


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        if (tabLayout!=null)tabLayout.setVisibility(View.VISIBLE);
    }
    public void selectall(){
        adapter.selectall();
        menutxt.setText(String.valueOf(adapter.getSelectedCount()));
    }
    public void deselectall(){
        adapter.removeSelection();
        menutxt.setText("");
    }
    //Delete selected rows
    public void deleteRows() {

        final SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected id
        List<Integer> poslist=adapter.getSelectedPoses();
        int size=selected.size();

        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage("آیا از پاک کردن "+size+"مخاطب مطمئن هستید؟");
//        bld.setView(myview);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = (selected.size() - 1); j >= 0; j--) {
                    if (selected.valueAt(j)) {
                        //If current id is selected remove the item via key
                        int co=db.deleteFromContact(selected.keyAt(j)+"");
                    }
                }

                adapter.removeitem(poslist);
                Message.message(context,selected.size()+"مخاطب پاک شد",1);
            }
        });
        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        bld.create().show();
      //  AlertDialog dialog = new Dia(getActivity(),R.style.Theme_Dialog);
        //Loop all selected ids


    }

    public void sharecontact(String id){
        String sharetxt="";
        if (id.equals("p")){
            final SparseBooleanArray selected = adapter
                    .getSelectedIds();//Get selected id
            int size=selected.size();

            for (int j = (selected.size() - 1); j >= 0; j--) {
                int counter=0;
                if (selected.valueAt(j)) {
                    itema item=db.get‌‌ByIdFromContact(selected.keyAt(j)+"");
                    sharetxt=sharetxt+"نام: "+item.getFirstName()+"\nشماره تماس: "+item.getMnumber();
                    if (!item.getNumber2().equals(""))sharetxt=sharetxt+"\n شماره تماس دوم: "+item.getNumber2();
                    if (!item.getNumber3().equals(""))sharetxt=sharetxt+"\n شماره تماس سوم: "+item.getNumber3();
                }
                sharetxt=sharetxt+"\n\n";

            }
        }

        else {
            itema item=db.get‌‌ByIdFromContact(id);
            sharetxt=sharetxt+"نام: "+item.getFirstName()+"\nشماره تماس: "+item.getMnumber();
            if (!item.getNumber2().equals(""))sharetxt=sharetxt+"\n شماره تماس دوم: "+item.getNumber2();
            if (!item.getNumber3().equals(""))sharetxt=sharetxt+"\n شماره تماس سوم: "+item.getNumber3();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharetxt);
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }
    


    private class ReadContactTask extends AsyncTask<Void , Integer, Void> {
        private Context mContext;
        private ProgressBar progressBar;
        int cursize;
        private ReadContactTask(Context context){
            mContext=context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar= myview.findViewById(R.id.progrss);
            if(tinyDB.getBoolean("lighttheme")){
                // progressBar.setBackgroundColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.darktgray));
                // progressBar.setOutlineAmbientShadowColor(getActivity().getResources().getColor(R.color.sidebarred));
                progtxt.setTextColor(context.getResources().getColor(R.color.darktgray));
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            List<contactmodel> list = new ArrayList<>();
            ContentResolver contentResolver = mContext.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            assert cursor != null;
            cursize=cursor.getCount();
           Handler mainHandler = new Handler(getMainLooper());
/*
            mainHandler.post(new Runnable() {
                @Override

                public void run() {


                }
            });
*/


            int counter=0;


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    publishProgress(counter);
                    counter++;

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id)));

                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
                        Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                        Bitmap photo = null;
                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                        }
                        assert cursorInfo != null;
                        while (cursorInfo.moveToNext()) {
                            contactmodel info = new contactmodel();
                            info.id = id;
                            info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            info.photo = photo;
                            info.photoURI = pURI;
                            list.add(info);



                        }

                        cursorInfo.close();
                    }
                }
                cursor.close();
                // Message.message(this,"read finish",1);
            }

            String curname="";
            String curnum="";
            String curnum2="";
            String curnum3="";
            int contactcounter=0;
            int phonecounter =0;
            int size=list.size();
            // Message.message(getApplicationContext(),size+"   size",1);
            if (size!=0){
                for (int i=0;i<size;i++){
                    contactmodel model=list.get(i);
                    // publishProgress(counter);
                    String name=model.getname();
                    String number=model.getMobileNumber();
                    //String numberunghanged=number;
                    number = number.replace(" ", "");
                    number = number.replace("-", "");
                    if (number.startsWith("+98")){number =number.replace("+98", "0");}
                    if ((curname.equals(name)&&curnum.equals(number))||(curname.equals(name)&&curnum2.equals(number))){}
                    else if (curname.equals(name) && phonecounter == 0){
                        phonecounter++;
                        curnum2=number;
                        db.updateP1Contact(contactcounter+"",number);
                    }

                    else if (curnum.equals(name)&& !curnum.equals(number) ||!curnum2.equals(number) && phonecounter ==1){
                        phonecounter++;
                        db.updateP2Contact(contactcounter+"",number);
                    }
                    else {
                        Bitmap avatar=model.getPhoto();
                        String savatar="";
                        if (avatar!=null) savatar=Encodeimg.encodeimg(avatar);
                        Long sucsess= db.insertdataContact(name,number,"","","","","",
                                "","","","","",savatar);
                        contactcounter++;
                        phonecounter=0;
                        curnum=number;
                        curnum2="";
                    }

                    curname=name;

                }
                tinyDB.putInt("contactinputed",1);
                Fragment frag=flist.getFragb();
                ((recentconfrag) frag).getCallDetails(getActivity(),0);
                ((recentconfrag) frag).writelogtoDB(getActivity());

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tinyDB.getBoolean("firstrestore")&&tinyDB.getBoolean("ispremium")){
                            if(backupDB.fileexists()){

                                AlertDialog.Builder bld=null;
                                if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                                bld.setCancelable(false);
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


                            tinyDB.putBoolean("firstrestore",false);
                        }

                    }
                });


            }

            // update the list myview

            return null;
        }



        @Override
        protected void onPostExecute(Void param)
        {
            createRecyclelist();
            sidebarmanager();
            tinyDB.putInt("contactinputed",1);
            progressBar.setVisibility(View.GONE);
            progtxt.setVisibility(View.GONE);

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            progressBar.setMax(cursize);
            progressBar.setProgress(values[0]);
            progtxt.setVisibility(View.VISIBLE);
            progtxt.setText("مخاطب وارد شده ..."+ values[0]+"\n لطفا صبر کنید...");

        }
    }


    void checkreaded(){

         if(tinyDB.getInt("contactinputed")==0) {
            ReadContactTask task=new ReadContactTask(context);
            task.execute();
            createRecyclelist();
            implementRecyclerViewClickListeners();
        }
        else createRecyclelist();
        implementRecyclerViewClickListeners();
    }



    void sidebarmanager(){
        ContactScrollerAdapter mContactScrollerAdapter = new ContactScrollerAdapter(mylist);
        List<Section> seclist= mContactScrollerAdapter.getsectionlist();
        final HashMap<String,Integer> indexes=new HashMap<>();
        List<Section> newseclist=new ArrayList<>();
        int size=seclist.size();
        if (size>58) {
            for (int j=0;j<size;j+=2){
                Section s=seclist.get(j);
                newseclist.add(s);
            }
        }

        else if (size>29 && size<42){
            int counter=0;
            for (int j=0;j<size;j++){
                if (counter>2){
                    counter=0;
                }
                else {
                    Section s=seclist.get(j);
                    newseclist.add(s);
                    counter++;
                }

            }

        }

        else newseclist=seclist;

        String[] sectitle=new String[newseclist.size()];
        for (int i=0;i<newseclist.size();i++){
            Section sec=newseclist.get(i);
            //Message.message(getActivity(),sec.getTitle()+"    "+ sec.getIndex(),1);
            sectitle[i]=sec.getTitle();
            int pos=sec.getIndex();
            indexes.put(sec.getTitle(),pos);
        }


        Arrays.sort(sectitle);
        sideBar.setIndexItems(sectitle) ;
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {

                int pos=indexes.get(index);
                //recycle.scrollToPosition(pos);
                mLayoutManager.scrollToPositionWithOffset(pos, 0);

            }
        });
    }


    void alert(String message) {
        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);        bld.setMessage(message);
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
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
