package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Darshan on 09/14/2017.
 */

public class DBGAD {
    private static final String SAMPLE_DB_NAME = "GAD_DB.db";
    public static Context context;
    SQLiteDatabase db;
    private String c, qry;
    public static Cursor msgdata;
    public static Cursor cur_filterfrag, cur_markersdetail;

    public DBGAD(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase("GAD_DB.db", MODE_PRIVATE, null);
        c = ("CREATE TABLE IF NOT EXISTS message(ID INTEGER PRIMARY KEY AUTOINCREMENT,created_date datetime,time text,nt_type text,tag_type text,geopoint text,Need_Name text,Tag_Id text)");
        db.execSQL(c);
    }

    public void insert_msg(String created_date, String time, String nt_type, String tag_type, String geopoint, String Need_Name, String tag_id) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd_str = sdf.format(date);
        String query = "INSERT INTO message(created_date,time,nt_type,tag_type,geopoint,Need_Name,Tag_Id) VALUES('" + created_date + "','" + time + "','" + nt_type + "','" + tag_type + "','" + geopoint + "','" + Need_Name + "','" + tag_id + "');";
        db.execSQL(query);
        /*String delquery="delete from message where datediff(now(), mytable.date) > 5";
        db.execSQL(delquery);*/
    }

    public void delete_messages() {
        String deletesql = "DELETE FROM message WHERE created_date <= date('now','-7 day')";
        db.execSQL(deletesql);
    }

    public int delete_row_message() {
        int i = 0;
        try {
            //  String sql = "delete *  from  yoga_table where ID >0";
            i = db.delete("message", "ID>0", null);
            // db.rawQuery(sql, null);
            //  db.close();
            // Toast.makeText(context, "Reset Successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public void update_msg() {
        String query = "update message set status='read' where status='unread'";
        db.execSQL(query);
    }

    public Cursor fetch(String fromdate, String enddate) {
        //String sql="SELECT * FROM message where date(message_time)='"+date+"' ORDER BY ID DESC";
        //SELECT * FROM message WHERE message_time BETWEEN '16-Sep-2017' AND '17-Sep-2017'
        //SELECT * FROM message WHERE `message_time` >= '15-Sep-2017' and `message_time` <= '17-Sep-2017' ORDER BY ID DESC
        //String sql = "SELECT * FROM message ORDER BY ID DESC";
        String sql = "SELECT * FROM message WHERE created_date  BETWEEN '" + fromdate + "' and  '" + fromdate + "' ORDER BY ID DESC";
        msgdata = db.rawQuery(sql, null);
        msgdata.moveToFirst();
        return msgdata;
    }

    public int fetch_count() {
        //String sql="SELECT * FROM message where date(message_time)='"+date+"' ORDER BY ID DESC";
        //SELECT * FROM message WHERE message_time BETWEEN '16-Sep-2017' AND '17-Sep-2017'
        //SELECT * FROM message WHERE `message_time` >= '15-Sep-2017' and `message_time` <= '17-Sep-2017' ORDER BY ID DESC
        String sql = "SELECT count(*) FROM message WHERE status ='unread'";

        msgdata = db.rawQuery(sql, null);
        msgdata.moveToFirst();
        int count = 0;
        if (msgdata.moveToFirst()) {
            count = msgdata.getInt(0);
        }
        return count;


    }

    public int fetch_yoga(String type, String Id, String action, String date) {
        //String sql="SELECT * FROM message where date(message_time)='"+date+"' ORDER BY ID DESC";
        //SELECT * FROM message WHERE message_time BETWEEN '16-Sep-2017' AND '17-Sep-2017'
        //SELECT * FROM message WHERE `message_time` >= '15-Sep-2017' and `message_time` <= '17-Sep-2017' ORDER BY ID DESC
        String sql = "";
        if (type.equals("club")) {
            sql = "SELECT count(*) FROM message WHERE club_event_id ='" + Id + "' and type_message='club'";

        } else if (type.equals("event")) {
            //ID INTEGER PRIMARY KEY AUTOINCREMENT,title text,message text,count text,message_time datetime,status text,club_event_id text,type_message text,action_messgae text,update_date text
            sql = "SELECT count(*) FROM message WHERE club_event_id ='" + Id + "' and update_date='" + date + "' and action_messgae='" + action + "' and type_message='event'";
        }


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
        //String sql="SELECT * FROM message where date(message_time)='"+date+"' ORDER BY ID DESC";
        //SELECT * FROM message WHERE message_time BETWEEN '16-Sep-2017' AND '17-Sep-2017'
        //SELECT * FROM message WHERE `message_time` >= '15-Sep-2017' and `message_time` <= '17-Sep-2017' ORDER BY ID DESC
        //String sql = "SELECT * FROM message ORDER BY ID DESC";
        String sql = "SELECT * FROM message";
        msgdata = db.rawQuery(sql, null);
        msgdata.moveToFirst();


        return msgdata;
    }
}
