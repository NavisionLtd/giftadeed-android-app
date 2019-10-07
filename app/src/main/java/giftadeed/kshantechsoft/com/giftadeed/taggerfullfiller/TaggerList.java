package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
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


////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows list of top 10 taggers in your city                //
/////////////////////////////////////////////////////////////////
public class TaggerList extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private Toolbar toolbar;
    private Context context;
    RecyclerView recyclerView;
    View rootview;
    private RecyclerView.LayoutManager layoutManager;
    static FragmentManager fragmgr;
    FragmentActivity myContext;
    Adapter_TopTenTagger adaptertoptentaggers;
    List<RESULT> lsttoptentagger = new ArrayList<RESULT>();
    SessionManager sessionManager;
    String strUSERID;
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    RelativeLayout relativeNoREcordsFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_tagger_list, container, false);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.top_ten_heading));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //----------------------------------this code is used for taking user id from session preference
        mDialog = new SimpleArcDialog(getContext());
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = TaggerList.newInstance(0);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        fragmgr = getFragmentManager();
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUSERID = user.get(sessionManager.USER_ID);
        //mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        }catch (Exception e){

        }
        //-----------------------------------recycler view layout------------------
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_tagger_List_Info);
        relativeNoREcordsFound = (RelativeLayout) rootview.findViewById(R.id.toptentaggernoResultFound);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //---------------------------------------------------------------------------
       /* TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);*/
        // TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //------------------------------method for getting the list of data -----------
//        Log.d("userid", strUSERID);
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            fetch_TopTenist(strUSERID);
        }
        //-------------------------tools-----------------------------------------
        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int i = 1;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    public static TaggerList newInstance(int sectionNumber) {
        TaggerList fragment = new TaggerList();
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

    public void fetch_TopTenist(String strUserId) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TaggeddNeedIntface service = retrofit.create(TaggeddNeedIntface.class);
        Call<ModalTaggedNeed> call = service.getTopTenTaggersList(strUserId);
        call.enqueue(new Callback<ModalTaggedNeed>() {
            @Override
            public void onResponse(Response<ModalTaggedNeed> response, Retrofit retrofit) {
                try {

                    ModalTaggedNeed deedDetailsModel = response.body();
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

                        ModalTaggedNeed result = new ModalTaggedNeed();
                        result = response.body();
                        int size = response.body().getRESULT().size();
                        for (int i = 0; i < size; i++) {
                            lsttoptentagger.add(new RESULT(result.getRESULT().get(i).getFirstName(), result.getRESULT().get(i).getLastName(), result.getRESULT().get(i).getTotalCreditPoint()));


                        }
                        if (size == 0) {
                            recyclerView.setVisibility(View.GONE);
                            relativeNoREcordsFound.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            relativeNoREcordsFound.setVisibility(View.GONE);
                            adaptertoptentaggers = new Adapter_TopTenTagger(getActivity(), lsttoptentagger);
                            recyclerView.setAdapter(adaptertoptentaggers);
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
