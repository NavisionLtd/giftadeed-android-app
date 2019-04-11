package giftadeed.kshantechsoft.com.giftadeed.taggerfullfiller;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.GridMenu.MenuGrid;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
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
////////////////////////////////////////////////////////////////////
//                                                               //
//     Shows list of top 10 tag fulfillers in your city         //
/////////////////////////////////////////////////////////////////
public class TopTenFullfillerList extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    RecyclerView recyclerView;
    RelativeLayout relativeNoResultFound;
    View rootview;
    private RecyclerView.LayoutManager layoutManager;
    static android.support.v4.app.FragmentManager fragmgr;
    FragmentActivity myContext;
    AdapterToptenFullfiller adaptertoptenfullfiller;
    List<RESULTFFILLER> lstTopTenFullFiller = new ArrayList<RESULTFFILLER>();
    SessionManager sessionManager;
    String strUSERID;
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_top_ten_fullfiller_list, container, false);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.top_ten_fulfill_heading));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //----------------------------------this code is used for taking user id from session preference
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(false);
        TaggedneedsActivity.back.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        fragmgr = getFragmentManager();
        TaggedneedsActivity.fragname= TopTenFullfillerList.newInstance(0);
        mDialog = new SimpleArcDialog(getContext());
        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUSERID = user.get(sessionManager.USER_ID);
       // mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        }catch(Exception e){

        }
        //-----------------------------------recycler view layout------------------
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_fullfiller_toptenList);
        relativeNoResultFound = (RelativeLayout) rootview.findViewById(R.id.reltoptenfullfillernoResultFound);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //---------------------------------------------------------------------------
        /*TaggedneedsActivity.imgappbarcamera.setVisibility(View.VISIBLE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.VISIBLE);
*/
        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuGrid menuGrid = new MenuGrid();
                fragmgr.beginTransaction().replace(R.id.content_frame, menuGrid).commit();
            }
        });

        if (!(Validation.isOnline(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            fetch_ToptenFillfiller(strUSERID);//used to fetch the data via retrofit
        }


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


        return rootview;
    }


    public static TopTenFullfillerList newInstance(int sectionNumber) {
        TopTenFullfillerList fragment = new TopTenFullfillerList();
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

    public void fetch_ToptenFillfiller(String strUSERID) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TopTenFullfiller service = retrofit.create(TopTenFullfiller.class);
        Call<ModaltoptenFullfill> call = service.getTopTenFullfillerList(strUSERID);
        call.enqueue(new Callback<ModaltoptenFullfill>() {
            @Override
            public void onResponse(Response<ModaltoptenFullfill> response, Retrofit retrofit) {
                try {
                    ModaltoptenFullfill deedDetailsModel = response.body();
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
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");

                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {


                    ModaltoptenFullfill result = new ModaltoptenFullfill();
                    result = response.body();
                    int size = response.body().getRESULTFFILLER().size();
                    System.out.println("Size fullfiller ten" + size);
                    for (int i = 0; i < size; i++) {
                        lstTopTenFullFiller.add(new RESULTFFILLER(result.getRESULTFFILLER().get(i).getFirstName(), result.getRESULTFFILLER().get(i).getLastName(), result.getRESULTFFILLER().get(i).getTotalFullfillerPoints(), result.getRESULTFFILLER().get(i).getFullFillerRank(), result.getRESULTFFILLER().get(i).getUrlFullfillerRank()));
                        //     adaptertoptenfullfiller = new AdapterToptenFullfiller(getActivity(), lstTopTenFullFiller);

                    }
                    if (size == 0) {
                        recyclerView.setVisibility(View.GONE);
                        relativeNoResultFound.setVisibility(View.VISIBLE);
                    } else {
                        relativeNoResultFound.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adaptertoptenfullfiller = new AdapterToptenFullfiller(getActivity(), lstTopTenFullFiller);
                        recyclerView.setAdapter(adaptertoptenfullfiller);

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
