package giftadeed.kshantechsoft.com.giftadeed.Utils;

/**
 * Created by I-Sys on 25-Apr-17.
 */

public class WebServices {

    /* Urls for development */
    public static final String MANI_URL = "https://kshandemo.co.in/";
    public static final String SOS_STORAGE_PATH_UPLOADS = "uploadsDev/";
    public static final String DATABASE_SOS_UPLOADS = "SOSDev";
    public static final String DATABASE_PROFILE_PIC_UPLOADS = "usersDev";
    public static final String PROFILEPIC_STORAGE_PATH_UPLOADS = "profilepicsDev/";
    public static final String SENDBIRD_APP_ID = "2B2DA376-91B5-4604-9279-C0533F130126";
    public static final String SENDBIRD_API_TOKEN = "cf709ee2fa69a3823f90bdc98647c0d2e850d3cf";
    /* End of Urls for development */


    /* Urls for production */
    /* public static final String MANI_URL = "https://www.giftadeed.com/";
    public static final String SOS_STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_SOS_UPLOADS = "SOS";
    public static final String DATABASE_PROFILE_PIC_UPLOADS = "users";
    public static final String PROFILEPIC_STORAGE_PATH_UPLOADS = "profilepics/";
    public static final String SENDBIRD_APP_ID = "A90E8A82-E9CD-477C-829D-A162F478B0E4";
    public static final String SENDBIRD_API_TOKEN = "2dcdfe3d32794628037846383f037b87db12a349"; */
    /* End of Urls for production */

    public static final String SUB_URL = "gad3p2/api/";
    public static final String CUSTOM_CATEGORY_IMAGE_URL = MANI_URL + SUB_URL + "image/group_category/";
    public static final String country = SUB_URL + "country_list.php";
    public static final String state = SUB_URL + "state_list.php";
    public static final String city = SUB_URL + "city_list.php";
    public static final String emailcheck = SUB_URL + "email_check.php";
    public static final String mobilecheck = SUB_URL + "mobileno_check.php";
    public static final String signup = SUB_URL + "app_signup.php";
    public static final String login = SUB_URL + "login.php";
    public static final String suggestsubtype = SUB_URL + "suggest_sub_type.php";
    public static final String linkedinlogin = SUB_URL + "linkedIn_signup.php";
    public static final String facebooklogin = SUB_URL + "facebook_signup.php";
    public static final String socialmedia = SUB_URL + "social_signup.php";
    public static final String forgotpassword = SUB_URL + "forgot_pass.php";
    public static final String fetchprofile = SUB_URL + "fetch_userprofile.php";
    public static final String updateprofil = SUB_URL + "update_userprofile.php";
    public static final String firstlogin = SUB_URL + "first_login.php";
    public static final String MAIN_SUB_URL = MANI_URL + SUB_URL;
    public static final String needtype = SUB_URL + "need_type.php";
    public static final String needsubtype = SUB_URL + "get_sub_type.php";
    public static final String taganeed = SUB_URL + "tag_need.php";
    public static final String UPLOAD_URL = MAIN_SUB_URL + "saveimg.php";
    public static final String UPLOAD_GIFT_URL = MAIN_SUB_URL + "saveimg_ful.php";
    public static final String FULLFIL_NEED = SUB_URL + "fulfilled_need.php";
    public static final String fetch_taggerNeed = SUB_URL + "top_taggers.php";
    public static final String fetch_toptenfullfiller = SUB_URL + "top_ten_fullfiller.php";
    public static final String taglist_fetch = SUB_URL + "deed_list.php";
    public static final String soslist_fetch = SUB_URL + "sos_list.php";
    public static final String reslist_fetch = SUB_URL + "resource_list.php";
    public static final String myTagsURL = SUB_URL + "MyTags.php";
    public static final String myFullfillTagsURL = SUB_URL + "MyfullFillTags.php";
    public static final String fulfildistance = SUB_URL + "distancefetch.php";
    public static final String GET_ADDRESS = MAIN_SUB_URL + "Testaddress.php";
    public static final String GET_COUNTRY_SOS_NUMBER = SUB_URL + "country_emergency_number.php";
    public static final String deeddetails = SUB_URL + "deed_details.php";
    public static final String endorsedeed = SUB_URL + "endorse_deed.php";
    public static final String commentdeed = SUB_URL + "post_comment.php";
    public static final String editdeed = SUB_URL + "edit_deed.php";
    public static final String reportdeed = SUB_URL + "report_deed.php";
    public static final String reportuser = SUB_URL + "report_user.php";
    public static final String tagcounter = SUB_URL + "tag_counter.php";
    public static final String contactUs = SUB_URL + "contact_us.php";
    public static final String dashBoard = SUB_URL + "dashboard.php";
    public static final String deeddeleted = SUB_URL + "is_deleted.php";
    public static final String advisoryurl = SUB_URL + "advisory_board.php";
    public static final String notificationcount = SUB_URL + "notification_count.php";
    public static final String Allnotifications = SUB_URL + "app_notify.php";
    public static final String Resend_link = SUB_URL + "resend_link.php";
    public static final String Active_User = SUB_URL + "active_user.php";
    public static final String Check_Deed = SUB_URL + "check_deed.php";

