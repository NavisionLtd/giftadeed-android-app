package giftadeed.kshantechsoft.com.giftadeed.giftaneed;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Animation.FadeInActivity;
import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.BuildConfig;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedInterface;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeeddeletedModel;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
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
import static android.app.Activity.RESULT_OK;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Used to fulfill a need                                   //
/////////////////////////////////////////////////////////////////
public class GiftANeedFrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener, Animation.AnimationListener {
    FragmentActivity myContext;
    View rootview;
    public static final int RequestPermissionCode = 1;
    int requiredDistFulfill;
    File mediaFile;
    int value = 1;
    Button btncamera, btngallary, btnsubmit, dialogbtnconfirm, dialogbtncancel, btnOk;
    ImageView gieftneedimg;
    TextView dialogtext, switchText;
    TextView txtdialogcreditpoints, txttotalpoints, txtneedname;
    ImageView imgshare, imgchar;
    EditText edstartwriting, noOfPeople;
    SwitchCompat swichFulfil;
    String fulfilOnOff = "Yes";
    ImageView benefitedInfo, imagePlus, imageMinus;
    String ispartial = "N";
    static FragmentManager fragmgr;
    private static int RESULT_LOAD_IMG = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private Bitmap capturedBitmap, rotatedBitmap;
    String imageToSend, strImagenamereturned;
    SimpleArcDialog simpleArcDialog;
    SessionManager sessionManager;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public String strimagePath;
    Uri filee;
    int REQUEST_CAMERA = 0;
    private GoogleApiClient mGoogleApiClient;
    String str_address, str_tagid, str_geopoint, str_taggedPhotoPath, str_description, str_characterPath, str_fname,
            str_lname, str_privacy, str_userID, str_needName, str_totalTaggedCreditPoints, str_totalFulfilledCreditPoints,
            str_title, str_date, str_distance, strAboutgift, strNoOfPeople, strUser_ID, strCreditpoints, strTotalpoints, tab;
    private static final String IMAGE_DIRECTORY_NAME = "GiftaDeed";
    private AlertDialog alertDialogForgot, alertDialogreturn;
    double current_latitude, current_longitude;

