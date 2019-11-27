package giftadeed.kshantechsoft.com.giftadeed.Resources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Group.GroupResponseStatus;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.Needdetails.SingleDeedMap;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
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

public class ResourceDetailsFrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    FragmentActivity myContext;
    LinearLayout resLocation, qtyLayout, subTypeLayout;
    View rootview;
    private AlertDialog alertDialog;
    TextView txtgroupname, txtaddress, txtresname, txtDate, txttypes, txtSubtypes, txt_qty_perperson;
    String str_ResCreatorId, strUser_ID, str_resid, callingFrom, str_geopoint, str_groupId, str_groupName, str_resName, str_address,
            str_resDesc, resAllGrpSelected, resAudienceGrpIds, resAudienceGrpNames;
    static FragmentManager fragmgr;
    SessionManager sessionManager;
    SimpleArcDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    StringBuffer sb, rescatId, rescat, ressubcatId, ressubcat;
    private String resCustomCatId = "", resCustomCat = "";

    public static ResourceDetailsFrag newInstance(int sectionNumber) {
        ResourceDetailsFrag fragment = new ResourceDetailsFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.resource_details_layout, container, false);
        sessionManager = new SessionManager(getActivity());
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
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.res_details));
        init();
        mGoogleApiClient = ((TaggedneedsActivity) getActivity()).mGoogleApiClient;
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        str_resid = this.getArguments().getString("str_resid");
        callingFrom = this.getArguments().getString("callingFrom");
        if (!(Validation.isNetworkAvailable(getActivity()))) {
            ToastPopUp.show(getActivity(), getString(R.string.network_validation));
        } else {
            getResource_Details();
        }

        resLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("str_tagid", str_resid);
                bundle.putString("str_geopoint", str_geopoint);
                bundle.putString("str_characterPath", "https://www.giftadeed.com/api/image/resource/resource_marker.png");
                bundle.putString("tab", "from_resource");
                SingleDeedMap fragInfo = new SingleDeedMap();
                fragInfo.setArguments(bundle);
                fragmgr.beginTransaction().replace(R.id.content_frame, fragInfo).commit();
            }
        });

        TaggedneedsActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callingFrom.equals("list")) {
                    // move to resource list
                    fragmgr = getFragmentManager();
                    fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).commit();
                } else {
                    // move to mapview
                    fragmgr = getFragmentManager();
                    fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                }
            }
        });
        return rootview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void getResource_Details() {
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
        ResourceDetailsInterface service = retrofit.create(ResourceDetailsInterface.class);
        Call<ResourcePOJO> call = service.fetchData(strUser_ID, str_resid);
        call.enqueue(new Callback<ResourcePOJO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<ResourcePOJO> response, Retrofit retrofit) {
                Log.d("response_resdetails", "" + response.body());
                try {
                    ResourcePOJO resourcePOJO = response.body();
                    int isblock = 0;
                    try {
                        isblock = resourcePOJO.getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        mDialog.dismiss();
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        mDialog.dismiss();
                        if (resourcePOJO.getStatus() == 1) {
                            for (int i = 0; i < resourcePOJO.getResDetailsPojos().size(); i++) {
                                str_groupId = resourcePOJO.getResDetailsPojos().get(0).getGroup_id();
                                str_groupName = resourcePOJO.getResDetailsPojos().get(0).getGroup_name();
                                txtgroupname.setText("" + str_groupName);
                                str_ResCreatorId = resourcePOJO.getResDetailsPojos().get(0).getCreatorId();
                                Log.d("res_creator", "" + str_ResCreatorId);
                                str_resName = resourcePOJO.getResDetailsPojos().get(0).getResName();
                                txtresname.setText("" + str_resName);
                                str_resDesc = resourcePOJO.getResDetailsPojos().get(0).getDescription();
                                if (resourcePOJO.getResDetailsPojos().get(0).getDescription().length() > 0) {
                                    qtyLayout.setVisibility(View.VISIBLE);
                                    txt_qty_perperson.setText(resourcePOJO.getResDetailsPojos().get(0).getDescription());
                                } else {
                                    qtyLayout.setVisibility(View.GONE);
                                }

                                resCustomCat = resourcePOJO.getResDetailsPojos().get(0).getResource_group_category_names();
                                resCustomCatId = resourcePOJO.getResDetailsPojos().get(0).getResource_group_categories();

                                sb = new StringBuffer("");
                                rescat = new StringBuffer("");
                                rescatId = new StringBuffer("");
                                ressubcat = new StringBuffer("");
                                ressubcatId = new StringBuffer("");
                                for (int j = 0; j < resourcePOJO.getResDetailsPojos().get(0).getSubTypes().size(); j++) {
                                    sb.append(resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getNeedname() + " : " +
                                            resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getSubTypeName() + "\n");
                                    rescatId.append(resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getNeedId() + ",");
                                    rescat.append(resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getNeedname() + ",");
                                    ressubcatId.append(resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getSubTypeId() + ",");
                                    ressubcat.append(resourcePOJO.getResDetailsPojos().get(0).getSubTypes().get(j).getSubTypeName() + ",");
                                }
                                if (resCustomCat.length() > 0) {
                                    sb.append(resCustomCat);
                                }
                                txtSubtypes.setText(sb);

                                str_geopoint = resourcePOJO.getResDetailsPojos().get(0).getGeopoint();
                                str_address = resourcePOJO.getResDetailsPojos().get(0).getAddress();
                                txtaddress.setText(resourcePOJO.getResDetailsPojos().get(0).getAddress());
                                resAllGrpSelected = resourcePOJO.getResDetailsPojos().get(0).getResource_audience_all_groups();
                                resAudienceGrpIds = resourcePOJO.getResDetailsPojos().get(0).getResource_audience_group_ids();
                                resAudienceGrpNames = resourcePOJO.getResDetailsPojos().get(0).getResource_audience_group_names();
                                txtDate.setText(resourcePOJO.getResDetailsPojos().get(0).getCreated_at());
                            }
                        } else if (resourcePOJO.getStatus() == 0) {
                            Toast.makeText(getContext(), resourcePOJO.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("response_resdetails", "" + e.getMessage());
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_resdetails", "" + t.getMessage());
                mDialog.dismiss();
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
            }
        });
    }

    //---------------------resource delete only for resource creator---------------------
    public void deleteResource() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ResourceDeleteInterface service = retrofit.create(ResourceDeleteInterface.class);
        Call<GroupResponseStatus> call = service.fetchData(strUser_ID, str_resid);
        call.enqueue(new Callback<GroupResponseStatus>() {
            @Override
            public void onResponse(Response<GroupResponseStatus> response, Retrofit retrofit) {
                Log.d("responsedelres", "" + response.body());
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
                        /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                    }
                                });*/

                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        if (groupResponseStatus.getStatus() == 1) {
                            Toast.makeText(getContext(), "Resource deleted successfully", Toast.LENGTH_SHORT).show();
                            if (callingFrom.equals("list")) {
                                // move to resource list
                                fragmgr = getFragmentManager();
                                fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).commit();
                            } else {
                                // move to mapview
                                fragmgr = getFragmentManager();
                                fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                            }
                        } else if (groupResponseStatus.getStatus() == 0) {
                            Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d("responsedelres", "" + e.getMessage());
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("responsedelres", "" + t.getMessage());
                ToastPopUp.show(myContext, getString(R.string.server_response_error));
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resource_details_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (strUser_ID.equals(str_ResCreatorId)) {
            //edit and delete resource options only visible to resource creator
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_edit_resource).setVisible(true);
            menu.findItem(R.id.action_delete_resource).setVisible(true);
        } else {
            getActivity().invalidateOptionsMenu();
            menu.findItem(R.id.action_edit_resource).setVisible(false);
            menu.findItem(R.id.action_delete_resource).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_resource:
                if (!(Validation.isNetworkAvailable(myContext))) {
                    ToastPopUp.show(myContext, getString(R.string.network_validation));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("str_rescat_id", rescatId.toString());
                    bundle.putString("str_resCustomCatId", resCustomCatId);
                    bundle.putString("str_rescat", rescat.toString() + resCustomCat);
                    bundle.putString("str_ressubcat_id", ressubcatId.toString());
                    bundle.putString("str_ressubcat", ressubcat.toString());
                    bundle.putString("str_resdesc", str_resDesc);
                    bundle.putString("str_geopoint", str_geopoint);
                    bundle.putString("str_address", str_address);
                    bundle.putString("str_grpid", str_groupId);
                    bundle.putString("str_grpname", str_groupName);
                    bundle.putString("str_rescreatorid", str_ResCreatorId);
                    bundle.putString("resAllGrp", resAllGrpSelected);
                    bundle.putString("resAudGrpIds", resAudienceGrpIds);
                    bundle.putString("resAudGrpNames", resAudienceGrpNames);
                    CreateResourceFragment createResourceFragment = new CreateResourceFragment();
                    createResourceFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    sessionManager.createResourceDetails("editRes", str_resid, str_resName);
                    fragmentTransaction.replace(R.id.content_frame, createResourceFragment);
                    fragmentTransaction.commit();
                }
                return true;

            case R.id.action_delete_resource:
                if (!(Validation.isNetworkAvailable(myContext))) {
                    ToastPopUp.show(myContext, getString(R.string.network_validation));
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View confirmDialog = li.inflate(R.layout.giftneeddialog, null);
                    Button dialogconfirm = (Button) confirmDialog.findViewById(R.id.btn_submit_mobileno);
                    Button dialogcancel = (Button) confirmDialog.findViewById(R.id.btn_Cancel_mobileno);
                    TextView dialogtext = (TextView) confirmDialog.findViewById(R.id.txtgiftneeddialog);
                    dialogtext.setText(getResources().getString(R.string.delete_resource_msg));
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
                            if (!(Validation.isNetworkAvailable(getActivity()))) {
                                ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                            } else {
                                deleteResource();
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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void init() {
        txtgroupname = (TextView) rootview.findViewById(R.id.tv_res_group);
        txtresname = (TextView) rootview.findViewById(R.id.tv_res_name);
        txt_qty_perperson = (TextView) rootview.findViewById(R.id.tv_qty_per_person);
        txttypes = (TextView) rootview.findViewById(R.id.tv_res_cat);
        resLocation = (LinearLayout) rootview.findViewById(R.id.res_details_location);
        subTypeLayout = (LinearLayout) rootview.findViewById(R.id.res_subtype_layout);
        qtyLayout = (LinearLayout) rootview.findViewById(R.id.res_qty_layout);
        txtSubtypes = (TextView) rootview.findViewById(R.id.tv_res_subcat);
        txtaddress = (TextView) rootview.findViewById(R.id.txt_res_address);
        txtDate = (TextView) rootview.findViewById(R.id.tv_res_created);
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
                    if (callingFrom.equals("list")) {
                        // move to resource list
                        fragmgr = getFragmentManager();
                        fragmgr.beginTransaction().replace(R.id.content_frame, ResourceListFragment.newInstance()).commit();
                    } else {
                        // move to mapview
                        fragmgr = getFragmentManager();
                        fragmgr.beginTransaction().replace(R.id.content_frame, TaggedneedsFrag.newInstance(1)).commit();
                    }
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
