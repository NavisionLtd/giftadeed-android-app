/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Login;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning.SOSOptionActivity;
import giftadeed.kshantechsoft.com.giftadeed.FirstLogin.First_Login;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.StatusModel;
import giftadeed.kshantechsoft.com.giftadeed.PrivacyPolicy.Privacy_policy;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignUp;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SharedPrefManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.termsandconditions.Terms_Conditions;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails.context;


////////////////////////////////////////////////////////////////////
//                                                               //
//     Used to Log into app                                     //
/////////////////////////////////////////////////////////////////
public class LoginActivity extends AppCompatActivity {
    private TextView txtagreet_C, txtagreePolicy;
    ImageView fbbtn, instagrambtn, googlebtn;
    TextView notregistered, signup, forgot, loginheading, txtloginTermsandcondn, txtagree1, txtTermsAnd, txtSelectLanguage;
    Button btnLogin, dialogconfirm, dialogcancel;
    TextInputLayout txtInpLoiginid, txtInpPasswrd;
    EditText email, password, edforgotpassEmail;
    TextView txtsaperator;
    String strEmail, strPassword;
    SharedPrefManager sharedPreferences;
    SimpleArcDialog mDialog;
    CallbackManager callbackManager;
    String id;
    AccessToken accessToken;
    public static final int RequestPermissionCode = 1;
    private static final int RC_SIGN_IN = 007;
    private AlertDialog alertDialogForgot, alertDialogreturn;
    Button btnSos;
    LinearLayout layoutSelectLanguage;
    Locale locale;
    Configuration config;
    String storedLanguage;
    DatabaseAccess databaseAccess;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        // generateHashkey();
        if (checkPermission()) {
            // Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
        //---------------google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        mDialog = new SimpleArcDialog(this);
        sharedPreferences = new SharedPrefManager(LoginActivity.this);
        storedLanguage = sharedPreferences.getLanguage();
        updateLanguage(storedLanguage);
        databaseAccess.deleteAllGroups();
        Log.d("LocalDbGroupSize", "" + databaseAccess.getAllGroups().size());

        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    // All Permissions Granted
                    Intent i = new Intent(LoginActivity.this, SOSOptionActivity.class);
                    sharedPreferences.store_sos_option1_clicked("no");
                    sharedPreferences.store_sos_option2_clicked("no");
                    sharedPreferences.store_sos_option3_clicked("no");
                    i.putExtra("callingfrom", "login");
                    startActivity(i);

                /*Intent i = new Intent(LoginActivity.this, FadeInActivity.class);
                startActivity(i);*/
                } else {
                    requestPermission();
                    Toast.makeText(LoginActivity.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
                }
            }
        });

        layoutSelectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_select_language);
                final RadioButton rbChinese = (RadioButton) dialog.findViewById(R.id.rb_chinese);
                final RadioButton rbEnglish = (RadioButton) dialog.findViewById(R.id.rb_english);
                final RadioButton rbFrench = (RadioButton) dialog.findViewById(R.id.rb_french);
                final RadioButton rbHindi = (RadioButton) dialog.findViewById(R.id.rb_hindi);
                final RadioButton rbPortuguese = (RadioButton) dialog.findViewById(R.id.rb_portuguese);
                final RadioButton rbSpanish = (RadioButton) dialog.findViewById(R.id.rb_spanish);
                RadioGroup rgLanguages = (RadioGroup) dialog.findViewById(R.id.rg_language_group);
                storedLanguage = sharedPreferences.getLanguage();
                if (storedLanguage.equals("zh")) {
                    rbChinese.setChecked(true);
                } else if (storedLanguage.equals("en")) {
                    rbEnglish.setChecked(true);
                } else if (storedLanguage.equals("fr")) {
                    rbFrench.setChecked(true);
                } else if (storedLanguage.equals("hi")) {
                    rbHindi.setChecked(true);
                } else if (storedLanguage.equals("pt")) {
                    rbPortuguese.setChecked(true);
                } else if (storedLanguage.equals("es")) {
                    rbSpanish.setChecked(true);
                }

                rgLanguages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (rbChinese.isChecked()) {
                            sharedPreferences.store_language("zh");
                            updateLanguage("zh");
                            dialog.dismiss();
                        } else if (rbEnglish.isChecked()) {
                            sharedPreferences.store_language("en");
                            updateLanguage("en");
                            dialog.dismiss();
                        } else if (rbFrench.isChecked()) {
                            sharedPreferences.store_language("fr");
                            updateLanguage("fr");
                            dialog.dismiss();
                        } else if (rbHindi.isChecked()) {
                            sharedPreferences.store_language("hi");
                            updateLanguage("hi");
                            dialog.dismiss();
                        } else if (rbPortuguese.isChecked()) {
                            sharedPreferences.store_language("pt");
                            updateLanguage("pt");
                            dialog.dismiss();
                        } else if (rbSpanish.isChecked()) {
                            sharedPreferences.store_language("es");
                            updateLanguage("es");
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        //---------------checking email validations on entering email-------------------------------
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //    edemailaddress.setSelection(edemailaddress.getText().length());   //set cursor at right placee of text
                //password.setFocusableInTouchMode(false);
                if (!(Validation.isNetworkAvailable(LoginActivity.this))) {
                    ToastPopUp.displayToast(LoginActivity.this, getResources().getString(R.string.network_validation));
                } else if (Validation.isStringNullOrBlank(email.getText().toString())) {

                } else {
                    //  password.setFocusableInTouchMode(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (email.getText().toString().contains(" ")) {
                    email.setText(email.getText().toString().replaceAll(" ", ""));
                    email.setSelection(email.getText().length());
                }
            }
        });

        if ((email.getText().toString().equals(""))) {
            email.requestFocus();
        }

        email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    if (!(Validation.isNetworkAvailable(LoginActivity.this))) {
                        ToastPopUp.show(LoginActivity.this, getString(R.string.network_validation));
                        email.setText("");
                        email.requestFocus();
                    } else if (Validation.isStringNullOrBlank(email.getText().toString())) {
                        ToastPopUp.show(context, getString(R.string.Enter_emailaddress));
                        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        ToastPopUp.show(context, getString(R.string.Enter_validemailaddress));
                        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        email.setText("");
                    }
                    if ((email.getText().toString().equals(""))) {
                        email.setText("");
                        // ToastPopUp.displayToast(SendBirdLoginActivity.this, "Password doesn't match");
                        email.requestFocus();
                    } else {
                        password.setFocusableInTouchMode(true);
                    }
                    return true;
                }
                return false;
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
                return false;
            }
        });

        txtagreet_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trmscondn = new Intent(LoginActivity.this, Terms_Conditions.class);
                startActivity(trmscondn);
            }
        });

        txtagreePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trmscondn = new Intent(LoginActivity.this, Privacy_policy.class);
                startActivity(trmscondn);
            }
        });

        //add terms and conditions
        //   customTextView(txtloginTermsandcondn, "Agree ", "Terms and Conditions", ", and ", "Privacy Policy ", "to be changed to suit global audience.");

        //add terms and conditions
        customTextView(txtloginTermsandcondn, "Agree ", "Terms and Conditions,", "\nand ", "Privacy Policy.", "");

        //-----------Clicking on login button------------------------------
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    // All Permissions Granted
                    strEmail = email.getText().toString();
                    strPassword = password.getText().toString();
                    if (Validation.isStringNullOrBlank(email.getText().toString())) {
                        ToastPopUp.show(context, getString(R.string.Enter_emailaddress));
                        email.requestFocus();
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        ToastPopUp.show(context, getString(R.string.Enter_validemailaddress));
                        email.setText("");
                        email.requestFocus();
                    } else if (Validation.isStringNullOrBlank(password.getText().toString())) {
                        ToastPopUp.show(context, getString(R.string.enter_password));
                        password.requestFocus();
                        //password.requestFocus();
                    } else {
                        login();
                    }
                } else {
                    requestPermission();
                    Toast.makeText(LoginActivity.this, "Please allow app permissions", Toast.LENGTH_LONG).show();
                }
            }
        });
        //-----------Clicking on signup text----------------------------------
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsignup = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intentsignup);
            }
        });
        //-----------Clicking on forgot password text-----------------------------------------------
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassworddialog();
            }
        });

        fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_hometown"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        id = loginResult.getAccessToken().getUserId();
                        accessToken = loginResult.getAccessToken();
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("SendBirdLogin Response", response.toString());
                                Log.e("jsonObject", "" + object);
                                long fbid = object.optLong("id");
                                String email = object.optString("email");
                                String first_name = object.optString("first_name");
                                String last_name = object.optString("last_name");
                                String lat_long = object.optString("locale");
                                String gender = object.optString("gender");

                                try {
                                    JSONArray arr = object.getJSONArray("location");
                                    for (int n = 0; n < arr.length(); n++) {
                                        JSONObject obj = arr.getJSONObject(n);
                                        final String town = obj.getString("name");
                                        Log.e("town", "" + town);
                                        // do some stuff....
                                    }
                                } catch (Exception e) {

                                }
                                Log.d("Details", fbid + "," + first_name + "," + last_name + "," + email);
                                System.out.print(fbid + "," + email + "," + first_name + "," + last_name + "," + lat_long + "," + gender);
                                facebookLogin(first_name, last_name, email, String.valueOf(fbid));
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,email,last_name,gender");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Cannot login", String.valueOf(error));
                    }
                });
            }
        });
        //-------------------------------Instagram Login
        instagrambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("firebase_token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("firebase_token", "" + token);
                        sharedPreferences.saveDeviceToken(token);
                    }
                });
    }

    //----------------------initializing UI variables-------------------------------------------------
    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutSelectLanguage = (LinearLayout) findViewById(R.id.layout_select_language_login);
        txtagreet_C = (TextView) findViewById(R.id.txtagreeT_C_1);
        txtagreePolicy = (TextView) findViewById(R.id.txtagreePolicy_1);
        btnSos = (Button) findViewById(R.id.btn_set_sos);
        notregistered = (TextView) findViewById(R.id.not_registered);
        signup = (TextView) findViewById(R.id.signuptxt);
        forgot = (TextView) findViewById(R.id.forgotpwd);
        btnLogin = (Button) findViewById(R.id.login);
        loginheading = (TextView) findViewById(R.id.loginhead);
        email = (EditText) findViewById(R.id.loginemail);
        password = (EditText) findViewById(R.id.loginpassword);
        txtInpLoiginid = (TextInputLayout) findViewById(R.id.inputlayoutemail);
        txtInpPasswrd = (TextInputLayout) findViewById(R.id.inputlayoutpassword);
        txtloginTermsandcondn = findViewById(R.id.txtloginTermsandcondn);
        txtagree1 = findViewById(R.id.txtagreeagree_1);
        txtTermsAnd = findViewById(R.id.txtagreeT_C_and);
        txtSelectLanguage = findViewById(R.id.select_language_text);
        txtsaperator = (TextView) findViewById(R.id.txtsaperator);
        fbbtn = (ImageView) findViewById(R.id.fb_btn);
        instagrambtn = (ImageView) findViewById(R.id.instagram_btn);
        googlebtn = (ImageView) findViewById(R.id.google_btn);