    //Permanent location api
    public static final String Remove_Location = SUB_URL + "remove_location.php";
    public static final String Permanent_DeedList = SUB_URL + "permanent_deed_list.php";

    //Group api
    public static final String Create_Group = SUB_URL + "create_group.php";
    public static final String Edit_Group = SUB_URL + "edit_group.php";
    public static final String Delete_Group = SUB_URL + "del_group.php";
    public static final String Group_List = SUB_URL + "group_list.php";
    public static final String Groups_Tags = SUB_URL + "group_home.php";
    public static final String Search_User = SUB_URL + "search_user.php";
    public static final String Add_Member = SUB_URL + "add_member.php";
    public static final String Remove_Member = SUB_URL + "remove_member.php";
    public static final String Member_List = SUB_URL + "member_list.php";
    public static final String Group_Info = SUB_URL + "group_info.php";
    public static final String Assign_Admin = SUB_URL + "assign_admin.php";
    public static final String Dismiss_Admin = SUB_URL + "dismiss_admin.php";
    public static final String Exit_Group = SUB_URL + "exit_group.php";
    public static final String Owned_Groups = SUB_URL + "owned_groups.php";

    //Collaboration api
    public static final String Create_Collab = SUB_URL + "create_collaboration.php";
    public static final String Edit_Collab = SUB_URL + "edit_collaboration.php";
    public static final String Delete_Collab = SUB_URL + "delete_collaboration.php";
    public static final String Collab_List = SUB_URL + "users_collaboration_list.php";
    public static final String Collab_Request_List = SUB_URL + "collaboration_request_list.php";
    public static final String Accept_Reject_Request = SUB_URL + "edit_collaboration_request_status.php";
    public static final String Collab_Member_List = SUB_URL + "collaboration_members_list.php";
    public static final String Invite_Group_Creator = SUB_URL + "invite_group_creators.php";
    public static final String Group_Creators_List = SUB_URL + "group_creators_list.php";
    public static final String Collab_Info = SUB_URL + "collaboration_information.php";
    public static final String Remove_Collab_Member = SUB_URL + "remove_member_from_collaboration.php";  // same api used for exit collaboration from member side

    //Resource api
    public static final String Add_Resource = SUB_URL + "add_resource.php";
    public static final String User_Resource = SUB_URL + "user_resource.php";
    public static final String Resource_Details = SUB_URL + "resource_details.php";
    public static final String Delete_Resource = SUB_URL + "del_resource.php";
    public static final String Multi_Subtype = SUB_URL + "get_multi_sub_type.php";

    //SOS api
    public static final String Add_SOS = SUB_URL + "add_sos.php";
    public static final String SOS_Details = SUB_URL + "sos_details.php";
    public static final String Delete_SOS = SUB_URL + "remove_sos.php";
    public static final String SOS_Types = SUB_URL + "get_sos_type.php";

    //send bird
    public static final String MANI_SENDBRD_URL = "https://api.sendbird.com/";
    public static final String UPDATE_GRP_CHANNEL = "v3/group_channels/";
    public static final String DELETE_USER_ACOUNT = "v3/users/";
    public static final String REMOVE_MEMBER = "v3/group_channels/";
}
