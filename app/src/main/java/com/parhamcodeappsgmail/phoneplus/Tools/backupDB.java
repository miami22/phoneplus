package com.parhamcodeappsgmail.phoneplus.Tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.parhamcodeappsgmail.phoneplus.DataBase.dbAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class backupDB {


    public static final String TAG = backupDB.class.getName();

    /** Directory that files are to be read from and written to **/
    protected static final File DATABASE_DIRECTORY =
            new File(Environment.getExternalStorageDirectory(),"PhonePlus");

    /** File path of Db to be imported **/
    protected static final File IMPORT_FILE =
            new File(DATABASE_DIRECTORY,"Phone+BackUp.db");

    public static final String PACKAGE_NAME = "com.parhamcodeappsgmail.phoneplus";
    public static final String DATABASE_NAME = "myDatabase";
    public static final String DATABASE_TABLE = "entryTable";

    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );

    /** Saves the application database to the
     * export directory under MyDb.db **/
    public static  boolean exportDb(){
        // if( ! SdIsPresent() ) return false;
        File dbFile = DATA_DIRECTORY_DATABASE;
        String filename = "Phone+BackUp.db";

        File exportDir = DATABASE_DIRECTORY;
        File file = new File(exportDir, filename);

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        try {
            file.createNewFile();
            copyFile(dbFile, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /** Replaces current database with the IMPORT_FILE if
     * import database is valid and of the correct editt **/
    public static boolean restoreDb(){
        // if( ! SdIsPresent() ) return false;

        File exportFile = DATA_DIRECTORY_DATABASE;
        File importFile = IMPORT_FILE;

        // if( ! checkDbIsValid(importFile) ) return false;

        if (!importFile.exists()) {
            Log.d(TAG, "File does not exist");
            return false;
        }

        try {
            exportFile.createNewFile();
            copyFile(importFile, exportFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean fileexists(){
        File importFile = IMPORT_FILE;
        if (importFile.exists()) {
            return true;
        }
        return false;
    }

    /** Imports the file at IMPORT_FILE **/
    protected static boolean importIntoDb(Context ctx){
        if( ! SdIsPresent() ) return false;

        File importFile = IMPORT_FILE;

        if( ! checkDbIsValid(importFile) ) return false;

        try{
            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase
                    (importFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = sqlDb.query(true, DATABASE_TABLE,
                    null, null, null, null, null, null, null
            );

            dbAdapter dbAdapter = new dbAdapter(ctx);
            // dbAdapter.open();

            final int titleColumn = cursor.getColumnIndexOrThrow("title");
            final int timestampColumn = cursor.getColumnIndexOrThrow("timestamp");

            // Adds all items in cursor to current database
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                // dbAdapter.createQuote(
                //     cursor.getString(titleColumn),
                //     cursor.getString(timestampColumn)
                // );
            }

            sqlDb.close();
            cursor.close();
            //  dbAdapter.close();
        } catch( Exception e ){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /** Given an SQLite database file, this checks if the file
     * is a valid SQLite database and that it contains all the
     * columns represented by DbAdapter.ALL_COLUMN_KEYS **/
    public static boolean checkDbIsValid( File db ){
        try{
            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase
                    (db.getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = sqlDb.query(true, DATABASE_TABLE,
                    null, null, null, null, null, null, null
            );

            // ALL_COLUMN_KEYS should be an array of keys of essential columns.
            // Throws exception if any column is missing
            // for( String s : DbAdapter.ALL_COLUMN_KEYS ){
            //   cursor.getColumnIndexOrThrow(s);
            //}

            sqlDb.close();
            cursor.close();
        } catch( IllegalArgumentException e ) {
            Log.d(TAG, "Database valid but not the right editt");
            e.printStackTrace();
            return false;
        } catch( SQLiteException e ) {
            Log.d(TAG, "Database file is invalid.");
            e.printStackTrace();
            return false;
        } catch( Exception e){
            Log.d(TAG, "checkDbIsValid encountered an exception");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    /** Returns whether an SD card is present and writable **/
    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


}
