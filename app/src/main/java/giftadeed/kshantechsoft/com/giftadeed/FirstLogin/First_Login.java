package giftadeed.kshantechsoft.com.giftadeed.FirstLogin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CityAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CityModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CitySignup;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountrySignup;
import giftadeed.kshantechsoft.com.giftadeed.Signup.EmailChechInterface;
import giftadeed.kshantechsoft.com.giftadeed.Signup.MobileModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateModel;
import giftadeed.kshantechsoft.com.giftadeed.Signup.StateSignup;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
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


////////////////////////////////////////////////////////////////////
//                                                               //
//     Used to complete details required to complete profile    //
/////////////////////////////////////////////////////////////////
public class First_Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText edfirstlogin_country, edfirstlogin_state, edfirstlogin_city, edfirstlogin_email;
    Button btnsubmit;
    TextView firstloginhead;
    private ArrayList<SignupPOJO> countries;
    private ArrayList<SignupPOJO> states;
    private ArrayList<SignupPOJO> cities;
    private ArrayList<SignupPOJO> countri = new ArrayList<>();
    String contryid = null, strCountry_Id;
    String stateid = null;
    String cityid = null, strCountry, strCity, strState, strUserId, strEmail, message, strFname, strLname, strPrivacy;
    Context context;
    ListView categorylist;
    EditText txtsearch;
    CountryAdapter ctryadptr;
    StateAdapter stateadptr;
    CityAdapter cityadptr;
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    SessionManager sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__login);
        init();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(First_Login.this)
                .enableAutoManage(First_Login.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        try {
            Bundle bundle = getIntent().getExtras();
            strUserId = bundle.getString("strMerchant_id");
            strEmail = bundle.getString("EmailId");
            strFname = bundle.getString("FName");
            strLname = bundle.getString("LName");
            message = bundle.getString("message");
            strCountry_Id = bundle.getString("countryid");
            strPrivacy = bundle.getString("privacy");
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            Bugreport bg = new Bugreport();
            bg.sendbug(writer.toString());
        }
        if (!(strCountry_Id.equals(""))) {
            getSelectedcountry();
        }

        if (strEmail.equals("")) {
            edfirstlogin_email.setVisibility(View.VISIBLE);
        } else {
            edfirstlogin_email.setVisibility(View.GONE);
        }

        mDialog = new SimpleArcDialog(this);
        sharedPreferences = new SessionManager(First_Login.this);
        context = First_Login.this;
        edfirstlogin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getcountry();
            }
        });

        edfirstlogin_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contryid != null) {
                    getstate(contryid, "Clicked");
                } else {
                    Toast.makeText(First_Login.this, getResources().getString(R.string.select_country), Toast.LENGTH_LONG).show();
                }
            }
        });
        edfirstlogin_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateid == null) {
                    Toast.makeText(First_Login.this, getResources().getString(R.string.select_state), Toast.LENGTH_LONG).show();
                } else {
                    getcity(stateid);
                }
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateform();
            }
        });

    }

    //-----------------getting country
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CountrySignup service = retrofit.create(CountrySignup.class);
        countries.clear();

        Call<CountryModel> call = service.sendData("country");
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Response<CountryModel> response, Retrofit retrofit) {
                //response.code();
                try {
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
                        Toast.makeText(First_Login.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
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
                                    edfirstlogin_country.setText(countries.get(i).getName());
                                    strCountry = edfirstlogin_country.getText().toString();
                                    contryid = countries.get(i).getId();
                                    edfirstlogin_state.setText("");
                                    edfirstlogin_city.setText("");
                                    First_Login.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(First_Login.this, getString(R.string.server_response_error));
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

    public void getSelectedcountry() {
        //countries = new ArrayList<>();
//        mDialog.setConfiguration(new ArcConfiguration(First_Login.this));
//        mDialog.show();
        // mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        CountrySignup service = retrofit.create(CountrySignup.class);

        Call<CountryModel> call = service.sendData("country");
        call.enqueue(new Callback<CountryModel>() {
            @Override
            public void onResponse(Response<CountryModel> response, Retrofit retrofit) {
                //response.code();
                countries = new ArrayList<>();
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
                            if (selected_id.equals(strCountry_Id)) {
                                contryid = selected_id;
                                strCountry = countries.get(i).getName();
                                edfirstlogin_country.setText(strCountry);

                            }

                        }


                        getstate(contryid, "NonClicked");
                        // mDialog.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(First_Login.this, getString(R.string.server_response_error));
                //  mDialog.dismiss();
            }
        });
    }


    //--------------------getting states from sever----------------------------------------------------
    public void getstate(String cntryid, final String fromwhere) {
        states = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getApplicationContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        states.clear();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
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
                        Toast.makeText(First_Login.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        if (fromwhere.equals("Clicked")) {
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
                                        edfirstlogin_state.setText(states.get(i).getName());
                                        stateid = states.get(i).getId();
                                        edfirstlogin_city.setText("");
                                        strState = edfirstlogin_state.getText().toString();
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
                        } else {
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    states.clear();
                    categorylist.setAdapter(null);
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());

                }

            }


            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(First_Login.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //--------------------getting cities from sever----------------------------------------------------
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
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
                    if (cities.size() == 0) {
                        Toast.makeText(First_Login.this, getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
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
                                    edfirstlogin_city.setText(cities.get(i).getName());
                                    strCity = edfirstlogin_city.getText().toString();
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
                } catch (Exception e) {
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(First_Login.this, getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //---------------views initlizations
    public void init() {
        firstloginhead = (TextView) findViewById(R.id.firstloginhead);
        edfirstlogin_country = (EditText) findViewById(R.id.edfirstlogin_country);
        edfirstlogin_state = (EditText) findViewById(R.id.edfirstlogin_state);
        edfirstlogin_city = (EditText) findViewById(R.id.edfirstlogin_city);
        edfirstlogin_email = (EditText) findViewById(R.id.edfirstloginemail);
        btnsubmit = (Button) findViewById(R.id.firstloginsubmit);


        btnsubmit.setTypeface(new FontDetails(First_Login.this).fontStandardForPage);
        edfirstlogin_country.setTypeface(new FontDetails(First_Login.this).fontStandardForPage);
        edfirstlogin_state.setTypeface(new FontDetails(First_Login.this).fontStandardForPage);
        edfirstlogin_city.setTypeface(new FontDetails(First_Login.this).fontStandardForPage);
        edfirstlogin_email.setTypeface(new FontDetails(First_Login.this).fontStandardForPage);

    }

    //----------check validations-----------------------------------------------------------------
    public void validateform() {
        String StrCountry_name = edfirstlogin_country.getText().toString();
        String strState_name = edfirstlogin_state.getText().toString();
        String strCity_name = edfirstlogin_city.getText().toString();


        if (StrCountry_name.length() < 1) {
            ToastPopUp.displayToast(First_Login.this, getResources().getString(R.string.select_country));
        } else if (strState_name.length() < 1) {
            ToastPopUp.displayToast(First_Login.this, getResources().getString(R.string.select_state));
        } else if (strCity_name.length() < 1) {
            ToastPopUp.displayToast(First_Login.this, getResources().getString(R.string.select_city));
        } else if (edfirstlogin_email.getVisibility() == View.VISIBLE) {
            strEmail = edfirstlogin_email.getText().toString();
            if (!(Validation.isNetworkAvailable(First_Login.this))) {
                ToastPopUp.show(First_Login.this, getString(R.string.network_validation));
            } else if (strEmail.length() < 1) {
                ToastPopUp.show(context, getString(R.string.Enter_emailaddress));
                edfirstlogin_email.requestFocus();
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edfirstlogin_email.getText().toString().trim()).matches()) {
                ToastPopUp.show(context, getString(R.string.Enter_validemailaddress));
                edfirstlogin_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                edfirstlogin_email.requestFocus();
                edfirstlogin_email.selectAll();
            } else {
                checkemail(0);
            }
        } else {
            firstLogin(contryid, stateid, cityid, strEmail, strUserId, strPrivacy);
        }
    }

    //------------------------------sending first login details to server-------------------------------
    public void firstLogin(String country_Id, String State_Id, String city_Id, String Email_Id, final String User_Id, final String privacy) {
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirstLoginInterface service = retrofit.create(FirstLoginInterface.class);
        Call<FirstLoginModel> call = service.sendData(country_Id, State_Id, city_Id, Email_Id, User_Id);
        call.enqueue(new Callback<FirstLoginModel>() {
            @Override
            public void onResponse(Response<FirstLoginModel> response, Retrofit retrofit) {
                try {
                    String SuccessStatus = response.body().getCheckstatus().get(0).getStatus();
                    String strFullName = strFname + " " + strLname;
                    if (SuccessStatus.equals("1")) {
                        mDialog.dismiss();
                        sharedPreferences.createUserCredentialSession(User_Id, strFullName, privacy);
                        sharedPreferences.store_radius(Validation.inital_radius);
                        Intent in = new Intent(First_Login.this, TaggedneedsActivity.class);
                        in.putExtra("message", message);
                        startActivity(in);
                    } else {
                        Toast.makeText(First_Login.this, getResources().getString(R.string.login_unsuccess), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
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
                mDialog.dismiss();
                ToastPopUp.show(First_Login.this, getString(R.string.server_response_error));
            }
        });

    }

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();


        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/
        Intent in = new Intent(First_Login.this, LoginActivity.class);
        in.putExtra("message", message);
        startActivity(in);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //--------------------------check email already exist
    public void checkemail(final int chkemail) {


        String stremailaddress = edfirstlogin_email.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
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
                        Toast.makeText(First_Login.this, "Email address already exists", Toast.LENGTH_LONG).show();
                        edfirstlogin_email.setText("");


                    } else {
                        firstLogin(contryid, stateid, cityid, strEmail, strUserId, strPrivacy);
                    }

                    /*else {
                        mDialog.dismiss();

                        if (chkemail == 0) {

                            //checkmobileno(1);
                            if (!(Validation.isNetworkAvailable(First_Login.this))) {
                                mDialog.dismiss();
                                ToastPopUp.show(First_Login.this, getString(R.string.network_validation));

                            } else {
                                *//*String user="";
                                Intent in =new Intent(SignUp.this, First_Login.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("EmailId", stremailaddress);
                                bundle.putString("strMerchant_id",user);
                                bundle.putString("message", message);
                                bundle.putString("FName",strFirstMerchantName);
                                bundle.putString("LName",strLastName);
                                bundle.putString("countryid",contryid);
                                in.putExtras(bundle);
                                startActivity(in);*//*
                                signupdata(strFirstMerchantName, strLastName, stremailaddress, contryid, strDeviceid);

                            }

                        }


                    }
*/

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
}
