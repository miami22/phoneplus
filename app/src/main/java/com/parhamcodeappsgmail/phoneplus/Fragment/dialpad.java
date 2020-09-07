package com.parhamcodeappsgmail.phoneplus.Fragment;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parhamcodeappsgmail.phoneplus.CustomViewPager;
import com.parhamcodeappsgmail.phoneplus.DI.DaggeractivityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.DaggerfragmentComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityComponent;
import com.parhamcodeappsgmail.phoneplus.DI.activityModule;
import com.parhamcodeappsgmail.phoneplus.DI.fragmentComponent;
import com.parhamcodeappsgmail.phoneplus.DI.fragmentModule;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.mainconfrag;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.recycleAdaptermain;
import com.parhamcodeappsgmail.phoneplus.MainActivity;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;
import com.parhamcodeappsgmail.phoneplus.newcontact;
import com.skyfishjy.library.RippleBackground;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link dialpad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dialpad extends Fragment {

    @Inject
    TinyDB tinyDB;
    @Inject Context context;
    CustomViewPager viewpagerc;
    public EditText screen;

     MediaPlayer mp;
     LinearLayout dialpadcontainer;
     LinearLayout screenlay;




    public dialpad() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static dialpad newInstance(String param1, String param2) {
        dialpad fragment = new dialpad();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        DaggerfragmentComponent.builder().activityComponent(DaggeractivityComponent.builder().activityModule(new activityModule((AppCompatActivity)context)).build()).fragmentModule(new fragmentModule(this)).build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dialpad,container,false);
        screenlay=view.findViewById(R.id.screenlay);

        if (tinyDB.getInt("dialpadsound")==1)mp=MediaPlayer.create(context,R.raw.beep);
        else if(tinyDB.getInt("dialpadsound")==2) mp=MediaPlayer.create(context,R.raw.beep2);
        else if(tinyDB.getInt("dialpadsound")==2) mp=MediaPlayer.create(context,R.raw.beep2);
        else if(tinyDB.getInt("dialpadsound")==3) mp=MediaPlayer.create(context,R.raw.clickglass);
        else if(tinyDB.getInt("dialpadsound")==4) mp=MediaPlayer.create(context,R.raw.drop);
        screen=view.findViewById(R.id.screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screen.setShowSoftInputOnFocus(false);
        }

        screen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String input=screen.getText().toString();

                if (input.startsWith("*")||input.startsWith("#")){}
                else  {
                    if (viewpagerc.getCurrentItem()!=0)viewpagerc.setCurrentItem(0);
                   mainconfrag.adapter.filter(input);
                }
                if(input.isEmpty()) {
                    tinyDB.putBoolean("searchmod",false);
                    screenlay.setVisibility(View.GONE);
                }
                else {
                    tinyDB.putBoolean("searchmod",true);
                    screenlay.setVisibility(View.VISIBLE);
                }



            }
        });
        ImageView b1 = view.findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen.setText(screen.getText().toString()+"1");
                if (tinyDB.getInt("dialpadsound")!=0)mp.start();
             }
        });
        ImageView b2 = view.findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"2");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b3 = view.findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"3");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b4 = view.findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"4");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b5 = view.findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"5");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b6 = view.findViewById(R.id.b6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"6");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b7 = view.findViewById(R.id.b7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"7");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b8 = view.findViewById(R.id.b8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"8");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b9 = view.findViewById(R.id.b9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"9");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView b0 = view.findViewById(R.id.b0);
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"0");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });

        ImageView backs = view.findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDB.getInt("dialpadsound")!=0)mp.start();
                String input=screen.getText().toString();
                if (!input.isEmpty())screen.setText(removeLastChar(input));
            }
        });
        backs.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (tinyDB.getInt("dialpadsound")!=0)mp.start();
                screen.setText("");
                return false;
            }
        });


        ImageView star = view.findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"*");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });
        ImageView hash = view.findViewById(R.id.hash);
        hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.setText(screen.getText().toString()+"#");if (tinyDB.getInt("dialpadsound")!=0)mp.start();
            }
        });


        ImageView savecon = view.findViewById(R.id.savecon);
        savecon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tinyDB.getInt("dialpadsound")!=0)mp.start();
                Intent mIntent = new Intent(context, newcontact.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("number", screen.getText().toString());
                mBundle.putInt("edit", 1);
                mBundle.putString("type","d");
                mIntent.putExtras(mBundle);
                context.startActivity(mIntent);
            }
        });

        ImageView b01 = view.findViewById(R.id.b01);
        b01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makecall(screen.getText().toString(),0);if (tinyDB.getInt("dialpadsound")!=0)mp.start();
                mainconfrag.adapter.filter("");
                closethis();
            }
        });
        ImageView b02 = view.findViewById(R.id.b02);
        if (tinyDB.getBoolean("isdual")){
            savecon.setVisibility(View.VISIBLE);
            b01.setImageResource(R.drawable.simb);
            b02.setImageResource(R.drawable.sima);
            b02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makecall(screen.getText().toString(),1);if (tinyDB.getInt("dialpadsound")!=0)mp.start();
                    mainconfrag.adapter.filter("");
                    closethis();
                }
            });
        }
        else {
            savecon.setVisibility(View.GONE);
            b01.setImageResource(R.drawable.phone);
            b02.setImageResource(R.drawable.save);
            b02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tinyDB.getBoolean("sound"))mp.start();
                    Intent mIntent = new Intent(context, newcontact.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("number", screen.getText().toString());
                    mBundle.putInt("edit", 1);
                    mBundle.putString("type","d");
                    mIntent.putExtras(mBundle);
                    context.startActivity(mIntent);
                }
            });
        }




        PushDownAnim.setPushDownAnimTo(b1, b2, b3, b4, b5, b6, b7, b8, b9, b0,screen, backs, star, hash);
        dialpadcontainer=view.findViewById(R.id.dialpad);
        if(tinyDB.getBoolean("lighttheme")){
            dialpadcontainer.setBackgroundColor(context.getResources().getColor(R.color.transgray));
            screen.setTextColor(context.getResources().getColor(R.color.likeblack));
        }

        viewpagerc=((AppCompatActivity)context).findViewById(R.id.viewpagercc);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onDetach() {
        super.onDetach();
    }



    public String removes(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    void changecolor(ImageView imageView){
        int colorFrom = getResources().getColor(R.color.snacktext);
        int colorTo = getResources().getColor(R.color.colorPrimary);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                imageView.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
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


    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
void closethis(){
    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().remove(this).commitNow();
}

    public static void hideKeyboard(Activity activity, View viewToHide) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewToHide.getWindowToken(), 0);
    }

}
