/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.MyProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.BuildConfig;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.main.ConnectionManager;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PreferenceUtils;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils.PushUtils;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CityAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CityModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CitySignup;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountrySignup;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobilecheckInterface;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignUp;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateSignup;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Utility;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows user profile details                               //
/////////////////////////////////////////////////////////////////
public class MyProfilefrag extends Fragment  {
    private StorageReference storageReference;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private List<Profile> profileList;
    private static final String IMAGE_DIRECTORY_NAME = "GiftaDeed";
    Uri fileUri;
    Bitmap bitmap;
    String path = "";
    String strimagePath = "", firebasePhotoUrl = "", strAvatarPath = "";
    View rootview;
    CircleImageView profilePic;
    Button changeProfilePic;
    static Fragment fragment;
    // FragmentManager fragmentManager;
    static FragmentManager fragmgr;
    FragmentActivity myContext;
    String strProfilrName, strCreditpoints, strUserId, strfname, stremail, strmobile, strgender, strprivacy;
    String strCountry, contryid, strState, stateid, strCity, cityid;
    private String strMobileno, strFirstName, strLastName, strAddress, stredgender, strPassword, strnewPassword;
    RadioGroup radioGroup, radiogrpprivacy;
    TextView txtName, txtCredits, txtPoints, txtGender;
    EditText edFname, edLname, edEmail, edPhone, edAddress, edCountry, edState, edCity, edpassword;
    TextInputLayout txtinputlayoutmyprofilepassword;
    Button btnbtnSave;
    RadioButton rbtnMale, rbtnFemale, rbtnpublic, rbtnanonymous;
    ImageView imgEditProfile, imgShare;
    SharedPrefManager sharedPrefManager;
    MyProfilefrag fragmentobj;
    CountryAdapter ctryadptr;
    StateAdapter stateadptr;
    CityAdapter cityadptr;
    EditText txtsearch;
    int editflag = 0;
    private ArrayList<SignupPOJO> countri = new ArrayList<>();
    private ArrayList<SignupPOJO> countries = new ArrayList<>();
    private ArrayList<SignupPOJO> stat = new ArrayList<>();
    private ArrayList<SignupPOJO> states = new ArrayList<>();
    private ArrayList<SignupPOJO> citi = new ArrayList<>();
    private ArrayList<SignupPOJO> cities = new ArrayList<>();
    SignUp country = new SignUp();
    SimpleArcDialog mDialog;


    public static MyProfilefrag newInstance(int sectionNumber) {
        MyProfilefrag fragment = new MyProfilefrag();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.my_profile));
        rootview = inflater.inflate(R.layout.fragment_my_profilefrag, container, false);
        //-------------------------------hide keyboard----------------------------------------------
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog = new SimpleArcDialog(getContext());
        sharedPrefManager = new SharedPrefManager(getActivity());
        init();
