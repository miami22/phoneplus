package com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.parhamcodeappsgmail.phoneplus.DI.DaggeractivityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.DaggerfragmentComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityModule;
import com.parhamcodeappsgmail.phoneplus.DI.fragmentModule;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.RecyclerClick_Listener;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.RecyclerTouchListener;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Toolbar_ActionMode_CallbackUltim.menutxtu;


public class NoLimitLog extends Fragment {

    @Inject TinyDB tinyDB;
    @Inject dbAdapter db;
    @Inject Context context;


    RecyclerView recycle;
    public  static recycleAdapterU adapter;
    LinearLayoutManager manager;
    private ActionMode mActionMode;
    List<Uitem> mylist;
    FloatingActionButton floatdate;
    TabLayout tabLayout;
    ConstraintLayout bottomlay;
    Boolean first1;
    ProgressBar progressBar;
    public NoLimitLog() {
    }


    public static NoLimitLog newInstance(String param1, String param2) {
        NoLimitLog fragment = new NoLimitLog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaggerfragmentComponent.builder().activityComponent(DaggeractivityComponent.builder().activityModule(new activityModule((AppCompatActivity)context)).build()).fragmentModule(new fragmentModule(this)).build()
                .inject(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first1=tinyDB.getBoolean("first1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_no_limit_log, container, false);
        recycle=view.findViewById(R.id.Urecycler);
        context=recycle.getContext();
        floatdate=view.findViewById(R.id.floatdate);
        floatdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickdata();
            }
        });
        tabLayout=((AppCompatActivity)context).findViewById(R.id.tablayoutC);
        bottomlay = ((AppCompatActivity)context).findViewById(R.id.bottomlayout);
        progressBar = ((AppCompatActivity)context).findViewById(R.id.mainprogress);

        ConstraintLayout mainlay=view.findViewById(R.id.mainlayout);
        if (tinyDB.getBoolean("lighttheme"))mainlay.setBackgroundColor(context.getResources().getColor(R.color.white));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        implementRecyclerViewClickListeners();
        createlist();
        progressBar.setVisibility(View.GONE);
        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 100);
         */


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        if(tinyDB.getBoolean("first1")){
            new MaterialTapTargetPrompt.Builder((AppCompatActivity) context)
                    .setTarget(floatdate)
                    .setPrimaryText("جستجو با تاریخ")
                    .setSecondaryText("انتخاب تاریخ برای فیلتر تماسها")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                                // Message.message(getActivity(),"clicked",1);
                            }
                        }
                    })
                    .show();
            tinyDB.putBoolean("first1",false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    void  createlist() {
        mylist = db.getDataLog();
        recycle.hasFixedSize();
        //Message.message(getActivity(),mylist.size()+"  Lsie",1);
        adapter = new recycleAdapterU(mylist, recycle.getContext());
        manager = new LinearLayoutManager(context);
        recycle.setLayoutManager(manager);
        recycle.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.devider));
        recycle.addItemDecoration(dividerItemDecoration);
        recycle.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist=0;
            boolean isVisible=true;
            static final int MINIMUM=25;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isVisible&&scrollDist>MINIMUM) {
                    // Scrolling up
                    floatdate.animate().translationY(floatdate.getHeight()+40)
                            .setInterpolator(new AccelerateInterpolator(2)).start();
                    scrollDist=0;
                    isVisible=false;

                } else if (!isVisible&&scrollDist<-MINIMUM) {
                    // Scrolling down
                    floatdate.animate().translationY(-40).setInterpolator(new
                            DecelerateInterpolator(2)).start();
                    scrollDist=0;
                    isVisible=true;

                }
                if ((isVisible&&dy>0)||(!isVisible&&dy<0)) {
                    scrollDist+=dy;
                }

            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {

                }
            }
        });

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
                   onListItemSelect(position);
            }
        }));
    }


    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) context).startSupportActionMode(new Toolbar_ActionMode_CallbackUltim(context,adapter,mylist,false));
            tabLayout.setVisibility(View.GONE);

        }
            // there are some selected items, start the mActionMode

        else if (!hasCheckedItems && mActionMode != null){
            mActionMode.finish();
        }
            // there no selected items, finish the mActionMode


        if (mActionMode != null){
            //mActionMode.setTitle(String.valueOf(adapter.getSelectedCount()) + "  انتخاب شده");
            menutxtu.setText(String.valueOf(adapter.getSelectedCount()));


        }
            //set action mode title on item selection


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        tabLayout.setVisibility(View.VISIBLE);
    }
    public void selectall(){
        adapter.selectall();
        menutxtu.setText(String.valueOf(adapter.getSelectedCount()));
    }
    public void deselectall(){
        adapter.removeSelection();
        menutxtu.setText("");
    }
    public void deleteRows() {

        final
        SparseBooleanArray selected = adapter.getSelectedIds();//Get selected id
        final SparseBooleanArray poses = adapter.getSelectedIds();//Get selected id
        List<Integer> poslist=adapter.getSelectedPoses();
        int size=selected.size();

        AlertDialog.Builder bld=null;
        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        else bld= new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        bld.setMessage("آیا از پاک کردن "+size+"رکورد مطمئن هستید؟");
        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0;j<selected.size();j++) {

                    //If current id is selected remove the item via key
                    int co=db.deleteFromUlog(selected.keyAt(j)+"");
                }

                adapter.removeitem(poslist);
                Message.message(context,selected.size()+"رکورد پاک شد",1);
            }
        });
        bld.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        bld.create().show();

    }

    void pickdata(){
        final ArrayList E=new ArrayList();
        PersianCalendar now = new PersianCalendar();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                                 // Toast.makeText(getActivity(), "" + year + "/" + monthOfYear + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
                                                                                 monthOfYear=monthOfYear+1;
                                                                                 String mnth="";
                                                                                 String day="";
                                                                                 if (monthOfYear<10){
                                                                                     mnth="0"+monthOfYear;
                                                                                 }
                                                                                 else mnth=monthOfYear+"";

                                                                                 if (dayOfMonth<10){
                                                                                     day="0"+dayOfMonth;
                                                                                 }
                                                                                 else day=dayOfMonth+"";

                                                                                 String newdate=year+"-"+mnth+"-"+day;
                                                                                 newdate = newdate.replace("-", "");

                                                                                 adapter.filter(Integer.parseInt(newdate)+"");
                                                                                 tinyDB.putBoolean("searchmod",true);

                                                                                 //timepick();


                                                                             }
                                                                         },
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay());

        datePickerDialog.setThemeDark(true);
        datePickerDialog.show(((AppCompatActivity)context).getFragmentManager(), "tpd");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }


    }

}
