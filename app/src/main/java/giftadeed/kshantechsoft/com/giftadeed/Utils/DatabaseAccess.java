package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.Contact;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;

/**
 * Created by Bhakti.Gore on 06-11-2017.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public long Create_Group(String id, String gname, String gimage, String checked) {
        ContentValues values = new ContentValues();
        values.put("group_id", id);
        values.put("group_name", gname);
        values.put("group_image", gimage);
        values.put("group_checked", checked);
        long ros = database.insert("group_details", null, values);
        return ros;
    }

    public int getMaxInvoiceId() {
        String myQuery = "SELECT max(invoice_id) FROM invoice_master";
        Cursor cursor = database.rawQuery(myQuery, null);
        int maxId = 0;
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
        }
        cursor.close();
        return maxId;
    }

    // code to get all contacts in a list view
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM contact_details";
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setContact1(cursor.getString(1));
                contact.setContact2(cursor.getString(2));
                contact.setContact3(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(String id, String contact1, String contact2, String contact3) {
        ContentValues values = new ContentValues();
        values.put("contact_1", contact1);
        values.put("contact_2", contact2);
        values.put("contact_3", contact3);
        // updating row
        return database.update("contact_details", values, "id" + " = ?",
                new String[]{id});
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM contact_details";
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int getGroupCheckedCount() {
        String countQuery = "SELECT * FROM group_details WHERE group_checked = 'true'";
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int getSelectedCheckedCount(String ids) {
        String countQuery = "SELECT * FROM group_details WHERE group_id IN (" + ids + ") AND group_checked = 'true'";
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public ArrayList<GroupPOJO> getAllGroups() {
        ArrayList<GroupPOJO> groupPOJOArrayList = new ArrayList<GroupPOJO>();
        String selectQuery = "SELECT * FROM group_details";
        Log.d("query", selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                GroupPOJO group = new GroupPOJO();
                group.setGroup_id(cursor.getString(0));
                group.setGroup_name(cursor.getString(1));
                if (cursor.getString(4).equals("true")) {
                    group.setChecked(true);
                } else {
                    group.setChecked(false);
                }
                groupPOJOArrayList.add(group);
            } while (cursor.moveToNext());
        }
        return groupPOJOArrayList;
    }

    public void Update_Group_Details(String gid, String checked) {
        ContentValues values = new ContentValues();
        values.put("group_checked", checked);
        database.update("group_details", values, "group_id" + "=" + gid, null);
    }

    // Deleting group
    public void Delete_Group(String groupid) {
        database.delete("group_details", "group_id" + " = ?",
                new String[]{groupid});
    }

    public boolean groupidExist(String gid) {
        boolean result;
        String selectQuery = "select * from group_details where group_id = '" + gid + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public void groupidNotExist(String gid) {
        String deleteQuery = "delete from group_details where group_id NOT IN (" + gid + ")";
        Log.d("deleteQuery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteAllGroups() {
        String deleteQuery = "delete from group_details";
        Log.d("deleteAllQuery", deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void exportDatabse() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//giftadeed.kshantechsoft.com//databases//giftadeed.db";
                String backupDBPath = "giftadeed.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(Environment.getExternalStorageDirectory() + "/giftadeed"
                        + "/DatabaseBackup/");

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.d("ErrorMsg", "" + e.getMessage());
        }
    }

}
