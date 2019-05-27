package giftadeed.kshantechsoft.com.giftadeed.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;

/**
 * Created by I-Sys on 03-Apr-17.
 */

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int private_mode = 0;

    private static final String PREF_NAME = "KshanTechSoft";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String USER_ID = "User_Id";
    public static final String USER_NAME = "name";
    public static final String KEY_SELECTED_LANGUAGE = "SelectedLanguage";
    public static final String KEY_SELECTED_GROUPS = "SelectedGroups";
    public static final String KEY_radius = "radius";
    public static final String DRAWER_STATUS = "Status";
    public static final String KEY_NOTIFICATION = "notify";
    public static final String PRIVACY = "Public";
    public static final String RES_CALL_FROM = "Res_Call_From";
    public static final String RESOURCE_ID = "Resource_Id";
    public static final String RESOURCE_NAME = "Resource_name";
    public static final String GRP_CALL_FROM = "Grp_Call_From";
    public static final String GROUP_ID = "Group_Id";
    public static final String GROUP_NAME = "Group_name";
    public static final String GROUP_DESC = "Group_desc";
    public static final String GROUP_IMAGE = "Group_image";
    public static final String KEY_GROUPNAME = "GROUPNAME";
    public static final String COLAB_CALL_FROM = "Colab_Call_From";
    public static final String COLAB_ID = "Colab_Id";
    public static final String COLAB_NAME = "Colab_name";
    public static final String COLAB_DESC = "Colab_desc";
    public static final String COLAB_FROMGID = "Colab_from_gid";
    public static final String COLAB_FROMGNAME = "Colab_from_gname";
    public static final String SOS_OPTION_1_CLICKED = "option1"; // call
    public static final String SOS_OPTION_2_CLICKED = "option2"; // sms
    public static final String SOS_OPTION_3_CLICKED = "option3"; // share location

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, private_mode);
        editor = pref.edit();
    }

    //-------------------------Storing details in shared preferences-------------------------------------
    public void createUserCredentialSession(String Merchant_ID, String name, String privacy) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, Merchant_ID);
        editor.putString(USER_NAME, name);
        editor.putString(PRIVACY, privacy);
        editor.commit();
    }

    //-------------------------Getting details from shared preferences----------------------------------
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(PRIVACY, pref.getString(PRIVACY, null));
        // user email id
        // user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        // user type
        // user.put(KEY_User, pref.getString(KEY_User, null));
        // return user
        return user;
    }

    public void createGroupDetails(String callingfrom, String gid, String gname, String gdesc, String gimage) {
        editor.putString(GRP_CALL_FROM, callingfrom);
        editor.putString(GROUP_ID, gid);
        editor.putString(GROUP_NAME, gname);
        editor.putString(GROUP_DESC, gdesc);
        editor.putString(GROUP_IMAGE, gimage);
        editor.commit();
    }

    public HashMap<String, String> getSelectedGroupDetails() {
        HashMap<String, String> group = new HashMap<String, String>();
        group.put(GRP_CALL_FROM, pref.getString(GRP_CALL_FROM, null));
        group.put(GROUP_ID, pref.getString(GROUP_ID, null));
        group.put(GROUP_NAME, pref.getString(GROUP_NAME, null));
        group.put(GROUP_DESC, pref.getString(GROUP_DESC, null));
        group.put(GROUP_IMAGE, pref.getString(GROUP_IMAGE, null));
        return group;
    }

    public void createColabDetails(String callingfrom, String cid, String cname, String cdesc, String fromGroupId, String fromGroupName) {
        editor.putString(COLAB_CALL_FROM, callingfrom);
        editor.putString(COLAB_ID, cid);
        editor.putString(COLAB_NAME, cname);
        editor.putString(COLAB_DESC, cdesc);
        editor.putString(COLAB_FROMGID, fromGroupId);
        editor.putString(COLAB_FROMGNAME, fromGroupName);
        editor.commit();
    }

    public HashMap<String, String> getSelectedColabDetails() {
        HashMap<String, String> collab = new HashMap<String, String>();
        collab.put(COLAB_CALL_FROM, pref.getString(COLAB_CALL_FROM, null));
        collab.put(COLAB_ID, pref.getString(COLAB_ID, null));
        collab.put(COLAB_NAME, pref.getString(COLAB_NAME, null));
        collab.put(COLAB_DESC, pref.getString(COLAB_DESC, null));
        collab.put(COLAB_FROMGID, pref.getString(COLAB_FROMGID, null));
        collab.put(COLAB_FROMGNAME, pref.getString(COLAB_FROMGNAME, null));
        return collab;
    }

    public void createResourceDetails(String callingfrom) {
        editor.putString(RES_CALL_FROM, callingfrom);
        editor.commit();
    }

    public HashMap<String, String> getSelectedResourceDetails() {
        HashMap<String, String> resource = new HashMap<String, String>();
        resource.put(RES_CALL_FROM, pref.getString(RES_CALL_FROM, null));
        resource.put(RESOURCE_ID, pref.getString(RESOURCE_ID, null));
        resource.put(RESOURCE_NAME, pref.getString(RESOURCE_NAME, null));
        return resource;
    }

    public void store_radius(float radius) {
        editor.putFloat(KEY_radius, radius);
        editor.commit();
    }

    //-------------------------Getting details from shared preferences----------------------------------
    public Float getradius() {
        float rad = pref.getFloat(KEY_radius, 0.0f);
        return rad;
    }

    public void store_language(String language) {
        editor.putString(KEY_SELECTED_LANGUAGE, language);
        editor.commit();
    }

    public String getLanguage() {
        String langugae = pref.getString(KEY_SELECTED_LANGUAGE, "en");
        return langugae;
    }

    public void store_sos_option1_clicked(String clicked) {
        editor.putString(SOS_OPTION_1_CLICKED, clicked);
        editor.commit();
    }

    public String getSosOption1Clicked() {
        return pref.getString(SOS_OPTION_1_CLICKED, "no");
    }

    public void store_sos_option2_clicked(String clicked) {
        editor.putString(SOS_OPTION_2_CLICKED, clicked);
        editor.commit();
    }

    public String getSosOption2Clicked() {
        return pref.getString(SOS_OPTION_2_CLICKED, "no");
    }

    public void store_sos_option3_clicked(String clicked) {
        editor.putString(SOS_OPTION_3_CLICKED, clicked);
        editor.commit();
    }

    public String getSosOption3Clicked() {
        return pref.getString(SOS_OPTION_3_CLICKED, "no");
    }

    //-----------------------------Getting current drawer status

    public void set_drawer_status(String status) {
        // Storing name in pref
        editor.putString(DRAWER_STATUS, status);
        editor.commit();
    }

    public HashMap<String, String> get_drawer_status() {
        HashMap<String, String> get_OTA = new HashMap<String, String>();
        get_OTA.put(DRAWER_STATUS, pref.getString(DRAWER_STATUS, ""));
        return get_OTA;
    }


    //-------------------------------Notification status
    public void set_notification_status(String status) {
        // Storing name in pref
        editor.putString(KEY_NOTIFICATION, status);
        editor.commit();
    }

    public HashMap<String, String> get_notification_status() {
        HashMap<String, String> get_OTA = new HashMap<String, String>();
        get_OTA.put(KEY_NOTIFICATION, pref.getString(KEY_NOTIFICATION, ""));
        return get_OTA;
    }

    public void store_GroupName(String groupName) {
        editor.putString(KEY_GROUPNAME, groupName);
        editor.commit();
    }

    public String getGroupName() {
        String strGroupName = pref.getString(KEY_GROUPNAME, "");
        return strGroupName;
    }

}