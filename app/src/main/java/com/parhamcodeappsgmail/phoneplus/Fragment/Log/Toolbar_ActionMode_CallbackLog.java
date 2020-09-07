package com.parhamcodeappsgmail.phoneplus.Fragment.Log;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.MainActivity;
import com.parhamcodeappsgmail.phoneplus.R;

import java.util.List;

import static com.parhamcodeappsgmail.phoneplus.MainActivity.flist;

public class Toolbar_ActionMode_CallbackLog implements ActionMode.Callback {

    private Context context;
    private recycleAdapterlog recyclerView_adapter;
    //private rec listView_adapter;
    private List<itemlog> message_models;
    private boolean isListViewFragment;
    private LinearLayout checkkk,delet,share;
    public static TextView menutxtlog;
    private ImageView checkimage;
    private Boolean checkall=false;


    public Toolbar_ActionMode_CallbackLog(Context context, recycleAdapterlog recyclerView_adapter, List<itemlog> message_models, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        //this.listView_adapter = listView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.navigatemenu, menu);//Inflate the menu over action mode

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        //menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        MenuItem itemcheck= menu.findItem(R.id.check);

        checkkk = (LinearLayout) itemcheck.getActionView();
        checkimage=checkkk.findViewById(R.id.menuimage);
        checkkk.setClickable(true);
        checkkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment recyclerFragment=flist.getFragb();
                //Toast.makeText(context, "You selected checkall.", Toast.LENGTH_SHORT).show();//Show toast
                if (!checkall){
                    checkimage.setBackground(context.getResources().getDrawable(R.drawable.check));
                    if (recyclerFragment != null){
                        //If recycler fragment not null
                        ((recentconfrag) recyclerFragment).selectall();
                        //mode.finish();//Finish action mode
                        checkall=true;
                    }
                }
                else {
                    checkimage.setBackgroundResource(0);
                    if (recyclerFragment != null)((recentconfrag) recyclerFragment).deselectall();
                    checkall=false;

                }

            }
        });
        menutxtlog=checkkk.findViewById(R.id.counttxt);
        delet=checkkk.findViewById(R.id.menudel);
        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment recyclerFragment=flist.getFragb();
                if (recyclerFragment != null)
                    //If recycler fragment not null
                    ((recentconfrag) recyclerFragment).deleteRows();//delete selected rows
                mode.finish();//Finish action mode
            }
        });
        share=checkkk.findViewById(R.id.menushare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment recyclerFragment=flist.getFragb();
                if (recyclerFragment != null)
                    //If recycler fragment not null
                    ((recentconfrag) recyclerFragment).sharecontact();//delete selected rows
                mode.finish();//Finish action mode
            }
        });



        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First checkkk current fragment action mode
        if (isListViewFragment) {
           // listView_adapter.removeSelection();  // remove selection
            Fragment listFragment = new MainActivity().getFragment(1);//Get list fragment
           // if (listFragment != null)
                //((ListView_Fragment) listFragment).setNullToActionMode();//Set action mode null
        } else {
            recyclerView_adapter.removeSelection();  // remove selection
           // Fragment recyclerFragment = new MainActivity().getFragment(0);//Get recycler fragment
            Fragment recyclerFragment=flist.getFragb();
            if (recyclerFragment != null)
                ((recentconfrag) recyclerFragment).setNullToActionMode();//Set action mode null
        }
    }
}
