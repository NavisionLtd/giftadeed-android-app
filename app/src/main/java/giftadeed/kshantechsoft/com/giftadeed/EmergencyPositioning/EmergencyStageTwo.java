package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.BuildConfig;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EmergencyStageTwo extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btnOpenCamera, btnSendDetails;
    private AlertDialog alertDialogLocation;
    CheckBox ch1, ch2, ch3, ch4, ch5, ch6;
    TextView tvRefreshLocation;
    ImageView imageCaptured;
    EditText etCurrentLocation;
    SimpleArcDialog simpleArcDialog;
    int REQUEST_CAMERA = 0;
    public static final int STORAGE_PERMISSION_CODE = 23;
    public static final int RequestPermissionCode = 1;
    public String strimagePath = "", strUser_ID = "", strDeviceid = "", strGeopoints = "", strAddress = "";
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "GiftaDeed";
    Bitmap bitmap;
    String callingfrom, latitude, longitude;
    ArrayList<String> checkedList;
    String formattedChecked = "";
    public static final String DATABASE_SOS_UPLOADS = "SOS";
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_stage2);
        btnOpenCamera = (Button) findViewById(R.id.btn_open_camera);
        btnSendDetails = (Button) findViewById(R.id.btn_sos_send_details);
        imageCaptured = (ImageView) findViewById(R.id.captured_photo);
        etCurrentLocation = (EditText) findViewById(R.id.et_current_location);
        tvRefreshLocation = (TextView) findViewById(R.id.tv_refresh_location);
        ch1 = (CheckBox) findViewById(R.id.chk_flood);
        ch2 = (CheckBox) findViewById(R.id.chk_eq);
        ch3 = (CheckBox) findViewById(R.id.chk_huricanes);
        ch4 = (CheckBox) findViewById(R.id.chk_tornado);
        ch5 = (CheckBox) findViewById(R.id.chk_volcano);
        ch6 = (CheckBox) findViewById(R.id.chk_tsunami);
        checkedList = new ArrayList<String>();
        sessionManager = new SessionManager(this);
        simpleArcDialog = new SimpleArcDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(EmergencyStageTwo.this)
                .enableAutoManage(EmergencyStageTwo.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(DATABASE_SOS_UPLOADS);
        storageReference = FirebaseStorage.getInstance().getReference();
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        strDeviceid = SharedPrefManager.getInstance(this).getDeviceToken();
        callingfrom = getIntent().getStringExtra("callingfrom");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isReadStorageAllowed()) {
                        requestStoragePermission();
                        //If permission is already having then showing the toast
                        //  Toast.makeText(UploadImage.this,"You already have the permission",Toast.LENGTH_LONG).show();
                        //Existing the method with return
                    }
                    if (checkgallPermission()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                fileUri = FileProvider.getUriForFile(EmergencyStageTwo.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
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
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }
        });

        strAddress = getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
        strGeopoints = latitude + "," + longitude;
        etCurrentLocation.setText(strAddress);

        tvRefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSTracker gps = new GPSTracker(EmergencyStageTwo.this);
                if (!gps.isGPSEnabled) {
                    gps.showSettingsAlert();
                } else {
                    String Latitude = String.valueOf(new GPSTracker(EmergencyStageTwo.this).getLatitude());
                    String Longitude = String.valueOf(new GPSTracker(EmergencyStageTwo.this).getLongitude());
                    strAddress = getAddress(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
                    strGeopoints = Latitude + "," + Longitude;
                    etCurrentLocation.setText(strAddress);
                }
            }
        });

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch1.getText().toString())) {
                        checkedList.add(String.valueOf(ch1.getText()));
                    }
                } else {
                    if (checkedList.contains(ch1.getText().toString())) {
                        checkedList.remove(String.valueOf(ch1.getText()));
                    }
                }
            }
        });

        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch2.getText().toString())) {
                        checkedList.add(String.valueOf(ch2.getText()));
                    }
                } else {
                    if (checkedList.contains(ch2.getText().toString())) {
                        checkedList.remove(String.valueOf(ch2.getText()));
                    }
                }
            }
        });

        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch3.getText().toString())) {
                        checkedList.add(String.valueOf(ch3.getText()));
                    }
                } else {
                    if (checkedList.contains(ch3.getText().toString())) {
                        checkedList.remove(String.valueOf(ch3.getText()));
                    }
                }
            }
        });

        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch4.getText().toString())) {
                        checkedList.add(String.valueOf(ch4.getText()));
                    }
                } else {
                    if (checkedList.contains(ch4.getText().toString())) {
                        checkedList.remove(String.valueOf(ch4.getText()));
                    }
                }
            }
        });

        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch5.getText().toString())) {
                        checkedList.add(String.valueOf(ch5.getText()));
                    }
                } else {
                    if (checkedList.contains(ch5.getText().toString())) {
                        checkedList.remove(String.valueOf(ch5.getText()));
                    }
                }
            }
        });

        ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (!checkedList.contains(ch6.getText().toString())) {
                        checkedList.add(String.valueOf(ch6.getText()));
                    }
                } else {
                    if (checkedList.contains(ch6.getText().toString())) {
                        checkedList.remove(String.valueOf(ch6.getText()));
                    }
                }
            }
        });

        btnSendDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formattedChecked = checkedList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                Log.d("checked_list", "" + formattedChecked);
                if (!(Validation.isOnline(EmergencyStageTwo.this))) {
                    ToastPopUp.show(EmergencyStageTwo.this, getString(R.string.network_validation));
                } else {
                    if (etCurrentLocation.getText().length() <= 0) {
                        ToastPopUp.displayToast(EmergencyStageTwo.this,"Location cannot be blank. Please get location");
                        /*AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyStageTwo.this);
                        LayoutInflater li = LayoutInflater.from(EmergencyStageTwo.this);
                        View confirmDialog = li.inflate(R.layout.refresh_location_dialog, null);
                        Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_refresh_yes);
                        Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_refresh_no);

                        //-------------Adding our dialog box to the view of alert dialog
                        alert.setView(confirmDialog);
                        alert.setCancelable(false);
                        alertDialogLocation = alert.create();
                        alertDialogLocation.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                        alertDialogLocation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialogLocation.show();
                        alertDialogLocation.setCancelable(false);
                        dialogconfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GPSTracker gps = new GPSTracker(EmergencyStageTwo.this);
                                if (!gps.isGPSEnabled) {
                                    gps.showSettingsAlert();
                                }
                                latitude = String.valueOf(new GPSTracker(EmergencyStageTwo.this).getLatitude());
                                longitude = String.valueOf(new GPSTracker(EmergencyStageTwo.this).getLongitude());
                                strAddress = getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                strGeopoints = latitude + "," + longitude;
                                etCurrentLocation.setText(strAddress);
                                alertDialogLocation.dismiss();
                            }
                        });
                        dialogcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogLocation.dismiss();
                            }
                        });*/
                    } else {
                        if (strUser_ID != null) {
                            createSOS(strUser_ID, "", strGeopoints, strAddress, formattedChecked, "1");
                        } else {
                            createSOS("", strDeviceid, strGeopoints, strAddress, formattedChecked, "1");
                        }
                    }
                }
            }
        });
    }

    //---------------------sending sos details to server-----------------------------------------------
    public void createSOS(String userid, String devid, String geopoints, String address, String sostypes, String devtype) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setText("Sending emergency details");
        simpleArcDialog.setConfiguration(configuration);
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CreateSOSInterface service = retrofit.create(CreateSOSInterface.class);
        Call<SOSResponseStatus> call = service.sendData(userid, devid, geopoints, address, sostypes, devtype);
        call.enqueue(new Callback<SOSResponseStatus>() {
            @Override
            public void onResponse(Response<SOSResponseStatus> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                Log.d("responsesos", "" + response.body());
                try {
                    SOSResponseStatus sosResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = sosResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        simpleArcDialog.dismiss();
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        Toast.makeText(EmergencyStageTwo.this, "You have been blocked", Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        int i = new DBGAD(EmergencyStageTwo.this).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (sosResponseStatus.getSos_id() > 0) {
                            String sosid = String.valueOf(sosResponseStatus.getSos_id());
                            Log.d("sosid", "" + sosid);
                            Toast.makeText(EmergencyStageTwo.this, "Emergency created successfully", Toast.LENGTH_SHORT).show();
                            //firebase call for image store with sos id
                            if (bitmap != null) {
                                uploadFile(sosid);
                            }
                            if (callingfrom.equals("login")) {
                                EmergencyStageTwo.this.finish();
                            } else {
                                Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                                startActivity(i);
                            }
                        } else if (sosResponseStatus.getSos_id() == 0) {
                            Toast.makeText(EmergencyStageTwo.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsesos", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("responsesos", "" + t.getMessage());
                ToastPopUp.show(EmergencyStageTwo.this, getString(R.string.server_response_error));
            }
        });
    }

    private void uploadFile(final String sosid) {
        //checking if file is available
        if (fileUri != null) {
            //displaying progress dialog while image is uploading
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg");

            //adding the file to reference
            sRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
//                            progressDialog.dismiss();

                            //displaying success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            UploadSOS upload = new UploadSOS(sosid, taskSnapshot.getDownloadUrl().toString());
                            mFirebaseDatabase.child(sosid).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
//                            progressDialog.dismiss();
                            Log.d("FirebaseUploadError", "" + exception.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(addresses.get(0).getAddressLine(0));
//                result.append(address.getLocality()).append("\n");
//                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        Log.d("your location", result.toString());
        return result.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    bitmap = BitmapFactory.decodeStream(EmergencyStageTwo.this.getContentResolver().openInputStream(fileUri), null, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            }
            strimagePath = file.getAbsolutePath();
            imageCaptured.setVisibility(View.VISIBLE);
            imageCaptured.setImageBitmap(bitmap);
            imageCaptured.setScaleType(ImageView.ScaleType.FIT_XY);
        }
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

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(EmergencyStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(EmergencyStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(EmergencyStageTwo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public boolean checkgallPermission() {
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(EmergencyStageTwo.this, WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(EmergencyStageTwo.this, READ_EXTERNAL_STORAGE);
        return ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestgallPermission() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EmergencyStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", EmergencyStageTwo.this.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(EmergencyStageTwo.this, "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", EmergencyStageTwo.this.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RequestPermissionCode);
                Toast.makeText(EmergencyStageTwo.this, "Permission are denied please enable permissions", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
//            StringWriter writer = new StringWriter();
//            e.printStackTrace(new PrintWriter(writer));
//            Bugreport bg = new Bugreport();
//            bg.sendbug(writer.toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
