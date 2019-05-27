package giftadeed.kshantechsoft.com.giftadeed.Group;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.leo.simplearcloader.SimpleArcDialog;

import java.util.HashMap;
import java.util.Locale;

import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CollabPendingInvitestFragment;
import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CreateCollabFragment;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.groupchannel.GroupChannelListFragment;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.SessionManager;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;

import static giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity.userClubCount;

public class GroupCollabFrag extends Fragment {
    private TabLayout tablayout;
    private ViewPager viewpgr;
    View rootview;
    FragmentActivity myContext;
    String value = "tab1";
    int tab_no;
    String selected_tab;
    SessionManager sessionManager;
    String strUserId;
    SimpleArcDialog mDialog;
    static android.support.v4.app.FragmentManager fragmgr;
    String selectedLangugae;
    Locale locale;
    Configuration config;

    public static GroupCollabFrag newInstance(int sectionNumber) {
        GroupCollabFrag fragment = new GroupCollabFrag();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.map_tagged_deeds));
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.imgShare.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.back.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = GroupCollabFrag.newInstance(0);
        TaggedneedsActivity.imgHamburger.setVisibility(View.GONE);
        mDialog = new SimpleArcDialog(getContext());
        rootview = inflater.inflate(R.layout.fragment_groupscollab, container, false);
        fragmgr = getFragmentManager();
        sessionManager = new SessionManager(getActivity());
        selectedLangugae = sessionManager.getLanguage();
        HashMap<String, String> user = sessionManager.getUserDetails();
        strUserId = user.get(sessionManager.USER_ID);
        viewpgr = (ViewPager) rootview.findViewById(R.id.group_pager);
        tablayout = (TabLayout) rootview.findViewById(R.id.group_tabLayout);
        TaggedneedsActivity.fragname = GroupCollabFrag.newInstance(0);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            value = bundle.getString("tab");
        }

        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.tab_group)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.tab_colab)));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setupWithViewPager(viewpgr);

        viewpgr.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_no = tab.getPosition();
                if (tab_no == 0) {
                    selected_tab = "tab1";
                } else {
                    selected_tab = "tab2";
                }
                if (!(Validation.isNetworkAvailable(myContext))) {
                    Toast.makeText(myContext, getResources().getString(R.string.network_validation), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpgr.setAdapter(new GroupsPager(getChildFragmentManager(), tablayout.getTabCount()));
        if (value.equals("tab2")) {
            viewpgr.setCurrentItem(1);
        } else {
            viewpgr.setCurrentItem(0);
        }

        rootview.getRootView().setFocusableInTouchMode(true);
        rootview.getRootView().requestFocus();
        rootview.getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();
                    return true;
                } else {
                    return false;
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateLanguage(String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        TaggedneedsActivity.updateTitle(getResources().getString(R.string.map_tagged_deeds));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.groups_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_group:
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                sessionManager.createGroupDetails("create", "", "", "", "");
                fragmgr.beginTransaction().replace(R.id.content_frame, createGroupFragment).commit();
                return true;
            case R.id.action_create_collab:
                CreateCollabFragment createCollabFragment = new CreateCollabFragment();
                sessionManager.createColabDetails("create", "", "", "", "", "");
                fragmgr.beginTransaction().replace(R.id.content_frame, createCollabFragment).commit();
                return true;
            case R.id.action_collab_invitations:
                CollabPendingInvitestFragment collabPendingInvitestFragment = new CollabPendingInvitestFragment();
                fragmgr.beginTransaction().replace(R.id.content_frame, collabPendingInvitestFragment).commit();
                return true;
            case R.id.action_group_messages:
                if (userClubCount != null) {
                    if (userClubCount.equals("Yes")) {
                        // Load list of Group Channels
                        Fragment fragment = GroupChannelListFragment.newInstance();
                        fragmgr.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                    } else if (userClubCount.equals("No")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_channel), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
