package com.parhamcodeappsgmail.phoneplus.DataBase;

import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parhamcodeappsgmail.phoneplus.Fragment.Log.itemlog;
import com.parhamcodeappsgmail.phoneplus.Fragment.MainCon.itema;
import com.parhamcodeappsgmail.phoneplus.Fragment.NoLimitLog.Uitem;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;

import java.util.ArrayList;
import java.util.List;

public class dbAdapter {
    private DBhelper myhelper;


    public dbAdapter(Context context){
        myhelper=new DBhelper(context);
    }

    static class DBhelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME="myDatabase";
        private static final int DATABASE_VERSION=10;

        private static final String TABLE_LOG ="log";
        private static final String LOGID ="id";
        private static final String LOGNUMBER ="lognumber";
        private static final String LOGDATE ="logdate"; // sale date
        private static final String LOGDUR ="grp";
        private static final String LOGTYPE ="date";
        private static final String LOGTIME ="time";
        private static final String LOGSIM ="sim";
        private static final String LOGPRIMDATE ="primdate";
        private static final String LOGSEC ="sec";
        private static final String LOGAGE ="age";

        private static final String TABLE_NLOG ="nlog";
        private static final String NLOGID ="id";
        private static final String NLOGNUMBER ="lognumber";
        private static final String NLOGDATE ="logdate";
        private static final String NLOGDAYOFWEEK="logday";
        private static final String NLOGDUR ="due";
        private static final String NLOGTYPE ="type";
        private static final String NLOGCOUNT="count";
        private static final String NLOGSIM ="sim";
        private static final String NLOGSEPERATE ="seperator";
        private static final String NLOGSHAMSI ="shamsi";
        private static final String NLOGTIME ="time";


        private static final String TABLE_CONTACT ="tablecontact";
        private static final String CONID ="id";
        private static final String CONNAME ="name";
        private static final String PHONE1 ="p1";
        private static final String PHONE2 ="p2";
        private static final String PHONE3 ="p3";
        private static final String ADDRESS ="address";
        private static final String EVENT ="event";
        private static final String EVENTTITLE ="eventtitle";
        private static final String TELEGRAM ="telegram";
        private static final String INSTAGRAM ="insts";
        private static final String WHATSUP ="whatsup";
        private static final String EMAIL ="email";
        private static final String INFO ="info";
        private static final String IMAGE ="image";

        private static final String CREATE_TABLE="CREATE TABLE " + TABLE_LOG + " ("+ LOGID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+ LOGNUMBER +" VARCHAR (255) ,"+ LOGPRIMDATE +" VARCHAR (255) ,"+ LOGDATE +" VARCHAR (255) , "+ LOGDUR +" VARCHAR (255) , "+ LOGTYPE +" VARCHAR (255) , "+ LOGTIME +" VARCHAR (255), "+ LOGSEC +" VARCHAR (255), "+ LOGSIM +" VARCHAR (255));";

        private static final String CREATE_TABLESALE_CON ="CREATE TABLE " + TABLE_CONTACT + " ("+ CONID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+ CONNAME +" VARCHAR (255) , "+ PHONE1 +" VARCHAR (255) , "+ PHONE2 +" VARCHAR (255) , "+ PHONE3 +" VARCHAR (255) , "+ ADDRESS +" VARCHAR (255) , "+ EVENT +" VARCHAR (255), "+ EVENTTITLE +" VARCHAR (255) , "+ TELEGRAM +" VARCHAR (255) , "+ INSTAGRAM + " VARCHAR (255) , "
                + WHATSUP + " VARCHAR (255) , "+ EMAIL +" VARCHAR (255) , "+ INFO +" VARCHAR (255) , "+ IMAGE +" VARCHAR (255));";

        private static final String CREATE_TABLESALE_NLOG ="CREATE TABLE " + TABLE_NLOG + " ("+ NLOGID +
                " INTEGER PRIMARY KEY , "+ NLOGNUMBER +" VARCHAR (255) , "+ NLOGDATE +" VARCHAR (255) , "+ NLOGDAYOFWEEK +" VARCHAR (255) , "+ NLOGDUR +" VARCHAR (255) , "+ NLOGTYPE +" VARCHAR (255) , "+ NLOGCOUNT +" VARCHAR (255), "+ NLOGSIM +" VARCHAR (255) , "+ NLOGSEPERATE +" VARCHAR (255) , "+ NLOGSHAMSI + " VARCHAR (255) , " +
               NLOGTIME +" VARCHAR (255));";


