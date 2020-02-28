/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Utils;

/**
 * Created by I-Sys on 25-Apr-17.
 */

public class WebServices {

    // TODO : Change URLs for Website, APIs, Firebase, SendBird, Google key
    public static final String MAIN_API_URL = "YOUR_API_MAIN_URL"; // For example: http://www.example.com/
    public static final String API_SUB_URL = "YOUR_API_SUB_URL/"; // For example: api/
    public static final String SOS_STORAGE_PATH_UPLOADS = "uploadsDev/"; // Firebase directory name
    public static final String DATABASE_SOS_UPLOADS = "SOSDev"; // Firebase database name
    public static final String DATABASE_PROFILE_PIC_UPLOADS = "usersDev"; // Firebase database name
    public static final String PROFILEPIC_STORAGE_PATH_UPLOADS = "profilepicsDev/"; // Firebase directory name
    public static final String SENDBIRD_APP_ID = "YOUR_SENDBIRD_APP_ID";
    public static final String SENDBIRD_API_TOKEN = "YOUR_SENDBIRD_API_TOKEN";
    public static final String GOOGLE_KEY = "YOUR_GOOGLE_API_KEY_HERE";
    public static final String PLAY_STORE_URL = "YOUR_PLAYSTORE_APP_URL";
    public static final String android_shortlink = "YOUR_PLAYSTORE_APP_SHORT_URL";
    public static final String ios_shortlink = "YOUR_APPSTORE_APP_SHORT_URL";
    public static final String website_url = "YOUR_WEBSITE_URL";
    public static final String EMAIL = "YOUR_EMAIL_ID"; // For Bug report on email
    public static final String PASSWORD = "YOUR_EMAIL_PASSWORD"; // For Bug report on email


    public static final String CUSTOM_CATEGORY_IMAGE_URL = MAIN_API_URL + API_SUB_URL + "image/group_category/";
    public static final String country = API_SUB_URL + "country_list.php";
    public static final String state = API_SUB_URL + "state_list.php";
    public static final String city = API_SUB_URL + "city_list.php";
    public static final String emailcheck = API_SUB_URL + "email_check.php";
    public static final String mobilecheck = API_SUB_URL + "mobileno_check.php";
    public static final String signup = API_SUB_URL + "app_signup.php";
    public static final String login = API_SUB_URL + "login.php";
    public static final String suggestsubtype = API_SUB_URL + "suggest_sub_type.php";
    public static final String linkedinlogin = API_SUB_URL + "linkedIn_signup.php";
    public static final String facebooklogin = API_SUB_URL + "facebook_signup.php";
    public static final String socialmedia = API_SUB_URL + "social_signup.php";
    public static final String forgotpassword = API_SUB_URL + "forgot_pass.php";
    public static final String fetchprofile = API_SUB_URL + "fetch_userprofile.php";
    public static final String updateprofil = API_SUB_URL + "update_userprofile.php";
    public static final String firstlogin = API_SUB_URL + "first_login.php";
    public static final String MAIN_SUB_URL = MAIN_API_URL + API_SUB_URL;
    public static final String needtype = API_SUB_URL + "need_type.php";
    public static final String needsubtype = API_SUB_URL + "get_sub_type.php";
    public static final String taganeed = API_SUB_URL + "tag_need.php";
    public static final String UPLOAD_URL = MAIN_SUB_URL + "saveimg.php";
    public static final String UPLOAD_GIFT_URL = MAIN_SUB_URL + "saveimg_ful.php";
    public static final String FULLFIL_NEED = API_SUB_URL + "fulfilled_need.php";
    public static final String fetch_taggerNeed = API_SUB_URL + "top_taggers.php";
    public static final String fetch_toptenfullfiller = API_SUB_URL + "top_ten_fullfiller.php";
    public static final String taglist_fetch = API_SUB_URL + "deed_list.php";
    public static final String soslist_fetch = API_SUB_URL + "sos_list.php";
    public static final String reslist_fetch = API_SUB_URL + "resource_list.php";
    public static final String myTagsURL = API_SUB_URL + "MyTags.php";
    public static final String myFullfillTagsURL = API_SUB_URL + "MyfullFillTags.php";
    public static final String fulfildistance = API_SUB_URL + "distancefetch.php";
    public static final String GET_ADDRESS = MAIN_SUB_URL + "Testaddress.php";
    public static final String GET_COUNTRY_SOS_NUMBER = API_SUB_URL + "country_emergency_number.php";
    public static final String deeddetails = API_SUB_URL + "deed_details.php";
    public static final String endorsedeed = API_SUB_URL + "endorse_deed.php";
    public static final String commentdeed = API_SUB_URL + "post_comment.php";
    public static final String editdeed = API_SUB_URL + "edit_deed.php";
    public static final String reportdeed = API_SUB_URL + "report_deed.php";
    public static final String reportuser = API_SUB_URL + "report_user.php";
    public static final String tagcounter = API_SUB_URL + "tag_counter.php";
    public static final String contactUs = API_SUB_URL + "contact_us.php";
    public static final String dashBoard = API_SUB_URL + "dashboard.php";
    public static final String deeddeleted = API_SUB_URL + "is_deleted.php";
    public static final String advisoryurl = API_SUB_URL + "advisory_board.php";
    public static final String notificationcount = API_SUB_URL + "notification_count.php";
    public static final String Allnotifications = API_SUB_URL + "app_notify.php";
    public static final String Resend_link = API_SUB_URL + "resend_link.php";
    public static final String Active_User = API_SUB_URL + "active_user.php";
    public static final String Check_Deed = API_SUB_URL + "check_deed.php";

