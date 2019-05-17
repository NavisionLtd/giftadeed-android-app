package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.BuildConfig;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Interfaces.UpdateGrpChannel;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.Pojo.ModalSendBrdUpdate;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

public class CreateGroupFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    View rootview;
    FragmentActivity myContext;
    static FragmentManager fragmgr;
    SimpleArcDialog mDialog;
    ImageView imageView;
    EditText groupName, groupDesc;
    Button btnCreateGroup;
    SessionManager sessionManager;
    Button takePicture, browsePicture;
    Uri filee, fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "GiftaDeed";
    public static final int MEDIA_TYPE_IMAGE = 1;
    int REQUEST_CAMERA = 0;
    private static int RESULT_LOAD_IMG = 11;
    Bitmap bitmap;
    String strimagePath, imgDecodableString, pathToSend = "", strUser_ID;
    String receivedGid = "", receivedGname = "", receivedGdesc = "", receivedGimage = "", callingFrom = "";
    private GoogleApiClient mGoogleApiClient;
    private List<String> lstusers = new ArrayList<String>();
    private boolean mIsDistinct;
    private String strMessage = "Welcome to GiftADeed Chat";
    private List<GroupListInfo> lstGetChannelsList = new ArrayList<>();
    private String fetchedChannelUrl;

    public static CreateGroupFragment newInstance(int sectionNumber) {
        CreateGroupFragment fragment = new CreateGroupFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.create_group_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.fragname = CreateGroupFragment.newInstance(0);
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        mDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        init();
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        HashMap<String, String> group = sessionManager.getSelectedGroupDetails();
        callingFrom = group.get(sessionManager.GRP_CALL_FROM);
        receivedGid = group.get(sessionManager.GROUP_ID);
        receivedGname = group.get(sessionManager.GROUP_NAME);
        receivedGdesc = group.get(sessionManager.GROUP_DESC);
        receivedGimage = group.get(sessionManager.GROUP_IMAGE);
        if (callingFrom.equals("create")) {
            //from create menu
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.action_create_group));
        } else {
            //from edit menu
            groupName.setText(receivedGname);
            groupDesc.setText(receivedGdesc);
            String path = WebServices.MAIN_SUB_URL + receivedGimage;
            if (receivedGimage.length() > 0) {
                Picasso.with(getContext()).load(path).placeholder(R.drawable.group_default_wallpaper).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.group_default_wallpaper);
            }
            TaggedneedsActivity.updateTitle(getResources().getString(R.string.edit_group));
            getChannelsDetails();
        }

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        browsePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallary();
            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupName.getText().length() == 0) {
                    groupName.requestFocus();
                    groupName.setFocusable(true);
                    groupName.setError("Group name required");
                } else {
                    if (!(Validation.isOnline(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        if (callingFrom.equals("create")) {
                            //call api
                            if (bitmap != null) {
                                pathToSend = getStringImage(bitmap);
                            }
                            createGroup(pathToSend, groupName.getText().toString(), groupDesc.getText().toString(), strUser_ID);
                        } else {
                            // call edit group api
                            if (bitmap != null) {
                                pathToSend = getStringImage(bitmap);
                                editGroup(pathToSend, groupName.getText().toString(), groupDesc.getText().toString(), strUser_ID, receivedGid);
                            } else {
                                if (receivedGname.equals(groupName.getText().toString()) && receivedGdesc.equals(groupDesc.getText().toString())) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.you_havnt_change), Toast.LENGTH_SHORT).show();
                                } else {
                                    editGroup(pathToSend, groupName.getText().toString(), groupDesc.getText().toString(), strUser_ID, receivedGid);
                                }
                            }
                        }
                    }
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom.equals("create")) {
//                    GroupsListFragment groupsListFragment = new GroupsListFragment();
//                    fragmgr.beginTransaction().replace(R.id.content_frame, groupsListFragment).commit();
                    fragmgr.beginTransaction().replace(R.id.content_frame, GroupCollabFrag.newInstance(9)).commit();
                } else {
                    GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                    fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
                }
            }
        });
        return rootview;
    }

    //--------------------------Initilizing the UI variables--------------------------------------------
    private void init() {
        imageView = (ImageView) rootview.findViewById(R.id.group_profile_image);
        takePicture = (Button) rootview.findViewById(R.id.group_take_picture);
        browsePicture = (Button) rootview.findViewById(R.id.group_browse_image);
        groupName = (EditText) rootview.findViewById(R.id.editText_group_name);
        groupDesc = (EditText) rootview.findViewById(R.id.editText_group_desc);
        btnCreateGroup = (Button) rootview.findViewById(R.id.button_save_group);
    }

    //---------------------sending group details to server for create group-----------------------------------------------
    public void createGroup(String groupimage, final String groupname, String groupdesc, String user_id) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Creating group..");
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CreateGroupInterface service = retrofit.create(CreateGroupInterface.class);
        Call<GroupResponseStatus> call = service.sendData(groupimage, groupname, user_id, groupdesc);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsegroup", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        int generatedGroupId = groupResponseStatus.getGroupid();
                        String channelName = "";
                        // Sendbird create channel. Concat with GRP for Group and CLB for Collaboration
                        channelName = groupname + " - GRP" + generatedGroupId;
                        Log.d("channel_name", channelName);
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.group_created_msg), Toast.LENGTH_SHORT).show();
                            sessionManager.store_GroupName(groupname);
                            //group chat
                            mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                            if (strUser_ID != null && channelName != null) {
                                lstusers.add(strUser_ID);
                                mIsDistinct = PreferenceUtils.getGroupChannelDistinct(myContext);
                                createGroupChannel(lstusers, channelName, strMessage, mIsDistinct);
                            }
                            // move to groups list fragment