//---------------------------------------setting font style-------------------------
        btnLogin.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        forgot.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        notregistered.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        signup.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        loginheading.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        email.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        password.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        txtInpLoiginid.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        txtInpPasswrd.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
        txtsaperator.setTypeface(new FontDetails(LoginActivity.this).fontStandardForPage);
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        loginheading.setText(getResources().getString(R.string.login_string));
        txtInpLoiginid.setHint(getResources().getString(R.string.email_string));
        txtInpPasswrd.setHint(getResources().getString(R.string.password_string));
        btnLogin.setText(getResources().getString(R.string.login_string));
        notregistered.setText(getResources().getString(R.string.not_registered));
        signup.setText(getResources().getString(R.string.signup));
        forgot.setText(getResources().getString(R.string.forgot));
        txtsaperator.setText(getResources().getString(R.string.txtseperator));
        txtagree1.setText(getResources().getString(R.string.txtagree1));
        txtagreet_C.setText(getResources().getString(R.string.login_terms));
        txtTermsAnd.setText(getResources().getString(R.string.terms_and));
        txtagreePolicy.setText(getResources().getString(R.string.login_privacy));
        txtSelectLanguage.setText(getResources().getString(R.string.select_language));
    }

    //----------------------forgot password dialog--------------------------------------------------
    public void forgotpassworddialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        final View confirmDialog = li.inflate(R.layout.activity_forgot, null);
        dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
        dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
        edforgotpassEmail = (EditText) confirmDialog.findViewById(R.id.edforgotpassword_email);
        //-------------Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);

        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        dialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isNetworkAvailable(LoginActivity.this))) {
                    ToastPopUp.show(LoginActivity.this, getString(R.string.network_validation));

                }
                if (!Validation.isStringNullOrBlank(edforgotpassEmail.getText().toString())) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edforgotpassEmail.getText().toString().trim()).matches()) {
                        ToastPopUp.show(LoginActivity.this, getString(R.string.Enter_validemailaddress));
                        edforgotpassEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edforgotpassEmail.setText("");
                    } else if (!Validation.isStringNullOrBlank(edforgotpassEmail.getText().toString())) {
                        Forgotpassword(edforgotpassEmail.getText().toString());
                    }
                } else {
                    ToastPopUp.show(LoginActivity.this, getString(R.string.Enter_emailaddress));
                    edforgotpassEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                alertDialogForgot.dismiss();
            }
        });
    }

    //--------------------sending email for forgot password------------------------------------------
    public void Forgotpassword(final String email) {
        mDialog.setConfiguration(new ArcConfiguration(getApplicationContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ForgetPasswordInterface service = retrofit.create(ForgetPasswordInterface.class);
        Call<MobileModel> call = service.sendData(email);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                try {
                    MobileModel result = new MobileModel();
                    String successstatus = response.body().getCheckstatus().get(0).getStatus();
                    String msg = response.body().getCheckstatus().get(0).getMsg();
                    System.out.println("successstatus" + successstatus);
                    if (successstatus.equals("1")) {
                        // status 1 : success
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.reg_completion_link_3) + "\n\n" + getResources().getString(R.string.reg_completion_link_2));
                        alertDialogForgot.dismiss();
                        mDialog.dismiss();
                    } else if (successstatus.equals("2")) {
                        // status 2 : account not verified
                        alertDialogForgot.dismiss();
                        mDialog.dismiss();
                        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                        LayoutInflater li = LayoutInflater.from(context);
                        View confirmDialog = li.inflate(R.layout.signup_dialog, null);
                        Button btnOk = (Button) confirmDialog.findViewById(R.id.btndialogsignupok);
                        TextView txtheadingofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_heading);
                        TextView txtofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_text);
                        TextView txtresend_email = confirmDialog.findViewById(R.id.txtresend_email);
                        txtresend_email.setVisibility(View.VISIBLE);
                        txtheadingofdialog.setText("Complete registration first !");
                        txtofdialog.setText(getResources().getString(R.string.reg_completion_link) + email + getResources().getString(R.string.reg_completion_link_1) + "\n\n" + getResources().getString(R.string.reg_completion_link_2));
                        alertdialog.setView(confirmDialog);
                        alertdialog.setCancelable(false);
                        alertDialogreturn = alertdialog.create();
                        alertDialogreturn.show();
                        txtresend_email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                resendEmail(email);
                            }
                        });

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogreturn.dismiss();
                            }
                        });
                    } else if (successstatus.equals("3")) {
                        // status 3 : email not found
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.registered_email));
                        edforgotpassEmail.setText("");
                        mDialog.dismiss();
                        mDialog.dismiss();
                    } else {
                        // status 0 : error
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.server_response_error));
                        edforgotpassEmail.setText("");
                        mDialog.dismiss();
                    }
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                    alertDialogForgot.dismiss();
                }

            }

            @Override
            public void onFailure(Throwable t) {
// we use this methiod in this place to for some situation when during progress dialog is running and that time
                //netowrk goes off then dialog continue running so due to this validation dialog is dismiss.
                ToastPopUp.show(LoginActivity.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //---------------------sending details of login to server and validating------------------------
    public void login() {
        String strDeviceid = sharedPreferences.getDeviceToken();
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        final String stremailaddress = email.getText().toString().trim();
        String strPassword = password.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginInterface service = retrofit.create(LoginInterface.class);
        Log.d("login_inputs", stremailaddress + "," + strPassword + "," + strDeviceid);
        Call<MobileModel> call = service.sendData(stremailaddress, strPassword, strDeviceid, "0");
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                try {
                    // progressDialog.dismiss();
                    MobileModel result = new MobileModel();
                    String successstatus = response.body().getCheckstatus().get(0).getStatus();
                    // String msg = response.body().getCheckstatus().get(0).getMsg();
                    Log.d("successstatus", "" + successstatus);
                    // System.out.println("strMerchant_id" + strMerchant_id);
                    if (successstatus.equals("1")) {
                        String strMerchant_id = response.body().getCheckstatus().get(0).getUserID();
                        String strFname = response.body().getCheckstatus().get(0).getFname();
                        String strLname = response.body().getCheckstatus().get(0).getLname();
                        String strFullName = strFname + " " + strLname;
                        String count = response.body().getCheckstatus().get(0).getCount();
                        String country_id = response.body().getCheckstatus().get(0).getCountryID();
                        String privacy = response.body().getCheckstatus().get(0).getPrivacy();
                        if (count.equals("0")) {
                            password.setText("");
                            email.setText("");
                            Intent in = new Intent(LoginActivity.this, First_Login.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("EmailId", stremailaddress);
                            bundle.putString("strMerchant_id", strMerchant_id);
                            bundle.putString("FName", strFname);
                            bundle.putString("LName", strLname);
                            bundle.putString("countryid", country_id);
                            bundle.getString("privacy");
                            in.putExtras(bundle);
                            startActivity(in);
                        } else {
                            password.setText("");
                            email.setText("");
                            sharedPreferences.createUserCredentialSession(strMerchant_id, strFullName, privacy);
                            sharedPreferences.store_radius(Validation.inital_radius);
                            Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                            startActivity(i);
                            //-----------------------set up notification status to ON-----------------------
                            sharedPreferences.set_notification_status("ON");
                        }
                    } else if (successstatus.equals("0")) {
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.registered_email));
                        password.setText("");
                        email.setText("");
                        //password.setFocusableInTouchMode(false);
                        email.requestFocus();
                        mDialog.dismiss();
                        //Toast.makeText(Login.this, "Enter a valid email address", Toast.LENGTH_LONG).show();
                    } else if (successstatus.equals("2")) {
                        // ToastPopUp.show(SendBirdLoginActivity.this, "A Registration Completion Link has been sent to " + stremailaddress + ". Please set your password using that link");
                        password.setText("");
                        mDialog.dismiss();
                        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                        LayoutInflater li = LayoutInflater.from(context);
                        View confirmDialog = li.inflate(R.layout.signup_dialog, null);
                        Button btnOk = (Button) confirmDialog.findViewById(R.id.btndialogsignupok);
                        TextView txtheadingofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_heading);
                        TextView txtofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_text);
                        TextView txtresend_email = confirmDialog.findViewById(R.id.txtresend_email);
                        txtresend_email.setVisibility(View.VISIBLE);
                        btnOk.setText("Cancel");
                        txtheadingofdialog.setText("Complete registration first !");
                        // txtofdialog.setText("Your profile is public, if you want to make it private, please go to View Profile");
                        txtofdialog.setText(getResources().getString(R.string.reg_completion_link) + stremailaddress + getResources().getString(R.string.reg_completion_link_1) + "\n\n" + getResources().getString(R.string.reg_completion_link_2));
                        //-------------Adding dialog box to the view of alert dialog
                        alertdialog.setView(confirmDialog);
                        alertdialog.setCancelable(false);
                        //----------------Creating an alert dialog
                        alertDialogreturn = alertdialog.create();
                        // alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                        //----------------Displaying the alert dialog
                        alertDialogreturn.show();

                        txtresend_email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                resendEmail(stremailaddress);
                            }
                        });
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogreturn.dismiss();
                            }
                        });
                        // Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_LONG).show();
                    } else if (successstatus.equals("4")) {
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.wrong_password));
                        // Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                    } else {
                        ToastPopUp.show(LoginActivity.this, getResources().getString(R.string.login_failed));
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    mDialog.dismiss();
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(LoginActivity.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    public void resendEmail(String strEmail) {
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        final String stremailaddress = email.getText().toString().trim();
        String strPassword = password.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ResendLinkInterface service = retrofit.create(ResendLinkInterface.class);

        Call<StatusModel> call = service.sendData(strEmail);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Response<StatusModel> response, Retrofit retrofit) {
                try {
                    String strsuccessStatus = response.body().getStatus().toString();
                    if (strsuccessStatus.equals("1")) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                        alertDialogreturn.dismiss();
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_response_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                ToastPopUp.show(getApplicationContext(), getResources().getString(R.string.server_response_error));
            }
        });
    }

    //-------------------requests all permissions-------------------------------------------------------
    private void requestPermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        READ_CONTACTS
                }, RequestPermissionCode);
    }

    //---------------------------------check permissions method-------------------------------------
    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //------------------------google login api-----------------------------------------------------
    public void googleLogin(final String first_Name, final String last_Name, final String google_Email, String Login_Type) {
        String strDeviceid = sharedPreferences.getDeviceToken();
       /* progressDialog = new ProgressDialog(SendBirdLoginActivity.this);
        progressDialog.show();*/
        if (first_Name == null) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.select_account), Toast.LENGTH_SHORT).show();
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            signIn();
                        }
                    });
        } else {
            mDialog.setConfiguration(new ArcConfiguration(this));
            mDialog.show();
            mDialog.setCancelable(false);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(1, TimeUnit.HOURS);
            client.setReadTimeout(1, TimeUnit.HOURS);
            client.setWriteTimeout(1, TimeUnit.HOURS);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebServices.MAIN_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GoogleLoginInterface service = retrofit.create(GoogleLoginInterface.class);
            Call<GoogleLoginModel> call = service.sendData(first_Name, last_Name, google_Email, strDeviceid, Login_Type);
            call.enqueue(new Callback<GoogleLoginModel>() {
                @Override
                public void onResponse(Response<GoogleLoginModel> response, Retrofit retrofit) {
                    try {
                        String successstatus = response.body().getCheckstatus().getStatus();
                        String count = response.body().getCheckstatus().getCount();
                        System.out.println("successstatus" + successstatus);
                        String strMerchant_id = response.body().getCheckstatus().getUserId();
                        String strFname = response.body().getCheckstatus().getFName();
                        String strLname = response.body().getCheckstatus().getLName();
                        String strPrivacy = response.body().getCheckstatus().getPrivacy();
                        String strFullName = strFname + " " + strLname;
                        if (successstatus.equals("1")) {
                            mDialog.dismiss();
                            if (count.equals("0")) {
                                Intent in = new Intent(LoginActivity.this, First_Login.class);
                                Bundle bundle = new Bundle();
                                //Add your data to bundle
                                bundle.putString("EmailId", google_Email);
                                bundle.putString("strMerchant_id", strMerchant_id);
                                bundle.putString("FName", first_Name);
                                bundle.putString("LName", last_Name);
                                bundle.putString("countryid", "");
                                bundle.putString("privacy", strPrivacy);
                                // in.putExtra("",);
                                in.putExtras(bundle);
                                startActivity(in);
                            } else {
                                sharedPreferences.createUserCredentialSession(strMerchant_id, strFullName, strPrivacy);
                                sharedPreferences.store_radius(Validation.inital_radius);

                                Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                                startActivity(i);
                                //----------------------set up status to ON-----------------------
                                sharedPreferences.set_notification_status("ON");
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unsuccess), Toast.LENGTH_SHORT).show();
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
                    ToastPopUp.show(LoginActivity.this, getString(R.string.server_response_error));
                    mDialog.dismiss();
                }
            });
        }

    }

    public void facebookLogin(final String first_Name, final String last_Name, final String fbEmail, String fb_Id) {
        String strDeviceid = sharedPreferences.getDeviceToken();
       /* progressDialog = new ProgressDialog(SendBirdLoginActivity.this);
        progressDialog.show();*/
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FacebookLoginInterface service = retrofit.create(FacebookLoginInterface.class);

        Call<GoogleLoginModel> call = service.sendData(first_Name, last_Name, fbEmail, strDeviceid, fb_Id);

        call.enqueue(new Callback<GoogleLoginModel>() {
            @Override
            public void onResponse(Response<GoogleLoginModel> response, Retrofit retrofit) {
                try {
                    String successstatus = response.body().getCheckstatus().getStatus();
                    String count = response.body().getCheckstatus().getCount();
                    System.out.println("successstatus" + successstatus);
                    String strMerchant_id = response.body().getCheckstatus().getUserId();
                    String strFname = response.body().getCheckstatus().getFName();
                    String strLname = response.body().getCheckstatus().getLName();
                    String privacy = response.body().getCheckstatus().getPrivacy();
                    String strFullName = strFname + " " + strLname;
                    if (successstatus.equals("1")) {
                        mDialog.dismiss();
                        if (count.equals("0")) {
                            Intent in = new Intent(LoginActivity.this, First_Login.class);

                            Bundle bundle = new Bundle();

//Add your data to bundle
                            bundle.putString("EmailId", fbEmail);
                            bundle.putString("strMerchant_id", strMerchant_id);
                            bundle.putString("FName", first_Name);
                            bundle.putString("LName", last_Name);
                            bundle.putString("countryid", "");
                            bundle.putString("privacy", privacy);
                            // in.putExtra("",);
                            in.putExtras(bundle);
                            startActivity(in);
                        } else {
                            sharedPreferences.createUserCredentialSession(strMerchant_id, strFullName, privacy);
                            sharedPreferences.store_radius(Validation.inital_radius);

                            Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                            startActivity(i);
                            //----------------------set up status to ON-----------------------
                            sharedPreferences.set_notification_status("ON");
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unsuccess), Toast.LENGTH_SHORT).show();
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
                ToastPopUp.show(LoginActivity.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //-----------------------Back button press------------------------------------------------------
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
        dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
        dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
        //-------------Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);
        alert.setCancelable(false);
        //----------------Creating an alert dialog
        alertDialogForgot = alert.create();
        alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialogForgot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //----------------Displaying the alert dialog
        alertDialogForgot.show();
        // if button is clicked, close the custom dialog
        dialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForgot.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForgot.dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //added on 14 june 2019 by Bhakti
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                String personName = account.getDisplayName();
                Log.e("Google_details", "display name: " + personName);
                String first_name = account.getGivenName();
                String last_name = account.getFamilyName();
                String personEmail = account.getEmail();
                try {
                    Log.e("Google_details", "Name: " + first_name + " last name:" + last_name + ", email: " + personEmail);
                    if (first_name.equals("null")) {
                        mDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.select_account), Toast.LENGTH_SHORT).show();
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        signIn();
                                    }
                                });
                        // mDialog.dismiss();
                        // Toast.makeText(SendBirdLoginActivity.this, "Retry login", Toast.LENGTH_SHORT).show();
                    } else {
                        googleLogin(first_name, last_name, personEmail, "gp");
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.select_account), Toast.LENGTH_SHORT).show();
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // ...
                                    signIn();
                                }
                            });
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google_details", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    //make spanaable text view
    private void customTextView(TextView view, String firstblacktext, String firsthighlight, String secndBlacktext,
                                final String scndHighLight, String thirdBlackTest) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                firstblacktext);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanTxt.length(), 0);

        spanTxt.append(firsthighlight);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 6, 26, 0);

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent trmscondn = new Intent(LoginActivity.this, Terms_Conditions.class);
                startActivity(trmscondn);
            }
        }, spanTxt.length() - firsthighlight.length(), spanTxt.length(), 0);
        spanTxt.append(secndBlacktext);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 31, spanTxt.length(), 0);
        spanTxt.append(scndHighLight);
//        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 47, spanTxt.length(), 0);

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent trmscondn = new Intent(LoginActivity.this, Privacy_policy.class);

                startActivity(trmscondn);
            }
        }, spanTxt.length() - scndHighLight.length(), spanTxt.length(), 0);
        // spanTxt.append(thirdBlackTest);
        //  spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 83, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}