    public static GiftANeedFrag newInstance() {
        GiftANeedFrag fragment = new GiftANeedFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.gift_title));
        rootview = inflater.inflate(R.layout.fragment_gift_aneed, container, false);
        init();
        fragmgr = getFragmentManager();
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        sessionManager = new SessionManager(getActivity());
        // mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        } catch (Exception e) {

        }
        //-------------------getting data from previous fragment and stoing in string-------------------
        str_address = this.getArguments().getString("str_address");
        str_tagid = this.getArguments().getString("str_tagid");
        str_geopoint = this.getArguments().getString("str_geopoint");
        str_taggedPhotoPath = this.getArguments().getString("str_taggedPhotoPath");
        str_description = this.getArguments().getString("str_description");
        str_characterPath = this.getArguments().getString("str_characterPath");
        str_fname = this.getArguments().getString("str_fname");
        str_lname = this.getArguments().getString("str_lname");
        str_privacy = this.getArguments().getString("str_privacy");
        str_userID = this.getArguments().getString("str_userID");
        str_needName = this.getArguments().getString("str_needName");
        tab = this.getArguments().getString("tab");
        str_totalTaggedCreditPoints = this.getArguments().getString("str_totalTaggedCreditPoints");
        str_totalFulfilledCreditPoints = this.getArguments().getString("str_totalFulfilledCreditPoints");
        str_title = this.getArguments().getString("str_title");
        str_date = this.getArguments().getString("str_date");
        str_distance = this.getArguments().getString("str_distance");

        //--------------Locking nevigation drawer---------------------------------------------------
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //-----------getting user id from shared preferences----------------------------------------
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        simpleArcDialog = new SimpleArcDialog(getContext());
        getFeetDistance();

        current_latitude = new GPSTracker(myContext).getLatitude();
        current_longitude  = new GPSTracker(myContext).getLongitude();

        edstartwriting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 49) {
                    ToastPopUp.show(getContext(), getString(R.string.length_msg_3));
                }
            }
        });
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isReadStorageAllowed()) {
                        requestStoragePermission();
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

        imagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNoOfPeople = noOfPeople.getText().toString().trim();
                if (strNoOfPeople.length() > 0) {
                    value = Integer.parseInt(strNoOfPeople);
                } else {
                    value = 1;
                }

                if (value != 99999) {
                    value = value + 1;
                    noOfPeople.setText("" + value);
                } else {
                    ToastPopUp.displayToast(getContext(), "You have exceeded limit");
                }
            }
        });

        imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNoOfPeople = noOfPeople.getText().toString().trim();
                if (strNoOfPeople.length() > 0) {
                    value = Integer.parseInt(strNoOfPeople);
                } else {
                    value = 1;
                }

                if (value == 0) {
                    ToastPopUp.displayToast(getContext(), "At least one person should be benefited");
                } else {
                    if (value != 1) {
                        value = value - 1;
                        noOfPeople.setText("" + value);
                    } else {
                        ToastPopUp.displayToast(getContext(), "At least one person should be benefited");
                    }
                }
            }
        });

        benefitedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(myContext);
                dialog.setContentView(R.layout.dialog_help_info);
                dialog.setCancelable(false);
                final TextView txtHelpInfo = (TextView) dialog.findViewById(R.id.txt_help_info);
                final ImageView btnInfoCancel = (ImageView) dialog.findViewById(R.id.iv_help_info_close);
                txtHelpInfo.setText(getResources().getString(R.string.mention_people_benifited));
                btnInfoCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btngallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkgallPermission()) {
                    loadImagefromGallery();
                } else {
                    requestgallPermission();
                }
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsubmit.setEnabled(false);
                checkvalidations();
                // checkimage();
            }
        });
        //-------------------pressing back icon in app and sendind data back----------------------------
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
                    int i = 7;
                    //fragmgr.beginTransaction().replace(R.id.content_frame, NeedDetailsFrag.newInstance(i)).commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("str_address", str_address);
                    bundle.putString("str_tagid", str_tagid);
                    bundle.putString("str_geopoint", str_geopoint);
                    bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                    bundle.putString("str_description", str_description);
                    bundle.putString("str_characterPath", str_characterPath);
                    bundle.putString("str_fname", str_fname);
                    bundle.putString("str_lname", str_lname);
                    bundle.putString("str_privacy", str_privacy);
                    bundle.putString("str_userID", str_userID);
                    bundle.putString("str_needName", str_needName);
                    bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                    bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);
                    bundle.putString("tab", tab);
                    bundle.putString("str_title", str_title);
                    bundle.putString("str_date", str_date);
                    bundle.putString("str_distance", str_distance);
                    NeedDetailsFrag fragInfo = new NeedDetailsFrag();
                    fragInfo.setArguments(bundle);
                    fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
                }
            }
        });

        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 7;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    fragmgr = getFragmentManager();
                    // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                    fragmgr.beginTransaction().replace(R.id.content_frame, NeedDetailsFrag.newInstance(i)).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        swichFulfil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    fulfilOnOff = "Yes";
                    ispartial = "N";
                    switchText.setText("Yes");
                    edstartwriting.setHint(getResources().getString(R.string.tell_people));
                } else {
                    fulfilOnOff = "No";
                    ispartial = "Y";
                    switchText.setText("No");
                    edstartwriting.setHint(getResources().getString(R.string.tell_people_remaining));
                }
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


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //------------------------------------Taking image from camera------------------------------
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        capturedBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(filee), null, options);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    capturedBitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    strimagePath = fileUri.getPath();
                }
                int bitmap_file_size = capturedBitmap.getByteCount();
                Log.d("camera_photo_size", "bitmap_size : " + bitmap_file_size);
                rotateImage(setReducedImageSize());
            }
        }

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
            strimagePath = cursor.getString(columnIndex);
            cursor.close();
            capturedBitmap = decodeSampledBitmapFromFile(strimagePath, 512, 512);
            int bitmap_file_size = capturedBitmap.getByteCount();
            Log.d("gallery_photo_size", "bitmap_size : " + bitmap_file_size);
            rotateImage(setReducedImageSize());
        }
    }

    private Bitmap setReducedImageSize() {
        int targetIVwidth = gieftneedimg.getWidth();
        int targetIVheight = gieftneedimg.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strimagePath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetIVwidth, cameraImageHeight / targetIVheight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(strimagePath, bmOptions);
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(strimagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        gieftneedimg.setImageBitmap(rotatedBitmap);
        imageToSend = getStringImage(rotatedBitmap);
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

    //---------------------------nougat updated code

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "GAD_" + timeStamp;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        strimagePath = mediaFile.getAbsolutePath();
        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //------------------------------loading image from gallary--------------------------------------
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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

    //------------------------Initilizing UI variables----------------------------------------------
    private void init() {
        benefitedInfo = (ImageView) rootview.findViewById(R.id.info_benefited_help);
        imagePlus = (ImageView) rootview.findViewById(R.id.image_plus);
        imageMinus = (ImageView) rootview.findViewById(R.id.image_minus);
        btncamera = (Button) rootview.findViewById(R.id.giftcamera);
        btngallary = (Button) rootview.findViewById(R.id.giftgallary);
        gieftneedimg = (ImageView) rootview.findViewById(R.id.giftneedimg);
        btnsubmit = (Button) rootview.findViewById(R.id.btnsubmitgift);
        noOfPeople = (EditText) rootview.findViewById(R.id.no_of_fulfilled);
        edstartwriting = (EditText) rootview.findViewById(R.id.edgiftaneedsomething);
        swichFulfil = (SwitchCompat) rootview.findViewById(R.id.switch_fulfil);
        switchText = (TextView) rootview.findViewById(R.id.switch_text);
        edstartwriting.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btncamera.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btngallary.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        btnsubmit.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        noOfPeople.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        noOfPeople.setText("1");
        edstartwriting.clearFocus();
        btnsubmit.setFocusable(true);
    }

    //---------------------------checking image is avilable to send to server-----------------------
    public void checkimage() {
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            if (rotatedBitmap == null) {
                fullfilTag(strUser_ID, str_tagid, strImagenamereturned, strAboutgift, ispartial, str_needName, strNoOfPeople);
            } else {
                sendImageToServer();
            }
        }

    }

    //---------------------------sending image to server--------------------------------------------
    public void sendImageToServer() {
        simpleArcDialog = new SimpleArcDialog(getContext());
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Uploading image...");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.UPLOAD_GIFT_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        simpleArcDialog.dismiss();
                        strImagenamereturned = s;
                        Calendar c = Calendar.getInstance();
                        c.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String currentDateTimeString = df.format(c.getTime());
                        Log.d("upload_image_api", "Response received time : " + currentDateTimeString);
                        Log.d("upload_image_api", "Response string : " + s);

                        if (!(Validation.isNetworkAvailable(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            fullfilTag(strUser_ID, str_tagid, strImagenamereturned, strAboutgift, ispartial, str_needName, strNoOfPeople);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        simpleArcDialog.dismiss();
                        ToastPopUp.show(myContext, getString(R.string.server_response_error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("image", imageToSend);
                params.put("name", "name");
                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateTimeString = df.format(c.getTime());
                Log.d("upload_image_api", "Parameters request time : " + currentDateTimeString);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,  // 20 seconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //--------------------getting string from bitmap------------------------------------------------
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //-----------------------checking validations before sending to server--------------------------
    public void checkvalidations() {
        strAboutgift = edstartwriting.getText().toString().trim();
        strNoOfPeople = noOfPeople.getText().toString().trim();
        if (strNoOfPeople.length() > 0) {
            value = Integer.parseInt(strNoOfPeople);
        } else {
            value = 1;
        }
        Log.d("people_value", "" + value);
        if (strNoOfPeople.length() < 1) {
            noOfPeople.requestFocus();
            noOfPeople.setFocusable(true);
            btnsubmit.setEnabled(true);
            noOfPeople.setError(getResources().getString(R.string.valid_number));
        } else if (value < 1) {
            noOfPeople.requestFocus();
            noOfPeople.setFocusable(true);
            btnsubmit.setEnabled(true);
            noOfPeople.setError(getResources().getString(R.string.valid_number));
        } else if (strAboutgift.length() < 1) {
            ToastPopUp.displayToast(getContext(), getResources().getString(R.string.tell_people));
            edstartwriting.setText("");
            btnsubmit.setEnabled(true);
            edstartwriting.requestFocus();
        } else {
            // Log.d("category:", strAboutgift);
            if (requiredDistFulfill == 0) {
                requiredDistFulfill = 10;
            }
            submitdialog();
        }
    }

    //---------------------------submit dialog before sending to server-----------------------------
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
        //alert.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        //-----------------------------------animation for dialog-----------------------------------
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        btnsubmit.setEnabled(true);
        dialogbtnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------distance code
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
//                    double current_latitude = new GPSTracker(myContext).getLatitude();
//                    double current_longitude = new GPSTracker(myContext).getLongitude();
                    Location myLocation = new Location("My Location");
                    myLocation.setLatitude(current_latitude);
                    myLocation.setLongitude(current_longitude);
                    String str_geo_point = str_geopoint;
                    //String str_geo_point = str_geopoint_new;
                    String[] words = str_geo_point.split(",");
                    if (words.length > 1) {
                        Location tagLocation2 = new Location("tag Location");
                        tagLocation2.setLatitude(Double.parseDouble(words[0]));
                        tagLocation2.setLongitude(Double.parseDouble(words[1]));

//--------------------------distance in km----------------------------------------------------------
                        float dist1 = myLocation.distanceTo(tagLocation2) / 1000;
//---------------------------distance in feet-------------------------------------------------------
                        float ft_distance = dist1 * 3280.8f;

                        if (ft_distance < requiredDistFulfill) {
                            isDeedDeleted();
                        } else {
                            //Toast.makeText(myContext, "You are not in the 10 feet area of needy persons", Toast.LENGTH_SHORT).show();
                            //fragmgr.beginTransaction().replace(R.id.content_frame, GiftANeedFrag.newInstance()).commit();

                            final AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                            LayoutInflater li = LayoutInflater.from(getContext());
                            View confirmDialog = li.inflate(R.layout.signup_dialog, null);
                            Button btnOk = (Button) confirmDialog.findViewById(R.id.btndialogsignupok);
                            TextView txtheadingofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_heading);
                            TextView txtofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_text);
                            // txtheadingofdialog.setText("Sign up successful!");
                            txtheadingofdialog.setVisibility(View.GONE);
                            txtofdialog.setText("You need to be within " + requiredDistFulfill + " feet area of the needy person");
                            //-------------Adding dialog box to the view of alert dialog
                            alertdialog.setView(confirmDialog);
                            alertdialog.setCancelable(false);

                            //----------------Creating an alert dialog
                            alertDialogreturn = alertdialog.create();

                            alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                            alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            //----------------Displaying the alert dialog
                            alertDialogreturn.show();


                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialogreturn.dismiss();
                               /* Intent loginintent = new Intent(SignUp.this, SendBirdLoginActivity.class);
                                loginintent.putExtra("message", message);
                                startActivity(loginintent);*/
                                }
                            });

                        }
                    }

                    //checkimage();

                    alertDialogForgot.dismiss();
                }
            }
        });
        dialogbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForgot.dismiss();
            }
        });
    }

    //-----------------------------------sending data to server-------------------------------------
    public void fullfilTag(String strUser_id, String strTag_ID, String strfulfilphotopath, String strDescr, String ispartial, String need, String no_of_people) {
        /*OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);*/
        simpleArcDialog = new SimpleArcDialog(getContext());
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Fulfilling deed...");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)    //.client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GiftaNeedInterface service = retrofit.create(GiftaNeedInterface.class);
        Call<MobileModel> call = service.sendData(strUser_id, strTag_ID, strfulfilphotopath, strDescr, ispartial, need, no_of_people);
        Log.d("fulfill_input", no_of_people);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
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
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        MobileModel model = response.body();
                        String success_status = model.getCheckstatus().get(0).getStatus();
                        if (success_status.equals("1")) {
                            String strImagepath = str_characterPath;
                            strCreditpoints = model.getCheckstatus().get(0).getCreditsEarned().toString();
                            strTotalpoints = model.getCheckstatus().get(0).getTotalCredits().toString();
                            Intent intent = new Intent(getContext(), FadeInActivity.class);
                            intent.putExtra("credit_points", strCreditpoints);
                            intent.putExtra("total_points", strTotalpoints);
                            intent.putExtra("need_name", str_needName);
                            intent.putExtra("reason", "by fulfilling");
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //-------getting distance from server to fulfill
    public void getFeetDistance() {
        simpleArcDialog.setConfiguration(new ArcConfiguration(getContext()));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        // cities.clear();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FulfillDistanceInterface service = retrofit.create(FulfillDistanceInterface.class);
        Call<FulfilDistance> call = service.sendData("10");
        call.enqueue(new Callback<FulfilDistance>() {
            @Override
            public void onResponse(Response<FulfilDistance> response, Retrofit retrofit) {
                FulfilDistance fdistance = response.body();
                fdistance.getDistancevalue().get(0).getDistanceName();
                fdistance.getDistancevalue().get(0).getDistanceUnit();
                requiredDistFulfill = Integer.parseInt(fdistance.getDistancevalue().get(0).getDistanceValue());
                Log.d("dist", String.valueOf(requiredDistFulfill));
                simpleArcDialog.dismiss();
            }

            // return dist;
            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
                simpleArcDialog.dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edstartwriting.post(new Runnable() {
            @Override
            public void run() {
                edstartwriting.requestFocus();
                //  edstartwriting.setHint(Html.fromHtml("<font color='#000000'></font> "));
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                /*InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(edstartwriting, InputMethodManager.SHOW_IMPLICIT);*/
            }
        });

        //edstartwriting.requestFocus();

//------------------------------------------back button press ---------------------------------------
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Bundle bundle = new Bundle();

                    bundle.putString("str_address", str_address);
                    bundle.putString("str_tagid", str_tagid);
                    bundle.putString("str_geopoint", str_geopoint);
                    bundle.putString("str_taggedPhotoPath", str_taggedPhotoPath);
                    bundle.putString("str_description", str_description);
                    bundle.putString("str_characterPath", str_characterPath);
                    bundle.putString("str_fname", str_fname);
                    bundle.putString("str_lname", str_lname);
                    bundle.putString("str_privacy", str_privacy);
                    bundle.putString("str_userID", str_userID);
                    bundle.putString("str_needName", str_needName);
                    bundle.putString("str_totalTaggedCreditPoints", str_totalTaggedCreditPoints);
                    bundle.putString("str_totalFulfilledCreditPoints", str_totalFulfilledCreditPoints);

                    bundle.putString("str_title", str_title);
                    bundle.putString("str_date", str_date);
                    bundle.putString("str_distance", str_distance);


                    NeedDetailsFrag mainHomeFragment = new NeedDetailsFrag();
                    mainHomeFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment).addToBackStack(null);
                    fragmentTransaction.commit();

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

    //---------------------request permissions method-----------------------------------------------
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

                //Toast.makeText(getActivity(), "Permission are denied please go to settings and enable", Toast.LENGTH_LONG).show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // SendMail smail=new SendMail("giftadeed2017@gmail.com",getResources().getString(R.string.error),e.toString());
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }
       /* ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                       *//* ACCESS_FINE_LOCATION*//*
                }, RequestPermissionCode);*/


    }

    //------------------------------------request require permissions-----------------------------------
    private void requestgallPermission() {

       /* ActivityCompat.requestPermissions(getActivity(), new String[]
                {

                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                       *//* ACCESS_FINE_LOCATION*//*
                }, RequestPermissionCode);*/
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
                //Toast.makeText(getActivity(), "Permission are denied please go to settings and enable", Toast.LENGTH_LONG).show();

            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(getActivity(), "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // SendMail smail=new SendMail("giftadeed2017@gmail.com",getResources().getString(R.string.error),e.toString());
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       /* if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(getActivity(), "Permission are denied please go to settings and enable", Toast.LENGTH_LONG).show();

        }*/
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
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();

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

    //--------------------check deed is already deleted or not------------------------------------------
    public void isDeedDeleted() {
        simpleArcDialog = new SimpleArcDialog(getContext());
        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setText("Checking deed availability...");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String user_id = user.get(sessionManager.USER_ID);
        String name = user.get(sessionManager.USER_NAME);
        final String fname = name.split(" ")[0];
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DeeddeletedInterface service = retrofit.create(DeeddeletedInterface.class);
        Call<DeeddeletedModel> call = service.fetchData(str_tagid);
        call.enqueue(new Callback<DeeddeletedModel>() {
            @Override
            public void onResponse(Response<DeeddeletedModel> response, Retrofit retrofit) {
                try {
                    DeeddeletedModel statusModel = response.body();
                    int strstatus = statusModel.getIsDeleted();
                    if (strstatus == 0) {
                        simpleArcDialog.dismiss();
                        checkimage();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.already_deed), Toast.LENGTH_SHORT).show();
                        simpleArcDialog.dismiss();
                        Bundle bundle = new Bundle();
                        int i = 3;
                        bundle.putString("tab", tab);
                        TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                        mainHomeFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction =
                                getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                        fragmentTransaction.commit();
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
                simpleArcDialog.dismiss();
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    //----------------------------------------gif showing after tag
    private void gifDialog(final String strImagepath) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gif_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageView img = (ImageView) dialog.findViewById(R.id.img_gif);
        Glide.with(getActivity())
                .load(R.drawable.clap)
                .into(img);
        dialog.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    returnDialog(strCreditpoints, strTotalpoints, strImagepath, str_needName);
                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    //---------------------------Return dialog on fullfiling need-----------------------------------
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

        txtneedname.setText(" You have fulfilled " + needtype + " need");
        txtdialogcreditpoints.setText("You have earned " + credits_points + " point(s)");
        txttotalpoints.setText("Your total point(s) are " + total_points);
        Picasso.with(getContext()).load(char_path).resize(50, 50).into(imgchar);
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String android_shortlink = "http://tiny.cc/kwb33y";
                String ios_shortlink = "http://tiny.cc/h4533y";
                String website = "https://www.giftadeed.com/";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey! My latest points are " + total_points + " in the GiftADeed App.\n" +
                        "You can earn your points by downloading the app from\n\n" +
                        "Android : " + android_shortlink + "\n" +
                        "iOS : " + ios_shortlink + "\n\n" +
                        "Also, check the website at " + website);
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
