/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Signup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.PrivacyPolicy.Privacy_policy;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.termsandconditions.Terms_Conditions;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


////////////////////////////////////////////////////////////////////
//                                                               //
//    used to create new account for app                        //
/////////////////////////////////////////////////////////////////
public class SignUp extends AppCompatActivity {
    TextView txtagreet_C, txtagreePolicy;
    TextView login, txtsignup, txtTermsandconditions;
    EditText edFname, edLname, edEmail, edState, edCity, edCountry;
    CheckBox chkboxagree;
    RadioGroup radioGroup;
    Button btnSignup;
    Context context;
    TextInputLayout txtILfnmae, txtILlnmae, txtILemail, txtILcountry;
    ListView categorylist;
    EditText txtsearch;
    private ArrayList<SignupPOJO> countries;
    private ArrayList<SignupPOJO> states;
    private ArrayList<SignupPOJO> cities;
    String contryid = null;
    String stateid = null;
    String cityid = null;
    private String stremailaddress, strFirstName, strLastName, strCountry;
    String strDeviceid;
    CountryAdapter ctryadptr;
    StateAdapter stateadptr;
    CityAdapter cityadptr;
    SimpleArcDialog mDialog;
    private AlertDialog alertDialogreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDialog = new SimpleArcDialog(this);

        context = SignUp.this;
        init();

        edFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
                    edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!(edFname.getText().toString().matches("^[a-zA-Z.'_\\s]*$"))) {
                    edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edFname.getText().length() > 14) {
                    ToastPopUp.show(SignUp.this, getResources().getString(R.string.length_error));
                }
            }
        });

        edFname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strFirstName = edFname.getText().toString().trim();
                if (!hasFocus) {
                    if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
                        ToastPopUp.show(context, getString(R.string.Enter_FirstName));
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    } else if (!(strFirstName.matches("^[a-zA-Z.'_\\s]*$"))) {
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        ToastPopUp.show(SignUp.this, getString(R.string.validation_first_name_special_characters_merchant) + " in first name");
                    } else if (!(strFirstName.matches("\\w*"))) {
                        strFirstName = strFirstName.replaceAll("\\s{2,}", " ");
                    } else if ((strFirstName.length() < 3) || (strFirstName.length() > 15)) {
                        edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        ToastPopUp.show(context, getString(R.string.min_length));
                    } else {

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
                if (Validation.isStringNullOrBlank(edLname.getText().toString())) {
                    edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!(edLname.getText().toString().matches("^[a-zA-Z.'_\\s]*$"))) {
                    edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ToastPopUp.show(SignUp.this, getString(R.string.validation_first_name_special_characters_merchant));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edLname.getText().length() > 19) {
                    ToastPopUp.show(SignUp.this, getResources().getString(R.string.length_error_2));
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
                            ToastPopUp.show(SignUp.this, getString(R.string.validation_last_name_special_characters_merchant));
                        } else if (!(edLname.getText().toString().matches("\\w*"))) {
                            strLastName = strLastName.replaceAll("\\s{2,}", " ");
                        } else if ((edLname.getText().toString().length() < 1) || (edLname.getText().toString().length() > 20)) {
                            edLname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            ToastPopUp.show(context, getString(R.string.min_length));
                        } else {

                        }
                        edLname.setText(strLastName);
                    }
                }
            }
        });

        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Validation.isStringNullOrBlank(edEmail.getText().toString())) {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches()) {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (edEmail.getText().toString().contains(" ")) {
                    edEmail.setText(edEmail.getText().toString().replaceAll(" ", ""));
                    edEmail.setSelection(edEmail.getText().length());
                }
            }
        });

        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(Validation.isNetworkAvailable(SignUp.this))) {
                        ToastPopUp.show(SignUp.this, getString(R.string.network_validation));
                    }
                    if (!Validation.isStringNullOrBlank(edEmail.getText().toString())) {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches()) {
                            ToastPopUp.show(context, getString(R.string.Enter_validemailaddress));
                            edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            edEmail.selectAll();
                        } else if (!Validation.isStringNullOrBlank(edEmail.getText().toString())) {
                            checkemail(1);
                        }
                    } else {
                        ToastPopUp.show(context, getString(R.string.Enter_emailaddress));
                        edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            }
        });

        edCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcountry();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignUp.this, LoginActivity.class);
                startActivity(login);
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvalidations();
            }
        });

        txtagreet_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trmscondn = new Intent(SignUp.this, Terms_Conditions.class);
                startActivity(trmscondn);
            }
        });

        txtagreePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trmscondn = new Intent(SignUp.this, Privacy_policy.class);
                startActivity(trmscondn);
            }
        });

        customTextView(txtTermsandconditions, "Agree ", "Terms and Conditions,", "\nand ", "Privacy Policy.", "to be changed to suit global audience.");
    }

    //--------------------Initilizing UI variables--------------------------------------------------
    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        login = (TextView) findViewById(R.id.logintxt);
        txtsignup = (TextView) findViewById(R.id.txtSignup);
        edFname = (EditText) findViewById(R.id.edfname);
        edLname = (EditText) findViewById(R.id.edlname);
        edEmail = (EditText) findViewById(R.id.edemail);
        edCountry = (EditText) findViewById(R.id.ed_country);
        chkboxagree = (CheckBox) findViewById(R.id.chkagree);
        btnSignup = (Button) findViewById(R.id.btnsignup);
        txtILfnmae = (TextInputLayout) findViewById(R.id.txtinputlayoutsignupfname);
        txtILlnmae = (TextInputLayout) findViewById(R.id.txtinputlayoutsignuplname);
        txtILemail = (TextInputLayout) findViewById(R.id.txtinputlayoutsignupemail);
        txtILcountry = (TextInputLayout) findViewById(R.id.txtinputlayoutsignupcountry);
        txtTermsandconditions = (TextView) findViewById(R.id.txtTermsandcondn);
        txtagreet_C = (TextView) findViewById(R.id.txtagreeT_C);
        txtagreePolicy = (TextView) findViewById(R.id.txtagreePolicy);
        btnSignup.setTypeface(new FontDetails(context).fontStandardForPage);
        txtTermsandconditions.setTypeface(new FontDetails(context).fontStandardForPage);
        txtILfnmae.setTypeface(new FontDetails(context).fontStandardForPage);
        txtILlnmae.setTypeface(new FontDetails(context).fontStandardForPage);
        txtILemail.setTypeface(new FontDetails(context).fontStandardForPage);
        txtILcountry.setTypeface(new FontDetails(context).fontStandardForPage);
        login.setTypeface(new FontDetails(context).fontStandardForPage);
        edFname.setTypeface(new FontDetails(context).fontStandardForPage);
        edLname.setTypeface(new FontDetails(context).fontStandardForPage);
        edEmail.setTypeface(new FontDetails(context).fontStandardForPage);
        edCountry.setTypeface(new FontDetails(context).fontStandardForPage);
        edEmail.setTypeface(new FontDetails(context).fontStandardForPage);
        chkboxagree.setTypeface(new FontDetails(context).fontStandardForPage);


    }

    public void selectCountry() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.category_dialog);

        ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
        Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
        categorylist.setAdapter(new CountryAdapter(countries, context));
        categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  category_id = categories.get(i).getCategory_id();
                edCountry.setText(countries.get(i).getName());
                strCountry = edCountry.getText().toString();
                contryid = countries.get(i).getId();
                edState.setText("");
                edCity.setText("");
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

    public void selectState() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.category_dialog);

        ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
        Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
        categorylist.setAdapter(new StateAdapter(states, context));
        categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  category_id = categories.get(i).getCategory_id();
                edState.setText(states.get(i).getName());
                stateid = states.get(i).getId();
                edCity.setText("");
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

    public void selectCity() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.category_dialog);

        ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
        Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
        categorylist.setAdapter(new CityAdapter(cities, context));
        categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  category_id = categories.get(i).getCategory_id();
                edCity.setText(cities.get(i).getName());
                cityid = cities.get(i).getId();
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
    }

    //--------------------getting country from sever------------------------------------------------
    public void getcountry() {
        countries = new ArrayList<>();
        countries.clear();

        mDialog.setConfiguration(new ArcConfiguration(getApplicationContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CountrySignup service = retrofit.create(CountrySignup.class);
        countries.clear();

        Call<CountryModel> call = service.sendData("country");
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Response<CountryModel> response, Retrofit retrofit) {
                //response.code();
                CountryModel countryData = response.body();
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
                if (countries.size() == 0) {
                    Toast.makeText(SignUp.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    //categorylist.getAdapter().notifyAll();
                    txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    ctryadptr = new CountryAdapter(countries, context);


                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
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
                            if (countries.size() > 0) {
                                edCountry.setText(countries.get(i).getName());
                                strCountry = edCountry.getText().toString();
                                contryid = countries.get(i).getId();
                                //edState.setText("");
                                //edCity.setText("");
                                SignUp.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                stateid = null;
                            }
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

                    dialog.show();
                    mDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(SignUp.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
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

    //--------------------getting states from sever-------------------------------------------------
    public void getstate(String cntryid) {
        states = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getApplicationContext()));
        mDialog.show();
        mDialog.setCancelable(false);
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
                try {


                    StateModel countryData = response.body();
                    for (int j = 0; j < countryData.getStatedata().size(); j++) {
                        //countries.add(countryData.getStatedata().get(j).getStateName().toString());
                        SignupPOJO data = new SignupPOJO();
                        data.setId(countryData.getStatedata().get(j).getStateID().toString());
                        data.setName(countryData.getStatedata().get(j).getStateName().toString());
                        states.add(data);
                    }
                    if (states.size() == 0) {
                        Toast.makeText(SignUp.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.category_dialog);
                        txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                        categorylist = (ListView) dialog.findViewById(R.id.category_list);
                        Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                        stateadptr = new StateAdapter(states, context);
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
                                if (states.size() > 0) {
                                    edState.setText(states.get(i).getName());
                                    stateid = states.get(i).getId();
                                    edCity.setText("");
                                    //  edPassword.requestFocus();
                                    //cityid.equals("");
                                    // getcity(stateid);
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
                        mDialog.dismiss();
                    }
                } catch (Exception e) {
                    states.clear();
                    categorylist.setAdapter(null);

                }

            }


            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(SignUp.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //--------------------getting cities from sever-------------------------------------------------
    public void getcity(String stateid) {
        cities = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getApplicationContext()));
        mDialog.show();
        mDialog.setCancelable(false);
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


                for (int j = 0; j < countryData.getCitydata().size(); j++) {
                    //countries.add(countryData.getStatedata().get(j).getStateName().toString());
                    SignupPOJO data = new SignupPOJO();
                    data.setId(countryData.getCitydata().get(j).getCityID().toString());
                    data.setName(countryData.getCitydata().get(j).getCityName().toString());
                    cities.add(data);
                }
                if (cities.size() == 0) {
                    Toast.makeText(SignUp.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    txtsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                    cityadptr = new CityAdapter(cities, context);
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
                            if (cities.size() > 0) {
                                edCity.setText(cities.get(i).getName());
                                cityid = cities.get(i).getId();
                                //getstate(contryid);
                                //   edPassword.requestFocus();
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
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(SignUp.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //----------------------checking email on server------------------------------------------------
    public void checkemail(final int chkemail) {
        stremailaddress = edEmail.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EmailChechInterface service = retrofit.create(EmailChechInterface.class);
        Call<MobileModel> call = service.sendData(stremailaddress);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                try {
                    MobileModel result = new MobileModel();
                    String successstatus = response.body().getCheckstatus().get(0).getStatus();
                    System.out.println("successstatus" + successstatus);
                    if (successstatus.equals("1")) {
                        mDialog.dismiss();
                        Toast.makeText(SignUp.this, getResources().getString(R.string.email_already_exist), Toast.LENGTH_LONG).show();
                        edEmail.setText("");
                    } else {
                        mDialog.dismiss();
                        if (chkemail == 0) {
                            //checkmobileno(1);
                            if (!(Validation.isNetworkAvailable(SignUp.this))) {
                                mDialog.dismiss();
                                ToastPopUp.show(SignUp.this, getString(R.string.network_validation));
                            } else {
                                /*String user="";
                                Intent in =new Intent(SignUp.this, First_Login.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("EmailId", stremailaddress);
                                bundle.putString("strMerchant_id",user);
                                bundle.putString("FName",strFirstName);
                                bundle.putString("LName",strLastName);
                                bundle.putString("countryid",contryid);
                                in.putExtras(bundle);
                                startActivity(in);*/
                                signupdata(strFirstName.trim(), strLastName.trim(), stremailaddress.trim(), contryid, strDeviceid);
                            }
                        }
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
// we use this methiod in this place to for some situation when during progress dialog is running and that time
                //netowrk goes off then dialog continue running so due to this validation dialog is dismiss.
                mDialog.dismiss();
            }
        });
    }

    //---------------------------checking validations-----------------------------------------------
    public void checkvalidations() {
        strFirstName = edFname.getText().toString().trim();
        strLastName = edLname.getText().toString().trim();
        stremailaddress = edEmail.getText().toString().trim();
        strCountry = edCountry.getText().toString();
        if (Validation.isStringNullOrBlank(edFname.getText().toString())) {
            ToastPopUp.show(context, getString(R.string.Enter_FirstName));
            edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (!(strFirstName.matches("^[a-zA-Z.'_\\s]*$"))) {
            edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ToastPopUp.show(SignUp.this, getString(R.string.validation_first_name_special_characters_merchant));
        } else if ((edFname.getText().toString().length() < 3) || (edFname.getText().toString().length() > 15)) {
            edFname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ToastPopUp.show(context, getString(R.string.min_length));
        } else if (!(Validation.isNetworkAvailable(SignUp.this))) {
            ToastPopUp.show(SignUp.this, getString(R.string.network_validation));
        } else if (stremailaddress.length() < 1) {
            ToastPopUp.show(context, getString(R.string.Enter_emailaddress));
            edEmail.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches()) {
            ToastPopUp.show(context, getString(R.string.Enter_validemailaddress));
            edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edEmail.requestFocus();
            edEmail.selectAll();
        } else if (!(Validation.isNetworkAvailable(SignUp.this))) {
            ToastPopUp.show(SignUp.this, getString(R.string.network_validation));
        } else if (strCountry.length() < 1) {
            ToastPopUp.displayToast(SignUp.this, getResources().getString(R.string.select_country));
        }
//        else if (!(chkboxagree.isChecked())) {
//            ToastPopUp.displayToast(SignUp.this, "Please accept the Terms and Conditions.");
//        }
        else {
            if (edEmail.isFocused()) {
                mDialog.setConfiguration(new ArcConfiguration(this));
                mDialog.show();
                mDialog.setCancelable(false);
                checkemail(0);
            } else {
                mDialog.setConfiguration(new ArcConfiguration(this));
                mDialog.show();
                mDialog.setCancelable(false);
                String strDeviceid = "";
                signupdata(strFirstName.trim(), strLastName.trim(), stremailaddress.trim(), contryid, strDeviceid);
            }
        }
    }

    //--------------------------sending signup data to server-------------------------------------------
    public void signupdata(String firstname, String lastname, final String email, String con_id, String d_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MAIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SignupInterface service = retrofit.create(SignupInterface.class);
        Call<MobileModel> call = service.sendData(firstname, lastname, email, con_id, d_id);
        call.enqueue(new Callback<MobileModel>() {
            @Override
            public void onResponse(Response<MobileModel> response, Retrofit retrofit) {
                try {
                    MobileModel result = new MobileModel();
                    String successstatus = response.body().getCheckstatus().get(0).getStatus();
                    Log.d("successstatus", successstatus);
                    if (successstatus.equals("1")) {
                        mDialog.dismiss();
                        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                        LayoutInflater li = LayoutInflater.from(context);
                        View confirmDialog = li.inflate(R.layout.signup_dialog, null);
                        Button btnOk = (Button) confirmDialog.findViewById(R.id.btndialogsignupok);
                        TextView txtheadingofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_heading);
                        TextView txtofdialog = (TextView) confirmDialog.findViewById(R.id.signupdialog_text);
                        txtheadingofdialog.setText("Sign up successful!");
                        // txtofdialog.setText("Your profile is public, if you want to make it private, please go to View Profile");
                        txtofdialog.setText(getResources().getString(R.string.reg_completion_link) + email + getResources().getString(R.string.reg_completion_link_1) + "\n\n" + getResources().getString(R.string.reg_completion_link_2));
                        //-------------Adding dialog box to the view of alert dialog
                        alertdialog.setView(confirmDialog);
                        alertdialog.setCancelable(false);
                        //----------------Creating an alert dialog
                        alertDialogreturn = alertdialog.create();
                        // alertDialogForgot.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                        //----------------Displaying the alert dialog
                        alertDialogreturn.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogreturn.dismiss();
                                Intent loginintent = new Intent(SignUp.this, LoginActivity.class);
                                startActivity(loginintent);
                            }
                        });
                    } else {
                        if (!(Validation.isNetworkAvailable(SignUp.this))) {
                            ToastPopUp.show(SignUp.this, getString(R.string.network_validation));
                            mDialog.dismiss();
                        } else {
                            ToastPopUp.show(SignUp.this, "Signup was unsuccessful");
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                ToastPopUp.show(SignUp.this, "Network issue");
// we use this methiod in this place to for some situation when during progress dialog is running and that time
                //netowrk goes off then dialog continue running so due to this validation dialog is dismiss.
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    //---------hide keypad on clicking anywhere-----------------------------------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { //--add for hide keyboard from screen-----------------------------------
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
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
                Intent trmscondn = new Intent(SignUp.this, Terms_Conditions.class);
                startActivity(trmscondn);
            }
        }, spanTxt.length() - firsthighlight.length(), spanTxt.length(), 0);
        spanTxt.append(secndBlacktext);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 31, spanTxt.length(), 0);
        spanTxt.append(scndHighLight);
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 45, spanTxt.length(), 0);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent trmscondn = new Intent(SignUp.this, Privacy_policy.class);

                startActivity(trmscondn);
            }
        }, spanTxt.length() - scndHighLight.length(), spanTxt.length(), 0);
        // spanTxt.append(thirdBlackTest);
        // spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 83, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}