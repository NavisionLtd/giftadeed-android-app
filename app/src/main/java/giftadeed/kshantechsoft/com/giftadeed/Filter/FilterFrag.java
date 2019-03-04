package giftadeed.kshantechsoft.com.giftadeed.Filter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.NeedDetailsFrag;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Signup.CountryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.Signup.SignupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import giftadeed.kshantechsoft.com.giftadeed.giftaneed.GiftANeedFrag;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Used to filter the tags according to conditions          //
/////////////////////////////////////////////////////////////////
public class FilterFrag extends Fragment {
    FragmentActivity myContext;
    View rootview;
    SimpleArcDialog mDialog;
    private ArrayList<CategoryPOJO> categories;
    String strNeedmapping_ID, strNeed_Name, strCharacter_Path, strImagenamereturned, strUser_ID, strDist;
    double radius;
    SeekBar seekBar;
    DiscreteSeekBar distance;
    EditText edselectcategory;
    TextView txtapplyfilter, txtradius, txtselectcategory, txtdist;
    Button btnapplyfilters;
    static FragmentManager fragmgr;
    SessionManager sessionManager;
    String value = "tab1";

    public static FilterFrag newInstance() {
        FilterFrag fragment = new FilterFrag();
                /*Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);*/
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_filter, container, false);
        TaggedneedsActivity.updateTitle("Filters");
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);

        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        sessionManager = new SessionManager(getContext());
        mDialog = new SimpleArcDialog(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("tab");
        }
        init();
        radius = sessionManager.getradius();
        edselectcategory.setText(Validation.FILTER_CATEGORY);
        edselectcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory();
            }
        });
        btnapplyfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 1;
                fragmgr = getFragmentManager();
                // Validation.radius = radius;
                sessionManager.store_radius((float) radius);
                // Log.d("Validation o radius", "validation from validation class" + Validation.radius + "Radius " + radius);
                strNeed_Name = edselectcategory.getText().toString();
                Validation.FILTER_CATEGORY = strNeed_Name;
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).addToBackStack(null).commit();
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int i = 1;
                fragmgr = getFragmentManager();
                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).addToBackStack(null).commit();*/
                Bundle bundle = new Bundle();
                // int i = 3;
                bundle.putString("tab", value);
                TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                mainHomeFragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                fragmentTransaction.commit();

            }
        });
        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 7;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    /*fragmgr = getFragmentManager();
                    // fragmentManager.beginTransaction().replace( R.id.Myprofile_frame,TaggedneedsFrag.newInstance(i)).commit();
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(i)).addToBackStack(null).commit();*/
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


        // Log.d("validation radius", String.valueOf(Validation.radius));

        txtdist.setText(sessionManager.getradius() + " Metres");

        double rad = sessionManager.getradius();
        distance.setProgress((int) rad);
        Log.d("validation radius", String.valueOf(rad));
        if (sessionManager.getradius() > 1000) {
            float valueKM = sessionManager.getradius() / 1000;
            txtdist.setText("" + sessionManager.getradius() + " Metres (" + valueKM + " kms)");
        } else {
            txtdist.setText("" + sessionManager.getradius() + " Metres");
        }
        distance.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (value > 1000) {
                    int valueKM = value / 1000;
                    txtdist.setText("" + value + " Metres (" + valueKM + " kms)");
                } else {
                    txtdist.setText("" + value + " Metres");
                }
                distance.setProgress(value);
                radius = value;
                // Toast.makeText(getBaseContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        return rootview;
    }


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void init() {
        // seekBar = (SeekBar) rootview.findViewById(R.id.filterseekbar);
        distance = (DiscreteSeekBar) rootview.findViewById(R.id.discreteProgressbar);
        edselectcategory = (EditText) rootview.findViewById(R.id.edfiltercategory);
        txtapplyfilter = (TextView) rootview.findViewById(R.id.txtfilterapplyfilter);
        txtradius = (TextView) rootview.findViewById(R.id.txtfilterradius);
        // txtselectcategory = (TextView) rootview.findViewById(R.id.txtfilterselectcategory);
        btnapplyfilters = (Button) rootview.findViewById(R.id.btnfilterapply);
        txtdist = (TextView) rootview.findViewById(R.id.txtdistance);

        btnapplyfilters.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        //  txtselectcategory.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtradius.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtapplyfilter.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edselectcategory.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
//        distance.setMin((int) Validation.inital_radius);

    }

    //------------------------------getting list data
    public void getCategory() {
        categories = new ArrayList<>();
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
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

                CategoryType categoryType = response.body();
                categories.clear();
                CategoryPOJO signupPOJO1 = new CategoryPOJO();
                signupPOJO1.setId("0");
                signupPOJO1.setName("All");
                signupPOJO1.setCharacterpath("");
                categories.add(signupPOJO1);
                if (categoryType.getNeedtype().size() > 0) {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        CategoryPOJO signupPOJO = new CategoryPOJO();
                        signupPOJO.setId(categoryType.getNeedtype().get(i).getNeedMappingID().toString());
                        signupPOJO.setName(categoryType.getNeedtype().get(i).getNeedName().toString());
                        signupPOJO.setCharacterpath(categoryType.getNeedtype().get(i).getCharacterPath());
                        // signupPOJO.setPhotoPath(categoryType.getNeedtype().get(i).getIconPath());
                        categories.add(signupPOJO);
                    }
                }
                try {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.category_dialog);
                    EditText edsearch = (EditText) dialog.findViewById(R.id.search_from_list);
                    edsearch.setVisibility(View.GONE);
                    ListView categorylist = (ListView) dialog.findViewById(R.id.category_list);
                    Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                    categorylist.setAdapter(new CategoriesAdaptor(categories, getContext()));
                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            edselectcategory.setText(categories.get(i).getName());
                            // strNeed_Name = edselectcategory.getText().toString();

                            strNeedmapping_ID = categories.get(i).getId();

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

                } catch (Exception e) {
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });


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
    }
}
