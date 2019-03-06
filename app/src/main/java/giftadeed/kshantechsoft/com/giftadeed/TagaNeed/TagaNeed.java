package giftadeed.kshantechsoft.com.giftadeed.TagaNeed;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;


import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.BuildConfig;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Resources.MultiSubCategories;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

////////////////////////////////////////////////////////
// Copy Rights : Navision Ltd.
// Last Modified On : 30-Nov-18 11:16 AM
// Description : Used to tag a new deed or edit existing deed
////////////////////////////////////////////////////////

public class TagaNeed extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    String flag = "1";
    int spinnerPosition = 0;
    Spinner spinnerType;
    ArrayList<String> subTypePref = new ArrayList<String>();
    ArrayList<TaggedDeedsPojo> taggeddeeds = new ArrayList<TaggedDeedsPojo>();
    public static ArrayList<String> selectedUserGroups = new ArrayList<String>();
    public static ArrayList<String> selectedUserGrpNames = new ArrayList<String>();
    String formattedSubTypePref = "", formattedUserOrgs = ""; // for removing brackets [ and ]
    String checkedOtherOrg = "N", checkedIndi = "N";
    LinearLayout permanentLayout, addressLayout;
    private ImageView btnSpeak;
    private String paddress = "N";
    private Switch locationSwitch;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    static android.app.Fragment fragment;
    //static FragmentManager fragmgr;
    public static final int RequestPermissionCode = 1;
    static android.support.v4.app.FragmentManager fragmgr;
    FragmentActivity myContext;
    String lat, longi;
    View rootview;
    String itemname, itemid;
    // private AlertDialog alertDialogForgot;
    ImageView imgcamera, gieftneedimg, imgcategory, imgshare, imgchar;
    Button btnCamera, btnGallery, btnPost, dialogbtnconfirm, dialogbtncancel, btnOk;
    TextView dialogtext, txtuploadpic, txtSelectcategory, txtSelectLocation, txtDescription, txtvalidity, txtdialogcreditpoints, txttotalpoints, txtneedname;
    EditText edselectcategory, selectedPref, edselectAudiance, edselectlocation, edDescription, edshortdescription;
    String latitude_source, longitude_source;
    String strNeedmapping_ID, strNeed_Name, strCharacter_Path, strImagenamereturned, strUser_ID, strCreditpoints, strTotalpoints;
    LinearLayout layout_container, layout_audiance;
    CheckBox Checkbox_container;
    Bitmap bitmap;
    String image;
    ScrollView deeddetails_scrollview;
    String strCategory, strlocation, strShortDescription, strFullDescription;
    SessionManager sessionManager;
    private static int RESULT_LOAD_IMG = 11;
    public static final int MEDIA_TYPE_IMAGE = 1;
    String imgDecodableString;
    private Uri fileUri, outputFileUri;
    private static final int CAMERA_REQUEST = 1888;
    private static final String IMAGE_DIRECTORY_NAME = "GiftaDeed";
    private ArrayList<SignupPOJO> categories;
    private ArrayList<SubCategories> subcategories = new ArrayList<>();
    ArrayList<GroupPOJO> groupArrayList;
    String adress_show;
    SimpleArcDialog simpleArcDialog;
    String value = "tab1", fragmentpage = "home";
    String mCurrentPhotoPath;
    int REQUEST_CAMERA = 0;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public String strimagePath;
    Uri filee;
    double LATITUDE, LONGITUDE;
    DiscreteSeekBar seekbarValidity;
    private AlertDialog alertDialogForgot, alertDialogreturn;
    String str_address, str_tagid, str_geopoint, str_taggedPhotoPath, str_description, str_characterPath, str_fname, str_lname, str_privacy,
            str_needName, str_totalTaggedCreditPoints, str_totalFulfilledCreditPoints, str_title, str_date, str_distance, str_needid, str_tab, strcontainer = "0", str_validity = "3", strCategory_click = "Clicked", strSubCategory_click = "Clicked", strAudiance_click = "Clicked";
    private GoogleApiClient mGoogleApiClient;
    AlertDialog.Builder alertdialoggif;

    public static TagaNeed newInstance(int sectionNumber) {
        TagaNeed fragment = new TagaNeed();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        imgcamera= (ImageView) rootview.findViewById(R.id.imgappbarcamera);
        rootview = inflater.inflate(R.layout.fragment_taga_need, container, false);
        sessionManager = new SessionManager(getActivity());
        TaggedneedsActivity.updateTitle("Tag A Deed");
        TaggedneedsActivity.fragname = TagaNeed.newInstance(0);
        android.support.v4.app.FragmentManager fragManager = myContext.getSupportFragmentManager();
        fragmgr = getFragmentManager();
        simpleArcDialog = new SimpleArcDialog(getContext());
        /*TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);*/
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
//        TaggedneedsActivity.imgHamburger.setVisibility(View.VISIBLE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        init();
        // mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    paddress = "Y";
                } else {
                    paddress = "N";
                }
            }
        });

        HashMap<String, String> user = sessionManager.getUserDetails();
        LATITUDE = new GPSTracker(myContext).latitude;
        LONGITUDE = new GPSTracker(myContext).longitude;
        str_geopoint = LATITUDE + "," + LONGITUDE;
        strUser_ID = user.get(sessionManager.USER_ID);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("tab");
            fragmentpage = bundle.getString("page");
            if (fragmentpage.equals("detailspage")) {
                strCategory_click = "nonClicked";
                strSubCategory_click = "nonClicked";
                strAudiance_click = "nonClicked";
                TaggedneedsActivity.updateTitle("Edit Deed");
                strlocation = this.getArguments().getString("str_address");
                str_tagid = this.getArguments().getString("str_tagid");
                str_geopoint = this.getArguments().getString("str_geopoint");
                strImagenamereturned = this.getArguments().getString("str_taggedPhotoPath");
                strFullDescription = this.getArguments().getString("str_description");
                str_characterPath = this.getArguments().getString("str_characterPath");
                str_fname = this.getArguments().getString("str_fname");
                str_lname = this.getArguments().getString("str_lname");
                str_privacy = this.getArguments().getString("str_privacy");
                strUser_ID = this.getArguments().getString("str_userID");
                str_needName = this.getArguments().getString("str_needName");
                str_tab = this.getArguments().getString("tab");
                paddress = this.getArguments().getString("str_permanent");
                formattedSubTypePref = this.getArguments().getString("str_subtypes");
                str_totalTaggedCreditPoints = this.getArguments().getString("str_totalTaggedCreditPoints");
                str_totalFulfilledCreditPoints = this.getArguments().getString("str_totalFulfilledCreditPoints");
                str_title = this.getArguments().getString("str_title");
                str_date = this.getArguments().getString("str_date");
                str_distance = this.getArguments().getString("str_distance");
                str_validity = this.getArguments().getString("validity");
                strcontainer = this.getArguments().getString("container");
                strNeedmapping_ID = this.getArguments().getString("mappingId");
                // strUser_ID, str_tagid, strNeedmapping_ID, str_Geopint, strImagenamereturned, str_needName, strFullDescription, strlocation, strcontainer, str_validity

                TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
                TaggedneedsActivity.back.setVisibility(View.VISIBLE);
                TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
                TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
                TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
                TaggedneedsActivity.editprofile.setVisibility(View.GONE);
                TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
                TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
                TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                edselectlocation.setText(strlocation.trim());
                edDescription.setText(strFullDescription);
                layout_audiance.setVisibility(View.GONE);
                if (formattedSubTypePref.length() > 0) {
                    selectedPref.setText(formattedSubTypePref);
                }
                if (paddress.equals("Y")) {
                    locationSwitch.setChecked(true);
                    paddress = "Y";
                }
                flag = "1";
                getCategory();
                String strImagepath = WebServices.MAIN_SUB_URL + strImagenamereturned;
                if (strImagepath.length() > 57) {
                    Picasso.with(getContext()).load(strImagepath).placeholder(R.drawable.imgbackground).into(gieftneedimg);
                    gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);
                    String[] words = strImagenamereturned.split("/");
                    strImagenamereturned = words[2].replace(".png", "");
                    Log.d("editimgpath", strImagenamereturned);
                }
                /*else {
                    img.setImageResource(R.drawable.kidsbig);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            } else if (fragmentpage.equals("map_permanent_dialog")) {
                permanentLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.GONE);
            } else {
                lat = String.valueOf(new GPSTracker(myContext).latitude);
                longi = String.valueOf(new GPSTracker(myContext).longitude);
                getAddress(lat, longi);
            }
        } else {
            lat = String.valueOf(new GPSTracker(myContext).latitude);
            longi = String.valueOf(new GPSTracker(myContext).longitude);
            getAddress(lat, longi);
        }

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strCategory = edselectcategory.getText().toString();
                if (strCategory.length() < 1) {
                    ToastPopUp.displayToast(getContext(), "Select category");
                } else {
                    strSubCategory_click = "Clicked";
                    if (subcategories.size() > 0) {
                        showSubCatDialog();
                    } else {
                        getSubCategory();
                    }
                }
            }
        });

        edDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = edDescription.getText().length();
                // deeddetails_scrollview.scrollTo(0, edDescription.getBottom());
                deeddetails_scrollview.fullScroll(View.FOCUS_DOWN);
                // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                if (len > 499) {
                    Toast.makeText(getContext(), "Length cannot be greater than 500 characters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int i = 3;
                bundle.putString("tab", str_tab);
                bundle.putString("str_address", strlocation);
                bundle.putString("str_tagid", str_tagid);
                bundle.putString("str_geopoint", str_geopoint);
                bundle.putString("str_taggedPhotoPath", strImagenamereturned);
                bundle.putString("str_description", strFullDescription);
                bundle.putString("str_characterPath", str_characterPath);
                bundle.putString("str_fname", str_fname);
                bundle.putString("str_lname", str_lname);
                bundle.putString("str_privacy", str_privacy);
                bundle.putString("str_userID", strUser_ID);
                bundle.putString("str_needName", str_needName);
                bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                // bundle.putString("tab", tab);
                bundle.putString("str_title", str_title);
                bundle.putString("str_date", str_date);
                bundle.putString("str_distance", str_distance);
                NeedDetailsFrag mainHomeFragment = new NeedDetailsFrag();
                mainHomeFragment.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, mainHomeFragment).commit();
                // }
            }
        });
        // gieftneedimg.setImageResource(R.drawable.pictu);
        // gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Geopoints", str_Geopint);
                //gifDialog() ;
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else if (fragmentpage.equals("detailspage")) {
                    btnPost.setEnabled(false);
                    editdeedValidations();
                } else {
                    btnPost.setEnabled(false);
                    checkvalidations();
                }
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isReadStorageAllowed()) {
                        requestStoragePermission();
                        //If permission is already having then showing the toast
                        //  Toast.makeText(UploadImage.this,"You already have the permission",Toast.LENGTH_LONG).show();
                        //Existing the method with return
                    }
                    if (checkgallPermission()) {
                        // Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
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
                    } else {
                        requestgallPermission();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkgallPermission()) {
                    // Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                    loadImagefromGallery();
                } else {
                    requestgallPermission();
                }
//                Log.d("path", strCharacter_Path);
            }
        });

        edselectcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCategory_click = "Clicked";
                flag = "1";
                getCategory();
            }
        });

        edselectAudiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAudiance_click = "Clicked";
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    getUserGroups(strUser_ID);
                }
            }
        });

        //------------------Pranali--------------added code 05/082016
        edselectlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity(1);
            }
        });

        seekbarValidity.setMin(1);
        if (fragmentpage.equals("detailspage")) {
            seekbarValidity.setProgress(Integer.parseInt(str_validity) + 1);
            txtvalidity.setText(str_validity + "hr(s)");
        } else {
            seekbarValidity.setProgress(4);
        }
        seekbarValidity.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                //  Toast.makeText(getContext(), ""+value+"hrs", Toast.LENGTH_SHORT).show();
                txtvalidity.setText(value + "hr(s)");
                str_validity = String.valueOf(value);
                seekbarValidity.setProgress(value);
                //radius = value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 0;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                  /*  fragmgr = getFragmentManager();
                    // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).commit();*/
                    Bundle bundle = new Bundle();
                    // int i = 3;
                    bundle.putString("tab", value);
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
        return rootview;
    }

    //new permission for marshmallo
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    //new camera nougat code
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "GAD_" + timeStamp;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // File file=new File(storageDir,imageFileName+".png");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        // mCurrentPhotoPath = file.getAbsolutePath();
        return mediaFile;
        // Save a file: path for use with ACTION_VIEW intents
        //"file:" + image.getAbsolutePath();
        // return file;
    }

    private void submitdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
        dialogbtnconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
        dialogbtncancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
        dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);

        dialogtext.setText(getResources().getString(R.string.post_msg));
        //-------------Adding dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        // dialog.setStyle(DialogFragment.STYLE_NO_FRAME, 0);

        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // alertDialogForgot.setS
        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        btnPost.setEnabled(true);
        dialogbtnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPost.setEnabled(true);
                checkimage();
                //sendaTag();
                alertDialogForgot.dismiss();
            }
        });
        dialogbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPost.setEnabled(true);
                alertDialogForgot.dismiss();
            }
        });
    }

    //--------------------------Initilizing the UI variables--------------------------------------------
    private void init() {
        selectedUserGroups = new ArrayList<String>();
        selectedUserGrpNames = new ArrayList<String>();
        layout_audiance = (LinearLayout) rootview.findViewById(R.id.select_audiance_layout);
        selectedPref = (EditText) rootview.findViewById(R.id.tv_select_sub_cat);
        locationSwitch = (Switch) rootview.findViewById(R.id.switch_location);
        permanentLayout = (LinearLayout) rootview.findViewById(R.id.permanent_layout);
        addressLayout = (LinearLayout) rootview.findViewById(R.id.address_layout);
        btnCamera = (Button) rootview.findViewById(R.id.btngiftaneedcamera);
        btnGallery = (Button) rootview.findViewById(R.id.btngiftaneedgallary);
        btnPost = (Button) rootview.findViewById(R.id.btntaganeedpost);
        // txtuploadpic = (TextView) rootview.findViewById(R.id.txttaganeeduploadpic);
        txtDescription = (TextView) rootview.findViewById(R.id.txttaganeeddescription);
        //txtSelectcategory = (TextView) rootview.findViewById(R.id.txttaganeedselectcategory);
        // txtSelectLocation = (TextView) rootview.findViewById(R.id.txttaganeedlocation);
        deeddetails_scrollview = rootview.findViewById(R.id.deeddetails_scrollview);
        edDescription = (EditText) rootview.findViewById(R.id.edtaganeedDescription);
        edshortdescription = (EditText) rootview.findViewById(R.id.edtaganeedshortDescription);
        edselectcategory = (EditText) rootview.findViewById(R.id.edtaganeedcategory);
        edselectAudiance = (EditText) rootview.findViewById(R.id.select_audience);
        edselectlocation = (EditText) rootview.findViewById(R.id.edtaganeedlocation);
        gieftneedimg = (ImageView) rootview.findViewById(R.id.giftneedimg);
        imgcategory = (ImageView) rootview.findViewById(R.id.imgselectcategoryimg);
        layout_container = rootview.findViewById(R.id.layout_container);
        btnCamera.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btnGallery.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btnPost.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        seekbarValidity = rootview.findViewById(R.id.discreteValidityProgressbar);
        txtvalidity = rootview.findViewById(R.id.txtvalidity);
        Checkbox_container = rootview.findViewById(R.id.Checkbox_container);
        //   txtuploadpic.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtDescription.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        //txtSelectcategory.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        // txtSelectLocation.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edselectlocation.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edselectcategory.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edDescription.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edshortdescription.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
    }

    private void openAutocompleteActivity(int requestcode_MyLoc_MyDest) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(myContext);
            startActivityForResult(intent, requestcode_MyLoc_MyDest);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(myContext, data);

                edselectlocation.setText(place.getName() + ",\n" +
                        place.getAddress() + "\n" + place.getPhoneNumber());

                //  Toast.makeText(getApplicationContext(), "select latlong "+place.getLatLng(), Toast.LENGTH_LONG).show();


                StringTokenizer st = new StringTokenizer("" + place.getLatLng(), ",");
                int i = 0;
                String strLat = "0.0", strLong = "0.0";
                while (st.hasMoreTokens()) {
                    String strValue = st.nextToken();
                    //Log.d(TAG, strValue);
                    ++i;
                    if (i == 1) {
                        latitude_source = strValue.substring(10);
                        //    Log.d(TAG, "********** Latitude = " + strLat);
                    } else if (i == 2) {
                        longitude_source = strValue.substring(0, (strValue.length() - 1));
                        //Log.d(TAG, "********** Longitude = " + strLong);
                    }
                }
                str_geopoint = latitude_source + "," + longitude_source;
                Log.d("Geopoints after change", str_geopoint);
                /*if ((edselectlocation.getText().length() > 0) && (edselectcategory.length() > 0)) {
                    // background check for already added deeds for selected location
                    checkDeed();
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(myContext,
                        data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


        if (requestCode == REQUEST_CAMERA) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            //bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            BitmapFactory.Options options = new BitmapFactory.Options();


            options.inSampleSize = 8;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    // bitmap = BitmapFactory.decodeFile(filee.getPath(), options);
                    //bitmap= decodeSampledBitmapFromFile(filee.getPath().toString(),512,512);
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(filee), null, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            }
            //Toast.makeText(UploadImage.this, "camera" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            strimagePath = file.getAbsolutePath();
            //  bitmap= decodeSampledBitmapFromFile(strimagePath,512,512);
            gieftneedimg.setImageBitmap(bitmap);
            gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);


        }

       /* if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();


                options.inSampleSize = 8;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(fileUri),null,options);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                }
                gieftneedimg.setImageBitmap(bitmap);
                gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);


            } catch (Exception e) {
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                Bugreport bg = new Bugreport();
                bg.sendbug(writer.toString());
            }
        }*/

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            bitmap = decodeSampledBitmapFromFile(imgDecodableString, 512, 512);
            // bitmap = BitmapFactory.decodeFile(imgDecodableString);
            // Set the Image in ImageView after decoding the String
            gieftneedimg.setImageBitmap(bitmap);
            gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edDescription.setText(result.get(0));
            }
        }
    }

    //from lat to address
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        str_geopoint = LATITUDE + "," + LONGITUDE;
        // Log.d("Geopoints before change", str_Geopint);
        Geocoder geocoder = new Geocoder(myContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //  Log.d("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Toast.makeText(myContext, "Unable to find address", Toast.LENGTH_SHORT).show();
                //  Log.d("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.d("My Current loction address", "Canont get Address!");
        }
        return strAdd;
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
//------------------------filename
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    //--------------------------Loading image from gallary----------------------------------------------
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    //--------------------------getting user groups from server------------------------------------------
    public void getUserGroups(String user_id) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        String strDeviceid = SharedPrefManager.getInstance(getContext()).getDeviceToken();
        groupArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GroupsInterface service = retrofit.create(GroupsInterface.class);
        Call<List<GroupPOJO>> call = service.sendData(user_id);
        call.enqueue(new Callback<List<GroupPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupPOJO>> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("response_grouplist", "" + response.body());
                try {
                    List<GroupPOJO> res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.get(0).getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
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
                        List<GroupPOJO> groupPOJOS = response.body();
                        groupArrayList.clear();
                        try {
                            for (int i = 0; i < groupPOJOS.size(); i++) {
                                GroupPOJO groupPOJO = new GroupPOJO();
                                groupPOJO.setGroup_id(groupPOJOS.get(i).getGroup_id());
                                groupPOJO.setGroup_name(groupPOJOS.get(i).getGroup_name());
                                groupPOJO.setGroup_image(groupPOJOS.get(i).getGroup_image());
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {
                            StringWriter writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            Bugreport bg = new Bugreport();
                            bg.sendbug(writer.toString());
                        }

                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.user_orgs_dialog);
                        TextView txtHead = (TextView) dialog.findViewById(R.id.txt_head);
                        ListView userorglist = (ListView) dialog.findViewById(R.id.user_orgs_list);
                        final CheckBox chkOtherOrg = (CheckBox) dialog.findViewById(R.id.chk_all_other_orgs);
                        final CheckBox chkAllIndi = (CheckBox) dialog.findViewById(R.id.chk_all_individuals);
                        Button ok = (Button) dialog.findViewById(R.id.user_org_ok);
                        Button cancel = (Button) dialog.findViewById(R.id.user_org_cancel);
                        Log.d("groupArrayList_size", "" + groupArrayList.size());
                        if (groupArrayList.size() > 0) {
                            txtHead.setVisibility(View.VISIBLE);
                            userorglist.setVisibility(View.VISIBLE);
                            userorglist.setAdapter(new UserGroupAdapter(groupArrayList, getContext()));
                        } else {
                            txtHead.setVisibility(View.GONE);
                            userorglist.setVisibility(View.GONE);
                        }

                        if (checkedOtherOrg.equals("Y")) {
                            chkOtherOrg.setChecked(true);
                        }
                        if (checkedIndi.equals("Y")) {
                            chkAllIndi.setChecked(true);
                        }

                        chkOtherOrg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (chkOtherOrg.isChecked()) {
                                    checkedOtherOrg = "Y";
                                } else {
                                    checkedOtherOrg = "N";
                                }
                            }
                        });

                        chkAllIndi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (chkAllIndi.isChecked()) {
                                    checkedIndi = "Y";
                                } else {
                                    checkedIndi = "N";
                                }
                            }
                        });

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                            Toast.makeText(getContext(), String.valueOf(selectedUserGroups), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (selectedUserGrpNames.size() > 0) {
                                    if (selectedUserGrpNames.get(0).length() > 15) {
                                        String txt = selectedUserGrpNames.get(0).substring(0, 14) + "... ";
                                        int count = selectedUserGrpNames.size();
                                        if (checkedOtherOrg.equals("Y")) {
                                            count++;
                                        }
                                        if (checkedIndi.equals("Y")) {
                                            count++;
                                        }
                                        if (count > 1) {
                                            edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                        } else {
                                            edselectAudiance.setText(txt);
                                        }
                                    } else {
                                        String txt = selectedUserGrpNames.get(0);
                                        int count = selectedUserGrpNames.size();
                                        if (checkedOtherOrg.equals("Y")) {
                                            count++;
                                        }
                                        if (checkedIndi.equals("Y")) {
                                            count++;
                                        }
                                        if (count > 1) {
                                            edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                        } else {
                                            edselectAudiance.setText(txt);
                                        }
                                    }
                                } else {
                                    if (checkedOtherOrg.equals("Y") && checkedIndi.equals("N")) {
                                        edselectAudiance.setText(chkOtherOrg.getText());
                                    } else if (checkedOtherOrg.equals("N") && checkedIndi.equals("Y")) {
                                        edselectAudiance.setText(chkAllIndi.getText());
                                    } else if (checkedOtherOrg.equals("Y") && checkedIndi.equals("Y")) {
                                        edselectAudiance.setText(chkOtherOrg.getText() + " + 1 more");
                                    } else {
                                        edselectAudiance.setText("");
                                        edselectAudiance.clearFocus();
                                    }
                                }
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } catch (Exception e) {
                    simpleArcDialog.dismiss();
                    Log.d("response_grouplist", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("getorgs_error", "" + t.getMessage());
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    //--------------------------getting categories from server------------------------------------------
    public void getCategory() {
        categories = new ArrayList<>();
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CategoryInterface service = retrofit.create(CategoryInterface.class);

        Call<CategoryType> call = service.sendData("category");
        call.enqueue(new Callback<CategoryType>() {
            @Override
            public void onResponse(Response<CategoryType> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                CategoryType categoryType = response.body();
                Log.d("response_categories", "" + response.body());
                if (strCategory_click.equals("nonClicked")) {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        if (strNeedmapping_ID.equals(categoryType.getNeedtype().get(i).getNeedMappingID().toString())) {
                            spinnerPosition = i;
                            edselectcategory.setText(categoryType.getNeedtype().get(i).getNeedName().toString());
                            str_needid = categoryType.getNeedtype().get(i).getNeedMappingID().toString();
                            strCharacter_Path = categoryType.getNeedtype().get(i).getCharacterPath();
                            String path = WebServices.MAIN_SUB_URL + strCharacter_Path;
                            Picasso.with(getContext()).load(path).resize(50, 50).into(imgcategory);
                            strNeedmapping_ID = str_needid;
                            strCategory = str_needName;
                            if (str_needid.equals("1") || str_needid.equals("21")) {
                                layout_container.setVisibility(View.VISIBLE);
                                if (strcontainer.equals("1")) {
                                    Checkbox_container.setChecked(true);
                                } else {
                                    Checkbox_container.setChecked(false);
                                }
                            } else {
                                layout_container.setVisibility(View.GONE);
                            }
                            //seekbarValidity.setProgress(Integer.parseInt(str_validity));
                        }
                        //  signupPOJO.setName(categoryType.getNeedtype().get(i).getNeedName().toString());
                        //  signupPOJO.setCharacterpath(categoryType.getNeedtype().get(i).getCharacterPath());
                        //   signupPOJO.setPhotoPath(categoryType.getNeedtype().get(i).getIconPath());
                    }
                } else {
                    categories.clear();
                    try {
                        for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                            SignupPOJO signupPOJO = new SignupPOJO();
                            signupPOJO.setId(categoryType.getNeedtype().get(i).getNeedMappingID().toString());
                            signupPOJO.setName(categoryType.getNeedtype().get(i).getNeedName().toString());
                            signupPOJO.setCharacterpath(categoryType.getNeedtype().get(i).getCharacterPath());
                            signupPOJO.setPhotoPath(categoryType.getNeedtype().get(i).getIconPath());
                            categories.add(signupPOJO);
                        }
                    } catch (Exception e) {

                    }
                    Log.d("response_categories", "" + categories.size());
                    if (flag.equals("1")) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.category_dialog);
                        EditText edsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                        edsearch.setVisibility(View.GONE);

                        ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                        Button cancel = (Button) dialog.findViewById(R.id.category_cancel);

                        categorylist.setAdapter(new CountryAdapter(categories, getContext()));
                        categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {  //  category_id = categories.get(i).getCategory_id();
                                    if (categories.size() > 0) {
                                        edselectcategory.setText(categories.get(i).getName());
                                        strNeed_Name = edselectcategory.getText().toString();
                                        strNeedmapping_ID = categories.get(i).getId();
                                        strCharacter_Path = categories.get(i).getCharacterpath();
                                        String path = WebServices.MAIN_SUB_URL + strCharacter_Path;
                                        if (strNeedmapping_ID.equals("1") || strNeedmapping_ID.equals("21")) {
                                            layout_container.setVisibility(View.VISIBLE);
                                        } else {
                                            layout_container.setVisibility(View.GONE);
                                        }
                                        //Log.d("path",path);
                                        Picasso.with(getContext()).load(path).resize(50, 50).into(imgcategory);
                                        subcategories = new ArrayList<SubCategories>();
                                    /*if (edselectlocation.getText().length() > 0) {
                                        // background check for permanent location already added deeds
                                        checkDeed();
                                    }*/
                                        selectedPref.setText("");
                                        selectedPref.clearFocus();
                                    }
                                } catch (Exception e) {
                                    StringWriter writer = new StringWriter();
                                    e.printStackTrace(new PrintWriter(writer));
                                    Bugreport bg = new Bugreport();
                                    bg.sendbug(writer.toString());
                                }
                                //  Picasso.with(context).load(user.getImagelinlk()).resize(100, 100).into(img);
                       /* Log.d("name", strNeed_Name);
                        Log.d("id", strNeedmapping_ID);
                        Log.d("path", strCharacter_Path);*/

                        /*stateid.equals("");
                        cityid.equals("");*/

                                //getstate(contryid);
                                dialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        simpleArcDialog.dismiss();
                        dialog.show();
                    } else {
                        ArrayAdapter<SignupPOJO> adapter = new ArrayAdapter<SignupPOJO>(getContext(), android.R.layout.simple_spinner_item, categories);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adapter);
                        for (int i = 0; i < categories.size(); i++) {
                            if (strNeedmapping_ID.equals(categories.get(i).getId())) {
                                spinnerPosition = i;
                            }
                        }
                        spinnerType.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //--------------------------getting sub categories from server------------------------------------------
    public void getSubCategory() {
        subcategories = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SubCategoryInterface service = retrofit.create(SubCategoryInterface.class);
        Call<List<SubCategories>> call = service.sendData(strNeedmapping_ID);
        call.enqueue(new Callback<List<SubCategories>>() {
            @Override
            public void onResponse(Response<List<SubCategories>> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                List<SubCategories> subCategoryType = response.body();
                if (strSubCategory_click.equals("nonClicked")) {
                    /*for (int i = 0; i < subCategoryType.getSubTypeName().size(); i++) {
                        if (strNeedmapping_ID.equals(subCategoryType.getSubNeedtype().get(i).getSubTypeId())) {
                            edselectcategory.setText(subCategoryType.getSubNeedtype().get(i).getSubTypeName());
                            Log.d("selected_subtype", subCategoryType.getSubNeedtype().get(i).getSubTypeName());
                            str_needid = subCategoryType.getSubNeedtype().get(i).getSubTypeId();
                            strNeedmapping_ID = str_needid;
                            strCategory = str_needName;
                            mDialog.dismiss();
                        }
                    }*/
                } else {
                    subcategories.clear();
                    try {
                        for (int i = 0; i < subCategoryType.size(); i++) {
                            SubCategories subCat = new SubCategories();
                            subCat.setSubCatId(subCategoryType.get(i).getSubCatId());
                            subCat.setSubCatName(subCategoryType.get(i).getSubCatName());
                            subCat.setQty(0);
                            subcategories.add(subCat);
                        }
                    } catch (Exception e) {

                    }

                    showSubCatDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("subtype_error", t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void showSubCatDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.sub_category_dialog);
        final ListView subcategorylist = (ListView) dialog.findViewById(R.id.sub_categorylist);
        subcategorylist.setVisibility(View.VISIBLE);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.txt_msg);
        Button ok = (Button) dialog.findViewById(R.id.sub_category_ok);
        Button cancel = (Button) dialog.findViewById(R.id.sub_category_cancel);
        Button btnSuggestSubType = (Button) dialog.findViewById(R.id.suggest_sub_type);
        Log.d("subcatlist_size", "" + subcategories.size());
        if (subcategories.size() > 0) {
            tvMsg.setText("Select preferences for number of people");
            subcategorylist.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            subcategorylist.setAdapter(new SubCatAdapter(subcategories, getContext()));
        } else {
            tvMsg.setText("No subtypes found");
            ok.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            subcategorylist.setVisibility(View.GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subTypePref = new ArrayList<String>();
                for (int i = 0; i < subcategories.size(); i++) {
                    if (subcategories.get(i).getQty() > 0) {
                        subTypePref.add(subcategories.get(i).getSubCatName() + ":" + subcategories.get(i).getQty()); // add position of the row
                    }
                }
                dialog.dismiss();
                if (subcategories.size() > 0) {
                    formattedSubTypePref = subTypePref.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    selectedPref.setText(formattedSubTypePref);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSuggestSubType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.suggest_subtype_dialog);
                spinnerType = (Spinner) dialog.findViewById(R.id.spinner_main_type);
                final EditText et_suggest_type = (EditText) dialog.findViewById(R.id.et_suggested_type);
                Button ok = (Button) dialog.findViewById(R.id.suggest_ok);
                Button cancel = (Button) dialog.findViewById(R.id.suggest_cancel);
                if (categories.size() > 0) {
                    ArrayAdapter<SignupPOJO> adapter = new ArrayAdapter<SignupPOJO>(getContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapter);
                    for (int i = 0; i < categories.size(); i++) {
                        if (strNeedmapping_ID.equals(categories.get(i).getId())) {
                            spinnerPosition = i;
                        }
                    }
                    spinnerType.setSelection(spinnerPosition);
                } else {
                    flag = "0";
                    strCategory_click = "Clicked";
                    getCategory();
                }
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        itemid = categories.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_suggest_type.getText().length() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.enter_suggession), Toast.LENGTH_SHORT).show();
                        } else {
                            // call suggest subtype api
                            itemname = et_suggest_type.getText().toString();
                            Log.d("suggestion", itemid + itemname);
                            if (!(Validation.isNetworkAvailable(getContext()))) {
                                Toast.makeText(getContext(), "OOPS! No INTERNET. Please check your network connection", Toast.LENGTH_SHORT).show();
                            } else {
                                suggestSubType();
                            }
                            dialog.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        dialog.show();
    }

    //--------------------------suggest sub-type------------------------------------------
    public void suggestSubType() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SuggestSubType service = retrofit.create(SuggestSubType.class);
        Call<SuggestType> call = service.sendData(itemid, itemname);
        call.enqueue(new Callback<SuggestType>() {
            @Override
            public void onResponse(Response<SuggestType> response, Retrofit retrofit) {
                try {
                    Integer successstatus = response.body().getStatus();
                    if (successstatus == 0) {
                        Toast.makeText(getContext(), getResources().getString(R.string.suggession_error), Toast.LENGTH_SHORT).show();
                    } else if (successstatus == 1) {
                        Toast.makeText(getContext(), getResources().getString(R.string.subtype_success), Toast.LENGTH_SHORT).show();
                    } else if (successstatus == 2) {
                        Toast.makeText(getContext(), getResources().getString(R.string.subtype_already_exist), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("suggest_exception", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    public void checkDeed() {
        taggeddeeds = new ArrayList<>();
        simpleArcDialog = new SimpleArcDialog(getContext());
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        TaggedDeedsInterface service = retrofit.create(TaggedDeedsInterface.class);
        Log.d("input_params", strUser_ID + "," + str_geopoint + "," + strNeedmapping_ID);
        Call<List<TaggedDeedsPojo>> call = service.sendData(strUser_ID, str_geopoint, strNeedmapping_ID);
        call.enqueue(new Callback<List<TaggedDeedsPojo>>() {
            @Override
            public void onResponse(Response<List<TaggedDeedsPojo>> response, Retrofit retrofit) {
                List<TaggedDeedsPojo> taggedDeedsPojoList = response.body();
                Log.d("response_checkdeedlist", "" + response.body());
                int isblock = 0;
                try {
                    isblock = taggedDeedsPojoList.get(0).getIsBlocked();
                } catch (Exception e) {
                    isblock = 0;
                }
                if (isblock == 1) {
                    simpleArcDialog.dismiss();
                    FacebookSdk.sdkInitialize(getActivity());
                    Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
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
                    simpleArcDialog.dismiss();
                    taggeddeeds.clear();
                    try {
                        for (int i = 0; i < taggedDeedsPojoList.size(); i++) {
                            TaggedDeedsPojo taggedDeedsPojo = new TaggedDeedsPojo();
                            taggedDeedsPojo.setTagid(taggedDeedsPojoList.get(i).getTagid());
                            taggedDeedsPojo.setNeedname(taggedDeedsPojoList.get(i).getNeedname());
                            taggedDeedsPojo.setIconpath(taggedDeedsPojoList.get(i).getIconpath());
                            taggeddeeds.add(taggedDeedsPojo);
                        }
                    } catch (Exception e) {
                        StringWriter writer = new StringWriter();
                        e.printStackTrace(new PrintWriter(writer));
                        Bugreport bg = new Bugreport();
                        bg.sendbug(writer.toString());
                    }

                    if (taggeddeeds.size() > 0) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.already_deeds_dialog);
                        final ListView already_deedslist = (ListView) dialog.findViewById(R.id.already_deeds_list);
                        Button btnNewTag = (Button) dialog.findViewById(R.id.btn_new_tag);
                        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel_dialog);
                        Log.d("taggeddeeds_size", "" + taggeddeeds.size());
                        already_deedslist.setAdapter(new TaggedDeedsAdapter(taggeddeeds, getContext()));
                        btnNewTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                edselectcategory.setText("");
                                edselectcategory.clearFocus();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                fragmgr = getFragmentManager();
                                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("response_checkdeedlist", t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //------------------------Checking image is available or not-----------------------------------------
    public void checkimage() {
        if (bitmap == null) {
            // Toast.makeText(getContext(), "You have not selected any image", Toast.LENGTH_SHORT).show();
            // Log.d("image",image);
            if (!(Validation.isOnline(getActivity()))) {
                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
            } else {
                if (fragmentpage.equals("detailspage")) {
                    editTadg(str_tagid, strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, str_needName, strFullDescription, strlocation, strcontainer, str_validity, formattedSubTypePref, paddress);
                } else {
                    formattedUserOrgs = selectedUserGroups.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                    if (fragmentpage.equals("map_permanent_dialog")) {
                        paddress = "Y";
                        Bundle bundle = this.getArguments();
                        strlocation = bundle.getString("permanent_address");
                        str_geopoint = bundle.getString("permanent_geopoints");
                    }
                    if (checkedOtherOrg.equals("Y")) {
                        // if all groups is selected then send blank user selected groups
                        sendaTag(strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, strShortDescription, strFullDescription, strlocation, strcontainer, str_validity, paddress, formattedSubTypePref, checkedOtherOrg, checkedIndi, "");
                    } else {
                        sendaTag(strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, strShortDescription, strFullDescription, strlocation, strcontainer, str_validity, paddress, formattedSubTypePref, checkedOtherOrg, checkedIndi, formattedUserOrgs.trim());
                    }
                }
            }
        } else {
            image = getStringImage(bitmap);
            sendImageToServer();
            // Toast.makeText(getContext(), "Your bitmap is not empty", Toast.LENGTH_SHORT).show();
        }
    }

    //---------------------sending tag data to server-----------------------------------------------
    public void sendaTag(String user_id, String NeedMapping_ID, String geopoints, String Imagename, String title, String description, String locat, String container, String validity, String paddress, String subTypePref, String checkedOtherOrg, String checkedIndi, String userOrgs) {
        //sendaTag(strUser_ID, strNeedmapping_ID, strGeopoints, strImagenamereturned, strShortDescription, strlocation, strFullDescription);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        TagAneedInterface service = retrofit.create(TagAneedInterface.class);
        Call<MobileModel> call = service.sendData(user_id, NeedMapping_ID, geopoints, Imagename, title, description, locat, container, validity, paddress, subTypePref, checkedOtherOrg, checkedIndi, userOrgs);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("responsedeed1", "" + response.body());
                try {
                    MobileModel deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
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
                        MobileModel mobilemodel = response.body();
                        String successStatus = mobilemodel.getCheckstatus().get(0).getStatus().toString();
                        if (successStatus.equals("1")) {
                            edshortdescription.setText("");
                            edselectcategory.setText("");
                            edDescription.setText("");
                            edselectlocation.setText(adress_show);
                            gieftneedimg.setImageResource(R.drawable.pictu);
                            gieftneedimg.setScaleType(ImageView.ScaleType.FIT_XY);
                            imgcategory.setImageResource(android.R.color.transparent);
                            bitmap = null;
                            image = null;
                            int i = 7;
                            // ToastPopUp.displayToast(getContext(), "Your tag was successful");
                            String strImagepath = WebServices.MAIN_SUB_URL + strCharacter_Path;
                            strCreditpoints = mobilemodel.getCheckstatus().get(0).getCreditsEarned().toString();
                            strTotalpoints = mobilemodel.getCheckstatus().get(0).getTotalCredits().toString();
                            //String char_path=mobilemodel.getCheckstatus().get(0).get().toString();
                            gifDialog(strImagepath);
                        } else {
                            if (!(Validation.isOnline(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                ToastPopUp.show(getActivity(), "Tag was unsuccessful");
                            }
                        }
                    }
                } catch (Exception e) {
                    simpleArcDialog.dismiss();
                    Log.d("responsedeed2", "" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("responsedeed3", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //------------sending edited data to server---------------------------------------------------------
    public void editTadg(String deedId, String user_id, String NeedMapping_ID, final String geopoints, final String Imagename, final String title, final String description, final String locat, String container, String validity, String str_subtypes, String str_permanent) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        EditdeedInterface service = retrofit.create(EditdeedInterface.class);
        Call<StatusModel> call = service.sendData(user_id, deedId, NeedMapping_ID, geopoints, Imagename, title, description, locat, container, validity, str_subtypes, str_permanent);
        Log.d("edit_deed_params", "" + user_id + "," + deedId + "," + NeedMapping_ID + "," + geopoints + "," + Imagename + "," + title + "," + description + "," + locat + "," + container + "," + validity + "," + str_subtypes + "," + str_permanent);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                try {
                    StatusModel deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();
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
                        StatusModel statusModel = response.body();
                        int status = statusModel.getStatus();
                        if (status == 1) {
                            Toast.makeText(getContext(), "Edited successfully", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            int i = 3;
                            bundle.putString("tab", str_tab);
                            bundle.putString("str_address", locat);
                            bundle.putString("str_tagid", str_tagid);
                            bundle.putString("str_geopoint", geopoints);
                            bundle.putString("str_taggedPhotoPath", Imagename);
                            bundle.putString("str_description", description);
                            bundle.putString("str_characterPath", str_characterPath);
                            bundle.putString("str_fname", str_fname);
                            bundle.putString("str_lname", str_lname);
                            bundle.putString("str_privacy", str_privacy);
                            bundle.putString("str_userID", strUser_ID);
                            bundle.putString("str_needName", str_needName);
                            bundle.putString("str_subtypes", formattedSubTypePref);
                            bundle.putString("str_permanent", paddress);
                            bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                            bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                            // bundle.putString("tab", tab);
                            bundle.putString("str_title", strCategory);
                            bundle.putString("str_date", str_date);
                            bundle.putString("str_distance", str_distance);
                            NeedDetailsFrag mainHomeFragment = new NeedDetailsFrag();
                            mainHomeFragment.setArguments(bundle);
                            fragmgr.beginTransaction().replace(R.id.content_frame, mainHomeFragment).commit();
                        } else {
                            Toast.makeText(getContext(), "Edited unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    simpleArcDialog.dismiss();
                    Log.d("editdeed_exception", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("editdeed_onfailure", "" + t.getMessage());
            }
        });
    }

    //----------------------------------------
    private void returnDialog(final String credits_points, final String total_points, String char_path, String needtype) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(getActivity());
        View confirmDialog = li.inflate(R.layout.gift_dialog, null);


        btnOk = (Button) confirmDialog.findViewById(R.id.btndialogok);
        txtdialogcreditpoints = (TextView) confirmDialog.findViewById(R.id.txtcredit_points);
        txttotalpoints = (TextView) confirmDialog.findViewById(R.id.txttotal_points);
        txtneedname = (TextView) confirmDialog.findViewById(R.id.typefulfill);
        imgshare = (ImageView) confirmDialog.findViewById(R.id.imgdialogshare);
        imgchar = (ImageView) confirmDialog.findViewById(R.id.imgdialogchar);

        //-------------Adding dialog box to the view of alert dialog
        alertdialog.setView(confirmDialog);
        alertdialog.setCancelable(false);

        //----------------Creating an alert dialog
        alertDialogreturn = alertdialog.create();
        //alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        // alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //----------------Displaying the alert dialog
        alertDialogreturn.show();
        Picasso.with(getContext()).load(char_path).resize(50, 50).into(imgchar);
        txtneedname.setText(getResources().getString(R.string.tag_success_msg1) + needtype + getResources().getString(R.string.tag_success_msg2));
        txtdialogcreditpoints.setText(getResources().getString(R.string.tag_success_msg3) + credits_points + getResources().getString(R.string.tag_success_msg4));
        txttotalpoints.setText(getResources().getString(R.string.tag_success_msg5) + total_points);

        final String url = "https://play.google.com/store/apps/details?id=giftadeed.kshantechsoft.com.giftadeed";
        final String here = "here.";
        // Picasso.with(getContext()).load(char_path).resize(50, 50).into(imgchar);
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey!\n" +
                        "I am using ‘Gift-A-Deed’ charity mobile app.\n" +
                        "You can download it from:\n" +
                        "https://play.google.com/store/apps/details?id=giftadeed.kshantechsoft.com.giftadeed");

                startActivity(Intent.createChooser(share, "Share your points on:"));
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogreturn.dismiss();
                int i = 1;
                fragmgr = getFragmentManager();
                // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).commit();
            }
        });
    }

    //----------------------------------------GIF showing after tag
    private void gifDialog(final String strImagepath) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gif_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        //gif.setImageResource(R.drawable.claping);
        //  gif.setGifResource("asset:claping");

        dialog.show();
        ImageView img = (ImageView) dialog.findViewById(R.id.img_gif);
        Glide.with(this)
                .load(R.drawable.thumb)
                .into(img);


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    returnDialog(strCreditpoints, strTotalpoints, strImagepath, strNeed_Name);
                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    //-----------------get address-----------------
    public void getAddress(final String latitude, final String longitude) {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.GET_ADDRESS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        simpleArcDialog.dismiss();
                        //Showing toast message of the response
                        // Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        // strImagenamereturned = s;
                        // sendaTag(strUser_ID, strNeedmapping_ID, str_Geopint, strImagenamereturned, strShortDescription, strFullDescription, strlocation);
                        edselectlocation.setText(s);
                        Log.d("address", s);
                        str_geopoint = latitude + "," + longitude;
                        Log.d("Geopoints", str_geopoint);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastPopUp.show(myContext, getString(R.string.server_response_error));
                        simpleArcDialog.dismiss();

                        //Showing toast
                        //Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();


                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //----------------------sending image captured by camera or gallery to server-------------------
    public void sendImageToServer() {
        final Bundle bundle = this.getArguments();
        //final Bitmap photo = ((BitmapDrawable)gieftneedimg.getDrawable()).getBitmap();
        /*mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.UPLOAD_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        // mDialog.dismiss();
                        //Showing toast message of the response
                        // Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        strImagenamereturned = s;
                        if (!(Validation.isOnline(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            if (fragmentpage.equals("detailspage")) {
                                editTadg(str_tagid, strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, str_needName, strFullDescription, strlocation, strcontainer, str_validity, formattedSubTypePref, paddress);
                            } else {
                                formattedUserOrgs = selectedUserGroups.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                                if (fragmentpage.equals("map_permanent_dialog")) {
                                    paddress = "Y";
                                    strlocation = bundle.getString("permanent_address");
                                    str_geopoint = bundle.getString("permanent_geopoints");
                                }
                                if (checkedOtherOrg.equals("Y")) {
                                    // if all groups is selected then send blank user selected groups
                                    sendaTag(strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, strShortDescription, strFullDescription, strlocation, strcontainer, str_validity, paddress, formattedSubTypePref, checkedOtherOrg, checkedIndi, "");
                                } else {
                                    sendaTag(strUser_ID, strNeedmapping_ID, str_geopoint, strImagenamereturned, strShortDescription, strFullDescription, strlocation, strcontainer, str_validity, paddress, formattedSubTypePref, checkedOtherOrg, checkedIndi, formattedUserOrgs.trim());
                                }
                            }
                            Log.d("photopath", strImagenamereturned);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastPopUp.show(myContext, getString(R.string.server_response_error));
                        //Showing toast
                        //Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                // String image = getStringImage(bitmap);
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("image", image);
                params.put("name", "name");
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //-----------------------------getting string from bitmap image---------------------------------
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //-----------------------------checking validations---------------------------------------------
    public void checkvalidations() {
        strCategory = edselectcategory.getText().toString();
        if (fragmentpage.equals("map_permanent_dialog")) {
            paddress = "Y";
            Bundle bundle = this.getArguments();
            strlocation = bundle.getString("permanent_address");
            str_geopoint = bundle.getString("permanent_geopoints");
        } else {
            strlocation = edselectlocation.getText().toString();
        }
        strShortDescription = edselectcategory.getText().toString().trim();
        strFullDescription = edDescription.getText().toString().trim();


        if (strCategory.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select category");
            btnPost.setEnabled(true);
        } else if (selectedPref.getText().length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select sub-category");
            btnPost.setEnabled(true);
        } else if (strlocation.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select location");
            btnPost.setEnabled(true);
        } else if (edselectAudiance.getText().length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select audiance");
            btnPost.setEnabled(true);
        } /*else if (strShortDescription.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Enter short description");
            edshortdescription.setText("");
            edshortdescription.requestFocus();
        }*/
        /*else if (!(edshortdescription.getText().toString().matches("^(?!\\s*$|\\s).*$"))) {
            //edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edshortdescription.requestFocus();
            ToastPopUp.show(getContext(), getString(R.string.first_character_not_space));
        }*/
        /*else if (strFullDescription.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Enter description");
            edDescription.setText("");
            btnPost.setEnabled(true);
            edDescription.requestFocus();
        } */
        else if (layout_container.getVisibility() == View.VISIBLE) {
            if (Checkbox_container.isChecked()) {
                strcontainer = "1";
            } else {
                strcontainer = "0";
            }
            submitdialog();
        }
       /* else if (!(edDescription.getText().toString().matches("^(?!\\s*$|\\s).*$"))) {
            //edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edDescription.requestFocus();
            ToastPopUp.show(getContext(), getString(R.string.first_character_not_space));
        }*/
        else {
            /*Log.d("category:", strCategory);
            Log.d("location:", strlocation);
            Log.d("title:", strShortDescription);
            Log.d("title:", strFullDescription);
            Log.d("geo:", str_Geopint);*/


            submitdialog();
        }
    }

    //------------------------------checking edit deed validations--------------------------------------
    public void editdeedValidations() {
        strCategory = edselectcategory.getText().toString();
        strlocation = edselectlocation.getText().toString();
        strShortDescription = edselectcategory.getText().toString().trim();
        strFullDescription = edDescription.getText().toString().trim();

        if (strCategory.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select category");
            btnPost.setEnabled(true);
        } else if (strlocation.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Select location");
            btnPost.setEnabled(true);
        } /*else if (strFullDescription.length() < 1) {
            ToastPopUp.displayToast(getContext(), "Enter description");
            edDescription.setText("");
            btnPost.setEnabled(true);
            edDescription.requestFocus();
        } */ else if (layout_container.getVisibility() == View.VISIBLE) {
            if (Checkbox_container.isChecked()) {
                strcontainer = "1";
            } else {
                strcontainer = "0";
            }
            submitdialog();
        }
       /* else if (!(edDescription.getText().toString().matches("^(?!\\s*$|\\s).*$"))) {
            //edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edDescription.requestFocus();
            ToastPopUp.show(getContext(), getString(R.string.first_character_not_space));
        }*/
        else {
            /*Log.d("category:", strCategory);
            Log.d("location:", strlocation);
            Log.d("title:", strShortDescription);
            Log.d("title:", strFullDescription);
            Log.d("geo:", str_Geopint);*/


            submitdialog();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    if (fragmentpage.equals("detailspage")) {
                        Bundle bundle = new Bundle();
                        int i = 3;
                        bundle.putString("tab", str_tab);

                        bundle.putString("str_address", strlocation);
                        bundle.putString("str_tagid", str_tagid);
                        bundle.putString("str_geopoint", str_geopoint);
                        bundle.putString("str_taggedPhotoPath", strImagenamereturned);
                        bundle.putString("str_description", strFullDescription);
                        bundle.putString("str_characterPath", str_characterPath);
                        bundle.putString("str_fname", str_fname);
                        bundle.putString("str_lname", str_lname);
                        bundle.putString("str_privacy", str_privacy);
                        bundle.putString("str_userID", strUser_ID);
                        bundle.putString("str_needName", str_needName);
                        bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                        bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                        // bundle.putString("tab", tab);
                        bundle.putString("str_title", str_title);
                        bundle.putString("str_date", str_date);
                        bundle.putString("str_distance", str_distance);

                        NeedDetailsFrag mainHomeFragment = new NeedDetailsFrag();
                        mainHomeFragment.setArguments(bundle);
                        fragmgr.beginTransaction().replace(R.id.content_frame, mainHomeFragment).commit();
                    } else {


                        Bundle bundle = new Bundle();
                        int i = 3;
                        bundle.putString("tab", value);
                        TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                        mainHomeFragment.setArguments(bundle);
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                        fragmentTransaction.commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public boolean checkcamPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);

        //int FourthPermissionResult = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

        // FourthPermissionResult == PackageManager.PERMISSION_GRANTED;*/
    }

    public boolean checkgallPermission() {
        //int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);

        //int FourthPermissionResult = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);

        //FirstPermissionResult == PackageManager.PERMISSION_GRANTED&&
        return ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

        // FourthPermissionResult == PackageManager.PERMISSION_GRANTED;*/
    }

    //---------------------request permissions method---------------------------------------------------
    private void requestcameraPermission() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // SendMail smail=new SendMail("giftadeed2017@gmail.com","Error",e.toString());
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }
        /*ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                       *//* ACCESS_FINE_LOCATION*//*
                }, RequestPermissionCode);*/


    }

    private void requestgallPermission() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();

            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // SendMail smail=new SendMail("giftadeed2017@gmail.com","Error",e.toString());
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }
        /*ActivityCompat.requestPermissions(getActivity(), new String[]
                {

                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                       *//* ACCESS_FINE_LOCATION*//*
                }, RequestPermissionCode);*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean InternetPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && WriteExternalStoragePermission && ReadExternalStoragePermission) {

                        //Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Displaying a toast
                    Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}


