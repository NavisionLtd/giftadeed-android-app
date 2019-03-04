package giftadeed.kshantechsoft.com.giftadeed.Mytags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsListFragment;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.DeedDetailsModel;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model.Taggedlist;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows list of tags done by logged in user                //
/////////////////////////////////////////////////////////////////
public class MyTagsList extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    RecyclerView recyclerView;
    RelativeLayout relativeNoResultFound;
    TextView txtmytags;
    View rootview;
    private RecyclerView.LayoutManager layoutManager;
    static android.support.v4.app.FragmentManager fragmgr;
    FragmentActivity myContext;
    SessionManager sessionManager;
    String strUSERID;
    SimpleArcDialog mDialog;
    public List<Taggedlist> lstMytags = new ArrayList<>();
    public AdapterMyTags adapterMytags;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_my_tags_list, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TaggedneedsActivity.updateTitle(getResources().getString(R.string.my_tags_heading));
        TaggedneedsActivity.fragname = MyTagsList.newInstance(0);
        //----------------------------------this code is used for taking user id from session preference
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUSERID = user.get(sessionManager.USER_ID);
        //-----------------------------------recycler view layout------------------
        mDialog = new SimpleArcDialog(getContext());
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_myTags);
        relativeNoResultFound = (RelativeLayout) rootview.findViewById(R.id.relNoResultMyTagsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        txtmytags = (TextView) rootview.findViewById(R.id.txtmytaglist);
        txtmytags.setText(getResources().getString(R.string.list_of_mytags));
        //---------------------------------------------------------------------------
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        fragmgr = getFragmentManager();
        // mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        } catch (Exception e) {

        }
        fetch_ListOfMyTags(strUSERID);//used to fetch the data via retrofit


        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 1;
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
                    return true;
                }
                return false;
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });
        return rootview;
    }
//----------------getting own tag list--------------------------------------------------------------
    public void fetch_ListOfMyTags(String strUSERID) {

        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyTagsInterface service = retrofit.create(MyTagsInterface.class);
        Call<ModelMytaglist> call = service.fetchData(strUSERID);
        call.enqueue(new Callback<ModelMytaglist>() {
            @Override
            public void onResponse(Response<ModelMytaglist> response, Retrofit retrofit) {
                try {

                    ModelMytaglist deedDetailsModel = response.body();
                    int isblock = 0;
                    try {
                        isblock = deedDetailsModel.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), "You have been blocked", Toast.LENGTH_SHORT).show();

                        LoginManager.getInstance().logOut();

                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/
                        try {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {

                                }
                            });
                        } catch (Exception e) {

                        }
                        sessionManager.createUserCredentialSession(null, null, null);
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");

                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        ModelMytaglist result = new ModelMytaglist();
                        result = response.body();
                        int size = response.body().getTaggedlist().size();
                        System.out.println("Size fullfiller ten" + size);
                        for (int i = 0; i < result.getTaggedlist().size(); i++) {
                            lstMytags.add(new Taggedlist(result.getTaggedlist().get(i).getTaggedPhotoPath(),
                                    result.getTaggedlist().get(i).getAddress(),
                                    result.getTaggedlist().get(i).getNeedName(),
                                    result.getTaggedlist().get(i).getCharacterPath(),
                                    result.getTaggedlist().get(i).getTaggedDatetime(),
                                    result.getTaggedlist().get(i).getTagStatus(),
                                    result.getTaggedlist().get(i).getViews(),
                                    result.getTaggedlist().get(i).getEndorse(),
                                    result.getTaggedlist().get(i).getPermanent()));

                    /*Log.d("photopath",result.getTaggedlist().get(i).getTaggedPhotoPath());
                    Log.d("charpath",result.getTaggedlist().get(i).getCharacterPath());
                    Log.d("date",result.getTaggedlist().get(i).getTaggedDatetime());*/
                            //
                        }
                        if (size == 0) {
                            recyclerView.setVisibility(View.GONE);
                            relativeNoResultFound.setVisibility(View.VISIBLE);
                            TextView txtnodatafound = (TextView) rootview.findViewById(R.id.txttoptenfullfillernoresult);
                            // txtnodatafound.setText(" ");

                            SpannableString ss = new SpannableString("Hey, looks like you haven't tagged any need yet Click here to get started");
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {
                                /*TagaNeed mainHomeFragment = new TagaNeed();

                                android.support.v4.app.FragmentTransaction fragmentTransaction =
                                        getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_main, mainHomeFragment);*/
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tab", "tab1");
                                    bundle.putString("page", "mytags");

                                    TagaNeed mainHomeFragment = new TagaNeed();
                                    mainHomeFragment.setArguments(bundle);
                                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                                            getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                                    fragmentTransaction.commit();
                                    // fragmentTransaction.commit();

                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setUnderlineText(false);
                                }
                            };
                            ss.setSpan(clickableSpan, 47, 58, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new UnderlineSpan(), 47, 58, 0);
                            txtnodatafound.setText(ss);
                            txtnodatafound.setMovementMethod(LinkMovementMethod.getInstance());
                            txtnodatafound.setHighlightColor(getResources().getColor(R.color.blue));


                        } else {
                            relativeNoResultFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapterMytags = new AdapterMyTags(getActivity(), lstMytags);
                            recyclerView.setAdapter(adapterMytags);

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
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });


    }

    public static MyTagsList newInstance(int sectionNumber) {
        MyTagsList fragment = new MyTagsList();
                /*Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
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
                    MenuGrid menuGrid = new MenuGrid();
                    fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
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