//----------------------------------------Camera and filter invisible-------------------------------
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = MyProfilefrag.newInstance(0);
        edEmail.setEnabled(false);
        //----------------------------------getting user id and name from shared preferences--------
        HashMap<String, String> user = sharedPrefManager.getUserDetails();
        strUserId = user.get(sharedPrefManager.USER_ID);
        Log.d("loggedin_userid", "" + strUserId);
        strProfilrName = user.get(sharedPrefManager.USER_NAME);
        txtName.setText(strProfilrName);
        if (!(Validation.isNetworkAvailable(getContext()))) {
            Toast.makeText(getContext(), getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {
            getProfileDetails();
            enabletext(false);
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_PROFILE_PIC_UPLOADS);
            storageReference = FirebaseStorage.getInstance().getReference();
            profileList = new ArrayList<>();
            DatabaseReference reference = mFirebaseDatabase.child("profile");
            //adding an event listener to fetch values
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Profile profile = postSnapshot.getValue(Profile.class);
                        profileList.add(profile);
                    }
                    Log.d("profile_list", "" + profileList.size());
                    if (profileList.size() > 0) {
                        for (Profile profile : profileList) {
                            if (profile.getUserid().equals(strUserId)) {
                                path = profile.getPhotourl();
                            }
                        }
                        if (path.length() > 0) {
                            Glide.with(getContext()).load(path).into(profilePic);
                            sharedPrefManager.store_profile_image_path(path);
                        } else {
                            Glide.with(getContext()).load(R.drawable.ic_default_profile_pic).into(profilePic);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        TaggedneedsActivity.editprofile.setVisibility(View.VISIBLE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else if (edFname.getText().length() < 1 || edLname.getText().length() < 1 || edCountry.getText().length() < 1 || edState.getText().length() < 1 || edCity.getText().length() < 1) {
                    edFname.setText("");
                    edLname.setText("");
                    edCountry.setText("");
                    edCity.setText("");
                    if (edpassword.getVisibility() == View.VISIBLE) {
                        edpassword.setText("");
                    }
                    editflag = 1;
                    if (!(Validation.isNetworkAvailable(getContext()))) {
                        Toast.makeText(getContext(), getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                    } else {
                        getProfileDetails();
                    }
                    //Toast.makeText(getContext(), "please retry editing your profile", Toast.LENGTH_SHORT).show();
                } else {
                    changeProfilePic.setVisibility(View.VISIBLE);
                    enabletext(true);
                    TaggedneedsActivity.editprofile.setVisibility(View.GONE);
                    TaggedneedsActivity.saveprofile.setVisibility(View.VISIBLE);
                }
            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.profile_pic_option_dialog);
//                dialog.setTitle("Title...");
                TextView text1 = (TextView) dialog.findViewById(R.id.opt_take_photo);
                TextView text2 = (TextView) dialog.findViewById(R.id.opt_gallery);
                TextView text3 = (TextView) dialog.findViewById(R.id.opt_avatars);
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                fileUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        } else {
                            fileUri = getOutputMediaFileUri(100);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        }
                        startActivityForResult(i, 100);
                        dialog.dismiss();
                    }
                });
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 200);
                        dialog.dismiss();
                    }
                });
                text3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(Validation.isNetworkAvailable(getContext()))) {
                            ToastPopUp.show(getContext(), getString(R.string.network_validation));
                        } else {
                            Intent i = new Intent(getContext(), ShowImageActivity.class);
                            startActivityForResult(i, 300);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });


       /* imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabletext(true);
            }
        });*/

        //----------------------sharing to social media---------------------------------------------
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey! My latest credit score is " + strCreditpoints + " points in the GiftADeed App.\n" +
                        "You can earn your credit points by downloading the app from\n\n" +
                        "Android : " + WebServices.android_shortlink + "\n" +
                        "iOS : " + WebServices.ios_shortlink + "\n\n" +
                        "Also, check the website at " + WebServices.website_url);
                startActivity(Intent.createChooser(share, "Share your points on:"));
            }
        });
        edFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     edMerchantName.setSelection(edMerchantName.getText().length());   //set cursor at right placee of text

                /*if (edFname.getText().length()>14){
                    ToastPopUp.show(getContext(), "Length cannot be grater then 15 characters");
                }*/

                if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
                    edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!(edFname.getText().toString().matches("^[a-zA-Z.'_\\s]*$"))) {
                    edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 15) {
                    if (edFname.isEnabled()) {
                        ToastPopUp.show(getContext(), getString(R.string.length_error));
                    }
                }
            }
        });
        final int maxLength = 15;


        edFname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strFirstName = edFname.getText().toString().trim();
                if (!hasFocus) {
                    if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
                        ToastPopUp.show(getContext(), getString(R.string.Enter_FirstName));
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (!(strFirstName.matches("^[a-zA-Z.'_\\s]*$"))) {
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        ToastPopUp.show(getActivity(), getString(R.string.validation_first_name_special_characters_merchant) + " in first name");
                    } else if (!(edFname.getText().toString().matches("\\w*"))) {
//                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                        ToastPopUp.show(getContext(), getString(R.string.spaces_not_allowed));
                        strFirstName = strFirstName.replaceAll("\\s{2,}", " ");
                    } else if ((edFname.getText().toString().length() < 3) || (edFname.getText().toString().length() > 15)) {
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        ToastPopUp.show(getContext(), getString(R.string.min_length));
                    }
                    /*else if ((edFname.getText().toString().length()>15)) {
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        ToastPopUp.show(context, getString(R.string.max_length));
                        edFname.setText("");
                    }*/
                    else {

                    }
                    edFname.setText(strFirstName);
                }
            }
        });

        edLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     edMerchantName.setSelection(edMerchantName.getText().length());   //set cursor at right placee of text
                if (Validation.isStringNullOrBlank(edLname.getText().toString())) {
                    edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!(edLname.getText().toString().matches("^[a-zA-Z.'_\\s]*$"))) {
                    edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edLname.getText().length() > 19) {
                    if (edLname.isEnabled()) {
                        ToastPopUp.show(getContext(), getString(R.string.length_error_2));
                    }
                }
            }
        });

        edLname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strLastName = edLname.getText().toString().trim();
                if (!hasFocus) {
                    if (strLastName.length() > 0) {
                        if (!(strLastName.matches("^[a-zA-Z.'_\\s]*$"))) {
                            edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            ToastPopUp.show(getActivity(), getString(R.string.validation_first_name_special_characters_merchant));
                        } else if (!(edLname.getText().toString().matches("\\w*"))) {
//                            edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                            ToastPopUp.show(getContext(), getString(R.string.spaces_not_allowed));
                            strLastName = strLastName.replaceAll("\\s{2,}", " ");
                        } else if ((edLname.getText().toString().length() < 1) || (edLname.getText().toString().length() > 20)) {
                            edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            ToastPopUp.show(getContext(), getString(R.string.min_length));
                        } /*else if ((edLname.getText().toString().length() > 20)) {
                            edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                            ToastPopUp.show(context, getString(R.string.max_length));
                            edLname.setText("");
                        } */ else {

                        }
                        edLname.setText(strLastName);
                    }
                }
            }
        });

        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     edMerchantName.setSelection(edMerchantName.getText().length());   //set cursor at right placee of text

                if (Validation.isStringNullOrBlank(edFname.getText().toString())) {


                    edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else if (!(edAddress.getText().toString().matches("^[a-zA-Z.'_\\s]*$"))) {
                    edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strAddress = edAddress.getText().toString();
                if (strAddress.length() > 0) {
                    if (Validation.isStringNullOrBlank(edAddress.getText().toString())) {
                        ToastPopUp.show(getContext(), getString(R.string.Enter_Address));
                        edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (!(strAddress.matches(".*[a-zA-Z]+.*"))) {
                        ToastPopUp.displayToast(getContext(), getResources().getString(R.string.address_toast));
                    }
                }
            }
        });

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     edMobileno.setSelection(edMobileno.getText().length());   //set cursor at right placee of text

                if (Validation.isStringNullOrBlank(edPhone.getText().toString())) {
                    edPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else {
                    //   edPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 10) {
                    edPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (edPhone.getText().toString().contains(" ")) {
                    edPhone.setText(edPhone.getText().toString().replaceAll(" ", ""));
                    edPhone.setSelection(edPhone.getText().length());
                }
            }
        });

        edPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));

                    } else if (Validation.isStringNullOrBlank(edPhone.getText().toString())) {
                        ToastPopUp.show(getContext(), getString(R.string.Enter_Mobileno));
                        edPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                    }/* else if (!(Validation.validatePhoneNumber(edPhone.getText().toString()))) {

                        ToastPopUp.show(getContext(), getString(R.string.Enter_validMobileno));
                        edPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edPhone.setText("");

                    }*/ else if (!Validation.isStringNullOrBlank(strmobile) && !strmobile.equals(edPhone.getText().toString())) {


                        checkmobileno(1);


                    }


                }
            }
        });


        stredgender = strgender;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //-------------------------------------------------------------------------------
                if (checkedId == R.id.edmyprofileradiomale) {
                    stredgender = "Male";
                } else if (checkedId == R.id.edmyprofileradiofemale) {
                    stredgender = "Female";
                }
            }
        });

        edpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edpassword.getText().length() > 19) {
                    if (edpassword.isEnabled()) {
                        ToastPopUp.show(getContext(), "Length cannot be grater then 20 characters");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strPassword = edpassword.getText().toString();
                if (!hasFocus) {
                    if (Validation.isStringNullOrBlank(edpassword.getText().toString())) {
                        ToastPopUp.displayToast(getActivity(), getString(R.string.Password_blank));
                    } else if (strPassword.length() < 6 || strPassword.length() > 20) {
                        ToastPopUp.displayToast(getActivity(), getString(R.string.Password_length));
                    }

                    // Refer GAD-3_Priority_2_SRS_version32_21May2019.doc Point no.15.2 Change Request
                    /*else if (!(strPassword.matches(".*[a-zA-Z]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast_1));
                        edpassword.setText("");
                    } else if (!(strPassword.matches(".*[!@#$%^&*()]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast_2));
                        edpassword.setText("");
                    } else if (!(strPassword.matches(".*[0-9]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast_3));
                        edpassword.setText("");
                    }*/
                }
            }
        });

        strprivacy = "Public";
        radiogrpprivacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.edmyprofileradiopublic) {
                    strprivacy = "Public";
                } else if (checkedId == R.id.edmyprofileradioanonymous) {
                    strprivacy = "Anonymous";
                }
            }
        });

        edCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countries = (ArrayList<SignupPOJO>) countri.clone();
                countries.size();
                selectCountry();
            }
        });
        edState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                states = (ArrayList<SignupPOJO>) stat.clone();
                selectState();
            }
        });
        edCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateid != null) {
                    cities = (ArrayList<SignupPOJO>) citi.clone();
                    selectCity();
                } else {
                    Toast.makeText(getContext(), "Select state", Toast.LENGTH_LONG).show();
                }
            }
        });

        TaggedneedsActivity.saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                } else {
                    checkvalidations();
                }
            }
        });

       /* btnbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkvalidations();


            }
        });*/

