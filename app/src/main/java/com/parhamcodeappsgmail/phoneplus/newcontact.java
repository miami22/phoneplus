package com.parhamcodeappsgmail.phoneplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Tools.Decodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Encodeimg;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;
import com.parhamcodeappsgmail.phoneplus.Tools.Snacktoast;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class newcontact extends AppCompatActivity {
    ImageView avater;
    public static final int REQUEST_IMAGE = 100;
    public String avatrstring="";
    int id;
    itema item;
    dbAdapter db;
    String name;
    String phone1;
    String phone2;
    String phone3;
    String insta;
    String teleg;
    String whatsup;
    String address;
    String date;
    String datetitle;
    String email;
    String info;
    Bitmap bitavat;
    int editt =0;
    Button save,cancel,chdate,clear;
    EditText tname,tnumber,tnumber2,tnumber3,taddress,temail,tinsta,tteleg,tinfo,tdatetitle;
    TextView tdate;
    TinyDB tinyDB;
    Bitmap bitmap;
    boolean saveokflag=false;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcontact);

        tinyDB=new TinyDB(this);
        clear=findViewById(R.id.clear);
        tname=findViewById(R.id.Nname);
        tname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if(checkduplicatecontact()){
                        AlertDialog.Builder bld=null;
                        if (tinyDB.getBoolean("lighttheme"))bld= new AlertDialog.Builder(newcontact.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        else bld= new AlertDialog.Builder(newcontact.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);                        bld.setMessage("نام وارد شده در مخاطبین موجود است.\n" +
                                "لطفا نام مخاطب را تغییر دهید");
                        bld.setNeutralButton("تایید", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        bld.create().show();
                        saveokflag=false;
                    }

                }
                else saveokflag=true;
            }
        });
        tnumber=findViewById(R.id.Np1);
        tnumber2=findViewById(R.id.Np2);
        tnumber3=findViewById(R.id.Np3);
        taddress=findViewById(R.id.Nadd);
        temail=findViewById(R.id.Nemail);
        tinsta=findViewById(R.id.Ninsta);
        tdate=findViewById(R.id.Ndate);
        avater=findViewById(R.id.eavatar);
        tteleg=findViewById(R.id.Ntele);
        tinfo=findViewById(R.id.Ninfo);
        tdatetitle=findViewById(R.id.Ndatetitle);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tdate.setText("");
            }
        });
        chdate=findViewById(R.id.Nchosedate);
        chdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickdata();

            }
        });
        if (tinyDB.getBoolean("first7")){
            tinyDB.putBoolean("first7",false);
            new MaterialTapTargetPrompt.Builder(newcontact.this)
                    .setTarget(chdate)
                    .setPrimaryText("یادآور مخاطب")
                    .setSecondaryText("در این قسمت برای هر مخاطب، میتوانید یادآور تعیین کنید")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                            }
                        }
                    })
                    .show();
        }
        save=findViewById(R.id.Nsave);

        cancel=findViewById(R.id.Ncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db=new dbAdapter(this);



        editt = Objects.requireNonNull(getIntent().getExtras()).getInt("edit");
        type=getIntent().getExtras().getString("type");
        assert type != null;
        if (editt ==1&&type.equals("mu")){
            id = getIntent().getExtras().getInt("id");
            item=db.get‌‌ByIdFromContact(id+"");
            initvalues();
            save.setText("ذخیره تغییرات");
        }
        else if (editt ==1&&type.equals("li")||type.equals("mi")){
            id = getIntent().getExtras().getInt("id");
            item=db.get‌‌ByIdFromContact(id+"");
            initvalues();
            save.setText("ذخیره تغییرات");
            String cons = getIntent().getExtras().getString("cons");
            assert cons != null;
            if (cons.equals("تلگرام"))tteleg.requestFocus();
            else if (cons.equals("اینستاگرام"))tinsta.requestFocus();

        }
        else if (editt ==1&&type.equals("d")||type.equals("ls")){
            String number=getIntent().getExtras().getString("number");
            tnumber.setText(number);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveinfo();
            }


        });




        avater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectandcrop.showImagePickerOptions(newcontact.this, new selectandcrop.PickerOptionListener() {
                    @Override
                    public void onTakeCameraSelected() {
                        launchCameraIntent();
                    }

                    @Override
                    public void onChooseGallerySelected() {
                        launchGalleryIntent();
                    }
                });
            }
        });


    }

    private void launchCameraIntent() {
        Intent intent = new Intent(newcontact.this, selectandcrop.class);
        intent.putExtra(selectandcrop.INTENT_IMAGE_PICKER_OPTION, selectandcrop.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(selectandcrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(selectandcrop.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(selectandcrop.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(selectandcrop.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(selectandcrop.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(selectandcrop.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(newcontact.this, selectandcrop.class);
        intent.putExtra(selectandcrop.INTENT_IMAGE_PICKER_OPTION, selectandcrop.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(selectandcrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(selectandcrop.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(selectandcrop.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(selectandcrop.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(selectandcrop.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(selectandcrop.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    avater.setImageBitmap(bitmap);

                    avatrstring= Encodeimg.convertBitmapToString(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void initvalues(){
        name=item.getFirstName();tname.setText(name);
        phone1=item.getMnumber();tnumber.setText(phone1);
        phone2=item.getNumber2();tnumber2.setText(phone2);
        phone3=item.getNumber3();tnumber3.setText(phone3);
        insta=item.getInsta(); tinsta.setText(insta);
        teleg=item.getTeleg(); tteleg.setText(teleg);
        address=item.getAddress();taddress.setText(address);
        date=item.getDate();tdate.setText(date);
        email=item.getEmail();temail.setText(email);
        info=item.getInfo();tinfo.setText(info);
        datetitle=item.getDatetitle();tdatetitle.setText(info);
        if (!date.equals(""))clear.setVisibility(View.VISIBLE);
        avatrstring=item.getAvatar();
        if (avatrstring.equals("")){ }
        else {
            bitavat = Decodeimg.decodeBase64(avatrstring);
            avater.setImageBitmap(bitavat);
        }

    }

    void saveinfo(){
        String name=tname.getText().toString();
        String p1=tnumber.getText().toString();
        String p2=tnumber2.getText().toString();
        String p3=tnumber3.getText().toString();
        String insta=tinsta.getText().toString();
        String tele=tteleg.getText().toString();
        String email=temail.getText().toString();
        String date=tdate.getText().toString();
        String info=tinfo.getText().toString();
        String add=taddress.getText().toString();
        String datetit=tdatetitle.getText().toString();

        if (p1.isEmpty()||name.isEmpty()){
            Snacktoast.inform(this,"وارد کردن نام و یک شماره تلفن ضروری است",R.color.snacktext);
        }

        else if (type.equals("m")||type.equals("d")||type.equals("ls")){
            if(!saveokflag){
                Snacktoast.inform(this,"نام مخاطب تکراری است!",R.color.snacktext);
            }
            else{
                db.insertdataContact(name,p1,p2,p3,add,date,datetit,
                        tele,insta,"",email,info,avatrstring);
                Message.message(this,"مخاطب ذخیره شد",1);
                tinyDB.putInt("fromnew",1);
                tinyDB.putInt("uplog",1);
                if (tinyDB.getInt("saveconloc")==2){
                    savenewcontact();
                }
                finish();
            }

        }
        else {
            db.updateContact(id+"",name,p1,p2,p3,email,add,info,date,datetit,insta,tele,"",avatrstring);
           // Snacktoast.inform(this,"مخاطب به روز شد",R.color.snacktext2);
            Message.message(this,"تغییرات ذخیره شد",1);
            tinyDB.putInt("fromnew",1);
            tinyDB.putInt("uplog",1);
            ContentResolver cr= getContentResolver();
           // updateContactdevice(name,"912242",cr);
            finish();
        }

    }


    private void updatecontact() {

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
                                                                                 tdate.setText(newdate);
                                                                                 clear.setVisibility(View.VISIBLE);                                                                                 //timepick();


                                                                             }
                                                                         },
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay());

        datePickerDialog.setThemeDark(true);
        datePickerDialog.show(getFragmentManager(), "tpd");
    }



    void savenewcontact(){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactID = ops.size();

        // Adding insert operation to operations list
        // to insert a new raw contact in the table
        // ContactsContract.RawContacts
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
                        null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        tname.getText().toString()).build());

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, tnumber.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap!= null) { // If an image is selected
            // successfully
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75,
                    stream);

            // Adding insert operation to operations list
            // to insert Photo in the table ContactsContract.Data
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY,
                            1)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.Photo.PHOTO,
                            stream.toByteArray()).build());

            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // Executing all the insert operations as a single database
            // transaction
            getContentResolver().applyBatch(ContactsContract.AUTHORITY,
                    ops);

        } catch (RemoteException e) {

            Message.message(this,"ذخیره در دفتر تلفن دستگاه موفقیت آمیز نبود",1);
        } catch (OperationApplicationException e) {
            Message.message(this,"ذخیره در دفتر تلفن دستگاه موفقیت آمیز نبود",1);
        }

    }


    public void updateContactdevice(String name, String phone,ContentResolver cr)
    {

        String[] arr = {"DISPLAY_NAME","MIMETYPE","TYPE"};

        String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";
        String[] params = new String[] {name,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};


        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();


        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, params)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "5657")
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Sample Name 21")
                .build());
        String where1 = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";
        String[] params1 = new String[] {name,"vnd.android.cursor.item/name"};
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where1, params1)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Sample Name")
                .build());


        String[] params2 = new String[] {name,"vnd.android.cursor.item/email_v2"};
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where1, params2)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, "Hi There")
                .build());
        // phoneCur.close();

        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


      Boolean  checkduplicatecontact(){
        String newname=tname.getText().toString();
        Boolean result=false;
        if (!newname.isEmpty()){
            List<itema> list=db.getDataContact2();
            for (int i=0;i<list.size();i++){
                itema item=list.get(i);
                if (item.getFirstName().equals(newname)){
                    result=true;
                    break;
                }
                else result= false;
            }
        }
        else result=false;
        return  result;
      }

}