//                            GroupsListFragment groupsListFragment = new GroupsListFragment();
//                            fragmgr.beginTransaction().replace(R.id.content_frame, groupsListFragment).commit();
                            fragmgr.beginTransaction().replace(R.id.content_frame, GroupCollabFrag.newInstance(9)).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsegroup", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsegroup", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------sending group details to server for edit group-----------------------------------------------
    public void editGroup(String groupimage, final String groupname, String groupdesc, String user_id, final String groupid) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Editing group..");
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        EditGroupInterface service = retrofit.create(EditGroupInterface.class);
        Call<GroupResponseStatus> call = service.sendData(groupimage, groupname, user_id, groupdesc, groupid);
        Log.d("edit_group_params", groupimage + ":" + groupname + ":" + user_id + ":" + groupdesc + ":" + groupid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("responsegrp_onresponse", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        Log.d("edit_group_status", groupResponseStatus.getStatus().toString());
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), getResources().getString(R.string.group_edited), Toast.LENGTH_SHORT).show();

                            // update sendbird also
                            String channelName = "";
                            // Sendbird edit channel. Concat with GRP for Group and CLB for Collaboration
                            channelName = groupname + " - GRP" + groupid;
                            filterGroupChannel(receivedGname + " - GRP" + receivedGid);
                            callUpdateSendBird(fetchedChannelUrl, channelName);

                            // move to groups list fragment
//                            GroupsListFragment groupsListFragment = new GroupsListFragment();
//                            fragmgr.beginTransaction().replace(R.id.content_frame, groupsListFragment).commit();
                            fragmgr.beginTransaction().replace(R.id.content_frame, GroupCollabFrag.newInstance(9)).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsegroup", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("responsegroup", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                filee = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, filee);
        } else {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void choosePhotoFromGallary() {
        loadImagefromGallery();
    }

    //new camera nougat code
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "GAD_" + timeStamp;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "GAD_" + timeStamp;
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(filee), null, options);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                }
                strimagePath = file.getAbsolutePath();
                imageView.setImageBitmap(bitmap);
            }
        }

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            bitmap = decodeSampledBitmapFromFile(imgDecodableString, 512, 512);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    //-----------------------------getting string from bitmap image---------------------------------
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    if (callingFrom.equals("create")) {
                        fragmgr.beginTransaction().replace(R.id.content_frame, GroupCollabFrag.newInstance(9)).commit();
                    } else {
                        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                        fragmgr.beginTransaction().replace(R.id.content_frame, groupDetailsFragment).commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    private void createGroupChannel(final List<String> userIds, final String clubName, final String message, final boolean distinct) {
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                GroupChannel.createChannelWithUserIds(userIds, distinct, clubName, "", message, new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }
                    }
                });
            }
        });
    }

    //get channels details
    public void getChannelsDetails() {
        //always use connect() along with any method of chat #phase 2 requirement 27 feb 2018 Nilesh
        SendBird.connect(strUser_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
                GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
                channelListQuery.setIncludeEmpty(true);
                channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                    @Override
                    public void onResult(List<GroupChannel> list, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;

                        }
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                System.out.println("Chnalls: " + list.get(i).getName());
                                /// lstGetChannelsList.add(list.get(i).getName().toString());
                                lstGetChannelsList.add(new GroupListInfo(list.get(i).getData().toString(), list.get(i).getName().toString(), list.get(i).getUrl().toString()));
                            }
                        }
                    }
                });
            }
        });

    }

    //***********************************transform*************************************************
    public void filterGroupChannel(String clubname) {
        System.out.println("clbname line n0 3950        " + clubname);
        if (lstGetChannelsList != null && lstGetChannelsList.size() != 0) {
            for (int i = 0; i < lstGetChannelsList.size(); i++) {
                if (lstGetChannelsList.get(i).getmChannelName().equals(clubname)) {
                    fetchedChannelUrl = lstGetChannelsList.get(i).getmUrl();
                    Log.d("UPDATE", "" + lstGetChannelsList.get(i).getmUrl());
                }
            }
        }
    }

    //==============================================================================================
    public void callUpdateSendBird(String urlOfChannel, final String channelName) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_SENDBRD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //pass the json obhect
        JsonObject postParam = new JsonObject();
        postParam.addProperty("name", channelName);

        //call interface
        UpdateGrpChannel service = retrofit.create(UpdateGrpChannel.class);
        Call<ModalSendBrdUpdate> call = service.sendData(urlOfChannel, postParam);
        call.enqueue(new Callback<ModalSendBrdUpdate>() {
            @Override
            public void onResponse(Response<ModalSendBrdUpdate> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    Log.d("UPCLUB", "Success.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.d("UPCLUB", "Not Success." + t.getMessage());
            }
        });
    }
}