//-------------------------------Back button press--------------------------------------------------

        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
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
        if (type == 100) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageFileName + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

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

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                // camera clicked image
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(fileUri), null, options);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                }
                strimagePath = file.getAbsolutePath();
                profilePic.setImageBitmap(bitmap);
                profilePic.setScaleType(ImageView.ScaleType.FIT_XY);
                strAvatarPath = "";
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == 200 && resultCode == RESULT_OK && imageReturnedIntent != null) {
            // Get the Image from gallery data
            fileUri = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(fileUri,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            strimagePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = decodeSampledBitmapFromFile(strimagePath, 512, 512);
            // bitmap = BitmapFactory.decodeFile(imgDecodableString);
            // Set the Image in ImageView after decoding the String
            profilePic.setImageBitmap(bitmap);
            profilePic.setScaleType(ImageView.ScaleType.FIT_XY);
            strAvatarPath = "";
        } else if (requestCode == 300) {
            // avatar stored in firebase database
            if (resultCode == RESULT_CANCELED) {

            } else {
                strAvatarPath = imageReturnedIntent.getStringExtra("url");
                Log.d("clicked_url_path", "" + strAvatarPath);
                Glide.with(getContext()).load(strAvatarPath).into(profilePic);
            }
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
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    //-------------------------Initilizing UI veriables-------------------------------------------------
    private void init() {
        profilePic = (CircleImageView) rootview.findViewById(R.id.profile_pic);
        changeProfilePic = (Button) rootview.findViewById(R.id.btn_change_pic);
        txtName = (TextView) rootview.findViewById(R.id.myprofiletxtname);
        txtCredits = (TextView) rootview.findViewById(R.id.myprofiletxtcredits);
        txtPoints = (TextView) rootview.findViewById(R.id.myprofiletxtpoints);
        txtGender = (TextView) rootview.findViewById(R.id.txtmyprofilegender);
        edFname = (EditText) rootview.findViewById(R.id.edmyprofilefname);
        edLname = (EditText) rootview.findViewById(R.id.edmyprofilelname);
        edEmail = (EditText) rootview.findViewById(R.id.edmyprofileemail);
        edPhone = (EditText) rootview.findViewById(R.id.edmyprofilePhone);
        edAddress = (EditText) rootview.findViewById(R.id.edmyprofileAddress);
        edCountry = (EditText) rootview.findViewById(R.id.edmyprofilecountry);
        edState = (EditText) rootview.findViewById(R.id.edmyprofilestate);
        edCity = (EditText) rootview.findViewById(R.id.edmyprofilecity);
        edpassword = (EditText) rootview.findViewById(R.id.edmyprofilepassword);
        txtinputlayoutmyprofilepassword = (TextInputLayout) rootview.findViewById(R.id.txtinputlayoutmyprofilepassword);
        // btnbtnSave = (Button) rootview.findViewById(R.id.btnmyprofileprofilesave);
        rbtnFemale = (RadioButton) rootview.findViewById(R.id.edmyprofileradiofemale);
        rbtnMale = (RadioButton) rootview.findViewById(R.id.edmyprofileradiomale);
        rbtnpublic = (RadioButton) rootview.findViewById(R.id.edmyprofileradiopublic);
        rbtnanonymous = (RadioButton) rootview.findViewById(R.id.edmyprofileradioanonymous);
        //   imgEditProfile = (ImageView) rootview.findViewById(R.id.imgeditprofile);
        imgShare = (ImageView) rootview.findViewById(R.id.myprofileimgshare);
        radioGroup = (RadioGroup) rootview.findViewById(R.id.edmyprofileradiogrpgender);
        radiogrpprivacy = (RadioGroup) rootview.findViewById(R.id.edmyprofileradiogrpprivecy);
        txtName.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        txtCredits.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        txtPoints.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        txtGender.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edFname.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edLname.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edEmail.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edPhone.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edAddress.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edCountry.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edState.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edCity.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        edpassword.setTypeface(new FontDetails(getContext()).fontStandardForPage);
        // btnbtnSave.setTypeface(new FontDetails(getContext()).fontStandardForPage);
    }

    //--------------------------getting profile details from server and setting values------------------
    public void getProfileDetails() {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        MyProfileInterface service = retrofit.create(MyProfileInterface.class);
        Call<FetchProfileData> call = service.sendData(strUserId);
        call.enqueue(new Callback<FetchProfileData>() {
            @Override
            public void onResponse(Response<FetchProfileData> response, Retrofit retrofit) {
                FetchProfileData fdata = response.body();
                Log.d("userid", strUserId);
                //Log.d("fname", response.body().getProfiledata().get(0).getFname());
                String login_type = "";
                try {
                    FetchProfileData deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        if (fdata.getProfiledata().get(0).getFname() != "") {
                            strfname = fdata.getProfiledata().get(0).getFname();
                            strLastName = response.body().getProfiledata().get(0).getLname();
                            stremail = response.body().getProfiledata().get(0).getEmail();
                            strMobileno = response.body().getProfiledata().get(0).getMobile();
                            strAddress = response.body().getProfiledata().get(0).getAddress();
                            contryid = response.body().getProfiledata().get(0).getCountryID();
                            stateid = response.body().getProfiledata().get(0).getStateID();
                            cityid = response.body().getProfiledata().get(0).getCityID();
                            strPassword = response.body().getProfiledata().get(0).getPassword();
                            int points = response.body().getProfiledata().get(0).getTotalPoints();
                            strprivacy = response.body().getProfiledata().get(0).getPrivacy();
                            strCreditpoints = String.valueOf(points);
                            stredgender = response.body().getProfiledata().get(0).getGender();
                            login_type = response.body().getProfiledata().get(0).getLoginType();
                            strmobile = strMobileno;
                            //if(strPassword)
                        }
                        if (login_type.equals("app")) {
                            txtinputlayoutmyprofilepassword.setVisibility(View.VISIBLE);
                            edpassword.setVisibility(View.VISIBLE);
                        } else {
                            txtinputlayoutmyprofilepassword.setVisibility(View.GONE);
                            edpassword.setVisibility(View.GONE);
                        }
                        edFname.setText(strfname);
                        edLname.setText(strLastName);
                        edAddress.setText(strAddress);
                        edpassword.setText(strPassword);
                        edEmail.setText(stremail);
                        edPhone.setText(strMobileno);
                        txtPoints.setText(strCreditpoints);
                        if (stredgender.equals("Male")) {
                            rbtnMale.setChecked(true);
                        } else if (stredgender.equals("")) {
                            rbtnMale.setChecked(false);
                            rbtnFemale.setChecked(false);
                        } else {
                            rbtnFemale.setChecked(true);
                        }
                        if (strprivacy.equals("Public")) {
                            rbtnpublic.setChecked(true);
                        } else {
                            rbtnanonymous.setChecked(true);
                        }
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
                getcountry();
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public void getcountry() {
        //countries = new ArrayList<>();
        /*mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);*/
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CountrySignup service = retrofit.create(CountrySignup.class);

        Call<CountryModel> call = service.sendData("country");
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Response<CountryModel> response, Retrofit retrofit) {
                //response.code();
                CountryModel countryData = response.body();
                try {
                    if (response.body().getCountrydata().size() > 0) {
                        for (int j = 0; j < countryData.getCountrydata().size(); j++) {
                            // countries.add(countryData.getCountrydata().get(j).getCntryName().toString());
                            SignupPOJO data = new SignupPOJO();
                            data.setId(countryData.getCountrydata().get(j).getCntryID().toString());
                            data.setName(countryData.getCountrydata().get(j).getCntryName().toString());
                            countries.add(data);
                        }
                        Collections.sort(countries, new Comparator<SignupPOJO>() {
                            @Override
                            public int compare(SignupPOJO o1, SignupPOJO o2) {
                                return o1.getName().compareTo(o2.getName());
                            }


                        });
                        countri = (ArrayList<SignupPOJO>) countries.clone();
                        //setting  country


                        for (int i = 0; i < countries.size(); i++) {
                            String selected_id = countries.get(i).getId();
                            if (selected_id.equals(contryid)) {
                                contryid = selected_id;
                                strCountry = countries.get(i).getName();
                                edCountry.setText(strCountry);
//                                mDialog.dismiss();
                            }

                        }


                        getstate(contryid);
                        // mDialog.dismiss();

                    }
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
//                mDialog.dismiss();
            }
        });
    }

    public void getstate(String cntryid) {
        states = new ArrayList<>();
        /*mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);*/
        states.clear();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        StateSignup service = retrofit.create(StateSignup.class);

        Call<StateModel> call = service.sendData(cntryid);
        call.enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(Response<StateModel> response, Retrofit retrofit) {
                //response.code();
                StateModel countryData = response.body();
                try {
                    for (int j = 0; j < countryData.getStatedata().size(); j++) {
                        //countries.add(countryData.getStatedata().get(j).getStateName().toString());
                        SignupPOJO data = new SignupPOJO();
                        data.setId(countryData.getStatedata().get(j).getStateID().toString());
                        data.setName(countryData.getStatedata().get(j).getStateName().toString());
                        states.add(data);
                    }

                    for (int i = 0; i < states.size(); i++) {
                        String selected_id = states.get(i).getId();
                        if (selected_id.equals(stateid)) {
                            stateid = selected_id;
                            String selected_name = states.get(i).getName();
                            edState.setText(selected_name);

                        }
                    }
                    stat = (ArrayList<SignupPOJO>) states.clone();
                    getcity(stateid);
                    // mDialog.dismiss();
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
//                mDialog.dismiss();
            }
        });
    }

    public void getcity(String stateid) {
        cities = new ArrayList<>();
        /*mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);*/
        cities.clear();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CitySignup service = retrofit.create(CitySignup.class);

        Call<CityModel> call = service.sendData(stateid);
        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Response<CityModel> response, Retrofit retrofit) {
                //response.code();
                CityModel countryData = response.body();
                try {
                    for (int j = 0; j < countryData.getCitydata().size(); j++) {
                        //countries.add(countryData.getStatedata().get(j).getStateName().toString());
                        SignupPOJO data = new SignupPOJO();
                        data.setId(countryData.getCitydata().get(j).getCityID().toString());
                        data.setName(countryData.getCitydata().get(j).getCityName().toString());
                        cities.add(data);
                    }
                    for (int i = 0; i < cities.size(); i++) {
                        String selected_id = cities.get(i).getId();
                        if (selected_id.equals(cityid)) {
                            cityid = selected_id;
                            String selected_name = cities.get(i).getName();
                            edCity.setText(selected_name);

                            if (editflag == 1) {
                                changeProfilePic.setVisibility(View.VISIBLE);
                                enabletext(true);
                                TaggedneedsActivity.editprofile.setVisibility(View.GONE);
                                TaggedneedsActivity.saveprofile.setVisibility(View.VISIBLE);
                            } else {
                                changeProfilePic.setVisibility(View.GONE);
                                enabletext(false);
                                TaggedneedsActivity.editprofile.setVisibility(View.VISIBLE);
                                TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
                            }
                            // getcity(strstste_id);
                        }
                    }
                    citi = (ArrayList<SignupPOJO>) cities.clone();
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
//                mDialog.dismiss();

            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
//                mDialog.dismiss();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void selectCountry() {
        //getcountry();
        if (!(Validation.isNetworkAvailable(myContext))) {
            Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (countries.size() <= 0) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);

                    ctryadptr = new CountryAdapter(countries, getContext());
                    categorylist.setAdapter(ctryadptr);
                    setDynamicHeight(categorylist);
                    //------------search
                    txtsearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                            if (txtsearch.getText().toString().contains(" ")) {
                                txtsearch.setText(txtsearch.getText().toString().replaceAll(" ", ""));
                                txtsearch.setSelection(txtsearch.getText().length());
                            }
                            String text = txtsearch.getText().toString().toLowerCase(Locale.getDefault());
                            ctryadptr.filter(text);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1,
                                                      int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                            // TODO Auto-generated method stub
                        }
                    });
                    //-------------------
//---------------clear search text------------------------------------------------------------------
                    txtsearch.setOnTouchListener(new View.OnTouchListener() {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                int leftEdgeOfRightDrawable = txtsearch.getRight()
                                        - txtsearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                                // when EditBox has padding, adjust leftEdge like
                                // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                                if (event.getRawX() >= leftEdgeOfRightDrawable) {
                                    // clicked on clear icon
                                    txtsearch.setText("");
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    //-------------------------------------------------------

                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //  category_id = categories.get(i).getCategory_id();
                            edCountry.setText(countries.get(i).getName());
                            strCountry = edCountry.getText().toString();
                            contryid = countries.get(i).getId();
                            edState.setText("");
                            stateid = null;
                            cityid = null;
                            edCity.setText("");
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            getstate(contryid);
                            dialog.dismiss();
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
//                StringWriter writer = new StringWriter();
//                e.printStackTrace(new PrintWriter(writer));
//                Bugreport bg = new Bugreport();
//                bg.sendbug(writer.toString());
            }
        }
    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    public void selectState() {
        if (!(Validation.isNetworkAvailable(myContext))) {
            Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (states.size() <= 0) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                    stateadptr = new StateAdapter(states, getContext());
                    categorylist.setAdapter(stateadptr);
                    setDynamicHeight(categorylist);
                    //------------search
                    txtsearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                            if (txtsearch.getText().toString().contains(" ")) {
                                txtsearch.setText(txtsearch.getText().toString().replaceAll(" ", ""));
                                txtsearch.setSelection(txtsearch.getText().length());
                            }
                            String text = txtsearch.getText().toString().toLowerCase(Locale.getDefault());
                            stateadptr.filter(text);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1,
                                                      int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                            // TODO Auto-generated method stub
                        }
                    });
                    //-------------------
//---------------clear search text------------------------------------------------------------------
                    txtsearch.setOnTouchListener(new View.OnTouchListener() {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                int leftEdgeOfRightDrawable = txtsearch.getRight()
                                        - txtsearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                                // when EditBox has padding, adjust leftEdge like
                                // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                                if (event.getRawX() >= leftEdgeOfRightDrawable) {
                                    // clicked on clear icon
                                    txtsearch.setText("");
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    //-------------------------------------------------------
                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //  category_id = categories.get(i).getCategory_id();
                            edState.setText(states.get(i).getName());
                            stateid = states.get(i).getId();
                            strState = edState.getText().toString();
                            edCity.setText("");
                            cityid = null;
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            getcity(stateid);
                            dialog.dismiss();
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
//                StringWriter writer = new StringWriter();
//                e.printStackTrace(new PrintWriter(writer));
//                Bugreport bg = new Bugreport();
//                bg.sendbug(writer.toString());
            }
        }
    }

    public void selectCity() {
        if (!(Validation.isNetworkAvailable(myContext))) {
            Toast.makeText(myContext, getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
        } else {
            if (cities.size() <= 0) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                    cityadptr = new CityAdapter(cities, getContext());
                    categorylist.setAdapter(cityadptr);
                    setDynamicHeight(categorylist);
                    //------------search
                    txtsearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub
                            if (txtsearch.getText().toString().contains(" ")) {
                                txtsearch.setText(txtsearch.getText().toString().replaceAll(" ", ""));
                                txtsearch.setSelection(txtsearch.getText().length());
                            }
                            String text = txtsearch.getText().toString().toLowerCase(Locale.getDefault());
                            cityadptr.filter(text);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1,
                                                      int arg2, int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                            // TODO Auto-generated method stub
                        }
                    });
                    //-------------------
//---------------clear search text------------------------------------------------------------------
                    txtsearch.setOnTouchListener(new View.OnTouchListener() {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                int leftEdgeOfRightDrawable = txtsearch.getRight()
                                        - txtsearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                                // when EditBox has padding, adjust leftEdge like
                                // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                                if (event.getRawX() >= leftEdgeOfRightDrawable) {
                                    // clicked on clear icon
                                    txtsearch.setText("");
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    //-------------------------------------------------------
                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //  category_id = categories.get(i).getCategory_id();
                            edCity.setText(cities.get(i).getName());
                            strCity = edCity.getText().toString();
                            cityid = cities.get(i).getId();
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

                    dialog.show();
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }
        }
    }

    public void enabletext(boolean enabletxt) {
        edFname.setEnabled(enabletxt);
        edLname.setEnabled(enabletxt);
        edPhone.setEnabled(enabletxt);
        edAddress.setEnabled(enabletxt);
        edCountry.setEnabled(enabletxt);
        edCity.setEnabled(enabletxt);
        edState.setEnabled(enabletxt);
        edpassword.setEnabled(enabletxt);
        txtinputlayoutmyprofilepassword.setPasswordVisibilityToggleEnabled(enabletxt);
        /*if (enabletxt)
        {
            edFname.requestFocus();
        }*/

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enabletxt);
        }
        for (int i = 0; i < radiogrpprivacy.getChildCount(); i++) {
            radiogrpprivacy.getChildAt(i).setEnabled(enabletxt);
        }
    }

    public void checkmobileno(final int chekmobile) {
        strMobileno = edPhone.getText().toString();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MobilecheckInterface service = retrofit.create(MobilecheckInterface.class);
        Call<MobileModel> call = service.sendData(strMobileno);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                try {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    MobileModel result = new MobileModel();
                    String successstatus = response.body().getCheckstatus().get(0).getStatus();

                    System.out.println("successstatus" + successstatus);
                    Log.d("successstatus ", successstatus);

                    if (successstatus.equals("1")) {
                        Toast.makeText(getActivity(), "Mobile Number Already Exists", Toast.LENGTH_LONG).show();
                        edPhone.setText("");
                    } else {
                        if (chekmobile == 0) {
                            if (!(Validation.isNetworkAvailable(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                                        strAddress, contryid, stateid, cityid, stredgender, strPassword, strprivacy);
                            }
                        }
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
// we use this methiod in this place to for some situation when during progress dialog is running and that time
                //netowrk goes off then dialog continue running so due to this validation dialog is dismiss.
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                // mDialog.dismiss();
            }
        });

    }

    //----------------------------updating profile to server--------------------------------------------
    public void updatedata(String U_Id, String Fname, String Lname, String email, String Mobile, String add, String con_Id, String Stat_Id, String CIt_Id, String gen, String Pass, final String privacy) {
        final String fname = Fname;
        final String lname = Lname;
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        UpdateProfile service = retrofit.create(UpdateProfile.class);
        Call<MobileModel> call = service.sendData(U_Id, Fname.trim(), Lname.trim(), email.trim(), Mobile.trim(), add.trim(), con_Id, Stat_Id, CIt_Id, gen, Pass, privacy);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                MobileModel result = new MobileModel();
                try {
                    editflag = 0;
                    MobileModel deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sharedPrefManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/
                        sharedPrefManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginintent);
                    } else {
                        String successstatus = response.body().getCheckstatus().get(0).getStatus();
                        if (successstatus.equals("1")) {
                            strmobile = "";
                            String strFullName = fname + " " + lname;
                            ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.profile_updated));
                            sharedPrefManager.createUserCredentialSession(strUserId, strFullName, privacy);

                            // update user full name in SendBird
                            DisconnectSendbirdCall();
                            String image_path = sharedPrefManager.getProfileImagePath();
                            if (image_path != null) {
                                loginWithSendbirdchat(strUserId, strFullName, image_path);
                            } else {
                                loginWithSendbirdchat(strUserId, strFullName, "");
                            }

                            HashMap<String, String> user = sharedPrefManager.getUserDetails();
                            strUserId = user.get(sharedPrefManager.USER_ID);
                            strProfilrName = user.get(sharedPrefManager.USER_NAME);
                            txtName.setText(strProfilrName);
                            //firebase call for image store with sos id
                            if (bitmap != null) {
                                if (strAvatarPath.length() > 0) {
                                    // selected image from avatar
                                    Profile profile = new Profile(strUserId, strAvatarPath);
                                    mFirebaseDatabase.child("profile").child(strUserId).setValue(profile);
                                } else {
                                    // selected image from camera or gallery
                                    uploadFile();
                                }
                            } else {
                                if (strAvatarPath.length() > 0) {
                                    // selected image from avatar
                                    Profile profile = new Profile(strUserId, strAvatarPath);
                                    mFirebaseDatabase.child("profile").child(strUserId).setValue(profile);
                                }
                            }

                            if (!(strPassword.equals(strnewPassword))) {
                                sharedPrefManager.createUserCredentialSession(null, null, null);
                                Intent loginintent = new Intent(getContext(), LoginActivity.class);
                                startActivity(loginintent);
                            } else {
                                //edPhone.requestFocus();
                                int i = 1;
                                fragmgr = getFragmentManager();
                                // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).addToBackStack(null).commit();
                            }
                        } else {
                            ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.updation_failed));
                        }
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    private void uploadFile() {
        //checking if file is available
        if (fileUri != null) {
            //displaying progress dialog while image is uploading
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child(WebServices.PROFILEPIC_STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg");
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
                            //creating the upload object to store uploaded image details
                            Profile profile = new Profile(strUserId, photoStringLink);
                            mFirebaseDatabase.child("profile").child(strUserId).setValue(profile);
                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
            /*//adding the file to reference
            sRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
//                            progressDialog.dismiss();

                            //displaying success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            Profile profile = new Profile(strUserId, taskSnapshot.getDownloadUrl().toString());
                            mFirebaseDatabase.child("profile").child(strUserId).setValue(profile);
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
                    });*/
        } else {
            //display an error if no file is selected
        }
    }

    private void updateCurrentUserInfo(final String userNickname, String photoPath) {
        Log.d("PHOTOSNADBIRD", "uner method :  " + photoPath);
        if (photoPath.equalsIgnoreCase("")) {
            SendBird.updateCurrentUserInfo(userNickname, Utility.avatorDefaultIcon, new SendBird.UserInfoUpdateHandler() {
                @Override
                public void onUpdated(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        Log.d("SB_update_error", "" + e.getCode() + ":" + e.getMessage());
                        return;
                    }
                    Log.d("TAG", "NickName189" + userNickname);
                    PreferenceUtils.setNickname(getContext(), userNickname);
                }
            });
        } else {
            SendBird.updateCurrentUserInfo(userNickname, photoPath, new SendBird.UserInfoUpdateHandler() {
                @Override
                public void onUpdated(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        Log.d("SB_update_error", "" + e.getCode() + ":" + e.getMessage());
                        // Show update failed snackbar
//                        ToastPopUp.displayToast(TaggedneedsActivity.this, "Update user nickname failed");
                        return;
                    }
                    Log.d("TAG", "NickName189" + userNickname);
                    PreferenceUtils.setNickname(getContext(), userNickname);
                }
            });
        }
    }

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------checking validations--------------------------------------------------------
    public void checkvalidations() {
        strFirstName = edFname.getText().toString().trim();
        strLastName = edLname.getText().toString().trim();
        strMobileno = edPhone.getText().toString().trim();
        strAddress = edAddress.getText().toString();
        strCountry = edCountry.getText().toString();
        strState = edState.getText().toString();
        strCity = edCity.getText().toString();
        // strgender = stredgender;
        strnewPassword = edpassword.getText().toString().trim();

        if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
            ToastPopUp.show(getContext(), getString(R.string.Enter_FirstName));
            edFname.requestFocus();
        } else if (!(strFirstName.matches("^[a-zA-Z.'_\\s]*$"))) {
            edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ToastPopUp.show(getActivity(), getString(R.string.validation_first_name_special_characters_merchant) + " in first name");
        } else if (strLastName.length() > 0) {
            if (!(strLastName.matches("^[a-zA-Z.'_\\s]*$"))) {
                edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ToastPopUp.show(getActivity(), getString(R.string.validation_first_name_special_characters_merchant) + " in last name");
                edLname.requestFocus();
            } else if ((edLname.getText().toString().length() < 1) || (edLname.getText().toString().length() > 20)) {
                ToastPopUp.show(getContext(), getString(R.string.min_length));
                edLname.requestFocus();
            } else if ((edFname.getText().toString().length() < 3) || (edFname.getText().toString().length() > 15)) {
                edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ToastPopUp.show(getContext(), getString(R.string.min_length));
            } else if (!(Validation.isNetworkAvailable(getActivity()))) {
                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
            } else if (strAddress.length() > 0) {
                if (Validation.isStringNullOrBlank(edAddress.getText().toString())) {
                    ToastPopUp.show(getContext(), getString(R.string.Enter_Address));
                    edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!(strAddress.matches(".*[a-zA-Z]+.*"))) {
                    ToastPopUp.displayToast(getContext(), getResources().getString(R.string.address_toast));
                } else if (strCountry.length() < 1) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_country));
                } else if (strState.length() < 1) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_state));
                } else if (strCity.length() < 1) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_city));
                } else if (edpassword.getVisibility() == View.VISIBLE) {
                    if (Validation.isStringNullOrBlank(edpassword.getText().toString())) {
                        ToastPopUp.displayToast(getActivity(), getString(R.string.Password_blank));
                    } else if (strnewPassword.length() < 6 || strnewPassword.length() > 20) {
                        ToastPopUp.displayToast(getActivity(), getString(R.string.Password_length));
                    }

                    // Refer GAD-3_Priority_2_SRS_version32_21May2019.doc Point no.15.2 Change Request
                    /*else if (!(strnewPassword.matches(".*[a-zA-Z]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                        edpassword.setText("");
                    } else if (!(strnewPassword.matches(".*[!@#$%^&*()]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                        edpassword.setText("");

                    } else if (!(strnewPassword.matches(".*[0-9]+.*"))) {
                        ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                        edpassword.setText("");
                    } */
                    else {
                        if (!(Validation.isNetworkAvailable(getActivity()))) {
                            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                        } else {
                            updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                                    strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                        }
                    }
                } else {
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                                strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                    }
                }
            } else if (strCountry.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_country));
            } else if (strState.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_state));
            } else if (strCity.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_city));
            } else if (edpassword.getVisibility() == View.VISIBLE) {
                if (Validation.isStringNullOrBlank(edpassword.getText().toString())) {
                    ToastPopUp.displayToast(getActivity(), getString(R.string.Password_blank));
                } else if (strnewPassword.length() < 6 || strnewPassword.length() > 20) {
                    ToastPopUp.displayToast(getActivity(), getString(R.string.Password_length));
                }

                // Refer GAD-3_Priority_2_SRS_version32_21May2019.doc Point no.15.2 Change Request
                /*else if (!(strnewPassword.matches(".*[a-zA-Z]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");
                } else if (!(strnewPassword.matches(".*[!@#$%^&*()]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");

                } else if (!(strnewPassword.matches(".*[0-9]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");
                } */

                else {
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                                strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                    }
                }
            } else {
                if (!(Validation.isNetworkAvailable(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                            strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                }
            }

        } else if ((edFname.getText().toString().length() < 3) || (edFname.getText().toString().length() > 15)) {
            edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ToastPopUp.show(getContext(), getString(R.string.min_length));
        } else if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));

        } else if (strAddress.length() > 0) {
            if (Validation.isStringNullOrBlank(edAddress.getText().toString())) {
                ToastPopUp.show(getContext(), getString(R.string.Enter_Address));
                edAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else if (!(strAddress.matches(".*[a-zA-Z]+.*"))) {
                ToastPopUp.displayToast(getContext(), getResources().getString(R.string.address_toast));
            } else if ((edFname.getText().toString().length() < 3) || (edFname.getText().toString().length() > 15)) {
                edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ToastPopUp.show(getContext(), getString(R.string.min_length));
            } else if (!(Validation.isNetworkAvailable(getActivity()))) {
                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
            } else if (strCountry.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_country));
            } else if (strState.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_state));
            } else if (strCity.length() < 1) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_city));
            } else if (edpassword.getVisibility() == View.VISIBLE) {
                if (Validation.isStringNullOrBlank(edpassword.getText().toString())) {
                    ToastPopUp.displayToast(getActivity(), getString(R.string.Password_blank));
                } else if (strnewPassword.length() < 6 || strnewPassword.length() > 20) {
                    ToastPopUp.displayToast(getActivity(), getString(R.string.Password_length));
                }

                // Refer GAD-3_Priority_2_SRS_version32_21May2019.doc Point no.15.2 Change Request
                /*else if (!(strnewPassword.matches(".*[a-zA-Z]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");
                } else if (!(strnewPassword.matches(".*[!@#$%^&*()]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");

                } else if (!(strnewPassword.matches(".*[0-9]+.*"))) {
                    ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                    edpassword.setText("");
                }*/

                else {
                    if (!(Validation.isNetworkAvailable(getActivity()))) {
                        ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                    } else {
                        updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                                strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                    }
                }
            } else {
                if (!(Validation.isNetworkAvailable(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                            strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                }
            }
        } else if (strCountry.length() < 1) {
            ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_country));
        } else if (strState.length() < 1) {
            ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_state));
        } else if (strCity.length() < 1) {
            ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.select_city));
        } else if (edpassword.getVisibility() == View.VISIBLE) {
            if (Validation.isStringNullOrBlank(edpassword.getText().toString())) {
                ToastPopUp.displayToast(getActivity(), getString(R.string.Password_blank));
            } else if (strnewPassword.length() < 6 || strnewPassword.length() > 20) {
                ToastPopUp.displayToast(getActivity(), getString(R.string.Password_length));
            }

            // Refer GAD-3_Priority_2_SRS_version32_21May2019.doc Point no.15.2 Change Request
            /*else if (!(strnewPassword.matches(".*[a-zA-Z]+.*"))) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                edpassword.setText("");
            } else if (!(strnewPassword.matches(".*[!@#$%^&*()]+.*"))) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                edpassword.setText("");

            } else if (!(strnewPassword.matches(".*[0-9]+.*"))) {
                ToastPopUp.displayToast(getActivity(), getResources().getString(R.string.password_toast));
                edpassword.setText("");
            }*/

            else {
                if (!(Validation.isNetworkAvailable(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                            strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
                }
            }
        } else {
            if (!(Validation.isNetworkAvailable(getActivity()))) {
                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
            } else {
                updatedata(strUserId, strFirstName, strLastName, stremail, strMobileno,
                        strAddress, contryid, stateid, cityid, stredgender, strnewPassword, strprivacy);
            }
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
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }



    public void loginWithSendbirdchat(String strUserId, String strUserName, String strPhotoPath) {
        if (strUserId != null && strUserName != null) {
            strUserId = strUserId.replaceAll("\\s", "");
            // String userNickname = mUserNicknameEditText.getText().toString();
            PreferenceUtils.setUserId(getContext(), strUserId);
            PreferenceUtils.setNickname(getContext(), strUserName);
            connectToSendBird(strUserId, strUserName, strPhotoPath);
        } else {
//            Toast.makeText(getContext(), "UnAuthorized Access", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     *
     * @param userId       The unique ID of the user.
     * @param userNickname The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname, final String strPhotoUrl) {
        // Show the loading indicator
        // showProgressBar(true);
        // mConnectButton.setEnabled(false);
        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                if (e != null) {
                    // Error!
                    Log.d("sendbird connect error", "" + e.getCode() + ": " + e.getMessage());
                    /*Toast.makeText(
                            getContext(), "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();*/

                    // Show login failure snackbar
//                    ToastPopUp.displayToast(getContext(),"Login to SendBird failed");
                    //     mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(getContext(), false);
                    return;
                }
                Log.d("PHOTOSNADBIRD", "vv  " + user.getProfileUrl());
                PreferenceUtils.setUserId(getContext(), user.getUserId());
                PreferenceUtils.setNickname(getContext(), user.getNickname());
                PreferenceUtils.setProfileUrl(getContext(), user.getProfileUrl());
                PreferenceUtils.setConnected(getContext(), true);
                PreferenceUtils.setGroupChannelDistinct(getContext(), false);   //set the group chat false to allow create many group by single users
                // Update the user's nickname
                updateCurrentUserInfo(userNickname, strPhotoUrl);
                updateCurrentUserPushToken();
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(getContext(), null);
    }

    public void DisconnectSendbirdCall() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    // Don't return because we still need to disconnect.
                } else {
//                    Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT).show();
                    ConnectionManager.logout(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                            PreferenceUtils.setConnected(getContext(), false);
                            //  finish();
                        }
                    });
                }
            }
        });
    }
}
