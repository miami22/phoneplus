package com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.CallLog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class recycleAdapterU extends  RecyclerView.Adapter<recycleAdapterU.MyViewHolder>{
    private List<Uitem> list;
    private Context context;
    private View aview;
    private dbAdapter db;
    private Calendar now;
    private String dateholder="";
    private int pos=-1;
    int firstclick=0;
    private View tempview;
    private SparseBooleanArray mSelectedItemsIds;
    private SparseBooleanArray marklist;
    TinyDB tinyDB;
    String srctext="";
    ArrayList<Uitem> ListFiltered;
    int fromsearch=0;

    public recycleAdapterU(List<Uitem> list, Context context) {
        this.list = list;
        this.context = context;

        db=new dbAdapter(context);
        now = Calendar.getInstance();
        mSelectedItemsIds = new SparseBooleanArray();
        marklist= new SparseBooleanArray();
        tinyDB=new TinyDB(context);
        this.ListFiltered = new ArrayList<>();
        this.ListFiltered.addAll(this.list);




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        aview= LayoutInflater.from(parent.getContext()).inflate(R.layout.ultralogitem,parent,false);
        return new MyViewHolder(aview);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Uitem item=ListFiltered.get(position);
        String number=item.getName();
        String date=item.getDate();
        String time=item.getTime();
        String durr=item.getDur();

        SpannableStringBuilder sb = new SpannableStringBuilder(number);
        Pattern word = Pattern.compile(srctext.toLowerCase());
        Matcher match = word.matcher(number.toLowerCase());

        while (match.find()) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.confirm)); //specify color here
            sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        holder.name.setText(sb);

        holder.date.setText(date);


        holder.time.setText(time);
        holder.dur.setText(durr);

        String sim=item.getSim();
        if (sim.equals("1"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mcin1));
        else if (sim.equals("2"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mcin2));
        else if (sim.equals("3"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.irancelln1));
        else if (sim.equals("4"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.irancelln2));
        else if (sim.equals("5"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.mcin));
        else if (sim.equals("6"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.irancelln));
        else if (sim.equals("7"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim11));
        else if (sim.equals("8"))holder.sim.setBackground(context.getResources().getDrawable(R.drawable.sim22));

        String type=item.getType();
        String calltype;
        int dircode = Integer.parseInt(type);
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                calltype = "OUTGOING";
                if(tinyDB.getBoolean("lighttheme")){
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.calloutblack));
                }
                else holder.type.setBackground(context.getResources().getDrawable(R.drawable.callout));
                break;
            case CallLog.Calls.INCOMING_TYPE:
                calltype = "INCOMING";
                if(tinyDB.getBoolean("lighttheme")){
                    holder.type.setBackground(context.getResources().getDrawable(R.drawable.callinblack));
                }
                else holder.type.setBackground(context.getResources().getDrawable(R.drawable.callin));

                break;

            case CallLog.Calls.MISSED_TYPE:
                calltype = "MISSED";
                holder.type.setBackground(context.getResources().getDrawable(R.drawable.callmissed));

                break;
        }

        if (marklist.get(position)){holder.itemlay.setBackgroundColor(R.color.transgray);}
        else holder.itemlay.setBackgroundColor(0);

/////////////////end of onbind
    }


    @Override
    public int getItemCount() {
        return ListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,time,dur;
        ImageView sim,type;
        ConstraintLayout itemlay;

        public MyViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.Uname);
            date=itemView.findViewById(R.id.Udate);
            time=itemView.findViewById(R.id.Utime);
            sim=itemView.findViewById(R.id.Usim);
            type=itemView.findViewById(R.id.Utype);
            dur=itemView.findViewById(R.id.Udur);
            itemlay=itemView.findViewById(R.id.ulogitem);
            /*

             */

                    if(tinyDB.getBoolean("lighttheme")){
                        name.setTextColor(context.getResources().getColor(R.color.likeblack));
                        date.setTextColor(context.getResources().getColor(R.color.darktgray));
                        time.setTextColor(context.getResources().getColor(R.color.darktgray));
                        dur.setTextColor(context.getResources().getColor(R.color.darktgray));
                    }
                }

    }




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
                int id=list.get(position).getId();
                //int count=Integer.parseInt(list.get(position).getCount());
                mSelectedItemsIds.put(id,value );
                marklist.put(position, value);

            }
            //com.parhamcodeappsgmail.phoneplus.Tools.Message.message(context,list.get(position).getid()+"//",1);
        }
        else{
            mSelectedItemsIds.delete(list.get(position).getId());
            marklist.delete(position);}


        notifyDataSetChanged();
    }

    public void selectall(){

        int size=ListFiltered.size();
        removeSelection();
        for (int i=0;i<size;i++){
            int id=ListFiltered.get(i).getId();
            mSelectedItemsIds.put(id, true);
            marklist.put(i, true);
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return marklist.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;
    }
    public List<Integer> getSelectedPoses() {
        List<Integer> posss=new ArrayList<>();
        for (int g=0;g<marklist.size();g++){
            posss.add(marklist.keyAt(g));
        }
        Collections.sort(posss, Collections.reverseOrder());
        return posss; }









    ////این قسمت باعث می شود با اسکرول کردن لیست پوزیشن آیتمها عوض نشود
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void removeitem(List<Integer> pos){
        for (int i=0;i<pos.size();i++){
            int k=pos.get(i);
            ListFiltered.remove(k);
            notifyItemRemoved(k);
            notifyItemRangeChanged(k,list.size());
        }

    }



    public static class MyObject implements Comparable<MyObject> {

        private Date dateTime;

        public Date getDateTime() {
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

    public void filter(final String text) {
        srctext=text;
        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                ListFiltered.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    ListFiltered.addAll(list);
                    fromsearch=0;

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Uitem item : list) {
                        String date=item.getDate();
                        date = date.replace("-", "");
                        String dd=Integer.parseInt(date)+"";
                        if (item.getName().toLowerCase().contains(text.toLowerCase())||dd.contains(text.toLowerCase())) {
                            // Adding Matched items
                            ListFiltered.add(item);
                        }
                    }
                    fromsearch=1;
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }



}