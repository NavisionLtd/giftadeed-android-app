package giftadeed.kshantechsoft.com.giftadeed.Filter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupsInterface;
import giftadeed.kshantechsoft.com.giftadeed.Login.LoginActivity;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryAdapter;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryInterface;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.CategoryType;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.Needtype;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DBGAD;
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

import static giftadeed.kshantechsoft.com.giftadeed.TagaNeed.TagaNeed.setDynamicHeight;

////////////////////////////////////////////////////////////////////
//                                                               //
//     Used to filter the tags according to conditions          //
/////////////////////////////////////////////////////////////////
public class FilterFrag extends Fragment {
    public static ArrayList<String> selectedFilterUserGroupIds = new ArrayList<String>();
    public static ArrayList<String> selectedFilterUserGrpNames = new ArrayList<String>();
    private String formattedUserGroupIds = "", formattedUserGroupNames = ""; // for removing brackets [ ] and white spaces
    String checkedAllGroups = "N";
    FragmentActivity myContext;
    View rootview;
    private ArrayList<GroupPOJO> groupArrayList;
    SimpleArcDialog mDialog;
    private ArrayList<Needtype> categories;
    String strNeedmapping_ID, strNeed_Name, strUser_ID, callingFrom = "screenload";
    double radius;
    DiscreteSeekBar distance;
    EditText edselectcategory, edselectAudiance;
    TextView txtapplyfilter, txtradius, txtdist;
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
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.filters));
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUser_ID = user.get(sessionManager.USER_ID);
        mDialog = new SimpleArcDialog(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("tab");
        }
        init();
        getUserGroups(strUser_ID);
        radius = sessionManager.getradius();
        edselectcategory.setText(Validation.FILTER_CATEGORY);

        edselectcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory();
            }
        });

        edselectAudiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Validation.isOnline(getActivity()))) {
                    ToastPopUp.show(getActivity(), getString(R.string.network_validation));
                } else {
                    callingFrom = "group";
                    getUserGroups(strUser_ID);
                }
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

                formattedUserGroupIds = selectedFilterUserGroupIds.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                formattedUserGroupNames = selectedFilterUserGrpNames.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+", "");
                if (checkedAllGroups.equals("Y")) {
                    Validation.FILTER_GROUP_IDS = "All";
                } else {
                    Validation.FILTER_GROUP_IDS = formattedUserGroupIds.trim();
                    Validation.FILTER_GROUP_NAMES = formattedUserGroupNames.trim();
                }
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
        selectedFilterUserGroupIds = new ArrayList<String>();
        selectedFilterUserGrpNames = new ArrayList<String>();
        distance = (DiscreteSeekBar) rootview.findViewById(R.id.discreteProgressbar);
        edselectcategory = (EditText) rootview.findViewById(R.id.edfiltercategory);
        edselectAudiance = (EditText) rootview.findViewById(R.id.edfiltergroup);
        txtapplyfilter = (TextView) rootview.findViewById(R.id.txtfilterapplyfilter);
        txtradius = (TextView) rootview.findViewById(R.id.txtfilterradius);
        btnapplyfilters = (Button) rootview.findViewById(R.id.btnfilterapply);
        txtdist = (TextView) rootview.findViewById(R.id.txtdistance);

        btnapplyfilters.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtdist.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtradius.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        txtapplyfilter.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edselectcategory.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
        edselectAudiance.setTypeface(new FontDetails(getActivity()).fontStandardForPage);
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
        Call<CategoryType> call = service.sendData("");
        call.enqueue(new Callback<CategoryType>() {
            @Override
            public void onResponse(Response<CategoryType> response, Retrofit retrofit) {
                CategoryType categoryType = response.body();
                categories.clear();
                Needtype needtype = new Needtype();
                needtype.setNeedMappingID("0");
                needtype.setNeedName("All");
                needtype.setCharacterPath("");
                categories.add(needtype);
                try {
                    for (int i = 0; i < categoryType.getNeedtype().size(); i++) {
                        Needtype needtype1 = new Needtype();
                        needtype1.setNeedMappingID(categoryType.getNeedtype().get(i).getNeedMappingID().toString());
                        needtype1.setNeedName(categoryType.getNeedtype().get(i).getNeedName().toString());
                        needtype1.setType(categoryType.getNeedtype().get(i).getType());
                        needtype1.setCharacterPath(categoryType.getNeedtype().get(i).getCharacterPath());
                        needtype1.setIconPath(categoryType.getNeedtype().get(i).getIconPath());
                        categories.add(needtype1);
                    }
                } catch (Exception e) {

                }
                Log.d("filters_categories", "" + categories.size());
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
                    categorylist.setAdapter(new CategoryAdapter(categories, getContext()));
                    //set dynamic height for all listviews
                    setDynamicHeight(categorylist);
                    categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            edselectcategory.setText(categories.get(i).getNeedName());
                            strNeedmapping_ID = categories.get(i).getNeedMappingID();
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
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getActivity(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }

    //--------------------------getting user groups from server------------------------------------------
    public void getUserGroups(String user_id) {
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        groupArrayList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        GroupsInterface service = retrofit.create(GroupsInterface.class);
        Call<List<GroupPOJO>> call = service.sendData(user_id);
        call.enqueue(new Callback<List<GroupPOJO>>() {
            @Override
            public void onResponse(Response<List<GroupPOJO>> response, Retrofit retrofit) {
                mDialog.dismiss();
                Log.d("response_grouplist", "" + response.body());
                try {
                    List<GroupPOJO> res = response.body();
                    int isblock = 0;
                    try {
                        isblock = res.get(0).getIsBlocked();
                    } catch (Exception e) {
                        isblock = 0;
                    }
                    if (isblock == 1) {
                        FacebookSdk.sdkInitialize(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.block_toast), Toast.LENGTH_SHORT).show();
                        sessionManager.createUserCredentialSession(null, null, null);
                        LoginManager.getInstance().logOut();
                        int i = new DBGAD(getContext()).delete_row_message();
                        sessionManager.set_notification_status("ON");
                        Intent loginintent = new Intent(getActivity(), LoginActivity.class);
                        loginintent.putExtra("message", "Charity");
                        startActivity(loginintent);
                    } else {
                        List<GroupPOJO> groupPOJOS = response.body();
                        groupArrayList.clear();
                        try {
                            for (int i = 0; i < groupPOJOS.size(); i++) {
                                GroupPOJO groupPOJO = new GroupPOJO();
                                groupPOJO.setGroup_id(groupPOJOS.get(i).getGroup_id());
                                groupPOJO.setGroup_name(groupPOJOS.get(i).getGroup_name());
                                groupPOJO.setGroup_image(groupPOJOS.get(i).getGroup_image());
                                groupArrayList.add(groupPOJO);
                            }
                        } catch (Exception e) {
                            StringWriter writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            Bugreport bg = new Bugreport();
                            bg.sendbug(writer.toString());
                        }
                        if (callingFrom.equals("screenload")) {
                            if (groupArrayList.size() > 0) {
                                edselectAudiance.setVisibility(View.VISIBLE);
                                if (Validation.FILTER_GROUP_IDS.equals("All")) {
//                                    edselectAudiance.setText(Validation.FILTER_GROUP_IDS);
                                    edselectAudiance.setText("All");
                                } else {
                                    edselectAudiance.setText(Validation.FILTER_GROUP_NAMES);
                                    Log.d("selected_filter_grpids", "" + Validation.FILTER_GROUP_IDS);
                                }
                            } else {
                                edselectAudiance.setVisibility(View.GONE);
                            }
                        } else {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.user_orgs_dialog);
                            TextView txtHead = (TextView) dialog.findViewById(R.id.txt_head);
                            ListView userorglist = (ListView) dialog.findViewById(R.id.user_orgs_list);
                            final CheckBox chkAllGrp = (CheckBox) dialog.findViewById(R.id.chk_all_other_orgs);
                            final CheckBox chkAllIndi = (CheckBox) dialog.findViewById(R.id.chk_all_individuals);
                            chkAllIndi.setVisibility(View.GONE);
                            Button ok = (Button) dialog.findViewById(R.id.user_org_ok);
                            Button cancel = (Button) dialog.findViewById(R.id.user_org_cancel);
                            Log.d("groupArrayList_size", "" + groupArrayList.size());
                            if (groupArrayList.size() > 0) {
                                txtHead.setVisibility(View.VISIBLE);
                                userorglist.setVisibility(View.VISIBLE);
                                userorglist.setAdapter(new FilterUserGroupAdapter(groupArrayList, getContext()));
                            } else {
                                txtHead.setVisibility(View.GONE);
                                userorglist.setVisibility(View.GONE);
                            }

                            if (checkedAllGroups.equals("Y")) {
                                chkAllGrp.setChecked(true);
                            }

                            chkAllGrp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (chkAllGrp.isChecked()) {
                                        checkedAllGroups = "Y";
                                    } else {
                                        checkedAllGroups = "N";
                                    }
                                }
                            });

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                            Toast.makeText(getContext(), String.valueOf(selectedUserGroups), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    if (selectedFilterUserGrpNames.size() > 0) {
                                        if (selectedFilterUserGrpNames.get(0).length() > 15) {
                                            String txt = selectedFilterUserGrpNames.get(0).substring(0, 14) + "... ";
                                            int count = selectedFilterUserGrpNames.size();
                                            if (checkedAllGroups.equals("Y")) {
                                                count++;
                                            }
                                            if (count > 1) {
                                                edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                            } else {
                                                edselectAudiance.setText(txt);
                                            }
                                        } else {
                                            String txt = selectedFilterUserGrpNames.get(0);
                                            int count = selectedFilterUserGrpNames.size();
                                            if (checkedAllGroups.equals("Y")) {
                                                count++;
                                            }
                                            if (count > 1) {
                                                edselectAudiance.setText(txt + " + " + String.valueOf(count - 1) + " more");
                                            } else {
                                                edselectAudiance.setText(txt);
                                            }
                                        }
                                    } else {
                                        if (checkedAllGroups.equals("Y")) {
                                            edselectAudiance.setText(chkAllGrp.getText());
                                        } else {
                                            edselectAudiance.setText("");
                                            edselectAudiance.clearFocus();
                                        }
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
                        }
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.d("response_grouplist", "" + e.getMessage());
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Bugreport bg = new Bugreport();
                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
                Log.d("getorgs_error", "" + t.getMessage());
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
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
