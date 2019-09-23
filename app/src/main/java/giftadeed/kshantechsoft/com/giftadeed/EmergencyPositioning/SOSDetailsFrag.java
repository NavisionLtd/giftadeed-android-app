package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.SingleDeedMap;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
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

//////////////////////////////////////////////////////////////////////////
//  Shows details of resource.
//  Delete resource option is only available for resource creator.
/////////////////////////////////////////////////////////////////////////

public class SOSDetailsFrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    FragmentActivity myContext;
    LinearLayout creatorlayout, locationIcon, emergencyLayout;
    View rootview;
    private AlertDialog alertDialog;
    ImageView sosImage;
    TextView txtsos_creater, txtaddress, txtemergency, txtDate;
    Button btnDeleteSOS;
    String strUser_ID, str_geopoints = "";
    static FragmentManager fragmgr;
    SessionManager sessionManager;
    SimpleArcDialog mDialog;
    String str_sosid;
    private GoogleApiClient mGoogleApiClient;
    private List<UploadSOS> soslist;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseStorage mStorageInstance;
    private StorageReference photoRef;
    String path = "", mImageUrl = "";

    public static SOSDetailsFrag newInstance(int sectionNumber) {
        SOSDetailsFrag fragment = new SOSDetailsFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.sos_details_layout, container, false);
        sessionManager = new SessionManager(getActivity());
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.emer_details));
        init();
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        str_sosid = this.getArguments().getString("str_sosid");
        if (!(Validation.isOnline(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            getSOS_Details();
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_SOS_UPLOADS);
            photoRef = FirebaseStorage.getInstance().getReference();
            soslist = new ArrayList<>();
            DatabaseReference reference = mFirebaseDatabase;
            //adding an event listener to fetch values
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        UploadSOS uploadSOS = postSnapshot.getValue(UploadSOS.class);
                        soslist.add(uploadSOS);
                    }
                    Log.d("sos_list", "" + soslist.size());
                    if (soslist.size() > 0) {
                        for (UploadSOS sos : soslist) {
                            if (sos.getSosid().equals(str_sosid)) {
                                path = sos.getSosurl();
                            }
                        }
                        if (path.length() > 0) {
                            sosImage.setVisibility(View.VISIBLE);
                            Glide.with(myContext).load(path).into(sosImage);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("str_tagid", str_sosid);
                bundle.putString("str_geopoint", str_geopoints);
                bundle.putString("str_characterPath", "https://www.giftadeed.com/api/image/sos/sos_marker.png");
                bundle.putString("tab", "from_sos");
                SingleDeedMap fragInfo = new SingleDeedMap();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
            }
        });

        btnDeleteSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                    Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                    Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                    TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                    dialogtext.setText(getResources().getString(R.string.remove_sos_msg));
                    alert.setView(confirmDialog);
                    alert.setCancelable(false);
                    alertDialog = alert.create();
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                    dialogconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // call delete resource api
                            if (!(Validation.isOnline(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                deleteSOS();
                            }
                            alertDialog.dismiss();
                        }
                    });
                    dialogcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to mapview
                fragmgr = getFragmentManager();
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
            }
        });
        return rootview;
    }

    public void getSOS_Details() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        sessionManager = new SessionManager(getActivity());
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SOSDetailsInterface service = retrofit.create(SOSDetailsInterface.class);
        Call<List<EmergencyInfoPOJO>> call = service.fetchData(str_sosid);
        call.enqueue(new Callback<List<EmergencyInfoPOJO>>() {
            @Override
            public void onResponse(Response<List<EmergencyInfoPOJO>> response, Retrofit retrofit) {
                Log.d("response_sosdetails", "" + response.body());
                try {
                    List<EmergencyInfoPOJO> emergencyInfoPOJOS = response.body();
                    mDialog.dismiss();
                    if (emergencyInfoPOJOS.get(0).getUsername() != null) {
                        creatorlayout.setVisibility(View.VISIBLE);
                        txtsos_creater.setText(emergencyInfoPOJOS.get(0).getUsername());
                    } else {
                        creatorlayout.setVisibility(View.GONE);
                    }
                    if (emergencyInfoPOJOS.get(0).getSostype() != null) {
                        if (emergencyInfoPOJOS.get(0).getSostype().length() > 0) {
                            emergencyLayout.setVisibility(View.VISIBLE);
                            txtemergency.setText(emergencyInfoPOJOS.get(0).getSostype());
                        } else {
                            emergencyLayout.setVisibility(View.GONE);
                        }
                    }
                    str_geopoints = emergencyInfoPOJOS.get(0).getGeopoints();
                    txtaddress.setText(emergencyInfoPOJOS.get(0).getAddress());
                    txtDate.setText(emergencyInfoPOJOS.get(0).getCdate());
                } catch (Exception e) {
                    Log.d("response_sosdetails", "" + e.getMessage());
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_sosdetails", "" + t.getMessage());
                mDialog.dismiss();
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    //---------------------sos delete---------------------
    public void deleteSOS() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SOSDeleteInterface service = retrofit.create(SOSDeleteInterface.class);
        Call<GroupResponseStatus> call = service.fetchData(strUser_ID, str_sosid);
        Log.d("input_sos", strUser_ID + ":" + str_sosid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                Log.d("responsedelsos", "" + response.body());
                try {
                    GroupResponseStatus groupResponseStatus = response.body();
                    int isblock = 0;
                    try {
                        isblock = groupResponseStatus.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
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

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), "SOS deleted successfully", Toast.LENGTH_SHORT).show();

                            //Deleting SOS image from firebase
                            mFirebaseDatabase.child(str_sosid).child("sosurl").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    try {
                                        if (snapshot.getValue() != null) {
                                            try {
                                                mImageUrl = "" + snapshot.getValue();
                                                Log.d("sos_imageurl", mImageUrl);
                                                mStorageInstance = FirebaseStorage.getInstance();
                                                //delete image reference file
                                                photoRef = mStorageInstance.getReferenceFromUrl(mImageUrl);
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // File deleted successfully
                                                        Log.d("sos_firebase", "onSuccess: deleted file");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Uh-oh, an error occurred!
                                                        Log.d("sos_firebase", "onFailure: did not delete file");
                                                    }
                                                });

                                                //delete database child
                                                mFirebaseDatabase.child(str_sosid).removeValue();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("sos_imageurl", " it's null.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("onCancelled", " cancelled");
                                }
                            });

                            fragmgr = getFragmentManager();
                            fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsedelsos", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("responsedelsos", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void init() {
        sosImage = (ImageView) rootview.findViewById(R.id.sos_image);
        txtsos_creater = (TextView) rootview.findViewById(R.id.tv_sos);
        txtemergency = (TextView) rootview.findViewById(R.id.tv_emergency);
        locationIcon = (LinearLayout) rootview.findViewById(R.id.sos_details_locationicon);
        emergencyLayout = (LinearLayout) rootview.findViewById(R.id.sos_emergency_layout);
        creatorlayout = (LinearLayout) rootview.findViewById(R.id.sos_creator_layout);
        txtaddress = (TextView) rootview.findViewById(R.id.txt_sos_address);
        txtDate = (TextView) rootview.findViewById(R.id.tv_sos_created);
        btnDeleteSOS = (Button) rootview.findViewById(R.id.btn_delete_sos);
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
                    fragmgr = getFragmentManager();
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
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
}
