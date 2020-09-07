import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class smslogger {
    Context context;
    public smslogger(Context context){
        this.context=context;
    }
    protected void querySMS() {
        Uri uriSMS = Uri.parse("content://sms/");
        Cursor cur = context.getContentResolver().query(uriSMS, null, null, null, null);
        cur.moveToNext(); // this will make it point to the first record, which is the last SMS sent
        String body = cur.getString(cur.getColumnIndex("body")); //content of sms
        String add = cur.getString(cur.getColumnIndex("address")); //phone num
        String time = cur.getString(cur.getColumnIndex("date")); //date
        String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
        if (protocol == null)
            Toast.makeText(context, "Sending to "+add +
                    ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Receive from "+add +
                ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();

        /*logging action HERE...*/
        cur.close();
    }


    public class SMSObserver extends ContentObserver
    {
        smslogger smsLogger;

        public SMSObserver(smslogger smsLogger) {
            super(new Handler());
            this.smsLogger = smsLogger;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            smsLogger.querySMS();
        }
    }
}
