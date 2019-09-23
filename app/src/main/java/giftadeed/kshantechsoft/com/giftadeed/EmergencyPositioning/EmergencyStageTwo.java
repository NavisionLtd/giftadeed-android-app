package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
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
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<EmergencyTypes> emergencyList;
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
    Bitmap capturedBitmap, rotatedBitmap;
    String callingfrom, latitude, longitude;
    ArrayList<String> checkedList;
    String formattedChecked = "";
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
        mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_SOS_UPLOADS);
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
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
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

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (!(Validation.isOnline(EmergencyStageTwo.this))) {
            ToastPopUp.show(EmergencyStageTwo.this, getString(R.string.network_validation));
        } else {
            getTypes();
        }

        btnSendDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedList = new ArrayList<String>();
                List<EmergencyTypes> stList = ((EmergencyTypesAdapter) mAdapter)
                        .getCheckedList();

                for (int i = 0; i < stList.size(); i++) {
                    EmergencyTypes type = stList.get(i);
                    if (type.isSelected()) {
                        checkedList.add(type.getTypeid());
                    }
                }

                formattedChecked = checkedList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                Log.d("checked_list", "" + formattedChecked);

                if (!(Validation.isOnline(EmergencyStageTwo.this))) {
                    ToastPopUp.show(EmergencyStageTwo.this, getString(R.string.network_validation));
                } else {
                    if (etCurrentLocation.getText().length() <= 0) {
                        ToastPopUp.displayToast(EmergencyStageTwo.this, getResources().getString(R.string.sos_location_error));
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
        Log.d("input_createsos", "userid : " + userid + " , sostypes : " + sostypes);
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
                        Toast.makeText(EmergencyStageTwo.this, getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (sosResponseStatus.getSos_id() > 0) {
                            String sosid = String.valueOf(sosResponseStatus.getSos_id());
                            Log.d("sosid", "" + sosid);
                            Toast.makeText(EmergencyStageTwo.this, getResources().getString(R.string.sos_success), Toast.LENGTH_SHORT).show();
                            //firebase call for image store with sos id
                            if (capturedBitmap != null) {
                                uploadFile(sosid);
                            }
                            if (callingfrom.equals("login")) {
                                EmergencyStageTwo.this.finish();
                            } else {
                                Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                                startActivity(i);
                            }
                        } else if (sosResponseStatus.getSos_id() == 0) {
                            Toast.makeText(EmergencyStageTwo.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
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
        if (fileUri != null) {
            //getting the storage reference
            final StorageReference sRef = storageReference.child(WebServices.SOS_STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask = sRef.putFile(fileUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return sRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        if (downloadUri != null) {
                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("Upload " + photoStringLink);
                            UploadSOS upload = new UploadSOS(sosid, photoStringLink);
                            mFirebaseDatabase.child(sosid).setValue(upload);
                        }
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            //display an error if no file is selected
        }
    }

    // method to get nature of emergency types from server
    public void getTypes() {
        simpleArcDialog.setConfiguration(new ArcConfiguration(this));
        simpleArcDialog.show();
        simpleArcDialog.setCancelable(false);
        emergencyList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        EmergencyTypesInterface service = retrofit.create(EmergencyTypesInterface.class);
        Call<List<EmergencyTypes>> call = service.sendData("category");
        call.enqueue(new Callback<List<EmergencyTypes>>() {
            @Override
            public void onResponse(Response<List<EmergencyTypes>> response, Retrofit retrofit) {
                simpleArcDialog.dismiss();
                List<EmergencyTypes> subCategoryType = response.body();
                emergencyList.clear();
                try {
                    for (int i = 0; i < subCategoryType.size(); i++) {
                        EmergencyTypes subCat = new EmergencyTypes();
                        subCat.setTypeid(subCategoryType.get(i).getTypeid());
                        subCat.setType(subCategoryType.get(i).getType());
                        subCat.setSelected(false);
                        emergencyList.add(subCat);
                    }
                } catch (Exception e) {

                }

                if (emergencyList.size() > 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter = new EmergencyTypesAdapter(emergencyList);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                simpleArcDialog.dismiss();
                Log.d("emergency_type_error", t.getMessage());
                ToastPopUp.show(EmergencyStageTwo.this, getString(R.string.server_response_error));
            }
        });
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
            if (resultCode == RESULT_OK) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        capturedBitmap = BitmapFactory.decodeStream(EmergencyStageTwo.this.getContentResolver().openInputStream(fileUri), null, options);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    capturedBitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    strimagePath = fileUri.getPath();
                }
                int bitmap_file_size = capturedBitmap.getByteCount();
                Log.d("camera_photo_size", "bitmap_size : " + bitmap_file_size);
//                imageCaptured.setVisibility(View.VISIBLE);
                rotateImage(setReducedImageSize());
            }
        }
    }

    private Bitmap setReducedImageSize() {
        int targetIVwidth = imageCaptured.getWidth();
        int targetIVheight = imageCaptured.getHeight();

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
        imageCaptured.setImageBitmap(rotatedBitmap);
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
        strimagePath = mediaFile.getAbsolutePath();
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
