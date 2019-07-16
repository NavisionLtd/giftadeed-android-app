package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.Contact;
import giftadeed.kshantechsoft.com.giftadeed.Notifications.Notification;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Darshan on 09/14/2017.
 */

public class DBGAD {
    private static final String SAMPLE_DB_NAME = "GAD_DB.db";
    public static Context context;
    SQLiteDatabase db;
    private String c;
    public static Cursor msgdata;

    public DBGAD(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase("GAD_DB.db", MODE_PRIVATE, null);
        c = ("CREATE TABLE IF NOT EXISTS message(ID INTEGER PRIMARY KEY AUTOINCREMENT,created_date datetime,time text,nt_type text,tag_type text,geopoint text,Need_Name text,Tag_Id text,Seen text,DistanceInKms text)");
        db.execSQL(c);
    }

    public void insert_msg(String created_date, String time, String nt_type, String tag_type, String geopoint, String Need_Name, String tag_id, String seen, String distance) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd_str = sdf.format(date);
        String query = "INSERT INTO message(created_date,time,nt_type,tag_type,geopoint,Need_Name,Tag_Id,Seen,DistanceInKms) VALUES('" + created_date + "','" + time + "','" + nt_type + "','" + tag_type + "','" + geopoint + "','" + Need_Name + "','" + tag_id + "','" + seen + "','" + distance + "');";
        db.execSQL(query);
    }

    public void delete_All_messages() {
        db.execSQL("DELETE FROM message");
    }

    public ArrayList<Notification> getDateRecords(String fromdate, String enddate) {
        ArrayList<Notification> nList = new ArrayList<Notification>();
        String sql = "SELECT * FROM message WHERE created_date BETWEEN '" + fromdate + "' AND  '" + enddate + "'";
        Cursor cursor = db.rawQuery(sql, null);
        Log.d("query",sql);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification();
                notification.setDate(cursor.getString(1));
                notification.setTime(cursor.getString(2));
                notification.setNtType(cursor.getString(3));
                notification.setTagType(cursor.getString(4));
                notification.setGeopoint(cursor.getString(5));
                notification.setNeedName(cursor.getString(6));
                notification.setTagid(cursor.getString(7));
                notification.setSeen(cursor.getString(8));
                notification.setDistanceInKms(cursor.getString(9));
                nList.add(notification);
            } while (cursor.moveToNext());
        }
        return nList;
    }

    // Getting Count
    public int getMessagesCount() {
        String countQuery = "SELECT * FROM message";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int fetch_count() {
        String sql = "SELECT count(*) FROM message WHERE status ='unread'";
        msgdata = db.rawQuery(sql, null);
        msgdata.moveToFirst();
        int count = 0;
        if (msgdata.moveToFirst()) {
            count = msgdata.getInt(0);
        }
        return count;
    }

    public void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "billionyogis.kshantechsoft.com.billionyogis" + "/databases/" + SAMPLE_DB_NAME;
        String backupDBPath = SAMPLE_DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cursor fetch_all_data() {
        String sql = "SELECT * FROM message";
        msgdata = db.rawQuery(sql, null);
        msgdata.moveToFirst();
        return msgdata;
    }
}