    //Permanent location api
    public static final String Remove_Location = API_SUB_URL + "remove_location.php";
    public static final String Permanent_DeedList = API_SUB_URL + "permanent_deed_list.php";

    //Group api
    public static final String Create_Group = API_SUB_URL + "create_group.php";
    public static final String Edit_Group = API_SUB_URL + "edit_group.php";
    public static final String Delete_Group = API_SUB_URL + "del_group.php";
    public static final String Group_List = API_SUB_URL + "group_list.php";
    public static final String Groups_Tags = API_SUB_URL + "group_home.php";
    public static final String Search_User = API_SUB_URL + "search_user.php";
    public static final String Add_Member = API_SUB_URL + "add_member.php";
    public static final String Remove_Member = API_SUB_URL + "remove_member.php";
    public static final String Member_List = API_SUB_URL + "member_list.php";
    public static final String Group_Info = API_SUB_URL + "group_info.php";
    public static final String Assign_Admin = API_SUB_URL + "assign_admin.php";
    public static final String Dismiss_Admin = API_SUB_URL + "dismiss_admin.php";
    public static final String Exit_Group = API_SUB_URL + "exit_group.php";
    public static final String Owned_Groups = API_SUB_URL + "owned_groups.php";

    //Collaboration api
    public static final String Create_Collab = API_SUB_URL + "create_collaboration.php";
    public static final String Edit_Collab = API_SUB_URL + "edit_collaboration.php";
    public static final String Delete_Collab = API_SUB_URL + "delete_collaboration.php";
    public static final String Collab_List = API_SUB_URL + "users_collaboration_list.php";
    public static final String Collab_Request_List = API_SUB_URL + "collaboration_request_list.php";
    public static final String Accept_Reject_Request = API_SUB_URL + "edit_collaboration_request_status.php";
    public static final String Collab_Member_List = API_SUB_URL + "collaboration_members_list.php";
    public static final String Invite_Group_Creator = API_SUB_URL + "invite_group_creators.php";
    public static final String Group_Creators_List = API_SUB_URL + "group_creators_list.php";
    public static final String Collab_Info = API_SUB_URL + "collaboration_information.php";
    public static final String Remove_Collab_Member = API_SUB_URL + "remove_member_from_collaboration.php";  // same api used for exit collaboration from member side

    //Resource api
    public static final String Add_Resource = API_SUB_URL + "add_resource.php";
    public static final String Update_Resource = API_SUB_URL + "update_resource.php";
    public static final String User_Resource = API_SUB_URL + "user_resource.php";
    public static final String Resource_Details = API_SUB_URL + "resource_details.php";
    public static final String Delete_Resource = API_SUB_URL + "del_resource.php";
    public static final String Multi_Subtype = API_SUB_URL + "get_multi_sub_type.php";

    //SOS api
    public static final String Add_SOS = API_SUB_URL + "add_sos.php";
    public static final String SOS_Details = API_SUB_URL + "sos_details.php";
    public static final String Delete_SOS = API_SUB_URL + "remove_sos.php";
    public static final String SOS_Types = API_SUB_URL + "get_sos_type.php";

    //send bird
    public static final String MANI_SENDBRD_URL = "https://api.sendbird.com/";
    public static final String UPDATE_GRP_CHANNEL = "v3/group_channels/";
    public static final String DELETE_USER_ACOUNT = "v3/users/";
    public static final String REMOVE_MEMBER = "v3/group_channels/";
}