        private static final String DROP_TABLE="DROP TABLE IF EXISTS "+ TABLE_LOG;

        private static final String DROP_TABLESALE="DROP TABLE IF EXISTS "+ TABLE_CONTACT;

        private static final String DROP_TABLENLOG="DROP TABLE IF EXISTS "+ TABLE_NLOG;



        private Context context;



        public DBhelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_TABLESALE_CON);
                db.execSQL(CREATE_TABLESALE_NLOG);
                // Message.message(context,"table created",2);
            } catch (Exception e){
                Message.message(context,""+e,2);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                db.execSQL(DROP_TABLESALE);



                onCreate(db);
            } catch (Exception e){
            //    Message.message(context,""+e,1);
            }
        }


    }



    public long insertdataContact (String name, String phone1,String phone2, String phone3,String address,String date,String datetitle, String telegram,String instagram,String whatsup, String email , String info , String image){
        SQLiteDatabase dbb=myhelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBhelper.CONNAME,name);
        contentValues.put(DBhelper.PHONE1,phone1);
        contentValues.put(DBhelper.PHONE2,phone2);
        contentValues.put(DBhelper.PHONE3,phone3);
        contentValues.put(DBhelper.ADDRESS,address);
        contentValues.put(DBhelper.EVENT,date);
        contentValues.put(DBhelper.TELEGRAM,telegram);
        contentValues.put(DBhelper.INSTAGRAM,instagram);
        contentValues.put(DBhelper.WHATSUP,whatsup);
        contentValues.put(DBhelper.EMAIL,email);
        contentValues.put(DBhelper.INFO,info);
        contentValues.put(DBhelper.IMAGE,image);
        contentValues.put(DBhelper.EVENTTITLE,datetitle);
        long id=dbb.insert(DBhelper.TABLE_CONTACT,null,contentValues);
        return id;
    }

    public long insertdataLog (String number,String primdate, String date,String time, String dur,String type,String sim,String sec){
        SQLiteDatabase dbb=myhelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBhelper.LOGNUMBER,number);
        contentValues.put(DBhelper.LOGDATE,date);
        contentValues.put(DBhelper.LOGTIME,time);
        contentValues.put(DBhelper.LOGDUR,dur);
        contentValues.put(DBhelper.LOGTYPE,type);
        contentValues.put(DBhelper.LOGSIM,sim);
        contentValues.put(DBhelper.LOGPRIMDATE,primdate);
        contentValues.put(DBhelper.LOGSEC,sec);

        long id=dbb.insert(DBhelper.TABLE_LOG,null,contentValues);
        return id;
    }


    public List<Long> insertdataNLog (List<itemlog> list){
        SQLiteDatabase dbb=myhelper.getWritableDatabase();
        List<Long> result=new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            itemlog itemlog=list.get(i);
            ContentValues contentValues=new ContentValues();
            contentValues.put(DBhelper.NLOGID,itemlog.getId());
            contentValues.put(DBhelper.NLOGNUMBER,itemlog.getNumber());
            contentValues.put(DBhelper.NLOGDATE,itemlog.getdate());
            contentValues.put(DBhelper.NLOGDAYOFWEEK,itemlog.getDayofweek());
            contentValues.put(DBhelper.NLOGDUR,itemlog.getDuration());
            contentValues.put(DBhelper.NLOGTYPE,itemlog.getType());
            contentValues.put(DBhelper.NLOGCOUNT,itemlog.getCount());
            contentValues.put(DBhelper.NLOGSIM,itemlog.getSimnum());
            contentValues.put(DBhelper.NLOGSEPERATE,itemlog.getSeperator());
            contentValues.put(DBhelper.NLOGSHAMSI,itemlog.getShamsi());
            contentValues.put(DBhelper.NLOGTIME,itemlog.getTime());
            long id=dbb.insert(DBhelper.TABLE_NLOG,null,contentValues);
            result.add(id);
        }

        return result;
    }

    public long insertItemNLog (itemlog itemlog){
        SQLiteDatabase dbb=myhelper.getWritableDatabase();
        List<Long> result=new ArrayList<>();

            ContentValues contentValues=new ContentValues();
            contentValues.put(DBhelper.NLOGID,itemlog.getId());
            contentValues.put(DBhelper.NLOGNUMBER,itemlog.getNumber());
            contentValues.put(DBhelper.NLOGDATE,itemlog.getdate());
            contentValues.put(DBhelper.NLOGDAYOFWEEK,itemlog.getDayofweek());
            contentValues.put(DBhelper.NLOGDUR,itemlog.getDuration());
            contentValues.put(DBhelper.NLOGTYPE,itemlog.getType());
            contentValues.put(DBhelper.NLOGCOUNT,itemlog.getCount());
            contentValues.put(DBhelper.NLOGSIM,itemlog.getSimnum());
            contentValues.put(DBhelper.NLOGSEPERATE,itemlog.getSeperator());
            contentValues.put(DBhelper.NLOGSHAMSI,itemlog.getShamsi());
            contentValues.put(DBhelper.NLOGTIME,itemlog.getTime());
            long id=dbb.insert(DBhelper.TABLE_NLOG,null,contentValues);
            result.add(id);


        return id;
    }



    public List<Uitem> getDataLog() {

        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {DBhelper.LOGID, DBhelper.LOGNUMBER, DBhelper.LOGDATE, DBhelper.LOGDUR, DBhelper.LOGTYPE,DBhelper.LOGTIME,DBhelper.LOGSIM,DBhelper.LOGPRIMDATE,DBhelper.LOGSEC};
        Cursor cursor = db.query(DBhelper.TABLE_LOG, columns, null, null, null, null, DBhelper.LOGPRIMDATE+" DESC");
        List<Uitem> myList=new ArrayList<>();

        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(DBhelper.LOGID));
            String number = cursor.getString(cursor.getColumnIndex(DBhelper.LOGNUMBER));
            String date = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDATE));
            String dur = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDUR));
            String type = cursor.getString(cursor.getColumnIndex(DBhelper.LOGTYPE));
            String time = cursor.getString(cursor.getColumnIndex(DBhelper.LOGTIME));
            String sim = cursor.getString(cursor.getColumnIndex(DBhelper.LOGSIM));
            String primdate = cursor.getString(cursor.getColumnIndex(DBhelper.LOGPRIMDATE));
            String sec = cursor.getString(cursor.getColumnIndex(DBhelper.LOGSEC));

            Uitem item=new Uitem(cid,number,primdate,date,time,dur,type,sim,sec);
            myList.add(item);

        }
        cursor.close();
        return myList;
    }

    public List<itemlog> getDataNLog() {

        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {DBhelper.NLOGID, DBhelper.NLOGNUMBER, DBhelper.NLOGDATE, DBhelper.NLOGDAYOFWEEK, DBhelper.NLOGDUR,DBhelper.NLOGTYPE,DBhelper.NLOGCOUNT,DBhelper.NLOGSIM,DBhelper.NLOGSEPERATE,DBhelper.NLOGSHAMSI,DBhelper.NLOGTIME};
        Cursor cursor = db.query(DBhelper.TABLE_NLOG, columns, null, null, null, null, DBhelper.NLOGDATE+" DESC");
        List<itemlog> myList=new ArrayList<>();

        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(DBhelper.NLOGID));
            String number = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGNUMBER));
            String date = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDATE));
            String dayofweek = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDAYOFWEEK));
            String dur = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDUR));
            String type = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTYPE));
            String count = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGCOUNT));
            String sim = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSIM));
            String seperator = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSEPERATE));
            String shamsi = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSHAMSI));
            String time = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTIME));

            if (cursor.getPosition()>1000)break;

            itemlog item=new itemlog(cid,number,date,dayofweek,dur,type,count,sim,seperator,shamsi,time);
            myList.add(item);
        }
        cursor.close();
        return myList;
    }

    public itemlog getFirtNLog() {

        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {DBhelper.NLOGID, DBhelper.NLOGNUMBER, DBhelper.NLOGDATE, DBhelper.NLOGDAYOFWEEK, DBhelper.NLOGDUR,DBhelper.NLOGTYPE,DBhelper.NLOGCOUNT,DBhelper.NLOGSIM,DBhelper.NLOGSEPERATE,DBhelper.NLOGSHAMSI,DBhelper.NLOGTIME};
        Cursor cursor = db.query(DBhelper.TABLE_NLOG, columns, null, null, null, null, DBhelper.NLOGDATE+" DESC");


        cursor.moveToFirst();
            int cid = cursor.getInt(cursor.getColumnIndex(DBhelper.LOGID));
            String number = cursor.getString(cursor.getColumnIndex(DBhelper.LOGNUMBER));
            String date = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDATE));
            String dayofweek = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDAYOFWEEK));
            String dur = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDUR));
            String type = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTYPE));
            String count = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGCOUNT));
            String sim = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSIM));
            String seperator = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSEPERATE));
            String shamsi = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSHAMSI));
            String time = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTIME));


            itemlog item=new itemlog(cid,number,date,dayofweek,dur,type,count,sim,seperator,shamsi,time);


        cursor.close();
        return item;
    }



    public  List<itema> getDataContact2 () {

        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {DBhelper.CONID,DBhelper.CONNAME, DBhelper.PHONE1, DBhelper.PHONE3, DBhelper.ADDRESS, DBhelper.TELEGRAM,DBhelper.INSTAGRAM,DBhelper.PHONE2,DBhelper.IMAGE,DBhelper.INFO,DBhelper.WHATSUP,DBhelper.EMAIL,DBhelper.EVENT,DBhelper.EVENTTITLE};
        Cursor cursor = db.query(DBhelper.TABLE_CONTACT, columns, null, null, null, null, DBhelper.CONNAME);
        List<itema> myList=new ArrayList<>();


        while (cursor.moveToNext()) {
           try {
               int id = cursor.getInt(cursor.getColumnIndex(DBhelper.CONID));
               String name = cursor.getString(cursor.getColumnIndex(DBhelper.CONNAME));
               String phone1 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE1));
               String phone2 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE2));
               String phone3 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE3));
               String add = cursor.getString(cursor.getColumnIndex(DBhelper.ADDRESS));
               String date = cursor.getString(cursor.getColumnIndex(DBhelper.EVENT));
               String telegram = cursor.getString(cursor.getColumnIndex(DBhelper.TELEGRAM));
               String insta = cursor.getString(cursor.getColumnIndex(DBhelper.INSTAGRAM));
               String whatsup = cursor.getString(cursor.getColumnIndex(DBhelper.WHATSUP));
               String email = cursor.getString(cursor.getColumnIndex(DBhelper.EMAIL));
               String info = cursor.getString(cursor.getColumnIndex(DBhelper.INFO));
               String image = cursor.getString(cursor.getColumnIndex(DBhelper.IMAGE));
               String datetitle = cursor.getString(cursor.getColumnIndex(DBhelper.EVENTTITLE));
               itema item=new itema(id,name,phone1,phone2,phone3,add,email,insta,telegram,whatsup,image,info,date,datetitle);

               myList.add(item);
           }
           catch (Exception e){
               e.printStackTrace();
           }

        }
        cursor.close();
        return myList;
    }

    public  MutableLiveData<List<itema>> getDataContactLive () {

        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {DBhelper.CONID,DBhelper.CONNAME, DBhelper.PHONE1, DBhelper.PHONE3, DBhelper.ADDRESS, DBhelper.TELEGRAM,DBhelper.INSTAGRAM,DBhelper.PHONE2,DBhelper.IMAGE,DBhelper.INFO,DBhelper.WHATSUP,DBhelper.EMAIL,DBhelper.EVENT,DBhelper.EVENTTITLE};
        Cursor cursor = db.query(DBhelper.TABLE_CONTACT, columns, null, null, null, null, DBhelper.CONNAME);
        List<itema> myList=new ArrayList<>();
        MutableLiveData<List<itema>> result=new MutableLiveData<>();


        while (cursor.moveToNext()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(DBhelper.CONID));
                String name = cursor.getString(cursor.getColumnIndex(DBhelper.CONNAME));
                String phone1 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE1));
                String phone2 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE2));
                String phone3 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE3));
                String add = cursor.getString(cursor.getColumnIndex(DBhelper.ADDRESS));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.EVENT));
                String telegram = cursor.getString(cursor.getColumnIndex(DBhelper.TELEGRAM));
                String insta = cursor.getString(cursor.getColumnIndex(DBhelper.INSTAGRAM));
                String whatsup = cursor.getString(cursor.getColumnIndex(DBhelper.WHATSUP));
                String email = cursor.getString(cursor.getColumnIndex(DBhelper.EMAIL));
                String info = cursor.getString(cursor.getColumnIndex(DBhelper.INFO));
                String image = cursor.getString(cursor.getColumnIndex(DBhelper.IMAGE));
                String datetitle = cursor.getString(cursor.getColumnIndex(DBhelper.EVENTTITLE));
                itema item=new itema(id,name,phone1,phone2,phone3,add,email,insta,telegram,whatsup,image,info,date,datetitle);

                myList.add(item);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        cursor.close();
        result.postValue(myList);
        return result;
    }



    public String getAvatarfromContact(String a){
        String[] args={a};
        String re=null;
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor c=db.rawQuery("select "+ DBhelper.IMAGE +" from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.PHONE1 +" =?",args);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    re = c.getString(0);
                }
            } finally {
                c.close();
            }
        }

        return re;

    }

    public itema GetContactByName(String a){
        String [] args={a};

        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.CONNAME+" =?",args);
        itema item=new itema(0,"","","","","","","","","","","","","");
        if (cursor != null) {

            while(cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndex(DBhelper.CONID));
                String name = cursor.getString(cursor.getColumnIndex(DBhelper.CONNAME));
                String phone1 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE1));
                String phone2 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE2));
                String phone3 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE3));
                String add = cursor.getString(cursor.getColumnIndex(DBhelper.ADDRESS));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.EVENT));
                String telegram = cursor.getString(cursor.getColumnIndex(DBhelper.TELEGRAM));
                String insta = cursor.getString(cursor.getColumnIndex(DBhelper.INSTAGRAM));
                String whatsup = cursor.getString(cursor.getColumnIndex(DBhelper.WHATSUP));
                String email = cursor.getString(cursor.getColumnIndex(DBhelper.EMAIL));
                String info = cursor.getString(cursor.getColumnIndex(DBhelper.INFO));
                String image = cursor.getString(cursor.getColumnIndex(DBhelper.IMAGE));
                String datetitle = cursor.getString(cursor.getColumnIndex(DBhelper.EVENTTITLE));
                 item=new itema(id,name,phone1,phone2,phone3,add,email,insta,telegram,whatsup,image,info,date,datetitle);

            }
            cursor.close();

        }
        return item ;

    }


    public ArrayList<Uitem> GetLogByName(String a){
        String [] args={a};

        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ DBhelper.TABLE_LOG +" where "+ DBhelper.LOGNUMBER+" =?"+" ORDER BY "+DBhelper.LOGPRIMDATE+" DESC",args);
        ArrayList<Uitem> myList=new ArrayList<>();
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                int cid = cursor.getInt(cursor.getColumnIndex(DBhelper.LOGID));
                String number = cursor.getString(cursor.getColumnIndex(DBhelper.LOGNUMBER));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDATE));
                String dur = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDUR));
                String type = cursor.getString(cursor.getColumnIndex(DBhelper.LOGTYPE));
                String time = cursor.getString(cursor.getColumnIndex(DBhelper.LOGTIME));
                String sim = cursor.getString(cursor.getColumnIndex(DBhelper.LOGSIM));
                String primdate = cursor.getString(cursor.getColumnIndex(DBhelper.LOGPRIMDATE));
                String sec = cursor.getString(cursor.getColumnIndex(DBhelper.LOGSEC));


                 Uitem item = new Uitem(cid,number,primdate,date,time,dur,type,sim,sec);
                 myList.add(item);

            }
            cursor.close();
        }
        return myList;

    }

    public itemlog GetNLogById(String a){
        String [] args={a};
        itemlog item=new itemlog(0,"","","","","","","","","","");
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ DBhelper.TABLE_NLOG +" where "+ DBhelper.NLOGID+" =?",args);
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                int cid = cursor.getInt(cursor.getColumnIndex(DBhelper.LOGID));
                String number = cursor.getString(cursor.getColumnIndex(DBhelper.LOGNUMBER));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDATE));
                String dayofweek = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDAYOFWEEK));
                String dur = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGDUR));
                String type = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTYPE));
                String count = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGCOUNT));
                String sim = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSIM));
                String seperator = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSEPERATE));
                String shamsi = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGSHAMSI));
                String time = cursor.getString(cursor.getColumnIndex(DBhelper.NLOGTIME));


                 item=new itemlog(cid,number,date,dayofweek,dur,type,count,sim,seperator,shamsi,time);

            }
            cursor.close();
        }
        return item;

    }


    public String getNamefromContact(String a){
        String[] args={a};
        String re=null;
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor c=db.rawQuery("select "+ DBhelper.CONNAME +" from "+ DBhelper.TABLE_CONTACT +" where ("+ DBhelper.PHONE1 +" =?" + " OR  "+DBhelper.PHONE2 + "=?  " + " OR "+DBhelper.PHONE3+ "=? )"  ,args);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    re = c.getString(0);
                }
            } finally {
                c.close();
            }
        }

        return re;

    }

    public String getNamefromContact2(String a){
        String[] args={a};
        String re=null;
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor c=db.rawQuery("select "+ DBhelper.CONNAME +" from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.PHONE2 +" =?"  ,args);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    re = c.getString(0);
                }
            } finally {
                c.close();
            }
        }

        return re;

    }

    public String getNumberByName(String a){
        String[] args={a};
        String re=null;
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor c=db.rawQuery("select "+ DBhelper.PHONE1 +" from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.CONNAME +" =?",args);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    re = c.getString(0);
                }
            } finally {
                c.close();
            }
        }

        return re;

    }
    public String getIdByName(String a){
        String[] args={a};
        String re=null;
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor c=db.rawQuery("select "+ DBhelper.CONID +" from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.CONNAME +" =?",args);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    re = c.getString(0);
                }
            } finally {
                c.close();
            }
        }

        return re;

    }


    public itema get‌‌ByIdFromContact(String a){
        String [] args={a};
        itema item=new itema(0,"","","","","","","","","","","","","");
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ DBhelper.TABLE_CONTACT +" where "+ DBhelper.CONID +" =?",args);

        if (cursor != null) {
            while(cursor.moveToNext()){

                int id = cursor.getInt(cursor.getColumnIndex(DBhelper.CONID));
                String name = cursor.getString(cursor.getColumnIndex(DBhelper.CONNAME));
                String phone1 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE1));
                String phone2 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE2));
                String phone3 = cursor.getString(cursor.getColumnIndex(DBhelper.PHONE3));
                String add = cursor.getString(cursor.getColumnIndex(DBhelper.ADDRESS));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.EVENT));
                String telegram = cursor.getString(cursor.getColumnIndex(DBhelper.TELEGRAM));
                String insta = cursor.getString(cursor.getColumnIndex(DBhelper.INSTAGRAM));
                String whatsup = cursor.getString(cursor.getColumnIndex(DBhelper.WHATSUP));
                String email = cursor.getString(cursor.getColumnIndex(DBhelper.EMAIL));
                String info = cursor.getString(cursor.getColumnIndex(DBhelper.INFO));
                String image = cursor.getString(cursor.getColumnIndex(DBhelper.IMAGE));
                String datetitle = cursor.getString(cursor.getColumnIndex(DBhelper.EVENTTITLE));
                item=new itema(id,name,phone1,phone2,phone3,add,email,insta,telegram,whatsup,image,info,date,datetitle);

            }
            cursor.close();

        }
        return item;

    }



    public  int deleteFromContact(String uid) //delete one row of contact table
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uid};
        int count =db.delete(myhelper.TABLE_CONTACT,myhelper.CONID +" = ?",whereArgs);
        return  count;
    }
    public  int deleteFromUlog(String uid) //delete one row of contact table
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uid};
        int count =db.delete(DBhelper.TABLE_LOG, DBhelper.LOGID +" = ?",whereArgs);
        return  count;
    }



    public  int deleteFromNlog(String uid) //delete one row of contact table
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uid};
        int count =db.delete(DBhelper.TABLE_NLOG, DBhelper.NLOGID +" = ?",whereArgs);
        return  count;
    }




    public void ContactdeleteAll(){
        SQLiteDatabase db=myhelper.getWritableDatabase();

        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DBhelper.TABLE_CONTACT + "'");
        int cout=db.delete(myhelper.TABLE_CONTACT,null,null);
        Message.message(myhelper.context,cout+" ردیف پاک شد",1);
    }






    public ArrayList<String> searchCodefromCost(String a){
        String[] args={a};
        ArrayList<String> ret=new ArrayList<>();
        SQLiteDatabase db=myhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ DBhelper.TABLE_LOG +" where "+ DBhelper.LOGNUMBER +" =?",args);

        if (cursor != null) {
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(DBhelper.LOGID));
                String name = cursor.getString(cursor.getColumnIndex(DBhelper.LOGNUMBER));
                String count = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDATE));
                String price = cursor.getString(cursor.getColumnIndex(DBhelper.LOGDUR));
                String date = cursor.getString(cursor.getColumnIndex(DBhelper.LOGTYPE));

                ret.add(id);
                ret.add(name);
                ret.add(count);
                ret.add(price);
                ret.add(date);
            }

            cursor.close();
        }


        return ret;
    }







    public  int deletefromCost(String uid)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uid};

        int count =db.delete(DBhelper.TABLE_LOG, DBhelper.LOGID +" = ?",whereArgs);
        //Message.message(myhelper.context,count+" ردیف پاک شد",1);

        return  count;
    }



    public void Updatesale(String R,String a,String b,String c,String d,String e,String f){
        String[] select={R};
        SQLiteDatabase db=myhelper.getWritableDatabase();
        ContentValues args = new ContentValues();


        args.put(DBhelper.PHONE1,a);
        args.put(DBhelper.PHONE2,b);
        args.put(DBhelper.ADDRESS,c);
        args.put(DBhelper.PHONE3,d);
        args.put(DBhelper.TELEGRAM,e);
        args.put(DBhelper.INSTAGRAM,f);
        db.update(DBhelper.TABLE_CONTACT,args, DBhelper.CONNAME +" =?",select);

    }
    public void updateCountNlog(String a, String b){
        String[] s={a};
        SQLiteDatabase db=myhelper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(DBhelper.NLOGCOUNT,b);

        db.update(DBhelper.TABLE_NLOG,args, DBhelper.NLOGID +" =?",s);
    }

    public void updateSepNlog(String a, String b){
        String[] s={a};
        SQLiteDatabase db=myhelper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(DBhelper.NLOGSEPERATE,b);

        db.update(DBhelper.TABLE_NLOG,args, DBhelper.NLOGID +" =?",s);
    }

    public void updateP1Contact(String a, String b){
        String[] s={a};
        SQLiteDatabase db=myhelper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(DBhelper.PHONE2,b);

        db.update(DBhelper.TABLE_CONTACT,args, DBhelper.CONID +" =?",s);
    }

    public void updateP2Contact(String a, String b){
        String[] s={a};
        SQLiteDatabase db=myhelper.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(DBhelper.PHONE3,b);

        db.update(DBhelper.TABLE_CONTACT,args, DBhelper.CONID +" =?",s);
    }


    public void updateContact(String a, String name,String p1,String p2,String p3,String email,String add,String info,String date,String datetitle,String insta,String tele,String whats,String avat){
        String[] s={a};
        SQLiteDatabase db=myhelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
try{
    contentValues.put(DBhelper.CONNAME,name);
    contentValues.put(DBhelper.PHONE1,p1);
    contentValues.put(DBhelper.PHONE2,p2);
    contentValues.put(DBhelper.PHONE3,p3);
    contentValues.put(DBhelper.ADDRESS,add);
    contentValues.put(DBhelper.EVENT,date);
    contentValues.put(DBhelper.EVENTTITLE,datetitle);
    contentValues.put(DBhelper.TELEGRAM,tele);
    contentValues.put(DBhelper.INSTAGRAM,insta);
    contentValues.put(DBhelper.WHATSUP,whats);
    contentValues.put(DBhelper.EMAIL,email);
    contentValues.put(DBhelper.INFO,info);
    contentValues.put(DBhelper.IMAGE,avat);

    db.update(DBhelper.TABLE_CONTACT,contentValues, DBhelper.CONID +" =?",s);
}
catch (Exception e){e.printStackTrace();}
    }


}

