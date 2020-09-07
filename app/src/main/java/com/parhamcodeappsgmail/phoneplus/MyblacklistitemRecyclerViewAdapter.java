package com.parhamcodeappsgmail.phoneplus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.Fragment.blacklistitemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyblacklistitemRecyclerViewAdapter extends RecyclerView.Adapter<MyblacklistitemRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final Context context;
    private final TinyDB tinyDB;
    private final List<String> names;
    dbAdapter db;

    public MyblacklistitemRecyclerViewAdapter(List<String> items, Context context) {
        mValues = items;
        this.context=context;
        db=new dbAdapter(context);
        tinyDB=new TinyDB(context);
        names=new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blacklistitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String item = mValues.get(position);
        ArrayList<String> blacklist=tinyDB.getListString("blacklist");
        holder.name.setText(item);
        itema itemM=db.GetContactByName(item);

        if (itemM.getMnumber().equals("")){
            holder.number1.setText(item);
        }
        else {

            String num1=itemM.getMnumber();
             String num2 = itemM.getNumber2();
            if(!num2.equals(""))num2=num2.substring(1);
             String num3 = itemM.getNumber3();
            if(!num3.equals(""))num3=num3.substring(1);
            String finalNum = num2;
            String finalNum1 = num3;
            if (!num1.equals("")){
                holder.number1.setText(num1);
            }
            if (!num2.equals("")){
                holder.number2.setVisibility(View.VISIBLE);
                holder.number2.setText("0"+num2);
                holder.number2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        ArrayList<String> blacklist=tinyDB.getListString("blacklist");

                        if (!b){
                            blacklist.remove(finalNum);
                        }
                        else blacklist.add(finalNum);
                        tinyDB.putListString("blacklist",blacklist);
                    }
                });

                if(blacklist.contains(finalNum)){
                    holder.number2.setChecked(true);
                }
                else holder.number2.setChecked(false);
            }

            if (!num3.equals("")){
                holder.number3.setVisibility(View.VISIBLE);
                holder.number3.setText("0"+num3);
                holder.number3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        ArrayList<String> blacklist=tinyDB.getListString("blacklist");
                        if (!b){

                            blacklist.remove(finalNum1);
                        }
                        else blacklist.add(finalNum1);
                        tinyDB.putListString("blacklist",blacklist);
                    }
                });
                if(blacklist.contains(finalNum1))holder.number3.setChecked(true);
                else holder.number3.setChecked(false);
            }

            //Message.message(context,num1,1);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> blist=tinyDB.getListString("blacklist");
            ArrayList<String> blistname=tinyDB.getListString("blacklistname");
            String nn=holder.name.getText().toString();
            @Override
            public void onClick(View view) {
                itema item=db.GetContactByName(nn);
                blistname.remove(nn);
                if (item.getMnumber().equals("")){
                    nn = nn.replace(" ", "");
                    nn=nn.substring(1);
                    blist.remove(nn);
                }
                else {

                    String num1=item.getMnumber();
                    String num2=item.getNumber2();
                    String num3=item.getNumber3();
                    if (!num1.equals("")){
                        num1=num1.substring(1);
                        blist.remove(num1);
                    }
                    if (!num2.equals("")){
                        num2=num2.substring(1);
                        blist.remove(num2);
                    }
                    if (!num3.equals("")){
                        num3=num3.substring(1);
                        blist.remove(num3);
                    }
                    //Message.message(context,num1,1);
                }
                /*
                if(Character.isDigit(nn.charAt(0))){
                    nn=nn.substring(1);
                    nn = nn.replace(" ", "");
                    blist.remove(nn);
                    //Message.message(context,nn,1);
                }
                else {
                    itema newitem=db.GetContactByName(nn);
                    String num1=newitem.getMnumber();
                    num1 = num1.substring(1);
                    String num2=newitem.getNumber2();
                    String num3=newitem.getNumber3();
                    blist.remove(num1);
                    if (!num2.equals("")){
                        num2=num2.substring(1);
                        blist.remove(num2);
                    }
                    if (!num3.equals("")){
                        num3=num3.substring(1);
                        blist.remove(num3);
                    }
                }
                 */

                removeitem(position);
                tinyDB.putListString("blacklist",blist);
                tinyDB.putListString("blacklistname",blistname);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final ImageButton delete;
        public final CheckBox number1, number2,number3;
        public String mItem;
        LinearLayout lay;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.blacktext);
            delete=view.findViewById(R.id.blackimage);
            number1 =view.findViewById(R.id.blackcall);
            number2 =view.findViewById(R.id.blacksms);
            number3 =view.findViewById(R.id.blackcall2);
            lay=view.findViewById(R.id.blacklay);
            if(tinyDB.getBoolean("lighttheme")){
                name.setTextColor(context.getResources().getColor(R.color.likeblack));
                number1.setTextColor(context.getResources().getColor(R.color.darktgray));
                number2.setTextColor(context.getResources().getColor(R.color.darktgray));
                number3.setTextColor(context.getResources().getColor(R.color.darktgray));
                lay.setBackgroundColor(context.getResources().getColor(R.color.lightgray));


            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }

    public void removeitem(int pos){

            mValues.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,mValues.size());
        }
}
